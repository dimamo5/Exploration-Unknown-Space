package utilities;

import javafx.util.Pair;
import model.map.HeatElement;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by diogo on 08/12/2016.
 */
public class Utilities {
    public Utilities() {
    }

    public static int distPos(Pair<Integer, Integer> pos1, Pair<Integer, Integer> pos2) {
        return (int) Math.ceil(Math.sqrt(Math.pow((pos1.getKey() - pos2.getKey()), 2) + Math.pow((pos1.getValue() -
                pos2.getValue()), 2)));
    }

    public static ArrayList<HeatElement> twoDArrayToList(HeatElement[][] twoDArray) {
        ArrayList<HeatElement> list = new ArrayList<HeatElement>();
        for (HeatElement[] array : twoDArray) {
            list.addAll(Arrays.asList(array));
        }
        return list;
    }

    public static boolean posEqual(Pair<Integer, Integer> pos1, Pair<Integer, Integer> pos2) {
        return pos1.getKey().equals(pos2.getKey()) && pos1.getValue().equals(pos2.getValue());
    }

    public static void printArrayPair(ArrayList<Pair<Integer, Integer>> array) {
        System.out.println(Arrays.toString(array.toArray()));
    }
}
