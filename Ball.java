import java.awt.*;
import java.awt.geom.*;

public class Ball extends ObjectProperties {
    // (posX, posY) is center of the ellipse.
    private double posX, posY, width, height;
    private double speedX, speedY;
    private Color fill;
    private Color outlineFill;
    private BasicStroke outlineWidth;
    private Ellipse2D.Double ball;

    public Ball(double x, double y, double w, double h, double sx, double sy, Color fill, Color outlineFill, int outlineWidth){
        super(x, y, w, h);
        speedX = sx;
        speedY = sy;
        this.fill = fill;
        this.outlineFill = outlineFill;
        this.outlineWidth = new BasicStroke(outlineWidth);
        generateShape();
    }

    public void generateShape(){
        ball = new Ellipse2D.Double(posX-width/2, posY-height/2, width, height);
    }

    public void draw(Graphics2D g2d, AffineTransform reset){
        g2d.setPaint(fill);
        g2d.fill(ball);
        g2d.setPaint(outlineFill);
        g2d.setStroke(outlineWidth);
        g2d.draw(ball);
        g2d.setTransform(reset);
    }

    // mutator functions
    public void adjustX(){posX += speedX;}
    public void adjustY(){posY += speedY;}
    public void invertXDirection(){speedX *= -1;}
    public void invertYDirection(){speedY *= -1;}
    
}
