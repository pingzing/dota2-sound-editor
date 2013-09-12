/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dota.sound.editor;

import java.util.Objects;

/**
 *
 * @author Image 17
 */
public class NamedHero implements Comparable<NamedHero>
{

    String internalName;
    String friendlyName;

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
        nameToClean = capitalizeString(nameToClean);
        nameToClean = handleSpecialCases(nameToClean);
        return nameToClean;
    }

    private String handleSpecialCases(String nameToClean)
    {
        //check and deal with special cases, i.e. bristlebog/bristleback
        return nameToClean;
    }

    private static String capitalizeString(String string)
    {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++)
        {
            if (!found && Character.isLetter(chars[i]))
            {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            }
            else if (Character.isWhitespace(chars[i]) || chars[i] == '.' || chars[i] == '\'')
            { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }

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
