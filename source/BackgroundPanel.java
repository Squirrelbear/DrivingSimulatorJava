import java.awt.*;
import javax.swing.*;
import java.io.*;

/**
 * The BackgroundPanel class controls the animation of the road lanes.
 *  
 * @author Peter Mitchell 
 * @version 26/9/2009
 */
public class BackgroundPanel extends JPanel
{
    // instance variables
    /** The image to be used for the animated road background. */
    private Image background; 
    /** The current animation frame. */
    private int frame; 
    /** Whether the lanes are visible or not. */
    private boolean lanesVisible; 
    
    /**
     * Constructor for objects of class BackgroundPanel.
     */
    public BackgroundPanel()
    {
        // configure the background
        setBounds(0,0,490,700);
        setBackground(Color.gray);
        frame = 0;
        lanesVisible = true;
        
        // load the image for the animation
        background = loadImage("Sprites/lane.jpg");
    }
    
    /**
    * The method loadImage() loads the image file called 
    * filename and returns the image.
    * 
    * @param    filename    The filename of the file to load.
    * @return   The image that has been loaded. 
    * */
    private Image loadImage(String filename) 
    {
        File f = new File(filename);
        Image image = getToolkit().getImage(f.getPath());
        return image;
    } 
    
    /**
    * The method updateFrame() updates the status of the animation.
    * It increments the animation frame, and if it is greater 
    * than 2 it resets back to frame 0. 
    * */
    public void updateFrame()
    {
        frame++;
        
        if(frame > 2) frame = 0;
    }
    
    /**
    * The method ToggleLanes() toggles whether the lanes are showing. 
    * */
    public void ToggleLanes()
    {
        lanesVisible = !lanesVisible;
    }

    /** 
     * This method draws the background.
     * It tiles the current animation frame. Where the current
     * frame is defined by drawing the image at different positions.
     */
    public void paint(Graphics g)  {
        // draw the background colour
        super.paintComponent(g);
        
        if(background != null && lanesVisible) {
            for(int i = 0; i < 13; i++) {
                g.drawImage(background,i*35,-100+frame*30,this); 
            }
        }
    }
}