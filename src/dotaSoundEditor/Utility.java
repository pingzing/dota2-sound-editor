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
    public static void setFrameIcon(Window iconWindow)
    {
         try
        {
            java.net.URL url = ClassLoader.getSystemResource("dotaSoundEditor/resources/default.png");
            Toolkit kit = Toolkit.getDefaultToolkit();
            Image img = kit.createImage(url);
            iconWindow.setIconImage(img);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
}
