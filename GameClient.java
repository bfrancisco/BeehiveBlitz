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
    This class contains all the codes for the GameClient which handles reading and writing data to and from the server.
    The methods and inner classes this class contain are either required to handle data to send towards the server, or require data coming from the server to function.
*/

import java.util.*;
import java.io.*;
import java.net.*;
import javax.sound.sampled.*;
import java.io.IOException;
import java.io.File;

public class GameClient{
    private GameCanvas canvas;

    private int playerID;
    private Socket socket;
    private ReadFromServer rfsRunnable;
    private WriteToServer wtsRunnable;

    private BGM bgmPlayer;

    // This sets the Game Canvas made in the GameFrame to be the same canvas that the GameClient will use.
    public void setGameCanvas(GameCanvas gc){
        canvas = gc;
    }

    // Returns the playerID
    public int getPlayerID(){
        return playerID;
    }
    
    // Connects the client to the socket with the same port and IP Address
    public void connectToServer(){
        System.out.println("Client");
        try{
            Scanner scan = new Scanner(System.in);
            System.out.print("Insert IP Address: ");
            String ipAddress = scan.nextLine();
            System.out.print("Port: ");
            int portNumber = Integer.parseInt(scan.nextLine());
            // String ipAddress = "localhost";
            // int portNumber = 24396;
            socket = new Socket(ipAddress, portNumber);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            playerID = in.readInt();
            System.out.println("Connected as Player #" + playerID);
            rfsRunnable = new ReadFromServer(in);
            wtsRunnable = new WriteToServer(out);
            rfsRunnable.waitForStartMsg();
            bgmPlayer = new BGM();
        }catch (IOException ex){
            System.out.println("Server not found");
        }
    }

    /*
    An inner class that reads data sent by the server. 
    Since this class reads info from the server, it also has methods inside that use the information
    received from the server, such as collision.
    */
    private class ReadFromServer implements Runnable{

        private DataInputStream dataIn;
        
        // Instantiation of the DataInputStream instance that will be used to read data from the server.
        public ReadFromServer(DataInputStream in){
            dataIn = in;
            System.out.println("RFS Runnable created");
        }

        //Reads data from the server and handles collisions and canvas updates for movement and sfx.
        public void run(){
            try{
                while (true){
                    int serverGameState = dataIn.readInt();
                    double ex = dataIn.readDouble();
                    double ey = dataIn.readDouble();
                    double eA = dataIn.readDouble();
                    int gotPunctured = dataIn.readInt();
                    int enemyPunctured = dataIn.readInt();
                    int gotHoney = dataIn.readInt();
                    int enemyHoney = dataIn.readInt();
                    int dashTimer = dataIn.readInt();
                    int hx = dataIn.readInt();
                    int hy = dataIn.readInt();
                    

                    if (!canvas.doesEnemyExists()) continue;

                    if (canvas.getGameState() == 0 && serverGameState == 1){
                        canvas.setGameState(1);
                        if (bgmPlayer.playing()) bgmPlayer.stopBGM();
                        bgmPlayer = new BGM();
                        bgmPlayer.playBGM();
                    }
                    else if (canvas.getGameState() == 1 && serverGameState == 2){
                        canvas.setGameState(2);
                    }
                    else if (canvas.getGameState() == 2 && serverGameState == 0){
                        canvas.pressRestart();
                    }
                    canvas.setDashTimer(dashTimer);
                    canvas.getEnemy().setX(ex);
                    canvas.getEnemy().setY(ey);
                    canvas.getEnemy().setAngle(eA);
                    canvas.getHoney().setX(hx);
                    canvas.getHoney().setY(hy);
                    
                    // if GameState is 1, then game is being played.
                    if (canvas.getGameState() == 1){
                        if (gotPunctured == 1 && !canvas.getYou().isInvincible()){
                            canvas.getYou().bodyPunctured();
                            canvas.getEnemy().addScore(1);
                            canvas.getYou().addScore(-1);
                            canvas.getYou().playHitSound();
                        }
                        if (enemyPunctured == 1 && !canvas.getEnemy().isInvincible()){
                            canvas.getEnemy().bodyPunctured();
                            canvas.getYou().addScore(1);
                            canvas.getEnemy().addScore(-1);
                            canvas.getEnemy().getPointSound();
                        }
    
                        if (gotHoney == 1 && !canvas.getYou().justGotHoney()){
                            canvas.getYou().addScore(1);
                            canvas.getYou().gotHoney();
                            canvas.getYou().getPointSound();
                        }
                        if (enemyHoney == 1 && !canvas.getEnemy().justGotHoney()){
                            canvas.getEnemy().addScore(1);
                            canvas.getEnemy().gotHoney();
                        }

                        if (Math.abs(dashTimer - Constants.DASHTRIGGER) < 7){
                            canvas.getYou().toggleDash();
                            canvas.getEnemy().toggleDash();
                        }
                    }     
                }
            }catch (IOException ex){
                System.out.println("IOException from RFS run");
            }
        }
        
        // A check if there are two players already connected to the server. Client will only "launch" if there are two players connected.
        public void waitForStartMsg(){
            try{
                String startMsg = dataIn.readUTF();
                System.out.println("Message from server: " + startMsg);
                
                Thread readThread = new Thread(rfsRunnable);
                Thread writeThread = new Thread(wtsRunnable);
                readThread.start();
                writeThread.start();
                
            }catch (IOException ex){
                System.out.println("IOException from wait for start");
            }
        }
    }

    /*
    Sets up the background music of the game, this class has methods for playing and stopping the music.
    */
    public class BGM{
        public Clip clip;
        public boolean isPlaying = false;
        
        //Plays the background music from the very start.
        public void playBGM(){
            try{
                File file = new File(Constants.BGMUSIC);
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                clip = AudioSystem.getClip();
                clip.open(audioStream);
                isPlaying = true;
                clip.start();
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
            catch(Exception e){
                System.out.println("e");
            }
        }

        //Stops the background music.
        public void stopBGM(){
            clip.stop();
            isPlaying = false;
        }

        //Checks if the background music is playing.
        public boolean playing(){
            return isPlaying;
        }
    }
    
    /*
    This class writes data from the client to the server. 
    This class sends the data of the player to the server which 
    is then used by the server to send it to the other player's client.
    */ 
    private class WriteToServer implements Runnable{

        private DataOutputStream dataOut;

        public WriteToServer(DataOutputStream out){
            dataOut = out;
            System.out.println("WTS Runnable created");
        }

        public void run(){
            try{
                while (true){

                    if (canvas.doesEnemyExists()){
                        dataOut.writeInt(canvas.getCanvasState());
                        dataOut.writeDouble(canvas.getYou().getX());
                        dataOut.writeDouble(canvas.getYou().getY());
                        dataOut.writeDouble(canvas.getYou().getAngle());
                        dataOut.writeInt(canvas.getYou().getScore());
                        dataOut.flush();
                    }
                    try{
                        Thread.sleep(10);
                    }catch (InterruptedException ex){
                        System.out.println("InterruptedException from wts run");
                    }
                }
            } catch (IOException ex){
                System.out.println("IOException from wts run");
            }
        }
    }

}