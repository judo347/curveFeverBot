package mk.bot;

import java.awt.image.BufferedImage;

import mk.bot.GameInfo.PlayerColor;
import mk.bot.GraficalAnalyser.*;
import mk.bot.GameInfo.GameState;

import static mk.bot.GameInfo.GameState.*;

public class GameTickPacket {

    private BufferedImage screenshot;
    private GraficalAnalyser graficalAnalyser;
    private OwnShape gameShape;

    private boolean isGameRunning; //is there a game border?
    private boolean isGameCountingDown; //is the game counting down to the start?
    private boolean isRoundActive; //is the round active / can we stear?

    private boolean isGameDetected;
    private GameState currentGameState;

    private Player player = null;

    class Player{

        PlayerColor playerColor;
        int x, y;
        boolean isAlive;

        public Player(PlayerColor playerColor, Coord pos, boolean isAlive) {
            this.playerColor = playerColor;
            this.x = pos.x;
            this.y = pos.y;
            this.isAlive = isAlive;
        }

        public void updatePosition(Coord pos){
            this.x = pos.x;
            this.y = pos.y;
        }

        public void setAlive(boolean alive) {
            isAlive = alive;
        }
    }

    public GameTickPacket() {
        isGameDetected = false;
        currentGameState = NOT_KNOWN;
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

            //Limit the screenshot to the gamescreen.
            screenshot = graficalAnalyser.getSubImage(screenshot, gameShape.xMin, gameShape.yMin, gameShape.xSize, gameShape.ySize);

            //Is game counting down?
            isGameCountingDown = isGameCountingDown(screenshot);
            if(isGameCountingDown){
                //TODO Find player color.
                PlayerColor playerColor = graficalAnalyser.findPlayerColor(screenshot); //TODO TEST + FINISH
                System.out.println("Game is counting down");
                System.out.println(playerColor);

            }


            checkIsRoundActive(screenshot);
            //Game border is found. Do further information gathering.

            //Update/get player data.
            if(player == null)
                player = graficalAnalyser.getPlayer(screenshot);
            else //TODO How to find player: is counting? We are the one where most pixels.
                player.updatePosition(graficalAnalyser.getPlayerLocation(screenshot, player.playerColor));

            //TODO

            //is GameCountingDown or is RoundActive?

        }
    }

    /** Called when you want to check if the game is running. Refreshed the field boolean isGameRunning. */
    private void checkIsGameRunning(BufferedImage screenshot){
        isGameRunning = graficalAnalyser.hasGameStarted(screenshot);
    }

    /** Called when you want to check if the game is counting down. Refreshed the field boolean isGameCountingDown. */
    private boolean isGameCountingDown(BufferedImage screenshot){
        return graficalAnalyser.isGameCountingDown2(screenshot);
    }

    /** Called when you want to check if the game is round active. Refreshed the field boolean isRoundActive.
     * It is done by finding top right corner, and checking if there is white in that area. */
    public void checkIsRoundActive(BufferedImage screenshot){
        graficalAnalyser.isRoundActive(screenshot);
    }


}
