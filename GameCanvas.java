import javax.swing.*;
import java.awt.geom.*;
import java.awt.*;
import java.awt.event.*;

import java.io.*;
import java.net.*;

public class GameCanvas extends JComponent{
    private static final String P1SPRITE = "assets/player1.png";
    private static final String P2SPRITE = "assets/player2.png";

    private Color color = Color.BLACK;

    private int width, height;
    private Ball you;
    private Ball enemy;

    private boolean enemyExists = false;

    private int playerID;

    private Socket socket;

    private ReadFromServer rfsRunnable;
    private WriteToServer wtsRunnable;
    // private Timer movementTimer;
    // private double rad;
    // private int baseSpeed = 10;

    public GameCanvas(int w, int h){
        width = w;
        height = h;
        this.setPreferredSize(new Dimension(width, height));
    }

    public void setUpBG( Graphics2D g2d, AffineTransform af){
        // g2d.setPaint(Color.decode("#292B29"));
        g2d.setPaint(color);
        g2d.fillRect(0, 0, width, height);
        g2d.setTransform(af);
    }

    public void setUpSprites(){
        if (playerID == 1){
            you = new Ball(width/2 - width/4, height/2, 100, 50, (double)5, (double)5, P1SPRITE);
            enemy = new Ball(width/2 + width/4, height/2, 100, 50, (double)5, (double)5, P2SPRITE);
        }
        else{
            enemy = new Ball(width/2 - width/4, height/2, 100, 50, (double)5, (double)5, P1SPRITE);
            you = new Ball(width/2 + width/4, height/2, 100, 50, (double)5, (double)5, P2SPRITE);
        }
    }

    @Override
    protected void paintComponent(Graphics g){
        Graphics2D g2d = (Graphics2D)g;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);
        AffineTransform af = g2d.getTransform();
        
        setUpBG(g2d, af);
        you.draw(g2d, af);
        enemy.draw(g2d, af);
        
        enemyExists = true;

        double prevX = enemy.getX();
        double prevA = enemy.getAngle();

        // check if data is transferred
        if (enemy.getX() != prevX && enemy.getAngle() != prevA){
            System.out.println(enemy.getX());
            System.out.println(enemy.getY());
            System.out.println(enemy.getAngle());
        }
    }

    public void SetUpMovement(){
        Timer movementTimer = new Timer(20, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                you.moveAngle();
                you.move();
                repaint();
            }
        });
        movementTimer.start();
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
                    System.out.println("Data received");
                    // boolean dashBool = dataIn.readBoolean();
                    if (color == Color.BLACK) color = Color.RED;
                    else if (color == Color.RED) color = Color.BLACK;
                    // if (dashBool && enemyExists){
                        
                    //     System.out.println("Should dash now");
                    //     // enemy.setX(enemy.getX()+Math.cos(enemy.getAngle())*100);
                    //     // enemy.setY(enemy.getY()+Math.sin(enemy.getAngle())*100);
                        
                    //     // you.setX(you.getX()+Math.cos(you.getAngle())*100);
                    //     // you.setY(you.getY()+Math.sin(you.getAngle())*100);
                    // }
                    if (enemyExists){
                        // System.out.println("Enemy is not null");
                        
                        // System.out.println(ex);
                        // System.out.println(ey);
                        // System.out.println(eA);
                        enemy.setX(ex);
                        enemy.setY(ey);
                        enemy.setAngle(eA);
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
                        Thread.sleep(25);
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
    public Ball getBall(){return you;}
    public int getPlayerID(){return playerID;}
}
