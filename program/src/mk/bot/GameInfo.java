package mk.bot;

import java.awt.*;
import java.util.ArrayList;

public class GameInfo {



    public enum PlayerColors{

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
        RGB startColor;
        RGB ingameColor;
        RGB scoreboardColor;

        PlayerColors(RGB startColor, RGB ingameColor, RGB scoreboardColor) {
            this.startColor = startColor;
            this.ingameColor = ingameColor;
            this.scoreboardColor = scoreboardColor;

            colorMinValues = setMaxOrMinValues(scoreboardColor, ingameColor, scoreboardColor, true);
            colorMaxValues = setMaxOrMinValues(scoreboardColor, ingameColor, scoreboardColor, false);
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

        /** Take a color and returns the corresponding enum. */
        public PlayerColors getPlayerColorFromColor(Color color){

            if(color.getRed() <= 206 && 251 <= color.getRed() &&
                    color.getGreen() <= 95 && 140 <= color.getGreen() &&
                    color.getBlue() <= 13 && 44 <= color.getBlue())
                return ORANGE;

            if(color.getRed() <= 117 && 158 <= color.getRed() &&
                    color.getGreen() <= 54 && 94 <= color.getGreen() &&
                    color.getBlue() <= 97 && 140 <= color.getBlue())
                return PURPLE;

            if(color.getRed() <= 23 && 63 <= color.getRed() &&
                    color.getGreen() <= 130 && 176 <= color.getGreen() &&
                    color.getBlue() <= 44 && 96 <= color.getBlue())
                return GREEN;

            //TODO Missing values for COLOR AT START
            if(color.getRed() <= 14 && 14 <= color.getRed() &&
                    color.getGreen() <= 151 && 177 <= color.getGreen() &&
                    color.getBlue() <= 162 && 190 <= color.getBlue())
                return BLUE;

            if(color.getRed() <= 191 && 225 <= color.getRed() &&
                    color.getGreen() <= 23 && 53 <= color.getGreen() &&
                    color.getBlue() <= 36 && 61 <= color.getBlue())
                return RED;

            if(color.getRed() <= 165 && 195 <= color.getRed() &&
                    color.getGreen() <= 173 && 207 <= color.getGreen() &&
                    color.getBlue() <= 2 && 36 <= color.getBlue())
                return LIME;

            return null; //Color not found
        }

        /** Used to the determine: is the given color a player color? */
        public boolean isColorAPlayerColor(Color color){
            return getPlayerColorFromColor(color) != null;
        }

        /** A class to represent RGB*/
        static class RGB{

            int red;
            int green;
            int blue;

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
