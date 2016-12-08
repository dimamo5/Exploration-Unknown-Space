package agent;

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

/**
 * Created by sergi on 16/10/2016.
 */
public class Captain extends Human {

    public int cellphone_range;

    private agent_state state = agent_state.INITIAL_COMM_WITH_CAPTAINS;

    public ArrayList<AID> getTeamSoldiers() {
        return teamSoldiers;
    }

    public void setTeamSoldiers(ArrayList<AID> teamSoldiers) {
        this.teamSoldiers = teamSoldiers;
    }

    private ArrayList<AID> teamSoldiers;

    public Captain(int vision_range, int radio_range, int cellphone_range) {
        super(vision_range, radio_range);
        this.cellphone_range = cellphone_range;
        teamSoldiers = new ArrayList<>();
    }

    public int getCellphone_range() {
        return cellphone_range;
    }

    public void setCellphone_range(int cellphone_range) {
        this.cellphone_range = cellphone_range;
    }

    public void move() {

    }

    public void addSoldierToTeam(AID sol){
        teamSoldiers.add(sol);
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
            }

            private static final long serialVersionUID = 1L;


        });
    }


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
        ArrayList<AID> robotsOnRange = new ArrayList<>();
        ArrayList<AID> soldiersOnRange = new ArrayList<>();

        switch (state) {

            case INITIAL_COMM_WITH_CAPTAINS:
                ArrayList<AID> allCaptains = getAllCaptains();

                //todo desenvolver isto
                break;

            case EXPLORING:
                for (Agent agent : onRangeAgents) {

                    if (agent instanceof Robot) {
                        Pair<Integer, Integer> robotCoos = ((Robot) agent).getModel_link().getMyCoos(),
                                humanCoos = getModel_link().getMyCoos();

                        if (robotIsInCommRange(humanCoos, robotCoos)) {
                            robotsOnRange.add(agent.getAID());
                        }

                    } else if (agent instanceof Soldier) {
                        soldiersOnRange.add(agent.getAID());
                    }/*else if(agent instanceof Captain ){  //pode ser útil saber os capitães q tao no viewRange

                    }*/
                }

                ArrayList<AID> robotsToRequest = checkRobotComms(robotsOnRange);

                //comms with robots
                if (robotsToRequest.size() > 0) {
                    System.out.println(getAID() + "  requested info from robot(s)");
                    requestAgentsForInfo(robotsToRequest);
                }

                //comms with soldiers

                //comms with captains  - ALL MAP RANGE VIA TELEFONE

                break;
            /*case AT_EXIT:
                break; */
        }
    }

    private ArrayList<AID> getAllCaptains() {
        ArrayList<AID> captains = new ArrayList<>();

        for(Agent agent: getModel_link().getAgents_list()){
            if(agent instanceof Captain){
                captains.add(agent.getAID());
            }
        }

        return captains;
    }


    private void requestForInfo(AID aid) {
        /*try {
            ACLMessage msg = new ACLMessage(Message.REQUEST);

            System.out.println("#" + getId()+ "  sending request");

            msg.setContentObject(m);
            msg.addReceiver(aid);

            send(msg);

        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }


    private void sendMyInfoToAgent(ACLMessage msg) {

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

    //used on behaviour that informs every captain
    //todo needs rework to only send the msg to captains  (simulating cellphone communication)
    private void sendMyInfo() {

        try {
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);

            msg.setContentObject("MyInfo");

            for (ExplorerAgent agent : getModel_link().getAgents_list()) {
                if (agent.getAID() != this.getAID()) {
                    msg.addReceiver(agent.getAID());
                }
            }
            send(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
