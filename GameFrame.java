import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameFrame{
    private int screenWidth, screenHeight;

    private JFrame gameFrame;
    private GameCanvas gameCanvas;

    private Timer movementTimer;

    public GameFrame(int width, int height){
        screenWidth = width; screenHeight = height;
        gameFrame = new JFrame();
        gameCanvas = new GameCanvas(width, height);
    }

    public void setUpGUI(){
        gameCanvas.setPreferredSize(new Dimension(screenWidth, screenHeight));
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.add(gameCanvas);
    
        gameFrame.pack();
        gameFrame.setVisible(true);
    }

    private class TimeListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent ae){
            Object o = ae.getSource();
            if (o == movementTimer){
                if(gameCanvas.getPlayer(1).getX() + gameCanvas.getPlayer(1).getWidth() >= screenWidth) {
                    gameCanvas.getPlayer(1).invertXDirection();
                }else if(gameCanvas.getPlayer(1).getX() <= 0) {
                    gameCanvas.getPlayer(1).invertXDirection();
                }gameCanvas.getPlayer(1).adjustX();

                if(gameCanvas.getPlayer(1).getY() + gameCanvas.getPlayer(1).getHeight() >= screenHeight) {
                    gameCanvas.getPlayer(1).invertYDirection();
                }else if(gameCanvas.getPlayer(1).getY() <= 0) {
                    gameCanvas.getPlayer(1).invertYDirection();
                }gameCanvas.getPlayer(1).adjustY();
                gameCanvas.repaint();
            }
        }
    }
    public void setUpListeners(){
        TimeListener tl = new TimeListener();
        movementTimer = new Timer(20, tl);
        movementTimer.start();
    }
}