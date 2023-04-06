import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameFrame{
    private int screenWidth, screenHeight;

    private JFrame gameFrame;
    private GameCanvas gameCanvas;

    // private Timer movementTimer;

    public GameFrame(int width, int height){
        screenWidth = width; screenHeight = height;
        gameFrame = new JFrame();
        gameCanvas = new GameCanvas(width, height);
    }

    public void setUpGUI(){
        gameFrame.setTitle("Base Rush");
        gameCanvas.setPreferredSize(new Dimension(screenWidth, screenHeight));
        
        gameFrame.add(gameCanvas);
        gameFrame.pack();
        gameCanvas.SetUpMovement();
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setVisible(true);
        
    }

    public void setKeyBindings(){
        JPanel cp = (JPanel) gameFrame.getContentPane();
        cp.setFocusable(true);

        ActionMap am = cp.getActionMap();
        InputMap im = cp.getInputMap();

        am.put("cw", new MoveAction("cw"));
        am.put("ccw", new MoveAction("ccw"));
        am.put("stop", new MoveAction("stop"));

        am.put("print", new MoveAction("print"));

        am.put("move", new MoveAction("move"));
        am.put("stopMove", new MoveAction("stopMove"));

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false), "cw");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), "ccw");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), "stop");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), "stop");

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, 0, false), "print");

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false), "move");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true), "stopMove");
        
    }

    private class MoveAction extends AbstractAction {
        private String command;

        public MoveAction(String com){
          command = com;
        }
    
        @Override
        public void actionPerformed(ActionEvent ae) {
            
            if (command.equals("print")){
                System.out.print(gameCanvas.getBall().getX());
                System.out.print(" " +  gameCanvas.getBall().getY() + gameCanvas.getBall().getMovement()+ "\n");
            }
            else if (!command.equals("move") && !command.equals("stopMove")){
                gameCanvas.getBall().setAngleMovement(command);
            }
            else{
                gameCanvas.getBall().setMovement(command);
            }
        }
    }
    
}