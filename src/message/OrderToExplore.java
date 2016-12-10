package message;

import javafx.util.Pair;

/**
 * Created by sergi on 08/12/2016.
 */
public class OrderToExplore extends Message {

    public boolean isGoToExit() {
        return goToExit;
    }

    public void setGoToExit(boolean goToExit) {
        this.goToExit = goToExit;
    }

    private boolean goToExit = false;

    public OrderToExplore(Pair<Integer, Integer> position, boolean goToExit) {
        super(position);
        this.goToExit = goToExit;
    }
}
