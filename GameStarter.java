public class GameStarter{
    public static void main(String[] args){
        GameFrame gameFrame = new GameFrame(600, 400);
        gameFrame.connectToServer();
        gameFrame.setUpGUI();
        gameFrame.setKeyBindings();
        // gameFrame.setUpListeners();
        
    }
}