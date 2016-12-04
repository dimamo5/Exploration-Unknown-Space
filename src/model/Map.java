package model;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by sergi on 16/10/2016.
 */
public class Map {

    private int input_width,input_height, width, height;
    private int[][] map;
    private char[][] map_in_array;

    public char[][] getMap_in_array() {
        return map_in_array;
    }

    public void setMap_in_array(char[][] map_in_array) {
        this.map_in_array = map_in_array;
    }

    public int getInput_width() {
        return input_width;
    }

    public void setInput_width(int input_width) {
        this.input_width = input_width;
    }

    public int getInput_height() {
        return input_height;
    }

    public void setInput_height(int input_height) {
        this.input_height = input_height;
    }

    public Map(String filename) {
        load_map(filename);
    }

    public Map(int width, int height) {

        input_width = width;
        input_height = height;

        map = new int[this.input_width][this.input_height];
        generateMaze(0, 0);

        this.width = width*2+2;
        this.height = height*2+1;
        this.map_in_array = getMapInArray();
    }

    public int[][] getMap() {
        return map;
    }

    public void setMap(int[][] map) {
        this.map = map;
    }

    private static boolean between(int v, int upper) {
        return (v >= 0) && (v < upper);
    }

    private enum DIR {
        N(1, 0, -1), S(2, 0, 1), E(4, 1, 0), W(8, -1, 0);
        private final int bit;
        private final int dx;
        private final int dy;
        private DIR opposite;

        // use the static initializer to resolve forward references
        static {
            N.opposite = S;
            S.opposite = N;
            E.opposite = W;
            W.opposite = E;
        }

        private DIR(int bit, int dx, int dy) {
            this.bit = bit;
            this.dx = dx;
            this.dy = dy;
        }
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
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

    private void load_map(String filename) {

        int width, height;

        Path file = FileSystems.getDefault().getPath("input", filename);

        try (InputStream in = Files.newInputStream(file);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {

            String[] size; //dimensions format : width|height
            String line = null;
            line = reader.readLine();

            if (line != null) {
                size = line.split("-");
                width = Integer.valueOf(size[0]);
                height = Integer.valueOf(size[1]);
                map = new int[width][height];
            } else {
                System.err.println("load_map() - error reading dimensions from map file");
                return;
            }

            for (int i = 0; i < width; i++) {
                line = reader.readLine();

                if (line == null || line.length() != map[i].length) {
                    System.err.println("load_map(): Error in map file dimensions.");
                    break;
                }
                for (int j = 0; j < line.length(); j++) {
                    map[j][i] = (int) line.charAt(j) - '0';
                }
            }

        } catch (IOException x) {
            System.err.println("load_map(): " + x.getMessage());
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

    private char[][] getMapInArray(){

        String s = getPrettyMap();
        char x[] = s.toCharArray();

        char c[][] = new char[height][width]; //newline

        for (int i = 0, index = 0; i < height; i++, index += width) {
            c[i] = Arrays.copyOfRange(x, index, index+width);
        }

        return c;
    }

    public static void main(String[] args) {

        Map map = new Map(10, 10);
        for(int i = 0; i< map.getHeight(); i++)
            System.out.println(Arrays.toString(map.getMap_in_array()[i]));

    }
}
