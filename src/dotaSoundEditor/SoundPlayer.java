/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dotaSoundEditor;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import javazoom.jl.player.Player;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author
 * Image
 * 17
 */
public class SoundPlayer
{

    //Objects that make noise
    private Player mp3Player;
    private Clip clip;
    
    //Objects and members that hold data
    private File soundFile;
    private boolean isMp3 = false;
    private boolean waveIsComplete = true;    
    private JFrame parentFrame = null;

    public SoundPlayer()
    {}
    
    public SoundPlayer(String filePath)
    {
        soundFile = new File(filePath);
        if (filePath.contains(".wav"))
        {
            isMp3 = false;
        }
        else if (filePath.contains(".mp3"))
        {
            isMp3 = true;
        }
    }            

    public SoundPlayer(BufferedInputStream bis)
    {
    }
    
    public void stopSound()
    {
        if(isMp3)
        {
            mp3Player.close();
        }
        else
        {
            clip.stop();
        }
    }    
    
    public boolean getSoundIsComplete()
    {
        if(isMp3)
        {
            return mp3Player.isComplete();
        }
        else
        {
            return waveIsComplete;
        }
    }

    public void playSound()
    {                
        if (isMp3)
        {
            try
            {
                FileInputStream fis = new FileInputStream(soundFile);
                mp3Player = new Player(fis);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                System.err.println("Failed to open sound file.");
                JOptionPane.showMessageDialog(this.parentFrame, "Unable to play sound file.",
                                                "Sound Error", JOptionPane.ERROR_MESSAGE);
            }
            new Thread()
            {
                public void run()
                {
                    try
                    {
                        mp3Player.play();                        
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                        System.err.println("Failed to play MP3 file.");
                        JOptionPane.showMessageDialog(parentFrame, "Unable to play sound file.",
                                                "Sound Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }.start();
        }
        
        else if(!isMp3)
        {
            try
            {
                clip = AudioSystem.getClip();
                AudioInputStream ais = AudioSystem.getAudioInputStream(soundFile);                
                LineListener listener = new LineListener()
                {
                    public void update(LineEvent event)
                    {                        
                        if(event.getType() == Type.STOP)
                        {
                            waveIsComplete = true;
                        }
                    }
                };
                clip.addLineListener(listener);
                waveIsComplete = false;
                clip.open(ais);
                clip.start();
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
                System.err.println("Failed to play WAV file.");
            }
        }
    }
    
    public void setParentFrame(JFrame newParent)
    {
        this.parentFrame = newParent;
    }
}
