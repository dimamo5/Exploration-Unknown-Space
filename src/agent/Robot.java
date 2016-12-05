package agent;

import model.map.MapElement;
import model.map.Obstacle;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by sergi on 16/10/2016.
 */
public class Robot extends ExplorerAgent {

    private static final int DEFAULT_ENERGY = 15; //15 cells/turns by default

    private int energy = DEFAULT_ENERGY;

    public Robot(int id, int vision_range, int energy) {
        super(id, vision_range);
        this.energy = energy;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public void getInfo() {

    }


    private void move() {

        //se modo ñ eficient estiver ligado:
        //move_random();
        //se nao
        //move_efficient
    }

    private void move_random(){ //can the robot move after finding the exit????

       // Vector<MapElement> neighbors = get_closest_neighbors();

       // get_neighbors_empty_spaces(neighbors);

        //TODO.....move

        if(is_At_map_exit()){
            setAt_map_exit(true);
            setFound_map_exit(true);
        }
    }

    private ArrayList<MapElement> get_neighbors_empty_spaces(Vector<MapElement> neighbors) {

        ArrayList<MapElement> empty_spaces = new ArrayList<>();

        for(MapElement neighbour : neighbors ){
            if( !(neighbour instanceof Obstacle) ) {
                empty_spaces.add(neighbour);
            }
        }

        //TODO: REPETIDOS- podem haver objs na mesma posição e dar aso a que hajam: k empty_spaces, k = nº agentes no mesmo espaço
        return empty_spaces;
    }


}
