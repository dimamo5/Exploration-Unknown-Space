package model.map;

import javafx.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sergi on 05/12/2016.
 */
public class ViewMap implements Serializable{

    private int size;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    private HeatElement map[][];

    public ViewMap(HeatElement[][] map) {
    }

    public ViewMap(int size) {
        this.size = size;
        map = new HeatElement[size][size];
        for (int i = 0; i < map.length; i++) {
            for (int m = 0; m < map.length; m++) {
                map[i][m] = new HeatElement(m, i);
            }
        }
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

    public boolean canMoveDir(DIR dir, Pair<Integer, Integer> pos) {
        //north
        if (dir == DIR.N && map[pos.getValue() - 1][pos.getKey()].heat != -2) {
            return true;
        }
        //south
        if (dir == DIR.S && map[pos.getValue() + 1][pos.getKey()].heat != -2) {
            return true;
        }
        //este
        if (dir == DIR.E && map[pos.getValue()][pos.getKey() + 1].heat != -2) {
            return true;
        }
        //oeste
        if (dir == DIR.W && map[pos.getValue()][pos.getKey() - 1].heat != -2) {
            return true;
        }
        return false;
    }

    public ArrayList<DIR> getPossibleDir(Pair<Integer, Integer> pos) {
        ArrayList<DIR> posDir = new ArrayList<>();
        //north
        if (map[pos.getValue() - 1][pos.getKey()].heat >= 0) {
            posDir.add(DIR.N);
        }
        //south
        if (map[pos.getValue() + 1][pos.getKey()].heat >= 0) {
            posDir.add(DIR.S);
        }
        //este
        if (map[pos.getValue()][pos.getKey() + 1].heat >= 0) {
            posDir.add(DIR.E);
        }
        //oeste
        if (map[pos.getValue()][pos.getKey() - 1].heat >= 0) {
            posDir.add(DIR.W);
        }
        return posDir;
    }

    public void print() {
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                System.out.print(this.map[i][j].heat + " ");
            }
            System.out.print("\n");
        }
    }


    public enum DIR {N, S, E, W}
}
