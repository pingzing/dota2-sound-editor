/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//Features to add:
// - *OPTIONAL* Make program not write out portraits to folder 
// - Implement About dialog
// - Write a readme
// - Get sound length
// - Change Replace to something less scary
// - Make more obvious that sound names need to be expanded
// - Fix ScriptParser's functions to not automatically fire when its constructors are called.
package dotaSoundEditor;

import java.lang.Thread.UncaughtExceptionHandler;
import javax.swing.JDialog;
import javax.swing.UIManager;

/**
 *
 * @author
 * Image
 * 17
 */
public class Main
{

    private static Object lock = new Object();

    public static void main(String args[]) throws Exception
    {
        javax.swing.UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        UserPrefs prefs = new UserPrefs();
        Handler handler = new Handler();
        Thread.setDefaultUncaughtExceptionHandler((UncaughtExceptionHandler) handler);
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
