package mk.bot;

import java.awt.*;

public class GameInfo {



    public enum GameState {
        COUNTING, INGAME, SCOREBOARD, NOT_KNOWN;
    }

    public enum PlayerColor {

            /* COLOR NOTES:
        R,G,B   INGAME      SCOREBOARD  START
        ORANGE: 213,118,37  251,140,44  206,95,13
        PURPLE: 158,94,140  142,92,127  117,54,97
        GREEN:  63,176,96   53,149,81   23,130,44
        BLUE:   14,177,190  14,151,162  //TODO
        RED:    225,53,61   193,52,58   191,23,36
        LIME:   195,207,36  179,188,35  165,173,2
         */


        ORANGE(new RGB(206,95,13), new RGB(213,118,37), new RGB(251,140,44)),
        PURPLE(new RGB(117,54,97), new RGB(158,94,140), new RGB(142,92,127)),
        GREEN(new RGB(23,130,44), new RGB(63,176,96), new RGB(53,149,81)),
        BLUE(new RGB(0,0,0), new RGB(14,177,190), new RGB(14,151,162)), //TODO START VALUES
        RED(new RGB(191,23,36), new RGB(225,53,61), new RGB(193,52,58)),
        LIME(new RGB(165,173,2), new RGB(195,207,36), new RGB(179,188,35));

        RGB colorMinValues;
        RGB colorMaxValues;
        RGB countingColor;
        RGB ingameColor;
        RGB scoreboardColor;

        private final static int ACD = 5; //ALLOWED_COLOR_DEVIATION

        PlayerColor(RGB countingColor, RGB ingameColor, RGB scoreboardColor) {
            this.countingColor = countingColor;
            this.ingameColor = ingameColor;
            this.scoreboardColor = scoreboardColor;

            colorMinValues = setMaxOrMinValues(scoreboardColor, ingameColor, scoreboardColor, true);
            colorMaxValues = setMaxOrMinValues(scoreboardColor, ingameColor, scoreboardColor, false);
        }

        /** Take a color and returns the corresponding enum. */
        public static PlayerColor getPlayerColorFromColor(Color color){

            for(PlayerColor playerColor : PlayerColor.values()){

                int minAllowedRedValue = playerColor.getColorMinValues().red - ACD;
                int minAllowedGreenValue = playerColor.getColorMinValues().green - ACD;
                int minAllowedBlueValue = playerColor.getColorMinValues().blue - ACD;
                int maxAllowedRedValue = playerColor.getColorMaxValues().red + ACD;
                int maxAllowedGreenValue = playerColor.getColorMaxValues().green + ACD;
                int maxAllowedBlueValue = playerColor.getColorMaxValues().blue + ACD;

                boolean isRedValueOk = color.getRed() >= minAllowedRedValue && maxAllowedRedValue >= color.getRed();
                boolean isGreenValueOk = color.getGreen() >= minAllowedGreenValue && maxAllowedGreenValue >= color.getGreen();
                boolean isBlueValueOk = color.getBlue() >= minAllowedBlueValue && maxAllowedBlueValue - ACD >= color.getBlue();

                if(isRedValueOk && isGreenValueOk && isBlueValueOk)
                    return playerColor;

            }

            return null; //Color not found
        }

        public RGB getColorMinValues() {
            return colorMinValues;
        }

        public RGB getColorMaxValues() {
            return colorMaxValues;
        }

        /** @return the stageColor matching the given RGB. */
        public static GameState getStageColorFromRGB(RGB rgb){

            for(PlayerColor playerColor : PlayerColor.values()){
                if(rgb.getRed() == playerColor.countingColor.getRed() &&
                        rgb.getGreen() == playerColor.countingColor.getGreen() &&
                        rgb.getBlue() == playerColor.countingColor.getBlue())
                    return GameState.COUNTING;
                if(rgb.getRed() == playerColor.ingameColor.getRed() &&
                        rgb.getGreen() == playerColor.ingameColor.getGreen() &&
                        rgb.getBlue() == playerColor.ingameColor.getBlue())
                    return GameState.INGAME;
                if(rgb.getRed() == playerColor.scoreboardColor.getRed() &&
                        rgb.getGreen() == playerColor.scoreboardColor.getGreen() &&
                        rgb.getBlue() == playerColor.scoreboardColor.getBlue())
                    return GameState.SCOREBOARD;
            }

            return null;
        }

        /** Returns an RGB with either max or min values of the given RGB elements.
         * @param element1, element2, element3: the three elements to be checked.
         * @param min true if you want min values, false if you want max values.
         * @return an RGB with either the min or max values of the given elements, depending on the parameter min. */
        private RGB setMaxOrMinValues(RGB element1, RGB element2, RGB element3, boolean min){

            RGB holder = new RGB(element1.getRed(), element1.getGreen(), element1.getBlue());

            RGB[] elements = {element1, element2, element3};

            for(int i = 0; i < 2; i++){

                if(min){
                    if(elements[i].red < holder.red)
                        holder.red = elements[i].red;
                    if(elements[i].green < holder.green)
                        holder.green = elements[i].green;
                    if(elements[i].blue < holder.blue)
                        holder.blue = elements[i].blue;
                }else{
                    if(elements[i].red > holder.red)
                        holder.red = elements[i].red;
                    if(elements[i].green > holder.green)
                        holder.green = elements[i].green;
                    if(elements[i].blue > holder.blue)
                        holder.blue = elements[i].blue;
                }
            }

            return holder;
        }

        /** Used to the determine: is the given color a player color? */
        public boolean isColorAPlayerColor(Color color){
            return getPlayerColorFromColor(color) != null;
        }
        /** A class to represent RGB*/
        public static class RGB{



            int red;
            int green;
            int blue;

            public RGB(Color color){
                new RGB(color.getRed(), color.getGreen(), color.getBlue());
            }

            public RGB(int red, int green, int blue) {
                this.red = red;
                this.green = green;
                this.blue = blue;
            }

            public int getRed() {
                return red;
            }

            public int getGreen() {
                return green;
            }

            public int getBlue() {
                return blue;
            }
        }
    }
}
