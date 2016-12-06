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
    private agent_type type;
    //TODO: é necessário obter os vizinhos através do método de vonneuman ? se sim deve ser feito nesta classe?

    public AgentModel(int pos_x, int pos_y, Object2DGrid c, agent_type type, ArrayList<ExplorerAgent> agents) {
        super(pos_x, pos_y, c);
        this.type = type;
        this.agents_list = agents;
    }

    public static Object2DGrid getMap() {
        return map;
    }

    public static void setMap(Object2DGrid map) {
        AgentModel.map = map;
    }

    public ArrayList<ExplorerAgent> getAgents_list() {
        return agents_list;
    }

    public agent_type getType() {
        return type;
    }

    public void setType(agent_type type) {
        this.type = type;
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
                //TODO agentModel receives label param and uses it in last param of bellow call at <"agent">
                simGraphics.drawStringInOval(Color.cyan,Color.white, "agent");
                break;
            case SOLDIER:
                simGraphics.drawFastCircle(Color.ORANGE);
                break;
            case ROBOT:
                simGraphics.drawFastCircle(Color.red);
                break;
            default:
                break;
        }
    }

    public enum agent_type {CAPTAIN, SOLDIER, ROBOT}
}
