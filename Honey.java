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
    This class contains all the properties for the Honey sprite.
    This includes the logic for drawing the Honey sprite, 
    and Getter & Setter functions that is inherited from the ObjectProperties class.
*/

import java.awt.*;
import java.awt.geom.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Honey extends ObjectProperties {
    // (posX, posY) is the center.

    private BufferedImage sprite;
    
    // Initializes (x, y) coordinate, sprite image, width, and height.
    public Honey(double x, double y){
        super(x, y);
        try{
            sprite = ImageIO.read(new File(Constants.HONEY));
        } catch (IOException e){
            e.printStackTrace();
        }
        setWidth(sprite.getWidth());
        setHeight(sprite.getHeight());
    }

    // Draws the Honey sprite.
    public void draw(Graphics2D g2d, AffineTransform reset){
        if (posX < 0 && posY < 0) return; // if (x, y) are set to negative values, the honey sprite shouldn't be drawn. 

        g2d.translate(posX - sprite.getWidth(null)/2, posY - sprite.getHeight(null)/2);
        g2d.drawImage(sprite, 0, 0, null);
        g2d.setTransform(reset);
    }
}
