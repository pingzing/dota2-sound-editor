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