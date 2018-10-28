package mk.bot;

import mk.bot.GameInfo.*;
import mk.bot.GameInfo.PlayerColor.*;
import mk.bot.GameTickPacket.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

//http://www.javatechblog.com/java/how-to-take-screenshot-programmatically-in-java/

//TODO: Maybe some sort of way to make the screenshot have fewer color for more reliable results/reads?
//TODO modify functions to only take the portion of the screen that is the game.
//TODO: CREATE A GAME TICK PACKET. This will make alot update at the same time, and will make everything a lot faster!

public class GraficalAnalyser extends JFrame{ //TODO does it need to extend JFrame?

    private static final long serialVersionUID = 1L; //TODO is this really needed?

    private static ArrayList<Integer> countingDownColorHistory = new ArrayList<>();

    /** @return a screenshot. */
    public BufferedImage takeScreenShot(){

        try {
            Robot robot = new Robot();
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage screenshot = robot.createScreenCapture(screenRect);

            return screenshot;

        } catch (AWTException e) {
            e.printStackTrace();
        }

        return null;
    }

    /** @return true if the game is counting down for a game. */
    public boolean isGameCountingDown(BufferedImage screenshot){

        //is counting down colors present?
        //TODO. This could also be done with the getMiddleNumber function. Might be a lot better!

        //Get all colors from screenshot matching the player colors.
        ArrayList<RGB> allPlayerColorPresented = getAllPlayerColorsFromImage(screenshot);

        //Remove all the colors not matching CountingDown colors
        ArrayList<RGB> allPlayerColorsCountingDown = getAllRGBMatchingGameState(allPlayerColorPresented, GameState.COUNTING);

        System.out.println("Counting down colors: " + allPlayerColorsCountingDown.size()); //TODO TEMP
        //Check if there is only countingDown colors left in array
        return allPlayerColorsCountingDown.size() != 0;
    }

    /***/
    public PlayerColor findPlayerColor(BufferedImage screenshot){

        //Create hashMap to be used as a counter.
        HashMap<PlayerColor, Integer> playerColorCounter = new HashMap<>();
        for(PlayerColor playerColor : PlayerColor.values())
            playerColorCounter.put(playerColor, 0);


        //Count player colors present in area. Get the color matching the most present color found.
        for(int i = 0; i < screenshot.getWidth(); i++) {
            for (int j = 0; j < screenshot.getHeight(); j++) {

                //Is the pixel matching a player color?
                PlayerColor currentPixel = PlayerColor.getPlayerColorFromColor(new Color(screenshot.getRGB(i,j)));

                if(currentPixel != null)
                    playerColorCounter.put(currentPixel, playerColorCounter.get(currentPixel) + 1);
            }
        }

        //Find the player
        PlayerColor mostPointsPlayer = null;
        int mostPlayerPoints = -1;

        for(PlayerColor playerColor : PlayerColor.values()){

            int currentPlayerPoints = playerColorCounter.get(playerColor);

            if(mostPlayerPoints < currentPlayerPoints){
                mostPlayerPoints = currentPlayerPoints;
                mostPointsPlayer = playerColor;
            }
        }

        return mostPointsPlayer;
    }



    /** @return if the count of white pixels in the middle area of the screen is over 1000. */
    public boolean isGameCountingDown2(BufferedImage screenshot){

        //Get center screen, for number
        BufferedImage middleNumber = getMiddleNumber(screenshot);

        //Count white pixels
        int whitePixels = countWhitePixels(middleNumber);
        // Count doing countdown is 1333, 1435 or 1459. Player in area = 2,16,28

        //return based on the count.
        return whitePixels > 1000;
    }

    private ArrayList<RGB> getAllRGBMatchingGameState(ArrayList<RGB> array, GameState gameState){

        ArrayList<RGB> rgbMatchingGameState = new ArrayList<>();

        for(RGB rgb : array)
            if(GameInfo.PlayerColor.getStageColorFromRGB(rgb) == gameState)
                rgbMatchingGameState.add(rgb);

        return rgbMatchingGameState;
    }

    /** @return arrayList of RGBs that all fit the colors of players. */
    private ArrayList<RGB> getAllPlayerColorsFromImage(BufferedImage image){

        ArrayList<RGB> foundPlayerColors = new ArrayList<>();

        for(int i = 0; i < image.getWidth(); i++)
            for(int j = 0; j < image.getHeight(); j++){

                Color currentColor = new Color(image.getRGB(i,j));
                PlayerColor currentPlayerColor = PlayerColor.getPlayerColorFromColor(currentColor);

                if(currentPlayerColor != null)
                    foundPlayerColors.add(new RGB(currentColor));
            }

            return foundPlayerColors;
    }

    /** @return the gameSquare. Null if none were found. */
    public OwnShape getGameSquare(BufferedImage screenshot){

        //Find all white shapes
        ArrayList<boolean[][]> whiteShapes = getAllWhiteShapes(screenshot);

        //Find playing field square
        OwnShape gameSquare = getGameSquare(whiteShapes); //TODO Should use this as the playing field for the rest of the game IMPORTANT!!

        //drawTransRectangle(gameSquare); //TODO Temp

        return gameSquare;
    }

    /** @return true if start is counting down or game has started. */
    public boolean hasGameStarted(BufferedImage screenshot){

        //Find all white shapes
        ArrayList<boolean[][]> whiteShapes = getAllWhiteShapes(screenshot);

        //Find playing field square
        OwnShape gameSquare = getGameSquare(whiteShapes); //TODO Should use this as the playing field for the rest of the game IMPORTANT!!

        //drawTransRectangle(gameSquare); //TODO Temp

        return gameSquare != null;
    }

    /** @return the shape from whiteShapes that matches a game field. (if any is found.) */
    private OwnShape getGameSquare(ArrayList<boolean[][]> whiteShapes){

        //Convert to OwnShape object
        ArrayList<OwnShape> ownWhiteShapes = new ArrayList<>();
        for(boolean[][] shape : whiteShapes)
            ownWhiteShapes.add(new OwnShape(shape));

        //Filter: size
        ArrayList<OwnShape> sizeFilteredShapes = new ArrayList<>();
        for(OwnShape shape : ownWhiteShapes)
            if(shape.isShapeRightSize())
                sizeFilteredShapes.add(shape);

        //Find and return the biggest of the shapes //TODO this should properly be done in another way.
        if(sizeFilteredShapes.size() == 0)
            return null;
        else if(sizeFilteredShapes.size() == 1)
            return sizeFilteredShapes.get(0);
        else {
            OwnShape biggestShape = sizeFilteredShapes.get(0);

            for(int i = 1; i < sizeFilteredShapes.size(); i++){

                OwnShape currentShape = sizeFilteredShapes.get(i);

                if(biggestShape.xSize * biggestShape.ySize < currentShape.xSize * currentShape.ySize)
                    biggestShape = currentShape;
            }

            return biggestShape;
        }
    }

    /** @return tries to find player in picture and returns a new player. */
    public Player getPlayer(BufferedImage image){

        //PlayerColor playerColor = getPlayerColor();


        //Player player = new Player(); //TODO

        return null; //TODO
    }

    public Coord getPlayerLocation(BufferedImage image, PlayerColor playerColor){

        //TODO

        return null; //TODO
    }

    /** Done by detecting white in the top right corner of the gameSquare. */
    public boolean isRoundActive(BufferedImage image){

        //Get top right corner of image
        BufferedImage topRightImage = getTopRightCorner(image);

        //Count white pixel
        int whitePixelCount = countWhitePixels(topRightImage);

        //Return true if number of white pixels is greater than x
        return whitePixelCount > 400;
    }

    /** @return subImage from the given image only containing the number in the middle of the screen. */
    private BufferedImage getMiddleNumber(BufferedImage image){

        final int roundNumberSizeWidth = 44;
        final int roundNumberSizeHeight = 68;

        return image.getSubimage(813, 441, roundNumberSizeWidth, roundNumberSizeHeight);
    }

    /**
     * @param image an image of the gameSquare
     * @return top right corner of the given image. */
    private BufferedImage getTopRightCorner(BufferedImage image){

        final int goalTextSizeWidth = 100;
        final int goalTextSizeHeight = 50;

        // 1666 total width, 937 total height.
        // wanted area 100 x 50

        return image.getSubimage(image.getWidth() - goalTextSizeWidth, 0, goalTextSizeWidth, goalTextSizeHeight);
    }

    /** @return number of white pixels in the given image. */
    private int countWhitePixels(BufferedImage image) {

        int counter = 0;

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {

                Color currentPixel = new Color(image.getRGB(i,j));

                //Is the color white?
                if(currentPixel.getRed() > 180 && currentPixel.getGreen() > 180 && currentPixel.getBlue() > 180)
                    counter++;
            }
        }

        return counter;
    }

    /** Used for the white shapes. Contains additional useful information. */
    class OwnShape{

        public int xMax = -1;
        public int xMin = Integer.MAX_VALUE;
        public int yMax = -1;
        public int yMin = Integer.MAX_VALUE;

        public int xSize;
        public int ySize;

        public boolean[][] shape;

        public OwnShape(boolean[][] shape){
            this.shape = shape;
            setFields();
        }

        /** Sets fields values. */
        private void setFields(){
            for(int i = 0; i < shape.length; i++){
                for(int j = 0; j < shape[0].length; j++){

                    if(shape[i][j]){
                        if(i > xMax)
                            xMax = i;
                        if(i < xMin)
                            xMin = i;
                        if(j > yMax)
                            yMax = j;
                        if(j < yMin)
                            yMin = j;
                    }
                }
            }

            //Is all coords set?
            if(xMax == -1 || yMax == -1 || xMin == Integer.MAX_VALUE || yMin == Integer.MAX_VALUE)
                throw new IllegalArgumentException(); //Does this happen?

            //Is it the right size?
            xSize = xMax - xMin;
            ySize = yMax - yMin;
        }

        //TODO should maybe take a parameter: check size
        /** Used to determine if the size of the shape is larger that a portion of the screen size. */
        public boolean isShapeRightSize(){

            //TODO TEST 1/4 of screen size ok?
            int xMinSize = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 4;
            int yMinSize = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 4;

            if(xSize < xMinSize || ySize < yMinSize)
                return false;

            return true;
        }
    }

    /** Draws a button with the size and placement of the given shape. */
    private void drawTransRectangle(OwnShape shape){

        //https://stackoverflow.com/questions/1768062/how-to-create-an-overlay-window-in-java

        JFrame frame = new JFrame("Transparent window");
        frame.setUndecorated(true);
        frame.setBackground(new Color(0,0,0));
        frame.setAlwaysOnTop(true);
        // Without this, the window is draggable from any non transparent
        // point, including points  inside textboxes.
        frame.getRootPane().putClientProperty("apple.awt.draggableWindowBackground", false);
        frame.getContentPane().setLayout(new BorderLayout());

        //Create the button representing the shape
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(shape.xSize, shape.ySize));
        button.setText("hello");
        frame.add(button);

        frame.setLocation(shape.xMin, shape.yMin);
        frame.setVisible(true);
        frame.pack();
    }

    /** @return arrayList of all whiteShapes. */
    private ArrayList<boolean[][]> getAllWhiteShapes(BufferedImage screenshot){

        //Convert screenshot into 2d-array of colors.
        Color[][] screenshotPixels = new Color[screenshot.getWidth()][screenshot.getHeight()];
        for(int i = 0; i < screenshot.getWidth(); i++)
            for(int j = 0; j < screenshot.getHeight(); j++)
                screenshotPixels[i][j] = new Color(screenshot.getRGB(i,j));

        //Search array for white
        //if found search for shape and add to list //set found to null
        //else set pixel to null
        ArrayList<boolean[][]> whiteShapes = new ArrayList<>();
        for(int i = 0; i < screenshotPixels.length; i++){
            for(int j = 0; j < screenshotPixels[0].length; j++){

                if(screenshotPixels[i][j] == null)
                    continue;
                else if(isColorWhite(screenshotPixels[i][j].getRGB())){ //White found! if found search for scape and add to list //set found to null
                    whiteShapes.add(findCoherentWhitePixels(screenshotPixels, i, j)); //TODO
                }else //Not white! Set pixel to null
                    screenshotPixels[i][j] = null;
            }
        }

        return whiteShapes;
    }

    /** Returns a sections of the given image from the x,y (top-left corner)
     * and with the size of the given width and height.
     * @return a section of the given image.*/
    public BufferedImage getSubImage(BufferedImage image, int x, int y, int width, int height){
        return image.getSubimage(x, y, width, height);
    }

    /** Checks if the given color is white. */
    private boolean isColorWhite(int rgb){
        return isColorWhite(new Color(rgb));
    }

    /** Checks if the given color is white. */
    private boolean isColorWhite(Color color){

        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();

        return (red == 255 && green == 255 && blue == 255);
    }

    /** TODO CLEANUP*/
    private boolean[][] findCoherentWhitePixels(Color[][] screenshotPixels, int startX, int startY){

        boolean[][] shape = new boolean[screenshotPixels.length][screenshotPixels[0].length];

        findCoherentWhitePixels(screenshotPixels, shape, startX, startY);

        return shape;
    }

    /** TODO CLEANUP*/
    private void findCoherentWhitePixels(Color[][] screenshotPixels, boolean[][] shape, int x, int y) {

        try{
            //OutOfBoundsCheck
            if(x < 0 || x > screenshotPixels.length-1)
                return;
            else if(y < 0 || y > screenshotPixels[0].length-1)
                return;
            if (screenshotPixels[x][y] == null) {
                return;
            }else if (!isColorWhite(screenshotPixels[x][y])) { //location is not white
                screenshotPixels[x][y] = null;
                return;
            } else { //Pixel is white

                screenshotPixels[x][y] = null; //Set current pixel to null
                shape[x][y] = true; //Set current pixel in shape to true

                //Call this method on all surrounding pixels
                for(int i = -1; i < 2; i++){
                    for(int j = -1; j < 2; j++){
                        findCoherentWhitePixels(screenshotPixels, shape, x+i, y+j);
                    }
                }
            }
        }catch (StackOverflowError e){
            return; //TODO should be fized
        }
    }

    /** Saves the given screenshot. */
    public void saveScreenshot(BufferedImage screenshot){

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd hh mm ss a");
        Calendar now = Calendar.getInstance();
        try {
            ImageIO.write(screenshot, "PNG", new File("d:\\"+formatter.format(now.getTime())+".jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public BufferedImage setAlphaForAllPixels(BufferedImage image, int alpha){
        //TODO

        for(int i = 0; i < image.getWidth(); i++){
            for(int j = 0; j < image.getHeight(); j++){

                Color currentPixel = new Color(image.getRGB(i,j));
                Color newPixel = new Color(currentPixel.getRed(), currentPixel.getGreen(), currentPixel.getBlue(), alpha);
                image.setRGB(i,j,newPixel.getRGB());
            }
        }

        return image;
    }

    //TODO NOT SURE IF I SHOULD KEEP?

    /** used for coordinates*/
    public class Coord{

        public int x = -1;
        public int y = -1;
        public Coord(){}

        public Coord(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    /** @return the color the given screenshot at the given coords. */
    public Color getColorFromCoords(int x, int y, BufferedImage screenshot){

        return new Color(screenshot.getRGB(x, y));
    }
}
