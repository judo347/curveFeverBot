package mk.bot;

import java.awt.image.BufferedImage;

import mk.bot.GameInfo.PlayerColor;
import mk.bot.GraficalAnalyser.*;

public class GameTickPacket {

    private BufferedImage screenshot;
    private GraficalAnalyser graficalAnalyser;
    private OwnShape gameShape;

    private boolean isGameRunning; //is there a game border?
    private boolean isGameCountingDown; //is the game counting down to the start?
    private boolean isRoundActive; //is the round active / can we stear?

    private PlayerColor myPlayerColor;

    public GameTickPacket() {
        isGameRunning = false;
        isGameCountingDown = false;
        isRoundActive = false;

        graficalAnalyser = new GraficalAnalyser();

        update();
    }

    public void update(){

        screenshot = graficalAnalyser.takeScreenShot();
        gameShape = graficalAnalyser.getGameSquare(screenshot); //Try to find game screen.
        if(gameShape != null){ //Is there a gameborder?
            isGameRunning = true;
            screenshot = graficalAnalyser.getSubImage(screenshot, gameShape.xMin, gameShape.yMin, gameShape.xSize, gameShape.ySize); //Limit the screenshot to the gamescreen.

            graficalAnalyser.saveScreenshot(screenshot); //TODO TEMP
            //If game border is found. Do further information gathering.


        }
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
