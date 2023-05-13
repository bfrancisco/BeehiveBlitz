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
    This class is an abstract class used to store X position, Y position, width, and height of a sprite.
    This is inherited by the Player class and the Honey class.
*/

import java.awt.*;
import java.awt.geom.*;

public abstract class ObjectProperties {
    protected double posX;
    protected double posY;
    protected double width;
    protected double height;

    // A constructor to set the initial values of X and Y positions.
    public ObjectProperties(double x, double y){
        posX = x;
        posY = y;
    }

    // Getter functions
    public double getX() {return posX;}
    public double getY() {return posY;}
    public double getWidth() {return width;}
    public double getHeight() {return height;}

    // Setter functions
    public void setX(double v){posX = v;}
    public void setY(double v){posY = v;}
    public void setWidth(double w){width = w;}
    public void setHeight(double h){height = h;}

    // Abstract function to ensure that all subclass has a draw method.
    abstract void draw(Graphics2D g2d, AffineTransform reset);
}
