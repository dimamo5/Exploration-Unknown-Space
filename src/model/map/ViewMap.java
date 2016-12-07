package model.map;

import javafx.util.Pair;

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

    public void addViewRange(Pair<Integer, Integer> pos, Map map, int range) {
        //Norte
        for (int i = 0; i < range && i + pos.getKey() > 0; i++) {
            if (map.getMap()[pos.getValue()][pos.getKey() + i] == 0 && i == 0) {
                if (i == 0) {
                    this.map[pos.getValue()][pos.getKey() + i].addMyHeat();
                } else {
                    this.map[pos.getValue()][pos.getKey() + i].addVisionHeat();
                }
            } else {
                this.map[pos.getValue()][pos.getKey() + i].addWallHeat();
                break;
            }
        }
        //Sul
        for (int i = 0; i < range && i + pos.getKey() > 0; i++) {
            if (map.getMap()[pos.getValue()][pos.getKey() - i] == 0 && i == 0) {
                if (i == 0) {
                    this.map[pos.getValue()][pos.getKey() - i].addMyHeat();
                } else {
                    this.map[pos.getValue()][pos.getKey() - i].addVisionHeat();
                }
            } else {
                this.map[pos.getValue()][pos.getKey() - i].addWallHeat();
                break;
            }
        }
        //Este
        for (int i = 0; i < range && i + pos.getKey() > 0; i++) {
            if (map.getMap()[pos.getValue() + i][pos.getKey()] == 0 && i == 0) {
                if (i == 0) {
                    this.map[pos.getValue() + i][pos.getKey()].addMyHeat();
                } else {
                    this.map[pos.getValue() + i][pos.getKey()].addVisionHeat();
                }
            } else {
                this.map[pos.getValue() + i][pos.getKey()].addWallHeat();
                break;
            }
        }
        //Oeste
        for (int i = 0; i < range && i + pos.getKey() > 0; i++) {
            if (map.getMap()[pos.getValue() - i][pos.getKey()] == 0 && i == 0) {
                if (i == 0) {
                    this.map[pos.getValue() - i][pos.getKey()].addMyHeat();
                } else {
                    this.map[pos.getValue() - i][pos.getKey()].addVisionHeat();
                }
            } else {
                this.map[pos.getValue() - i][pos.getKey()].addWallHeat();
                break;
            }
        }
    }

    public void addViewMap(Map map) {
        //TODO tomorrow
    }
}
