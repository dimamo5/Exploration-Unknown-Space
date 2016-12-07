package agent;

import javafx.util.Pair;
import model.map.AgentModel;
import model.map.ViewMap;
import sajas.core.Agent;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by sergi on 16/10/2016.
 */
public class ExplorerAgent extends Agent {

    /*
    * Dir:
    * North- 0
    * West- 1
    * South- 2
    * East- 3
   * */

    private static final int DEFAULT_VISION_RANGE = 5;

    private int vision_range = DEFAULT_VISION_RANGE,    //cells count
            current_dir = -1, previous_dir = -1;            //no current/previous direction at the begining

    // TODO: 16/10/2016 add agent's own globalMap

    private boolean at_map_exit = false, found_map_exit = false;
    private AgentModel model_link;
    private ViewMap myViewMap;

    public ViewMap getMyViewMap() {
        return myViewMap;
    }

    public void setMyViewMap(ViewMap myViewMap) {
        this.myViewMap = myViewMap;
    }

    public AgentModel getModel_link() {
        return model_link;
    }

    public void setModel_link(AgentModel model_link) {
        this.model_link = model_link;
    }


    //==================================== METHODS ====================================================//

    public ExplorerAgent(int vision_range) {
        this.vision_range = vision_range;
    }


    protected Pair<Integer, Integer> move(ViewMap.DIR dirToMove) {

        ArrayList<ViewMap.DIR> dirs = new ArrayList<ViewMap.DIR>(Arrays.asList(ViewMap.DIR.N, ViewMap.DIR.S, ViewMap
                .DIR.E, ViewMap.DIR.W));

        int index = dirs.indexOf(dirToMove);
        Pair<Integer, Integer> coos = new Pair<>(getModel_link().getX(), getModel_link().getY());

        switch (index) {
            case 0: //N
                return new Pair<>(coos.getKey(), coos.getValue() - 1);

            case 1: //S
                return new Pair<>(coos.getKey(), coos.getValue() + 1);

            case 2: //E
                return new Pair<>(coos.getKey() + 1, coos.getValue());

            case 3: //W
                return new Pair<>(coos.getKey() - 1, coos.getValue());

            default:
                System.out.println("Unexpected direction");
                return null;
        }
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


    public int getVision_range() {
        return vision_range;
    }

    public void setVision_range(int vision_range) {
        this.vision_range = vision_range;
    }

    public boolean is_At_map_exit() {
        return at_map_exit;
    }

    public void setAt_map_exit(boolean at_map_exit) {
        this.at_map_exit = at_map_exit;
    }

    public boolean isAt_map_exit() {
        return at_map_exit;
    }

    public boolean isFound_map_exit() {
        return found_map_exit;
    }

    public void setFound_map_exit(boolean found_map_exit) {
        this.found_map_exit = found_map_exit;
    }


}
