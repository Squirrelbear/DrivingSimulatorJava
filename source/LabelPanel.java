import java.awt.*;
import javax.swing.*;

/**
 * The LabelPanel class is used to display 3 labels.
 * They display the current score status of the player.
 * 
 * @author Peter Mitchell 
 * @version 22/9/2009
 */
public class LabelPanel extends JPanel
{
    // instance variables
    /** Label showing how many cars have been dodged. */
    private JLabel lblDodged;
    /** Label showing how many car crashes have occured. */
    private JLabel lblCrashes;
    /** Label showing the total score. */
    private JLabel lblTotalScore; 
    /** The number of cars that have been dodged. */
    private int dodged;
    /** The number of crashes that have occured. */
    private int crashes;
    /** The total score of the player. */
    private int totalscore; 

    /**
     * Constructor for objects of class LabelPanel.
     * Create the labels and setup the other instance variables.
     */
    public LabelPanel()
    {
        // setup the panel
        setPreferredSize(new Dimension(150,80));
        setBackground(Color.lightGray);
        setLayout(new GridLayout(3,1));
        
        // initialise the instance variables
        dodged = 0;
        crashes = 0;
        totalscore = 0;
        
        // create the labels        
        lblDodged = new JLabel("Cars Dodged: 0");
        lblCrashes = new JLabel("Crashes: 0");
        lblTotalScore = new JLabel("Total Score: 0");
        
        // add the labels to the window
        add(lblDodged);
        add(lblCrashes);
        add(lblTotalScore);
    }

    /**
    * The method increaseScore() increases the score and updates the labels.
    *  
    * @param  amount    The amount to increase the score by.
    * */
    public void increaseScore(int amount)
    {
        totalscore += amount;
        dodged++;
        lblDodged.setText("Cars Dodged: " + dodged);
        lblTotalScore.setText("Total Score: " + totalscore);
    }
    
    /**
    * The method increaseCrashes() increases the crashes and updates the label.
    * */
    public void increaseCrashes()
    {
        crashes++;
        lblCrashes.setText("Crashes: " + crashes);
    }
}