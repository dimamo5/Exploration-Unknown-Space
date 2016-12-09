package agent;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import javafx.util.Pair;
import message.ExplorationResponse;
import message.InformViewMap;
import message.Message;
import message.OrderToExplore;
import model.Model;
import model.map.AgentModel;
import model.map.ViewMap;
import sajas.core.Agent;
import sajas.core.behaviours.CyclicBehaviour;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import static agent.Human.agent_state.*;

/**
 * Created by sergi on 16/10/2016.
 */
public class Captain extends Human {

    public int cellphone_range;

    private agent_state state = INITIAL_COMM_WITH_CAPTAINS;
    private ArrayList<AID> teamSoldiers, wentExploringSoldiers;
    private boolean captainMove = false;
    private Stack<Pair<Integer, Integer>> coosToExplore;

    public Captain(int vision_range, int radio_range, int cellphone_range) {
        super(vision_range, radio_range);
        this.cellphone_range = cellphone_range;
        teamSoldiers = new ArrayList<>();
    }

    public ArrayList<AID> getTeamSoldiers() {
        return teamSoldiers;
    }

    public void setTeamSoldiers(ArrayList<AID> teamSoldiers) {
        this.teamSoldiers = teamSoldiers;
    }

    public int getCellphone_range() {
        return cellphone_range;
    }

    public void setCellphone_range(int cellphone_range) {
        this.cellphone_range = cellphone_range;
    }

    public void move() {

    }

    public void addSoldierToTeam(AID sol) {
        teamSoldiers.add(sol);
    }

    @Override
    protected void setup() {

        beginMsgListener();

        addBehaviour(new CyclicBehaviour(this) {
            private static final long serialVersionUID = 1L;

            @Override
            public void action() {
                tick++;

                if (tick % 100 == 0) { //TODO destrolhar isto
                    System.out.println("CAPTAIN state: " + state);
                    update();
                    //move_random();
                }
            }


        });
    }


    private void beginMsgListener() {
        addBehaviour(new CyclicBehaviour(this) {
            private static final long serialVersionUID = 1L;

            @Override
            public void action() {
                ACLMessage msg = myAgent.receive();

                if (msg == null) {
                    return;
                }

                if (msg.getPerformative() == Message.REQUEST) {
                    sendMyInfoToAgent(msg);
                } else if (msg.getPerformative() == Message.INFORM) {
                    try {
                        if (state == WAITING_4_TEAM_RESPONSES && msg.getContentObject() instanceof ExplorationResponse) {

                            wentExploringSoldiers.remove(msg.getSender());
                            getMyViewMap().addViewMap(((ExplorationResponse) msg.getContentObject()).getViewMap());

                            if (captainMove) {
                                if (coosToExplore.size() > 0) {
                                    Pair<Integer, Integer> newPos = coosToExplore.pop();
                                    updatePosition(newPos);
                                    getMyViewMap().addViewRange(newPos, Model.getForest(), getVision_range());
                                }else{ //chegou ao novo destino
                                    captainMove = false;
                                }
                            }

                            if (wentExploringSoldiers.size() == 0 && !captainMove ) {
                                state = GIVING_ORDERS;
                            }
                        }

                        /*if (msg.getContentObject() instanceof InformViewMap) {
                            getMyViewMap().addViewMap(((InformViewMap) msg.getContentObject()).getViewMap());
                        }*/
                    } catch (UnreadableException e) {
                        e.printStackTrace();
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
                // ArrayList<AID> allCaptains = getAllCaptains();

                //todo desenvolver isto
                state = GIVING_ORDERS;
                break;

            case GIVING_ORDERS:

                wentExploringSoldiers = new ArrayList<>();

                ArrayList<Pair<Integer, Integer>> coosToExplore = myViewMap.coosToExplore(getModel_link().getMyCoos()
                        , getRadio_range());

                if (coosToExplore.size() == 0) {
                    coosToExplore = new ArrayList<>();
                    coosToExplore.add(myViewMap.coosToExplore(getModel_link().getMyCoos(), 99).get(0));
                    captainMove = true;
                    coosToExplore = myViewMap.getPath(getModel_link().getMyCoos(), coosToExplore.get(0));

                    for (int i = 0; i < teamSoldiers.size(); i++) {
                        sendOrderToExplore(coosToExplore, i);
                    }

                } else { //regroup on other area to explore
               /* for (int i = 0; i < coosToExplore.size(); i++) { //TODO!!
                    System.out.println("Pos: " + coosToExplore.get(i).getKey() + " " + coosToExplore.get(i).getValue
                            ());
                }*/
                    for (int i = 0; i < teamSoldiers.size() && i < coosToExplore.size(); i++) {
                        sendOrderToExplore(coosToExplore, i);
                    }
                }
                state = WAITING_4_TEAM_RESPONSES;
                break;

            case WAITING_4_TEAM_RESPONSES:

                //commWithAgents(onRangeAgents, robotsOnRange, soldiersOnRange);

                break;

            case EXPLORING:


                break;
            /*case AT_EXIT:
                break; */
        }
    }

    private void sendOrderToExplore(ArrayList<Pair<Integer, Integer>> coosToExplore, int i) {
        try {
            OrderToExplore order = new OrderToExplore(coosToExplore.get(i));
            ACLMessage msg = new ACLMessage(Message.REQUEST);
            msg.setContentObject(order);
            msg.addReceiver(teamSoldiers.get(i));
            send(msg);
            wentExploringSoldiers.add(teamSoldiers.get(i));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<AID> getAllCaptains() {
        ArrayList<AID> captains = new ArrayList<>();

        for (Agent agent : getModel_link().getAgents_list()) {
            if (agent instanceof Captain && agent.getAID() != getAID()) {
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
