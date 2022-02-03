import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * The Car class contains the definition and methods
 * that are used to create and manipulate cars.
 * 
 * @author Peter Mitchell
 * @version 26/9/2009
 */
public class Car extends JComponent  
{
    // instance variables
    /** Constant width defined for the car. */
    private final int WIDTH = 30; 
    /** Constant height defined for the car. */ 
    private final int HEIGHT = 60; 
    /** The lane for the car to drive in. */
    private int lane; 
    /** The type of car; determines colour, speed, and point value */
    private int carType; 
    /** For non-player cars this defines whether they can move and allows 
     * them to be collided with; for player cars, it just defines if they 
     * can be collided with. */
    private boolean active; 
    
    // Cheat variables:
    /** Whether or not the car changes colours randomly. */
    boolean colours; 
    /** The current random colour. It is needed so that it only changes on the timer */
    Color randomColor; 
    /** Whether or not the cars are sprites or simple boring blocks. */
    boolean boring; 
    /** A reference to the game panel. */
    GamePanel gpanelRef;  
    
    /**
     * Constructor for objects of class Car.
     * This overload of the constructor is used to define non-player cars. 
     * 
     * @param  lane         The lane that the car is set to use.
     * @param  gpanelRef    A pointer to the game panel. (Used for drawing sprites)
     */
    public Car(int lane, GamePanel gpanelRef)  {
        super();
        // setup the instance variables and bounds of the car
        this.lane = lane;
        setBounds(-100, -100, WIDTH, HEIGHT);
        resetCar(); // configure the location, initial colour and active settings
        
        // setup cheat variables
        this.gpanelRef = gpanelRef;
        colours = false;
        boring = true;
    }
    
    /**
     * Constructor for objects of class Car.
     * This overload of the constructor is used specifically to create the player car. 
     * 
     * @param  gpanelRef    A pointer to the game panel. (Used for drawing sprites)
     */
    public Car(GamePanel gpanelRef)  {
        super();
        // The player does not have a specific lane, so just set to -1
        this.lane = -1;
        setBounds(245, 610, WIDTH, HEIGHT); // start the player in the centre of the road
        setBackground(Color.white);
        active = true; // for the player the active attribute so that collisions are checked for
        
        // setup cheat variables
        colours = false;
        boring = true;
        carType = 4; // used to show correct sprite
        this.gpanelRef = gpanelRef;
    }

    /**
     * The method resetCar() moves the car to the starting position
     * depending on the lane. It also sets a new random car type and
     * disables the car.
     */
    public void resetCar() {
        setLocation(lane*35,-100);
        newCarType();
        active = false;
    }
    
    /**
     * The method setXLocation() attempts to move the car
     * to the X coordinate and is restricted to certain bounds.
     * 
     * @param  X    The x coordinate to try and move the car to.
     */
    public void setXLocation(int X)
    {
        if(X < 0) X = 0; // lower X limit
        if(X > 455) X = 455; // upper X limit
        
        // update the car location
        setLocation(X, getY());   
    } 
    
    /**
     * The method newCarType() randomly changes the car type
     * and based on the new car type will change the colour.
     */
    private void newCarType()
    {
        // randomly set a new car type
        Random generator = new Random();
        carType = generator.nextInt(4);
        
        // assign the colour of the car based on the car type
        switch(carType) {
            case 0:
                setBackground(Color.blue);
                break;
            case 1:
                setBackground(Color.green);
                break;
            case 2:
                setBackground(Color.red);
                break;
            case 3:
                setBackground(Color.yellow);
                break;
        }   
    }
    
    /**
     * The method updateYPosition() increments the Y coordinate of the car
     * based on the type of car.
     * 
     * @return  The updated Y coordinate of the car. 
     */
    public int updateYPosition()
    {
        // get the current speed
        int YSpeed;
        switch(carType) {
            case 0:
                YSpeed = 5;
            case 1:
                YSpeed = 8;
            case 2:
                YSpeed = 10;
            default: // case 3
                Random generator = new Random();
                YSpeed = generator.nextInt(20) + 1;
        }

        // update the car's location
        setLocation(getX(), getY()+YSpeed);
        
        // update the random colour if the cheat is activated
        if(colours) {
            Random gen = new Random();
            randomColor = new Color(gen.nextInt(256), gen.nextInt(256), gen.nextInt(256));
        }
        
        return getY(); // return the updated speed
    }
    
    /**
     * The method getPointValue() gets the score the user will get for dodging this car type.
     * 
     * @return  The point value of the car.
     */
    public int getPointValue()
    {
        return carType+1;       
    }
    
    /**
     * The method getActive() is an accessor method for the active variable.
     * 
     * @return  The value of active.
     */
    public boolean getActive()
    {
        return active;
    }
    
    /**
     * The method setActive() is a mutator method for the variable active.
     * 
     * @param  active    The value to set the class variable active to.
     */
    public void setActive(boolean active)
    {
        this.active = active;
    }
    
    /**
     * The method testCollision() compares the bounding boxes of 2 cars
     * to test whether they are currently intersecting.
     * 
     * @param  othercar     The other car to test a collision against.
     * 
     * @return  Whether the 2 cars collide.
     */
    public boolean testCollision(Car othercar)
    {
        // only compare cars that are currently active
        if(active && othercar.getActive()) {
            // get the left and right bounding points
            int left = getX();
            int otherleft = othercar.getX();
            int right = left + getWidth();
            int otherright = otherleft + othercar.getWidth();
        
            // get the upper and lower bounding points
            int top = getY();
            int othertop = othercar.getY();
            int bottom = top + getHeight();
            int otherbottom = othertop + othercar.getHeight();
        
            // break if any of the following are true because it means they don't intersect
            if(bottom < othertop) return false;
            if(top > otherbottom) return false;
            if(right < otherleft) return false;
            if(left > otherright) return false;
            
            // the bounding boxes do intersect
            return true;
        }
        
        return false;
    }
    
    /**
     * The method isPointInCar() compares a set of coordinates 
     * to check if the point is inside the car.
     * 
     * @param  x    The x coordinate to check.
     * @param  y    The y coordinate to check.
     * 
     * @return  Whether the point is inside the car.
     */
    public boolean isPointInCar(int x, int y)
    {
        // get the bounding box for the car
        int left = getX();
        int right = left + getWidth();
        int top = getY();
        int bottom = top + getHeight();
        
        // check if the point is outside the bounding box
        if(bottom < y) return false;
        if(top > y) return false;
        if(right < x) return false;
        if(left > x) return false;
            
        // the point is inside the bounding box
        return true;   
    }
    
    /** 
     * This method draws the car.
     * Depending on the cheats that are active the appearance changes.
     * 
     * post:   this method draws a Car
     *          and  the upper left corner is (getX(), getY()) 
     *          and  the Car's dimensions are getWidth() and getHeight()
     *          and  the Car's color is getBackground()
     */
    public void paint(Graphics g)  {
        // If the car is the default or not a tank
        if(boring && carType != 5) {
            // If the colours cheat is not activated use the current colour
            if(!colours) {
                g.setColor( getBackground() );
            } else {
                // if it is use the random colour
                g.setColor( randomColor );
            }
            
            // draw the shape of the car
            g.fillRect(0, 0, getWidth()-1, getHeight()-1);
            g.setColor(Color.black);
            g.fillRect(2,10, 25,10);
            g.fillRect(2,40, 25,10);
            paintChildren(g);
        } else {
            // draw the car as a sprite
            // get the image to display from the game panel's sprite array
            Image img = gpanelRef.getImagebyID(carType);
            if(img != null) {
                g.drawImage(img,0,0,this);   
            }
        }
    }
    
    // *******************************************************************
    // Cheat methods
    // *******************************************************************
    
    /**
     * The method ToggleColours() is a mutator for the colours variable.
     * It toggles the variable, which affects whether or not the cars are 
     * changing colours randomly.
     */
    public void ToggleColours()
    {
        colours = !colours;
    }
    
    /**
     * The method ToggleBoring() is a mutator for the boring variable.
     * It toggles the variable, which affects whether or not the cars are 
     * sprites that have been loaded from files or the normal boring appearance.
     */
    public void ToggleBoring()
    {
        boring = !boring;
    }
    
    
    /**
     * The method getCarType() is an accessor method for the carType variable.
     * 
     * @return  Return the value of carType;
     */
    public int getCarType()
    {
        return carType;
    }
    
    /**
     * The method setCarType() is a mutator method for the carType variable.
     * It will set carType to whatever the parameter is that is passed to the method.
     * 
     * @param  carType  The carType to set the car to.
     */
    public void setCarType(int carType)
    {
        this.carType = carType;   
    }   
}