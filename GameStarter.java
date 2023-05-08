public class GameStarter{
    public static void main(String[] args){
        GameFrame gameFrame = new GameFrame(Constants.FRAMEWIDTH, Constants.FRAMEHEIGHT);
        gameFrame.connectToServer();
        gameFrame.setKeyBindings();
        gameFrame.setUpGUI();
        
        // gameFrame.setUpListeners();
        
    }
}