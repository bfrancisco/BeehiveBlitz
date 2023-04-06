import java.awt.*;
import java.awt.geom.*;

public class Ball extends ObjectProperties {
    // (posX, posY) is the center of the ball.
    private double speedX, speedY;
    private Color fill, outlineFill;
    private BasicStroke outlineWidth;
    private Ellipse2D.Double ball;
    private double angle, angleSensitivity; // in radians
    private int angleMovement; // 0 - stable, 1 - clockwise, 2 - counterclockwise
    private boolean toMove; 

    public Ball(double x, double y, double w, double h, double sx, double sy, Color fill, Color outlineFill, int outlineWidth){
        super(x, y, w, h);
        speedX = sx;
        speedY = sy;
        this.fill = fill;
        this.outlineFill = outlineFill;
        this.outlineWidth = new BasicStroke(outlineWidth);
        angle = 0;
        angleSensitivity = 0.3;
        angleMovement = 0;
        toMove = false;
    }

    public void generateShape(){
        ball = new Ellipse2D.Double(posX-width/2, posY-height/2, width, height);
    }

    public void draw(Graphics2D g2d, AffineTransform reset){
        // System.out.println(posX + " " + posY);
        generateShape();
        g2d.rotate(angle, posX, posY);
        g2d.setPaint(fill);
        g2d.fill(ball);
        g2d.setPaint(outlineFill);
        g2d.setStroke(outlineWidth);
        g2d.draw(ball);
        g2d.setTransform(reset);
    }

    // Mutator Functions
    public void invertXDirection(){speedX *= -1;}
    public void invertYDirection(){speedY *= -1;}

    // For Controls
    public void setAngleMovement(String command){
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
    }
    public double getAngle(){return (angle*(Math.PI/180))%360;}
    
    public void setMovement(String command){
        if (command.equals("stopMove")) toMove = false;
        else if (command.equals("move")) toMove = true;
    }

    public boolean isMoving(){return toMove;}

    public void move(){
        if (toMove){
            posX += Math.cos(angle)*speedX;
            posY += Math.sin(angle)*speedY;
        }
    }
}
