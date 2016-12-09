package agent;

import javafx.util.Pair;
import model.Model;
import model.map.AgentModel;
import model.map.ViewMap;
import sajas.core.Agent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static model.map.ViewMap.DIR.W;


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

    protected static final int DEFAULT_VISION_RANGE = 5;

    protected int vision_range = DEFAULT_VISION_RANGE;   //cells count
    protected long tick=0;

    public ViewMap.DIR getCurrent_dir() {
        return current_dir;
    }

    public void setCurrent_dir(ViewMap.DIR current_dir) {
        this.current_dir = current_dir;
    }

    protected ViewMap.DIR current_dir;

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
        super();
        this.vision_range = vision_range;
        System.out.println(this.getAID());
    }


    protected Pair<Integer, Integer> move(ViewMap.DIR dirToMove) {

        Pair<Integer, Integer> coos = new Pair<>(getModel_link().getX(), getModel_link().getY());

        switch (dirToMove) {
            case N: //N
                return new Pair<>(coos.getKey(), coos.getValue() - 1);

            case S: //S
                return new Pair<>(coos.getKey(), coos.getValue() + 1);

            case E: //E
                return new Pair<>(coos.getKey() + 1, coos.getValue());

            case W: //W
                return new Pair<>(coos.getKey() - 1, coos.getValue());

            default:
                System.out.println("Unexpected direction");
                return null;
        }
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

    protected void move_random() {

        Pair<Integer, Integer> oldPos = new Pair<>(getModel_link().getX(), getModel_link().getY());
        Pair<Integer, Integer> newPos;

        if (current_dir == null || !getMyViewMap().canMoveDir(current_dir, oldPos)) {
            ArrayList<ViewMap.DIR> possibleDirs = getMyViewMap().getPossibleDir(oldPos);

            Random r = new Random();
            int dir = r.nextInt(possibleDirs.size());

            //new coordinates
            current_dir = possibleDirs.get(dir);
        }

        newPos = move(current_dir);

        //move on globalMap

        AgentModel.setGlobalMap(AgentModel.getGlobalMap()); //extract these calls to 1 method

        //update pos
        updatePosition(newPos);

        //update viewmap
        getMyViewMap().addViewRange(newPos, Model.getForest(), getVision_range());
    }

    protected void updatePosition(Pair<Integer, Integer> newPos) {
        getModel_link().setPos_x(newPos.getKey());
        getModel_link().setPos_y(newPos.getValue());
    }
}
