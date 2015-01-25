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

import dotaSoundEditor.Helpers.PortraitFinder;
import dotaSoundEditor.Helpers.ScriptParser;
import dotaSoundEditor.Helpers.Utility;
import dotaSoundEditor.*;
import dotaSoundEditor.Helpers.*;
import info.ata4.vpk.VPKArchive;
import info.ata4.vpk.VPKEntry;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ToolTipManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeSelectionModel;
import org.apache.commons.io.FilenameUtils;

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
        ToolTipManager.sharedInstance().setDismissDelay(20000);
        ToolTipManager.sharedInstance().setInitialDelay(0);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        itemImageLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        itemTree = new javax.swing.JTree();
        itemLabel = new javax.swing.JLabel();
        itemHelpLabel = new javax.swing.JLabel();

        itemImageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        itemImageLabel.setMaximumSize(new java.awt.Dimension(128, 72));
        itemImageLabel.setMinimumSize(new java.awt.Dimension(128, 72));
        itemImageLabel.setPreferredSize(new java.awt.Dimension(124, 64));

        jScrollPane2.setViewportView(itemTree);

        itemLabel.setText("Item:");

        itemHelpLabel.setForeground(new java.awt.Color(255, 0, 0));
        itemHelpLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        itemHelpLabel.setText("<html><p align=\"right\">Sounds not working?<br>Mouseover here!</p></html>");
        itemHelpLabel.setToolTipText("<html>Item sound replacements are more restricted than other sounds.\n<br>Item sounds must be the same file type (WAV or MP3) as the original. \n<brIf the new sound is longer than the original, it will be cut off.\n<br>They must  also match the bitrate (128 kpbs for MP3s, 1141 kbps for WAVs) and frequency (44.1KHz) of the original.</html>");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(itemLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 120, Short.MAX_VALUE)
                        .addComponent(itemImageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 38, Short.MAX_VALUE)
                        .addComponent(itemHelpLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(itemLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(itemImageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(itemHelpLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel itemHelpLabel;
    private javax.swing.JLabel itemImageLabel;
    private javax.swing.JLabel itemLabel;
    private javax.swing.JTree itemTree;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables

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

    //This panel doesn't use a dropdown box. No need to implement.
    @Override
    void populateDropdownBox()
    {
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
            entry = getItemScriptFile();
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

        currentTree.setModel(buildSoundListTree(scriptTree));
        currentTree.setRootVisible(false);
        currentTree.setShowsRootHandles(true);
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
        return "custom/items/";
    }

    private VPKEntry getItemScriptFile()
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
    void updateCache(String scriptKey, long internalCrc)
    {
        String internalPath = "scripts/game_sounds_items.txt";
        cacheManager.putScript(scriptKey, internalPath, internalCrc);
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

        NamedItem clickedItem = new NamedItem(node.getUserObject().toString(), this.getCurrentScriptString());
        fillImageFrame(clickedItem);
        itemLabel.setText("Item: " + clickedItem.getFriendlyName());

    }

    //For whatever reasons, item sounds replacements REFUSE to work unless the 
    //filename remains unchanged, so we're overriding the logic here to copy the 
    //custom sound, rename it and just note what it used to be in a wavestring 
    //comment. Additionally, it limits the filetypes we're allowed to use, 
    //because we have to use whatever the original was.
    @Override
    protected File promptUserForNewFile(String wavePath)
    {
        DefaultMutableTreeNode selectedTreeNode = (DefaultMutableTreeNode) getTreeNodeFromWavePath(wavePath);
        String waveString = selectedTreeNode.getUserObject().toString();
        String allowedExtension = FilenameUtils.getExtension(waveString).replace("\"", "");
        
        JFileChooser chooser = new JFileChooser(new File(UserPrefs.getInstance().getWorkingDirectory()));
        FileNameExtensionFilter filter = allowedExtension.equals("wav")
                ? new FileNameExtensionFilter("WAVs", "wav")
                : new FileNameExtensionFilter("MP3s", "mp3");
        chooser.setAcceptAllFileFilterUsed((false));
        chooser.setFileFilter(filter);
        chooser.setMultiSelectionEnabled(false);

        int chooserRetVal = chooser.showOpenDialog(chooser);
        if (chooserRetVal == JFileChooser.APPROVE_OPTION)
        {            
            Path chosenFile = Paths.get(chooser.getSelectedFile().getAbsolutePath());           

            int startIndex = -1;
            int endIndex = -1;
            //Get the actual value for the wavestring key-value pair.
            if (waveString.contains("\"wave\""))
            {
                startIndex = Utility.nthOccurrence(selectedTreeNode.getUserObject().toString(), '\"', 2);
                endIndex = Utility.nthOccurrence(selectedTreeNode.getUserObject().toString(), '\"', 3);
            }
            else    //Some wavestrings don't have the "wave" at the beginning for some reason
            {
                startIndex = Utility.nthOccurrence(selectedTreeNode.getUserObject().toString(), '\"', 0);
                endIndex = Utility.nthOccurrence(selectedTreeNode.getUserObject().toString(), '\"', 1);
            }
            String waveStringFilePath = waveString.substring(startIndex, endIndex + 1);            
            String waveStringNormalizedFilePath = waveStringFilePath.substring(0, waveStringFilePath.lastIndexOf("\""));
            waveStringNormalizedFilePath = waveStringNormalizedFilePath.replace(")", "");
            waveStringNormalizedFilePath = waveStringNormalizedFilePath.replace("\"", "");
                                    
            Path destPath = Paths.get(installDir, "/dota/sound/" + waveStringNormalizedFilePath);
            UserPrefs.getInstance().setWorkingDirectory(chosenFile.getParent().toString());

            try
            {
                new File(destPath.toString()).mkdirs();
                Files.copy(chosenFile, destPath, StandardCopyOption.REPLACE_EXISTING);

                if (waveString.contains("//")) { waveString = waveString.replace(waveString.substring(waveString.indexOf("//"), waveString.length()), ""); }
                waveString = waveString.replace(waveStringFilePath, "\"" + waveStringNormalizedFilePath + "\" //Replaced by: " + chosenFile.getFileName().toString());
                selectedTreeNode.setUserObject(waveString);

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
}
