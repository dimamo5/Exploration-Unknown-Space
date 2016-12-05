package model.map;

import jade.content.Predicate;

/**
 * Created by sergi on 05/12/2016.
 */
public class ViewMap implements Predicate{

    private boolean map[][];

    public ViewMap(boolean[][] map) {
        this.map = map;
    }

    public boolean[][] getMap() {
        return map;
    }

    public void setMap(boolean[][] map) {
        this.map = map;
    }

}
