import javax.swing.*;
import java.awt.geom.*;
import java.awt.*;

public class GameCanvas extends JComponent{
    private int canvasWidth, canvasHeight;

    public GameCanvas(int width, int height){
        canvasWidth = width; canvasHeight = height;
        setPreferredSize(new Dimension(canvasWidth, canvasHeight));
    }
    @Override
    protected void paintComponent(Graphics g){
        
        //placeholder to ensure it works
        g.setColor(Color.BLUE);
        g.fillRect(10,10,100,200);
    }
}
