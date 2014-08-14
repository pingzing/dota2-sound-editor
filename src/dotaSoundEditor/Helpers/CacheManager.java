package dotaSoundEditor.Helpers;

import dotaSoundEditor.UserPrefs;
import info.ata4.vpk.VPKArchive;
import info.ata4.vpk.VPKEntry;
import info.ata4.vpk.VPKException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Properties;

public class CacheManager
{
    //TODO: Investigate turning this from a Singleton into something that's
    //just available in the EditorPanel base class

    private static CacheManager cacheValidatorInstance = null;
    private Properties scriptsProperties = new Properties();
    private HashMap<String, Long> sessionCrcs = new HashMap<>();
    private VPKEntry cachedEntry = null;
    private final String SCRIPTS_FILE_NAME = "scripts.properties";
    private final String PATH_SEP = "_path";
    private final String SESSION_CRC_SEP = "_latestcrc";

    private CacheManager()
    {
        String savePath = "dotaSoundEditor/resources/" + SCRIPTS_FILE_NAME;
        URL url = ClassLoader.getSystemResource(savePath);
        File cacheFile = null;
        try
        {
            cacheFile = new File(url.toURI());
        }
        catch(URISyntaxException ex)
        {
            System.out.println("URL " + url.toString() + " could not be converted"
                    + "into a URI.");
            ex.printStackTrace();
        }        
        if (cacheFile != null && cacheFile.isFile())
        {
            try
            {
                scriptsProperties.load(new FileInputStream(cacheFile));
            }
            catch (FileNotFoundException fnfe)
            {
                System.out.println("File not found: " + SCRIPTS_FILE_NAME + ". This should NEVER happen. What gives?");
                fnfe.printStackTrace();
            }
            catch (IOException ioe)
            {
                System.out.println("Failed to load cache from " + SCRIPTS_FILE_NAME);
                ioe.printStackTrace();
            }
        }
    }

    public static synchronized CacheManager getInstance()
    {
        if (cacheValidatorInstance == null)
        {
            cacheValidatorInstance = new CacheManager();
        }
        return cacheValidatorInstance;
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

    public VPKEntry getCachedVpkEntry()
    {
        return cachedEntry;
    }

    public void saveCache() throws IOException, URISyntaxException, SecurityException, NullPointerException
    {
        String savePath = "dotaSoundEditor/resources/" + SCRIPTS_FILE_NAME;
        URL url = ClassLoader.getSystemResource(savePath);
        OutputStream os = new FileOutputStream(new File(url.toURI()));
        scriptsProperties.store(os, null);
    }
}