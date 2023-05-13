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
    This class contains all the codes for the GameCanvas, or the JComponent of the program.
    This includes all the necessary functions for drawing the graphics of the game, and instances of the Player and Honey class.
 */

import javax.swing.*;
import java.awt.geom.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.net.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class GameCanvas extends JComponent{
    private int width, height;
    private int gameState; // 0 - home screen, 1 - game proper, 2 - you win/lose
    private Player you;
    private Player enemy;
    private int yourID;
    private Honey honey;
    private boolean enemyExists = false;
    private int dashTimer;

    private BufferedImage startMenuBG;
    private BufferedImage startMenuWait;
    private BufferedImage bgImage;
    private BufferedImage timeHexagon;
    private BufferedImage timeHexagonGlow;
    private BufferedImage youWinOverlay;
    private BufferedImage youLoseOverlay;
    private int menuCounter; // 0-menu bg, 1-menu waiting, 2-you win/lose
    
    private Font font;
    private Color blue, orange;

    // for Double-Buffering, in reference of Killer Game Programming in Java by Andrew Davidson
    private Graphics dbg;
    private Image dbImage = null;

    public GameCanvas(int w, int h){
        width = w;
        height = h;
        gameState = 0;
        this.setPreferredSize(new Dimension(width, height));
        
    }
    
    // Returns game state of GameCanvas instance
    public int getGameState(){
        return gameState;
    }

    // Sets game state of GameCanvas instance
    public void setGameState(int g){
        gameState = g;
        if (g == 2){
            menuCounter = 2;
        }   
    }

    // Sets player ID of GameCanvas instance
    public void setPlayerID(int i){
        yourID = i;
    }

    // Called when client pressed "S" when in the Menu Screen. 
    // Sets menuCounter to 1, which changes the menu background to "waiting" image.
    public void pressStart(){
        menuCounter = 1;
    }

    // Called when client pressed "R" when in the Result Screen.
    // Resets the initial values of all necessary variables for restarting the game.
    public void pressRestart(){
        menuCounter = 0;
        gameState = 0;
        you.setInitialStats();
        enemy.setInitialStats();
    }

    // Returns the state of the menu
    public int getCanvasState(){
        return menuCounter;
    }

    // Sets up all necessary sprites, including Player instances, images, and GUI-related elements.
    public void setUpSprites(){
        if (yourID == 1){
            you = new Player(width/2 - width/4, height/2, Constants.P1SPRITE, Constants.P1SPRITE2);
            enemy = new Player(width/2 + width/4, height/2, Constants.P2SPRITE, Constants.P2SPRITE2);
        }
        else if (yourID == 2){
            enemy = new Player(width/2 - width/4, height/2, Constants.P1SPRITE, Constants.P1SPRITE2);
            you = new Player(width/2 + width/4, height/2, Constants.P2SPRITE, Constants.P2SPRITE2);
        }
        honey = new Honey(-1, -1);
        font = new Font(Constants.FONTNAME, Font.PLAIN, Constants.FONTSZ);
        orange = Color.decode("#F4A134");
        blue = Color.decode("#52A4A8");
        try{
            startMenuBG = ImageIO.read(new File(Constants.STARTMENUBG));
            startMenuWait = ImageIO.read(new File(Constants.STARTMENUWAIT));
            menuCounter = 0;
            bgImage = ImageIO.read(new File(Constants.BGSPRITE));
            timeHexagon = ImageIO.read(new File(Constants.TIMEHEXAGON));
            timeHexagonGlow = ImageIO.read(new File(Constants.TIMEHEXAGONGLOW));
            youWinOverlay = ImageIO.read(new File(Constants.YOUWIN));
            youLoseOverlay = ImageIO.read(new File(Constants.YOULOSE));
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    // Draws image when in the Start Screen.
    public void drawStartMenu(Graphics2D g2d, AffineTransform af){
        if (menuCounter == 0)
            g2d.drawImage(startMenuBG, 0, 0, null);
        else if (menuCounter == 1)
            g2d.drawImage(startMenuWait, 0, 0, null);
        
    }

    // Draws image when in the Result Screen.
    public void drawResult(Graphics2D g2d, AffineTransform af){
        if (gameState == 2){
            if (you.getScore() == Constants.WINSCORE)
                g2d.drawImage(youWinOverlay, 0, 0, null);
            else
                g2d.drawImage(youLoseOverlay, 0, 0, null);
        }
        
    }

    // Draws background image during Play Screen.
    public void drawBG(Graphics2D g2d, AffineTransform af){
        g2d.drawImage(bgImage, 0, 0, null);
        
        int midX = width/2;

        // Draws timer indicators (honey sprites)
        if (gameState == 1){
            for (int i = 100; i <= dashTimer; i += 100){
                if (dashTimer < 300){
                    g2d.drawImage(timeHexagon, (midX - (timeHexagon.getWidth()/2)) - (73 * (2-(i/100)+1)), 75 - (timeHexagon.getHeight()/2), null);
                    g2d.drawImage(timeHexagon, (midX - (timeHexagon.getWidth()/2)) + (73 * (2-(i/100)+1)), 75 - (timeHexagon.getHeight()/2), null);
                }
                else if (300 <= dashTimer && dashTimer <= 400){
                    g2d.drawImage(timeHexagonGlow, (midX - (timeHexagonGlow.getWidth()/2)) - (73 * (2-(i/100)+1)), 75 - (timeHexagonGlow.getHeight()/2), null);
                    g2d.drawImage(timeHexagonGlow, (midX - (timeHexagonGlow.getWidth()/2)) + (73 * (2-(i/100)+1)), 75 - (timeHexagonGlow.getHeight()/2), null);
                }
            }
        }
        
        // Draws score
        String youScoreStr = Integer.toString(you.getScore());
        String enemyScoreStr = Integer.toString(enemy.getScore());
        FontMetrics metrics = g2d.getFontMetrics();
        g2d.setFont(font);
        if (yourID == 1){
            g2d.setPaint(orange);
            g2d.drawString(youScoreStr, midX - 295 - (metrics.stringWidth(youScoreStr)/2) - 8, 82 + (metrics.getHeight()/2));
            g2d.setPaint(blue);
            g2d.drawString(enemyScoreStr, midX + 295 - (metrics.stringWidth(enemyScoreStr)/2) - 8, 82 + (metrics.getHeight()/2));
        }
        else if (yourID == 2){
            g2d.setPaint(orange);
            g2d.drawString(enemyScoreStr, midX - 295 - (metrics.stringWidth(enemyScoreStr)/2) - 8, 82 + (metrics.getHeight()/2));
            g2d.setPaint(blue);
            g2d.drawString(youScoreStr, midX + 295 - (metrics.stringWidth(youScoreStr)/2) - 8, 82 + (metrics.getHeight()/2));
        }
        
        g2d.setTransform(af);
    }
    
    // In reference to "Killer Game Programming in Java" by Andrew Davidson.
    // Creates an empty image to be painted by all the elements included in one frame.
    private void gameRender(){
        // create empty image. this will be the image to be PAINTED at the end
        dbImage = createImage(width, height);
        if (dbImage == null){
            System.out.println("dbImage is null");
            return;
        }
        else{
            // get address of Graphics instance of dbg, so that dbImage will be painted by operating on its Graphics instance
            dbg = dbImage.getGraphics();
        }
        
        // convert dbg to Graphics2D to apply antialiasing
        Graphics2D g2d = (Graphics2D)dbg;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);
        AffineTransform af = g2d.getTransform();

        // clear background
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        // draw game elements
        if (gameState == 0){
            drawStartMenu(g2d, af);
        }
        else if (gameState > 0){
            drawBG(g2d, af);
            honey.draw(g2d, af);
            enemy.draw(g2d, af);
            you.draw(g2d, af);
            drawResult(g2d, af);
        }
            
        enemyExists = true;
        
    }

    // In reference to "Killer Game Programming in Java" by Andrew Davidson.
    // Paints generated image from gameRender to the program window.
    // Imitates what paintComponent does.
    private void paintScreen(){
        Graphics2D g2d;
        try{
            g2d = (Graphics2D) this.getGraphics(); // get Graphics of JComponent so that if we draw, it will show up on the GUI
            if ((g2d != null) && (dbImage != null)){
                g2d.drawImage(dbImage, 0, 0, null);
            }
            Toolkit.getDefaultToolkit().sync(); // spaghetti, something related to compatibility across all platforms
            g2d.dispose(); // spaghetti
        }
        catch (Exception e){
            System.out.println("Graphics context error: " + e);
        }

        // System.out.println(you.getScore() + " | " + enemy.getScore());
    }

    // Timer to constantly update the frames every 30 milliseconds.
    public void SetUpGameUpdate(){
        // https://stackoverflow.com/questions/25025715/javax-swing-timer-vs-java-util-timer-inside-of-a-swing-application
        new java.util.Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (gameState > 0){
                    you.moveAngle();
                    you.move();
                    handleBorderCollision();
                }
                gameRender();
                paintScreen();
            }}, 0, 30);
    }

    // Handles player collision when collided to window border.
    public void handleBorderCollision(){
        if (you.getX() < 0){
            you.setAngleToIncidence(false);
            you.setX(0);
        }
        else if (you.getY() < 0){
            you.setAngleToIncidence(true);
            you.setY(0);
        }
        else if (you.getX() > width){
            you.setAngleToIncidence(false);
            you.setX(width);
        }
        else if (you.getY() > height){
            you.setAngleToIncidence(true);
            you.setY(height);
        }
    }

    // Sets dash timer value. Used in GameClient.
    public void setDashTimer(int dT){
        dashTimer = dT;
    }

    // Getter methods; used in GameClient.
    
    public Player getYou(){
        return you;
    }
    public Player getEnemy(){
        return enemy;
    }
    public Honey getHoney(){
        return honey;
    }
    public boolean doesEnemyExists(){
        return enemyExists;
    }
}
