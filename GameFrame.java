import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameFrame{
    private int screenWidth, screenHeight;

    private JFrame gameFrame;
    private GameCanvas gameCanvas;
    private GameClient gameClient;

    public GameFrame(int width, int height){
        screenWidth = width; screenHeight = height;
        gameFrame = new JFrame();
        gameCanvas = new GameCanvas(width, height);
        gameClient = new GameClient();
        gameClient.setGameCanvas(gameCanvas);
    }

    public void setUpGUI(){
        gameFrame.setTitle("Beehive Blitz: Player# " + gameClient.getPlayerID());
        gameCanvas.setPreferredSize(new Dimension(screenWidth, screenHeight));
        gameCanvas.setPlayerID(gameClient.getPlayerID());

        gameFrame.add(gameCanvas);
        gameFrame.pack();
        gameCanvas.setUpSprites();
        gameCanvas.SetUpGameUpdate();
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setVisible(true);
        gameFrame.setResizable(false);
    }

    public void setKeyBindings(){
        JPanel cp = (JPanel) gameFrame.getContentPane();
        cp.setFocusable(true);

        ActionMap am = cp.getActionMap();
        InputMap im = cp.getInputMap();

        am.put("cw", new MoveAction("cw"));
        am.put("ccw", new MoveAction("ccw"));
        am.put("stop", new MoveAction("stop"));
        am.put("startGame", new MoveAction("startGame"));
        
        am.put("print", new MoveAction("print"));

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false), "cw");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), "ccw");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), "stop");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), "stop");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false), "startGame");

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, 0, false), "print");
        
    }

    private class MoveAction extends AbstractAction {
        private String command;

        public MoveAction(String com){
          command = com;
        }
    
        @Override
        public void actionPerformed(ActionEvent ae) {
            
            if (gameCanvas.getGameState() == 1){
                if (command.equals("print")){
                    System.out.print(gameCanvas.getYou().getX());
                    System.out.print(" " +  gameCanvas.getYou().getY() + " " + gameCanvas.getYou().getAngle() + "\n");
                }
                else if (!command.equals("move") && !command.equals("stopMove")){
                    gameCanvas.getYou().setAngleMovement(command);
                }
            }
            else if (gameCanvas.getGameState() == 0){
                if (command.equals("startGame")){
                    System.out.println("Game started");
                    gameCanvas.setGameState(1);
                }
            }
        }
    }

    public void connectToServer(){gameClient.connectToServer();}
}