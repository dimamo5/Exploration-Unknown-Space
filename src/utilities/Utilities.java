package utilities;

import javafx.util.Pair;
import model.map.HeatElement;

import java.awt.*;
import java.util.*;
import java.util.List;

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

    public static void main(String[] args) {
        System.out.println(distPos(new Pair<>(1, 1), new Pair<>(1, 2)));
    }
}
