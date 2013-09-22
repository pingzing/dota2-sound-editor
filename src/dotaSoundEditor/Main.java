/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//Features to add:
// - Ability to revert sounds to default
// - *OPTIONAL* Make program not write out portraits to folder 
// - Implement taskbar at the top
// - Write a readme
// - Get sound length
// - Change Replace to something less scary
// - Make more obvious that sound names need to be expanded

package dotaSoundEditor;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.Thread.UncaughtExceptionHandler;
import javax.swing.UIManager;

/**
 *
 * @author Image 17
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
            final SteamLocationForm locForm = new SteamLocationForm(prefs);
            Thread t = new Thread()
            {
                public void run()
                {
                    synchronized (lock)
                    {

                        while (locForm.isVisible())
                        {
                            try
                            {
                                lock.wait();
                            }
                            catch (InterruptedException e)
                            {
                                e.printStackTrace();
                            }
                        }
                        System.out.println("Location form launched.");
                    }
                }
            };
            t.start();

            locForm.addWindowListener(new WindowAdapter()
            {
                @Override
                public void windowClosing(WindowEvent arg0)
                {
                    synchronized (lock)
                    {
                        locForm.setVisible(false);
                        lock.notify();
                    }
                }
            });
            t.join();
        }

        String vpkDir = prefs.getVPKDir();
        String installDir = prefs.getInstallDir();

        if (!(vpkDir.equals("")) && !(installDir.equals("")))
        {
            SoundEditorMainForm mainForm = new SoundEditorMainForm(vpkDir, installDir);
        }
    }
}

