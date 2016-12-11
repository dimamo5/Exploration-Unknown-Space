package agent;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import javafx.util.Pair;
import message.InformViewMap;
import message.Message;
import message.RequestViewMap;
import model.Model;
import org.apache.velocity.runtime.parser.node.ASTElseIfStatement;
import sajas.core.Agent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static utilities.Utilities.distPos;

/**
 * Created by sergi on 16/10/2016.
 */
public class Human extends ExplorerAgent {

    private static final int DEFAULT_RADIO_RANGE = 10;

    private int radio_range = DEFAULT_RADIO_RANGE;

    protected Map<AID, Long> communicatedRobots;
    protected Pair<Integer, Integer> exitCoords;
    protected HashMap<AID, Long> commsAgentsList;

    public int getHumanUpdTickPeriod() {
        return humanUpdTickPeriod;
    }

    public void setHumanUpdTickPeriod(int humanUpdTickPeriod) {
        this.humanUpdTickPeriod = humanUpdTickPeriod;
    }

    protected int humanUpdTickPeriod = Model.HUMAN_UPD_TICK_PERIOD;

    public Human(int vision_range, int radio_range) {
        super(vision_range);
        this.radio_range = radio_range;
        communicatedRobots = new HashMap<>();
        commsAgentsList = new HashMap<>();
    }

    public int getRadio_range() {
        return radio_range;
    }

    public void setRadio_range(int radio_range) {
        this.radio_range = radio_range;
    }

    protected ArrayList<AID> checkRobotComms(ArrayList<AID> robotsOnRange) {
        ArrayList<AID> robotsToRequest = new ArrayList<>();

        for (AID ag : robotsOnRange) {

            if (!communicatedRobots.containsKey(ag)) {
                communicatedRobots.put(ag, tick);
                robotsToRequest.add(ag);
            } else {
                if (tick - communicatedRobots.get(ag) >= 200) {
                    robotsToRequest.add(ag);
                    communicatedRobots.replace(ag, tick);
                }
            }
        }
        return robotsToRequest;
    }

    protected void commWithAgents(ArrayList<ExplorerAgent> onRangeAgents) {

        ArrayList<AID> robotsOnRange = new ArrayList<>(), humansOnRange = new ArrayList<>();
        for (Agent agent : onRangeAgents) {

            if (agent instanceof Robot) {
                Pair<Integer, Integer> robotCoos = ((Robot) agent).getModel_link().getMyCoos(),
                        humanCoos = getModel_link().getMyCoos();

                if (robotIsInCommRange(humanCoos, robotCoos)) {
                    robotsOnRange.add(agent.getAID());
                }

            } else {
                humansOnRange.add(agent.getAID());
            }
        }


        ArrayList<AID> robotsToRequest = checkRobotComms(robotsOnRange);

        //robots + humans
        robotsToRequest.addAll(humansOnRange); //quitos trolha mas pronts

        Iterator<AID> iter = robotsToRequest.iterator();

        while (iter.hasNext()) {
            AID id = iter.next();

            if (commsAgentsList.containsKey(id)) {
                if ((tick - commsAgentsList.get(id)) > 20)
                    commsAgentsList.replace(id, tick);
                else
                    iter.remove();
            } else {
                commsAgentsList.put(id, tick);
            }
        }

        //comms with robots + humans
        if (robotsToRequest.size() > 0) {
            //System.out.println(getAID() + "  requested info from agent(s)");
            requestAgentsForInfo(robotsToRequest);
        }
    }

    protected void sendMyInfoToAgent(ACLMessage msg) {

        ACLMessage reply = msg.createReply();
        reply.setPerformative(Message.INFORM);

        try {
            Pair<Integer, Integer> pos = new Pair<>(getModel_link().getX(), getModel_link().getY());
            reply.setContentObject(new InformViewMap(pos, getMyViewMap()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        send(reply);
    }

    protected enum agent_state {WAITING_4_ORDERS, INITIAL_COMM_WITH_CAPTAINS, WAITING_4_TEAM_RESPONSES, GIVING_ORDERS, EXPLORING, EXPLORATION_DONE, AT_EXIT}

    void requestAgentsForInfo(ArrayList<AID> agents) {
        ACLMessage msg = new ACLMessage(Message.REQUEST);

        Pair<Integer, Integer> pos = new Pair<>(getModel_link().getX(), getModel_link().getY());
        try {
            msg.setContentObject(new RequestViewMap(pos));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (AID agent : agents) {
            msg.addReceiver(agent);
        }
        send(msg);
    }

    boolean robotIsInCommRange(Pair<Integer, Integer> humanCoos, Pair<Integer, Integer> robotCoos) {
        return distPos(humanCoos, robotCoos) <= 1;
    }

}
