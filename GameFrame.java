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
    This class contains all the codes for setting up the JFrame, key bindings, and a function to accept server connection.
    This class is instantiated on the GameStarter class. 
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameFrame{
    private int screenWidth, screenHeight;

    private JFrame gameFrame;
    private GameCanvas gameCanvas;
    private GameClient gameClient;

    // This constructor initalizes a JFrame, an instance of GameCanvas, and an instance of GameClient.
    public GameFrame(int width, int height){
        screenWidth = width; screenHeight = height;
        gameFrame = new JFrame();
        gameCanvas = new GameCanvas(width, height);
        gameClient = new GameClient();
        gameClient.setGameCanvas(gameCanvas);
    }
    
    // This class contains all the codes for setting up the JFrame, key bindings, and a function to accept server connection.
    // This class is instantiated on the GameStarter class. 
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

    // Sets the key bindings for the player. 
    public void setKeyBindings(){
        JPanel cp = (JPanel) gameFrame.getContentPane();
        cp.setFocusable(true);

        ActionMap am = cp.getActionMap();
        InputMap im = cp.getInputMap();

        am.put("cw", new MoveAction("cw"));
        am.put("ccw", new MoveAction("ccw"));
        am.put("stop", new MoveAction("stop"));
        am.put("startGame", new MoveAction("startGame"));
        am.put("restartGame", new MoveAction("restartGame"));

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false), "cw");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), "ccw");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), "stop");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), "stop");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false), "startGame");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0, false), "restartGame");
        
    }

    // Inner class to set instructions for each key in a clean way.
    // Copied from one of the key binding files in Canvas.
    private class MoveAction extends AbstractAction {
        private String command;

        public MoveAction(String com){
          command = com;
        }
    
        @Override
        public void actionPerformed(ActionEvent ae) {
            if (gameCanvas.getGameState() == 0){
                if (command.equals("startGame")){
                    gameCanvas.pressStart();
                }
            }
            else if (gameCanvas.getGameState() == 1){
                if (!command.equals("move") && !command.equals("stopMove")){
                    gameCanvas.getYou().setAngleMovement(command);
                }
            }
            else if (gameCanvas.getGameState() == 2){
                if (command.equals("restartGame")){
                    gameCanvas.pressRestart();
                }
            }
        }
    }

    // Connects client to server.
    public void connectToServer(){gameClient.connectToServer();}
}