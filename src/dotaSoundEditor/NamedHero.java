package dotaSoundEditor;

import dotaSoundEditor.Helpers.Utility;
import java.nio.file.Paths;

/**
 *
 * @author Neil McAlister
 */
public final class NamedHero extends NamedBase
{   

    public NamedHero(String _internalName, String _filePath)
    {
        internalName = _internalName;
        friendlyName = this.cleanUpName(internalName);
        internalFilePath = Paths.get(_filePath);
        iconName = generateIconName(internalName);
    }

    @Override
    protected String cleanUpName(String nameToClean)
    {
        nameToClean = nameToClean.replaceAll("_", " ");
        nameToClean = Utility.capitalizeString(nameToClean);
        nameToClean = handleSpecialCases(nameToClean);
        return nameToClean;
    }

    //TODO: Centralize these names in a class somewhere. Utility class maybe?
    @Override
    protected String handleSpecialCases(String nameToClean)
    {
        switch(nameToClean)
        {
            case "Antimage":            nameToClean = "Anti Mage";
                break;
            case "Batrider":            nameToClean = "Bat Rider";
                break;
            case "Centaur":             nameToClean = "Centaur Warrunner";
                break;
            case "Crystalmaiden":       nameToClean = "Crystal Maiden";
                break;
            case "Doombringer":         nameToClean = "Doom";
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
            case "Skeletonking":        nameToClean = "Wraith King";
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
        String localIconName = "";
        localIconName = name.toLowerCase();
        localIconName = localIconName.replaceAll(" ", "_");
        switch (localIconName)
        {
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
        }
        return localIconName;
    }
}
