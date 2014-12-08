package dotaSoundEditor.Controls;

import dotaSoundEditor.Helpers.*;
import dotaSoundEditor.NamedVoice;
import info.ata4.vpk.VPKArchive;
import info.ata4.vpk.VPKEntry;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class VoicePanel extends EditorPanel
{

    Executor e = Executors.newSingleThreadExecutor();
    PortraitFinder portraitFinder;
    String voiceVpkPath;

    public VoicePanel()
    {
        initComponents();
    }

    public VoicePanel(String _vpkPath, String _installDir, CacheManager _cm, SoundPlayer _sp)
    {
        vpkPath = _vpkPath;
        installDir = _installDir;
        voiceVpkPath = findVoiceVpkPath();
        this.setName("Voices");
        initComponents();

        soundPlayer = _sp;
        cacheManager = _cm;
        portraitFinder = Utility.portraitFinder;
        currentDropdown = voiceDropdown;
        currentTree = voiceTree;
        this.populateDropdownBox();
        this.attachDoubleClickListenerToTree();
        this.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        voiceImageLabel = new javax.swing.JLabel();
        voiceLabel = new javax.swing.JLabel();
        voiceDropdown = new javax.swing.JComboBox();
        voicePanelScrollFrame = new javax.swing.JScrollPane();

        voiceImageLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        voiceImageLabel.setMaximumSize(new java.awt.Dimension(128, 72));
        voiceImageLabel.setMinimumSize(new java.awt.Dimension(128, 72));
        voiceImageLabel.setPreferredSize(new java.awt.Dimension(128, 72));

        voiceLabel.setText("Voice");
        voiceLabel.setName("voiceLabel"); // NOI18N

        voiceDropdown.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        voiceDropdown.setName("voiceDropdownBox"); // NOI18N
        voiceDropdown.addItemListener(new java.awt.event.ItemListener()
        {
            public void itemStateChanged(java.awt.event.ItemEvent evt)
            {
                voiceDropdownStateChanged(evt);
            }
        });

        voicePanelScrollFrame.setName("voiceListFrame"); // NOI18N

        voiceTree.setMinimumSize(new java.awt.Dimension(72, 64));
        voicePanelScrollFrame.setViewportView(voiceTree);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 485, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(voiceLabel)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(voiceDropdown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, Short.MAX_VALUE))
                        .addComponent(voicePanelScrollFrame, javax.swing.GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(0, 0, Short.MAX_VALUE)
                            .addComponent(voiceImageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, Short.MAX_VALUE)))
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 495, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(voiceLabel)
                        .addComponent(voiceDropdown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(voiceImageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(voicePanelScrollFrame, javax.swing.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE)
                    .addContainerGap()))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void voiceDropdownStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_voiceDropdownStateChanged
    {//GEN-HEADEREND:event_voiceDropdownStateChanged
        //TODO: Find a way to inform the Advanced button that it needs to reset.
        if (evt.getStateChange() == ItemEvent.SELECTED)
        {
            System.out.println("Dropdown selected: " + currentDropdown.getSelectedItem().toString());
            //In a background thread so the app doesn't choke on fast scroling
            if (!getAdvancedMode())
            {
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
            try
            {
                fillImageFrame((NamedVoice) currentDropdown.getSelectedItem());
            }
            catch (IOException ex)
            {
                System.err.println(ex.getMessage());
            }
        }
    }//GEN-LAST:event_voiceDropdownStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox voiceDropdown;
    private javax.swing.JLabel voiceImageLabel;
    private javax.swing.JLabel voiceLabel;
    private javax.swing.JScrollPane voicePanelScrollFrame;
    private final javax.swing.JTree voiceTree = new javax.swing.JTree();
    // End of variables declaration//GEN-END:variables

    @Override
    void fillImageFrame(Object _selectedItem) throws IOException
    {
        NamedVoice selectedItem;
        if (_selectedItem instanceof NamedVoice)
        {
            selectedItem = (NamedVoice) _selectedItem;
        }
        else
        {
            return;
        }
        try
        {
            voiceImageLabel.setIcon(new ImageIcon(portraitFinder.getPortrait(selectedItem.getInternalName())));
        }
        catch (NullPointerException ex)
        {
            System.err.println("Icon not found for voice: " + selectedItem.getFriendlyName());
            voiceImageLabel.setIcon(new ImageIcon(""));
        }
    }

    @Override
    void populateDropdownBox()
    {
        currentDropdown.removeAllItems();
        ArrayList<NamedVoice> namedVoiceList = new ArrayList<>();

        File file = new File(vpkPath);
        VPKArchive vpk = new VPKArchive();

        try
        {
            vpk.load(file);
        }
        catch (Exception ex)
        {
            System.err.println("Cant open archive " + ex.getMessage());
            return;
        }

        String vpkVoiceSoundsDir = "scripts/voscripts/";
        for (VPKEntry entry : vpk.getEntriesForDir(vpkVoiceSoundsDir))
        {
            String internalName = entry.getName();
            if (internalName.contains("phonemes"))
            {
                continue;
            }
            internalName = internalName.replace("game_sounds_vo_", "");

            NamedVoice nv = new NamedVoice(internalName, entry.getPath());
            namedVoiceList.add(nv);
        }

        Collections.sort(namedVoiceList);
        for (NamedVoice nv : namedVoiceList)
        {
            currentDropdown.addItem(nv);
        }
    }

    @Override
    void populateSoundList()
    {
        inAdvancedMode = false;
        currentTree.setEditable(false);
        NamedVoice selectedVoice = (NamedVoice) currentDropdown.getSelectedItem();

        Path scriptPath = Paths.get(this.installDir, "/dota/scripts/voscripts/game_sounds_vo_" + selectedVoice.getInternalName() + ".txt");
        File scriptFile = new File(scriptPath.toString());
        String scriptKey = "game_sounds_vo_" + selectedVoice.getInternalName() + ".txt".toLowerCase();

        boolean needsValidation = false;
        VPKEntry entry;
        if (!scriptFile.isFile())
        {
            entry = getVoiceScriptFile(selectedVoice.getInternalName());
            writeScriptFileToDisk(entry, false);
            updateCache(scriptKey, entry.getCRC32());
        }
        else
        {
            needsValidation = true;
        }
        ScriptParser parser = new ScriptParser(scriptPath.toFile());
        TreeModel scriptTree = parser.getTreeModel();
        if (needsValidation)
        {
            boolean isUpToDate = this.validateScriptFile(scriptKey, "scripts/voscripts/" + scriptKey);
            if (!isUpToDate)
            {
                this.writeScriptFileToDisk(cacheManager.getCachedVpkEntry(), true);
                mergeNewChanges(scriptTree, scriptPath);
                this.updateCache(cacheManager.getCachedVpkEntry().getName() + ".txt", cacheManager.getCachedVpkEntry().getCRC32());
            }
        }
        this.currentTreeModel = scriptTree;
        currentTree.setModel(buildSoundListTree(scriptTree));
    }

    @Override
    String getCurrentScriptString()
    {
        return this.getScriptPathByVoiceName(((NamedVoice) currentDropdown.getSelectedItem()).getInternalName());
    }

    private String getScriptPathByVoiceName(String internalName)
    {
        String scriptPathString
                = Paths.get(installDir, "/dota/scipts/voscripts/game_sounds_vo"
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
    String getCustomSoundPathString()
    {
        return Paths.get("custom", ((NamedVoice) currentDropdown.getSelectedItem()).getInternalName()).toString() + File.separator;
    }

    private VPKEntry getVoiceScriptFile(String voiceName)
    {
        voiceName = voiceName.toLowerCase();
        String internalScriptPath = "scripts/voscripts/game_sounds_vo_" + voiceName + ".txt";
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
        String internalPath = "scripts/voscripts/game_sounds_vo_" + ((NamedVoice) currentDropdown.getSelectedItem()).getInternalName() + ".txt";
        cacheManager.putScript(scriptKey, internalPath, internalCrc);
    }

//The following two methods are pretty much unacceptable for shipping.
//For now, We're going to use this as a workaround, because EdtiorPanel's PlaySound
//assumes that the sound lives in the pak01.vpk, while VO sounds live in their own VPK.
//TODO: Fix this in EditorPanel to allow passing some kind of arg.
    @Override
    protected void playSoundButtonActionPerformed(ActionEvent evt)
    {        
        String originalVpkPath = vpkPath;
        vpkPath = voiceVpkPath;
        super.playSoundButtonActionPerformed(evt);
        vpkPath = originalVpkPath;
    }

    @Override
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
                        String originalVpkPath = vpkPath;
                        vpkPath = voiceVpkPath;
                        playSelectedTreeSound(selPath);
                        vpkPath = originalVpkPath;
                    }
                }
            }
        };
        currentTree.addMouseListener(ml);
    }

    private String findVoiceVpkPath()
    {
        return Paths.get(installDir + "/dota/sound_vo_english_dir.vpk").toString();
    }
}
