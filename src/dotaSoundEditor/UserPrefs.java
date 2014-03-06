/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dotaSoundEditor;

import java.io.File;
import java.nio.file.*;
import java.util.prefs.*;
import javax.swing.JFileChooser;

/**
 *
 * @author
 * Image
 * 17
 */
public class UserPrefs
{

    Preferences prefs = Preferences.userNodeForPackage(dotaSoundEditor.UserPrefs.class);
    private String dotaDirPrefName = "dota_install_dir";
    private String mainVPKPrefName = "main_vpk_dir";
    private String installDir = "";
    private String vpkDir = "";
    private boolean success = false;

    public UserPrefs()
    {
    }

    public String getInstallDir()
    {
        boolean exists = prefs.get(dotaDirPrefName, null) != null;
        if (exists)
        {
            this.installDir = prefs.get(dotaDirPrefName, null);
        }
        return installDir;
    }

    public String getVPKDir()
    {
        boolean exists = prefs.get(mainVPKPrefName, null) != null;
        if (exists)
        {
            this.vpkDir = prefs.get(mainVPKPrefName, null);
        }
        return this.vpkDir;
    }

    public boolean getSuccess()
    {
        return this.success;
    }

    public void setInstallKeys()
    {
        prefs.put(dotaDirPrefName, installDir);
        prefs.put(mainVPKPrefName, vpkDir);
    }

    public void setInstallDir()
    {
        JFileChooser chooser = new JFileChooser("Locate your Dota 2 install directory.");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int chooserRetVal = chooser.showOpenDialog(chooser);
        if (chooserRetVal == JFileChooser.APPROVE_OPTION)
        {
            System.out.println(chooser.getSelectedFile().getAbsolutePath() + " was chosen.");
            this.installDir = chooser.getSelectedFile().getAbsolutePath();
            //Validate that the user's choice is actually the dota directory
            Path totalFilePath = Paths.get(installDir + "//dota//");
            Path vpkPath = Paths.get(totalFilePath + "//pak01_dir.vpk//");
            System.out.println("Searching in " + totalFilePath.toString());
            File vpkFile = new File(vpkPath.toString());
            if (vpkFile.exists())
            {
                this.vpkDir = vpkPath.toString();
                this.success = true;
            }
        }
    }

    public void setInstallDir(String _installDir)
    {
        this.installDir = _installDir;
        //Validate
        try
        {
            Path filePath = Paths.get(_installDir + "//dota//");
            Path vpkPath = Paths.get(filePath + "//pak01_dir.vpk//");
            File vpkFile = new File(vpkPath.toString());
            if (vpkFile.exists())
            {
                this.vpkDir = vpkPath.toString();
                this.success = true;
            }
        }
        catch (java.nio.file.InvalidPathException ex)
        {
            System.err.println("Unable to find Dota directory from given path: " + _installDir);
            this.success = false;
        }
    }
}
