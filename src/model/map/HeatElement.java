package model.map;

import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;

import java.awt.*;
import java.io.Serializable;

/**
 * Created by diogo on 06/12/2016.
 */
public class HeatElement implements Drawable, Serializable {
    public int heat; // num of time passed
    public int x, y;

    public HeatElement(int heat, int xpos, int ypos) {
        this.heat = heat;
        x = xpos;
        y = ypos;
    }

    public HeatElement(int xpos, int ypos) {
        this.heat = -1;
        x = xpos;
        y = ypos;
    }

    public void addMyHeat() {
        if (heat < 0) {
            this.heat = 1;
        } else {
            this.heat++;
        }
    }

    public void addOtherHeat() {
        if (heat < 0 || heat != -2) {
            heat = 0;
        }
    }
    public void addVisionHeat() {
        if (heat == -1) {
            heat = 1;
        }
    }
    public void addWallHeat() {
            heat = -2;
    }



    @Override
    public void draw(SimGraphics g) {
        if (heat == 1)    //Vision
            g.drawFastRect(Color.yellow);
        else if (heat > 1)    //Numero de vezes que passou
            g.drawFastRect(new Color((heat > 9) ? 255 : heat * 25+25, 0, 0));
        else if (heat == 0)  //passado pelos outros
            g.drawFastRect(new Color(10, 50, 100));
        else if (heat == -2)  //Paredes
            g.drawFastRect(Color.gray);
        else
            g.drawFastRect(Color.white); //-1
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }
}
