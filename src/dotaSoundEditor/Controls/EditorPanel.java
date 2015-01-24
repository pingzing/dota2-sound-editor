/* 
 * The MIT License
 *
 * Copyright 2015 Neil McAlister.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package dotaSoundEditor.Controls;

import dotaSoundEditor.Helpers.SoundPlayer;
import dotaSoundEditor.Helpers.ScriptParser;
import dotaSoundEditor.Helpers.Utility;
import dotaSoundEditor.*;
import dotaSoundEditor.Helpers.CacheManager;
import info.ata4.vpk.VPKArchive;
import info.ata4.vpk.VPKEntry;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Scanner;
import javax.sound.sampled.AudioInputStream;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.*;
import org.apache.commons.io.FileUtils;

public abstract class EditorPanel extends JPanel
{
    protected TreeModel currentTreeModel;
    protected SoundPlayer soundPlayer;
    protected JTree currentTree;
    protected JComboBox currentDropdown;
    protected String vpkPath;
    protected String installDir;
    protected CacheManager cacheManager;    
    //TODO: Create some ActionListeners that pay attention to this value and react to it changing
    boolean inAdvancedMode = false;

    abstract void populateSoundList();

    abstract void fillImageFrame(Object selectedItem) throws IOException;          

    abstract void populateDropdownBox();

    abstract String getCurrentScriptString();

    abstract String getCustomSoundPathString();

    abstract void updateCache(String scriptString, long internalCrc);

    protected File promptUserForNewFile(String wavePath)
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
            //Strip caps and spaces out of filenames. The item sound parser seems to have trouble with them.
            String destFileName = chosenFile.getFileName().toString().toLowerCase().replace(" ", "_");
            Path destPath = Paths.get(installDir, "/dota/sound/" + getCustomSoundPathString() + destFileName);
            UserPrefs.getInstance().setWorkingDirectory(chosenFile.getParent().toString());

            try
            {
                new File(destPath.toString()).mkdirs();
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

                String waveStringFilePath = waveString.substring(startIndex, endIndex + 1);
                waveString = waveString.replace(waveStringFilePath, "\"" + getCustomSoundPathString() + destFileName + "\"");
                selectedFile.setUserObject(waveString);

                //Write out modified tree to scriptfile.
                ScriptParser parser = new ScriptParser(this.currentTreeModel);
                String scriptString = getCurrentScriptString();
                Path scriptPath = Paths.get(scriptString);
                parser.writeModelToFile(scriptPath.toString());

                //Update UI
                ((DefaultMutableTreeNode) currentTree.getLastSelectedPathComponent()).setUserObject(waveString);
                ((DefaultTreeModel) currentTree.getModel()).nodeChanged((DefaultMutableTreeNode) currentTree.getLastSelectedPathComponent());
                JOptionPane.showMessageDialog(this, "Sound file successfully replaced.");

            }
            catch (IOException ex)
            {
                JOptionPane.showMessageDialog(null, "Unable to replace sound.\nDetails: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return null;
    }

    //Overridden in VoicePanel, due to using a different VPK.
    protected void attachDoubleClickListenerToTree()
    {
        MouseListener ml = new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                int selRow = currentTree.getRowForLocation(e.getX(), e.getY());
                TreePath selPath = currentTree.getPathForLocation(e.getX(), e.getY());
                if (selRow != -1 && ((DefaultMutableTreeNode) selPath.getLastPathComponent()).isLeaf())
                {
                    if (e.getClickCount() == 2)
                    {
                        playSelectedTreeSound(selPath, Paths.get(vpkPath));
                    }
                }
            }
        };
        currentTree.addMouseListener(ml);
    }

    protected void playSelectedTreeSound(TreePath selPath, Path vpkToPlayFrom)
    {
        try
        {
            DefaultMutableTreeNode selectedFile = ((DefaultMutableTreeNode) selPath.getLastPathComponent());
            String waveString = selectedFile.getUserObject().toString();
            File soundFile = createSoundFileFromWaveString(waveString, vpkToPlayFrom);
            soundPlayer.loadSound(soundFile.getAbsolutePath());
            soundPlayer.playSound();
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

    private File createSoundFileFromWaveString(String waveString, Path vpkToPlayFrom)
    {
        if (!(waveString.contains(".wav") || (waveString.contains(".mp3"))))
        {
            return null;
        }

        File file = new File(vpkToPlayFrom.toString());
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

        if (!waveString.contains("custom") && !waveString.contains("//Replaced"))
        {
            File localFile = new File(Paths.get(installDir + "/sound/" + waveSubstring).toString());
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
                    ? new File(Paths.get(System.getProperty("user.dir") + "/scratch/scratch.wav").toString())
                    : new File(Paths.get(System.getProperty("user.dir") + "/scratch/scratch.mp3").toString());

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
            entryFile = new File(Paths.get(installDir, "/dota/sound/" + waveSubstring).toString());
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
        waveSubstring = waveSubstring.replace("*", "");
        File soundFileToDelete = new File(Paths.get(installDir, "/dota/sound/" + waveSubstring).toString());
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
        if(cacheManager == null)
        {
            cacheManager = new CacheManager();
        }
        cacheManager.putScriptPath(scriptKey, scriptPath);
        long crc = cacheManager.getSessionCrc(scriptKey);
        if (crc == 0)
        {
            return false;
        }
        return validateScriptFile(scriptKey, crc);
    }

    public boolean validateScriptFile(String scriptKey, long internalCrc)
    {
        if(cacheManager == null)
        {
            cacheManager = new CacheManager();
        }
        if (!cacheManager.isUpToDate(scriptKey, internalCrc))
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
     * @return The merged tree.
     */
    TreeModel mergeNewChanges(TreeModel oldTree, Path scriptPath)
    {
        return mergeNewChanges(oldTree, scriptPath.toFile());
    }

    /**
     * @param oldTree The tree that was previously in use
     * @param scriptFilePath A File object pointing to or containing a script.
     * @return The merged tree.
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
            if (node.getUserObject().toString().contains("custom\\") || node.getUserObject().toString().contains("custom/"))
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
            int childIndex = -1;
            int parentIndex = -1;
            DefaultMutableTreeNode parent = (DefaultMutableTreeNode) savedNode.getParent();
            if (parent.getUserObject().toString().contains("rndwave"))
            {
                rndwaveIndex = parent.getParent().getIndex(parent);
                childIndex = parent.getIndex(savedNode);
                parent = (DefaultMutableTreeNode) parent.getParent();
                parentIndex = parent.getParent().getIndex(parent);                
            }
            else
            {
                parentIndex = parent.getParent().getIndex(parent);
                childIndex = parent.getIndex(savedNode);
            }

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

    void advancedButtonActionPerformed(java.awt.event.ActionEvent evt, JButton advancedButton)
    {
        if (!getAdvancedMode())
        {
            setAdvancedMode(true);
            String scriptPath = getCurrentScriptString();
            ScriptParser parser = new ScriptParser(new File(Paths.get(scriptPath).toString()));
            TreeModel model = parser.getTreeModel();
            currentTree.setModel(model);
            currentTree.setEditable(true);
            for (int i = 0; i < currentTree.getRowCount(); i++)
            {
                currentTree.expandRow(i);
            }

            //Change button and action to Basic-revert:
            advancedButton.setText("Basic <<");
            advancedButton.setMnemonic('a');
        }
        else if (getAdvancedMode())
        {
            setAdvancedMode(false);
            this.populateSoundList();
            advancedButton.setText("Advanced >>");
            advancedButton.setMnemonic('a');
            currentTree.setEditable(false);
        }
    }

    /**
     * @return Whether or not the panel is currently in advanced mode or not.
     */
    public boolean getAdvancedMode()
    {
        return inAdvancedMode;
    }

    public void setAdvancedMode(boolean _newMode)
    {
        inAdvancedMode = _newMode;
    }

    protected TreeModel buildSoundListTree(TreeModel scriptTree)
    {
        TreeNode rootNode = (TreeNode) scriptTree.getRoot();
        int childCount = rootNode.getChildCount();
        TreeModel soundListTreeModel = new DefaultTreeModel(new DefaultMutableTreeNode("root"));
        ArrayList<String> wavePathsList = new ArrayList<>();
        for (int i = 0; i < childCount; i++)
        {
            String nodeValue = scriptTree.getChild(rootNode, i).toString();
            if (nodeValue.trim().startsWith("//"))
            {
                continue;
            }
            wavePathsList = getWavePathsAsList((TreeNode) scriptTree.getChild(rootNode, i));
            DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(nodeValue);
            for (String s : wavePathsList)
            {
                DefaultMutableTreeNode tempNode = new DefaultMutableTreeNode(s);
                newNode.add(tempNode);
            }
            ((DefaultMutableTreeNode) soundListTreeModel.getRoot()).add(newNode);
        }
        return soundListTreeModel;
    }
    
    protected void writeScriptFileToDisk(VPKEntry entryToWrite, boolean overwriteExisting)
    {
        File existsChecker = new File(Paths.get(installDir, entryToWrite.getPath()).toString());
        boolean fileExistsLocally = existsChecker.exists();
        if (fileExistsLocally && !overwriteExisting)
        {
            return;
        }
        File entryFile = new File(Paths.get(installDir, "/dota/").toFile(), entryToWrite.getPath());
        File entryDir = entryFile.getParentFile();
        if (entryDir != null && !entryDir.exists())
        {
            entryDir.mkdirs();
        }
        try (final FileChannel fc = FileUtils.openOutputStream(entryFile).getChannel())
        {
            fc.write(entryToWrite.getData());
        }
        catch (IOException ex)
        {
            JOptionPane.showMessageDialog(this, "Error: Unable to write script file to disk.\nDetails: " + ex.getMessage(), "Error writing script file", JOptionPane.ERROR_MESSAGE);
        }
    }

    protected void revertButtonActionPerformed(ActionEvent evt)
    {
        revertButtonActionPerformed(evt, Paths.get(vpkPath));
    }
    
    protected void revertButtonActionPerformed(ActionEvent evt, Path vpkToRevert)
    {
        //TODO: See if we can abstract away some of this functionality
        if (currentTree.getSelectionRows().length != 0 && ((TreeNode) currentTree.getSelectionPath().getLastPathComponent()).isLeaf())
        {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) currentTree.getSelectionPath().getLastPathComponent();
            String selectedWaveString = ((DefaultMutableTreeNode) selectedNode).getUserObject().toString();
            String selectedWaveParentString = ((DefaultMutableTreeNode) ((DefaultMutableTreeNode) selectedNode).getParent()).getUserObject().toString();
            selectedNode = (DefaultMutableTreeNode) this.getTreeNodeFromWavePath(selectedWaveString);
            //First go in and delete the sound in customSounds
            deleteSoundFileByWaveString(selectedWaveString);
            //Get the relevant wavestring from the internal scriptfile
            VPKArchive vpk = new VPKArchive();
            try
            {
                vpk.load(new File(vpkToRevert.toString()));
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
            String scriptDir = getCurrentScriptString();
            scriptDir = scriptDir.replace(Paths.get(installDir, "/dota/").toString(), "");
            scriptDir = scriptDir.replace("\\", "/"); //Match internal forward slashes
            scriptDir = scriptDir.substring(1); //Cut off leading slash
            byte[] bytes = null;
            VPKEntry entry = vpk.getEntry(scriptDir);
            try
            {
                ByteBuffer scriptBuffer = entry.getData();
                bytes = new byte[scriptBuffer.remaining()];
                scriptBuffer.get(bytes);
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
            String scriptFileString = new String(bytes, Charset.forName("UTF-8"));
            ArrayList<String> wavePathList = this.getWavePathsAsList(selectedNode.getParent());
            int waveStringIndex = wavePathList.indexOf(selectedWaveString);
            //Cut off every part of the scriptFileString before we get to the entry describing the relevant hero action, so we don't accidentally get the wrong wavepaths
            StringBuilder scriptFileStringShortened = new StringBuilder();
            Scanner scan = new Scanner(scriptFileString);
            boolean found = false;
            while (scan.hasNextLine())
            {
                String curLine = scan.nextLine();
                if (curLine.equals(selectedWaveParentString))
                {
                    found = true;
                }
                if (found == true)
                {
                    scriptFileStringShortened.append(curLine).append(System.lineSeparator());
                }
            }
            scriptFileString = scriptFileStringShortened.toString();
            ArrayList<String> internalWavePathsList = getWavePathListFromString(scriptFileString);
            String replacementString = internalWavePathsList.get(waveStringIndex);
            selectedNode.setUserObject(replacementString);
            ScriptParser parser = new ScriptParser(this.currentTreeModel);
            parser.writeModelToFile(getCurrentScriptString());
            //Modify the UI treeNode in addition to the backing TreeNode
            ((DefaultMutableTreeNode) currentTree.getLastSelectedPathComponent()).setUserObject(replacementString);
            ((DefaultTreeModel) currentTree.getModel()).nodeChanged((DefaultMutableTreeNode) currentTree.getLastSelectedPathComponent());
        }
    }   

    protected void playSoundButtonActionPerformed(ActionEvent evt)
    {
        playSoundButtonActionPerformed(evt, Paths.get(vpkPath));
    }
    
    protected void playSoundButtonActionPerformed(ActionEvent evt, Path vpkToPlayFrom)
    {
        if (currentTree.getSelectionRows().length != 0 && ((TreeNode) currentTree.getSelectionPath().getLastPathComponent()).isLeaf())
        {
            this.playSelectedTreeSound(currentTree.getSelectionPath(), vpkToPlayFrom);
        }
    }

    protected void replaceButtonActionPerformed(ActionEvent evt)
    {
        if (currentTree.getSelectionRows() != null && currentTree.getSelectionRows().length != 0 
                && ((TreeNode) currentTree.getSelectionPath().getLastPathComponent()).isLeaf())
        {
            TreeNode selectedFile = (TreeNode) currentTree.getSelectionPath().getLastPathComponent();
            promptUserForNewFile(selectedFile.toString());
        }
    }

    protected void revertAllButtonActionPerformed(ActionEvent evt)
    {
        //Delete existing script file
        String scriptFilePath = getCurrentScriptString();
        File scriptFileToDelete = new File(scriptFilePath);
        if (scriptFileToDelete.isFile())
        {
            try
            {
                Files.delete(Paths.get(scriptFilePath));
            }
            catch (NoSuchFileException | DirectoryNotEmptyException | SecurityException ex)
            {
                ex.printStackTrace();
            }
            catch (IOException ex)
            {
                System.err.println("IOException in delete.");
            }
        }
        else
        {
            System.err.println("Unable to delete script file at " + scriptFileToDelete.getAbsolutePath());
        }
        //Repopulate soundtree
        populateSoundList();
    }
}
