package agent;

import uchicago.src.sim.space.Object2DGrid;

import java.awt.image.BufferedImage;

/**
 * Created by sergi on 16/10/2016.
 */
public class Human extends ExplorerAgent {

    private static final int DEFAULT_RADIO_RANGE = 10;

    private int radio_range = DEFAULT_RADIO_RANGE;

    public Human(int id, int vision_range, int radio_range) {
        super(id, vision_range);
        this.radio_range = radio_range;
    }

    public int getRadio_range() {
        return radio_range;
    }

    public void setRadio_range(int radio_range) {
        this.radio_range = radio_range;
    }

}
