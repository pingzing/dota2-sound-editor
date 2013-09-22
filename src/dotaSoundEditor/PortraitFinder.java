/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dotaSoundEditor;

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

    String fileName;
    BufferedImage portrait;
    TreeMap<String, BufferedImage> portraitMap = new TreeMap<>();

    public PortraitFinder(String _fileName)
    {
        fileName = _fileName;
        File file = new File(fileName);
        VPKArchive vpk = new VPKArchive();

        System.out.println("In extract.");
        System.out.println(file);

        try
        {
            vpk.load(file);
        }
        catch (Exception ex)
        {
            System.err.println("Can't open archive: " + ex.getMessage());
            return;
        }

        BufferedImage image = null;
        for (VPKEntry entry : vpk.getEntries())
        {
            //Put criteria to search for here
            if (entry.getPath().contains("resource/flash3/images/heroes/") && entry.getType().equals("png")
                    && !(entry.getPath().contains("selection")))
            {
                File imageFile = new File(entry.getPath());

                try (FileChannel fc = FileUtils.openOutputStream(imageFile).getChannel())
                {
                    fc.write(entry.getData());
                    System.out.println("Writing image to File object.");
                    image = ImageIO.read(imageFile);
                    String heroName = handleSpecialCaseHeroNames(entry.getName());                    
                    portraitMap.put(heroName, image);
                }
                catch (IOException ex)
                {
                    System.err.println("Can't write " + entry.getPath() + ": " + ex.getMessage());
                }
            }
        }
    }

    public BufferedImage getPortrait(String heroName)
    {
        if (portraitMap.containsKey(heroName))
        {
            return portraitMap.get(heroName);
        }
        else
        {
            return portraitMap.get("default");
        }
    }

    private String handleSpecialCaseHeroNames(String name)
    {
        //Make portrait hero names match internal hero names. Internal names don't have underscores
        switch (name)
        {
            case "witch_doctor":
                name = "witchdoctor";
                break;
            case "doom_bringer":
                name = "doombringer";
                break;
            case "night_stalker":
                name = "nightstalker";
                break;
            case "skeleton_king":
                name = "skeletonking";
                break;
            case "shadow_shaman":
                name = "shadowshaman";
                break;
            case "crystal_maiden":
                name = "crystalmaiden";
                break;
            case "drow_ranger":
                name = "drowranger";
                break;
            case "sand_king":
                name = "sandking";
                break;
            case "storm_spirit":
                name = "stormspirit";
                break;
        }
        return name;
    }
}
