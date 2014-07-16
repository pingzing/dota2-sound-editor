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
import java.util.Properties;

public class CacheManager
{
    private static CacheManager cacheValidatorInstance = null;
    Properties scriptsProperties = new Properties();
    final String SCRIPTS_FILE_NAME = "scripts.properties";
    final String PATH_SEP = "_path";

    private CacheManager()
    {
        if (new File(SCRIPTS_FILE_NAME).isFile())
        {
            try
            {
                scriptsProperties.loadFromXML(new FileInputStream(SCRIPTS_FILE_NAME));
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

    public String putScript(String scriptKey, String scriptPath, long crc)
    {
        String oldCrc = scriptsProperties.setProperty(scriptKey, ((Long) crc).toString()).toString();
        scriptsProperties.setProperty(scriptKey + PATH_SEP, scriptPath);
        return oldCrc;
    }

    public String removeScript(String scriptKey)
    {
        Object removed = scriptsProperties.remove(scriptKey);
        if (removed != null)
        {
            scriptsProperties.remove(scriptKey + PATH_SEP);
        }
        return removed.toString();
    }

    public boolean isUpToDate(String scriptKey) throws ScriptNotFoundException
    {
        long cachedCrc;
        Object value = scriptsProperties.getProperty(scriptKey);
        if (value == null)
        {
            throw new ScriptNotFoundException(scriptKey + " has no CRC value cached.");
        }
        cachedCrc = Long.parseLong(value.toString());
        long internalCrc = getInternalCrc(scriptKey);
        return cachedCrc == internalCrc;
    }

    private long getInternalCrc(String scriptKey)
    {
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
        }
        catch (ScriptNotFoundException | VPKException ex)
        {
            System.out.println("Failed to load VPK in CacheManager. Details: " + ex.getMessage());
            ex.printStackTrace();
        }
        catch (IOException ioe)
        {
            System.out.println("Failed to load PVK in CacheManager. Details: " + ioe.getMessage());
            ioe.printStackTrace();
        }
        finally
        {
            return internalCrc;
        }
    }

    public void saveCache()
    {
        try
        {
            URL url = ClassLoader.getSystemResource("/dotaSoundEditor/resources/" + SCRIPTS_FILE_NAME);
            OutputStream os = new FileOutputStream(new File(url.toURI()));
            scriptsProperties.storeToXML(os, null);
        }
        catch (URISyntaxException use)
        {
            System.out.println("Unable to convert " + SCRIPTS_FILE_NAME + " filename into URI.");
            use.printStackTrace();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }
}