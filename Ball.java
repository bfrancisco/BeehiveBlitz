import java.awt.*;
import java.awt.geom.*;

public class Ball extends ObjectProperties {
    // (posX, posY) is the center of the ball.
    private static final double RAD90 = 1.57; // 1.570796327
    private static final double ANGLESENS = 0.2;

    private double speedX, speedY;
    private double angle, angleSensitivity; // in radians
    private int angleMovement; // 0 - stable, 1 - clockwise, 2 - counterclockwise
    private boolean toMove;

    Toolkit t = Toolkit.getDefaultToolkit();
    private Image sprite;

    public Ball(double x, double y, double w, double h, double sx, double sy, String spritefile){
        super(x, y, w, h);
        speedX = sx;
        speedY = sy;
        angle = RAD90;
        angleSensitivity = ANGLESENS;
        angleMovement = 0;
        toMove = false;
        sprite = t.getImage(spritefile);
    }


    public void draw(Graphics2D g2d, AffineTransform reset){
        // System.out.println(posX + " " + posY);
        g2d.rotate(angle, posX, posY);
        // g2d.setPaint(fill);
        // g2d.fill(ball);
        // g2d.setPaint(outlineFill);
        // g2d.setStroke(outlineWidth);
        // g2d.draw(ball);
        // g2d.setTransform(reset);

        g2d.drawImage(sprite, (int)posX - sprite.getWidth(null)/2, (int)posY - sprite.getHeight(null)/2, null);
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

    public boolean isMoving(){return toMove;}

    public void move(){
        if (toMove){
            posX += Math.round(Math.cos(angle)*speedX * 100) / 100;
            posY += Math.round(Math.sin(angle)*speedY * 100) / 100;
        }
    }
    public void setAngle(double theta){angle = theta;}
}
