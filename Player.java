import java.awt.*;

public class Player{
    private int xPos, yPos;
    private int scale;
    
    // private int power;

    // private double angle;

    // temporary values
    private int hSpeed = 10;
    private int vSpeed = 10;

    public Player(int initX, int initY){
        xPos = initX; yPos = initY;
        scale = 25;
        // angle = 120;
    }
    public void draw(Graphics g){
        g.fillOval(xPos, yPos, scale, scale);
    }

    public int getX(){return xPos;}
    public int getY(){return yPos;}
    public int getWidth(){return scale;}
    public int getHeight(){return scale;}
    // public double getAngle(){return angle;}

    public void adjustX(){xPos += hSpeed;}
    public void adjustY(){yPos += vSpeed;}

    // public void adjustAngle(double delta){angle += delta;}

    public void invertXDirection(){hSpeed *= -1;}
    public void invertYDirection(){vSpeed *= -1;}
}