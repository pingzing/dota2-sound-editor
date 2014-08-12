package dotaSoundEditor.Helpers;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;

public class Utility
{
    //TODO: Investigate placement of this, why is it in here instead of in PortraitFinder?
    public static PortraitFinder portraitFinder;
    public static void initPortraitFinder(String vpkPath)
    {
        portraitFinder = new PortraitFinder(vpkPath);
    }

    public static void setFrameIcon(Window iconWindow)
    {
        try
        {
            java.net.URL url = ClassLoader.getSystemResource("dotaSoundEditor/resources/editorIconTiny.png");
            Toolkit kit = Toolkit.getDefaultToolkit();
            Image img = kit.createImage(url);
            iconWindow.setIconImage(img);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public static int nthOccurrence(String str, char c, int n)
    {
        int pos = str.indexOf(c, 0);
        while (n-- > 0 && pos != -1)
        {
            pos = str.indexOf(c, pos + 1);
        }
        return pos;
    }

    public static String splitCamelCase(String s)
    {
        return s.replaceAll(
                String.format("%s|%s|%s",
                "(?<=[A-Z])(?=[A-Z][a-z])",
                "(?<=[^A-Z])(?=[A-Z])",
                "(?<=[A-Za-z])(?=[^A-Za-z])"),
                " ");
    }
    
    public static String capitalizeString(String string)
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
}
