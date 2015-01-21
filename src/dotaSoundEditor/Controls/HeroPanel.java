package dotaSoundEditor.Controls;

import dotaSoundEditor.Helpers.PortraitFinder;
import dotaSoundEditor.Helpers.ScriptParser;
import dotaSoundEditor.Helpers.Utility;
import dotaSoundEditor.*;
import dotaSoundEditor.Helpers.*;
import info.ata4.vpk.VPKArchive;
import info.ata4.vpk.VPKEntry;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.tree.TreeModel;

public class HeroPanel extends EditorPanel
{
    Executor e = Executors.newSingleThreadExecutor();
    PortraitFinder portraitFinder;

    //Just used for designer compatibility. Should never be called from code.
    public HeroPanel()
    {
        initComponents();
    }

    public HeroPanel(String _vpkPath, String _installDir, CacheManager _cm, SoundPlayer _sp)
    {
        vpkPath = _vpkPath;
        installDir = _installDir;
        this.setName("Hero Spells");
        initComponents();

        soundPlayer = _sp;
        cacheManager = _cm;
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

        heroLabel = new javax.swing.JLabel();
        heroImageLabel = new javax.swing.JLabel();
        heroSpellsDropdown = new javax.swing.JComboBox();
        heroPanelScrollFrame = new javax.swing.JScrollPane();

        setPreferredSize(new java.awt.Dimension(485, 495));

        heroLabel.setText("Hero:");
        heroLabel.setName("heroLabel"); // NOI18N

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

        heroPanelScrollFrame.setName("heroListFrame"); // NOI18N

        heroSpellTree.setMinimumSize(new java.awt.Dimension(72, 64));
        heroPanelScrollFrame.setViewportView(heroSpellTree);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(heroLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(heroSpellsDropdown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(heroPanelScrollFrame, javax.swing.GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(heroImageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(heroLabel)
                    .addComponent(heroSpellsDropdown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(heroImageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(heroPanelScrollFrame, javax.swing.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void heroSpellsDropdownStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_heroSpellsDropdownStateChanged
    {//GEN-HEADEREND:event_heroSpellsDropdownStateChanged
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
    private javax.swing.JLabel heroLabel;
    private javax.swing.JScrollPane heroPanelScrollFrame;
    private final javax.swing.JTree heroSpellTree = new javax.swing.JTree();
    private javax.swing.JComboBox heroSpellsDropdown;
    // End of variables declaration//GEN-END:variables

    @Override
    protected void fillImageFrame(Object _selectedItem) throws IOException
    {
        NamedHero selectedItem;
        if (_selectedItem instanceof NamedHero)
        {
            selectedItem = (NamedHero) _selectedItem;
        }
        else
        {
            return;
        }
        try
        {
            heroImageLabel.setIcon(new ImageIcon(portraitFinder.getPortrait(selectedItem.getIconName())));
        }
        catch (NullPointerException ex)
        {
            System.err.println("Icon not found for hero: " + selectedItem.getFriendlyName());
            heroImageLabel.setIcon(new ImageIcon(""));
        }
    }

    @Override
    protected void populateDropdownBox()
    {
        currentDropdown.removeAllItems();
        ArrayList<NamedHero> namedHeroList = new ArrayList<>();
        //Build list of heroes and populate dropwdown with it                
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

        String vpkHeroSoundsDir = "scripts/game_sounds_heroes/";
        for (VPKEntry entry : vpk.getEntriesForDir(vpkHeroSoundsDir))
        {
            String internalName = entry.getName();
            internalName = internalName.replace("game_sounds_", "");

            NamedHero nh = new NamedHero(internalName, entry.getPath());
            namedHeroList.add(nh);
        }

        Collections.sort(namedHeroList);
        for (NamedHero nh : namedHeroList)
        {
            currentDropdown.addItem(nh);
        }
    }

    @Override
    protected void populateSoundList()
    {
        inAdvancedMode = false;
        currentTree.setEditable(false);
        NamedHero selectedHero = (NamedHero) currentDropdown.getSelectedItem();

        Path scriptPath = Paths.get(this.installDir, "/dota/scripts/game_sounds_heroes/game_sounds_" + selectedHero.getInternalName() + ".txt");
        File scriptFile = new File(scriptPath.toString());
        String scriptKey = "game_sounds_" + selectedHero.getInternalName() + ".txt".toLowerCase();
                
        boolean needsValidation = false;
        //if it doesn't exist yet, don't bother validating, and just write it out
        VPKEntry entry;
        if (!scriptFile.isFile())
        {
            entry = this.getHeroScriptFile(selectedHero.getInternalName());
            this.writeScriptFileToDisk(entry, false);
            this.updateCache(scriptKey, entry.getCRC32());
        }
        else //if it exists, we need to validate it
        {
            needsValidation = true;
        }
        ScriptParser parser = new ScriptParser(scriptPath.toFile());
        TreeModel scriptTree = parser.getTreeModel();
        if (needsValidation)
        {
            boolean isUpToDate = this.validateScriptFile(scriptKey, "scripts/game_sounds_heroes/" + scriptKey);
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
        return this.getScriptPathByHeroName(((NamedHero) currentDropdown.getSelectedItem()).getInternalName());
    }

    private String getScriptPathByHeroName(String internalName)
    {
        String scriptPathString
                = Paths.get(installDir, "/dota/scripts/game_sounds_heroes/game_sounds_"
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
        return Paths.get("custom", ((NamedHero) currentDropdown.getSelectedItem()).getInternalName()).toString() + File.separator;
    }

    private VPKEntry getHeroScriptFile(String heroName)
    {
        heroName = heroName.toLowerCase();
        String internalScriptPath = "scripts/game_sounds_heroes/game_sounds_" + heroName + ".txt";
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
        String internalPath = "scripts/game_sounds_heroes/game_sounds_" + ((NamedHero) currentDropdown.getSelectedItem()).getInternalName() + ".txt";
        cacheManager.putScript(scriptKey, internalPath, internalCrc);
    }
}
