package agent;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import javafx.util.Pair;
import message.InformViewMap;
import message.Message;
import message.RequestViewMap;
import model.Model;
import model.map.AgentModel;
import model.map.ViewMap;
import sajas.core.Agent;
import sajas.core.behaviours.Behaviour;
import sajas.core.behaviours.CyclicBehaviour;
import sajas.core.behaviours.TickerBehaviour;
import uchicago.src.sim.space.Object2DGrid;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by sergi on 16/10/2016.
 */
public class Soldier extends Human {

    private agent_state state = agent_state.FINDING_EXIT;

    public Soldier(int vision_range, int radio_range) {
        super(vision_range, radio_range);
    }

    @Override
    protected void takeDown() {
        super.takeDown();
    }

    @Override
    protected void setup() {
        beginMsgListener();

        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                tick++;

                if (tick % 100 == 0) { //TODO destrolhar isto
                    move_random();
                }
            } //TODO period nao estar hardcoded
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

                if (msg != null) {

                    if (msg.getPerformative() == Message.REQUEST) {
                        sendMyInfoToAgent(msg);
                    } else if (msg.getPerformative() == Message.INFORM) {
                        try {
                            if (msg.getContentObject() instanceof InformViewMap) {

                                System.out.println("Previous viewmap");
                                getMyViewMap().print();

                                getMyViewMap().addViewMap(((InformViewMap) msg.getContentObject()).getViewMap());

                                System.out.println("Updated viewmap");
                                getMyViewMap().print();
                            }
                        } catch (UnreadableException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    private void update() {

        switch (state) {
            case FINDING_EXIT:
                //TODO

                ArrayList<ExplorerAgent> onRangeAgents = getModel_link().getOnRadioRangeAgents(getRadio_range());
                ArrayList<AID> robotsOnRange = new ArrayList<>();
                ArrayList<Soldier> soldiersOnRange = new ArrayList<>();


                for (Agent agent : onRangeAgents) {

                    if (agent instanceof Robot) {
                        Pair<Integer, Integer> robotCoos = ((Robot) agent).getModel_link().getMyCoos(),
                                humanCoos = getModel_link().getMyCoos();

                        if (robotIsInCommRange(humanCoos, robotCoos)) {
                            robotsOnRange.add(agent.getAID());
                        }

                    } else if (agent instanceof Soldier) {
                        //TODO
                    }/*else if(agent instanceof Captain ){  //pode ser útil saber os capitães q tao no viewRange

                    }*/
                }

                //comms with robots
                if (robotsOnRange.size() > 0) {
                    System.out.println(getAID() + "  requested info from robot(s)");
                    requestRobotForInfo(robotsOnRange);
                }

                //comms with soldiers

                //comms with captains  - ALL MAP RANGE VIA TELEFONE


                break;
            case AT_EXIT:
                break;
        }
    }


    public void move() {

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
}
