/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dotaSoundEditor.Controls;

import dotaSoundEditor.Helpers.PortraitFinder;
import dotaSoundEditor.Helpers.ScriptParser;
import dotaSoundEditor.Helpers.Utility;
import dotaSoundEditor.*;
import dotaSoundEditor.Helpers.*;
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
import javax.swing.JOptionPane;
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
 * @author Image 17
 */
public final class ItemPanel extends EditorPanel
{

    PortraitFinder portraitFinder;

    public ItemPanel()
    {
        initComponents();
    }

    public ItemPanel(String _vpkPath, String _installDir, CacheManager _cm, SoundPlayer _sp)
    {
        vpkPath = _vpkPath;
        installDir = _installDir;
        this.setName("Items");
        initComponents();
        
        soundPlayer = _sp;
        cacheManager = _cm;
        currentTree = itemTree;
        portraitFinder = Utility.portraitFinder;
        this.populateSoundList();
        initTreeSelectionListener();
        fillImageFrame("default");
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

    private VPKEntry getItemScriptFile(String nop)
    {
        String internalScriptPath = "scripts/game_sounds_items.txt";
        File vpkFile = new File(vpkPath);
        VPKArchive vpk = new VPKArchive();
        try
        {
            vpk.load(vpkFile);
        }
        catch (Exception ex)
        {
            JOptionPane.showMessageDialog(this,
                    "Error: Unable to open VPK file.\nDetails: " + ex.getMessage(),
                    "Error opening VPK", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            return null;
        }

        VPKEntry entry = vpk.getEntry(internalScriptPath);
        return entry;
    }
   
    @Override
    void populateSoundList()
    {
        inAdvancedMode = false;
        currentTree.setEditable(false);
        String scriptKey = "game_sounds_items.txt";
        File scriptFile = new File(getCurrentScriptString());
        VPKEntry entry;
        boolean needsValidation = false;

        if (!scriptFile.isFile())
        {
            entry = getItemScriptFile("");
            this.writeScriptFileToDisk(entry, false);
            this.updateCache(scriptKey, entry.getCRC32());
            scriptFile = new File(getCurrentScriptString());
        }
        else
        {
            needsValidation = true;
        }
        ScriptParser parser = new ScriptParser(scriptFile);
        TreeModel scriptTree = parser.getTreeModel();
        if (needsValidation)
        {            
            boolean isUpToDate = this.validateScriptFile(scriptKey, "scripts/" + scriptKey);
            if (!isUpToDate)
            {
                this.writeScriptFileToDisk(cacheManager.getCachedVpkEntry(), true);
                mergeNewChanges(scriptTree, scriptFile);
                this.updateCache(cacheManager.getCachedVpkEntry().getName() + ".txt", cacheManager.getCachedVpkEntry().getCRC32());
            }
        }
        this.currentTreeModel = scriptTree;       
        
        currentTree.setModel(BuildSoundListTree(scriptTree));        
        currentTree.setRootVisible(false);
        currentTree.setShowsRootHandles(true);
    }

    @Override
    void fillImageFrame(Object _selectedItem)
    {
        NamedItem selectedItem = new NamedItem();
        try
        {
            if (_selectedItem instanceof NamedItem)
            {
                selectedItem = (NamedItem) _selectedItem;
                itemImageLabel.setIcon(new ImageIcon(portraitFinder.getPortrait(selectedItem.getIconName())));
            }
            else
            {
                itemImageLabel.setIcon(new ImageIcon(portraitFinder.getPortrait("default")));
            }
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
                vpk.load(new File(this.vpkPath));
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
            String scriptDir = this.getCurrentScriptString();
            scriptDir = scriptDir.replace(Paths.get(installDir, "/dota/").toString(), "");
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
            this.playSelectedTreeSound(currentTree.getSelectionPath());
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
        populateSoundList();
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

        NamedItem clickedItem = new NamedItem(node.getUserObject().toString());
        fillImageFrame(clickedItem);
        itemLabel.setText("Item: " + clickedItem.getFriendlyName());

    }

    //This panel doesn't use a dropdown box. No need to implement.
    @Override
    void populateDropdownBox()
    {
    }

    @Override
    String getCurrentScriptString()
    {
        String scriptPathString = Paths.get(installDir, "/dota/scripts/game_sounds_items.txt").toString();
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
        return ")custom/items/";
    }

    @Override
    void updateCache(String scriptKey, long internalCrc)
    {        
        String internalPath = "scripts/game_sounds_items.txt";
        cacheManager.putScript(scriptKey, internalPath, internalCrc);
    }
}
