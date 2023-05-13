/**
    @author James Bryan M. Francisco (222677)
    @author James Ivan P. Mostajo (224396)
    @version May 13, 2023
**/
/*
    I have not discussed the Java language code in my program
    with anyone other than my instructor or the teaching assistants
    assigned to this course.
    I have not used Java language code obtained from another student,
    or any other unauthorized source, either modified or unmodified.
    If any Java language code or documentation used in my program
    was obtained from another source, such as a textbook or website,
    that has been clearly noted with a proper citation in the comments
    of my program.
*/

/*
    This class contains all the necessary properties of the Player class.
    An instance of this class acts as the bee in the game.
    This class is instantiated in the GameCanvas class.
*/

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.io.IOException;
import java.io.File;

public class Player extends ObjectProperties {
    // (posX, posY) is the center of the player.
    private double initialX, initialY;
    private BufferedImage sprite, sprite_flap;
    private int speed;
    private double angle, angleSensitivity; // in radians
    private int angleMovement; // 0 - stable, 1 - clockwise, 2 - counterclockwise
    private int minSpeed;
    private int maxSpeed;
    private int speedIncrement;
    private boolean isSpeedingUp;
    private boolean isInvincible;
    private boolean justGotHoney;
    private int inviCounter;
    private int animCounter;
    private int honeyCounter;
    private AlphaComposite hitAlpha;
    private AlphaComposite normalAlpha;
    private int score;

    // Constructor of player class. Initalizes all values necessary.
    public Player(double x, double y, String spritefile, String spritefileflap){
        super(x, y);
        initialX = x;
        initialY = y;
        try{
            sprite = ImageIO.read(new File(spritefile));
            sprite_flap = ImageIO.read(new File(spritefileflap));
        }catch (IOException e){
            e.printStackTrace();
        }
        setWidth(sprite.getWidth());
        setHeight(sprite.getHeight());
        setInitialStats();

        hitAlpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Constants.INVIALPHA); // opacity of bee when hit.
        normalAlpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f); // normal opacity.
    }

    // Draws the bee. 
    public void draw(Graphics2D g2d, AffineTransform reset){
        g2d.rotate(angle, posX, posY);
        g2d.translate(posX - sprite.getWidth(null)/2, posY - sprite.getHeight(null)/2);

        // If bee is hit, alternatingly paint the bee with low opacity and normal opacity by intervals of 5 frames.
        if (isInvincible && inviCounter % 10 < 5){
            g2d.setComposite(hitAlpha);
        }
        else{
            g2d.setComposite(normalAlpha);
        }
        
        // Alternatingly paints the bee with its normal sprite and flapping sprite according to the speed of the bee.
        if (animCounter % 100 < 50){
            g2d.drawImage(sprite, 0, 0, null);
        }
        else{
            g2d.drawImage(sprite_flap, 0, 0, null);
        }

        g2d.setTransform(reset);

        // Handle counter increments/decrements

        if (inviCounter > 0)
            inviCounter -= 1;
        else if (inviCounter == 0)
            resetInviCounter();

        if (honeyCounter > 0)
            honeyCounter -= 1;
        else if (honeyCounter == 0)
            resetHoneyCounter();

        animCounter = (animCounter + speed) % 1000;
        
        
    }

    // Changes the angle/direction of the bee.
    // This method is being called on the key bindings section of GameFrame
    public void setAngleMovement(String command){
        if (command.equals("cw")) angleMovement = 1;
        else if (command.equals("ccw")) angleMovement = 2;
        else if (command.equals("stop")) angleMovement = 0;
    }

    // Sets the angle/direction of the bee by a constant value.
    public void moveAngle(){
        if (angleMovement == 1) angle -= angleSensitivity;
        else if (angleMovement == 2) angle += angleSensitivity;

        // Following if-statements are implemented to avoid overflows
        if (angle > Constants.RAD90*4) angle -= Constants.RAD90*4;
        if (angle < 0) angle += Constants.RAD90*4;
    }

    // Getter function of the bee's angle. Used for sending angle to enemy client.
    public double getAngle(){return angle;}

    // Sets the bee's angle (in radians).
    public void setAngle(double theta){angle = theta;}

    // Checks if bee is dashing. 
    public boolean isDashing(){return (speed != minSpeed);}

    // Moves the bee according to bee's speed and angle.
    // Also sets boolean values necessary on the bee's dashing motion.
    public void move(){
        posX += Math.round(Math.cos(angle)*speed * 100) / 100;
        posY += Math.round(Math.sin(angle)*speed * 100) / 100;
        
        if (!isSpeedingUp){
            if (speed > minSpeed)
                speed = Math.max(speed - speedIncrement, minSpeed);
        }
        else{
            if (speed < maxSpeed)
                speed = Math.min(speed + speedIncrement, maxSpeed);
            else if (speed >= maxSpeed)
                isSpeedingUp = false;
        }
    }

    // Prepares the bee to speed up/accelerate.
    public void toggleDash(){isSpeedingUp = true;}

    // Sets the angle to the incidence angle; sets the angle to a new angle when the bee hits the border.
    public void setAngleToIncidence(boolean vertical){
        // If bee hits a vertical border
        if (vertical){
            angle = (-angle)+4*Constants.RAD90;
        }
        // Else, if bee hits a horizontal border
        else{
            angle = (-angle)+2*Constants.RAD90;
        }
    }

    // Sets necessary values when the bee is hit by the enemy's needle.
    public void bodyPunctured(){
        speed = minSpeed;
        isSpeedingUp = false;
        isInvincible = true;
        inviCounter = Constants.INVIDURATION;
    }

    // Resets necessary values when cooldown of the bee being hit finishes.
    public void resetInviCounter(){
        inviCounter = 0;
        isInvincible = false;
    }

    // Checks if the bee is currently invincible (or just got hit).
    public boolean isInvincible(){
        return isInvincible;
    }

    // Add/subtract score of player.
    public void addScore(int v){
        score += v;
        if (score < 0) score = 0;
    }

    // Getter method of player's score.
    public int getScore(){
        return score;
    }

    // Sets necessary values if the bee collided with a honey.
    public void gotHoney(){
        justGotHoney = true;
        honeyCounter = Constants.GOTHONEYDURATION;
    }

    // Resets necessary values when cooldown of the bee getting a honey finishes.
    public void resetHoneyCounter(){
        honeyCounter = 0;
        justGotHoney = false;
    }

    // Checks if bee just got a honey. (This is to prevent the bee on getting honey rapidly)
    public boolean justGotHoney(){
        return justGotHoney;
    }

    // Plays a sound effect when the bee got hit by the enemy.
    public void playHitSound(){
        try{
            File file = new File(Constants.HITSOUND);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
		    Clip clip = AudioSystem.getClip();
		    clip.open(audioStream);
            clip.start();
         }
        catch(Exception e){
            System.out.println("e");
        }
    }

    // Plays a sound effect when the player gets a point.
    public void getPointSound(){
        try{
            File file = new File(Constants.SCORESOUND);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
		    Clip clip = AudioSystem.getClip();
		    clip.open(audioStream);
            clip.start();
         }
        catch(Exception e){
            System.out.println("e");
        }
    }

    // Sets all initial stats of the player. Used when restarting the game.
    public void setInitialStats(){
        posX = initialX;
        posY = initialY;
        speed = Constants.NORMALSPEED;
        angle = Constants.RAD90;
        angleSensitivity = Constants.ANGLESENS;
        angleMovement = 0;
        minSpeed = Constants.NORMALSPEED;
        maxSpeed = Constants.MAXSPEED;
        speedIncrement = Constants.SPEEDINCREMENT;
        isSpeedingUp = false;
        isInvincible = false;
        justGotHoney = false;
        inviCounter = 0;
        animCounter = 0;
        honeyCounter = 0;
        score = 0;
    }
}
