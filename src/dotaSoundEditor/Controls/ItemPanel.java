/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dotaSoundEditor.Controls;

import dotaSoundEditor.Helpers.PortraitFinder;
import dotaSoundEditor.Helpers.ScriptParser;
import dotaSoundEditor.Helpers.Utility;
import dotaSoundEditor.*;
import info.ata4.vpk.VPKArchive;
import info.ata4.vpk.VPKEntry;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author
 * Image
 * 17
 */
public final class ItemPanel extends EditorPanel
{
    PortraitFinder portraitFinder;

    public ItemPanel()
    {
        initComponents();
    }

    public ItemPanel(String _vpkDir, String _installDir)
    {
        vpkDir = _vpkDir;
        installDir = _installDir;
        this.setName("Items");
        initComponents();
        currentTree = itemTree;
        portraitFinder = Utility.portraitFinder;       
        this.populateSoundListAsTree();
        initTreeSelectionListener();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        itemImageLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        itemTree = new javax.swing.JTree();
        itemLabel = new javax.swing.JLabel();

        itemImageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        itemImageLabel.setMaximumSize(new java.awt.Dimension(128, 72));
        itemImageLabel.setMinimumSize(new java.awt.Dimension(128, 72));
        itemImageLabel.setPreferredSize(new java.awt.Dimension(124, 64));

        jScrollPane2.setViewportView(itemTree);

        itemLabel.setText("Item:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(itemLabel)
                        .addGap(0, 439, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(itemImageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(itemLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(itemImageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel itemImageLabel;
    private javax.swing.JLabel itemLabel;
    private javax.swing.JTree itemTree;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables

    private VPKEntry getAndWriteItemScriptFile()
    {
        File existsChecker = new File(Paths.get(installDir + "\\dota\\scripts\\game_sounds_items.txt").toString());
        boolean fileExistsLocally = existsChecker.exists() ? true : false;

        File vpkFile = new File(vpkDir);
        VPKArchive vpk = new VPKArchive();
        try
        {
            vpk.load(vpkFile);
        }
        catch (Exception ex)
        {
            System.err.println("Can't open VPK Archive. Details: " + ex.getMessage());
            return null;
        }

        File destDir = Paths.get(installDir + "\\dota\\").toFile();

        //TODO: change to vpk.getentry()
        for (VPKEntry entry : vpk.getEntries())
        {
            if (entry.getName().contains("game_sounds_items"))
            {
                if (fileExistsLocally)
                {
                    return entry;
                }

                File entryFile = new File(destDir, entry.getPath());
                File entryDir = entryFile.getParentFile();
                if (entryDir != null && !entryDir.exists())
                {
                    entryDir.mkdirs();
                }

                try (FileChannel fc = FileUtils.openOutputStream(entryFile).getChannel())
                {
                    fc.write(entry.getData());
                    return entry;
                }
                catch (IOException ex)
                {
                    System.err.println("Can't write " + entry.getPath() + ": " + ex.getMessage());
                }
            }
        }
        return null;
    }

    @Override
    void populateSoundListAsTree()
    {
        currentTree.setEditable(false);
        File scriptFile = new File(getCurrentScriptString());
        if (!scriptFile.isFile())
        {
            this.getAndWriteItemScriptFile();
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
    void fillImageFrame(Object _selectedItem) throws IOException
    {
        NamedItem selectedItem;
        if (_selectedItem instanceof NamedItem)
        {
            selectedItem = (NamedItem) _selectedItem;
        }
        else
        {
            return;
        }
        try
        {
            itemImageLabel.setIcon(new ImageIcon(portraitFinder.getPortrait(selectedItem.getIconName())));
        }
        catch (NullPointerException ex)
        {
            System.err.println("Icon not found for item: " + selectedItem.getFriendlyName());
            itemImageLabel.setIcon(new ImageIcon(""));
        }
    } 

    @Override
    void revertButtonActionPerformed(ActionEvent evt)
    {
        //TODO: See if we can abstract away some of this + promptUserForNewFile()'s functionality        

        if (currentTree.getSelectionRows().length != 0
                && ((TreeNode) currentTree.getSelectionPath().getLastPathComponent()).isLeaf())
        {
            DefaultMutableTreeNode selectedNode = ((DefaultMutableTreeNode) currentTree.getSelectionPath().getLastPathComponent());
            String selectedWaveString = ((DefaultMutableTreeNode) selectedNode).getUserObject().toString();
            String selectedWaveParentString = ((DefaultMutableTreeNode) ((DefaultMutableTreeNode) selectedNode).getParent()).getUserObject().toString();
            selectedNode = (DefaultMutableTreeNode) this.getTreeNodeFromWavePath(selectedWaveString);

            //First go in and delete the sound in customSounds   
            deleteSoundFileByWaveString(selectedWaveString);

            //Get the relevant wavestring from the internal scriptfile                    
            VPKArchive vpk = new VPKArchive();
            try
            {
                vpk.load(new File(this.vpkDir));
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
            String scriptDir = this.getCurrentScriptString();
            scriptDir = scriptDir.replace(Paths.get(installDir + "\\dota\\").toString(), "");
            scriptDir = scriptDir.replace("\\", "/");                           //Match internal forward slashes
            scriptDir = scriptDir.substring(1);                                 //Cut off leading slash
            scriptDir = scriptDir.substring(0, scriptDir.lastIndexOf("/") + 1); //Cut off file extension            

            String scriptFileString = null;
            byte[] bytes = null;
            for (VPKEntry entry : vpk.getEntriesForDir(scriptDir))
            {
                if (entry.getName().contains("game_sounds_items"))
                {
                    try
                    {
                        ByteBuffer scriptBuffer = null;
                        scriptBuffer = entry.getData();
                        bytes = new byte[scriptBuffer.remaining()];
                        scriptBuffer.get(bytes);
                    }
                    catch (IOException ex)
                    {
                        ex.printStackTrace();
                    }
                    scriptFileString = new String(bytes, Charset.forName("UTF-8"));
                    break;
                }
            }
            //First, getWavePathListAsString() from currently-selected sound's parent, and get the index of the relevant wavepath
            ArrayList<String> wavePathList = this.getWavePathsAsList(selectedNode.getParent());
            int waveListIndex = wavePathList.indexOf(selectedWaveString);

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
            String replacementString = internalWavePathsList.get(waveListIndex);

            selectedNode.setUserObject(replacementString);
            ScriptParser parser = new ScriptParser(this.currentTreeModel);
            parser.writeModelToFile(this.getCurrentScriptString());

            //Modify the UI treeNode in addition to the backing TreeNode
            ((DefaultMutableTreeNode) currentTree.getLastSelectedPathComponent()).setUserObject(replacementString);
            ((DefaultTreeModel) currentTree.getModel()).nodeChanged(((DefaultMutableTreeNode) currentTree.getLastSelectedPathComponent()));
        }
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
            System.out.println("Deleting old item script file successful?" + deleteSuccess);
        }
        else
        {
            System.err.println("Unable to delete script file at " + scriptFileToDelete.getAbsolutePath());
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
            String scriptPath = this.getCurrentScriptString();
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
        else if (advancedButton.getText().equals("Basic <<"))
        {
            this.populateSoundListAsTree();
            advancedButton.setText("Advanced >>");
            advancedButton.setMnemonic('a');
            currentTree.setEditable(false);
        }
    }      

    private void initTreeSelectionListener()
    {
        currentTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        currentTree.addTreeSelectionListener(new TreeSelectionListener()
        {
            @Override
            public void valueChanged(TreeSelectionEvent evt)
            {
                currentTreeSelectionChanged(evt);
            }
        });
        attachDoubleClickListenerToTree();
    }

    //Need this for ItemPanel because it's only a single script file, and thus each node requires a different portrait
    private void currentTreeSelectionChanged(TreeSelectionEvent evt)
    {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) currentTree.getLastSelectedPathComponent();
        if (node == null)
        {
            return;
        }
        //get highest parent of selected node
        while (node.getParent() != null && !node.getParent().equals(node.getRoot()))
        {
            node = (DefaultMutableTreeNode) node.getParent();
        }
        try
        {
            NamedItem clickedItem = new NamedItem(node.getUserObject().toString());
            fillImageFrame(clickedItem);
            itemLabel.setText("Item: " + clickedItem.getFriendlyName());
        }
        catch (IOException ex)
        {
            System.err.println(ex.getMessage());
        }
    }
    
    //This panel doesn't use a dropdown box. No need to implement.
    @Override
    void populateDropdownBox() {    }

    @Override
    String getCurrentScriptString()
    {
        String scriptPathString = Paths.get(installDir + "\\dota\\scripts\\game_sounds_items.txt").toString();
        File scriptFilePath = new File(scriptPathString);

        if (scriptFilePath.isFile())
        {
            return scriptFilePath.getAbsolutePath();
        }
        else
        {
            return "";
        }
    }

    @Override
    String getCustomSoundPathString()
    {
        return "custom/items/";
    }
}
