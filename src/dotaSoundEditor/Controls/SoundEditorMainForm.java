package dotaSoundEditor.Controls;

import dotaSoundEditor.Helpers.CacheManager;
import dotaSoundEditor.Helpers.PortraitFinder;
import dotaSoundEditor.Helpers.SoundPlayer;
import dotaSoundEditor.UserPrefs;
import dotaSoundEditor.Helpers.Utility;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.*;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class SoundEditorMainForm extends javax.swing.JFrame
{

    private PortraitFinder portraitFinder;
    private String vpkPath;
    private String installDir;
    private JPanel currentTabPanel;
    private static final SoundPlayer soundPlayer = new SoundPlayer();
    private SoundPlayingListener soundPlayingListener = new SoundPlayingListener();
    private CacheManager cacheManager = new CacheManager();
    
    public SoundEditorMainForm(String _fileName, String _installDir)
    {       
        initComponents();
        soundPlayer.addPropertyChangeListener(soundPlayingListener);
        Utility.setFrameIcon(this);
        vpkPath = _fileName;
        installDir = _installDir;
        Utility.initPortraitFinder(vpkPath);
        portraitFinder = Utility.portraitFinder;
        portraitFinder.buildHeroPortraits();
        portraitFinder.buildItemPortraits();        
        
         try
        {
            //See if they have an autoexec.cfg
            String autoExecPath = checkForAutoExec();
            if (autoExecPath != null)
            {
                //Make sure snd_overridecache is set
                checkAndSetSndUpdate(autoExecPath);
            }
            else    //Otherwise, create autoexec.cfg and set snd_updatecache
            {
                autoExecPath = createAutoExecCfg();
                checkAndSetSndUpdate(autoExecPath);
            }
        }
        catch (Exception ex)
        {
            System.err.println("File not found");
        }

        //Create tabs
        tabPane.add(new HeroPanel(vpkPath, installDir, cacheManager, soundPlayer));
        tabPane.add(new ItemPanel(vpkPath, installDir, cacheManager, soundPlayer));
        tabPane.add(new MusicPanel(vpkPath, installDir, cacheManager, soundPlayer));
        tabPane.add(new VoicePanel(vpkPath, installDir, cacheManager, soundPlayer));

        currentTabPanel = (JPanel) tabPane.getComponentAt(tabPane.getSelectedIndex());
        this.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jSeparator2 = new javax.swing.JSeparator();
        tabPane = new javax.swing.JTabbedPane();
        revertAllButton = new javax.swing.JButton();
        advancedButton = new javax.swing.JButton();
        revertButton = new javax.swing.JButton();
        playStopButton = new javax.swing.JButton();
        replaceButton = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenuFileButton = new javax.swing.JMenu();
        JMenuClose = new javax.swing.JMenuItem();
        jMenuSettingsButton = new javax.swing.JMenu();
        JMenuInstallDir = new javax.swing.JMenuItem();
        jMenuHelpButton = new javax.swing.JMenu();
        jMenuHowTo = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JPopupMenu.Separator();
        JMenuAbout = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Dota 2 Sound Editor");
        setMinimumSize(new java.awt.Dimension(500, 570));
        addWindowListener(new java.awt.event.WindowAdapter()
        {
            public void windowClosing(java.awt.event.WindowEvent evt)
            {
                formWindowClosing(evt);
            }
        });

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        tabPane.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                tabPaneStateChanged(evt);
            }
        });

        revertAllButton.setMnemonic('t');
        revertAllButton.setText("Revert All");
        revertAllButton.setToolTipText("");
        revertAllButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                revertAllButtonActionPerformed(evt);
            }
        });

        advancedButton.setMnemonic('d');
        advancedButton.setText("Advanced >>");
        advancedButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                advancedButtonActionPerformed(evt);
            }
        });

        revertButton.setMnemonic('e');
        revertButton.setText("Revert");
        revertButton.setToolTipText("Revert selected sound to its default sound.");
        revertButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                revertButtonActionPerformed(evt);
            }
        });

        playStopButton.setMnemonic('a');
        playStopButton.setText("Play Sound");
        playStopButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                playStopButtonActionPerformed(evt);
            }
        });

        replaceButton.setMnemonic('r');
        replaceButton.setText("Replace");
        replaceButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                replaceButtonActionPerformed(evt);
            }
        });

        jMenuFileButton.setMnemonic('f');
        jMenuFileButton.setText("File");

        JMenuClose.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        JMenuClose.setText("Close");
        JMenuClose.setToolTipText("");
        JMenuClose.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                JMenuCloseActionPerformed(evt);
            }
        });
        jMenuFileButton.add(JMenuClose);

        jMenuBar1.add(jMenuFileButton);

        jMenuSettingsButton.setMnemonic('s');
        jMenuSettingsButton.setText("Settings");

        JMenuInstallDir.setText("Change install directory");
        JMenuInstallDir.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                JMenuInstallDirActionPerformed(evt);
            }
        });
        jMenuSettingsButton.add(JMenuInstallDir);

        jMenuBar1.add(jMenuSettingsButton);

        jMenuHelpButton.setMnemonic('h');
        jMenuHelpButton.setText("Help");

        jMenuHowTo.setText("How to use");
        jMenuHowTo.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuHowToActionPerformed(evt);
            }
        });
        jMenuHelpButton.add(jMenuHowTo);
        jMenuHelpButton.add(jSeparator7);

        JMenuAbout.setText("About");
        JMenuAbout.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                JMenuAboutActionPerformed(evt);
            }
        });
        jMenuHelpButton.add(JMenuAbout);

        jMenuBar1.add(jMenuHelpButton);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(advancedButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                .addComponent(revertAllButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(revertButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(playStopButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(replaceButton)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 244, Short.MAX_VALUE)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 244, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tabPane, javax.swing.GroupLayout.DEFAULT_SIZE, 523, Short.MAX_VALUE)
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(revertAllButton)
                    .addComponent(revertButton)
                    .addComponent(advancedButton)
                    .addComponent(replaceButton)
                    .addComponent(playStopButton))
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 291, Short.MAX_VALUE)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 292, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void JMenuCloseActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_JMenuCloseActionPerformed
    {//GEN-HEADEREND:event_JMenuCloseActionPerformed
        Window w = this;
        w.getToolkit().getSystemEventQueue().postEvent(new WindowEvent(w, WindowEvent.WINDOW_CLOSING));
    }//GEN-LAST:event_JMenuCloseActionPerformed

    private void jMenuHowToActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuHowToActionPerformed
    {//GEN-HEADEREND:event_jMenuHowToActionPerformed
        JFrame readme = new ReadmePanel();
        readme.setVisible(true);
    }//GEN-LAST:event_jMenuHowToActionPerformed

    //Delete scratch.wav and scratch.mp3 if they exist. Not 100% reliable
    //Should probably do this on load too, just to be nice
    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
        deleteScratchFiles();        
        try
        {
            cacheManager.saveCache();
        }
        catch (IOException | SecurityException | URISyntaxException | NullPointerException ex)
        {
            JOptionPane.showMessageDialog(this, "Error: Unable to save scripts cache."
                    + "\nDetails: " + ex.getMessage(), "Error saving cache", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }//GEN-LAST:event_formWindowClosing

    private void JMenuInstallDirActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_JMenuInstallDirActionPerformed
    {//GEN-HEADEREND:event_JMenuInstallDirActionPerformed
        //Change steam install loc   
        UserPrefs prefs = UserPrefs.getInstance();
        JDialog locationCheckDialog = new JDialog();
        locationCheckDialog.setModal(true);
        locationCheckDialog.setAlwaysOnTop(true);
        locationCheckDialog.setTitle("Locate Dota 2");
        locationCheckDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        SteamLocationPanel panel = new SteamLocationPanel(prefs, true, locationCheckDialog);
        locationCheckDialog.add(panel);
        locationCheckDialog.setSize(panel.getPreferredSize());
        locationCheckDialog.setVisible(true);
    }//GEN-LAST:event_JMenuInstallDirActionPerformed

    private void JMenuAboutActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_JMenuAboutActionPerformed
    {//GEN-HEADEREND:event_JMenuAboutActionPerformed
        JDialog aboutDialog = new JDialog();
        aboutDialog.setModal(true);
        aboutDialog.setAlwaysOnTop(true);
        aboutDialog.setTitle("About Dota 2 Sound Editor");
        aboutDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        AboutPanel panel = new AboutPanel(aboutDialog);
        aboutDialog.add(panel);
        aboutDialog.setSize(panel.getPreferredSize());
        aboutDialog.setVisible(true);
    }//GEN-LAST:event_JMenuAboutActionPerformed

    private void revertButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_revertButtonActionPerformed
    {//GEN-HEADEREND:event_revertButtonActionPerformed
        ((EditorPanel) currentTabPanel).revertButtonActionPerformed(evt);
    }//GEN-LAST:event_revertButtonActionPerformed

    private void playStopButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_playStopButtonActionPerformed
    {//GEN-HEADEREND:event_playStopButtonActionPerformed
        ((EditorPanel) currentTabPanel).playSoundButtonActionPerformed(evt);
    }//GEN-LAST:event_playStopButtonActionPerformed

    private void revertAllButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_revertAllButtonActionPerformed
    {//GEN-HEADEREND:event_revertAllButtonActionPerformed
        ((EditorPanel) currentTabPanel).revertAllButtonActionPerformed(evt);
    }//GEN-LAST:event_revertAllButtonActionPerformed

    private void replaceButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_replaceButtonActionPerformed
    {//GEN-HEADEREND:event_replaceButtonActionPerformed
        ((EditorPanel) currentTabPanel).replaceButtonActionPerformed(evt);
    }//GEN-LAST:event_replaceButtonActionPerformed

    private void advancedButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_advancedButtonActionPerformed
    {//GEN-HEADEREND:event_advancedButtonActionPerformed
        ((EditorPanel) currentTabPanel).advancedButtonActionPerformed(evt, advancedButton);        
    }//GEN-LAST:event_advancedButtonActionPerformed

    private void tabPaneStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_tabPaneStateChanged
    {//GEN-HEADEREND:event_tabPaneStateChanged
        currentTabPanel = (JPanel) tabPane.getComponentAt(tabPane.getSelectedIndex());
        if (!((EditorPanel) currentTabPanel).getAdvancedMode())
        {
            advancedButton.setText("Advanced >>");
            advancedButton.setMnemonic('a');
        }
        else
        {
            advancedButton.setText("Basic <<");
            advancedButton.setMnemonic('a');
        }
    }//GEN-LAST:event_tabPaneStateChanged

    private void deleteScratchFiles()
    {
        Path scratchMp3Path = Paths.get(System.getProperty("user.dir") + "/scratch/scratch.mp3");
        File scratchMp3File = new File(scratchMp3Path.toString());
        if (scratchMp3File.isFile())
        {
            boolean success = scratchMp3File.delete();
            System.out.println("mp3 success: " + success);
        }
        Path scratchWavPath = Paths.get(System.getProperty("user.dir") + "/scratch/scratch.wav");
        File scratchWavFile = new File(scratchWavPath.toString());
        if (scratchWavFile.isFile())
        {
            boolean success = scratchWavFile.delete();
            System.out.println("wav success: " + success);
        }
    }

    private String checkForAutoExec()
    {
        Path autoExecPath = Paths.get(installDir + "/dota/cfg/autoexec.cfg");
        File autoExecFile = new File(autoExecPath.toString());
        File[] fileList = autoExecFile.getParentFile().listFiles();
        if(fileList == null) { return null; }

        for (File f : fileList)
        {
            if (f.getAbsolutePath().equalsIgnoreCase(autoExecFile.getAbsolutePath()))
            {
                return f.getAbsolutePath();
            }
        }
        return null;
    }

    //Reads autoexec.cfg for the snd_updateaudiocache line. Adds it if nonexistent.   
    private void checkAndSetSndUpdate(String autoExecPath)
    {
        InputStream fis;
        BufferedReader br;

        FileWriter fos;
        BufferedWriter bw;
        String line;

        try
        {
            fis = new FileInputStream(autoExecPath);
            br = new BufferedReader(new InputStreamReader(fis));

            while ((line = br.readLine()) != null)
            {
                if (line.contains("snd_updateaudiocache"))
                {
                    return;
                }
            }
            br.close();

            //If the line doesn't exist, append it to to the file
            fos = new FileWriter(autoExecPath, true);
            bw = new BufferedWriter(fos);
            bw.write(System.lineSeparator() + "snd_updateaudiocache");
            bw.close();
            fos.close();
            br.close();
            fis.close();
        }
        catch (Exception ex)
        {
            System.err.println(ex);
            JOptionPane.showMessageDialog(this, "Unable to update autoexec.cfg. You may have to do it manually.", "Autoexec Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String createAutoExecCfg()
    {
        Path autoExecPath = Paths.get(installDir + "//dota//cfg//");
        File autoExecFile = new File(autoExecPath.toString(), "autoexec.cfg");
        autoExecFile.getParentFile().mkdirs();
        try
        {
            autoExecFile.createNewFile();
        }
        catch (Exception ex)
        {
            System.err.println(ex);
        }
        return autoExecFile.getAbsolutePath();
    }   
    
    class SoundPlayingListener implements PropertyChangeListener
    {
        @Override
        public void propertyChange(PropertyChangeEvent evt)
        {
            String propertyName = evt.getPropertyName();
            if (propertyName.equals("soundIsPlaying"))
            {                
                //Sound started playing. Change Play button to Stop butotn.
                if ((boolean) evt.getNewValue() == true)
                {
                    playStopButton.setMnemonic('o');
                    playStopButton.setText("Stop Sound");
                    removeActionListeners();
                    playStopButton.addActionListener(new java.awt.event.ActionListener()
                    {
                        @Override
                        public void actionPerformed(java.awt.event.ActionEvent evt)
                        {
                            soundPlayer.stopSound();
                        }
                    });
                }
                //Sound just stopped. Change it back to play button.
                if ((boolean) evt.getNewValue() == false)
                {
                    playStopButton.setMnemonic('a');
                    playStopButton.setText("Play Sound");
                    removeActionListeners();
                    playStopButton.addActionListener(new java.awt.event.ActionListener()
                    {
                        @Override
                        public void actionPerformed(java.awt.event.ActionEvent evt)
                        {
                            ((EditorPanel) currentTabPanel).playSoundButtonActionPerformed(evt);
                        }
                    });
                }
            }
        }                

        private void removeActionListeners()
        {
            for (ActionListener listener : playStopButton.getActionListeners())
            {
                playStopButton.removeActionListener(listener);
            }
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem JMenuAbout;
    private javax.swing.JMenuItem JMenuClose;
    private javax.swing.JMenuItem JMenuInstallDir;
    private javax.swing.JButton advancedButton;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenu jMenuFileButton;
    private javax.swing.JMenu jMenuHelpButton;
    private javax.swing.JMenuItem jMenuHowTo;
    private javax.swing.JMenu jMenuSettingsButton;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator7;
    private javax.swing.JButton playStopButton;
    private javax.swing.JButton replaceButton;
    private javax.swing.JButton revertAllButton;
    private javax.swing.JButton revertButton;
    private javax.swing.JTabbedPane tabPane;
    // End of variables declaration//GEN-END:variables
}
