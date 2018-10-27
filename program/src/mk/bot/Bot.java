package mk.bot;

import mk.bot.GameInfo.*;

import java.awt.image.BufferedImage;

public class Bot {

    private boolean isGameRunning = false; //is there a game border?
    private boolean isGameCountingDown = false; //is the game counting down to the start?
    private boolean isRoundActive = false; //is the round active / can we stear?

    private GraficalAnalyser graficalAnalyser;

    private PlayerColor myPlayerColor;

    public void startBot(){

        this.graficalAnalyser = new GraficalAnalyser();

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
        System.out.println("GameBorder: " + isGameRunning + " GameCounting: " + isGameCountingDown + " RoundActive: " + isRoundActive);
    }

    /** Used to update the three game state booleans. */
    private void updateGameState(){

        BufferedImage screenshot = graficalAnalyser.takeScreenShot();
        //BufferedImage screenshot2 = graficalAnalyser.takeScreenShot();



        checkIsGameCountingDown(screenshot); //TODO DOES NOT WORK // Should propperly be reworked to use image recognition / pattern recognition
        checkIsGameRunning(screenshot); //TODO SEEMS TO WORK
        checkIsRoundActive(screenshot); //TODO NOT IMPLEMENTED
    }

    /** Called when you want to check if the game is running. Refreshed the field boolean isGameRunning. */
    private void checkIsGameRunning(BufferedImage screenshot){
        isGameRunning = graficalAnalyser.hasGameStarted(screenshot);
    }

    /** Called when you want to check if the game is counting down. Refreshed the field boolean isGameCountingDown. */
    private void checkIsGameCountingDown(BufferedImage screenshot){
        isGameCountingDown = graficalAnalyser.isGameCountingDown(screenshot);
    }

    /** Called when you want to check if the game is round active. Refreshed the field boolean isRoundActive. */
    private void checkIsRoundActive(BufferedImage screenshot){
        //TODO //Can be done by detecting if isGameCountingDown = false && isGameRunning == true;
    }

}
