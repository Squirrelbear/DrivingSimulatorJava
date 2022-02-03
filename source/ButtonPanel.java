import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * The ButtonPanel class holds the buttons for creating a
 * New Game, Showing/Hiding lane lines, Pausing the game, and 
 * Quitting the game. The events for these are handled in this 
 * class, but call methods that are in the Driver class.
 * 
 * @author Peter Mitchell
 * @version 26/9/2009
 */
public class ButtonPanel extends JPanel
{
    // instance variables
    /** A pointer to the Driver class. */
    private Driver gameRef;
    /** New Game button instance to start new games. */
    private JButton btnNewGame; 
    /** Show/Hide lanes button to show/hide lanes. */
    private JButton btnLanes;
    /** Pause button to pause the game. */
    private JButton btnPause;
    /** Quit button to quit the game. */
    private JButton btnQuit; 
    
    /**
     * Constructor for objects of class ButtonPanel. 
     * It creates 4 buttons with an action listener, then adding them to the window.
     * 
     * @param gameRef   A pointer to the Driver instance.
     */
    public ButtonPanel(Driver gameRef)
    {
        // set the instance variable, and panel settings
        this.gameRef = gameRef;
        setBackground(Color.lightGray);
        setPreferredSize(new Dimension(150,95));
        setLayout(new GridLayout(4,1));
        
        // create the 4 buttons
        btnNewGame = new JButton("New Game");
        btnLanes = new JButton("Hide Lanes");
        btnPause = new JButton("Pause");
        btnQuit = new JButton("Quit");
        
        // The following line removes issues with the keyevent not being detected
        btnQuit.setFocusable(false);
        
        // Create the button listener and add the listener to the buttons
        ButtonListener listener = new ButtonListener();
        btnNewGame.addActionListener(listener);
        btnLanes.addActionListener(listener);
        btnPause.addActionListener(listener);
        btnQuit.addActionListener(listener);
        
        // Add the buttons to the panel
        add(btnNewGame);
        add(btnLanes);
        add(btnPause);
        add(btnQuit);
    }
    
    /**
     * The Pause() method is an accessor method for the Pause button.
     * It changes the display text of the button. Either by toggling
     * it or setting it. Depending on the value of toggle.
     * 
     * @param toggle    Whether or not to simply toggle the message or set it.
     */    
    public void Pause(boolean toggle)
    {
        if(btnPause.getText().equals("Pause") || !toggle) {
            btnPause.setText("Play");   
        } else {
            btnPause.setText("Pause");   
        }
    }

    /**
     * The ButtonListener that is implemented by the ButtonPanel to control
     * the the specified actions.
     */
    private class ButtonListener implements ActionListener
    {
        /**
        * Handle the 4 button events: New Game, Hide/Show Lanes, Pause, and Quit.
        *  
        * @param  event   The variables related to the event that caused this method to be called.
        * */
        public void actionPerformed(ActionEvent event)
        {
            if(event.getSource() == btnNewGame) {
                // New Game has been clicked, so call New Game in the Driver class
                gameRef.NewGame();
                btnPause.setText("Pause");
            } else if(event.getSource() == btnLanes) { 
                // Show/Hide the lane lines
                gameRef.ToggleLanes();
                if(btnLanes.getText().equals("Hide Lanes")) {
                    btnLanes.setText("Show Lanes");   
                } else {
                    btnLanes.setText("Hide Lanes");   
                }      
            } else if(event.getSource() == btnPause) {
                // Pause has been clicked, so call Pause in the Driver class
                gameRef.Pause();
                Pause(true);
            } else { // anything else will be the quit button
                // Display a dialog with the below question
                int choice = JOptionPane.showConfirmDialog(null,"Are you sure you want to quit?");
                if(choice == JOptionPane.YES_OPTION) { // if they have pressed Yes quit
                    System.exit(0);
                }   
            }
        }
    }
}