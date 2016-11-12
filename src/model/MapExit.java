package model;

import uchicago.src.sim.space.Object2DGrid;

import java.awt.image.BufferedImage;

/**
 * Created by sergi on 12/11/2016.
 */
public class MapExit extends MapElement {

    public MapExit(int pos_x, int pos_y, Object2DGrid c, BufferedImage icon) {
        super(pos_x, pos_y, c, icon);
    }
}
