import javax.swing.*;
import java.awt.geom.*;
import java.awt.*;
import java.awt.event.*;

public class GameCanvas extends JComponent{
    private int canvasWidth, canvasHeight;
    private Player player1;

    private Timer movementTimer;
    // private double rad;
    private int baseSpeed = 10;

    public GameCanvas(int width, int height){
        canvasWidth = width; canvasHeight = height;
        player1 = new Player(50, 50);
    }
    @Override
    protected void paintComponent(Graphics g){
        player1.draw(g);
    }

    public Player getPlayer(int playerNum){
        return player1;
    }
}
