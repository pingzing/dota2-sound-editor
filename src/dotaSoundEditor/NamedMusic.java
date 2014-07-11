package dotaSoundEditor;

import dotaSoundEditor.Helpers.Utility;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class NamedMusic implements Comparable<NamedMusic>
{

    private String internalName;
    private Path filePath;
    private String friendlyName;

    public NamedMusic(String _internalName, String _filePath)
    {
        internalName = _internalName;
        friendlyName = cleanUpName(internalName);
        filePath = Paths.get(_filePath);
    }
    
    public String getFriendlyName()
    {
        return this.friendlyName;
    }
    
    public Path getFilePath()
    {
        return this.filePath;
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
        this.filePath = _newFP;
    }
    
    public void setFilePath(String _newFP)
    {
        this.filePath = Paths.get(_newFP);
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
            case "Game Sounds Music Int":       nameToClean = "The International";
                break;
            case "Game Sounds Music":           nameToClean = "Music";
                break;
            case "Game Sounds Music Spectator": nameToClean = "Spectator";
                break;
            case "Game Sounds Music Tutorial":  nameToClean = "Tutorial";
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