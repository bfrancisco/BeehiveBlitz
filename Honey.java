import java.awt.*;
import java.awt.geom.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Honey extends ObjectProperties {
    // (posX, posY) is the center.

    private BufferedImage sprite;
    
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

    public void draw(Graphics2D g2d, AffineTransform reset){
        if (posX < 0 && posY < 0) return;

        g2d.translate(posX - sprite.getWidth(null)/2, posY - sprite.getHeight(null)/2);
        g2d.drawImage(sprite, 0, 0, null);
        g2d.setTransform(reset);
    }
}
