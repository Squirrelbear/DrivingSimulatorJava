import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Random;
import java.io.*;

/**
 * The GamePanel class controls the displaying of the cars and updating events to do with them.
 * 
 * @author Peter Mitchell
 * @version 26/9/2009
 */
public class GamePanel extends JPanel
{
    /** Width of the game panel. */
    private final int WIDTH = 490; 
    /** Height of the game panel. */
    private final int HEIGHT = 700; 
    /** The number of car lanes to have in the game. (14 so that there will always be at least one empty lane) */
    private final int LANES = 14;
    /** An animated road background. */
    BackgroundPanel background;
    
    /** A pointer to the Driver class. */
    private Driver gameRef; 
    /** An array to hold the car instances. */
    private Car[] cars;
    /** The player's car. */
    private Car player;
    /** Whether dragging the mouse will result in the player moving. */
    private boolean playerMove; 
    /** the current difficulty level */
    private int difficulty;
    /** number of currently active cars */
    private int activeCars; 
    /** whether the game is currently paused. */
    private boolean pause; 
    
    /** The timer instance that is used to control animation and update the game. */
    private Timer timer; 
    
    // cheat variables
    /** When true killablecars makes it so that clicking on cars will remove them. */
    private boolean killablecars; 
    /** An array that can hold all of the sprites that are shown for cheats. */
    private Image[] carSprites; 
    
    /**
     * Constructor for objects of class GamePanel.
     * Setup a car for each lane, add the player, setup listeners and start the game.
     * 
     * @param gameRef   A pointer to the Driver instance.
     */
    public GamePanel(Driver gameRef)
    {
        // configure the panel
        this.gameRef = gameRef;
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.gray);
        setLayout(null);
        
        // configure cheats
        killablecars = false;
        // load sprites
        carSprites = new Image[6];
        carSprites[0] = loadImage("Sprites/BlueCar.png");
        carSprites[1] = loadImage("Sprites/GreenCar.png"); 
        carSprites[2] = loadImage("Sprites/RedCar.png");
        carSprites[3] = loadImage("Sprites/YellowCar.png");
        carSprites[4] = loadImage("Sprites/WhiteCar.png");
        carSprites[5] = loadImage("Sprites/TankCar.png");
        
        // add the animated background
        background = new BackgroundPanel();
        add(background,0);
        
        // instantiate the cars array and add the cars to the window
        cars = new Car[LANES]; // create an array with the number of needed lanes
        for(int i = 0; i < LANES; i++) {
            cars[i] = new Car(i,this);   
            add(cars[i], 0);
        }
        
        // setup the player
        player = new Car(this);
        add(player, 0);
        
        // setup mouse listener for player
        PlayerListener listener = new PlayerListener();
        addMouseListener(listener);
        addMouseMotionListener(listener);
        
        // Setup timer timer
        timer = new Timer(100, new UpdateListener());
        
        // default to level 1 difficulty
        setDifficulty(1);

        // Setup the New Game
        NewGame();

        // Start the timer
        timer.start();
    }

    /**
     * The method setDifficulty() changes the difficulty level of the game.
     * 
     * @param  difficulty   The difficulty to set the game to.
     */
    public void setDifficulty(int difficulty)
    {
        pause = true; // pause the game when the difficulty has been changed
        this.difficulty = difficulty;
        int delay = 100; // default delay is 100
        
        // based on the difficulty requested set the activeCars and delay variables
        switch(difficulty) 
        {
            case 1:
                activeCars = 3;
                break;
            case 2:
                activeCars = 5;
                break;
            case 3:
                activeCars = 10;
                break;
            case 4:
                activeCars = 5;
                delay = 50;
                break;
            case 5:
                activeCars = 10;
                delay = 50;
                break;
            case 6:
                activeCars = 13;
                delay = 50;
                break;
            case 7:
                activeCars = 10;
                delay = 25;
                break;
            case 8:
                activeCars = 13;
                delay = 25;
                break;
            case 9:
                activeCars = 10;
                delay = 1;
                break;
            case 10:
                activeCars = 13;
                delay = 1;
                break;
        }
        
        // update the timer delay
        timer.setDelay(delay);
    }
    
    /**
     * The method Pause() is a mutator method that toggles whether the game is Paused.
     */
    public void Pause()
    {
        pause = !pause;
    }
    
    /**
     * The method NewGame() resets the game and starts it with a fresh wave of cars. 
     */
    public void NewGame()
    {
        //  Reset the cars to the top of the screen
        for(int i = 0; i < LANES; i++) {
               cars[i].resetCar();
        }
        
        // Start a random set of cars
        for(int i = 0; i < activeCars; i++) {
               activateRandomCar();
        }
                
        // reset the player
        player.setXLocation(245);
        
        pause = false;
    }
    
    /**
     * The method ToggleLanes() calls the accessor method in background to toggle the lanes. 
     */
    public void ToggleLanes()
    {
        background.ToggleLanes();
    }
    
    /**
     * The method activateRandomCar() randomly looks till it finds an inactive car and activates it.  
     */
    private void activateRandomCar()
    {
        Random generator = new Random();
        boolean carfound = false;
        
        // loop until an inactive car is found
        while(!carfound) {
            int tempid = generator.nextInt(LANES);
            if(!cars[tempid].getActive()) {
                cars[tempid].setActive(true);  
                carfound = true;
            }
        }
    }

    /**
     * The UpdateListener that is implemented by GamePanel to control timer events.
     */
    private class UpdateListener implements ActionListener
    {
        /**
        * Handle the timer event. Update cars is not paused.
        *  
        * @param  event   The variables related to the event that caused this method to be called.
        * */        
        public void actionPerformed(ActionEvent event)
        {
            // only do something if the game is not paused
            if(!pause) {
                // update all of the AI controlled cars
                for(int i = 0; i < LANES; i++) {
                    // only update active cars
                    if(cars[i].getActive()) {
                        // update the Y position of the car and check if it has left the window
                        if(cars[i].updateYPosition() > HEIGHT) {
                            // the car has left the window
                            // increase the score based on the point value of the car
                            gameRef.increaseScore(cars[i].getPointValue()*difficulty);
                            // reset the car to the lane starting position
                            cars[i].resetCar();
                            // activate an inactive car randomly
                            activateRandomCar();
                        }
                    }
                }
                
                // for each car test if it is colliding with the player
                for(int i = 0; i < LANES; i++) {
                    if(player.testCollision(cars[i])) {
                        gameRef.increaseCrashes();
                        i = 13; // only increase crash once per round
                    }
                }
                
                // update the background
                background.updateFrame();
                
                repaint();
            }
        }
    }
    
    /**
     * The PlayerListener that is implemented by the GamePanel to control the player movement.
     */
    private class PlayerListener implements MouseListener, MouseMotionListener
    {
        /**
        * Handle the mouse being pressed down. 
        * Allow the player to move if the game is not paused and the player is clicking in the car.
        * Also if the cheat is activated allow clicking to remove cars.
        *  
        * @param  event   The variables related to the event that caused this method to be called.
        * */
        public void mousePressed(MouseEvent event)
        {
            // check if the game is currently paused
            if(!pause) {
                int x = event.getX();
                int y = event.getY();
                
                // check if the player has clicked inside the player car
                if(player.isPointInCar(x, y)) {
                    playerMove = true;
                }
                           
                // cheat to allow clicking on cars to remove them
                if(killablecars) {
                    for(int i = 0; i < LANES; i++) {
                        // test if the mouse is being clicked in another car
                        if(cars[i].isPointInCar(x, y)) {
                            // reset the car to the lane starting position
                            cars[i].resetCar();
                            // activate an inactive car randomly
                            activateRandomCar();
                        }
                    }
                }
            }
        }
        
        /**
        * Handle the mouse being dragged. 
        * If the game is not paused and player movement is enabled update the player cars location.  
        *  
        * @param  event   The variables related to the event that caused this method to be called.
        * */
        public void mouseDragged(MouseEvent event)
        {
            // check if paused
            if(!pause) {
                // check if able to move
                if(playerMove) {
                    // update the position of the car so that it's X coordinate is centred on the mouse
                    player.setXLocation(event.getX()-15);
                    
                    // If the player has moved too far away disable player movement
                    if(event.getY() < (player.getY()-30) || event.getY() > HEIGHT) {
                        playerMove = false;
                    }
            
                    repaint();
                }
            }
        }
        
        /**
        * Handle the mouse being released. 
        * Disable player movement.  
        *  
        * @param  event   The variables related to the event that caused this method to be called.
        * */
        public void mouseReleased(MouseEvent event) {
            playerMove = false;   
        }
        
        // other unused methods
        public void mouseMoved(MouseEvent event) {}
        public void mouseClicked(MouseEvent event) {}
        public void mouseEntered(MouseEvent event) {}
        public void mouseExited(MouseEvent event) {}
    }
    
    // *******************************************************************
    // Cheat methods
    // *******************************************************************
    
    /**
    * The method ToggleGodMode() is for the "GODMODE" cheat.
    * The cheat is activated/disactivated by toggling whether the player is active.
    * For the player the active attribute simply controls whether it can be collided with.
    * As compared to the AI for which the active variable determines both whether
    * the object is collidable and whether it should be currently moving.
    * */
    public void ToggleGodMode()
    {
        player.setActive(!player.getActive());   
    }
    
    /**
    * The method ToggleColours() is for the "Colours!" cheat.
    * The cheat is toggled using a method in the Car class.
    * The cheat needs to be stored in the Car class because it needs to be accessed when drawing.
    * */
    public void ToggleColours()
    {
        for(int i = 0; i < LANES; i++) {
               cars[i].ToggleColours();
        }    
    }
    
    /**
    * The method ToggleKillableCars() is for the "Make Them Go Away" cheat.
    * The method toggles the variable killablecars which when true allows
    * for clicking on cars to make them disappear.
    * */
    public void ToggleKillableCars()
    {
        killablecars = !killablecars;   
    }
    
    /**
    * The method ToggleBoring() is for the "These Cars Are Boring" cheat.
    * The method toggles the cheat in all of the cars that will be affected.
    * */
    public void ToggleBoring()
    {
        for(int i = 0; i < LANES; i++) {
               cars[i].ToggleBoring();
        } 
        
        player.ToggleBoring();
    }
    
    /**
    * The method ToggleTank() is for the "Tank Me Up" cheat.
    * The method toggles the car type to make it display a 
    * tank image for the player's car.
    * */
    public void ToggleTank()
    {
        int newType = (player.getCarType() == 4) ? 5 : 4;
        player.setCarType(newType);
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
    * The method getImagebyID() finds the image in the carSprites array
    * and returns it.
    * 
    * @param    imageID     The array index of the image being requsted.
    * @return   The requested image.
    * */
    public Image getImagebyID(int imageID)
    {
        return carSprites[imageID];   
    }
}