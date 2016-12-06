package model.map;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * Created by sergi on 16/10/2016.
 */
public class Map {

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

    private static boolean between(int v, int upper) {
        return (v >= 0) && (v < upper);
    }

    public static void main(String[] args) {

        Map map = new Map(10, 10);
        map.print();

        for (int i = 0; i < map.getHeight(); i++)
            System.out.println(Arrays.toString(map.map_in_array[i]));

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
                //System.out.print((map[j][i] & 1) == 0 ? "+-" : "+ ");
                s += (map[j][i] & 1) == 0 ? "+-" : "+ ";
            }
            //System.out.println("+");
            s += "+\n";
            // draw the west edge
            for (int j = 0; j < input_width; j++) {
                //System.out.print((map[j][i] & 8) == 0 ? "| " : "  ");
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
                "map=" + Arrays.toString(map) +
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
                //System.out.print((map[j][i] & 1) == 0 ? "+-" : "+ ");
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
                //System.out.print((map[j][i] & 8) == 0 ? "| " : "  ");
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
                        this.exitSide =DIR.N;
                        return;
                    }
                    break;
                case 1:
                    if (map_in_array[pos][this.width - 2] == 0) {
                        this.exit = new int[]{this.width - 1, pos};
                        this.exitSide =DIR.E;
                        return;
                    }
                    break;
                case 2:
                    if (map_in_array[this.height - 2][pos] == 0) {
                        this.exit = new int[]{pos, this.height - 1};
                        this.exitSide =DIR.S;
                        return;
                    }
                    break;
                case 3:
                    if (map_in_array[pos][1] == 0) {
                        this.exit = new int[]{0, pos};
                        this.exitSide =DIR.W;
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
}
