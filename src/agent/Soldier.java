package agent;

import uchicago.src.sim.space.Object2DGrid;

/**
 * Created by sergi on 16/10/2016.
 */
public class Soldier extends Human {
    public Soldier(int pos_x, int pos_y, Object2DGrid c, int id, int vision_range, int radio_range) {
        super(pos_x, pos_y, c, id, vision_range, radio_range);
    }
}
