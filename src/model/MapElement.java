package model;

import uchicago.src.sim.engine.Stepable;
import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;
import uchicago.src.sim.space.Object2DGrid;

import java.awt.image.BufferedImage;


/**
 * Created by sergi on 16/10/2016.
 */

/**
 * Represents a map's element (human/robot/obstacle/exit)
 */

public class MapElement implements Drawable,Stepable {

    private int pos_x, pos_y;
    protected static Object2DGrid map;
    private static BufferedImage image;

    public static Object2DGrid getMap() {
        return map;
    }

    public static void setMap(Object2DGrid map) {
        MapElement.map = map;
    }

    public static BufferedImage getImage() {
        return image;
    }

    public static void setImage(BufferedImage image) {
        MapElement.image = image;
    }

    public MapElement(int pos_x, int pos_y, Object2DGrid c, BufferedImage icon) {
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        map = c;
        image = icon;
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

    public void setPos_x(int pos_x) {
        this.pos_x = pos_x;
    }

    public void setPos_y(int pos_y) {
        this.pos_y = pos_y;
    }
}
