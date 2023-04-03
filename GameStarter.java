public class GameStarter{
    public static void main(String[] args){
        GameFrame gameFrame = new GameFrame(1280, 720);
        gameFrame.setUpGUI();
        gameFrame.setUpListeners();
        
    }
}