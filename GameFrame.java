import javax.swing.*;

public class GameFrame{
    private int screenWidth, screenHeight;

    private JFrame gameFrame;
    private GameCanvas gameCanvas;

    public GameFrame(int width, int height){
        screenWidth = width; screenHeight = height;
        gameFrame = new JFrame();
        gameCanvas = new GameCanvas(width, height);
    }

    public void setUpGUI(){
        gameFrame.setSize(screenWidth, screenHeight);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gameFrame.add(gameCanvas);
        gameFrame.pack();
        gameFrame.setVisible(true);
    }
}