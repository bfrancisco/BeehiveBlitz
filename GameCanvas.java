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
    private Honey honey;
    private boolean enemyExists = false;
    private int dashTimer; 

    private int yourID;

    private BufferedImage bgImage;
    private BufferedImage timeHexagon;
    private BufferedImage timeHexagonGlow;
    private Font font, fontOffset;
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

    public void setGameState(int g){
        gameState = g;
    }

    public void setPlayerID(int i){
        yourID = i;
    }

    public void setUpSprites(){
        if (yourID == 1){
            you = new Player(width/2 - width/4, height/2, Constants.NORMALSPEED, Constants.SPEEDINCREMENT, Constants.MAXSPEED, Constants.P1SPRITE, Constants.P1SPRITE2);
            enemy = new Player(width/2 + width/4, height/2, Constants.NORMALSPEED, Constants.SPEEDINCREMENT, Constants.MAXSPEED, Constants.P2SPRITE, Constants.P2SPRITE2);
        }
        else if (yourID == 2){
            enemy = new Player(width/2 - width/4, height/2, Constants.NORMALSPEED, Constants.SPEEDINCREMENT, Constants.MAXSPEED, Constants.P1SPRITE, Constants.P1SPRITE2);
            you = new Player(width/2 + width/4, height/2, Constants.NORMALSPEED, Constants.SPEEDINCREMENT, Constants.MAXSPEED, Constants.P2SPRITE, Constants.P2SPRITE2);
        }
        honey = new Honey(-1, -1);
        font = new Font(Constants.FONTNAME, Font.PLAIN, Constants.FONTSZ);
        orange = Color.decode("#F4A134");
        blue = Color.decode("#52A4A8");
        try{
            bgImage = ImageIO.read(new File(Constants.BGSPRITE));
            timeHexagon = ImageIO.read(new File(Constants.TIMEHEXAGON));
            timeHexagonGlow = ImageIO.read(new File(Constants.TIMEHEXAGONGLOW));
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public void drawBG( Graphics2D g2d, AffineTransform af){
        g2d.drawImage(bgImage, 0, 0, null);
        
        // draw timer indicators
        int midX = width/2;
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
        
        // draw score
        // System.out.println(you.getScore() + " | " + enemy.getScore());
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
    
    private void gameRender(){
        // mostly from Killer Game Programming in Java by Andrew Davidson

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
        drawBG(g2d, af);
        honey.draw(g2d, af);
        enemy.draw(g2d, af);
        you.draw(g2d, af);
        
        enemyExists = true;
        
    }

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

    public void SetUpGameUpdate(){
        // https://stackoverflow.com/questions/25025715/javax-swing-timer-vs-java-util-timer-inside-of-a-swing-application
        new java.util.Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                you.moveAngle();
                you.move();
                handleBorderCollision();
                gameRender();
                paintScreen();
            }}, 0, 30);
    }

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
    public void setDashTimer(int dT){
        dashTimer = dT;
    }
    // get methods
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
