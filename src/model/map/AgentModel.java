package model.map;

import uchicago.src.sim.space.Object2DGrid;

import java.util.Vector;

/**
 * Created by sergi on 04/12/2016.
 */
public class AgentModel extends MapElement {

    protected static Object2DGrid map; //todo verificar se é suposto isto tar aqui
    //TODO: é necessário obter os vizinhos através do método de vonneuman ? se sim deve ser feito nesta classe?

    public AgentModel(int pos_x, int pos_y, Object2DGrid c) {
        super(pos_x, pos_y, c);
    }

    protected Vector<MapElement> get_closest_neighbors() {
        //call to object2dGrid method
        //vonneuman neighbours -> https://pt.wikipedia.org/wiki/Vizinhan%C3%A7a_de_von_Neumann

        return map.getVonNeumannNeighbors(this.getX(),this.getY(), true);
    }
}
