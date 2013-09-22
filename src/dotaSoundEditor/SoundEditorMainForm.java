/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dotaSoundEditor;

import info.ata4.vpk.VPKArchive;
import info.ata4.vpk.VPKEntry;
import java.awt.Window;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.commons.io.FileUtils;
import static java.nio.file.StandardCopyOption.*;
import java.util.Enumeration;
import javafx.embed.swing.JFXPanel;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

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
    String vpkDir;
    String installDir;
    PortraitFinder portraitFinder;
    TreeModel currentHeroTreeModel;
    Media media;                //Global so we can force it to halt on close
    MediaPlayer player;         //Ditto.     

    public SoundEditorMainForm(String _fileName, String _installDir)
    {
        initComponents();
        vpkDir = _fileName;
        installDir = _installDir;
        portraitFinder = new PortraitFinder(vpkDir);

        //jMenuBar1.setVisible(false);
        populateDropdownBox();

        //Required to initialize the JavaFX libraries. Doesn't serve any other purpose.
        JFXPanel token = new JFXPanel();

        deleteScratchFiles();

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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                setVisible(true);
            }
        });
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

        heroDropdown = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        heroImageLabel = new javax.swing.JLabel();
        replaceButton = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        advancedButton = new javax.swing.JButton();
        playSoundButton = new javax.swing.JButton();
        revertButton = new javax.swing.JButton();
        revertAllButton = new javax.swing.JButton();
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
        setMinimumSize(new java.awt.Dimension(353, 495));
        addWindowListener(new java.awt.event.WindowAdapter()
        {
            public void windowClosing(java.awt.event.WindowEvent evt)
            {
                formWindowClosing(evt);
            }
        });

        heroDropdown.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        heroDropdown.setName("heroDropdownBox"); // NOI18N
        heroDropdown.addItemListener(new java.awt.event.ItemListener()
        {
            public void itemStateChanged(java.awt.event.ItemEvent evt)
            {
                heroDropdownStateChanged(evt);
            }
        });

        jLabel1.setText("Hero:");
        jLabel1.setName("heroLabel"); // NOI18N

        jScrollPane1.setName("heroListFrame"); // NOI18N

        jTree1.setMinimumSize(new java.awt.Dimension(72, 64));
        jScrollPane1.setViewportView(jTree1);

        heroImageLabel.setMaximumSize(new java.awt.Dimension(128, 72));
        heroImageLabel.setMinimumSize(new java.awt.Dimension(128, 72));
        heroImageLabel.setPreferredSize(new java.awt.Dimension(128, 72));

        replaceButton.setText("Replace");
        replaceButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                replaceButtonActionPerformed(evt);
            }
        });

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);

        advancedButton.setText("Advanced");
        advancedButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                advancedButtonActionPerformed(evt);
            }
        });

        playSoundButton.setText("Play Sound");
        playSoundButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                playSoundButtonActionPerformed(evt);
            }
        });

        revertButton.setText("Revert");
        revertButton.setToolTipText("Revert selected sound to its default sound.");
        revertButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                revertButtonActionPerformed(evt);
            }
        });

        revertAllButton.setText("Revert All");
        revertAllButton.setToolTipText("");
        revertAllButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                revertAllButtonActionPerformed(evt);
            }
        });

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
        jMenu3.add(jMenuItem3);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(heroDropdown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(advancedButton, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addComponent(revertAllButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(revertButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(playSoundButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(replaceButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(heroImageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 223, Short.MAX_VALUE)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 224, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(223, Short.MAX_VALUE)
                    .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(224, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(heroDropdown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(heroImageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(advancedButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(replaceButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(playSoundButton)
                    .addComponent(revertButton)
                    .addComponent(revertAllButton))
                .addGap(22, 22, 22))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 232, Short.MAX_VALUE)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 232, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(227, Short.MAX_VALUE)
                    .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(224, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void heroDropdownStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_heroDropdownStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED)
        {
            System.out.println(heroDropdown.getSelectedItem().toString());
            //populateSoundList((NamedHero) heroDropdown.getSelectedItem());
            populateSoundListAsTree((NamedHero) heroDropdown.getSelectedItem());
            jTree1.setRootVisible(false);
            jTree1.setShowsRootHandles(true);
            try
            {
                fillImageFrame((NamedHero) heroDropdown.getSelectedItem());
            }
            catch (IOException ex)
            {
                System.err.println(ex.getMessage());
            }
        }
    }//GEN-LAST:event_heroDropdownStateChanged

    private void replaceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_replaceButtonActionPerformed
        if (jTree1.getSelectionRows() != null
                && ((TreeNode) jTree1.getSelectionPath().getLastPathComponent()).isLeaf())
        {
            TreeNode selectedFile = ((TreeNode) jTree1.getSelectionPath().getLastPathComponent());

            //Prompt user to pick a file to replace with
            promptUserForNewFile(selectedFile.toString());
        }
    }//GEN-LAST:event_replaceButtonActionPerformed

    private void advancedButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_advancedButtonActionPerformed
    {//GEN-HEADEREND:event_advancedButtonActionPerformed
        //TODO:
        // Abstract this into its own function        
        //This is going to turn into the "Advanced" view, later
        String scriptPath = this.getScriptPathByHeroName(((NamedHero) heroDropdown.getSelectedItem()).getInternalName());
        ScriptParser parser = new ScriptParser(new File(Paths.get(scriptPath).toString()));
        TreeModel model = parser.getTreeModel();
        jTree1.setModel(model);
    }//GEN-LAST:event_advancedButtonActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem1ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem1ActionPerformed
        Window w = this;
        w.getToolkit().getSystemEventQueue().postEvent(new WindowEvent(w, WindowEvent.WINDOW_CLOSING));
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem2ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void playSoundButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_playSoundButtonActionPerformed
    {//GEN-HEADEREND:event_playSoundButtonActionPerformed
        if (jTree1.getSelectionRows().length != 0
                && ((TreeNode) jTree1.getSelectionPath().getLastPathComponent()).isLeaf())
        {
            DefaultMutableTreeNode selectedFile = ((DefaultMutableTreeNode) jTree1.getSelectionPath().getLastPathComponent());
            String waveString = selectedFile.getUserObject().toString();

            File soundFile = createSoundFileFromWaveString(waveString);
            String soundFilePath = soundFile.toURI().toString();
            media = new Media(soundFilePath);
            player = new MediaPlayer(media);
            player.play();
        }
    }//GEN-LAST:event_playSoundButtonActionPerformed

    //Delete scratch.wav and scratch.mp3 if they exist. Not 100% reliable
    //Should probably do this on load too, just to be nice
    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
        deleteScratchFiles();
    }//GEN-LAST:event_formWindowClosing

    //TODO: Clean this hellion up
    private void revertButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_revertButtonActionPerformed
    {//GEN-HEADEREND:event_revertButtonActionPerformed
        //TODO: See if we can abstract away some of this + promptUserForNewFile()'s functionality
        if (jTree1.getSelectionRows() != null
                && ((TreeNode) jTree1.getSelectionPath().getLastPathComponent()).isLeaf())
        {
            DefaultMutableTreeNode selectedNode = ((DefaultMutableTreeNode) jTree1.getSelectionPath().getLastPathComponent());
            String selectedWaveString = ((DefaultMutableTreeNode) selectedNode).getUserObject().toString();
            String selectedWaveParentString = ((DefaultMutableTreeNode) ((DefaultMutableTreeNode) selectedNode).getParent()).getUserObject().toString();
            selectedNode = (DefaultMutableTreeNode) this.getTreeNodeFromWavePath(selectedWaveString);

            //First go in and delete the sound in customSounds            
            int startIndex = -1;
            int endIndex = -1;
            if (selectedWaveString.contains("\"wave\""))
            {
                startIndex = nthOccurrence(selectedNode.getUserObject().toString(), '\"', 2);
                endIndex = nthOccurrence(selectedNode.getUserObject().toString(), '\"', 3);
            }
            else
            {
                startIndex = nthOccurrence(selectedNode.getUserObject().toString(), '\"', 1);
                endIndex = nthOccurrence(selectedNode.getUserObject().toString(), '\"', 2);
            }

            String waveSubstring = selectedWaveString.substring(startIndex, endIndex + 1);
            waveSubstring = waveSubstring.replace(")", "");
            waveSubstring = waveSubstring.replace("\"", "");
            File soundFileToDelete = new File(Paths.get(installDir + "\\dota\\" + waveSubstring).toString());
            if (soundFileToDelete.isFile())
            {
                soundFileToDelete.delete();
            }
            else
            {
                System.err.println("Cannot find and delete custom sound file " + waveSubstring);
            }

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
            String scriptDir = this.getScriptPathByHeroName(((NamedHero) heroDropdown.getSelectedItem()).getInternalName());
            scriptDir = scriptDir.replace(Paths.get(installDir + "\\dota\\").toString(), "");
            scriptDir = scriptDir.replace("\\", "/");                           //Match internal forward slashes
            scriptDir = scriptDir.substring(1);                                 //Cut off leading slash
            scriptDir = scriptDir.substring(0, scriptDir.lastIndexOf("/") + 1); //Cut off file extension            

            String scriptFileString = null;
            byte[] bytes = null;
            for (VPKEntry entry : vpk.getEntriesForDir(scriptDir))
            {
                if (entry.getName().contains("game_sounds_" + ((NamedHero) heroDropdown.getSelectedItem()).getInternalName()))
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
            scriptFileString = scriptFileString.substring(scriptFileString.indexOf(selectedWaveParentString), scriptFileString.length());
            ArrayList<String> internalWavePathsList = this.getWavePathListFromString(scriptFileString);
            String replacementString = internalWavePathsList.get(waveListIndex);

            selectedNode.setUserObject(replacementString);
            ScriptParser parser = new ScriptParser(this.currentHeroTreeModel);
            parser.writeModelToFile(this.getScriptPathByHeroName(((NamedHero) heroDropdown.getSelectedItem()).getInternalName()));

            //Modify the UI treeNode in addition to the backing TreeNode
            ((DefaultMutableTreeNode) jTree1.getLastSelectedPathComponent()).setUserObject(replacementString);
            ((DefaultTreeModel) jTree1.getModel()).nodeChanged(((DefaultMutableTreeNode) jTree1.getLastSelectedPathComponent()));
        }
    }//GEN-LAST:event_revertButtonActionPerformed

    private void revertAllButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_revertAllButtonActionPerformed
    {//GEN-HEADEREND:event_revertAllButtonActionPerformed
        //Delete existing script file
        String scriptFilePath = getScriptPathByHeroName(((NamedHero) heroDropdown.getSelectedItem()).getInternalName());
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
        populateSoundListAsTree((NamedHero) heroDropdown.getSelectedItem());
    }//GEN-LAST:event_revertAllButtonActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem4ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem4ActionPerformed
        //Change steam install loc   
        final UserPrefs prefs = new UserPrefs();
        SteamLocationForm steamForm = new SteamLocationForm(prefs, true);

        steamForm.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent arg0)
            {
                if (prefs.getSuccess())
                {
                    vpkDir = prefs.getVPKDir();
                    installDir = prefs.getInstallDir();
                }
            }
        });
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void populateDropdownBox()
    {
        heroDropdown.removeAllItems();
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
                if (vpk.isMultiChunk())
                {
                    //System.out.printf("%s:%s\n", entry.getFile().getName(), entry.getPath());
                }
                else
                {
                    //System.out.println(entry.getPath());
                }
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
            heroDropdown.addItem(tempHero);
        }

        //populateSoundList((NamedHero) heroDropdown.getSelectedItem());
        populateSoundListAsTree((NamedHero) heroDropdown.getSelectedItem());
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
    private void populateSoundListAsTree(NamedHero selectedHero)
    {
        Path scriptPath = Paths.get(this.installDir + "\\dota\\scripts\\game_sounds_heroes\\game_sounds_" + selectedHero.getInternalName() + ".txt");
        File scriptFile = new File(scriptPath.toString());
        //Defer writing script file to disk until we're sure it doesn't exist
        if (!scriptFile.isFile())
        {
            this.getHeroScriptFile(selectedHero.getInternalName());
        }
        ScriptParser parser = new ScriptParser(scriptPath.toFile());
        TreeModel scriptTree = parser.getTreeModel();
        this.currentHeroTreeModel = scriptTree;
        DefaultListModel scriptList = new DefaultListModel();
        TreeNode rootNode = (TreeNode) scriptTree.getRoot();
        int childCount = rootNode.getChildCount();

        TreeModel soundListTreeModel = new DefaultTreeModel(new DefaultMutableTreeNode("root"));
        ArrayList<String> wavePathsList = new ArrayList<String>();
        for (int i = 0; i < childCount; i++)
        {

            wavePathsList = this.getWavePathsAsList((TreeNode) scriptTree.getChild(rootNode, i));
            String nodeValue = scriptTree.getChild(rootNode, i).toString();
            DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(nodeValue);

            for (String s : wavePathsList)
            {
                DefaultMutableTreeNode tempNode = new DefaultMutableTreeNode(s);
                newNode.add(tempNode);
            }
            ((DefaultMutableTreeNode) soundListTreeModel.getRoot()).add(newNode);
        }

        jTree1.setModel(soundListTreeModel);
    }

    private void fillImageFrame(NamedHero selectedItem) throws IOException
    {
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

    public static int nthOccurrence(String str, char c, int n)
    {
        int pos = str.indexOf(c, 0);
        while (n-- > 0 && pos != -1)
        {
            pos = str.indexOf(c, pos + 1);
        }
        return pos;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton advancedButton;
    private javax.swing.JComboBox heroDropdown;
    private javax.swing.JLabel heroImageLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator7;
    private javax.swing.JTree jTree1;
    private javax.swing.JButton playSoundButton;
    private javax.swing.JButton replaceButton;
    private javax.swing.JButton revertAllButton;
    private javax.swing.JButton revertButton;
    // End of variables declaration//GEN-END:variables

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

    //TODO: Rewrite this to not write to disk unless we're actually making changes.
    private VPKEntry getHeroScriptFile(String heroName)
    {
        //String massaging to get the hero name from the filepath. Remove underscores because underscore use in game files for hero names is inconsistent.        
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

    private File promptUserForNewFile(String wavePath)
    {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("MP3s and WAVs", "mp3", "wav");
        chooser.setFileFilter(filter);

        int chooserRetVal = chooser.showOpenDialog(chooser);
        if (chooserRetVal == JFileChooser.APPROVE_OPTION)
        {
            DefaultMutableTreeNode selectedFile = (DefaultMutableTreeNode) getTreeNodeFromWavePath(wavePath);
            Path chosenFile = Paths.get(chooser.getSelectedFile().getAbsolutePath());
            Path destPath = Paths.get(installDir + "\\dota\\sound\\custom\\"
                    + ((NamedHero) heroDropdown.getSelectedItem()).getInternalName()
                    + "\\" + chosenFile.getFileName());

            try
            {
                //Copy in the new wav/mp3 file
                boolean success = new File(destPath.toString()).mkdirs();
                Files.copy(chosenFile, destPath, REPLACE_EXISTING);

                //Replace the wavestring in the treenode
                String waveString = selectedFile.getUserObject().toString();
                int startIndex = -1;
                int endIndex = -1;
                if (waveString.contains("\"wave\""))
                {
                    startIndex = nthOccurrence(selectedFile.getUserObject().toString(), '\"', 2);
                    endIndex = nthOccurrence(selectedFile.getUserObject().toString(), '\"', 3);
                }
                else
                {
                    startIndex = nthOccurrence(selectedFile.getUserObject().toString(), '\"', 0);
                    endIndex = nthOccurrence(selectedFile.getUserObject().toString(), '\"', 1);
                }


                String waveSubstring = waveString.substring(startIndex, endIndex + 1);
                waveString = waveString.replace(waveSubstring, "\")custom/"
                        + ((NamedHero) heroDropdown.getSelectedItem()).getInternalName()
                        + "/" + chosenFile.getFileName() + "\"");
                selectedFile.setUserObject(waveString);

                //Parse the modified TreeModel into a script file, and write the file to disk.
                ScriptParser parser = new ScriptParser(this.currentHeroTreeModel);

                //This line can probably be replaced with globally-available data
                String scriptString = this.getScriptPathByHeroName(((NamedHero) heroDropdown.getSelectedItem()).getInternalName());

                Path scriptPath = Paths.get((scriptString));
                parser.writeModelToFile(scriptPath.toString());

                //Update UI bits
                populateSoundListAsTree((NamedHero) heroDropdown.getSelectedItem());

                JOptionPane.showMessageDialog(this, "Sound file successfully replaced.");
            }
            catch (IOException ex)
            {
                System.err.println(ex);
            }
        }
        return null;
    }

    private ArrayList<String> getWavePathsAsList(TreeNode selectedFile)
    {
        ArrayList<String> wavePathsList = new ArrayList<String>();
        Enumeration e = selectedFile.children();
        while (e.hasMoreElements())
        {
            Object currentElement = e.nextElement();

            //If a soundfile has multiple possible wavefiles
            if (currentElement.toString().contains("\"rndwave\""))
            {
                Enumeration innerE = ((TreeNode) currentElement).children();
                while (innerE.hasMoreElements())
                {
                    Object currentInnerElement = innerE.nextElement();
                    if (currentInnerElement.toString().contains("\"wave\"") || currentInnerElement.toString().contains(".wav") || currentInnerElement.toString().contains(".mp3"))
                    {
                        //Maybe do some string massaging here before we just hand it back
                        wavePathsList.add(((TreeNode) currentInnerElement).toString());
                    }
                }
            }
            //If it only has one
            else if (currentElement.toString().contains("\"wave\""))
            {
                wavePathsList.add(((TreeNode) currentElement).toString());
            }
        }
        return wavePathsList;
    }

    private ArrayList<String> getWavePathListFromString(String scriptString)
    {
        ArrayList<String> wavePathsList = new ArrayList<String>();
        BufferedReader buf = new BufferedReader(new StringReader(scriptString));
        String line = null;
        try
        {
            while ((line = buf.readLine()) != null)
            {
                //Stop reading after we're done with the wave paths               
                if (line.contains("soundentry"))
                {
                    break;
                }

                if (line.contains("\"wave\"") || line.contains(".wav") || line.contains(".mp3"))
                {
                    wavePathsList.add(line);
                }
            }
            return wavePathsList;
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    private TreeNode getTreeNodeFromWavePath(String wavePath)
    {
        TreeModel model = this.currentHeroTreeModel;

        TreeNode root = (TreeNode) model.getRoot();
        for (Enumeration e = ((DefaultMutableTreeNode) root).breadthFirstEnumeration(); e.hasMoreElements() && root != null;)
        {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
            if (node.toString().contains(wavePath))
            {
                return node;
            }
        }
        return null;
    }

    private File createSoundFileFromWaveString(String waveString)
    {
        File file = new File(vpkDir);
        VPKArchive vpk = new VPKArchive();
        File entryFile = null;

        String waveSubstring = "";
        int startIndex = -1;
        int endIndex = -1;
        if (waveString.contains("\"wave\""))
        {
            startIndex = nthOccurrence(waveString, '\"', 2);
            endIndex = nthOccurrence(waveString, '\"', 3);
        }
        //For weird special cases where the "wave" part of the string is missing, i.e. Treant's Overgrowth.Target spell
        else
        {
            startIndex = nthOccurrence(waveString, '\"', 0);
            endIndex = nthOccurrence(waveString, '\"', 1);
        }

        waveSubstring = waveString.substring(startIndex, endIndex + 1);
        waveSubstring = waveSubstring.replace(")", "");
        waveSubstring = waveSubstring.replace("\"", "");
        waveSubstring = waveSubstring.replace("\\", "/");

        if (!waveString.contains("custom"))
        {
            try
            {
                vpk.load(file);
            }
            catch (Exception ex)
            {
                System.err.println("Can't open archive: " + ex.getMessage());
            }

            for (VPKEntry entry : vpk.getEntries())
            {
                if (entry.getPath().contains(waveSubstring))
                {
                    entryFile = entry.getType().contains("wav")
                            ? new File(Paths.get(System.getProperty("user.dir") + "\\scratch\\scratch.wav").toString())
                            : new File(Paths.get(System.getProperty("user.dir") + "\\scratch\\scratch.mp3").toString());

                    try (FileChannel fc = FileUtils.openOutputStream(entryFile).getChannel())
                    {
                        fc.write(entry.getData());
                    }
                    catch (IOException ex)
                    {
                        ex.printStackTrace();
                    }
                    break;
                }
            }
            return entryFile;
        }
        else    //If it's NOT stored in the VPK, it's on the local filesys
        {
            entryFile = new File(Paths.get(installDir + "\\dota\\sound\\" + waveSubstring).toString());
            return entryFile;
        }
    }

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
}
