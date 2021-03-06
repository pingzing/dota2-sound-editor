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

//Features to add:
//Roadmap to next release:
// - Testing!
// - Update readme
// - Update version number
//Pushed to 1.3.1:
// - Rewrite prefs to not write to the registry. Just a local, serialized dictionary is a
// waaay better choice
// - Write a migration method that removes old registry keys.
// - Try to auto-find Dota install dir via Registry (if Windows). Might be at: Computer\HKEY_LOCAL_MACHINE\SOFTWARE\Wow6432Node\Microsoft\Windows\CurrentVersion\Uninstall\Steam App 570
// - Or: Computer\HKEY_LOCAL_MACHINE\SOFTWARE\Wow6432Node\Valve\Steam
// - Fix crash on scrolling list too fast
// - Find way to play sound files from memory instead of writing to local
// filesys, then playing. ByteBuffer to byte[] to File, maybe?
// PROGRESS: byte[] doesn't work so well, because apparently Java Audio can't interpret the header data
// - Auto-updater / update notifier.
// - Make multi-pressing play impossible
// - Export/import functionality, so users can just export a lump of sounds and give them to friends
// - Make Advanced button handling less spaghetti-codey. Events! Something!
// - Handle Advanced mode changes actually applying to tree (*note: make this
// COMPLETELY BREAK automatic cache invalidation. I ain't regenerating dis shit)
// - Expand "Advanced" functionality to allow for adding of nodes. (Also add a right-click menu with Add, Edit, Delete)
// - UI sounds
// - Get sound length, display along bottom of window
// - Fix ScriptParser's functions to not automatically fire when its constructors are called.
// - Investigate scanning common install paths for Dota before asking the user
// - Rewrite image-extraction and loading code to avoid disk IO every time we start the program. Check flash3's modified-date in vpk. if diff, update!
// - Turn the Dropdown and Image handling code into Dropdownnable and Imageable interfaces?
// - Javadoc. Need to document things!
// - Localization? Investigate how Java handles localized strings
/***
* Features completed for 1.3:
* - Added voice editing!
* - Fixed missing icons and incorrect names in items panel.
* - Fixed tree collapsing every time sound got replaced.
* - Fix crash on attempting to Replace with nothing selected.
* - Fixed MP3s never reverting Play/Stop button back to Play mode.
* - Fix item sounds sometimes not being written correctly.
* - Lots of behind-the-scenes rewrites.
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
