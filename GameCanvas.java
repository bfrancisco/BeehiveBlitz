import javax.swing.*;
import java.awt.geom.*;
import java.awt.*;
import java.awt.event.*;


public class GameCanvas extends JComponent{
    private int width, height;
    private Ball ball;
    // private Timer movementTimer;
    // private double rad;
    // private int baseSpeed = 10;

    public GameCanvas(int w, int h){
        width = w;
        height = h;
        this.setPreferredSize(new Dimension(width, height));

        ball = new Ball(width/2, height/2, 100, 50, (double)5, (double)5, Color.BLUE, Color.BLACK, 1);
    }

    @Override
    protected void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D)g;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);
        AffineTransform af = g2d.getTransform();
        
        ball.draw(g2d, af);
        // System.out.println("repainting");
    }

    public void SetUpMovement(){
        Timer movementTimer = new Timer(20, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                ball.moveAngle();
                ball.move();
                repaint();
            }
        });
        movementTimer.start();
    }

    public Ball getBall(){
        return ball;
    }
}
