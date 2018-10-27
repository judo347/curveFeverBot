package mk.bot;

import mk.bot.GameInfo.*;

import java.awt.image.BufferedImage;

public class Bot {

    public void startBot(){

        //this.graficalAnalyser = new GraficalAnalyser();

        //TODO TEMP
        while (true){
            updateGameState();
            printOutTheThreeBooleans();
        }

        /*
        //Wait until game has started
        while (!isGameRunning){
            updateGameState();
            //TODO maybe add delay?
        }*/
    }

    /** Used for testing. Prints out the three booleans. */
    private void printOutTheThreeBooleans(){
        //System.out.println("GameBorder: " + isGameRunning + " GameCounting: " + isGameCountingDown + " RoundActive: " + isRoundActive);
    }

    /** Used to update the three game state booleans. */
    private void updateGameState(){

        //BufferedImage screenshot = graficalAnalyser.takeScreenShot();
        //BufferedImage screenshot2 = graficalAnalyser.takeScreenShot();



        //checkIsGameCountingDown(screenshot); //TODO DOES NOT WORK // Should propperly be reworked to use image recognition / pattern recognition
        //checkIsGameRunning(screenshot); //TODO SEEMS TO WORK
        //checkIsRoundActive(screenshot); //TODO NOT IMPLEMENTED
    }



}
