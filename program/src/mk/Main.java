package mk;

public class Main {

    public static void main(String[] args) {

        TestTest testTest = new TestTest();

        ScreenshotTaker screenshotTaker = new ScreenshotTaker();

        screenshotTaker.takeScreenShot();

        System.out.println(screenshotTaker.screenshot.getRGB(0,0));
        System.out.println(screenshotTaker.getColorFromCoords(0,0));


        KeyStroker keyStroker = new KeyStroker();
        keyStroker.sendKeyStroke();
    }
}
