package agent;

import uchicago.src.sim.space.Object2DGrid;

import java.awt.image.BufferedImage;

/**
 * Created by sergi on 16/10/2016.
 */
public class Captain extends Human {


    public int cellphone_range;

    public Captain(int pos_x, int pos_y, Object2DGrid c, BufferedImage icon, int id, int vision_range, int radio_range, int cellphone_range) {
        super(pos_x, pos_y, c, icon, id, vision_range, radio_range);
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
}
