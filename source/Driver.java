import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * The Driver class contains the code to start the program.
 * It also exists to be the central point that all the other classes communicate through.
 * 
 * @author Peter Mitchell 
 * @version 26/9/2009
 */
public class Driver
{
    // instance variables
    /** The JFrame instance. */
    JFrame win;
    
    /** The LabelPanel instance. */
    private LabelPanel labelPanel; 
    
    /** The ButtonPanel instance. */
    ButtonPanel buttonPanel;
    
    /** The GamePanel instance. */
    private GamePanel gamePanel;
    
    /**
    * Constructor for objects of the Driver class.
    * Exists as the entry point to the game and creates the windows. 
    * */
    public Driver()
    {
        // Setup the main window
        win = new JFrame("Driving Simulator");
        win.setBounds(10,10,620,700);
        win.setLayout(new BorderLayout());
        win.setResizable(false); // disable resizing
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // when the game is closed, exit the program
        
        // create the key listener and add it to the window
        InputListener listener = new InputListener();
        win.addKeyListener(listener);
        
        //Create a control panel and add it to the window
        JPanel ControlPanel = new JPanel();
        ControlPanel.setPreferredSize(new Dimension(120,10));
        ControlPanel.setBackground(Color.lightGray);
        ControlPanel.setLayout(new BorderLayout());
        win.add(ControlPanel, BorderLayout.EAST);

        // Populate the ControlPanel
        buttonPanel = new ButtonPanel(this);
        ControlPanel.add(buttonPanel, BorderLayout.SOUTH);

        JPanel NorthPanel = new JPanel();
        NorthPanel.setPreferredSize(new Dimension(120,150));
        NorthPanel.setBackground(Color.lightGray);
        NorthPanel.setLayout(new GridLayout(2,1));
        
        labelPanel = new LabelPanel();
        NorthPanel.add(labelPanel);

        ScrollerPanel scrollerPanel = new ScrollerPanel(this);
        NorthPanel.add(scrollerPanel);
   
        ControlPanel.add(NorthPanel, BorderLayout.NORTH);
        
        // Create a game panel and add it to teh window
        gamePanel = new GamePanel(this);
        win.add(gamePanel, BorderLayout.CENTER);     
        
        // Pack/Show window and repaint it
        win.pack();
        win.setVisible(true);
        win.requestFocus(); // focus on the window so that keyevents can be registered
        win.repaint();
    }
    
    // *******************************************************************
    // Button event related methods
    // *******************************************************************
    
    /**
    * The method NewGame() exists an an accessor method for the gamePanel.
    * Specifically to call the NewGame() method.
    * */
    public void NewGame()
    {
        gamePanel.NewGame();
        win.requestFocus(); // returns focus to the window so that keyevents are not ignored
    }
    
    /**
    * The method ToggleLanes() exists an an accessor method for the gamePanel.
    * Specifically to call the ToggleLanes() method.
    * */
    public void ToggleLanes()
    {
        gamePanel.ToggleLanes();
        win.requestFocus(); // returns focus to the window so that keyevents are not ignored
    }
    
    /**
    * The method Pause() exists as an accessor method for the gamePanel.
    * Specifically to call the Pause() method.
    * */
    public void Pause()
    {
        gamePanel.Pause();
        win.requestFocus(); // returns focus to the window so that keyevents are not ignored
    }
    
    /**
    * The method SetDifficulty() exists as an accessor method for the gamePanel.
    * Specifically to call the setDifficulty() method using the parameter difficulty.
    *  
    * @param    difficulty  The requested difficulty.
    * */
    public void SetDifficulty(int difficulty)
    {
        gamePanel.setDifficulty(difficulty);
        buttonPanel.Pause(false); // change the message on the pause button to pause
        win.requestFocus(); // returns focus to the window so that keyevents are not ignored
    }
    
    // *******************************************************************
    // Game event related methods
    // *******************************************************************
    
    /**
    * The method increaseScore() exists as an accessor method for the labelPanel.
    * Specifically to call the increaseScore() method using the parameter amount.
    *  
    * @param    amount  The amount to increase the score by.
    * */
    public void increaseScore(int amount)
    {
        labelPanel.increaseScore(amount);
    }
    
    /**
    * The method increaseCrashes() exists as an accessor method for the labelPanel.
    * Specifically to call the increaseCrashes() method. Also ask if the user wants
    * to continue.
    * */
    public void increaseCrashes()
    {
        // call the method in the LabelPanel
        labelPanel.increaseCrashes();
        Pause(); // pause the game
        
        // give the user a choice as to whether they want to continue
        int choice = JOptionPane.showConfirmDialog(null,"You have crashed! Would you like to resume with a new car?");
        if(choice == JOptionPane.YES_OPTION) {
            NewGame(); // setup a new game     
        } else {
            System.exit(0); // exit the game
        }
    }
    
    // *******************************************************************
    // Cheat methods
    // *******************************************************************
    
    /**
    * The method activateCheat() compares a string to test if it is a valid cheat code.
    * If it is a valid cheat code it will toggle the activation of the particular cheat.
    * 
    * @param    cheatcode   The String that holds the entered cheat. 
    * */
    public void activateCheat(String cheatcode)
    {
        if(cheatcode.equals("GODMODE")) {
            gamePanel.ToggleGodMode();
        } else if(cheatcode.equals("Tank Me Up")) {
            gamePanel.ToggleTank();
        } else if(cheatcode.equals("Colours!")) {
            gamePanel.ToggleColours();
        } else if(cheatcode.equals("These Cars Are Boring")) {
            gamePanel.ToggleBoring();   
        } else if(cheatcode.equals("Make Them Go Away")) {
            gamePanel.ToggleKillableCars();  
        } else {
            JOptionPane.showMessageDialog(null, "You have not entered a valid cheat code.");   
        }   
    }
    
    /**
    * The class InputListener handles the input from the keyboard. 
    * Specifically to handle the pressing of C to open the cheat panel.
    * */
    private class InputListener implements KeyListener
    {
        /**
        * Handle keyTyped events.
        * If the user types a C, open a panel to enter a cheat.
        *  
        * @param  event   The variables related to the event that caused this method to be called.
        * */
        public void keyTyped(KeyEvent event)
        {
            if(event.getKeyChar() == 'c') {
                String cheatcode = "";
                try {
                    cheatcode = JOptionPane.showInputDialog("Enter a cheat code: ");
                    activateCheat(cheatcode);
                } catch(NullPointerException e) {
                    // catch the exception caused by pressing cancel   
                }
            }
        }
        
        // other unused methods
        public void keyPressed(KeyEvent event) {}
        public void keyReleased(KeyEvent event) {}
    }
}