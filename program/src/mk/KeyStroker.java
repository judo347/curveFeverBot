package mk;

//https://stackoverflow.com/questions/32641269/how-to-programmatically-send-a-key-event-to-any-window-process-in-a-java-app

import java.awt.*;

public class KeyStroker {

    public void sendKeyStroke(){

        try {
            Robot robot = new Robot();
            int i = 1;
            while(i < 100){
                robot.mouseMove(10,50);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                robot.mouseMove(100,500);
                i++;
            }



        } catch (AWTException e) {
            e.printStackTrace();
        }
    }
}
