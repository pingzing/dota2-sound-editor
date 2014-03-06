/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dotaSoundEditor;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;

/**
 *
 * @author
 * Image
 * 17
 */
public class Utility
{

    public static PortraitFinder portraitFinder;

    public static void initPortraitFinder(String vpkDir)
    {
        portraitFinder = new PortraitFinder(vpkDir);
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
}
