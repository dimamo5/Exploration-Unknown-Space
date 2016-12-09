package agent;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import javafx.util.Pair;
import message.InformViewMap;
import message.Message;
import model.Model;
import model.map.AgentModel;
import model.map.ViewMap;
import sajas.core.Agent;
import sajas.core.behaviours.CyclicBehaviour;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import static agent.Human.agent_state.WAITING_4_ORDERS;

/**
 * Created by sergi on 16/10/2016.
 */
public class Soldier extends Human {

    private agent_state state = WAITING_4_ORDERS;

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
                    update();
                    //move_random();
                }
                //update();
            }
            private static final long serialVersionUID = 1L;


        });
    }


    //TODO CREATE behaviour class 4 this
    private void beginMsgListener() {
        addBehaviour(new CyclicBehaviour(this) {
            private static final long serialVersionUID = 1L;

            @Override
            public void action() {
                ACLMessage msg = myAgent.receive();

                if (msg != null) {

                    if (msg.getPerformative() == Message.REQUEST) { //TODO CHANGE THIS TO RECEIVE MSG WITH ORDERS FROM CAPTAIN SE TIVER NO ESTADO WAITING 4 ORDERS
                        System.out.println("SENDING MY INFO>>>" + msg.getSender() );
                        sendMyInfoToAgent(msg);
                    } else if (msg.getPerformative() == Message.INFORM) {
                        try {
                            if (msg.getContentObject() instanceof InformViewMap) {
                                getMyViewMap().addViewMap(((InformViewMap) msg.getContentObject()).getViewMap());
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

        ArrayList<ExplorerAgent> onRangeAgents = getModel_link().getOnRadioRangeAgents(getRadio_range());
        ArrayList<AID> robotsOnRange = new ArrayList<>(), soldiersOnRange = new ArrayList<>();


        switch (state) {
            case WAITING_4_ORDERS:

                //communicates with agents(soldiers+robots) on range
                commWithAgents(onRangeAgents, robotsOnRange, soldiersOnRange);

                break;

            case EXPLORING:

                commWithAgents(onRangeAgents, robotsOnRange, soldiersOnRange);

                //TODO call method moving to target pos

                break;

            case AT_EXIT:
                break;
        }
    }

    private void commWithAgents(ArrayList<ExplorerAgent> onRangeAgents, ArrayList<AID> robotsOnRange, ArrayList<AID> soldiersOnRange) {
        for (Agent agent : onRangeAgents) {

            if (agent instanceof Robot) {
                Pair<Integer, Integer> robotCoos = ((Robot) agent).getModel_link().getMyCoos(),
                        humanCoos = getModel_link().getMyCoos();

                if (robotIsInCommRange(humanCoos, robotCoos)) {
                    robotsOnRange.add(agent.getAID());
                }

            } else if (agent instanceof Soldier) {
                soldiersOnRange.add(agent.getAID());
            }
        }

        //TODO adaptar para os restantes agentes tb

        ArrayList<AID> robotsToRequest = checkRobotComms(robotsOnRange);

        //robots + soldiers
        robotsToRequest.addAll(soldiersOnRange); //quitos trolha mas pronts

        //comms with robots+soldiers
        if (robotsToRequest.size() > 0) {
            System.out.println(getAID() + "  requested info from agent(s)");
            requestAgentsForInfo(robotsToRequest);
        }

        //comms with soldiers

        //comms with captains  - ALL MAP RANGE VIA TELEFONE
    }


    public void move() {

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
