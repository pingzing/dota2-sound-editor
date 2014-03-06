/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dotaSoundEditor.Controls;

import dotaSoundEditor.AboutPanel;
import dotaSoundEditor.PortraitFinder;
import dotaSoundEditor.ReadmePanel;
import dotaSoundEditor.SteamLocationPanel;
import dotaSoundEditor.UserPrefs;
import dotaSoundEditor.Utility;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.io.*;
import java.nio.file.*;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author
 * Neil
 * McAlister
 */
public class SoundEditorMainForm extends javax.swing.JFrame
{

    /**
     * Creates
     * new
     * form
     * SoundEditorMainForm
     */
    private PortraitFinder portraitFinder;
    private String vpkDir;
    private String installDir;
    private JPanel currentTabPanel;

    public SoundEditorMainForm(String _fileName, String _installDir)
    {
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

        Utility.setFrameIcon(this);
        initComponents();
        vpkDir = _fileName;
        installDir = _installDir;
        Utility.initPortraitFinder(vpkDir);
        portraitFinder = Utility.portraitFinder;
        portraitFinder.buildHeroPortraits();
        portraitFinder.buildItemPortraits();
        

        //Create tabs
        tabPane.add(new HeroPanel(vpkDir, installDir));
        tabPane.add(new ItemPanel(vpkDir, installDir));

        currentTabPanel = (JPanel) tabPane.getComponentAt(tabPane.getSelectedIndex());
        this.setVisible(true);


    }

    /**
     * This
     * method
     * is
     * called
     * from
     * within
     * the
     * constructor
     * to
     * initialize
     * the
     * form.
     * WARNING:
     * Do
     * NOT
     * modify
     * this
     * code.
     * The
     * content
     * of
     * this
     * method
     * is
     * always
     * regenerated
     * by
     * the
     * Form
     * Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jSeparator2 = new javax.swing.JSeparator();
        tabPane = new javax.swing.JTabbedPane();
        revertAllButton = new javax.swing.JButton();
        advancedButton = new javax.swing.JButton();
        revertButton = new javax.swing.JButton();
        playSoundButton = new javax.swing.JButton();
        replaceButton = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JPopupMenu.Separator();
        jMenuItem3 = new javax.swing.JMenuItem();

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

        playSoundButton.setMnemonic('a');
        playSoundButton.setText("Play Sound");
        playSoundButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                playSoundButtonActionPerformed(evt);
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

        jMenu1.setMnemonic('f');
        jMenu1.setText("File");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem1.setText("Close");
        jMenuItem1.setToolTipText("");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setMnemonic('s');
        jMenu2.setText("Settings");

        jMenuItem4.setText("Change install directory");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);

        jMenuBar1.add(jMenu2);

        jMenu3.setMnemonic('h');
        jMenu3.setText("Help");

        jMenuItem2.setText("How to use");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem2);
        jMenu3.add(jSeparator7);

        jMenuItem3.setText("About");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem3);

        jMenuBar1.add(jMenu3);

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
                .addComponent(playSoundButton)
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
                    .addComponent(playSoundButton))
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 291, Short.MAX_VALUE)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 292, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem1ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem1ActionPerformed
        Window w = this;
        w.getToolkit().getSystemEventQueue().postEvent(new WindowEvent(w, WindowEvent.WINDOW_CLOSING));
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem2ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem2ActionPerformed
        JFrame readme = new ReadmePanel();
        readme.setVisible(true);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    //Delete scratch.wav and scratch.mp3 if they exist. Not 100% reliable
    //Should probably do this on load too, just to be nice
    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
        deleteScratchFiles();
    }//GEN-LAST:event_formWindowClosing

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem4ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem4ActionPerformed
        //Change steam install loc   
        UserPrefs prefs = new UserPrefs();
        JDialog locationCheckDialog = new JDialog();
        locationCheckDialog.setModal(true);
        locationCheckDialog.setAlwaysOnTop(true);
        locationCheckDialog.setTitle("Locate Dota 2");
        locationCheckDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        SteamLocationPanel panel = new SteamLocationPanel(prefs, true, locationCheckDialog);
        locationCheckDialog.add(panel);
        locationCheckDialog.setSize(panel.getPreferredSize());
        locationCheckDialog.setVisible(true);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem3ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem3ActionPerformed
        JDialog aboutDialog = new JDialog();
        aboutDialog.setModal(true);
        aboutDialog.setAlwaysOnTop(true);
        aboutDialog.setTitle("About Dota 2 Sound Editor");
        aboutDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        AboutPanel panel = new AboutPanel(aboutDialog);
        aboutDialog.add(panel);
        aboutDialog.setSize(panel.getPreferredSize());
        aboutDialog.setVisible(true);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void revertButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_revertButtonActionPerformed
    {//GEN-HEADEREND:event_revertButtonActionPerformed
        ((EditorPanel) currentTabPanel).revertButtonActionPerformed(evt);
    }//GEN-LAST:event_revertButtonActionPerformed

    private void playSoundButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_playSoundButtonActionPerformed
    {//GEN-HEADEREND:event_playSoundButtonActionPerformed
        ((EditorPanel) currentTabPanel).playSoundButtonActionPerformed(evt);
    }//GEN-LAST:event_playSoundButtonActionPerformed

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
    }//GEN-LAST:event_tabPaneStateChanged

    private void deleteScratchFiles()
    {
        Path scratchMp3Path = Paths.get(System.getProperty("user.dir") + "\\scratch\\scratch.mp3");
        File scratchMp3File = new File(scratchMp3Path.toString());
        if (scratchMp3File.isFile())
        {
            boolean success = scratchMp3File.delete();
            System.out.println("mp3 success: " + success);
        }
        Path scratchWavPath = Paths.get(System.getProperty("user.dir") + "\\scratch\\scratch.wav");
        File scratchWavFile = new File(scratchWavPath.toString());
        if (scratchWavFile.isFile())
        {
            boolean success = scratchWavFile.delete();
            System.out.println("wav success: " + success);
        }
    }

    private String checkForAutoExec()
    {
        Path autoExecPath = Paths.get(installDir + "//dota//cfg//autoexec.cfg");
        File autoExecFile = new File(autoExecPath.toString());
        File[] fileList = autoExecFile.getParentFile().listFiles();

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
    //DEPRECATED. Left in as historical
//    private void populateSoundList(NamedHero selectedHero)
//    {
//        this.getHeroScriptFile(selectedHero.internalName);
//        Path scriptPath = Paths.get(this.installDir + "\\dota\\scripts\\game_sounds_heroes\\game_sounds_" + selectedHero.getInternalName() + ".txt");
//        ScriptParser parser = new ScriptParser(scriptPath.toFile());
//        TreeModel scriptTree = parser.getTreeModel();
//        DefaultListModel scriptList = new DefaultListModel();
//
//        //Get all children of root node:
//        TreeNode rootNode = (TreeNode) scriptTree.getRoot();
//        int childCount = rootNode.getChildCount();
//        for (int i = 0; i < childCount; i++)
//        {
//            scriptList.addElement(scriptTree.getChild(rootNode, i));
//        }
//
//        heroSoundList.setModel(scriptList);
//    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton advancedButton;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator7;
    private javax.swing.JButton playSoundButton;
    private javax.swing.JButton replaceButton;
    private javax.swing.JButton revertAllButton;
    private javax.swing.JButton revertButton;
    private javax.swing.JTabbedPane tabPane;
    // End of variables declaration//GEN-END:variables
}
