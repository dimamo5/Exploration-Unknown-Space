package model.map;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * Created by sergi on 16/10/2016.
 */
public class Map implements Serializable {

    private int input_width, input_height, width, height;
    private int[][] map;
    private int[][] map_in_array;
    private int[] exit;
    private DIR exitSide;

    public Map(int width, int height) {

        input_width = width;
        input_height = height;

        map = new int[this.input_width][this.input_height];
        generateMaze(0, 0);

        this.width = width * 2 + 1;
        this.height = height * 2 + 1;
        map_in_array = new int[this.height][this.width];
        getMapInArray();
        createExit();
        map_in_array[this.exit[1]][this.exit[0]] = 2;
    }

    public static void saveMap(Map m) {
        try {

            // Write to disk with FileOutputStream
            FileOutputStream f_out = new
                    FileOutputStream("./res/map.data");

            ObjectOutputStream obj_out = new
                    ObjectOutputStream(f_out);

            obj_out.writeObject(m);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map loadMap() {
        // Read from disk using FileInputStream
        FileInputStream f_in = null;
        try {
            f_in = new
                    FileInputStream("./res/map.data");
            // Read object using ObjectInputStream
            ObjectInputStream obj_in =
                    new ObjectInputStream(f_in);
            // Read an object
            Object obj = obj_in.readObject();
            if (obj instanceof Map) {
                // Cast object to a Vector
                return (Map) obj;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static boolean between(int v, int upper) {
        return (v >= 0) && (v < upper);
    }

    public static void main(String[] args) {

        Map map = new Map(10, 10);
        map.print();

     /*   int[] posCapitain = globalMap.createPositions();
        System.out.println("capitain " + Arrays.toString(posCapitain));
*/
      /*  ArrayList<int[]> soldiers = globalMap.createSoldiersPosition(posCapitain, 5, 5);

        for (int i = 0; i < soldiers.size(); i++) {
            System.out.println("soldier " + i + ' ' + Arrays.toString(soldiers.get(i)));
        }*/

        ArrayList<int[]> capitains = map.createCapitainsPosition(8, 15);

        System.out.println(map.countSpaces(capitains.get(0), map, 2).toString());


        /*

        for (int i = 0; i < capitains.size(); i++) {
            System.out.println("capitain " + i + ' ' + Arrays.toString(capitains.get(i)));
        }

        System.out.println("exit " + Arrays.toString(map.exit));

        for (int i = 0; i < map.getHeight(); i++) {
            System.out.println(Arrays.toString(map.map_in_array[i]));
        }
        */


    }

    public int[][] getMap_in_array() {
        return map_in_array;
    }

    public int[][] getMap() {
        return map;
    }

    public void setMap(int[][] map) {
        this.map = map;
    }

    public DIR getExitSide() {
        return exitSide;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private void generateMaze(int cx, int cy) {
        Map.DIR[] dirs = Map.DIR.values();
        Collections.shuffle(Arrays.asList(dirs));
        for (Map.DIR dir : dirs) {
            int nx = cx + dir.dx;
            int ny = cy + dir.dy;
            if (between(nx, input_width) && between(ny, input_height)
                    && (map[nx][ny] == 0)) {
                map[cx][cy] |= dir.bit;
                map[nx][ny] |= dir.opposite.bit;
                generateMaze(nx, ny);
            }
        }
    }

    public String getPrettyMap() {
        String s = "";

        for (int i = 0; i < input_height; i++) {
            // draw the north edge
            for (int j = 0; j < input_width; j++) {
                //System.out.print((globalMap[j][i] & 1) == 0 ? "+-" : "+ ");
                s += (map[j][i] & 1) == 0 ? "+-" : "+ ";
            }
            //System.out.println("+");
            s += "+\n";
            // draw the west edge
            for (int j = 0; j < input_width; j++) {
                //System.out.print((globalMap[j][i] & 8) == 0 ? "| " : "  ");
                s += (map[j][i] & 8) == 0 ? "| " : "  ";
            }
            //System.out.println("|");
            s += "|\n";
        }
        // draw the bottom line
        for (int j = 0; j < input_width; j++) {
            //System.out.print("+-");
            s += "+-";
        }
        //System.out.println("+");
        s += "+\n";
        return s;
    }

    @Override
    public String toString() {
        return "Map{" +
                "globalMap=" + Arrays.toString(map) +
                '}';
    }

    public void print() {
        System.out.println(getPrettyMap());
    }

    private void getMapInArray() {
        //String s = "";

        for (int i = 0; i < input_height; i++) {
            // draw the north edge
            for (int j = 0; j < input_width; j++) {
                //System.out.print((globalMap[j][i] & 1) == 0 ? "+-" : "+ ");
                if ((map[j][i] & 1) == 0) {
                    map_in_array[i * 2][j * 2] = 1;
                    map_in_array[i * 2][j * 2 + 1] = 1;
                } else {
                    map_in_array[i * 2][j * 2] = 1;
                    map_in_array[i * 2][j * 2 + 1] = 0;
                }
            }

            map_in_array[i * 2][this.width - 1] = 1;
            //System.out.println("+");
            //s += "+\n";
            // draw the west edge
            for (int j = 0; j < input_width; j++) {
                //System.out.print((globalMap[j][i] & 8) == 0 ? "| " : "  ");
                if ((map[j][i] & 8) == 0) {
                    map_in_array[i * 2 + 1][j * 2] = 1;
                    map_in_array[i * 2 + 1][j * 2 + 1] = 0;
                } else {
                    map_in_array[i * 2 + 1][j * 2] = 0;
                    map_in_array[i * 2 + 1][j * 2 + 1] = 0;
                }
            }
            //System.out.println("|");
            //s += "|\n";
            map_in_array[i * 2 + 1][this.width - 1] = 1;
        }
        // draw the bottom line
        for (int j = 0; j < input_width; j++) {
            //System.out.print("+-");
            map_in_array[this.height - 1][j * 2] = 1;
            map_in_array[this.height - 1][j * 2 + 1] = 1;
        }
        //System.out.println("+");
        map_in_array[this.width - 1][this.height - 1] = 1;
        //s += "+\n";
    }

    private void createExit() {
        while (this.exit == null) {
            int side = new Random().nextInt(3);
            int pos = new Random().nextInt(this.width);
            switch (side) {
                case 0:
                    if (map_in_array[1][pos] == 0) {
                        this.exit = new int[]{pos, 0};
                        this.exitSide = DIR.N;
                        return;
                    }
                    break;
                case 1:
                    if (map_in_array[pos][this.width - 2] == 0) {
                        this.exit = new int[]{this.width - 1, pos};
                        this.exitSide = DIR.E;
                        return;
                    }
                    break;
                case 2:
                    if (map_in_array[this.height - 2][pos] == 0) {
                        this.exit = new int[]{pos, this.height - 1};
                        this.exitSide = DIR.S;
                        return;
                    }
                    break;
                case 3:
                    if (map_in_array[pos][1] == 0) {
                        this.exit = new int[]{0, pos};
                        this.exitSide = DIR.W;
                        return;
                    }
                    break;
            }
        }
    }

    private enum DIR {
        N(1, 0, -1), S(2, 0, 1), E(4, 1, 0), W(8, -1, 0);

        // use the static initializer to resolve forward references
        static {
            N.opposite = S;
            S.opposite = N;
            E.opposite = W;
            W.opposite = E;
        }

        private final int bit;
        private final int dx;
        private final int dy;
        private DIR opposite;

        DIR(int bit, int dx, int dy) {
            this.bit = bit;
            this.dx = dx;
            this.dy = dy;
        }
    }

    public int[] createPositions() {
        int[] a = new int[]{};

        while (a.length == 0) {
            int posY = new Random().nextInt(this.height);
            int posX = new Random().nextInt(this.width);

            switch (exitSide) {
                case N:
                    posY = posY / 3;
                    if (map_in_array[this.height - posY - 1][posX] == 0) {
                        a = new int[]{posX, this.height - 1 - posY};
                        return a;
                    }
                    break;
                case E:
                    posX = posX / 3;
                    if (map_in_array[posY][posX] == 0) {
                        a = new int[]{posX, posY};
                        return a;
                    }
                    break;
                case S:
                    posY = posY / 3;
                    if (map_in_array[posY][posX] == 0) {
                        a = new int[]{posX, posY};
                        return a;
                    }
                    break;
                case W:
                    posX = posX / 3;
                    if (map_in_array[posY][this.width - 1 - posX] == 0) {
                        a = new int[]{this.width - 1 - posX, posY};
                        return a;
                    }
                    break;
            }
        }
        return a;
    }

    public int[] countSpaces(int[] capitainPosition, Map map, int numSoldiers) {
        int[] count = {0, 0, 0, 0};

        for (int i = 1; i <= numSoldiers; i++) {
            //Norte
            if (capitainPosition[1] - i > 0 && map.getMap_in_array()[capitainPosition[1] - i][capitainPosition[0]] ==
                    0) {
                count[0]++;
            } else {
                break;
            }
        }

        for (int i = 1; i <= numSoldiers; i++) {
            //Sul
            if (capitainPosition[1] + i > map.getMap_in_array().length && map.getMap_in_array()[capitainPosition[1] +
                    i][capitainPosition[0]] == 0) {
                count[1]++;
            } else {
                break;
            }
        }

        for (int i = 1; i <= numSoldiers; i++) {
            //Este
            if (capitainPosition[0] + i > map.getMap_in_array().length && map.getMap_in_array()
                    [capitainPosition[1]][capitainPosition[0] + i] == 0) {
                count[2]++;
            } else {
                break;
            }
        }

        for (int i = 1; i <= numSoldiers; i++) {
            //Oeste
            if (capitainPosition[0] - i > 0 && map.getMap_in_array()[capitainPosition[1]][capitainPosition[0] - i] ==
                    0) {
                count[3]++;
            } else {
                break;
            }
        }

        return count;
    }


    public ArrayList<int[]> createSoldiersPosition(ArrayList<int[]> capitains, int[] capitainPosition, int
            numSoldiers, int viewRange, int distance) {
        ArrayList<int[]> soldiers = new ArrayList<>();
        int[] count = countSpaces(capitainPosition, this, numSoldiers);
        int sum = IntStream.of(count).sum();
        boolean valid = true;

        for (int i = 0; i < capitains.size(); i++) {
            double dst = Math.sqrt((capitains.get(i)[0] - capitainPosition[0]) * (capitains.get(i)[0] -
                    capitainPosition[0]) + (capitains.get(i)[1] - capitainPosition[1]) * (capitains.get(i)[1] -
                    capitainPosition[1]));
            if (!(dst >= distance && distance > 0 && dst < this.getWidth() && !capitains.contains(capitainPosition)
                    && capitainPosition != capitains.get(i))) {
                valid = false;
            }
        }

        while (sum < numSoldiers && !valid) {
            capitainPosition = createPositions();
            count = countSpaces(capitainPosition, this, numSoldiers);
            sum = IntStream.of(count).sum();
        }


        soldiers.add(capitainPosition);
        int[] soldier;
        int i = 0, rand;
        while (i < numSoldiers) {
            rand = new Random().nextInt(4);
            if (rand == 0 && count[rand] > 0) {
                soldier = new int[]{capitainPosition[0], capitainPosition[1] - count[rand]};
                soldiers.add(soldier);
                count[rand]--;
                i++;
            } else if (rand == 1 && count[rand] > 0) {
                soldier = new int[]{capitainPosition[0], capitainPosition[1] + count[rand]};
                soldiers.add(soldier);
                count[rand]--;
                i++;
            } else if (rand == 2 && count[rand] > 0) {
                soldier = new int[]{capitainPosition[0] + count[rand], capitainPosition[1]};
                soldiers.add(soldier);
                count[rand]--;
                i++;
            } else if (rand == 3 && count[rand] > 0) {
                soldier = new int[]{capitainPosition[0] - count[rand], capitainPosition[1]};
                soldiers.add(soldier);
                count[rand]--;
                i++;
            }
        }

        return soldiers;

    }

    public ArrayList<int[]> createCapitainsPosition(int numCapitains, int distance) {

        ArrayList<int[]> capitains = new ArrayList<int[]>();
        int[] capitain = createPositions();
        capitains.add(capitain);

        for (int i = 1; i < numCapitains; i++) {
            while (capitains.size() <= i) {
                capitain = createPositions();
                double dst = Math.sqrt((capitains.get(i - 1)[0] - capitain[0]) * (capitains.get(i - 1)[0] -
                        capitain[0]) + (capitains
                        .get(i - 1)[1] - capitain[1]) * (capitains.get(i - 1)[1] - capitain[1]));
                if (dst >= distance && distance > 0 && dst < this.getWidth() && !capitains.contains(capitain) &&
                        capitain != capitains
                                .get(i -
                                        1)) {
                    capitains.add(capitain);
                }
            }
        }

        for (int i = 0; i < capitains.size(); i++)
            System.out.println("Capitain: " + distance + "----------------" + capitains.get(i)[0] + " " + capitains.get(
                    i)[1]);

        return capitains;
    }

    public ArrayList<int[]> createRobotsPosition(int numRobots) {
        int[] newPos = new int[]{};
        ArrayList<int[]> robots = new ArrayList<int[]>();
        for (int i = 0; i < numRobots; i++) {
            while (newPos.length == 0) {
                int posY = new Random().nextInt(this.height);
                int posX = new Random().nextInt(this.width);

                if (map_in_array[this.height - posY - 1][posX] == 0) {
                    newPos = new int[]{posX, this.height - 1 - posY};
                    robots.add(newPos);
                    newPos = new int[]{};
                    break;
                }
            }
        }
        return robots;

    }


}
