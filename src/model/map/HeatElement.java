package model.map;

import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;

import java.awt.*;

/**
 * Created by diogo on 06/12/2016.
 */
public class HeatElement implements Drawable {
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

    @Override
    public void draw(SimGraphics g) {
        if (heat > 0)
            g.drawFastRect(new Color((heat > 9) ? 255 : heat * 25, 0, 0));
        else if (heat == 0)
            g.drawFastRect(new Color(10, 50, 100));
        else if (heat == -2)
            g.drawFastRect(Color.gray);
        else if (heat == -3)
            g.drawFastRect(Color.YELLOW);
        else
            g.drawFastRect(Color.white);
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
