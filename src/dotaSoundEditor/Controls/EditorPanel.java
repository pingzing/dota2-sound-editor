/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dotaSoundEditor.Controls;

import dotaSoundEditor.Helpers.SoundPlayer;
import dotaSoundEditor.Helpers.ScriptParser;
import dotaSoundEditor.Helpers.Utility;
import dotaSoundEditor.*;
import dotaSoundEditor.Helpers.CacheManager;
import info.ata4.vpk.VPKArchive;
import info.ata4.vpk.VPKEntry;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.apache.commons.io.FileUtils;

public abstract class EditorPanel extends JPanel
{

    protected TreeModel currentTreeModel;
    protected SoundPlayer currentSound = SoundPlayer.getInstance();
    protected JTree currentTree;
    protected JComboBox currentDropdown;
    protected String vpkPath;
    protected String installDir;    

    abstract void populateSoundListAsTree();

    abstract void fillImageFrame(Object selectedItem) throws IOException;

    abstract void revertButtonActionPerformed(java.awt.event.ActionEvent evt);

    abstract void playSoundButtonActionPerformed(java.awt.event.ActionEvent evt);

    abstract void revertAllButtonActionPerformed(java.awt.event.ActionEvent evt);

    abstract void replaceButtonActionPerformed(java.awt.event.ActionEvent evt);

    abstract void advancedButtonActionPerformed(java.awt.event.ActionEvent evt, JButton advancedButton);

    abstract void populateDropdownBox();

    abstract String getCurrentScriptString();

    abstract String getCustomSoundPathString();    

    abstract void updateCache(String scriptString, long internalCrc);

    protected final File promptUserForNewFile(String wavePath)
    {
        JFileChooser chooser = new JFileChooser(new File(UserPrefs.getInstance().getWorkingDirectory()));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("MP3s and WAVs", "mp3", "wav");
        chooser.setAcceptAllFileFilterUsed((false));
        chooser.setFileFilter(filter);
        chooser.setMultiSelectionEnabled(false);

        int chooserRetVal = chooser.showOpenDialog(chooser);
        if (chooserRetVal == JFileChooser.APPROVE_OPTION)
        {
            DefaultMutableTreeNode selectedFile = (DefaultMutableTreeNode) getTreeNodeFromWavePath(wavePath);
            Path chosenFile = Paths.get(chooser.getSelectedFile().getAbsolutePath());
            Path destPath = Paths.get(installDir + "\\dota\\sound\\" + getCustomSoundPathString() + chosenFile.getFileName());
            UserPrefs.getInstance().setWorkingDirectory(chosenFile.getParent().toString());

            try
            {
                boolean success = new File(destPath.toString()).mkdirs();
                Files.copy(chosenFile, destPath, StandardCopyOption.REPLACE_EXISTING);

                String waveString = selectedFile.getUserObject().toString();
                int startIndex = -1;
                int endIndex = -1;
                if (waveString.contains("\"wave\""))
                {
                    startIndex = Utility.nthOccurrence(selectedFile.getUserObject().toString(), '\"', 2);
                    endIndex = Utility.nthOccurrence(selectedFile.getUserObject().toString(), '\"', 3);
                }
                else    //Some wavestrings don't have the "wave" at the beginning for some reason
                {
                    startIndex = Utility.nthOccurrence(selectedFile.getUserObject().toString(), '\"', 0);
                    endIndex = Utility.nthOccurrence(selectedFile.getUserObject().toString(), '\"', 1);
                }

                String waveSubstring = waveString.substring(startIndex, endIndex + 1);
                waveString = waveString.replace(waveSubstring, "\"" + getCustomSoundPathString() + chosenFile.getFileName() + "\"");
                selectedFile.setUserObject(waveString);

                //Write out modified tree to scriptfile.
                ScriptParser parser = new ScriptParser(this.currentTreeModel);
                String scriptString = getCurrentScriptString();
                Path scriptPath = Paths.get(scriptString);
                parser.writeModelToFile(scriptPath.toString());

                //Update UI
                populateSoundListAsTree();
                JOptionPane.showMessageDialog(this, "Sound file successfully replaced.");

            }
            catch (IOException ex)
            {
                System.err.println(ex);
            }
        }
        return null;
    }

    protected void attachDoubleClickListenerToTree()
    {
        MouseListener ml = new MouseAdapter()
        {
            public void mousePressed(MouseEvent e)
            {
                int selRow = currentTree.getRowForLocation(e.getX(), e.getY());
                TreePath selPath = currentTree.getPathForLocation(e.getX(), e.getY());
                if (selRow != -1 && ((DefaultMutableTreeNode) selPath.getLastPathComponent()).isLeaf())
                {
                    if (e.getClickCount() == 2)
                    {
                        playSelectedTreeSound(selPath);
                    }
                }
            }
        };
        currentTree.addMouseListener(ml);
    }

    protected void playSelectedTreeSound(TreePath selPath)
    {        
        try
        {
            DefaultMutableTreeNode selectedFile = ((DefaultMutableTreeNode) selPath.getLastPathComponent());
            String waveString = selectedFile.getUserObject().toString();
            File soundFile = createSoundFileFromWaveString(waveString);
            currentSound.loadSound(soundFile.getAbsolutePath());
            currentSound.playSound();                        
        }
        catch (Exception ex)
        {
            JOptionPane.showMessageDialog(null, "The selected node does not represent a valid sound file.", "Error", JOptionPane.ERROR_MESSAGE);                        
        }
    }

    protected ArrayList<String> getWavePathsAsList(TreeNode selectedFile)
    {
        ArrayList<String> wavePathsList = new ArrayList<>();
        Enumeration e = selectedFile.children();
        while (e.hasMoreElements())
        {
            Object currentElement = e.nextElement();

            //If a soundfile has multiple possible wavefiles
            if (currentElement.toString().contains("\"rndwave\""))
            {
                Enumeration innerE = ((TreeNode) currentElement).children();
                while (innerE.hasMoreElements())
                {
                    Object currentInnerElement = innerE.nextElement();
                    if (currentInnerElement.toString().contains("\"wave\"")
                            || currentInnerElement.toString().contains(".wav")
                            || currentInnerElement.toString().contains(".mp3")
                            && !currentInnerElement.toString().trim().startsWith("//"))
                    {
                        //Maybe do some string massaging here before we just hand it back
                        wavePathsList.add(((TreeNode) currentInnerElement).toString());
                    }
                }
            }
            //If it only has one
            else if (currentElement.toString().contains("\"wave\""))
            {
                if (!currentElement.toString().trim().startsWith("//"))
                {
                    wavePathsList.add(((TreeNode) currentElement).toString());
                }
            }
        }
        return wavePathsList;
    }

    private File createSoundFileFromWaveString(String waveString)
    {
        if (!(waveString.contains(".wav") || (waveString.contains(".mp3"))))
        {
            return null;
        }

        File file = new File(vpkPath);
        VPKArchive vpk = new VPKArchive();
        File entryFile = new File("");

        String waveSubstring = "";
        int startIndex = -1;
        int endIndex = -1;
        if (waveString.contains("\"wave\""))
        {
            startIndex = Utility.nthOccurrence(waveString, '\"', 2);
            endIndex = Utility.nthOccurrence(waveString, '\"', 3);
        }
        //For weird special cases where the "wave" part of the string is missing, i.e. Treant's Overgrowth.Target spell
        else
        {
            startIndex = Utility.nthOccurrence(waveString, '\"', 0);
            endIndex = Utility.nthOccurrence(waveString, '\"', 1);
        }

        waveSubstring = waveString.substring(startIndex, endIndex + 1);
        waveSubstring = waveSubstring.replace(")", "");
        waveSubstring = waveSubstring.replace("\"", "");
        waveSubstring = waveSubstring.replace("\\", "/");
        waveSubstring = waveSubstring.replace("#", "");
        waveSubstring = waveSubstring.replace("*", "");

        if (!waveString.contains("custom"))
        {
            File localFile = new File(Paths.get(installDir + "\\sound\\" + waveSubstring).toString());
            if (localFile.isFile())
            {
                return localFile;
            }

            try
            {
                vpk.load(file);
            }
            catch (Exception ex)
            {
                System.err.println("Can't open archive: " + ex.getMessage());
            }
            waveSubstring = "sound/" + waveSubstring;
            VPKEntry entry = vpk.getEntry(waveSubstring.toLowerCase());           

            entryFile = entry.getType().contains("wav")
                    ? new File(Paths.get(System.getProperty("user.dir") + "\\scratch\\scratch.wav").toString())
                    : new File(Paths.get(System.getProperty("user.dir") + "\\scratch\\scratch.mp3").toString());

            try (FileChannel fc = FileUtils.openOutputStream(entryFile).getChannel())
            {
                fc.write(entry.getData());
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }

            return entryFile;
        }
        else    //If it's NOT stored in the VPK, it's on the local filesys
        {
            entryFile = new File(Paths.get(installDir + "\\dota\\sound\\" + waveSubstring).toString());
            return entryFile;
        }
    }

    protected TreeNode getTreeNodeFromWavePath(String wavePath)
    {
        TreeModel model = this.currentTreeModel;

        TreeNode root = (TreeNode) model.getRoot();
        for (Enumeration e = ((DefaultMutableTreeNode) root).breadthFirstEnumeration(); e.hasMoreElements() && root != null;)
        {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
            if (node.toString().contains(wavePath))
            {
                return node;
            }
        }
        return null;
    }

    protected void deleteSoundFileByWaveString(String selectedWaveString)
    {
        int startIndex = -1;
        int endIndex = -1;
        if (selectedWaveString.contains("\"wave\""))
        {
            startIndex = Utility.nthOccurrence(selectedWaveString, '\"', 2);
            endIndex = Utility.nthOccurrence(selectedWaveString, '\"', 3);
        }
        else
        {
            startIndex = Utility.nthOccurrence(selectedWaveString, '\"', 1);
            endIndex = Utility.nthOccurrence(selectedWaveString, '\"', 2);
        }

        String waveSubstring = selectedWaveString.substring(startIndex, endIndex + 1);
        waveSubstring = waveSubstring.replace(")", "");
        waveSubstring = waveSubstring.replace("\"", "");
        File soundFileToDelete = new File(Paths.get(installDir + "\\dota\\sound\\" + waveSubstring).toString());
        if (soundFileToDelete.isFile())
        {
            soundFileToDelete.delete();
        }
        else
        {
            System.err.println("Cannot find and delete custom sound file " + waveSubstring);
        }
    }

    protected ArrayList<String> getWavePathListFromString(String scriptString)
    {
        ArrayList<String> wavePathsList = new ArrayList<>();
        BufferedReader buf = new BufferedReader(new StringReader(scriptString));
        String line = null;
        try
        {
            while ((line = buf.readLine()) != null)
            {
                //Stop reading after we're done with the wave paths               
                if (line.contains("soundentry"))
                {
                    break;
                }

                if (line.contains("\"wave\"") || line.contains(".wav") || line.contains(".mp3"))
                {
                    wavePathsList.add(line);
                }
            }
            return wavePathsList;
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    public boolean validateScriptFile(String scriptKey, String scriptPath)
    {
        CacheManager cm = CacheManager.getInstance();        
        cm.putScriptPath(scriptKey, scriptPath);
        long crc = cm.getSessionCrc(scriptKey);
        if(crc == 0)
        {
            return false;
        }
        return validateScriptFile(scriptKey, crc);
    }
    
    public boolean validateScriptFile(String scriptKey, long internalCrc)
    {
        CacheManager cm = CacheManager.getInstance();
        if (!cm.isUpToDate(scriptKey, internalCrc))
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    
    /**
     * 
     * @param oldTree The tree that was previously in use.
     * @param scriptPath The full filepath to the script.
     * @return The merged tree.
     */
    TreeModel mergeNewChanges(TreeModel oldTree, String scriptPath)
    {
        return mergeNewChanges(oldTree, new File(scriptPath));
    }
    
    /**
     * @param oldTree The tree that was previously in use
     * @param scriptPath The full filepath to the script. 
     *@return The merged tree.
     */
    TreeModel mergeNewChanges(TreeModel oldTree, Path scriptPath)
    {
        return mergeNewChanges(oldTree, scriptPath.toFile());
    }
         
    /**
     * @param oldTree The tree that was previously in use
     * @param scriptFilePath A File object pointing to or containing a script.
     *@return The merged tree.
     */
    TreeModel mergeNewChanges(TreeModel oldTree, File scriptFilePath)
    {
        //Look for any modified wavestrings. Save their nodes, and note their indices.
        //Parse in updated script tree, replace nodes at indices with saved nodes.   
        //Return merged tree
        System.out.println("Running a merge operation!");

        TreeNode oldRoot = (TreeNode) oldTree.getRoot();
        ArrayList<DefaultMutableTreeNode> savedNodeList = new ArrayList<>();
        for (Enumeration e = ((DefaultMutableTreeNode) oldRoot).depthFirstEnumeration(); e.hasMoreElements() && oldRoot != null;)
        {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
            if (node.getUserObject().toString().contains("custom"))
            {
                savedNodeList.add(node);
            }
        }
        ScriptParser parser = new ScriptParser(scriptFilePath);
        TreeModel newTree = parser.getTreeModel();
        TreeNode newRoot = (TreeNode) newTree.getRoot();
        for (DefaultMutableTreeNode savedNode : savedNodeList)
        {
            int rndwaveIndex = -1;
            int childIndex;
            int parentIndex;
            DefaultMutableTreeNode parent = (DefaultMutableTreeNode) savedNode.getParent();
            if (parent.getUserObject().toString().contains("rndwave"))
            {
                rndwaveIndex = parent.getParent().getIndex(parent);
                parent = (DefaultMutableTreeNode) parent.getParent();
            }
            parentIndex = parent.getParent().getIndex(parent);
            childIndex = parent.getIndex(savedNode);

            TreeNode newParentNode = newRoot.getChildAt(parentIndex);
            TreeNode newChildNode;
            if (rndwaveIndex != -1)
            {
                newChildNode = newParentNode.getChildAt(rndwaveIndex);
                newChildNode = newChildNode.getChildAt(childIndex);
            }
            else
            {
                newChildNode = newParentNode.getChildAt(childIndex);
            }
            newChildNode = savedNode;
        }
        return newTree;
    }
}
