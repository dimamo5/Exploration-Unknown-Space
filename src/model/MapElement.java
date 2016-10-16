package model;

import uchicago.src.sim.engine.Stepable;
import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;
import uchicago.src.sim.space.Object2DGrid;


/**
 * Created by sergi on 16/10/2016.
 */

/**
 * Represents a map's element (human/robot/obstacle)
 */
public class MapElement implements Drawable,Stepable {

    private int pos_x, pos_y;
    private static Object2DGrid cell;

    public MapElement(int pos_x, int pos_y, Object2DGrid c) {
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        cell = c;
    }

    @Override
    public void step() {

    }

    @Override
    public void draw(SimGraphics simGraphics) {

    }

    @Override
    public int getX() {
        return pos_x;
    }

    @Override
    public int getY() {
        return pos_y;
    }
}
