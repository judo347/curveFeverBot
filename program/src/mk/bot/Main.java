package mk.bot;

import java.awt.image.BufferedImage;

public class Main {

    public static void main(String[] args) {

        //Bot bot = new Bot();

        //bot.startBot();


        GraficalAnalyser graficalAnalyser = new GraficalAnalyser();
        BufferedImage image = graficalAnalyser.takeScreenShot();

        try {
            graficalAnalyser.saveScreenshot(image);
            Thread.sleep(1000);
            graficalAnalyser.saveScreenshot(graficalAnalyser.setAlphaForAllPixels(image,0));
            Thread.sleep(1000);
            graficalAnalyser.saveScreenshot(graficalAnalyser.setAlphaForAllPixels(image,255));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
