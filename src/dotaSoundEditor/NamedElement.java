package dotaSoundEditor;

import java.nio.file.Path;

public interface NamedElement
{
    String getFriendlyName();

    Path getFilePath();

    String getInternalName();

    void setInternalName(String newInternalName);

    void setFilePath(Path newFilePath);

    void setFilePath(String newFilePath);   
    
    String getIconName();
    
    void setIconName(String newIconName);

    @Override
    String toString();

    int compareTo(NamedElement o);

    @Override
    boolean equals(Object other);

    @Override
    int hashCode();
}
