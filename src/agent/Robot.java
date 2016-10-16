package agent;

import uchicago.src.sim.space.Object2DGrid;

/**
 * Created by sergi on 16/10/2016.
 */
public class Robot extends Agent {

    private int energy = 15; //15 cells by default

    public Robot(int pos_x, int pos_y, Object2DGrid c, int id, int vision_field, int energy) {
        super(pos_x, pos_y, c, id, vision_field);
        this.energy = energy;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }
}
