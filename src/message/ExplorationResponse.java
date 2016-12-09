package message;

import javafx.util.Pair;
import model.map.ViewMap;

/**
 * Created by sergi on 09/12/2016.
 */
public class ExplorationResponse extends InformViewMap {

    boolean foundExit = false;

    public ExplorationResponse(Pair<Integer, Integer> position, ViewMap map, boolean foundExit) {
        super(position, map);
        this.foundExit = foundExit;
    }
}
