package agent;

import model.MapElement;
import uchicago.src.sim.space.Object2DGrid;

/**
 * Created by sergi on 16/10/2016.
 */
public class Agent extends MapElement {

    /*
    * Dir:
    * North- 0
    * West- 1
    * South- 2
    * East- 3
   * */

    private int id, vision_range, /*cells count*/
            current_dir = -1, previous_dir = -1; //no current/previous direction at the begining

    // TODO: 16/10/2016 add agent's own map

    public Agent(int pos_x, int pos_y, Object2DGrid c, int id, int vision_range) {
        super(pos_x, pos_y, c);
        this.id = id;
        this.vision_range = vision_range;
    }

    public int getCurrent_dir() {
        return current_dir;
    }

    public void setCurrent_dir(int current_dir) {
        this.current_dir = current_dir;
    }

    public int getPrevious_dir() {
        return previous_dir;
    }

    public void setPrevious_dir(int previous_dir) {
        this.previous_dir = previous_dir;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVision_range() {
        return vision_range;
    }

    public void setVision_range(int vision_range) {
        this.vision_range = vision_range;
    }
}
