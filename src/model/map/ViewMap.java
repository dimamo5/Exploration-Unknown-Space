package model.map;

import javafx.util.Pair;

import java.util.ArrayList;

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
        this.map = new HeatElement[size][size];
    }

    public HeatElement[][] getMap() {
        return map;
    }

    public void addViewRange(Pair<Integer, Integer> pos, Map map, int viewRange) {
        //Norte
        for (int i = 0; i < viewRange && i + pos.getKey() > 0; i++) {
            if (map.getMap_in_array()[pos.getValue()][pos.getKey() + i] == 0) {
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
        for (int i = 0; i < viewRange && i + pos.getKey() > 0; i++) {
            if (map.getMap_in_array()[pos.getValue()][pos.getKey() - i] == 0) {
                if (i != 0) {
                    this.map[pos.getValue()][pos.getKey() - i].addVisionHeat();
                }
            } else {
                this.map[pos.getValue()][pos.getKey() - i].addWallHeat();
                break;
            }
        }
        //Este
        for (int i = 0; i < viewRange && i + pos.getKey() > 0; i++) {
            if (map.getMap_in_array()[pos.getValue() + i][pos.getKey()] == 0) {
                if (i != 0) {
                    this.map[pos.getValue() + i][pos.getKey()].addVisionHeat();
                }
            } else {
                this.map[pos.getValue() + i][pos.getKey()].addWallHeat();
                break;
            }
        }
        //Oeste
        for (int i = 0; i < viewRange && i + pos.getKey() > 0; i++) {
            if (map.getMap_in_array()[pos.getValue() - i][pos.getKey()] == 0) {
                if (i != 0) {
                    this.map[pos.getValue() - i][pos.getKey()].addVisionHeat();
                }
            } else {
                this.map[pos.getValue() - i][pos.getKey()].addWallHeat();
                break;
            }
        }
    }

    public void addViewMap(ViewMap map) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (this.map[i][j].heat == 0 && this.map[i][j].heat != map.getMap()[i][j].heat) {
                    this.map[i][j].heat = map.getMap()[i][j].heat;
                }
            }
        }
    }

    public ArrayList<DIR> getPossibleDir(Pair<Integer, Integer> pos) {
        ArrayList<DIR> posDir = new ArrayList<>();
        //north
        if (map[pos.getKey() + 1][pos.getValue()].heat == 0) {
            posDir.add(DIR.N);
        }
        //south
        if (map[pos.getKey() - 1][pos.getValue()].heat == 0) {
            posDir.add(DIR.S);
        }
        //este
        if (map[pos.getKey()][pos.getValue() + 1].heat == 0) {
            posDir.add(DIR.E);
        }
        //oeste
        if (map[pos.getKey()][pos.getValue() - 1].heat == 0) {
            posDir.add(DIR.W);
        }
        return posDir;
    }


    public enum DIR {N, S, E, W}
}
