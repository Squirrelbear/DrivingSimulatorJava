import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * The ScrollerPanel class holds the slider to set
 * the difficulty and a label stating what difficulty
 * has currently been set.
 * 
 * @author Peter Mitchell 
 * @version 22/9/2009
 */
public class ScrollerPanel extends JPanel
{    
    // instance variables
    /** A pointer to the Driver class. */
    private Driver gameRef; 
    /** The slider to control the difficulty. */
    private JSlider difficulty; 
    /** A label to show the currently set difficulty. */
    private JLabel lbldifficulty;

    /**
     * Constructor for objects of class ScrollerPanel.
     * Create a Slider and a Label to change and display the difficulty.
     * 
     * @param gameRef   A pointer to the Driver instance.
     */
    public ScrollerPanel(Driver gameRef)
    {
        // set the instance variable, and panel settings
        this.gameRef = gameRef;
        setBackground(Color.lightGray);
        setPreferredSize(new Dimension(120,60));
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        
        // create a label showing the current difficulty
        lbldifficulty = new JLabel("Difficulty: 1");
        add(lbldifficulty);
        
        // create a Slider with marks on it to change the difficulty
        difficulty = new JSlider(1, 10, 1);
        difficulty.setPaintTicks(true);
        difficulty.setMajorTickSpacing(2);
        difficulty.setMinorTickSpacing(1);
        add(difficulty);
        
        // add a listener to the slider
        SliderListener listener = new SliderListener();
        difficulty.addChangeListener(listener);
    }

    /**
     * The SliderListener that is implemented by the ScrollerPanel to 
     * change the difficulty when the slider is changed.
     */
    private class SliderListener implements ChangeListener
    {
        /**
        * Handle the difficulty slider event.
        *  
        * @param  event   The variables related to the event that caused this method to be called.
        * */
        public void stateChanged(ChangeEvent event)
        {
            // The state has changed, so call SetDifficulty in Driver to update the games state
            gameRef.SetDifficulty(difficulty.getValue());
            lbldifficulty.setText("Difficulty: " + difficulty.getValue());
        }
    }
}