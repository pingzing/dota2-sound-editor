//Features to add:
//Roadmap to next release:    
// - Hero VO
// - Rewrite prefs to not write to the registry. Just a local, serialized dictionary is a
//    waaay better choice
// - Write a migration method that removes old registry keys.
// - Look into abstracting more methods into EditorPanel by adding a 
//    "scriptFilePath" instance member that each child implements
// - Generally refactor the panels. There's a lot of improvement that can 
//     happen in the vpk-handling sections.
// - Try to auto-find Dota install dir via Registry (if Windows). Might be at: Computer\HKEY_LOCAL_MACHINE\SOFTWARE\Wow6432Node\Microsoft\Windows\CurrentVersion\Uninstall\Steam App 570
// - Or: Computer\HKEY_LOCAL_MACHINE\SOFTWARE\Wow6432Node\Valve\Steam
// - Automatically fill in the relevant console launch args for Dota 2. The file is in the same place as autoexec.cfg. (maybe)
// - Find way to play sound files from memory instead of writing to local 
//     filesys, then playing. ByteBuffer to byte[] to File, maybe?

// - Auto-updater / update notifier.
// - Make multi-pressing play impossible
// - Export/import functionality, so users can just export a lump of sounds and give them to friends
// - Make Advanced button handling less spaghetti-codey. Events! Something!
// - Handle Advanced mode changes actually applying to tree (*note: make this 
//     COMPLETELY BREAK automatic cache invalidation. I ain't regenerating dis shit)
// - UI sounds
// - Fix the libraries and dependencies to actually get into source control, so somebody cloning the repo can build right away
// - Get sound length, display along bottom of window
// - Fix ScriptParser's functions to not automatically fire when its constructors are called.
// - Investigate scanning common install paths for Dota before asking the user
// - Rewrite image-extraction and loading code to avoid disk IO every time we start the program. Check flash3's modified-date in vpk. if diff, update!
// - Expand "Advanced" functionality to allow for adding of nodes. (Also add a right-click menu with Add, Edit, Delete)
// - Turn the Dropdown and Image handling code into Dropdownnable and Imageable interfaces?
// - Javadoc. Need to document things!
// - Localization? Investigate how Java handles localized strings

/***
 * Features completed for 1.3:
 *      - Fixed missing icons and incorrect names in items panel.
 *      - Fixed tree collapsing every time sound got replaced.
 *      - Fixed MP3s never reverting Play/Stop button back to Play mode.
 */
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

        String vpkPath = prefs.getVPKPath();
        String installDir = prefs.getInstallDir();

        if (!(vpkPath.equals("")) && !(installDir.equals("")))
        {
            try
            {
                //TODO: Don't automatically run the mainForm in its constructor. Make a .run() method.
                SoundEditorMainForm mainForm = new SoundEditorMainForm(vpkPath, installDir);
            }
            //This should cut down on at least 90% of support emails...
            //TODO: Look into whether this belongs i_n Handler.java
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
