package dotaSoundEditor.Helpers;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
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

public final class SoundPlayer
{   
    //Objects that make noise
    private Player mp3Player;
    private Clip clip;
    //Objects and members that hold data
    private File soundFile;
    private boolean isMp3 = false;
    private boolean soundIsPlaying = false;
    private JFrame parentFrame = null;
    //Event foundation
    private final PropertyChangeSupport handler = new PropertyChangeSupport(this);

    public SoundPlayer(){    }
    
    public void addPropertyChangeListener(PropertyChangeListener pcl)
    {
        handler.addPropertyChangeListener(pcl);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener pcl)
    {
        handler.removePropertyChangeListener(pcl);
    }

    public void loadSound(String filePath)
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

    public void stopSound()
    {
        if(mp3Player != null && !mp3Player.isComplete())
        {
            mp3Player.close();
            soundIsPlaying = false;
            handler.firePropertyChange("soundIsPlaying", true, false);
        }
        if(clip != null)
        {
            clip.stop();
            soundIsPlaying = false;
            handler.firePropertyChange("soundIsPlaying", true, false);
        }
    }

    public boolean isSoundPlaying()
    {
       return soundIsPlaying;
    }

    public void playSound()
    {        
        stopSound();
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
                        soundIsPlaying = true;
                        handler.firePropertyChange("soundIsPlaying", false, true);
                        mp3Player.play();
                        soundIsPlaying = false;
                        handler.firePropertyChange("soundIsPlaying", true, false);
                        
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
        else if (!isMp3)
        {
            try
            {
                clip = AudioSystem.getClip();
                AudioInputStream ais = AudioSystem.getAudioInputStream(soundFile);
                LineListener listener = new LineListener()
                {
                    public void update(LineEvent event)
                    {
                        if (event.getType() == Type.STOP)
                        {
                            soundIsPlaying = false;
                            handler.firePropertyChange("soundIsPlaying", true, false);
                            clip.close();
                        }
                    }
                };
                clip.addLineListener(listener);
                soundIsPlaying = true;
                handler.firePropertyChange("soundIsPlaying", false, true);
                clip.open(ais);
                clip.start();
            }
            catch (Exception ex)
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
