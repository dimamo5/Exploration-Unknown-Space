package message;

import javafx.util.Pair;
import model.map.ViewMap;

/**
 * Created by sergi on 07/12/2016.
 */
public class InformViewMap extends Message {

    private ViewMap viewMap;

    public InformViewMap(Pair<Integer,Integer> position, ViewMap viewMap) {
        super(position);
        this.viewMap = viewMap;
    }

    public ViewMap getViewMap() {
        return viewMap;
    }

    public void setViewMap(ViewMap viewMap) {
        this.viewMap = viewMap;
    }
}
