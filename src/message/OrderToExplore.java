package message;

import javafx.util.Pair;

/**
 * Created by sergi on 08/12/2016.
 */
public class OrderToExplore extends Message {

    public OrderToExplore(Pair<Integer, Integer> position) {
        super(position);
    }
}
