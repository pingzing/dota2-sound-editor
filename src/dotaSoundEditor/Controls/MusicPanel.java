package dotaSoundEditor.Controls;

import dotaSoundEditor.Helpers.*;
import dotaSoundEditor.NamedMusic;
import info.ata4.vpk.VPKArchive;
import info.ata4.vpk.VPKEntry;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import org.apache.commons.io.FileUtils;

//TODO: Deal with music having been suddenly split up into a bunch of different places
public final class MusicPanel extends EditorPanel
{

    public MusicPanel()
    {
        initComponents();
    }

    public MusicPanel(String _vpkDir, String _installDir)
    {
        vpkDir = _vpkDir;
        installDir = _installDir;
        this.setName("Music");
        initComponents();
        currentDropdown = musicDropdown;
        currentTree = musicTree;
        this.populateDropdownBox();
        this.populateSoundListAsTree();
        attachDoubleClickListenerToTree();
    }

    private VPKEntry getMusicScriptFile(String fileName)
    {
        File vpkFile = new File(vpkDir);
        VPKArchive vpk = new VPKArchive();
        VPKEntry entryToReturn = null;
        try
        {
            vpk.load(vpkFile);
        }
        catch (Exception ex)
        {
            System.err.println("Can't open VPK Archive. Details: " + ex.getMessage());
            return entryToReturn;
        }

        String fn = fileName.replace("\\", "/"); //jVPKLib only uses forward slashes in paths.
        return vpk.getEntry(fn);                
    }

    //TODO: Use this as a model and see if we can't at least turn it and the one below into an abstract pair of methods   
    private void writeMusicScriptFile(VPKEntry entryToWrite)
    {
        File existsChecker = new File(Paths.get(installDir + entryToWrite.getPath()).toString());
        boolean fileExistsLocally = existsChecker.exists() ? true : false;
        if (fileExistsLocally)
        {
            return;
        }

        File entryFile = new File(Paths.get(installDir + "\\dota\\").toFile(), entryToWrite.getPath());
        File entryDir = entryFile.getParentFile();
        if (entryDir != null && !entryDir.exists())
        {
            entryDir.mkdirs();
        }

        try (FileChannel fc = FileUtils.openOutputStream(entryFile).getChannel())
        {
            int written = fc.write(entryToWrite.getData());
            System.out.println(written + " bytes written in SoundPanel");
        }
        catch (IOException ex)
        {
            System.err.println("Can't write " + entryToWrite.getPath() + ": " + ex.getMessage());
        }
    }

    @Override
    void populateSoundListAsTree()
    {
        currentTree.setEditable(false);
        File scriptFile = new File(getCurrentScriptString());
        if (!scriptFile.isFile())
        {
            String currentMusicPath = ((NamedMusic) currentDropdown.getSelectedItem()).getFilePath().toString();
            VPKEntry scriptEntry = getMusicScriptFile(currentMusicPath);
            writeMusicScriptFile(scriptEntry);
            scriptFile = new File(getCurrentScriptString());
        }
        ScriptParser parser = new ScriptParser(scriptFile);
        TreeModel scriptTree = parser.getTreeModel();
        this.currentTreeModel = scriptTree;
        DefaultListModel scriptList = new DefaultListModel();
        TreeNode rootNode = (TreeNode) scriptTree.getRoot();
        int childCount = rootNode.getChildCount();

        TreeModel soundListTreeModel = new DefaultTreeModel(new DefaultMutableTreeNode("root"));
        ArrayList<String> wavePathsList = new ArrayList<>();
        for (int i = 0; i < childCount; i++)
        {
            wavePathsList = super.getWavePathsAsList((TreeNode) scriptTree.getChild(rootNode, i));
            String nodeValue = scriptTree.getChild(rootNode, i).toString();
            DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(nodeValue);

            for (String s : wavePathsList)
            {
                DefaultMutableTreeNode tempNode = new DefaultMutableTreeNode(s);
                newNode.add(tempNode);
            }
            ((DefaultMutableTreeNode) soundListTreeModel.getRoot()).add(newNode);
        }
        currentTree.setModel(soundListTreeModel);
        currentTree.setRootVisible(false);
        currentTree.setShowsRootHandles(true);
    }

    @Override
    void fillImageFrame(Object selectedItem) throws IOException
    {
    } //This panel has no image Frame, and so this method is unecessary

    @Override
    void revertButtonActionPerformed(ActionEvent evt)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    void playSoundButtonActionPerformed(ActionEvent evt)
    {
        if (currentTree.getSelectionRows().length != 0
                && ((TreeNode) currentTree.getSelectionPath().getLastPathComponent()).isLeaf())
        {
            boolean regenSound = this.playSelectedTreeSound(currentTree.getSelectionPath());
            if(regenSound)
            {
                this.revertAllButtonActionPerformed(null);
            }
        }
    }

    @Override
    void revertAllButtonActionPerformed(ActionEvent evt)
    {
        String scriptFilePath = getCurrentScriptString();
        File scriptFileToDelete = new File(scriptFilePath);

        if (scriptFileToDelete.isFile())
        {
            boolean deleteSuccess = scriptFileToDelete.delete();
            System.out.println("Deleting old script file successful? " + deleteSuccess);
        }
        else
        {
            System.err.println("Unable to delete file at " + scriptFileToDelete.getAbsolutePath());
        }
        populateSoundListAsTree();
    }

    @Override
    void replaceButtonActionPerformed(ActionEvent evt)
    {
        if (currentTree.getSelectionRows() != null && ((TreeNode) currentTree.getSelectionPath().getLastPathComponent()).isLeaf())
        {
            TreeNode selectedFile = ((TreeNode) currentTree.getSelectionPath().getLastPathComponent());
            promptUserForNewFile(selectedFile.toString());
        }
    }

    //TODO: Move this method into the parent.
    @Override
    void advancedButtonActionPerformed(ActionEvent evt, JButton advancedButton)
    {
        if (advancedButton.getText().equals("Advanced >>"))
        {
            String scriptPath = getCurrentScriptString();
            ScriptParser parser = new ScriptParser(new File(Paths.get(scriptPath).toString()));
            TreeModel model = parser.getTreeModel();
            currentTree.setModel(model);
            currentTree.setEditable(true);

            //Change button and action to Basic-revert:
            advancedButton.setText("Basic <<");
            advancedButton.setMnemonic('a');
        }
        else if (advancedButton.getText().equals("Basic <<"))
        {
            this.populateSoundListAsTree();
            advancedButton.setText("Advanced >>");
            advancedButton.setMnemonic('a');
            currentTree.setEditable(false);
        }
    }

    @Override
    void populateDropdownBox()
    {
        currentDropdown.removeAllItems();
        ArrayList<String> vpkSearchPaths = new ArrayList<>();
        ArrayList<NamedMusic> namedMusicList = new ArrayList<>();
        vpkSearchPaths.add("sound/music/valve_dota_001/music/");
        vpkSearchPaths.add("scripts/");
        File file = new File(vpkDir);
        VPKArchive vpk = new VPKArchive();

        try
        {
            vpk.load(file);
        }
        catch (Exception ex)
        {
            System.err.println("Can't open archive: " + ex.getMessage());
            return;
        }

        for (String dir : vpkSearchPaths)
        {
            for (VPKEntry entry : vpk.getEntriesForDir(dir))
            {
                //TODO: Replace this with a (user-editable?) list of paths to search read in from a file(?)
                if (entry.getPath().contains("game_sounds_music.txt")
                        || entry.getPath().contains("game_sounds_music_int.txt")
                        || entry.getPath().contains("game_sounds_music_spectator.txt")
                        || entry.getPath().contains("game_sounds_music_tutorial.txt"))
                {
                    String internalName = entry.getName();
                    //format internal name a little bit, remove prefixes

                    NamedMusic nm = new NamedMusic(internalName, entry.getPath());
                    namedMusicList.add(nm);
                }
            }
        }

        Collections.sort(namedMusicList);
        for (NamedMusic nm : namedMusicList)
        {
            currentDropdown.addItem(nm);
        }
        populateSoundListAsTree();
    }

    //This method may not make sense for the music panel--it's the only kind of sound
    //where all the script files are in different folders, and can't be located with a single
    //script string.
    @Override
    String getCurrentScriptString()
    {
        String internalPath = ((NamedMusic) currentDropdown.getSelectedItem()).getFilePath().toString();
        String scriptPathString = Paths.get(installDir, "dota", internalPath).toString();
        if (new File(scriptPathString).isFile())
        {
            return new File(scriptPathString).getAbsolutePath();
        }
        else
        {
            return "";
        }
    }

    @Override
    String getCustomSoundPathString()
    {
        return "custom/music/";
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        musicLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        musicTree = new javax.swing.JTree();
        musicDropdown = new javax.swing.JComboBox();

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        musicLabel.setText("Music Type:");

        jScrollPane2.setViewportView(musicTree);

        musicDropdown.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        musicDropdown.addItemListener(new java.awt.event.ItemListener()
        {
            public void itemStateChanged(java.awt.event.ItemEvent evt)
            {
                musicDropdownItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(musicLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(musicDropdown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(musicLabel)
                    .addComponent(musicDropdown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(70, 70, 70)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void musicDropdownItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_musicDropdownItemStateChanged
    {//GEN-HEADEREND:event_musicDropdownItemStateChanged
        if(evt.getStateChange() == ItemEvent.SELECTED)
        {
            populateSoundListAsTree();
            currentTree.setRootVisible(false);
            currentTree.setShowsRootHandles(true);
        }
    }//GEN-LAST:event_musicDropdownItemStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JComboBox musicDropdown;
    private javax.swing.JLabel musicLabel;
    private javax.swing.JTree musicTree;
    // End of variables declaration//GEN-END:variables
}
