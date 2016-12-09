package agent;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import javafx.util.Pair;
import message.Message;
import message.RequestViewMap;
import sajas.core.Agent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static utilities.Utilities.distPos;

/**
 * Created by sergi on 16/10/2016.
 */
public class Human extends ExplorerAgent {

    private static final int DEFAULT_RADIO_RANGE = 10;

    private int radio_range = DEFAULT_RADIO_RANGE;

    protected Map<AID,Long> communicatedRobots;


    public Human(int vision_range, int radio_range) {
        super(vision_range);
        this.radio_range = radio_range;
        communicatedRobots = new HashMap<>();
    }

    public int getRadio_range() {
        return radio_range;
    }

    public void setRadio_range(int radio_range) {
        this.radio_range = radio_range;
    }

    protected ArrayList<AID> checkRobotComms(ArrayList<AID> robotsOnRange) {
        ArrayList<AID> robotsToRequest = new ArrayList<>();

        for(AID ag : robotsOnRange){

            if(!communicatedRobots.containsKey(ag)){
                communicatedRobots.put(ag,tick);
                robotsToRequest.add(ag);
            }else{
                if(tick - communicatedRobots.get(ag) >= 200){
                    robotsToRequest.add(ag);
                    communicatedRobots.replace(ag,tick);
                }
            }
        }
        return robotsToRequest;
    }

    protected void commWithAgents(ArrayList<ExplorerAgent> onRangeAgents, ArrayList<AID> robotsOnRange, ArrayList<AID> soldiersOnRange) {
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

        //comms with captains  - ALL MAP RANGE VIA TELEFONE
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

    boolean robotIsInCommRange(Pair<Integer, Integer> humanCoos, Pair<Integer, Integer> robotCoos){
        return distPos(humanCoos, robotCoos) <= 1;
    }

}
