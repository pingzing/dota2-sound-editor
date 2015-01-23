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
package dotaSoundEditor;

import java.io.File;
import java.nio.file.*;
import java.util.prefs.*;
import javax.swing.JFileChooser;

public final class UserPrefs
{
    private static UserPrefs userPrefsInstance = null;
    Preferences prefs = Preferences.userNodeForPackage(dotaSoundEditor.UserPrefs.class);
    private String dotaDirPrefName = "dota_install_dir";
    private String mainVPKPrefName = "main_vpk_dir";
    private String installDir = "";
    private String vpkPath = "";
    private String currentWorkingDirectoryPrefName = "working_dir";
    private boolean changeInstallDirSuccess = false;

    private UserPrefs() {        }    
    
    //Gogo Singleton pattern
    public static synchronized UserPrefs getInstance()
    {
        if(userPrefsInstance == null)
        {
            userPrefsInstance = new UserPrefs();
        }
        return userPrefsInstance;
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

    public String getVPKPath()
    {
        boolean exists = prefs.get(mainVPKPrefName, null) != null;
        if (exists)
        {
            this.vpkPath = prefs.get(mainVPKPrefName, null);
        }
        return this.vpkPath;
    }

    public boolean getInstallDirSuccess()
    {
        return this.changeInstallDirSuccess;
    }

    public void setInstallKeys()
    {
        prefs.put(dotaDirPrefName, installDir);
        prefs.put(mainVPKPrefName, vpkPath);
    }

    public void setInstallDir()
    {
        JFileChooser chooser = new JFileChooser("Locate your Dota 2 install directory.");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int chooserRetVal = chooser.showOpenDialog(chooser);
        if (chooserRetVal == JFileChooser.APPROVE_OPTION)
        {
            this.changeInstallDirSuccess = false;
            System.out.println(chooser.getSelectedFile().getAbsolutePath() + " was chosen.");
            this.installDir = chooser.getSelectedFile().getAbsolutePath();
            //Validate that the user's choice is actually the dota directory
            Path totalFilePath = Paths.get(installDir + "//dota//");
            Path vpkPath = Paths.get(totalFilePath + "//pak01_dir.vpk//");
            System.out.println("Searching in " + totalFilePath.toString());
            File vpkFile = new File(vpkPath.toString());
            if (vpkFile.exists())
            {
                this.vpkPath = vpkPath.toString();
                this.changeInstallDirSuccess = true;
            }
        }
    }

    public void setInstallDir(String _installDir)
    {
        this.installDir = _installDir;
        this.changeInstallDirSuccess = false;
        //Validate
        try
        {
            Path filePath = Paths.get(_installDir + "//dota//");
            Path vpkPath = Paths.get(filePath + "//pak01_dir.vpk//");
            File vpkFile = new File(vpkPath.toString());
            if (vpkFile.exists())
            {
                this.vpkPath = vpkPath.toString();
                this.changeInstallDirSuccess = true;
            }
        }
        catch (java.nio.file.InvalidPathException ex)
        {
            System.err.println("Unable to find Dota directory from given path: " + _installDir);
            this.changeInstallDirSuccess = false;
        }
    }
    
    //TODO: Refactor these two so we're not constantly pulling in and out of the registry. Should just hold value in memory and only grab from/store
    //to on load and on exit.
    public String getWorkingDirectory()
    {
        boolean exists = prefs.get(this.currentWorkingDirectoryPrefName, null) != null;
        if(exists)
        {
            return prefs.get(this.currentWorkingDirectoryPrefName, null);
        }
        return "";
    }
    
    public void setWorkingDirectory(String _workingDirectory)
    {
        prefs.put(this.currentWorkingDirectoryPrefName, _workingDirectory);
    }    
}
