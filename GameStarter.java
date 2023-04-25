public class GameStarter{
    public static void main(String[] args){
        GameFrame gameFrame = new GameFrame(960, 540);
        gameFrame.connectToServer();
        gameFrame.setKeyBindings();
        gameFrame.setUpGUI();
        
        // gameFrame.setUpListeners();
        
    }
}