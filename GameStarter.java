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
    This class starts the game from the client side.
    This contains the main method. 
    The player should run this class file to their terminal to start the game.
*/

public class GameStarter{
    public static void main(String[] args){
        GameFrame gameFrame = new GameFrame(Constants.FRAMEWIDTH, Constants.FRAMEHEIGHT);
        gameFrame.connectToServer();
        gameFrame.setKeyBindings();
        gameFrame.setUpGUI();
    }
}