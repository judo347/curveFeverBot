package mk.bot;

import mk.bot.GameInfo.*;

public class Bot {

    private boolean isGameRunning = false;
    private boolean isGameCountingDown = false;
    private boolean isRoundActive = false;

    private GraficalAnalyser graficalAnalyser;

    private PlayerColors myPlayerColor;


    public void startBot(){

        this.graficalAnalyser = new GraficalAnalyser();

        //Wait until game has started
        while (!isGameRunning){
            checkIsGameRunning();
            //TODO maybe add delay?
        }


    }




    /** Called when you want to check if the game is running. Refreshed the field boolean isGameRunning. */
    private void checkIsGameRunning(){
        isGameRunning = graficalAnalyser.hasGameStarted(graficalAnalyser.takeScreenShot());
    }

}
