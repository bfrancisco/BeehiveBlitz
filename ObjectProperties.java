import java.awt.*;
import java.awt.geom.*;

public abstract class ObjectProperties {
    protected double posX;
    protected double posY;
    protected double width;
    protected double height;

    public ObjectProperties(double x, double y, double w, double h){
        posX = x;
        posY = y;
        width = w;
        height = h;
    }

    // Getter Functions
    public double getX() {return posX;}
    public double getY() {return posY;}
    public double getWidth() {return width;}
    public double getHeight() {return height;}

    //Mutator Functions
    public void addX(double v){posX += v;}
    public void addY(double v){posY += v;}

    abstract void draw(Graphics2D g2d, AffineTransform reset);
}
