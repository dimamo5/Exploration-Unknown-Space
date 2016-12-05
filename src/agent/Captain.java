package agent;

import sajas.core.behaviours.Behaviour;
import uchicago.src.sim.space.Object2DGrid;

import java.awt.image.BufferedImage;

/**
 * Created by sergi on 16/10/2016.
 */
public class Captain extends Human {

    public int cellphone_range;

    public Captain(int id, int vision_range, int radio_range, int cellphone_range) {
        super(id, vision_range, radio_range);
        this.cellphone_range = cellphone_range;
    }

    public int getCellphone_range() {
        return cellphone_range;
    }

    public void setCellphone_range(int cellphone_range) {
        this.cellphone_range = cellphone_range;
    }

    public void move(){

    }

    @Override
    protected void setup() {
        super.setup();
    }

    @Override
    protected void takeDown() {
        super.takeDown();
    }

    @Override
    public void addBehaviour(Behaviour b) {
        super.addBehaviour(b);
    }


}
