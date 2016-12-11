package model.map;

import agent.ExplorerAgent;
import jade.core.AID;
import javafx.util.Pair;
import uchicago.src.sim.gui.SimGraphics;
import uchicago.src.sim.space.Object2DGrid;
import utilities.Utilities;

import java.awt.*;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by sergi on 04/12/2016.
 */
public class AgentModel extends MapElement {

    protected static Object2DGrid globalMap; //todo verificar se é suposto isto tar aqui
    private final ArrayList<ExplorerAgent> agents_list;
    private agent_type type;
    //TODO: é necessário obter os vizinhos através do método de vonneuman ? se sim deve ser feito nesta classe?

    public AgentModel(int pos_x, int pos_y, Object2DGrid global, agent_type type, ArrayList<ExplorerAgent> agents) {
        super(pos_x, pos_y);
        this.type = type;
        this.agents_list = agents;
        globalMap = global;
    }

    public Pair<Integer, Integer> getMyCoos() {
        return new Pair<>(getX(), getY());
    }

    public ArrayList<AID> getRobotsFromAgentList() {  //testing purposes
        ArrayList<AID> robots = new ArrayList<>();

        for (ExplorerAgent agent : agents_list) {
            if (agent instanceof agent.Robot) {
                robots.add(agent.getAID());
            }
        }

        return robots;
    }

    public ArrayList<ExplorerAgent> getOnRadioRangeAgents(int radio_range) {
        ArrayList<ExplorerAgent> agentsInRange = new ArrayList<>();
        for (ExplorerAgent agent : this.agents_list) {
            Pair<Integer, Integer> pos = new Pair<>(agent.getModel_link().getX(), agent.getModel_link().getY());
            Pair<Integer, Integer> currentpos = new Pair<>(this.getX(), this.getY());
            int range = Utilities.distPos(pos, currentpos);
            if (this.type != agent_type.ROBOT && radio_range >= range) { //robots cant request info
                agentsInRange.add(agent);
            }
        }
        return agentsInRange;
    }

    public ArrayList<ExplorerAgent> getOnViewRangeAgents(int viewRange) {
        ArrayList<ExplorerAgent> agentsInRange = new ArrayList<>();
        Pair<Integer, Integer> pos = new Pair<>(getX(), getY());
        /*//Norte
        for (int i = 0; i < viewRange && i + pos.getKey() > 0; i++) {
            if (Model.getForest().getMap_in_array()[pos.getValue()][pos.getKey() + i] == 0) {
                if (i == 0) {
                    this.map[pos.getValue()][pos.getKey() + i].addMyHeat();
                } else {
                    this.map[pos.getValue()][pos.getKey() + i].addVisionHeat();
                }
            } else {
                this.map[pos.getValue()][pos.getKey() + i].addWallHeat();
                break;
            }
        }
        //Sul
        for (int i = 0; i < viewRange && i + pos.getKey() > 0; i++) {
            if (map.getMap_in_array()[pos.getValue()][pos.getKey() - i] == 0) {
                if (i != 0) {
                    this.map[pos.getValue()][pos.getKey() - i].addVisionHeat();
                }
            } else {
                this.map[pos.getValue()][pos.getKey() - i].addWallHeat();
                break;
            }
        }
        //Este
        for (int i = 0; i < viewRange && i + pos.getKey() > 0; i++) {
            if (map.getMap_in_array()[pos.getValue() + i][pos.getKey()] == 0) {
                if (i != 0) {
                    this.map[pos.getValue() + i][pos.getKey()].addVisionHeat();
                }
            } else {
                this.map[pos.getValue() + i][pos.getKey()].addWallHeat();
                break;
            }
        }
        //Oeste
        for (int i = 0; i < viewRange && i + pos.getKey() > 0; i++) {
            if (map.getMap_in_array()[pos.getValue() - i][pos.getKey()] == 0) {
                if (i != 0) {
                    this.map[pos.getValue() - i][pos.getKey()].addVisionHeat();
                }
            } else {
                this.map[pos.getValue() - i][pos.getKey()].addWallHeat();
                break;
            }
        }*/
        return agentsInRange;
    }

    public static Object2DGrid getGlobalMap() {
        return globalMap;
    }

    public static void setGlobalMap(Object2DGrid globalMap) {
        AgentModel.globalMap = globalMap;
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
        return globalMap.getVonNeumannNeighbors(this.getX(), this.getY(), true);
    }


    @Override
    public void draw(SimGraphics simGraphics) {

        switch (type) {
            case CAPTAIN:
                simGraphics.drawRect(Color.ORANGE);
                simGraphics.drawString("C", Color.BLACK);
                break;
            case SOLDIER:
                simGraphics.drawRect(Color.cyan);
                simGraphics.drawString("S", Color.BLACK);
                break;
            case ROBOT:
                simGraphics.drawRect(Color.red);
                simGraphics.drawString("R", Color.BLACK);
                break;
            default:
                break;
        }

    }

    public enum agent_type {CAPTAIN, SOLDIER, ROBOT}
}
