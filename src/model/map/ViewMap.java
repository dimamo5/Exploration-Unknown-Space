package model.map;

import javafx.util.Pair;
import model.Model;
import utilities.Utilities;

import java.io.Serializable;
import java.util.*;

/**
 * Created by sergi on 05/12/2016.
 */
public class ViewMap implements Serializable {

    private int size;

    private boolean[][] wasHere;
    private HeatElement map[][];
    private boolean exitFound = false;
    private Pair<Integer, Integer> exit;

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
        wasHere = new boolean[size][size];
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
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
                Set<Pair<Integer, Integer>> h = unexploredArea(this.map[y][x], pos, radioRange);
                if (h != null) {
                    rip.addAll(h);
                }
            }
        }
        return rip;
    }

    public Set<Pair<Integer, Integer>> unexploredArea(HeatElement h, Pair pos, int radioRange) {
        Set<Pair<Integer, Integer>> values = new HashSet<>();
        if (Utilities.distPos(pos, new Pair<>(h.getX(), h.getY())) > radioRange) {
            return null;
        }
        if (h.heat < 0) {
            return null;
        }
        if (h.getY() - 1 > 0 && this.map[h.getY() - 1][h.getX()].heat == -1) {
            values.add(new Pair<>(h.getX(), h.getY()));
        }
        if (h.getY() + 1 < this.size && this.map[h.getY() + 1][h.getX()].heat == -1) {
            values.add(new Pair<>(h.getX(), h.getY()));
        }
        if (h.getX() + 1 < this.size && this.map[h.getY()][h.getX() + 1].heat == -1) {
            values.add(new Pair<>(h.getX(), h.getY()));
        }
        if (h.getX() - 1 > 0 && this.map[h.getY()][h.getX() - 1].heat == -1) {
            values.add(new Pair<>(h.getX(), h.getY()));
        }
        return values;
    }

    public boolean addViewMapPos(Pair<Integer, Integer> pos, boolean hor) {
        boolean wall = false;
        if (Model.getForest().getMap_in_array()[pos.getValue()][pos.getKey()] == 1) {
            this.map[pos.getValue()][pos.getKey()].addWallHeat();
            wall = true;
        } else if (Model.getForest().getMap_in_array()[pos.getValue()][pos.getKey()] == 2) {
            this.map[pos.getValue()][pos.getKey()].addDoorHeat();
            wall = true;
            exitFound = true;
            this.exit = new Pair<>(pos.getKey(), pos.getValue());
        } else if (Model.getForest().getMap_in_array()[pos.getValue()][pos.getKey()] == 0) {
            this.map[pos.getValue()][pos.getKey()].addMyHeat();
        }

        if (!hor) {
            if (pos.getKey() - 1 >= 0 && pos.getKey() + 1 < this.size) {
                if (Model.getForest().getMap_in_array()[pos.getValue()][pos.getKey() - 1] == 1) {
                    this.map[pos.getValue()][pos.getKey() - 1].addWallHeat();
                } else if (Model.getForest().getMap_in_array()[pos.getValue()][pos.getKey() - 1] == 2) {
                    this.map[pos.getValue()][pos.getKey() - 1].addDoorHeat();
                    this.exitFound = true;
                    this.exit = new Pair<>(pos.getKey() - 1, pos.getValue());
                }
                if (Model.getForest().getMap_in_array()[pos.getValue()][pos.getKey() + 1] == 1) {
                    this.map[pos.getValue()][pos.getKey() + 1].addWallHeat();
                } else if (Model.getForest().getMap_in_array()[pos.getValue()][pos.getKey() + 1] == 2) {
                    this.map[pos.getValue()][pos.getKey() + 1].addDoorHeat();
                    this.exitFound = true;
                    this.exit = new Pair<>(pos.getKey() + 1, pos.getValue());
                }
            }
        } else if (hor) {
            if (pos.getValue() - 1 >= 0 && pos.getValue() + 1 < this.size) {
                if (Model.getForest().getMap_in_array()[pos.getValue() - 1][pos.getKey()] == 1) {
                    this.map[pos.getValue() - 1][pos.getKey()].addWallHeat();
                } else if (Model.getForest().getMap_in_array()[pos.getValue() - 1][pos.getKey()] == 2) {
                    this.map[pos.getValue() - 1][pos.getKey()].addDoorHeat();
                    this.exitFound = true;
                    this.exit = new Pair<>(pos.getKey(), pos.getValue() - 1);
                }

                if (Model.getForest().getMap_in_array()[pos.getValue() + 1][pos.getKey()] == 1) {
                    this.map[pos.getValue() + 1][pos.getKey()].addWallHeat();
                } else if (Model.getForest().getMap_in_array()[pos.getValue() + 1][pos.getKey()] == 2) {
                    this.map[pos.getValue() + 1][pos.getKey()].addDoorHeat();
                    this.exitFound = true;
                    this.exit = new Pair<>(pos.getKey(), pos.getValue() + 1);

                }
            }
        }
        return wall;
    }

    public boolean addViewRange(Pair<Integer, Integer> pos, Map map, int viewRange) {
        boolean foundExit = false;
        this.map[pos.getValue()][pos.getKey()].addMyHeat();

        for (int i = 0; i < viewRange && pos.getValue() - i >= 0; i++) {
            if (addViewMapPos(new Pair<>(pos.getKey(), pos.getValue() - i), false)) {
                break;
            }
        }

        for (int i = 0; i < viewRange && pos.getValue() + i < this.size; i++) {
            if (addViewMapPos(new Pair<>(pos.getKey(), pos.getValue() + i), false)) {
                break;
            }
        }

        for (int i = 0; i < viewRange && pos.getKey() - i >= 0; i++) {
            if (addViewMapPos(new Pair<>(pos.getKey() - i, pos.getValue()), true)) {
                break;
            }
        }

        for (int i = 0; i < viewRange && pos.getKey() + i < this.size; i++) {
            if (addViewMapPos(new Pair<>(pos.getKey() + i, pos.getValue()), true)) {
                break;
            }
        }

        return foundExit;
    }

    public void addViewMap(ViewMap map) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (this.map[i][j].heat == -1 &&
                        this.map[i][j].heat != map.getMap()[i][j].heat) {
                    //this.map[i][j].heat = map.getMap()[i][j].heat;
                    if (map.getMap()[i][j].heat == -2) {
                        this.map[i][j].addWallHeat();
                    } else if (map.getMap()[i][j].heat > 0 || map.getMap()[i][j].heat == 0) {
                        this.map[i][j].addOtherHeat();
                    }
                }
                if (map.isExitFound()) {
                    this.exitFound = true;
                    this.exit = new Pair<>(map.getExitCoords().getKey(), map.getExitCoords().getValue());
                }
            }
        }
    }

    public Pair<Integer, Integer> getExitCoords() {
        return this.exit;
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
        if (pos.getValue() - 1 > 0 && map[pos.getValue() - 1][pos.getKey()].heat != -2) {
            posDir.add(DIR.N);
        }
        //south
        if (pos.getValue() + 1 < this.size && map[pos.getValue() + 1][pos.getKey()].heat != -2) {
            posDir.add(DIR.S);
        }
        //este
        if (pos.getKey() + 1 < this.size && map[pos.getValue()][pos.getKey() + 1].heat != -2) {
            posDir.add(DIR.E);
        }
        //oeste
        if (pos.getKey() - 1 > 0 && map[pos.getValue()][pos.getKey() - 1].heat != -2) {
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
        this.wasHere = new boolean[this.size][this.size];
        recursiveSolve(start.getKey(), start.getValue(), end, path);
        if (path.size() > 0) {
            if (Utilities.distPos(path.get(path.size() - 1), start) >= 1) {
                Collections.reverse(path);
            }
            path.remove(path.size() - 1); //Removed current element
            //path.add(end);  // add end pos
            path.add(0, end);
        }
        return path;
    }

    public boolean recursiveSolve(int x, int y, Pair<Integer, Integer> end, ArrayList<Pair<Integer, Integer>> path) {
        if (x == end.getKey() && y == end.getValue()) return true; // If you reached the end
        if (Model.getForest().getMap_in_array()[y][x] == 1 || wasHere[x][y]) return false;
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
                path.add(new Pair<>(x, y));
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

    public boolean isExitFound() {
        return exitFound;
    }

    public Pair<Integer, Integer> getRegroupSite(ArrayList<Pair<Integer, Integer>> possiblePos, Pair<Integer, Integer>
            capPos) {
        ArrayList<Double> prob = new ArrayList<Double>();
        ArrayList<Integer> dist = new ArrayList<>();
        for (Pair pos : possiblePos) {
            prob.add(calcProbExit(pos));
            dist.add(getPath(capPos, pos).size());
        }

        int closest = dist.indexOf(Collections.min(dist));
        return possiblePos.get(closest);
    }

    private Double calcProbExit(Pair<Integer, Integer> pos) {
        final int RADIO = 5;
        int countWhite = 0, countBlack = 0;
        for (int y = 0; y < RADIO && pos.getValue() - y > 0 && pos.getValue() + y < this.size; y++) {
            for (int x = 0; x < RADIO && pos.getKey() - x > 0 && pos.getKey() + x < this.size; x++) {
                if (this.map[pos.getValue() - y][pos.getKey() - x].heat == -1) {
                    countWhite++;
                } else {
                    countBlack++;
                }
                if (this.map[pos.getValue() + y][pos.getKey() - x].heat == -1) {
                    countWhite++;
                } else {
                    countBlack++;
                }
                if (this.map[pos.getValue() - y][pos.getKey() + x].heat == -1) {
                    countWhite++;
                } else {
                    countBlack++;
                }
                if (this.map[pos.getValue() + y][pos.getKey() + x].heat == -1) {
                    countWhite++;
                } else {
                    countBlack++;
                }
            }
        }
        return countWhite / (double) countBlack;
    }

    public ArrayList<Pair<Integer, Integer>> closestPoints(ArrayList<Pair<Integer, Integer>> agentsCoords,
                                                           ArrayList<Pair<Integer, Integer>> possibleCoords) {
        ArrayList<Pair<Integer, Integer>> pointsToAgent = new ArrayList<>();
        for (int i = 0; i < agentsCoords.size() || possibleCoords.size() > 0; i++) {
            Pair<Integer, Integer> agentCoord = possibleCoords.get(i);
            Collections.sort(possibleCoords, (o1, o2) -> {
                int dist1 = Utilities.distPos(agentCoord, o1);
                int dist2 = Utilities.distPos(agentCoord, o2);
                if (dist1 > dist2) {
                    return 1;
                } else if (dist1 == dist2) {
                    return 0;
                } else return -1;
            });
            pointsToAgent.add(possibleCoords.get(0));
            possibleCoords.remove(0);
        }
        return pointsToAgent;
    }


    public enum DIR {N, S, E, W}
}
