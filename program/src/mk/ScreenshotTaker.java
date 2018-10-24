package mk;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

//http://www.javatechblog.com/java/how-to-take-screenshot-programmatically-in-java/

public class ScreenshotTaker extends JFrame{

    private static final long serialVersionUID = 1L; //TODO is this really needed?

    public BufferedImage screenshot = null;

    public void takeScreenShot(){

        try {
            Robot robot = new Robot();
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage sceenFullImage = robot.createScreenCapture(screenRect);
            //ImageIO.write(sceenFullImage, "jpg", new File(fileName));

            screenshot = sceenFullImage;

        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public Color getColorFromCoords(int x, int y){

        return new Color(screenshot.getRGB(x, y));
    }
}
