import javax.swing.*;
import java.awt.geom.*;
import java.awt.*;
import java.awt.event.*;

public class GameCanvas extends JComponent{
    private int canvasWidth, canvasHeight;
    private Player player1;

    // private Timer movementTimer;
    // private double rad;
    // private int baseSpeed = 10;

    public GameCanvas(int width, int height){
        canvasWidth = width;
        canvasHeight = height;
    }

    @Override
    protected void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D)g;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);
        AffineTransform af = g2d.getTransform();
        
        player1.draw(g2d, af);
    }

    public Player getPlayer(int playerNum){
        return player1;
    }
}
