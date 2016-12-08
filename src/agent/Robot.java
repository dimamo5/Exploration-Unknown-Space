package agent;

import cern.colt.Arrays;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import javafx.util.Pair;
import message.InformViewMap;
import message.Message;
import message.RequestViewMap;
import model.Model;
import model.map.AgentModel;
import model.map.MapElement;
import model.map.Obstacle;
import model.map.ViewMap;
import sajas.core.behaviours.Behaviour;
import sajas.core.behaviours.CyclicBehaviour;
import sajas.core.behaviours.TickerBehaviour;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

/**
 * Created by sergi on 16/10/2016.
 */
public class Robot extends ExplorerAgent {

    private Robot_state state = Robot_state.MOVING; //BY default

    public enum Robot_state {MOVING, OUT_OF_ENERGY}

    private static final int DEFAULT_ENERGY = 15; //15 cells/turns by default
    private int energy = DEFAULT_ENERGY;

    public Robot(int vision_range, int energy) {
        super(vision_range);
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

    @Override
    protected void setup() {
        beginMsgListener();

        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                tick++;

                if(tick % 100 == 0 ) { //TODO period nao estar hardcoded
                    update();
                }
            }
            private static final long serialVersionUID = 1L;


        });
    }


    //TODO CREATE beahviour class 4 this
    private void beginMsgListener() {
        addBehaviour(new CyclicBehaviour(this) {
            private static final long serialVersionUID = 1L;

            @Override
            public void action() {
                ACLMessage msg = myAgent.receive();

                try {
                    if (msg != null && msg.getPerformative() == Message.REQUEST && msg.getContentObject() instanceof
                            RequestViewMap) {
                        System.out.println("Robot#" + getAID() + " sending viewmap");
                        sendMyInfoToAgent(msg);
                    }
                } catch (UnreadableException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void update() {

        switch (state) {
            case MOVING:
                moveRobot();   // --energy > 0 -> moverandomly..
                break;
            case OUT_OF_ENERGY:
                break;
        }
    }

    private void sendMyInfoToAgent(ACLMessage msg) {
        ACLMessage reply = msg.createReply();
        reply.setPerformative(Message.INFORM);

        try {
            Pair<Integer, Integer> pos = new Pair<>(getModel_link().getX(), getModel_link().getY());
            InformViewMap inform = new InformViewMap(pos, getMyViewMap());
            reply.setContentObject(inform);
        } catch (IOException e) {
            e.printStackTrace();
        }

        send(reply);
    }

    private void moveRobot() {

        if (--energy >= 0) {
            move_random();
        }else{
            state = Robot_state.OUT_OF_ENERGY;
        }
    }

    private void move_random() {

        Pair<Integer, Integer> oldPos = new Pair<>(getModel_link().getX(), getModel_link().getY());
        Pair<Integer, Integer> newPos;

        if (current_dir == null || !getMyViewMap().canMoveDir(current_dir, oldPos)) {
            ArrayList<ViewMap.DIR> possibleDirs = getMyViewMap().getPossibleDir(oldPos);

            Random r = new Random();
            int dir = r.nextInt(possibleDirs.size());

            //new coordinates
            current_dir = possibleDirs.get(dir);
        }

        newPos = move(current_dir);

        //move on globalMap

        AgentModel.setGlobalMap(AgentModel.getGlobalMap()); //extract these calls to 1 method

        //update pos
        getModel_link().setPos_x(newPos.getKey());
        getModel_link().setPos_y(newPos.getValue());

        //update viewmap
        getMyViewMap().addViewRange(newPos, Model.getForest(), getVision_range());
    }

    private ArrayList<MapElement> get_neighbors_empty_spaces(Vector<MapElement> neighbors) {

        ArrayList<MapElement> empty_spaces = new ArrayList<>();

        for (MapElement neighbour : neighbors) {
            if (!(neighbour instanceof Obstacle)) {
                empty_spaces.add(neighbour);
            }
        }

        //TODO: REPETIDOS- podem haver objs na mesma posição e dar aso a que hajam: k empty_spaces, k = nº agentes no
        // mesmo espaço
        return empty_spaces;
    }


}
