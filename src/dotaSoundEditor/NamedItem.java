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
package dotaSoundEditor;

import dotaSoundEditor.Helpers.Utility;
import java.nio.file.Paths;

public final class NamedItem extends NamedBase
{   
    private String iconName;
    
    public NamedItem()
    {
        internalName = "default";
        friendlyName = "Default";
        iconName = "default";
    }

    public NamedItem(String _internalName, String _filePath)
    {
        internalName = _internalName;
        friendlyName = cleanUpName(internalName);
        internalFilePath = Paths.get(_filePath);
        iconName = generateIconName(friendlyName);
    }  

    public String getIconName()
    {
        return this.iconName;
    }

    public void setIconName(String _iconName)
    {
        this.iconName = _iconName;
    }
    
    @Override
    protected String cleanUpName(String _nameToClean)
    {
        String nameToClean = _nameToClean;
        /* Newer item sounds break naming conventions established by old sound names.
         * The new convention is is DOTA_Item.<Name>.<Sound trigger>.
         * Usually. Sometimes. Maybe?
         */
        if (nameToClean.contains("Midas"))
        {
            nameToClean = "Hand of Midas";
        }        
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
        else if(nameToClean.contains("Desolator"))
        {
            nameToClean = "Desolator";
        }
        else if(nameToClean.contains("Maim"))        
        {
            nameToClean = "Sange (and S&Y)";
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

    @Override
    protected String handleSpecialCases(String nameToClean)
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
            case "MKB":                 nameToClean = "Monkey King Bar";
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

    @Override
    protected String generateIconName(String friendlyName)
    {
        String localIconName = "";
        localIconName = friendlyName.toLowerCase();
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
            case "sange_(and_s&y)":             localIconName = "sange";
        }
        return localIconName;
    }
}
