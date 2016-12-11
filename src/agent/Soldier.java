package agent;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import javafx.util.Pair;
import message.*;
import model.Model;
import sajas.core.behaviours.CyclicBehaviour;
import utilities.Utilities;

import java.io.IOException;
import java.util.*;

import static agent.Human.agent_state.*;

/**
 * Created by sergi on 16/10/2016.
 */
public class Soldier extends Human {

    private agent_state state = WAITING_4_ORDERS;
    private Stack<Pair<Integer, Integer>> coosToExplore;

    public ArrayList<Soldier> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(ArrayList<Soldier> teamMembers) {
        this.teamMembers = teamMembers;
    }

    private ArrayList<Soldier> teamMembers;

    public AID getTeamLeader() {
        return teamLeader;
    }

    public void setTeamLeader(AID teamLeader) {
        this.teamLeader = teamLeader;
    }

    private AID teamLeader;

    public Soldier(int vision_range, int radio_range) {
        super(vision_range, radio_range);
        teamMembers = new ArrayList<>();
    }


    @Override
    protected void setup() {
        beginMsgListener();

        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                tick++;

                if (tick % Model.humanUpdTickPeriod == 0) { //TODO destrolhar isto
                    //System.out.println(getAID() + " state: " + state);
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

                    if (state == AT_EXIT)
                        return;

                    if (msg.getPerformative() == Message.REQUEST || msg.getPerformative() == Message.INFORM) {
                        handleMsg(msg);
                    }
                }
            }
        });
    }


    private void handleMsg(ACLMessage msg) {

        Message toParseMsg = null;
        try {
            toParseMsg = (Message) msg.getContentObject();
        } catch (UnreadableException e) {
            e.printStackTrace();
        }

        if(toParseMsg instanceof OrderToExplore && (((OrderToExplore) toParseMsg).isGoToExit())){
            //System.out.println(getAID() + "state: " + state + "  RECEIVED ORDER WHILE EXPLORING");
        }

        if (toParseMsg instanceof InformTeam && state == EXPLORATION_DONE) {
            //System.out.println("RECEIVED INFORM TEAM");
            myViewMap.addViewMap(((InformTeam) toParseMsg).getViewMap());
            state = WAITING_4_ORDERS;

        } else if (toParseMsg instanceof OrderToExplore && (state == EXPLORING || state == EXPLORATION_DONE)) {
            state = EXPLORING;
            //System.out.println(getAID() + "  RECEIVED ORDER WHILE EXPLORING");

            coosToExplore = new Stack<>();

            if (((OrderToExplore) toParseMsg).isGoToExit()) {
                //System.out.println(msg.getSender() + "  >>>>>>>>>>>GO TO EXIT  >>" + getAID());
                found_map_exit = true;
                exitCoords = toParseMsg.getPosition();
            }

            Pair<Integer, Integer> destiny = toParseMsg.getPosition();
            //System.out.println(getAID() + "AT POS " + getModel_link().getMyCoos() + " RECEIVED ORDER TO MOVE TO: " + destiny);

            if (Utilities.distPos(getModel_link().getMyCoos(), destiny) != 0) {
                ArrayList<Pair<Integer, Integer>> pathCoos = myViewMap.getPath(getModel_link().getMyCoos(), destiny);
                pushToStack(pathCoos);
            }
            //System.out.println("MY COOS TO EXPLORE: " + coosToExplore.toString());

        } else if (toParseMsg instanceof OrderToExplore && state == WAITING_4_ORDERS) {
            state = EXPLORING;
            coosToExplore = new Stack<>();

            if (((OrderToExplore) toParseMsg).isGoToExit()) {
                //System.out.println(msg.getSender() + "  >>>>>>>>>>>GO TO EXIT  >>" + getAID());
                found_map_exit = true;
                exitCoords = toParseMsg.getPosition();
            }

            Pair<Integer, Integer> destiny = toParseMsg.getPosition();
            //System.out.println(getAID() + "AT POS " + getModel_link().getMyCoos() + " RECEIVED ORDER TO MOVE TO: " + destiny);

            if (Utilities.distPos(getModel_link().getMyCoos(), destiny) != 0) {
                ArrayList<Pair<Integer, Integer>> pathCoos = myViewMap.getPath(getModel_link().getMyCoos(), destiny);
                pushToStack(pathCoos);

                // System.out.println("MY COOS TO EXPLORE: " + coosToExplore.toString());
            }
        } else if (toParseMsg instanceof RequestViewMap) {
            //System.out.println(getAID() + ">> SENDING MY INFO >>" + msg.getSender());
            sendMyInfoToAgent(msg);
        }
    }


    private void pushToStack(ArrayList<Pair<Integer, Integer>> pathCoos) {
        //Collections.reverse(pathCoos);
        for (Pair<Integer, Integer> pathCoo : pathCoos) {
            coosToExplore.push(pathCoo);
        }
    }

    private void update() {

        ArrayList<ExplorerAgent> onRangeAgents = getModel_link().getOnRadioRangeAgents(getRadio_range());

        for (Soldier sol : teamMembers) {
            onRangeAgents.remove(sol);
        }

        onRangeAgents.removeIf(ag -> ag.getAID() == teamLeader || teamMembers.indexOf(ag) != -1 || ag.getAID() == this.getAID());

        switch (state) {
            case WAITING_4_ORDERS:
                commWithAgents(onRangeAgents);
                break;

            case EXPLORING:
                commWithAgents(onRangeAgents);

                if (exitCoords != null & !found_map_exit)
                    found_map_exit = true;

                if (coosToExplore.size() > 0) {
                    Pair<Integer, Integer> newPos = coosToExplore.pop();
                    updatePosition(newPos);
                    getMyViewMap().addViewRange(newPos, Model.getForest(), getVision_range());
                    if (coosToExplore.size() == 0 || (exitCoords != null && newPos == exitCoords)) {
                        state = EXPLORATION_DONE;

                        if (found_map_exit) {
                            state = AT_EXIT;
                            at_map_exit = true;
                            //System.out.println("FOUND EXIT NOW NOTIFYING CAPTAIN");
                        }
                        notifyTeamLeader(new ExplorationResponse(getModel_link().getMyCoos(), getMyViewMap(), found_map_exit), Message.INFORM);
                    }
                } else {
                    state = EXPLORATION_DONE;
                    if (found_map_exit) {
                        state = AT_EXIT;
                        at_map_exit = true;
                        //System.out.println("FOUND EXIT NOW NOTIFYING CAPTAIN");
                    }
                    notifyTeamLeader(new ExplorationResponse(getModel_link().getMyCoos(), getMyViewMap(), found_map_exit), Message.INFORM);
                }
                break;

            case EXPLORATION_DONE:
                commWithAgents(onRangeAgents);
                if (found_map_exit) {
                    //System.out.println("SOLDIER>>>>>>>>>>>>>FOUND EXIT");
                }
                if (at_map_exit || (exitCoords != null && exitCoords.equals(getModel_link().getMyCoos()))) {
                    state = AT_EXIT;
                }
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

}
