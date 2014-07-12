//Features to add:
// - Handle Advanced mode changes actually applying to tree
// - Hero VO
// - Music
// - Announcers??? maybe not...
// - Get sound length, display along bottom of 
// - Improve cache invalidation to preserve users' changes
// - Find way to play sound files from memory instead of writing to local filesys, then playing. So slow!
// - Fix ScriptParser's functions to not automatically fire when its constructors are called.
// - Investigate scanning common install paths for dota before asking the user
// - Rewrite image-extraction and loading code to avoid disk IO every time we start the program. Check flash3's modified-date in vpk. if diff, update!
// - Look into abstracting more methods into EditorPanel by adding a "scriptFilePath" instance member that each child implements
//   - This one may not be possible with the MusicPanel. It's scripts are scattered all over the place. A list of scriptFilePaths, maybe?
// - Generally refactor the panels. There's a lot of improvement that can happen in the vpk-handling sections.
// - Expand "Advanced" functionality to allow for adding of nodes. (Also add a right-click menu with Add, Edit, Delete)
// - Javadoc. Need to document things!
// - Localization?
package dotaSoundEditor;

import dotaSoundEditor.Controls.SteamLocationPanel;
import dotaSoundEditor.Controls.SoundEditorMainForm;
import java.lang.Thread.UncaughtExceptionHandler;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class Main
{   
    private static UserPrefs prefs = UserPrefs.getInstance();
    public static void main(String args[]) throws Exception
    {                
        javax.swing.UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());                                
        Handler handler = new Handler();
        Thread.setDefaultUncaughtExceptionHandler((UncaughtExceptionHandler) handler);
        
        //Need to find dota 2 install dir
        if (prefs.getInstallDir().equals(""))
        {            
            JDialog locationCheckDialog = new JDialog();
            locationCheckDialog.setModal(true);
            locationCheckDialog.setAlwaysOnTop(true);
            locationCheckDialog.setTitle("Locate Dota 2");
            locationCheckDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            SteamLocationPanel panel = new SteamLocationPanel(prefs, false, locationCheckDialog);
            locationCheckDialog.add(panel);
            locationCheckDialog.setSize(panel.getPreferredSize());
            locationCheckDialog.setVisible(true);
        }

        String vpkDir = prefs.getVPKDir();
        String installDir = prefs.getInstallDir();

        if (!(vpkDir.equals("")) && !(installDir.equals("")))
        {
            try
            {
                SoundEditorMainForm mainForm = new SoundEditorMainForm(vpkDir, installDir);
            }
            //This should cut down on at least 90% of support emails...
            //TODO: Look into whether this belongs in Handler.java
            catch(java.lang.NoClassDefFoundError nce)
            {
                JFrame errorFrame = new JFrame();                      
                if(nce.getMessage().contains("info/ata4/vpk/VPKArchive"))
                {
                    JOptionPane.showMessageDialog(errorFrame, "<html><body>Couldn't find a required file. Did you extract the \"lib\" folder as well?"
                            + "<br>Missing file: " + nce.getMessage()+"</body></html>");                      
                }
                else
                {
                    JOptionPane.showMessageDialog(errorFrame, "<html><body>A required file could not be found."
                            + "<br>Missing file: "+ nce.getMessage() +"</body><html>");
                }              
                errorFrame.dispose();
                System.exit(0);
            }            
        }        
    }
}
