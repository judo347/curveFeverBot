package mk.bot;

import java.awt.image.BufferedImage;

public class GameTickPacket {

    private BufferedImage screenshot;
    private GraficalAnalyser graficalAnalyser;


    public GameTickPacket() {
        graficalAnalyser = new GraficalAnalyser();
        screenshot = graficalAnalyser.takeScreenShot();

        


    }


}
