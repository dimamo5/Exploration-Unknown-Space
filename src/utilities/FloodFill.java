package utilities;

import javafx.util.Pair;
import model.map.Map;

import java.awt.*;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

/**
 * Created by inesa on 10/12/2016.
 */
public class FloodFill {
    private ArrayList<Pair<Integer, Integer>> coords = new ArrayList<Pair<Integer, Integer>>();

    private ArrayList<Pair<Integer, Integer>> flood(Map map, boolean[][] mark,
                                                           int row, int col, int empty) {

        // make sure row and col are inside the image
        if (row < 0) return null;
        if (col < 0) return null;
        if (row >= map.getMap_in_array().length) return null;
        if (col >= map.getMap_in_array().length) return null;

        // make sure this pixel hasn't been visited yet
        if (mark[row][col]) return null;

        // make sure this pixel is the right color to fill
        if (map.getMap_in_array()[col][row] != -1) return null;

        // fill pixel with target color and mark it as visited
        coords.add(new Pair(row, col));
        mark[row][col] = true;

        // recursively fill surrounding pixels
        // (this is equivelant to depth-first search)
        flood(map, mark, row - 1, col, 0);
        flood(map, mark, row + 1, col, 0);
        flood(map, mark, row, col - 1, 0);
        flood(map, mark, row, col + 1, 0);

        return coords;

    }
}
