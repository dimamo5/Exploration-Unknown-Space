package message;

import javafx.util.Pair;

/**
 * Created by sergi on 10/12/2016.
 */
public class PropagateExit extends Message {
    public PropagateExit(Pair<Integer, Integer> position) {
        super(position);
    }
}
