package message;

import javafx.util.Pair;
import model.map.ViewMap;

/**
 * Created by sergi on 10/12/2016.
 */
public class PropagateExit extends Message {
    private ViewMap vMap;

    public PropagateExit(Pair<Integer, Integer> position, ViewMap vMap) {
        super(position);
        this.vMap = vMap;
    }

    public ViewMap getvMap() {
        return vMap;
    }
}
