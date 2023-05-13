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
    This class contains all the constant values used across all other classes.
    These constants include file paths, properties of players, timer counters, etc.
    This class is made to reduce redundancy and easy access to values used throughout the program.
 */

public class Constants {
    public static final int FRAMEWIDTH = 960;
    public static final int FRAMEHEIGHT = 540;
    
    public static final String P1SPRITE = "assets/bee-orange.png";
    public static final String P1SPRITE2 = "assets/bee-orange-flap.png";
    public static final String P2SPRITE = "assets/bee-blue.png";
    public static final String P2SPRITE2 = "assets/bee-blue-flap.png";
    public static final String BGSPRITE = "assets/background.png";
    public static final String STARTMENUBG = "assets/startmenubg.png";
    public static final String STARTMENUWAIT = "assets/startmenuwait.png";
    public static final String TIMEHEXAGON = "assets/timeindicator.png";
    public static final String TIMEHEXAGONGLOW = "assets/timeindicator-glow.png";
    public static final String HONEY = "assets/honey.png";
    public static final String YOUWIN = "assets/youwin.png";
    public static final String YOULOSE = "assets/youlose.png";

    public static final int NORMALSPEED = 3; // 3
    public static final int MAXSPEED = 30; // 30
    public static final int SPEEDINCREMENT = 1;
    public static final double RAD90 = 1.57; // 1.570796327
    public static final double ANGLESENS = 0.25;
    public static final double NEEDLEDIST = 26;
    public static final double BODYRADIUS = 20;

    public static final int DASHLIMIT = 400;
    public static final int DASHTRIGGER = 300; 

    public static final int INVIDURATION = 80;
    public static final float INVIALPHA = 0.5f;

    public static final String FONTNAME = "Impact";
    public static final int FONTSZ = 42;

    public static final int WINSCORE = 9;
    public static final int HONEYRAD = 23;
    public static final int GOTHONEYDURATION = 80;
}
