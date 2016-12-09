package model.map;

import javafx.util.Pair;
import utilities.Utilities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sergi on 05/12/2016.
 */
public class ViewMap implements Serializable {

    private int size;

    private boolean[][] wasHere;

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

    public HeatElement getHeat(Pair<Integer, Integer> pos) {
        return this.map[pos.getValue()][pos.getKey()];
    }

    public HeatElement[][] getMap() {
        return map;
    }

    public ArrayList<Pair<Integer, Integer>> coosToExplore(Pair pos, int radioRange) {
        ArrayList<Pair<Integer, Integer>> rip = new ArrayList<>();
        for (int y = 0; y < this.size; y++) {
            for (int x = 0; x < this.size; x++) {
                ArrayList<Pair<Integer, Integer>> h = unexploredArea(this.map[y][x], pos, radioRange);
                if (h != null) {
                    rip.addAll(h);
                }
            }
        }
        return rip;
    }

    public ArrayList<Pair<Integer, Integer>> unexploredArea(HeatElement h, Pair pos, int radioRange) {
        ArrayList<Pair<Integer, Integer>> values = new ArrayList<>();
        if (Utilities.distPos(pos, new Pair<>(h.getX(), h.getY())) < radioRange) {
            return null;
        }
        if (h.heat > 1 || h.heat == 0) {
            return null;
        }
        if (h.getY() > 0 && this.map[h.getY() - 1][h.getX()].heat == -2 || this.map[h.getY() - 1][h.getX()].heat == 1) {
            values.add(new Pair<>(h.getX(), h.getY() - 1));
        }
        if (this.map[h.getY() + 1][h.getX()].heat == -2 || this.map[h.getY() + 1][h.getX()].heat == 1) {
            values.add(new Pair<>(h.getX(), h.getY() + 1));
        }
        if (this.map[h.getY()][h.getX() + 1].heat == -2 || this.map[h.getY()][h.getX() + 1].heat == 1) {
            values.add(new Pair<>(h.getX() + 1, h.getY()));
        }
        if (this.map[h.getY()][h.getX() - 1].heat == -2 || this.map[h.getY()][h.getX() - 1].heat == 1) {
            values.add(new Pair<>(h.getX() - 1, h.getY()));
        }
        return values;
    }

    public void addViewRange(Pair<Integer, Integer> pos, Map map, int viewRange) {
        //Norte
        for (int i = 0; i < viewRange && pos.getValue() - i > 0; i++) {
            if (map.getMap_in_array()[pos.getValue() - i][pos.getKey()] == 0) {
                if (i == 0) {
                    this.map[pos.getValue() - i][pos.getKey()].addMyHeat();   //current pos
                } else {
                    this.map[pos.getValue() - i][pos.getKey()].addVisionHeat();
                }
            } else if (map.getMap_in_array()[pos.getValue() - i][pos.getKey()] == 2) {
                this.map[pos.getValue() - i][pos.getKey()].addDoorHeat();
            } else {
                this.map[pos.getValue() - i][pos.getKey()].addWallHeat();
                break;
            }
        }
        //Sul
        for (int i = 0; i < viewRange && pos.getValue() + i > 0; i++) {
            if (map.getMap_in_array()[pos.getValue() + i][pos.getKey()] == 0) {
                if (i != 0) {
                    this.map[pos.getValue() + i][pos.getKey()].addVisionHeat();
                }
            } else if (map.getMap_in_array()[pos.getValue() - i][pos.getKey()] == 2) {
                this.map[pos.getValue() - i][pos.getKey()].addDoorHeat();
            } else {
                this.map[pos.getValue() + i][pos.getKey()].addWallHeat();
                break;
            }
        }
        //Este
        for (int i = 0; i < viewRange && pos.getKey() + i > 0; i++) {
            if (map.getMap_in_array()[pos.getValue()][pos.getKey() + i] == 0) {
                if (i != 0) {
                    this.map[pos.getValue()][pos.getKey() + i].addVisionHeat();
                }
            } else if (map.getMap_in_array()[pos.getValue() - i][pos.getKey()] == 2) {
                this.map[pos.getValue() - i][pos.getKey()].addDoorHeat();
            } else {
                this.map[pos.getValue()][pos.getKey() + i].addWallHeat();
                break;
            }
        }
        //Oeste
        for (int i = 0; i < viewRange && pos.getKey() - i > 0; i++) {
            if (map.getMap_in_array()[pos.getValue()][pos.getKey() - i] == 0) {
                if (i != 0) {
                    this.map[pos.getValue()][pos.getKey() - i].addVisionHeat();
                }
            } else if (map.getMap_in_array()[pos.getValue() - i][pos.getKey()] == 2) {
                this.map[pos.getValue() - i][pos.getKey()].addDoorHeat();
            } else {
                this.map[pos.getValue()][pos.getKey() - i].addWallHeat();
                break;
            }
        }
    }

    public void addViewMap(ViewMap map) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (this.map[i][j].heat == 0 && map.getMap()[i][j].heat != 1 &&
                        this.map[i][j].heat != map.getMap()[i][j].heat) {
                    this.map[i][j].heat = map.getMap()[i][j].heat;
                }
            }
        }
    }

    public boolean canMoveDir(DIR dir, Pair<Integer, Integer> pos) {
        //north
        if (dir == DIR.N && pos.getValue() - 1 > 0 && map[pos.getValue() - 1][pos.getKey()].heat != -2) {
            return true;
        }
        //south
        if (dir == DIR.S && pos.getValue() + 1 < map.length && map[pos.getValue() + 1][pos.getKey()].heat != -2) {
            return true;
        }
        //este
        if (dir == DIR.E && pos.getKey() + 1 < map.length && map[pos.getValue()][pos.getKey() + 1].heat != -2) {
            return true;
        }
        //oeste
        if (dir == DIR.W && pos.getKey() - 1 > 0 && map[pos.getValue()][pos.getKey() - 1].heat != -2) {
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

    //Returns null
    public ArrayList<Pair<Integer, Integer>> getPath(Pair<Integer, Integer> start, Pair<Integer, Integer> end) {
        ArrayList<Pair<Integer, Integer>> path = new ArrayList<>();
        if (getHeat(start).heat == -1 || getHeat(end).heat == -2) {
            return null;
        } else {
            recursiveSolve(start.getKey(), start.getValue(), end, path);
        }
        return path;
    }

    public boolean recursiveSolve(int x, int y, Pair<Integer, Integer> end, ArrayList<Pair<Integer, Integer>> path) {
        if (x == end.getKey() && y == end.getValue()) return true; // If you reached the end
        if (this.map[y][x].heat == -2 || wasHere[x][y]) return false;
        // If you are on a wall or already were here
        wasHere[x][y] = true;
        if (x != 0) // Checks if not on left edge
            if (recursiveSolve(x - 1, y, end, path)) { // Recalls method one to the left
                //correctPath[x][y] = true; // Sets that path value to true;
                path.add(new Pair<>(x, y));
                return true;
            }
        if (x != this.size - 1) // Checks if not on right edge
            if (recursiveSolve(x + 1, y, end, path)) { // Recalls method one to the right
                //correctPath[x][y] = true;
                return true;
            }
        if (y != 0)  // Checks if not on top edge
            if (recursiveSolve(x, y - 1, end, path)) { // Recalls method one up
                //correctPath[x][y] = true;
                path.add(new Pair<>(x, y));
                return true;
            }
        if (y != this.size - 1) // Checks if not on bottom edge
            if (recursiveSolve(x, y + 1, end, path)) { // Recalls method one down
                //correctPath[x][y] = true;
                path.add(new Pair<>(x, y));
                return true;
            }
        return false;
    }


    public enum DIR {N, S, E, W}
}
