import java.awt.*;
import java.awt.geom.*;

public class Player extends ObjectProperties{

    private Ball ball;
    // private Arrow arrow;
    // private Trail trail; <-- can be in Ball class
    // private int power; <-- can be in Ball class
    // private double angle <-- can be in Ball class

    public Player(double x, double y, double w, double h){
        super(x, y, w, h);
        ball = new Ball(posX, posY, width, height, (double)5, (double)5, Color.BLUE, Color.BLACK, 1);
    }
    public void draw(Graphics2D g2d, AffineTransform reset){
        // draw everything here
        ball.draw(g2d, reset);
        
        g2d.setTransform(reset);
    }

    public void test(){
        System.out.println("eh eh eh eh ey");
    }

    
}