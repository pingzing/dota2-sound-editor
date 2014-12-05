package dotaSoundEditor;

import dotaSoundEditor.Helpers.Utility;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class NamedMusic implements Comparable<NamedMusic>
{

    private String internalName;
    private Path internalFilePath;
    private String friendlyName;

    public NamedMusic(String _internalName, String _filePath)
    {
        internalName = _internalName;
        friendlyName = cleanUpName(internalName);
        internalFilePath = Paths.get(_filePath);
    }
    
    public String getFriendlyName()
    {
        return this.friendlyName;
    }
    
    public Path getFilePath()
    {
        return this.internalFilePath;
    }
    
    public String getInternalName()
    {
        return this.internalName;
    }
    
    public void setInternalName(String _newIntName)
    {
        this.internalName = _newIntName;
    }
    
    public void setFilePath(Path _newFP)
    {
        this.internalFilePath = _newFP;
    }
    
    public void setFilePath(String _newFP)
    {
        this.internalFilePath = Paths.get(_newFP);
    }

    private String cleanUpName(String nameToClean)
    {
        nameToClean = nameToClean.replace("_", " ");
        nameToClean = Utility.capitalizeString(nameToClean);
        nameToClean = handleSpecialCases(nameToClean);
        return nameToClean;
    }
    
    private String handleSpecialCases(String nameToClean)
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
    public String toString()
    {
        return this.friendlyName;
    }
    
    @Override
    public int compareTo(NamedMusic o)
    {
        return this.friendlyName.compareTo(o.friendlyName);
    }
    
    @Override
    public boolean equals(Object other)
    {
        if (other == null)
        {
            return false;            
        }
        if(other == this)
        {
            return true;
        }
        if (!(other instanceof NamedMusic))
        {
            return false;
        }
        
        NamedMusic otherNamedMusic = (NamedMusic) other;
        if(otherNamedMusic.friendlyName.equals(this.friendlyName))
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