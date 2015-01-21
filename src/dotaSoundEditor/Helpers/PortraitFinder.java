package dotaSoundEditor.Helpers;

import info.ata4.vpk.VPKArchive;
import info.ata4.vpk.VPKEntry;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.TreeMap;
import javax.imageio.ImageIO;
import org.apache.commons.io.FileUtils;
/**
 *
 * @author
 * Image
 * 17
 */
public class PortraitFinder
{

    private String fileName;   
    private TreeMap<String, BufferedImage> portraitMap = new TreeMap<>();

    public PortraitFinder(String _fileName)
    {
        fileName = _fileName;        
    }
    
    public void buildPortraits()
    {
        File file = new File(fileName);
        VPKArchive vpk = new VPKArchive();
        try
        {
            vpk.load(file);            
        }
        catch(Exception ex)
        {
            System.err.println("Can't open archive: " + ex.getMessage());
            return;
        }
        buildHeroPortraits(vpk);
        buildItemPortraits(vpk);
        buildAnnouncerPortraits(vpk);        
    }
    
    private void buildHeroPortraits(VPKArchive vpk)
    {

        BufferedImage image = null;
        for (VPKEntry entry : vpk.getEntriesForDir("resource/flash3/images/heroes/"))
        {            
            if (entry.getType().equals("png") && !(entry.getPath().contains("selection")))
            {
                File imageFile = new File(entry.getPath());

                try (FileChannel fc = FileUtils.openOutputStream(imageFile).getChannel())
                {
                    fc.write(entry.getData());                    
                    image = ImageIO.read(imageFile);                    
                    portraitMap.put(entry.getName(), image);
                }
                catch (IOException ex)
                {
                    System.err.println("Can't write " + entry.getPath() + ": " + ex.getMessage());
                }
            }
        }
    }        

    private void buildItemPortraits(VPKArchive vpk)
    {

        BufferedImage image = null;
        for (VPKEntry entry : vpk.getEntriesForDir("resource/flash3/images/items/"))
        {            
            if (entry.getType().equals("png"))     
            {
                File imageFile = new File(entry.getPath());

                try (FileChannel fc = FileUtils.openOutputStream(imageFile).getChannel())
                {
                    fc.write(entry.getData());                    
                    image = ImageIO.read(imageFile);
                    String item = entry.getName();
                    portraitMap.put(item, image);                    
                }
                catch (IOException ex)
                {
                    System.err.println("Can't write " + entry.getPath() + ": " + ex.getMessage());
                }
            }
        }        
    }
    
    private void buildAnnouncerPortraits(VPKArchive vpk)
    {
        
        BufferedImage image = null;
        for(VPKEntry entry : vpk.getEntriesForDir("resource/flash3/images/econ/announcer/"))
        {
            if (entry.getName().contains("large"))
            {
                continue; //don't grab the huge images, we won't use them.
            }
            
            File imageFile = new File(entry.getPath());
            
            try (FileChannel fc = FileUtils.openOutputStream(imageFile).getChannel())
            {
                fc.write(entry.getData());
                image = ImageIO.read(imageFile);
                String announcer = entry.getName();
                portraitMap.put(announcer, image);
            }
            catch (IOException ex)
            {
                System.err.println("Can't write " + entry.getPath() + ": " + ex.getMessage());
            }
        }
    }

    public BufferedImage getPortrait(String portraitName)
    {
        if (portraitMap.containsKey(portraitName))
        {
            return portraitMap.get(portraitName);
        }
        else
        {
            return portraitMap.get("default");
        }
    }   
}
