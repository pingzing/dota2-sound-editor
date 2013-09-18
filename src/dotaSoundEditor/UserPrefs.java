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
 * @author Image 17
 */
class UserPrefs
{

    Preferences prefs = Preferences.userNodeForPackage(dotaSoundEditor.UserPrefs.class);
    final String DOTA_DIR_PREF_NAME = "dota_install_dir";
    final String MAIN_VPK_PREF_NAME = "main_vpk_dir";
    String installDir = "";
    String vpkDir = "";
    boolean success = false;

    public UserPrefs()
    {        
    }

    public String getInstallDir()
    {
        boolean exists = prefs.get(DOTA_DIR_PREF_NAME, null) != null;
        if (exists)
        {
            this.installDir = prefs.get(DOTA_DIR_PREF_NAME, null);            
        }       
        return installDir;
    }

    public String getVPKDir()
    {
        boolean exists = prefs.get(MAIN_VPK_PREF_NAME, null) != null;
        if (exists)
        {
            this.vpkDir = prefs.get(MAIN_VPK_PREF_NAME, null);
        }        
        return this.vpkDir;
    }

    public boolean getSuccess()
    {
        return this.success;
    }
    
    public void setInstallKeys()
    {
        prefs.put(DOTA_DIR_PREF_NAME, installDir);
        prefs.put(MAIN_VPK_PREF_NAME, vpkDir);         
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
        Path filePath = Paths.get(_installDir + "//dota//");
        Path vpkPath = Paths.get(filePath + "//pak01_dir.vpk//");
        File vpkFile = new File(vpkPath.toString());
        if(vpkFile.exists())
        {
            this.vpkDir = vpkPath.toString();
            this.success = true;
        }
    }
}
