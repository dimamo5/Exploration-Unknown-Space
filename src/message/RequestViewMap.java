package message;

import javafx.util.Pair;

/**
 * Created by sergi on 07/12/2016.
 */
public class RequestViewMap extends Message {

    public RequestViewMap(Pair<Integer,Integer> position) {
        super(position);
    }
}
