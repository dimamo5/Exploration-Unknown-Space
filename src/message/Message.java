package message;


import javafx.util.Pair;

import java.io.Serializable;

/**
 * Created by sergi on 07/12/2016.
 */
public class Message implements Serializable{

    public static final int INFORM = 7, REQUEST = 16, PROPAGATE = 21;

    private Pair<Integer,Integer> position ;

    public Message( Pair<Integer,Integer>  position){
        this.position = position;
    }

    public Pair<Integer, Integer> getPosition() {
        return position;
    }

    public void setPosition(Pair<Integer, Integer> position) {
        this.position = position;
    }
}
