package dotaSoundEditor;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public abstract class NamedBase implements NamedElement, Comparable<NamedElement>
{

    protected String internalName;
    protected Path internalFilePath;
    protected String friendlyName;   
    protected String iconName;

    @Override
    public String getFriendlyName()
    {
        return this.friendlyName;
    }

    @Override
    public Path getFilePath()
    {
        return this.internalFilePath;
    }

    @Override
    public String getInternalName()
    {
        return this.internalName;
    }

    @Override
    public void setFilePath(Path newPath)
    {
        this.internalFilePath = newPath;
    }
    
    @Override 
    public void setFilePath(String newPath)
    {
        this.internalFilePath = Paths.get(newPath);
    }
    
    @Override
    public void setInternalName(String newName)
    {
        this.internalName = newName;
    }
    
    @Override
    public String getIconName()
    {
        return this.iconName;        
    }
    
    @Override
    public void setIconName(String newIconName)
    {
        this.iconName = newIconName;
    }
    
    @Override
    public String toString()
    {
        return this.friendlyName;
    }
    
    @Override
    public int compareTo (NamedElement other)
    {
        return this.friendlyName.compareTo(other.getFriendlyName());
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
        if(!(other.getClass().equals(this.getClass()))) //Types aren't the same
        {
            return false;
        }
        
        NamedElement otherNamedElement = (NamedElement) other;
        if(otherNamedElement.getFriendlyName().equals(this.friendlyName))
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
        hash = 23 * hash + Objects.hashCode(this.friendlyName);
        return hash;
    }
    
    protected abstract String cleanUpName(String nameToClean);
    
    protected abstract String handleSpecialCases(String nameToClean);
    
    protected abstract String generateIconName(String friendlyName);
}
