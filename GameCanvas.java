import javax.swing.*;
import java.awt.geom.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.net.*;

public class GameCanvas extends JComponent{
    private static final String P1SPRITE = "assets/player1.png";
    private static final String P2SPRITE = "assets/player2.png";

    private int width, height;
    private Player you;
    private Player enemy;
    private boolean enemyExists = false;

    // private Color color = Color.BLACK;

    private int playerID;
    private Socket socket;
    private ReadFromServer rfsRunnable;
    private WriteToServer wtsRunnable;
    
    // for Double-Buffering, in reference of Killer Game Programming in Java by Andrew Davidson
    private Graphics dbg;
    private Image dbImage = null;


    public GameCanvas(int w, int h){
        width = w;
        height = h;
        this.setPreferredSize(new Dimension(width, height));
    }

    public void setUpBG( Graphics2D g2d, AffineTransform af){
        g2d.setPaint(Color.decode("#292B29"));
        // g2d.setPaint(color);
        g2d.fillRect(0, 0, width, height);
        g2d.setTransform(af);
    }

    public void setUpSprites(){
        if (playerID == 1){
            you = new Player(width/2 - width/4, height/2, 100, 50, 4, 4, 8, 64, P1SPRITE);
            enemy = new Player(width/2 + width/4, height/2, 100, 50, 4, 4, 8, 64, P2SPRITE);
        }
        else{
            enemy = new Player(width/2 - width/4, height/2, 100, 50, 4, 4, 8, 64, P1SPRITE);
            you = new Player(width/2 + width/4, height/2, 100, 50, 4, 4, 8, 64, P2SPRITE);
        }
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
        setUpBG(g2d, af);
        enemy.draw(g2d, af);
        you.draw(g2d, af);

        // idk ivan ikaw magtanggal at magdebug pag tinanggal mo na to
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

    public void connectToServer(){
        System.out.println("Client");
        try{
            socket = new Socket("localhost", 51734);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            playerID = in.readInt();
            System.out.println("Connected as p#" + playerID);
            rfsRunnable = new ReadFromServer(in);
            wtsRunnable = new WriteToServer(out);
            rfsRunnable.waitForStartMsg();
        }catch (IOException ex){
            System.out.println("Server not found");
        }
    }

    private class ReadFromServer implements Runnable{

        private DataInputStream dataIn;

        public ReadFromServer(DataInputStream in){
            dataIn = in;
            System.out.println("RFS Runnable created");
        }

        public void run(){
            try{
                while (true){
                    double ex = dataIn.readDouble();
                    double ey = dataIn.readDouble();
                    double eA = dataIn.readDouble();
                    // System.out.println("Data received");
                    int dashBool = dataIn.readInt();
                    // System.out.println(dashBool);   
                    
                    if (enemyExists){
                        enemy.setX(ex);
                        enemy.setY(ey);
                        enemy.setAngle(eA);
                    }
                    if (dashBool >= 195 && enemyExists){
                        // if (color == Color.BLACK) color = Color.RED;
                        // else if (color == Color.RED) color = Color.BLACK;
                        you.setMaxSpeed();
                        enemy.setMaxSpeed();
                        // System.out.println(Math.round(Math.toDegrees(you.getAngle())));
                        // System.out.println("Should dash now : " + dashBool);
                    }
        
                }
            }catch (IOException ex){
                System.out.println("IOException from RFS run");
            }
        }

        public void waitForStartMsg(){
            try{
                String startMsg = dataIn.readUTF();
                System.out.println("Message from server: " + startMsg);
                
                Thread readThread = new Thread(rfsRunnable);
                Thread writeThread = new Thread(wtsRunnable);
                readThread.start();
                writeThread.start();
                
            }catch (IOException ex){
                System.out.println("IOException for wait for start");
            }
        }
    }

    private class WriteToServer implements Runnable{

        private DataOutputStream dataOut;

        public WriteToServer(DataOutputStream out){
            dataOut = out;
            System.out.println("WTS Runnable created");
        }

        public void run(){
            try{
                while (true){
                    if (enemyExists){
                        dataOut.writeDouble(you.getX());
                        dataOut.writeDouble(you.getY());
                        dataOut.writeDouble(you.getAngle());
                        dataOut.flush();
                    }
                    try{
                        Thread.sleep(30);
                    }catch (InterruptedException ex){
                        System.out.println("InterruptedException fr wts run");
                    }
                }
            } catch (IOException ex){
                System.out.println("IOException from wts run");
            }
        }
    }

    // get methods
    public Player getPlayer(){return you;}
    public int getPlayerID(){return playerID;}
}
