package dotaSoundEditor;

import dotaSoundEditor.Helpers.Utility;
import java.nio.file.Paths;

public final class NamedVoice extends NamedBase
{   

    public NamedVoice(String _internalName, String _filePath)
    {
        internalName = _internalName;
        friendlyName = cleanUpName(internalName);
        internalFilePath = Paths.get(_filePath);
        iconName = generateIconName(internalName);
    }
    
    @Override
    protected String cleanUpName(String nameToClean)
    {
        nameToClean = nameToClean.replaceAll("_", " ");
        nameToClean = Utility.capitalizeString(nameToClean);
        nameToClean = nameToClean.replaceAll("Announcer", "Announcer:");
        nameToClean = nameToClean.replaceAll("Dlc ", "");
        nameToClean = nameToClean.replaceAll("Killing Spree", "(Killing Spree)");
        nameToClean = handleSpecialCases(nameToClean);
        return nameToClean;
    }   

    @Override
    protected String handleSpecialCases(String nameToClean)
    {
        switch(nameToClean)
        {
            /*Announcers & Misc*/
            case "Announcer:":                              nameToClean = "Announcer: Default";
                break;
            case "Announcer: (Killing Spree)":              nameToClean = "Announcer: Default (Killing Spree)";
                break;
            case "Announcer: Defensegrid":                  nameToClean = "Announcer: Defense Grid";
                break;
            case "Announcer: Defensegrid (Killing Spree)":  nameToClean = "Announcer: Defense Grid (Killing Spree)";
                break;
            case "Announcer: Glados":                       nameToClean = "Announcer: GLaDOS";
                break;
            case "Announcer: Glados (Killing Spree)":       nameToClean = "Announcer: GLaDOS (Killing Spree)";
                break;
            case "Announcer: Kleiner":                      nameToClean = "Announcer: Dr. Kleiner";
                break;
            case "Announcer: Kleiner (Killing Spree)":      nameToClean = "Announcer: Dr. Kleiner (Killing Spree)";
                break;
            case "Announcer: Naturesprophet":               nameToClean = "Announcer: Nature's Prophet";
                break;
            case "Announcer: Naturesprophet (Killing Spree)":   nameToClean = "Announcer: Nature's Prophet (Killing Spree)";    
                break;
            case "Announcer: Pflax":                        nameToClean = "Announcer: Pyrion Flax";
                break;
            case "Announcer: Pflax (Killing Spree)":        nameToClean = "Announcer: Pyrion Flax (Killing Spree)";
                break;
            case "Announcer: Stormspirit":                  nameToClean = "Announcer: Storm Spirit";
                break;
            case "Announcer: Stormspirit (Killing Spree)":  nameToClean = "Announcer: Storm Spirit (Killing Spree)";
                break;
            case "Announcer: Stanleyparable":               nameToClean = "Announcer: Stanley Parable";
                break;
            case "Announcer: Stanleyparable (Killing Spree)":   nameToClean = "Announcer: Stanley Parable (Killing Spree)";
                break;
            case "Secretshop":                              nameToClean = "Secret Shop";
                break;
            case "Tut1":                                    nameToClean = "Tutorial 1";
                break;
                
                /*Heroes*/
            case "Antimage":            nameToClean = "Anti Mage";
                break;
            case "Batrider":            nameToClean = "Bat Rider";
                break;
            case "Centaur":             nameToClean = "Centaur Warrunner";
                break;
            case "Crystalmaiden":       nameToClean = "Crystal Maiden";
                break;
            case "Doom Bringer":         nameToClean = "Doom";
                break;
            case "Drowranger":          nameToClean = "Drow Ranger";
                break;
            case "Furion":              nameToClean = "Nature's Prophet";
                break;
            case "Life Stealer":        nameToClean = "Lifestealer";
                break;
            case "Necrolyte":           nameToClean = "Necrophos";
                break;
            case "Magnataur":           nameToClean = "Magnus";
                break;
            case "Nightstalker":        nameToClean = "Night Stalker";
                break;
            case "Obsidian Destroyer":  nameToClean = "Outworld Devourer";
                break;
            case "Queenofpain":         nameToClean = "Queen of Pain";
                break;
            case "Rattletrap":          nameToClean = "Clockwerk";
                break;
            case "Sandking":            nameToClean = "Sand King";
                break;           
            case "Nevermore":           nameToClean = "Shadow Fiend";
                break;
            case "Shadowshaman":        nameToClean = "Shadow Shaman";
                break;
            case "Shredder":            nameToClean = "Timbersaw";
                break;
            case "Skeleton King":        nameToClean = "Wraith King";
                break;
            case "Stormspirit":         nameToClean = "Storm Spirit";
                break;
            case "Treant":              nameToClean = "Treant Protector";
                break;
            case "Vengefulspirit":      nameToClean = "Vengeful Spirit";
                break;
            case "Windrunner":          nameToClean = "Windranger";
                break;
            case "Wisp":                nameToClean = "Io";
                break;                
            case "Witchdoctor":         nameToClean = "Witch Doctor";
                break;
            case "Zuus":                nameToClean = "Zeus";
                break;
        }
        return nameToClean;
    }

    @Override
    protected String generateIconName(String name)
    {
        String localIconName = name.toLowerCase();
        localIconName = localIconName.replace(" ", "_"); 
        
        /* For announcers: Remove "_dlc_", Change "killing_spree" to "megakill_" 
        * and move it to the front of the filename, and if Mega Kill announcer, 
        * remove "_announcer_". This gets us most of the announcers for free.
        */
        localIconName = localIconName.replace("_dlc_", "_");        
        if(localIconName.contains("killing_spree"))
        {
            localIconName = localIconName.replace("_killing_spree", "");
            localIconName = "megakill_" + localIconName;
            localIconName = localIconName.replace("_announcer_", "_");
        }
        switch(localIconName)
        {
            //TODO: Still need special case handling for: Outworld, Shopkeeper, Warlock Golem, Tutorial (maybe) and all the announcers
            /*Units*/
            case "witchdoctor":
                localIconName = "witch_doctor";
                break;
            case "doombringer":
                localIconName = "doom_bringer";
                break;
            case "nightstalker":
                localIconName = "night_stalker";
                break;
            case "skeletonking":
                localIconName = "skeleton_king";
                break;
            case "shadowshaman":
                localIconName = "shadow_shaman";
                break;
            case "crystalmaiden":
                localIconName = "crystal_maiden";
                break;
            case "drowranger":
                localIconName = "drow_ranger";
                break;
            case "sandking":
                localIconName = "sand_king";
                break;
            case "stormspirit":
                localIconName = "storm_spirit";
                break;
            case "outworld_destroyer":
                localIconName = "obsidian_destroyer";
                break;
                
                /*Announcers*/                
            case "announcer":
                localIconName = "announcer_icon_default";
                break;
            case "megakill_announcer":
                localIconName = "announcer_icon_default";
                break;                
            
            case "announcer_defensegrid":
                localIconName = "announcer_dgrid";
                break;
            case "megakill_defensegrid":
                localIconName = "megakill_dgrid";
                break;
                
            case "announcer_diretide_2012":
                localIconName = "announcer_death_prophet";
                break; 
                
            case "announcer_glados":
                localIconName = "announcer_portal";
                break;
            case "megakill_glados":
                localIconName = "megakill_portal";
                break;                            
                
            case "announcer_naturesprophet":
                localIconName = "announcer_furion";
                break;
            case "megakill_naturesprophet":
                localIconName = "megakill_furion";
                break;
                
            case "announcer_tusk":
                localIconName = "announcer_tuskar";
                break;
            
            case "megakill_workshop_pirate":
                localIconName = "megakill_announcer_workshop_pirate";
                break;                                
        }
        return localIconName;
    }
}
