package agent;

import uchicago.src.sim.space.Object2DGrid;

/**
 * Created by sergi on 16/10/2016.
 */
public class Human extends Agent {

    private int radio_range = 5;

    public Human(int pos_x, int pos_y, Object2DGrid c, int id, int vision_range, int radio_range) {
        super(pos_x, pos_y, c, id, vision_range);
        this.radio_range = radio_range;
    }

    public int getRadio_range() {
        return radio_range;
    }

    public void setRadio_range(int radio_range) {
        this.radio_range = radio_range;
    }
}
