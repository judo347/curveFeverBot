package mk.bot;

public class Main {

    public static void main(String[] args) {

        thisOne();

        //Bot bot = new Bot();

        //bot.startBot();

        /*
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
        }*/
    }

    private static void thisOne(){

        GameTickPacket gtp = new GameTickPacket();

        while (true)
            gtp.update();

        //GraficalAnalyser graficalAnalyser = new GraficalAnalyser();

        //graficalAnalyser.isRoundActive(graficalAnalyser.getGameSquare(graficalAnalyser.takeScreenShot()));

    }
}
