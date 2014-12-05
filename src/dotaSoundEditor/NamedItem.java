package dotaSoundEditor;

import dotaSoundEditor.Helpers.Utility;

public class NamedItem
{

    private String internalName;    
    private String friendlyName;
    private String iconName;
    
    public NamedItem()
    {
        internalName = "default";
        friendlyName = "Default";
        iconName = "default";
    }

    public NamedItem(String _internalName)
    {
        internalName = _internalName;
        friendlyName = cleanUpName(internalName);        
        iconName = setIconName();
    }

    public String getInternalName()
    {
        return this.internalName;
    }

    public String getFriendlyName()
    {
        return this.friendlyName;
    }

    public String getIconName()
    {
        return this.iconName;
    }

    public void setIconName(String _iconName)
    {
        this.iconName = _iconName;
    }

    public void setInternalName(String _internalName)
    {
        internalName = _internalName;
        friendlyName = cleanUpName(internalName);
    }

    private String cleanUpName(String _nameToClean)
    {
        String nameToClean = _nameToClean;
        /* For some reason, Hand of Midas' script name is completely different 
         * than every other item's. Go figure. It follows the pattern 
         * DOTA_Item.Hand_Of_Midas, whereas everything else 
         * is DOTA_Item.<Name>.<Sound trigger>
         */
        if (nameToClean.contains("Midas"))
        {
            nameToClean = "Hand of Midas";
        }
        //Would you look at that, so is the Basher's. Sigh.
        else if (nameToClean.contains("SkullBasher"))
        {
            nameToClean = "Skull Basher";
        }
        else if(nameToClean.contains("Butterfly"))
        {
            nameToClean = "Butterfly";
        }
        else if(nameToClean.contains("BattleFury"))
        {
            nameToClean = "Battle Fury";
        }
        else if(nameToClean.contains("MKB"))
        {
            nameToClean = "Monkey King Bar";
        }
        else if(nameToClean.contains("Desolator"))
        {
            nameToClean = "Desolator";
        }
        else
        {
            try
            {
                nameToClean = nameToClean.replaceAll("_", "");
                int beginIndex = Utility.nthOccurrence(nameToClean, '.', 0);
                int endIndex = Utility.nthOccurrence(nameToClean, '.', 1);
                nameToClean = nameToClean.substring(beginIndex + 1, endIndex);
                nameToClean = Utility.splitCamelCase(nameToClean);
                nameToClean = nameToClean.replace("Of", "of");
                nameToClean = handleSpecialCases(nameToClean);
            }
            catch(IndexOutOfBoundsException ex)
            {
                ex.printStackTrace();
                return _nameToClean;
            }
        }
        return nameToClean;
    }

    private String handleSpecialCases(String nameToClean)
    {
        switch (nameToClean)
        {
            case "Armlet":              nameToClean = "Armlet of Mordiggian";
                break;            
            case "Daedelus":            nameToClean = "Daedalus";
                break;
            case "Do E":                nameToClean = "Drum of Endurance";
                break;
            case "Cyclone":             nameToClean = "Eul's Scepter of Divinity";
                break;
            case "Heavens Halberd":     nameToClean = "Heaven's Halberd";
                break;     
            case "Hot D":               nameToClean = "Helm of the Dominator";
                break;
            case "Linkens Sphere":      nameToClean = "Linken's Sphere";
                break;
            case "Manta":               nameToClean = "Manta Style";
                break;
            case "Orchid":              nameToClean = "Orchid Malevolence";
                break;    
            case "Pipe":                nameToClean = "Pipe of Insight";
                break;                    
            case "Refresher":           nameToClean = "Refresher Orb";
                break;
            case "Invisibility Sword":  nameToClean = "Shadow Blade";
                break; 
            case "Sheepstick":          nameToClean = "Scythe of Vyse";
                break;
            case "Shivas Guard":        nameToClean = "Shiva's Guard";
                break;                                   
            case "Veilof Discord":      nameToClean = "Veil of Discord";
                break;                               
        }
        return nameToClean;
    }

    private String setIconName()
    {
        String localIconName = "";
        localIconName = this.friendlyName.toLowerCase();
        localIconName = localIconName.replaceAll(" ", "_");
        switch (localIconName)
        {            
            case "clarity_potion":              localIconName = "clarity";
                break;            
            case "healing_salve":               localIconName = "flask";
                break;
            case "dust_of_appearance":          localIconName = "dust";
                break;
            case "sentry_ward":                 localIconName = "ward_sentry";
                break;
            case "observer_ward":               localIconName = "ward_observer";
                break;
            case "blink_dagger":                localIconName = "blink";
                break;
            case "pipe_of_insight":             localIconName = "pipe";
                break;
            case "orchid_malevolence":          localIconName = "orchid";
                break;
            case "eul's_scepter_of_divinity":   localIconName = "cyclone";
                break;
            case "refresher_orb":               localIconName = "refresher";
                break;
            case "shiva's_guard":               localIconName = "shivas_guard";
                break;
            case "manta_style":                 localIconName = "manta";
                break;
            case "shadow_blade":                localIconName = "invis_sword";
                break;
            case "linken's_sphere":             localIconName = "sphere";
                break;
            case "heaven's_halberd":            localIconName = "heavens_halberd";
                break;
            case "drum_of_endurance":           localIconName = "ancient_janggo";
                break;
            case "armlet_of_mordiggian":        localIconName = "armlet_active";
                break;
            case "scythe_of_vyse":              localIconName = "sheepstick";
                break;
            case "skull_basher":                localIconName = "basher";
                break;
            case "daedalus":                    localIconName = "greater_crit";
                break;
            case "battle_fury":                 localIconName = "bfury";
                break;            
        }
        return localIconName;
    }
}
