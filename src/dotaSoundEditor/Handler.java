package dotaSoundEditor;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
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
        }
    }
}
