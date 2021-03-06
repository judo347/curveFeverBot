package mk.tryout;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

//http://www.javatechblog.com/java/how-to-take-screenshot-programmatically-in-java/

public class ScreenshotTaker extends JFrame{

    private static final long serialVersionUID = 1L; //TODO is this really needed?

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

    /** returns true if start is counting down or game has started. */
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
        frame.getContentPane().setLayout(new java.awt.BorderLayout());

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
            if (screenshotPixels[x][y] == null) {
                return;
            }
            if (!isColorWhite(screenshotPixels[x][y])) { //location is not white
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
        }catch (ArrayIndexOutOfBoundsException e){
            return;
        }catch (StackOverflowError e){
            return; //TODO should be fized
        }
    }


    //TODO NOT SURE IF I SHOULD KEEP?

    /** used for coordinates*/
    private class Coord{

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
