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
package dotaSoundEditor.Helpers;

import dotaSoundEditor.UserPrefs;
import info.ata4.vpk.VPKArchive;
import info.ata4.vpk.VPKEntry;
import info.ata4.vpk.VPKException;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Properties;

public class CacheManager
{
    private Properties scriptsProperties = new Properties();
    private HashMap<String, Long> sessionCrcs = new HashMap<>();
    private VPKEntry cachedEntry = null;
    private String relativePath;
    private final String SCRIPTS_FILE_NAME = "scripts.properties";
    private final String PATH_SEP = "_path";
    private final String SESSION_CRC_SEP = "_latestcrc";

    public CacheManager()
    {
        try
        {
            relativePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();    
            relativePath = relativePath.substring(0, relativePath.lastIndexOf("/"));
            relativePath = relativePath.replaceAll("%20"," ");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return;
        }
        if(System.getProperty("os.name").toUpperCase().contains("WINDOWS"))
        {
            //Kill the leading slash for Windows systems, windows paths don't start with /
            relativePath = relativePath.substring(1);                     
        }
        File propsFile = Paths.get(relativePath, SCRIPTS_FILE_NAME).toFile();
        try
        {
            if (!propsFile.isFile())
            {
                propsFile.getParentFile().mkdirs();
                propsFile.createNewFile();
            }
            FileInputStream fis = new FileInputStream(propsFile);
            try
            {
                scriptsProperties.load(fis);
            }
            catch (IOException ex)
            {
                System.out.println("Could not load cache file.");
                ex.printStackTrace();
            }
        }
        catch (IOException ex)
        {
            System.out.println("Could not load cache file.");
            ex.printStackTrace();
        }
    }

    /**
     * @param scriptKey The filename of the script file. This will be used as
     * the key of the script's CRC32 in the cache.
     * @param scriptPath The vpk-internal file path of the script file. (i.e.
     * dota/scripts/game_sounds_heroes/game_sounds_lina.txt)
     * @return The old CRC value of the script that was just updated, or null if
     * it was just added.
     */
    public String putScript(String scriptKey, String scriptPath, long crc)
    {
        String oldCrc = scriptsProperties.setProperty(scriptKey, Long.toString(crc)) == null ? null
                : scriptsProperties.setProperty(scriptKey, Long.toString(crc)).toString();
        putScriptPath(scriptKey, scriptPath);
        sessionCrcs.put(scriptKey + SESSION_CRC_SEP, crc);
        return oldCrc;
    }

    public void putScriptPath(String scriptKey, String scriptPath)
    {
        scriptsProperties.setProperty(scriptKey + PATH_SEP, scriptPath);
    }

    public String removeScript(String scriptKey)
    {
        Object removed = scriptsProperties.remove(scriptKey);
        if (removed != null)
        {
            scriptsProperties.remove(scriptKey + PATH_SEP);
            sessionCrcs.remove(scriptKey + SESSION_CRC_SEP);
        }
        return removed.toString();
    }

    /**
     * @param scriptKey The filename of the script whose CRC you want.
     * @param internalCrc The CRC of the up-to-date, internal script.
     * @return True if the CRC saved in cache is equal to the internal CRC.
     * False otherwise.
     *
     */
    public boolean isUpToDate(String scriptKey, long internalCrc)
    {
        long cachedCrc;
        Object value = scriptsProperties.getProperty(scriptKey);
        if (value == null)
        {
            return false;
        }
        cachedCrc = Long.parseLong(value.toString());
        return cachedCrc == internalCrc;
    }

    /**
     * @param scriptKey The filename of the script whose CRC you want.
     * @return The CRC value of the session-cached script. These values are
     * retrieved and stored the first time a script is validated against cache,
     * and cleared again on program exit. Returns 0 on failure.
     *
     */
    public long getSessionCrc(String scriptKey)
    {
        //check local table first
        if (sessionCrcs.containsKey(scriptKey + SESSION_CRC_SEP))
        {
            return sessionCrcs.get(scriptKey + SESSION_CRC_SEP);
        }
        String vpkPath = UserPrefs.getInstance().getVPKPath();
        VPKArchive vpk = new VPKArchive();
        long internalCrc = 0;
        try
        {
            vpk.load(new File(vpkPath));
            String scriptPath = scriptsProperties.getProperty(scriptKey + PATH_SEP);
            VPKEntry scriptEntry = vpk.getEntry(scriptPath);
            if (scriptEntry != null)
            {
                internalCrc = scriptEntry.getCRC32();
            }
            else
            {
                throw new ScriptNotFoundException("Unable to locate VPKEntry at " + scriptPath);
            }
            cachedEntry = scriptEntry;
        }
        catch (ScriptNotFoundException | VPKException ex)
        {
            System.out.println("Failed to load VPK in CacheManager. Details: " + ex.getMessage());
            ex.printStackTrace();
        }
        catch (IOException ioe)
        {
            System.out.println("Failed to load VPK in CacheManager. Details: " + ioe.getMessage());
            ioe.printStackTrace();
        }
        finally
        {
            sessionCrcs.put(scriptKey + SESSION_CRC_SEP, internalCrc);
            return internalCrc;
        }
    }

    /**
     * Returns the VPKEntry that corresponds to file whose CRC32 was most recently checked in 
     * getSessionCrc.
     * @return The VPKEntry of the most recently looked-up file.
     */
    public VPKEntry getCachedVpkEntry()
    {
        return cachedEntry;
    }

    public void saveCache() throws IOException, URISyntaxException, SecurityException, NullPointerException
    {        
        File outFile = Paths.get(relativePath, SCRIPTS_FILE_NAME).toFile();
        OutputStream os = new FileOutputStream(outFile);
        scriptsProperties.store(os, null);
    }
}