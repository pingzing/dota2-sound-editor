package dotaSoundEditor;

import dotaSoundEditor.Helpers.Utility;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class NamedVoice implements Comparable<NamedVoice>
{

    private String internalName;
    private Path internalFilePath;
    private String friendlyName;

    public NamedVoice(String _internalName, String _filePath)
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
        return nameToClean;
    }

    @Override
    public String toString()
    {
        return this.friendlyName;
    }

    @Override
    public int compareTo(NamedVoice o)
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
        if (other == this)
        {
            return true;
        }
        if (!(other instanceof NamedVoice))
        {
            return false;
        }

        NamedVoice otherNamedVoice = (NamedVoice) other;
        if (otherNamedVoice.friendlyName.equals(this.friendlyName))
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
