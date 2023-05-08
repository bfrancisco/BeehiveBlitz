import java.awt.*;
import java.awt.geom.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Player extends ObjectProperties {
    // (posX, posY) is the center of the player.
    private static final double RAD90 = 1.57; // 1.570796327
    private static final double ANGLESENS = 0.2;
    private static final double NEEDLEDIST = 26;

    private BufferedImage sprite;
    private int speedX, speedY;
    private double angle, angleSensitivity; // in radians
    private int angleMovement; // 0 - stable, 1 - clockwise, 2 - counterclockwise
    private boolean toMove;
    private int minSpeed;
    private int maxSpeed;
    private int speedIncrement;
    private boolean isSpeedingUp;

    private double needleX, needleY;

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
        angle = RAD90;
        angleSensitivity = ANGLESENS;
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
        g2d.fillOval((int)needleX-5, (int)needleY-5, 10, 10);
        g2d.rotate(angle, posX, posY);
        g2d.translate(posX - sprite.getWidth(null)/2, posY - sprite.getHeight(null)/2);
        g2d.drawImage(sprite, 0, 0, null);
        
        g2d.setPaint(Color.white);
        g2d.drawRect(0, 0, (int)width, (int)height);
        
        g2d.setTransform(reset);
    }

    // Mutator Functions
    public void invertXDirection(){speedX *= -1;}
    public void invertYDirection(){speedY *= -1;}

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
        if (angle > RAD90*4) angle -= RAD90*4;
        if (angle < 0) angle += RAD90*4;
    }
    public double getAngle(){return angle;}
    
    public void setMovement(String command){
        if (command.equals("stopMove")) toMove = false;
        else if (command.equals("move")) toMove = true;
    }

    public boolean isMoving(){return (speedX > 0 && speedY > 0);}

    public void move(){
        posX += Math.round(Math.cos(angle)*speedX * 100) / 100;
        posY += Math.round(Math.sin(angle)*speedY * 100) / 100;
        setNeedlePoint();

        if (!isSpeedingUp){
            if (speedX > minSpeed){
                speedX = Math.max(speedX - speedIncrement, minSpeed);
                speedY = Math.max(speedY - speedIncrement, minSpeed);
            }
        }
        else{
            if (speedX < maxSpeed){
                speedX = Math.min(speedX + speedIncrement, maxSpeed);
                speedY = Math.min(speedY + speedIncrement, maxSpeed);
                // System.out.println(speedX);
            }
            if (speedX >= maxSpeed){
                isSpeedingUp = false;
                // System.out.println("stop speeding up");
            }
        }
    }

    public void toggleDash(){isSpeedingUp = true;}

    public void setAngle(double theta){angle = theta;}

    public void setAngleToIncidence(boolean vertical){
        if (vertical){
            angle = (-angle)+4*RAD90;
        }
        else{
            angle = (-angle)+2*RAD90;
        }
    }

    public void setNeedlePoint(){
        needleX = posX - Math.round(Math.cos(angle)*NEEDLEDIST * 100) / 100;
        needleY = posY - Math.round(Math.sin(angle)*NEEDLEDIST * 100) / 100;
    }
}
