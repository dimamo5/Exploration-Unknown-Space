package model;

import javax.media.protocol.SourceTransferHandler;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

/**
 * Created by sergi on 16/10/2016.
 */
public class Map {

    private int[][] map;

    public Map(String filename) {
        load_map(filename);
    }

    public int[][] getMap() {
        return map;
    }

    public void setMap(int[][] map) {
        this.map = map;
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

    private void print_map(){

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                System.out.print(map[j][i]);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {

        Map map = new Map("raw_map_1.txt");
        map.print_map();
    }
}
