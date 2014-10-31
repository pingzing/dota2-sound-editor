package dotaSoundEditor.Controls;

import dotaSoundEditor.Helpers.*;
import dotaSoundEditor.NamedMusic;
import info.ata4.vpk.VPKArchive;
import info.ata4.vpk.VPKEntry;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
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

    Executor e = Executors.newSingleThreadExecutor();

    public MusicPanel()
    {
        initComponents();
    }

    public MusicPanel(String _vpkPath, String _installDir)
    {
        vpkPath = _vpkPath;
        installDir = _installDir;
        this.setName("Music");
        initComponents();
        currentDropdown = musicDropdown;
        currentTree = musicTree;
        this.populateDropdownBox();
        this.populateSoundList();
        attachDoubleClickListenerToTree();
    }

    private VPKEntry getMusicScriptFile(String fileName)
    {
        File vpkFile = new File(vpkPath);
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

    private void writeMusicScriptFile(VPKEntry entryToWrite)
    {
        File existsChecker = new File(Paths.get(installDir + entryToWrite.getPath()).toString());
        boolean fileExistsLocally = existsChecker.exists() ? true : false;
        if (fileExistsLocally)
        {
            return;
        }

        File entryFile = new File(Paths.get(installDir, "/dota/").toFile(), entryToWrite.getPath());
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
    void populateSoundList()
    {
        currentTree.setEditable(false);
        inAdvancedMode = false;
        File scriptFile = new File(getCurrentScriptString());
        String scriptKey = ((NamedMusic) currentDropdown.getSelectedItem()).getInternalName().toLowerCase() + ".txt";
        VPKEntry entry;
        boolean needsValidation = false;
        if (!scriptFile.isFile())
        {
            String currentMusicPath = ((NamedMusic) currentDropdown.getSelectedItem()).getFilePath().toString();
            entry = getMusicScriptFile(currentMusicPath);
            writeMusicScriptFile(entry);
            scriptFile = new File(getCurrentScriptString());
            this.updateCache(scriptKey, entry.getCRC32());
        }
        else
        {
            needsValidation = true;
        }
        ScriptParser parser = new ScriptParser(scriptFile);
        TreeModel scriptTree = parser.getTreeModel();
        if (needsValidation)
        {
            CacheManager cm = CacheManager.getInstance();
            String internalScriptPath = ((NamedMusic) currentDropdown.getSelectedItem()).getFilePath().toString().toLowerCase();
            internalScriptPath = internalScriptPath.replace("/", "/");
            boolean isUpToDate = this.validateScriptFile(scriptKey, internalScriptPath);
            if (!isUpToDate)
            {
                this.writeMusicScriptFile(cm.getCachedVpkEntry());
                mergeNewChanges(scriptTree, scriptFile);
                this.updateCache(cm.getCachedVpkEntry().getName() + ".txt", cm.getCachedVpkEntry().getCRC32());
            }
        }
        this.currentTreeModel = scriptTree;

        //TODO: Break this out into separate method
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
            wavePathsList = super.getWavePathsAsList((TreeNode) scriptTree.getChild(rootNode, i));
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
            String scriptDir = ((NamedMusic) currentDropdown.getSelectedItem()).getFilePath().toString();
            scriptDir = scriptDir.replace("\\", "/");

            byte[] bytes = null;
            VPKEntry entry = vpk.getEntry(scriptDir);
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
            String scriptFileString = new String(bytes, Charset.forName("UTF-8"));
            ArrayList<String> wavePathList = this.getWavePathsAsList(selectedNode.getParent());
            int waveStringIndex = wavePathList.indexOf(selectedWaveString);

            //Cut off every parth of the scriptFileString before we get to the entry describing the relevant hero action, so we don't accidentally stop too early
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
                if (found)
                {
                    scriptFileStringShortened.append(curLine).append(System.lineSeparator());
                }
            }
            scriptFileString = scriptFileStringShortened.toString();
            ArrayList<String> internalWavePathsList = getWavePathListFromString(scriptFileString);
            String replacementString = internalWavePathsList.get(waveStringIndex);

            selectedNode.setUserObject(replacementString);
            ScriptParser parser = new ScriptParser(this.currentTreeModel);
            parser.writeModelToFile(Paths.get(installDir, "/dota/" + scriptDir).toString());

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
            System.out.println("Deleting old script file successful? " + deleteSuccess);
        }
        else
        {
            System.err.println("Unable to delete file at " + scriptFileToDelete.getAbsolutePath());
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

    @Override
    void populateDropdownBox()
    {
        currentDropdown.removeAllItems();
        ArrayList<String> vpkSearchPaths = new ArrayList<>();
        ArrayList<NamedMusic> namedMusicList = new ArrayList<>();        
        vpkSearchPaths.add("scripts/music/");
        vpkSearchPaths.add("scripts/music/terrorblade_arcana/");
        vpkSearchPaths.add("scripts/music/valve_dota_001/");
        vpkSearchPaths.add("scripts/music/valve_ti4/");
        File file = new File(vpkPath);
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
                        || entry.getPath().contains("game_sounds_music_tutorial.txt")
                        || entry.getPath().contains("game_sounds_stingers.txt")
                        || entry.getPath().contains("game_sounds_music_util.txt")
                        || entry.getPath().contains("game_sounds_stingers_diretide.txt")
                        || entry.getPath().contains("game_sounds_stingers_greevil.txt")
                        || entry.getPath().contains("game_sounds_stingers_main.txt"))
                {
                    int lastSlashIndex = entry.getDir().lastIndexOf("/");
                    int firstSlashIndex = entry.getDir().substring(0, lastSlashIndex - 1).lastIndexOf("/");
                    String parentDir = entry.getDir().substring(firstSlashIndex + 1, lastSlashIndex);
                    String internalName = parentDir + "/" + entry.getName();
                    
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
    }

    @Override
    String getCurrentScriptString()
    {
        if (currentDropdown.getSelectedItem() != null)
        {
            String internalPath = ((NamedMusic) currentDropdown.getSelectedItem()).getFilePath().toString();
            String scriptPathString = Paths.get(installDir, "dota", internalPath).toString();

            if (new File(scriptPathString).isFile())
            {
                return new File(scriptPathString).getAbsolutePath();
            }
        }
        else
        {
            return "";
        }
        return "";
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
        if (evt.getStateChange() == ItemEvent.SELECTED)
        {
            if (!getAdvancedMode())
            {
                //In a background thread so the app doesn't choke on fast scroling
                e.execute(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        populateSoundList();
                    }
                });
            }
            else
            {
                String scriptPath = getCurrentScriptString();
                ScriptParser parser = new ScriptParser(new File(Paths.get(scriptPath).toString()));
                TreeModel model = parser.getTreeModel();
                currentTree.setModel(model);
                currentTree.setEditable(true);
                for (int i = 0; i < currentTree.getRowCount(); i++)
                {
                    currentTree.expandRow(i);
                }
            }
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

    @Override
    void updateCache(String scriptKey, long internalCrc)
    {
        CacheManager cm = CacheManager.getInstance();
        String internalPath = ((NamedMusic) currentDropdown.getSelectedItem()).getFilePath().toString();
        internalPath = internalPath.replace("\\", "/");
        cm.putScript(scriptKey, internalPath, internalCrc);
    }
}
