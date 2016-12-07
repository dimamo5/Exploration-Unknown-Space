package model.map;

import agent.ExplorerAgent;
import uchicago.src.sim.gui.SimGraphics;
import uchicago.src.sim.space.Object2DGrid;

import java.awt.*;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by sergi on 04/12/2016.
 */
public class AgentModel extends MapElement {

    protected static Object2DGrid map; //todo verificar se é suposto isto tar aqui
    private final ArrayList<ExplorerAgent> agents_list;

    public ArrayList<ExplorerAgent> getAgents_list() {
        return agents_list;
    }
    //TODO: é necessário obter os vizinhos através do método de vonneuman ? se sim deve ser feito nesta classe?

    public enum agent_type {CAPTAIN, SOLDIER, ROBOT}

    private agent_type type;

    public static Object2DGrid getMap() {
        return map;
    }

    public static void setMap(Object2DGrid map) {
        AgentModel.map = map;
    }

    public agent_type getType() {
        return type;
    }

    public void setType(agent_type type) {
        this.type = type;
    }

    public AgentModel(int pos_x, int pos_y, Object2DGrid c, agent_type type, ArrayList<ExplorerAgent> agents) {
        super(pos_x, pos_y, c);
        this.type = type;
        this.agents_list = agents;
    }

    protected Vector<MapElement> get_closest_neighbors() {
        //call to object2dGrid method
        //vonneuman neighbours -> https://pt.wikipedia.org/wiki/Vizinhan%C3%A7a_de_von_Neumann
        return map.getVonNeumannNeighbors(this.getX(), this.getY(), true);
    }

    public void move(int new_x, int new_y) {
        map.putObjectAt(getX(), getY(), null);
        map.putObjectAt(new_x, new_y, this);
    }

    @Override
    public void draw(SimGraphics simGraphics) {

        switch (type) {
            case CAPTAIN:
                simGraphics.drawRect(Color.ORANGE);
                break;
            case SOLDIER:
                simGraphics.drawRect(Color.cyan);
                break;
            case ROBOT:
                simGraphics.drawRect(Color.red);
                break;
            default:
                break;
        }

    }
}
