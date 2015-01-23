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

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

class Handler implements Thread.UncaughtExceptionHandler
{
    @Override
    public void uncaughtException(Thread t, Throwable e)
    {
        JFrame frame = new JFrame();
        JOptionPane.showMessageDialog(frame, "An unexpected error has occurred. "
                + "An error log has been generated in the current directory."
                + "\nDetails: " + e.toString(), "Unexpected Error", 
                JOptionPane.ERROR_MESSAGE);
        Writer writer = null;
        System.err.println(e.getMessage());
        e.printStackTrace(System.err);

        try
        {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("error.txt"), "utf-8"));
            writer.write("Error: " + e.getMessage());
            writer.write("\n----------------------");
            writer.write("\n");            
            writer.close();    
            PrintWriter pwriter = new PrintWriter("error.txt");
            e.printStackTrace(pwriter);
            pwriter.close();
        }
        catch (IOException ex)
        {
            //well, now you're fucked
            ex.printStackTrace();
        }
        finally
        {
            try
            {
                writer.close();
            }
            catch (Exception ex)
            {
                //super fucked
                ex.printStackTrace();
            }
            frame.dispose();
            System.exit((1));
        }
    }
}
