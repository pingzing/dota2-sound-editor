
//Features to add:
// - Get sound length, display along bottom of window
// - Make "Play" button shift to "Stop" button when sound is playing
// - Fix ScriptParser's functions to not automatically fire when its constructors are called.
// - Investigate scanning common install paths for dota before asking the user
// - Rewrite image-extraction and loading code to avoid disk IO every time we start the program
// - Look into abstracting more methods into EditorPanel by adding a "scriptFilePath" instance member that each child implements
// - Expand "Advanced" functionality to allow for adding of nodes. (Also add a right-click menu with Add, Edit, Delete)
// - Localization?
package dotaSoundEditor;

import dotaSoundEditor.Controls.SoundEditorMainForm;
import java.lang.Thread.UncaughtExceptionHandler;
import javax.swing.JDialog;
import javax.swing.UIManager;

public class Main
{   
    public static void main(String args[]) throws Exception
    {                
        javax.swing.UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());                        
        UserPrefs prefs = new UserPrefs();
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
            SoundEditorMainForm mainForm = new SoundEditorMainForm(vpkDir, installDir);
        }        
    }
}
