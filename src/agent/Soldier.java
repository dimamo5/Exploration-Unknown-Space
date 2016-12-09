package agent;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import javafx.util.Pair;
import message.*;
import model.Model;
import model.map.ViewMap;
import sajas.core.Agent;
import sajas.core.behaviours.CyclicBehaviour;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import static agent.Human.agent_state.EXPLORATION_DONE;
import static agent.Human.agent_state.EXPLORING;
import static agent.Human.agent_state.WAITING_4_ORDERS;

/**
 * Created by sergi on 16/10/2016.
 */
public class Soldier extends Human {

    private agent_state state = WAITING_4_ORDERS;
    private Stack<Pair<Integer, Integer>> coosToExplore;

    public AID getTeamLeader() {
        return teamLeader;
    }

    public void setTeamLeader(AID teamLeader) {
        this.teamLeader = teamLeader;
    }

    private AID teamLeader;

    public Soldier(int vision_range, int radio_range) {
        super(vision_range, radio_range);
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
                }
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

                    if (msg.getPerformative() == Message.REQUEST) {
                        handleRequest(msg);

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

    private void handleRequest(ACLMessage msg) {

        Message toParseMsg = null;
        try {
            toParseMsg = (Message) msg.getContentObject();
        } catch (UnreadableException e) {
            e.printStackTrace();
        }

        if (toParseMsg instanceof OrderToExplore && state == WAITING_4_ORDERS) {
            state = EXPLORING;
            coosToExplore = new Stack<>();

            Pair<Integer, Integer> destiny = toParseMsg.getPosition();
            ArrayList<Pair<Integer, Integer>> pathCoos = myViewMap.getPath(getModel_link().getMyCoos(), destiny);
            pushToStack(pathCoos);

        } else if (toParseMsg instanceof RequestViewMap) {
            System.out.println(getAID() + " SENDING MY INFO>>" + msg.getSender());
            sendMyInfoToAgent(msg);
        }
    }

    private void pushToStack(ArrayList<Pair<Integer, Integer>> pathCoos) {
        for (int i = 0; i < pathCoos.size(); i++) {
            coosToExplore.push(pathCoos.get(pathCoos.size() - (i + 1)));
        }
    }

    private void update() {

        ArrayList<ExplorerAgent> onRangeAgents = getModel_link().getOnRadioRangeAgents(getRadio_range());
        ArrayList<AID> robotsOnRange = new ArrayList<>(), soldiersOnRange = new ArrayList<>();

        switch (state) {
            case WAITING_4_ORDERS:
                //communicates with agents(soldiers+robots) on range
                //commWithAgents(onRangeAgents, robotsOnRange, soldiersOnRange);

                break;

            case EXPLORING:
                //commWithAgents(onRangeAgents, robotsOnRange, soldiersOnRange);

                if (coosToExplore.size() > 0) {
                    Pair<Integer, Integer> newPos = coosToExplore.pop();
                    updatePosition(newPos);
                    getMyViewMap().addViewRange(newPos, Model.getForest(), getVision_range());

                } else {
                    notifyTeamLeader(new ExplorationResponse(getModel_link().getMyCoos(), getMyViewMap(),foundExit), Message.INFORM);
                    state = EXPLORATION_DONE;
                }
                break;

            case EXPLORATION_DONE:
                commWithAgents(onRangeAgents, robotsOnRange, soldiersOnRange);
                break;

            case AT_EXIT:
                break;
        }
    }

    private void notifyTeamLeader(Message myMessage, Integer performative) {
        ACLMessage msg = new ACLMessage(performative);
        try {
            msg.setContentObject(myMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        msg.addReceiver(teamLeader);
        send(msg);
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
