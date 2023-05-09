import java.awt.*;
import java.awt.geom.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Player extends ObjectProperties {
    // (posX, posY) is the center of the player.

    private BufferedImage sprite;
    private int speedX, speedY;
    private double angle, angleSensitivity; // in radians
    private int angleMovement; // 0 - stable, 1 - clockwise, 2 - counterclockwise
    private boolean toMove;
    private int minSpeed;
    private int maxSpeed;
    private int speedIncrement;
    private boolean isSpeedingUp;

    public double needleX, needleY;

    public Player(double x, double y, double w, double h, int sx, int sy, int si, int ms, String spritefile){
        super(x, y);
        try{
            sprite = ImageIO.read(new File(spritefile));
        }catch (IOException e){
            e.printStackTrace();
        }
        
        setWidth(sprite.getWidth());
        setHeight(sprite.getHeight());
        speedX = sx;
        speedY = sy;
        angle = Constants.RAD90;
        angleSensitivity = Constants.ANGLESENS;
        angleMovement = 0;
        toMove = false;
        minSpeed = sx;
        maxSpeed = ms;
        speedIncrement = si;
        isSpeedingUp = false;
        setNeedlePoint();
    }

    public void draw(Graphics2D g2d, AffineTransform reset){
        // System.out.println(posX + " " + posY);
        // g2d.fillOval((int)(needleX-Constants.BODYRADIUS), (int)(needleY-Constants.BODYRADIUS), (int)Constants.BODYRADIUS*2, (int)Constants.BODYRADIUS*2);
        
        g2d.rotate(angle, posX, posY);
        g2d.translate(posX - sprite.getWidth(null)/2, posY - sprite.getHeight(null)/2);
        g2d.drawImage(sprite, 0, 0, null);
        
        g2d.setPaint(Color.white);
        g2d.drawRect(0, 0, (int)width, (int)height);
        
        g2d.setTransform(reset);
    }
    // For Controls
    public void setAngleMovement(String command){
        // System.out.println(command);
        if (command.equals("cw")) angleMovement = 1;
        else if (command.equals("ccw")) angleMovement = 2;
        else if (command.equals("stop")) angleMovement = 0;
        else System.out.println("command: " + command);
        // System.out.println("angleMovement: " + angleMovement);
    }
    public void moveAngle(){
        // System.out.println(angle);
        if (angleMovement == 1) angle -= angleSensitivity;
        else if (angleMovement == 2) angle += angleSensitivity;
        if (angle > Constants.RAD90*4) angle -= Constants.RAD90*4;
        if (angle < 0) angle += Constants.RAD90*4;
    }
    public double getAngle(){return angle;}
    
    // public void setMovement(String command){
    //     if (command.equals("stopMove")) toMove = false;
    //     else if (command.equals("move")) toMove = true;
    // }

    public boolean isDashing(){return (speedX != minSpeed && speedY != minSpeed);}

    public void move(){
        posX += Math.round(Math.cos(angle)*speedX * 100) / 100;
        posY += Math.round(Math.sin(angle)*speedY * 100) / 100;
        setNeedlePoint();

        if (!isSpeedingUp){
            if (speedX < minSpeed){
                speedX += speedIncrement;
                speedY += speedIncrement;
            }
            if (speedX >= minSpeed){
                speedX = minSpeed;
                speedY = minSpeed;
            }
        }
        else{
            if (speedX > -maxSpeed){
                speedX = Math.max(speedX - speedIncrement, -maxSpeed);
                speedY = Math.max(speedY - speedIncrement, -maxSpeed);
                // System.out.println(speedX);
            }
            else if (speedX <= -maxSpeed){
                isSpeedingUp = false;
                // System.out.println("stop speeding up");
            }
        }
    }

    public void toggleDash(){isSpeedingUp = true;}

    public void setAngle(double theta){angle = theta;}

    public void setAngleToIncidence(boolean vertical){
        if (vertical){
            angle = (-angle)+4*Constants.RAD90;
        }
        else{
            angle = (-angle)+2*Constants.RAD90;
        }
    }

    public void setNeedlePoint(){
        needleX = posX - Math.round(Math.cos(angle)*Constants.NEEDLEDIST * 100) / 100;
        needleY = posY - Math.round(Math.sin(angle)*Constants.NEEDLEDIST * 100) / 100;
    }

    public void bodyPunctured(){
        speedX = minSpeed;
        speedY = minSpeed;
        isSpeedingUp = false;
    }
}
