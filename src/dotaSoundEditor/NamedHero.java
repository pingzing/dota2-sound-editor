package dotaSoundEditor;

import dotaSoundEditor.Helpers.Utility;
import java.util.Objects;

/**
 *
 * @author Neil McAlister
 */
public class NamedHero implements Comparable<NamedHero>
{

    private String internalName;
    private String friendlyName;

    public NamedHero(String _internalName)
    {
        internalName = _internalName;
        friendlyName = cleanUpName(internalName);
    }

    public String getInternalName()
    {
        return this.internalName;
    }

    public String getFriendlyName()
    {
        return this.friendlyName;
    }

    public void setInternalName(String _internalName)
    {
        internalName = _internalName;
        friendlyName = cleanUpName(internalName);
    }

    private String cleanUpName(String nameToClean)
    {
        nameToClean = nameToClean.replaceAll("_", " ");
        nameToClean = Utility.capitalizeString(nameToClean);
        nameToClean = handleSpecialCases(nameToClean);
        return nameToClean;
    }

    private String handleSpecialCases(String nameToClean)
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

    //Overrides what the dropdown box displays. Screw renderers! =D
    @Override
    public String toString()
    {
        return this.friendlyName;
    }

    @Override
    public int compareTo(NamedHero other)
    {
        return this.friendlyName.compareTo(other.friendlyName);
    }

    @Override
    public boolean equals(Object other)
    {
        if (other == null)
        {
            return false;
        }
        if (other == this)
        {
            return true;
        }
        if (!(other instanceof NamedHero))
        {
            return false;
        }

        NamedHero otherNamedHero = (NamedHero) other;
        if (otherNamedHero.friendlyName.equals(this.friendlyName))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.friendlyName);
        return hash;
    }
}
