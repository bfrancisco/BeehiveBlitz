import java.awt.*;
import java.awt.geom.*;

public abstract class ObjectProperties {
    protected double posX;
    protected double posY;
    protected double width;
    protected double height;

    public ObjectProperties(double x, double y){
        posX = x;
        posY = y;
    }

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

    //Setter Functions
    public void setX(double v){posX = v;}
    public void setY(double v){posY = v;}
    public void setWidth(double w){width = w;}
    public void setHeight(double h){height = h;}

    abstract void draw(Graphics2D g2d, AffineTransform reset);
}
