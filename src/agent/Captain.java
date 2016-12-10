package agent;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import javafx.util.Pair;
import message.*;
import model.Model;
import model.map.AgentModel;
import model.map.ViewMap;
import sajas.core.Agent;
import sajas.core.behaviours.CyclicBehaviour;
import sun.awt.windows.WEmbeddedFrame;
import utilities.Utilities;

import java.io.IOException;
import java.util.*;

import static agent.Human.agent_state.*;

/**
 * Created by sergi on 16/10/2016.
 */
public class Captain extends Human {

    public int cellphone_range;

    private agent_state state = INITIAL_COMM_WITH_CAPTAINS;
    private ArrayList<AID> teamSoldiers, wentExploringSoldiers;

    public ArrayList<Soldier> getTeamSoldiersObject() {
        return teamSoldiersObject;
    }

    public void setTeamSoldiersObject(ArrayList<Soldier> teamSoldiersObject) {
        this.teamSoldiersObject = teamSoldiersObject;
    }

    private ArrayList<Soldier> teamSoldiersObject;
    private boolean captainMove = false;
    private Stack<Pair<Integer, Integer>> coosToExplore;

    public Captain(int vision_range, int radio_range, int cellphone_range) {
        super(vision_range, radio_range);
        this.cellphone_range = cellphone_range;
        teamSoldiers = new ArrayList<>();
        teamSoldiersObject = new ArrayList<>();
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

    public void addSoldierToTeam(Soldier sol) {
        teamSoldiers.add(sol.getAID());
        teamSoldiersObject.add(sol);
    }

    @Override
    protected void setup() {

        beginMsgListener();

        addBehaviour(new CyclicBehaviour(this) {
            private static final long serialVersionUID = 1L;

            @Override
            public void action() {
                tick++;

                if (tick % 2 == 0) { //TODO destrolhar isto
                    System.out.println(getAID() + " state: " + state);
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

                            if (!wentExploringSoldiers.remove(msg.getSender()))
                                System.out.println("------------DEU          PEIDO----------------");

                            getMyViewMap().addViewMap(((ExplorationResponse) msg.getContentObject()).getViewMap());

                            if (wentExploringSoldiers.size() == 0 && !captainMove) {
                                System.out.println("CAPTAIN NOTIFY TEAM");
                                notifyTeam(new InformTeam(getModel_link().getMyCoos(), getMyViewMap()));
                                state = GIVING_ORDERS;
                            } else {
                                System.out.println("WENT_EXPLORING_NOT_EMPTY - SIZE " + wentExploringSoldiers.size() + " MOVING " + captainMove);
                            }
                        } else if (state == WAITING_4_TEAM_RESPONSES && !captainMove && wentExploringSoldiers.size() == 0) {
                            notifyTeam(new InformTeam(getModel_link().getMyCoos(), getMyViewMap()));
                            state = GIVING_ORDERS;
                        }
                        //response from commWithOnRangeAgents
                        else if (msg.getContentObject() instanceof InformViewMap) {
                            myViewMap.addViewMap(((InformViewMap) msg.getContentObject()).getViewMap());
                        }


                    } catch (UnreadableException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void notifyTeam(Message message) {
        ACLMessage msg = new ACLMessage(Message.INFORM);

        try {
            msg.setContentObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (AID id : teamSoldiers) {
            msg.addReceiver(id);
        }
        send(msg);
    }

    private void update() {

        ArrayList<ExplorerAgent> onRangeAgents = getModel_link().getOnRadioRangeAgents(getRadio_range());

        onRangeAgents.removeIf(ag -> teamSoldiers.indexOf(ag.getAID()) != -1 || ag.getAID() == this.getAID());

        for (Soldier sol : teamSoldiersObject) {
            onRangeAgents.remove(sol);
        }

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

                if (coosToExplore.size() == 0) { //regroup on other area to explore
                    coosToExplore = new ArrayList<>();
                    System.out.println("---------->>>>>>>>>>>>>>>>>>>>>>>>>>REGROUP");
                    //coosToExplore.add(myViewMap.coosToExplore(getModel_link().getMyCoos(), 999).get(0));  //to remove after this being stable

                    ArrayList<Pair<Integer, Integer>> tempCoos = myViewMap.coosToExplore(getModel_link().getMyCoos(), 999);

                    //get best option
                    coosToExplore.add(myViewMap.getRegroupSite(tempCoos, getModel_link().getMyCoos()));
                    coosToExplore = myViewMap.getPath(getModel_link().getMyCoos(), coosToExplore.get(0));

                    pushToStack(coosToExplore);
                    captainMove = true;

                    for (int i = 0; i < teamSoldiers.size(); i++) {
                        sendOrderToExplore(coosToExplore, i, 0);
                    }

                } else {
                    for (int i = 0; i < teamSoldiers.size() && i < coosToExplore.size(); i++) {
                        sendOrderToExplore(coosToExplore, i, i);
                    }
                }

                state = WAITING_4_TEAM_RESPONSES;
                break;

            case WAITING_4_TEAM_RESPONSES:

                if (captainMove) {
                    if (this.coosToExplore.size() > 0) {
                        Pair<Integer, Integer> newPos = this.coosToExplore.pop();
                        updatePosition(newPos);
                        getMyViewMap().addViewRange(newPos, Model.getForest(), getVision_range());

                        if (this.coosToExplore.size() == 0) {
                            captainMove = false;
                            if (wentExploringSoldiers.size() == 0) {
                                notifyTeam(new InformTeam(getModel_link().getMyCoos(), getMyViewMap()));
                                state = GIVING_ORDERS;
                            }
                        }
                    } else { //chegou ao novo destino
                        captainMove = false;
                        if (wentExploringSoldiers.size() == 0) {
                            notifyTeam(new InformTeam(getModel_link().getMyCoos(), getMyViewMap()));
                            state = GIVING_ORDERS;
                        }
                    }
                }

                commWithAgents(onRangeAgents);
                break;

            case AT_EXIT:
                break;
        }
    }

    private void pushToStack(ArrayList<Pair<Integer, Integer>> pathCoos) {
        //Collections.reverse(pathCoos);
        coosToExplore = new Stack<>();
        for (Pair<Integer, Integer> pathCoo : pathCoos) {
            coosToExplore.push(pathCoo);
        }
    }


    private void sendOrderToExplore(ArrayList<Pair<Integer, Integer>> coosToExplore, int soldierIndex, int cooIndex) {
        try {
            OrderToExplore order = new OrderToExplore(coosToExplore.get(cooIndex));
            ACLMessage msg = new ACLMessage(Message.REQUEST);
            msg.setContentObject(order);
            msg.addReceiver(teamSoldiers.get(soldierIndex));
            send(msg);
            wentExploringSoldiers.add(teamSoldiers.get(soldierIndex));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<AID> getAllCaptains() {
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
