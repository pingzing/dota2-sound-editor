package dotaSoundEditor.Controls;

import Helpers.PortraitFinder;
import Helpers.ScriptParser;
import Helpers.Utility;
import dotaSoundEditor.*;
import info.ata4.vpk.VPKArchive;
import info.ata4.vpk.VPKEntry;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.*;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import org.apache.commons.io.FileUtils;

public class HeroPanel extends EditorPanel
{        
    PortraitFinder portraitFinder;

    //Just used for designer compatibility. Should never be called from code.
    public HeroPanel()
    {
        initComponents();
    }

    public HeroPanel(String _vpkDir, String _installDir)
    {

        vpkDir = _vpkDir;
        installDir = _installDir;
        this.setName("Hero Spells");
        initComponents();
        
        portraitFinder = Utility.portraitFinder;
        currentDropdown = heroSpellsDropdown;
        currentTree = heroSpellTree;
        this.populateDropdownBox();
        this.attachDoubleClickListenerToTree();
        this.setVisible(true);                
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jLabel1 = new javax.swing.JLabel();
        heroImageLabel = new javax.swing.JLabel();
        heroSpellsDropdown = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();

        setPreferredSize(new java.awt.Dimension(485, 495));

        jLabel1.setText("Hero:");
        jLabel1.setName("heroLabel"); // NOI18N

        heroImageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        heroImageLabel.setMaximumSize(new java.awt.Dimension(128, 72));
        heroImageLabel.setMinimumSize(new java.awt.Dimension(128, 72));
        heroImageLabel.setPreferredSize(new java.awt.Dimension(128, 72));

        heroSpellsDropdown.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        heroSpellsDropdown.setName("heroDropdownBox"); // NOI18N
        heroSpellsDropdown.addItemListener(new java.awt.event.ItemListener()
        {
            public void itemStateChanged(java.awt.event.ItemEvent evt)
            {
                heroSpellsDropdownStateChanged(evt);
            }
        });

        jScrollPane1.setName("heroListFrame"); // NOI18N

        heroSpellTree.setMinimumSize(new java.awt.Dimension(72, 64));
        jScrollPane1.setViewportView(heroSpellTree);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(heroSpellsDropdown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(heroImageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(heroSpellsDropdown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(heroImageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void heroSpellsDropdownStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_heroSpellsDropdownStateChanged
    {//GEN-HEADEREND:event_heroSpellsDropdownStateChanged
        //TODO: Find a way to inform the Advanced button that it needs to reset.
        if (evt.getStateChange() == ItemEvent.SELECTED)
        {
            System.out.println(currentDropdown.getSelectedItem().toString());            
            populateSoundListAsTree();
            currentTree.setRootVisible(false);
            currentTree.setShowsRootHandles(true);
            try
            {
                fillImageFrame((NamedHero) currentDropdown.getSelectedItem());
            }
            catch (IOException ex)
            {
                System.err.println(ex.getMessage());
            }
        }
    }//GEN-LAST:event_heroSpellsDropdownStateChanged
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel heroImageLabel;
    private final javax.swing.JTree heroSpellTree = new javax.swing.JTree();
    private javax.swing.JComboBox heroSpellsDropdown;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    @Override
    protected void populateDropdownBox()
    {
        currentDropdown.removeAllItems();
        Set heroList = new CopyOnWriteArraySet();
        //Build list of heroes and populate dropwdown with it                
        File file = new File(vpkDir);
        VPKArchive vpk = new VPKArchive();

        System.out.println(file);

        try
        {
            vpk.load(file);
        }
        catch (Exception ex)
        {
            System.err.println("Can't open archive: " + ex.getMessage());
            return;
        }


        for (VPKEntry entry : vpk.getEntries())
        {
            if (entry.getPath().contains("scripts/game_sounds_heroes/"))
            {                
                heroList.add(entry.getName());
            }
        }
        //Format and prettify hero list
        for (Object hero : heroList)
        {
            String heroString = hero.toString();
            heroString = heroString.replace("game_sounds_", "");
            NamedHero tempNamedHero = new NamedHero(heroString);
            heroList.remove(hero);
            heroList.add(tempNamedHero);
        }
        Object[] heroListArray = heroList.toArray();
        Arrays.sort(heroListArray);
        for (Object h : heroListArray)
        {
            NamedHero tempHero = (NamedHero) h;
            currentDropdown.addItem(tempHero);
        }
       
        populateSoundListAsTree();
    }

    @Override
    protected void populateSoundListAsTree()            
    {
        NamedHero selectedHero = (NamedHero) currentDropdown.getSelectedItem();
        
        currentTree.setEditable(false);
        Path scriptPath = Paths.get(this.installDir + "\\dota\\scripts\\game_sounds_heroes\\game_sounds_" + selectedHero.getInternalName() + ".txt");
        File scriptFile = new File(scriptPath.toString());
        
        //Defer writing script file to disk until we're sure it doesn't exist
        if (!scriptFile.isFile())
        {
            this.getAndWriteHeroScriptFile(selectedHero.getInternalName());
        }
        ScriptParser parser = new ScriptParser(scriptPath.toFile());
        TreeModel scriptTree = parser.getTreeModel();
        this.currentTreeModel = scriptTree;        
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
    }

    private VPKEntry getAndWriteHeroScriptFile(String heroName)
    {        
        heroName = heroName.toLowerCase();

        //Don't bother looking if we already have a stored copy locally.
        File existsChecker = new File(Paths.get(installDir + "\\dota\\scripts\\game_sounds_heroes\\game_sounds_" + heroName + ".txt").toString());
        boolean fileExistsLocally = false;
        if (existsChecker.exists())
        {
            fileExistsLocally = true;
        }

        File file = new File(vpkDir);
        VPKArchive vpk = new VPKArchive();
        try
        {
            vpk.load(file);
        }
        catch (Exception ex)
        {
            System.err.println("Can't open archive: " + ex.getMessage());
            return null;
        }

        Path destPath = Paths.get(installDir + "\\dota\\");
        File destDir = destPath.toFile();

        //TODO: Change to to .getEntriesForDir(scriptDir)
        for (VPKEntry entry : vpk.getEntries())
        {
            if (entry.getName().contains("game_sounds_" + heroName))
            {
                //If it already exists, break out and move on.
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
    protected void fillImageFrame(Object _selectedItem) throws IOException
    {
        NamedHero selectedItem;
        if(_selectedItem instanceof NamedHero)
        {
            selectedItem = (NamedHero)_selectedItem;
        }
        else
        {
            return;
        }
        try
        {
            heroImageLabel.setIcon(new ImageIcon(portraitFinder.getPortrait(selectedItem.getInternalName())));
        }
        catch (NullPointerException ex)
        {
            System.err.println("Icon not found for hero: " + selectedItem.getFriendlyName());
            heroImageLabel.setIcon(new ImageIcon(""));
        }
    }

    private String getScriptPathByHeroName(String internalName)
    {
        String scriptPathString =
                Paths.get(installDir + "\\dota\\scripts\\game_sounds_heroes\\game_sounds_"
                + internalName + ".txt").toString();

        File scriptFilePath = new File(scriptPathString);

        if (scriptFilePath.isFile())
        {
            return scriptFilePath.getAbsolutePath();
        }
        else
        {
            return null;
        }
    }

    @Override
    protected void revertButtonActionPerformed(java.awt.event.ActionEvent evt)
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
            String scriptDir = this.getScriptPathByHeroName(((NamedHero) currentDropdown.getSelectedItem()).getInternalName());
            scriptDir = scriptDir.replace(Paths.get(installDir + "\\dota\\").toString(), "");
            scriptDir = scriptDir.replace("\\", "/");                           //Match internal forward slashes
            scriptDir = scriptDir.substring(1);                                 //Cut off leading slash
            scriptDir = scriptDir.substring(0, scriptDir.lastIndexOf("/") + 1); //Cut off file extension            

            String scriptFileString = null;
            byte[] bytes = null;
            for (VPKEntry entry : vpk.getEntriesForDir(scriptDir))
            {
                if (entry.getName().contains("game_sounds_" + ((NamedHero) currentDropdown.getSelectedItem()).getInternalName()))
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
            parser.writeModelToFile(this.getScriptPathByHeroName(((NamedHero) currentDropdown.getSelectedItem()).getInternalName()));

            //Modify the UI treeNode in addition to the backing TreeNode
            ((DefaultMutableTreeNode) currentTree.getLastSelectedPathComponent()).setUserObject(replacementString);
            ((DefaultTreeModel) currentTree.getModel()).nodeChanged(((DefaultMutableTreeNode) currentTree.getLastSelectedPathComponent()));
        }
    }

    @Override
    protected void revertAllButtonActionPerformed(java.awt.event.ActionEvent evt)
    {
        //Delete existing script file
        String scriptFilePath = getScriptPathByHeroName(((NamedHero) currentDropdown.getSelectedItem()).getInternalName());
        File scriptFileToDelete = new File(scriptFilePath);
        if (scriptFileToDelete.isFile())
        {
            scriptFileToDelete.delete();
        }
        else
        {
            System.err.println("Unable to delete script file at " + scriptFileToDelete.getAbsolutePath());
        }

        //Repopulate soundtree
        populateSoundListAsTree();
    }

    @Override
    protected void playSoundButtonActionPerformed(ActionEvent evt)
    {
        if (currentTree.getSelectionRows().length != 0
                && ((TreeNode) currentTree.getSelectionPath().getLastPathComponent()).isLeaf())
        {
            this.playSelectedTreeSound(currentTree.getSelectionPath());
        }
    }

    @Override
    protected void replaceButtonActionPerformed(java.awt.event.ActionEvent evt)
    {
        if (currentTree.getSelectionRows() != null
                && ((TreeNode) currentTree.getSelectionPath().getLastPathComponent()).isLeaf())
        {
            TreeNode selectedFile = ((TreeNode) currentTree.getSelectionPath().getLastPathComponent());           
            promptUserForNewFile(selectedFile.toString());
        }
    }

    @Override
    protected void advancedButtonActionPerformed(java.awt.event.ActionEvent evt, JButton advancedButton)
    {
        if (advancedButton.getText().equals("Advanced >>"))
        {
            String scriptPath = this.getScriptPathByHeroName(((NamedHero) currentDropdown.getSelectedItem()).getInternalName());
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

    @Override
    String getPanelScriptString()
    {
        return this.getScriptPathByHeroName(((NamedHero) currentDropdown.getSelectedItem()).getInternalName());
    }

    @Override
    String getCustomSoundPathString()
    {
        return "custom\\" + ((NamedHero) currentDropdown.getSelectedItem()).getInternalName() + "\\";
    }
}
