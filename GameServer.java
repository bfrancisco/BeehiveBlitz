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
    The game server class handles receving data from both players.
    It then sends the necessary info to the other player.
*/

import java.io.*;
import java.net.*;
import java.util.Random;

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

    private int p1gs, p2gs, serverGameState; 
    // player coordinates, angle, and needle coordinates
    private double p1x, p1y, p2x, p2y, p1a, p2a, p1nx, p1ny, p2nx, p2ny;
    private int dashTimer;
    private int p1s, p2s;
    private int hx, hy;
    int p1Hitsp2, p2Hitsp1;
    int p1GetsH, p2GetsH;

    //Initializes the game server and creates the server socket that both players would connect to.
    public GameServer(){
        setInitialGameStats();
        numPlayers = 0;
        maxPlayers = 2;
        System.out.println("server is running");
        try{
            System.out.println("socket not yet created");
            ss = new ServerSocket(24396);
            System.out.println("socket created");
        }
        catch (IOException ex){
             System.out.println("IOException from GameServer Constructor");
        }
    }

    // Sets the initial stats for the game.
    public void setInitialGameStats(){
        p1gs = p2gs = serverGameState = 0;
        p1x = 150;
        p1y = p2y = 200;
        p2x = 450;
        p1a = p2a = -1.570796327;
        dashTimer = 0;
        hx = hy = -1;
        
    }

    // This method accepts connecting players into the server. 
    // Once both players are connected, it starts the read and write threads that will handle receiving and sending data.
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

    /*
        This inner class is a timer that controls when the bees will dash and when the honeys will spawn.
    */ 
    private class TimerIncrement implements Runnable{
        // Starts the thread that will start the timer.
        public void run(){
        Random rand = new Random();
            while (true){
                dashTimer++;
                if (dashTimer == Constants.DASHLIMIT+1){
                    dashTimer = 1;
                    hx = hy = -100;
                }
                else if (dashTimer == Constants.DASHLIMIT/2){
                    hx = rand.nextInt(Constants.FRAMEWIDTH - 200) + 100;
                    hy = rand.nextInt(Constants.FRAMEHEIGHT - 200) + 100;
                }
                try{
                    Thread.sleep(10);
                }catch (InterruptedException ex){
                    System.out.println("timerincrement is not running");
                }
            }
        }
    }


    /*
        This class handles reading information from the two player clients.
    */
    private class ReadFromClient implements Runnable{
        private int playerID;
        private DataInputStream dataIn;

        //Sets the DataInputStream that would be used to read information from the client.
        public ReadFromClient(int pid, DataInputStream in){
            playerID = pid;
            dataIn = in;
            System.out.println("RFC" + playerID + " Runnable created");
        }

        //Starts the thread that will read information from the client.
        public void run(){
            try{
                while (true){
                    if (playerID == 1) {
                        p1gs = dataIn.readInt();
                        p1x = dataIn.readDouble();
                        p1y = dataIn.readDouble();
                        p1a = dataIn.readDouble();
                        p1nx = p1x - Math.round(Math.cos(p1a)*Constants.NEEDLEDIST * 100) / 100;
                        p1ny = p1y - Math.round(Math.sin(p1a)*Constants.NEEDLEDIST * 100) / 100;
                        p1s = dataIn.readInt();
                    }
                    else if (playerID == 2) {
                        p2gs = dataIn.readInt();
                        p2x = dataIn.readDouble();
                        p2y = dataIn.readDouble();
                        p2a = dataIn.readDouble();
                        p2nx = p2x - Math.round(Math.cos(p2a)*Constants.NEEDLEDIST * 100) / 100;
                        p2ny = p2y - Math.round(Math.sin(p2a)*Constants.NEEDLEDIST * 100) / 100;
                        p2s = dataIn.readInt();
                    }

                    //Syncs the gamestate of the server to the client and vice-versa.
                    if (serverGameState == 0 && p1gs == 1 && p2gs == 1){
                        setInitialGameStats();
                        serverGameState = 1;
                    }
                    else if (serverGameState == 1 && p1gs == 2 && p2gs == 2){
                        serverGameState = 2;
                    }
                    else if (serverGameState == 2 && p1gs == 0 && p2gs == 0){
                        setInitialGameStats();
                    }

                }
            }catch (IOException ex){
                System.out.println("IOException from RFC run()");
            }
        }
    }

    /*
        This class handles writing data from the server to the client.
        Relevant data such as the position of the other player, honey location, and dash timer is sent to the client.
     */
    private class WriteToClient implements Runnable{
        private int playerID;
        private DataOutputStream dataOut;

        // Sets the playerID to which to write to, and the DataOutputStream that will be used to send data.
        public WriteToClient(int pid, DataOutputStream out){
            playerID = pid;
            dataOut = out;
            System.out.println("WTC" + playerID + " Runnable created");
        }
        
        // Helper method that computes the distance between two points.
        public double getDistance(double x1, double y1, double x2, double y2){
            return Math.round(( Math.sqrt( Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2) ) ) * 100) / 100;
        }

        // Starts the thread that will send data to the client.
        public void run(){
            try{
                while(true){
                    p1Hitsp2 = p2Hitsp1 = p1GetsH = p2GetsH = 0;                 
                    
                    // Checks if a player has already won and changes the gamestate appropriately.
                    if (serverGameState == 1 && p1s == Constants.WINSCORE || p2s == Constants.WINSCORE){
                        serverGameState = 2;
                    }
                    
                    if (serverGameState == 1){
                        // Check bee collision
                        if (getDistance(p1x, p1y, p2nx, p2ny) <= Constants.BODYRADIUS){
                            p2Hitsp1 = 1;
                        }
                        if (getDistance(p2x, p2y, p1nx, p1ny) <= Constants.BODYRADIUS){
                            p1Hitsp2 = 1;
                        }

                        // Check honey collision
                        double p1toH = getDistance(p1x, p1y, (double)hx, (double)hy);
                        double p2toH = getDistance(p2x, p2y, (double)hx, (double)hy);
                        if (Math.min(p1toH, p2toH) <= Constants.HONEYRAD){
                            if (p1toH <= p2toH)
                                p1GetsH = 1;
                            else
                                p2GetsH = 1;
                        }
                    }
                    

                    dataOut.writeInt(serverGameState);
                    if (playerID == 1){
                        dataOut.writeDouble(p2x);
                        dataOut.writeDouble(p2y);
                        dataOut.writeDouble(p2a);
                        // if i got punctured
                        dataOut.writeInt(p2Hitsp1);
                        // if i punctured the enemy
                        dataOut.writeInt(p1Hitsp2);
                        // if i got honey
                        dataOut.writeInt(p1GetsH);
                        // if enemy got honey
                        dataOut.writeInt(p2GetsH);
                    }
                    else if (playerID == 2){
                        dataOut.writeDouble(p1x);
                        dataOut.writeDouble(p1y);
                        dataOut.writeDouble(p1a);
                        // if i got punctured
                        dataOut.writeInt(p1Hitsp2);
                        // if i punctured the enemy
                        dataOut.writeInt(p2Hitsp1);
                        // if i got honey
                        dataOut.writeInt(p2GetsH);
                        // if enemy got honey
                        dataOut.writeInt(p1GetsH);

                    }
                    dataOut.writeInt(dashTimer);
                    dataOut.writeInt(hx);
                    dataOut.writeInt(hy);
                    dataOut.flush();
                    try{
                        Thread.sleep(10);
                    }catch (InterruptedException ex){
                        System.out.println("InterruptedException from wtc run");
                    }
                }
            }catch (IOException ex){
                System.out.println("IOException from wtc run");
            }
        }

        // Sends the start message that would allow the two clients to "launch" or open the game window.
        public void sendStartMsg(){
            try{
                dataOut.writeUTF("We now have two players");
            }catch (IOException ex){
                System.out.println("IOException from sendmessage");
            }
        }
    }
    // Main method of the server.
    public static void main (String[] args){
        GameServer gs = new GameServer();
        gs.acceptConnections();
    }
}
