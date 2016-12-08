package agent;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import javafx.util.Pair;
import message.Message;
import message.RequestViewMap;

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

    protected enum agent_state {FINDING_EXIT, AT_EXIT}

    void requestRobotForInfo(ArrayList<AID> robots) {
        ACLMessage msg = new ACLMessage(Message.REQUEST);

        Pair<Integer, Integer> pos = new Pair<>(getModel_link().getX(), getModel_link().getY());
        try {
            msg.setContentObject(new RequestViewMap(pos));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (AID robot : robots) {
            msg.addReceiver(robot);
        }
        send(msg);
    }

    public boolean robotIsInCommRange(Pair<Integer,Integer> humanCoos, Pair<Integer,Integer> robotCoos){
        return distPos(humanCoos, robotCoos) <= 1;
    }

}
