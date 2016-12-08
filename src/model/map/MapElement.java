package model.map;

import uchicago.src.sim.engine.Stepable;
import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;

import java.awt.*;
import java.awt.image.BufferedImage;


/**
 * Created by sergi on 16/10/2016.
 */

/**
 * Represents a globalMap's element (human/robot/obstacle/exit)
 */

public class MapElement implements Drawable, Stepable {

    private int pos_x, pos_y;
    private Image image;

    public Image getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public MapElement(int pos_x, int pos_y) {
        this.pos_x = pos_x;
        this.pos_y = pos_y;
    }

    @Override
    public void step() {

    }

    @Override
    public void draw(SimGraphics simGraphics) {
        if (image != null) {
            simGraphics.drawImageToFit(getImage());
        } else {
            simGraphics.drawFastCircle(new Color(65, 88, 43));
        }
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
