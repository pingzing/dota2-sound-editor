package dotaSoundEditor.Controls;

import dotaSoundEditor.Helpers.*;
import info.ata4.vpk.VPKArchive;
import info.ata4.vpk.VPKEntry;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import org.apache.commons.io.FileUtils;

public class MusicPanel extends EditorPanel
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
        currentTree = musicTree;
        this.populateSoundListAsTree();
    }

    private VPKEntry getMusicScriptFile()
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

        for (VPKEntry entry : vpk.getEntriesForDir("/scripts"))
        {
            if (entry.getName().contains("game_sounds_music"))
            {
                entryToReturn = entry;
            }
        }
        return entryToReturn;
    }
    //TODO: Use this as a model and see if we can't at least turn it and the one below into an abstract pair of methods   

    private void writeMusicScriptFile(VPKEntry entryToWrite)
    {
        File existsChecker = new File(Paths.get(installDir + "\\dota\\scripts\\game_sounds_music.txt").toString());
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
            fc.write(entryToWrite.getData());
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
        File scriptFile = new File(getMusicScriptPath());
        if (!scriptFile.isFile())
        {
            VPKEntry scriptEntry = getMusicScriptFile();
            writeMusicScriptFile(scriptEntry);
            scriptFile = new File(getMusicScriptPath());
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
    void fillImageFrame(Object selectedItem) throws IOException { } //This panel has no image Frame, and so this method is unecessary

    @Override
    void revertButtonActionPerformed(ActionEvent evt)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    void playSoundButtonActionPerformed(ActionEvent evt)
    {
        if(currentTree.getSelectionRows().length != 0
                && ((TreeNode)currentTree.getSelectionPath().getLastPathComponent()).isLeaf())
        {
            this.playSelectedTreeSound(currentTree.getSelectionPath());
        }
    }

    @Override
    void revertAllButtonActionPerformed(ActionEvent evt)
    {
        String scriptFilePath = getMusicScriptPath();
        File scriptFileToDelete = new File(scriptFilePath);
        
        if(scriptFileToDelete.isFile())
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
        if(currentTree.getSelectionRows() != null && ((TreeNode) currentTree.getSelectionPath().getLastPathComponent()).isLeaf())
        {
            TreeNode selectedFile = ((TreeNode) currentTree.getSelectionPath().getLastPathComponent());
            promptUserForNewFile(selectedFile.toString());            
        }
    }

    @Override
    void advancedButtonActionPerformed(ActionEvent evt, JButton advancedButton)
    {
        if(advancedButton.getText().equals("Advanced >>"))
        {
            String scriptPath = this.getMusicScriptPath();
            ScriptParser parser = new ScriptParser(new File(Paths.get(scriptPath).toString()));
            TreeModel model = parser.getTreeModel();
            currentTree.setModel(model);
            currentTree.setEditable(true);
            for(int i = 0; i < currentTree.getRowCount(); i++)
            {
                currentTree.expandRow(i);
            }
            //Change button and action to Basic-revert:
            advancedButton.setText("Basic <<");
            advancedButton.setMnemonic('a');            
        }
        else if(advancedButton.getText().equals("Basic <<"))
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    String getPanelScriptString()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    String getCustomSoundPathString()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        musicLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        musicTree = new javax.swing.JTree();

        musicLabel.setText("Song:");

        jScrollPane2.setViewportView(musicTree);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(musicLabel)
                        .addGap(0, 437, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(musicLabel)
                .addGap(76, 76, 76)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel musicLabel;
    private javax.swing.JTree musicTree;
    // End of variables declaration//GEN-END:variables

    private String getMusicScriptPath()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
