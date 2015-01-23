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

import dotaSoundEditor.Helpers.*;
import dotaSoundEditor.NamedMusic;
import info.ata4.vpk.VPKArchive;
import info.ata4.vpk.VPKEntry;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.swing.tree.TreeModel;

public final class MusicPanel extends EditorPanel
{

    Executor e = Executors.newSingleThreadExecutor();

    public MusicPanel()
    {
        initComponents();
    }

    public MusicPanel(String _vpkPath, String _installDir, CacheManager _cm, SoundPlayer _sp)
    {
        vpkPath = _vpkPath;
        installDir = _installDir;
        this.setName("Music");
        initComponents();

        soundPlayer = _sp;
        cacheManager = _cm;
        currentDropdown = musicDropdown;
        currentTree = musicTree;
        this.populateDropdownBox();
        this.populateSoundList();
        attachDoubleClickListenerToTree();
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
    void fillImageFrame(Object selectedItem) throws IOException
    {
    } //This panel has no image Frame, implementation is unecessary

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
                //Music is scattered in a bunch of different directories, so look in all of them
                //Pretty brittle right now, thus the TODO up there
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
                    //format internal name a little bit, remove prefixes
                    int lastSlashIndex = entry.getDir().lastIndexOf("/");
                    int firstSlashIndex = entry.getDir().substring(0, lastSlashIndex - 1).lastIndexOf("/");
                    String parentDir = entry.getDir().substring(firstSlashIndex + 1, lastSlashIndex);
                    String internalName = parentDir + "/" + entry.getName();
                   
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
            writeScriptFileToDisk(entry, false);
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
            String internalScriptPath = ((NamedMusic) currentDropdown.getSelectedItem()).getFilePath().toString().toLowerCase();
            internalScriptPath = internalScriptPath.replace("\\", "/");
            boolean isUpToDate = this.validateScriptFile(scriptKey, internalScriptPath);
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

    @Override
    void updateCache(String scriptKey, long internalCrc)
    {
        String internalPath = ((NamedMusic) currentDropdown.getSelectedItem()).getFilePath().toString();
        internalPath = internalPath.replace("\\", "/");
        cacheManager.putScript(scriptKey, internalPath, internalCrc);
    }
}
