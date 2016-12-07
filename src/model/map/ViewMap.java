package model.map;

/**
 * Created by sergi on 05/12/2016.
 */
public class ViewMap {

    private int size;

    private HeatElement map[][];

    public ViewMap(HeatElement[][] map) {
    }

    public ViewMap(int size) {
        this.size = size;
        map = new HeatElement[size][size];
    }

    public HeatElement[][] getMap() {
        return map;
    }
}
