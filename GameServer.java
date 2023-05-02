import java.io.*;
import java.net.*;
import javax.swing.Timer;
import java.awt.event.*;

public class GameServer{
    
    private ServerSocket ss;
    private int numPlayers;
    private int maxPlayers;

    private Socket p1Socket;
    private Socket p2Socket;

    private ReadFromClient p1readRunnable;
    private ReadFromClient p2readRunnable;
    
    private WriteToClient p1writeRunnable;
    private WriteToClient p2writeRunnable;

    // player coordinates and angle
    private double p1x, p1y, p2x, p2y, p1a, p2a;

    private int dashTimer;

    public GameServer(){
        p1x = 150;
        p1y = p2y = 200;
        p2x = 450;
        p1a = p2a = -1.570796327;
        dashTimer = 0;

        System.out.println("server is running");
        numPlayers = 0;
        maxPlayers = 2;
        try{
            ss = new ServerSocket(51734);
        }catch (IOException ex){
             System.out.println("IOException from GameServer Constructor");
        }
    }

    public void acceptConnections(){
        try{
            System.out.println("waiting");
            while (numPlayers < maxPlayers){
                Socket s = ss.accept();
                DataInputStream in  = new DataInputStream(s.getInputStream());
                DataOutputStream out = new DataOutputStream(s.getOutputStream());
                
                numPlayers++;
                out.writeInt(numPlayers);
                System.out.println("Player #" + numPlayers + " has entered");
                
                ReadFromClient rfc = new ReadFromClient(numPlayers, in);
                WriteToClient wtc = new WriteToClient(numPlayers, out);

                if (numPlayers == 1){
                    p1Socket = s; 
                    p1readRunnable = rfc; 
                    p1writeRunnable = wtc;
                } else {
                    p2Socket = s;
                    p2readRunnable = rfc;
                    p2writeRunnable = wtc;

                    p1writeRunnable.sendStartMsg();
                    p2writeRunnable.sendStartMsg();

                    Thread readThread1 = new Thread(p1readRunnable);
                    Thread readThread2 = new Thread(p2readRunnable);
                    readThread1.start();
                    readThread2.start();
                    
                    Thread writeThread1 = new Thread(p1writeRunnable);
                    Thread writeThread2 = new Thread(p2writeRunnable);
                    writeThread1.start();
                    writeThread2.start();

                }
            }
            System.out.println("Lobby is full");
            TimerIncrement ti = new TimerIncrement();
            Thread timerThread = new Thread(ti);
            timerThread.start();
        }catch (IOException ex){
            System.out.println("IOException from acceptConnections");
        }
    }

    private class TimerIncrement implements Runnable{
        public void run(){
            while (true){
                dashTimer++;
                if (dashTimer == 201){dashTimer = 1;}
                try{
                    Thread.sleep(30);
                }catch (InterruptedException ex){
                    System.out.println("timerincrement is not running");
                }
            }
        }
    }

    private class ReadFromClient implements Runnable{

        private int playerID;
        private DataInputStream dataIn;

        public ReadFromClient(int pid, DataInputStream in){
            playerID = pid;
            dataIn = in;
            System.out.println("RFC" + playerID + " Runnable created");
        }

        public void run(){
            try{
                while (true){
                    if (playerID == 1){
                        p1x = dataIn.readDouble();
                        p1y = dataIn.readDouble();
                        p1a = dataIn.readDouble();
                    }else{
                        p2x = dataIn.readDouble();
                        p2y = dataIn.readDouble();
                        p2a = dataIn.readDouble();
                        // System.out.println(p2x);
                        // System.out.println(p2y);
                        // System.out.println(p2a);
                    }
                }
            }catch (IOException ex){
                System.out.println("IOException from RFC run()");
            }
        }
    }

    private class WriteToClient implements Runnable{

        private int playerID;
        private DataOutputStream dataOut;

        public WriteToClient(int pid, DataOutputStream out){
            playerID = pid;
            dataOut = out;
            System.out.println("WTC" + playerID + " Runnable created");
        }
        
        public void run(){
            try{
                while(true){
                    // System.out.println(playerID);
                    if(playerID == 1){
                        // System.out.println(p2x);
                        // System.out.println(p2y);
                        // System.out.println(p2a);
                        
                        dataOut.writeDouble(p2x);
                        dataOut.writeDouble(p2y);
                        dataOut.writeDouble(p2a);
                        dataOut.flush();
                    }
                    else{
                        // System.out.println(p1x);
                        // System.out.println(p1y);
                        // System.out.println(p1a);
                        dataOut.writeDouble(p1x);
                        dataOut.writeDouble(p1y);
                        dataOut.writeDouble(p1a);
                    }
                    dataOut.writeInt(dashTimer);
                    // System.out.println(dashTimer);
                    try{
                        Thread.sleep(30);
                    }catch (InterruptedException ex){
                        System.out.println("InterruptedException from wtc run");
                    }
                }
            }catch (IOException ex){
                System.out.println("IOException from wtc run");
            }
        }

        public void sendStartMsg(){
            try{
                dataOut.writeUTF("We now have two players");
            }catch (IOException ex){
                System.out.println("IOException from sendmessage");
            }
        }
    }

    public static void main (String[] args){
        GameServer gs = new GameServer();
        gs.acceptConnections();
    }
}
