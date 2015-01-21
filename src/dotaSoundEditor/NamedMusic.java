package dotaSoundEditor;

import dotaSoundEditor.Helpers.Utility;
import java.nio.file.Paths;

public final class NamedMusic extends NamedBase
{
    public NamedMusic(String _internalName, String _filePath)
    {
        internalName = _internalName;
        friendlyName = cleanUpName(internalName);
        internalFilePath = Paths.get(_filePath);
        iconName = null;
    }
    
    @Override
    protected String cleanUpName(String nameToClean)
    {
        nameToClean = nameToClean.replace("_", " ");
        nameToClean = Utility.capitalizeString(nameToClean);
        nameToClean = handleSpecialCases(nameToClean);
        return nameToClean;
    }
    
    @Override
    protected String handleSpecialCases(String nameToClean)
    {
        switch(nameToClean)
        {
            case "Music/game Sounds Music Int":         nameToClean = "The International";
                break;
            case "Music/game Sounds Music Spectator":   nameToClean = "Spectator";
                break;
            case "Music/game Sounds Music Tutorial":    nameToClean = "Tutorial";
                break;
            case "Music/game Sounds Music Util":        nameToClean = "Misc.";
                break;
            case "Music/game Sounds Stingers Diretide": nameToClean = "Diretide";
                break;
            case "Music/game Sounds Stingers Greevil":  nameToClean = "The Greeviling";
                break;
            case "Music/game Sounds Stingers Main":     nameToClean = "Stingers - 1";
                break;                                            
            case "Terrorblade Arcana/game Sounds Music":      nameToClean = "Terrorblade Arcana";
                break;
            case "Terrorblade Arcana/game Sounds Stingers":   nameToClean = "Terrorblade Arcana Stingers";
                break;
            case "Valve Dota 001/Game Sounds Music":          nameToClean = "Music";
                break;
            case "Valve Dota 001/Game Sounds Stingers":       nameToClean = "Stingers - 2";
                break;
            case "Valve Ti4/game Sounds Music":               nameToClean = "TI4 Compendium Music";
                break;
            case "Valve Ti4/game Sounds Stingers":            nameToClean = "TI4 Compendium Stingers";
                break;
        }
        
        return nameToClean;
    }        

    @Override
    protected String generateIconName(String friendlyName)
    {
        //May add icon support for music in the future, but not yet.
        throw new UnsupportedOperationException("Not supported yet.");
    }
}