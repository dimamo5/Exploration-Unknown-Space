package message;

import javafx.util.Pair;

/**
 * Created by sergi on 09/12/2016.
 */
public class ExplorationResponse extends Message {

    public ExplorationResponse(Pair<Integer, Integer> position) {
        super(position);
    }
}
