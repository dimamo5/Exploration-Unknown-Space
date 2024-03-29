package agent;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import javafx.util.Pair;
import message.*;
import model.Model;
import sajas.core.Agent;
import sajas.core.behaviours.CyclicBehaviour;

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
    protected boolean exitNotified = false;
    protected boolean notifiedExitTeamMembers = false;

    public ArrayList<Soldier> getTeamSoldiersObject() {
        return teamSoldiersObject;
    }

    public void setTeamSoldiersObject(ArrayList<Soldier> teamSoldiersObject) {
        this.teamSoldiersObject = teamSoldiersObject;
    }

    private ArrayList<Soldier> teamSoldiersObject;
    private boolean captainMove = false;
    private Stack<Pair<Integer, Integer>> coosToExplore;

    public Captain(int vision_range, int radio_range) {
        super(vision_range, radio_range);

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

                if (tick % humanUpdTickPeriod == 0) { //TODO destrolhar isto
                   // System.out.println(getAID() + " state: " + state);
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
                } else if (msg.getPerformative() == Message.PROPAGATE) {
                    try {
                        //System.out.println(getAID() +"STATE :>" +state +"  RECEIVED EXIT INFORMATION");
                        exitCoords = ((PropagateExit) msg.getContentObject()).getPosition();
                        myViewMap.addViewMap(((PropagateExit) msg.getContentObject()).getvMap());

                        notifiedExitTeamMembers = false;
                        found_map_exit = true;
                        exitNotified = true;

                        //todo foi isto que adicionei
                        /*wentExploringSoldiers.clear();
                        state = GIVING_ORDERS;*/

                    } catch (UnreadableException e) {
                        e.printStackTrace();
                    }
                } else if (msg.getPerformative() == Message.INFORM) {
                    try {
                        if (state == WAITING_4_TEAM_RESPONSES && msg.getContentObject() instanceof ExplorationResponse) {
                            //System.out.println("RECEIVED EXPLO_RESPONSE FROM > " + msg.getSender());

                            wentExploringSoldiers.remove(msg.getSender());
                            getMyViewMap().addViewMap(((ExplorationResponse) msg.getContentObject()).getViewMap());

                            if (wentExploringSoldiers.size() == 0 && !captainMove) {
                                //System.out.println("CAPTAIN NOTIFY TEAM");
                                notifyTeam(new InformTeam(getModel_link().getMyCoos(), getMyViewMap()));
                                if (at_map_exit)
                                    state = AT_EXIT;
                                else
                                    state = GIVING_ORDERS;
                            } else {
                                //System.out.println("WENT_EXPLORING_NOT_EMPTY - SIZE " + wentExploringSoldiers.size() + " MOVING " + captainMove);
                            }
                        } else if (state == WAITING_4_TEAM_RESPONSES && !captainMove && wentExploringSoldiers.size() == 0) {
                            notifyTeam(new InformTeam(getModel_link().getMyCoos(), getMyViewMap()));
                            if (at_map_exit)
                                state = AT_EXIT;
                            else
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

    private void update() {

        switch (state) {

            case INITIAL_COMM_WITH_CAPTAINS:
                // ArrayList<AID> allCaptains = getAllCaptains();

                //todo desenvolver isto
                state = GIVING_ORDERS;
                break;

            case GIVING_ORDERS:
                if (at_map_exit && wentExploringSoldiers.size() == 0) {
                    state = AT_EXIT;
                }

                ArrayList<Pair<Integer, Integer>> coosToExplore;

                //if(found_map_exit && !notifiedExitTeamMembers)
                    //System.out.println(getAID() + "need to notify team members");


                if (found_map_exit) {
                    coosToExplore = myViewMap.getPath(getModel_link().getMyCoos(), exitCoords);
                    pushToStack(coosToExplore);

                    //notifica team members
                    coosToExplore = new ArrayList<>();
                    coosToExplore.add(exitCoords);
                    for (int i = 0; i < teamSoldiers.size(); i++) {
                        sendOrderToExplore(coosToExplore, i, 0, true);
                    }

                    if(!exitNotified) {
                        notifyCaptains(new PropagateExit(exitCoords, myViewMap));
                    }
                    notifiedExitTeamMembers = true;
                    captainMove = true;
                    state = WAITING_4_TEAM_RESPONSES;
                    break;
                } else if (getMyViewMap().isExitFound()) {

                    exitCoords = myViewMap.getExitCoords();
                    coosToExplore = myViewMap.getPath(getModel_link().getMyCoos(), exitCoords);
                    pushToStack(coosToExplore);

                    //notifica outros capitaes
                    notifyCaptains(new PropagateExit(exitCoords, myViewMap));

                    //notifica team members
                    coosToExplore = new ArrayList<>();
                    coosToExplore.add(exitCoords);
                    for (int i = 0; i < teamSoldiers.size(); i++) {
                        sendOrderToExplore(coosToExplore, i, 0, true);
                    }

                    notifiedExitTeamMembers = true;
                    found_map_exit = true;
                    captainMove = true;
                    state = WAITING_4_TEAM_RESPONSES;
                    break;
                }


                wentExploringSoldiers = new ArrayList<>();

                coosToExplore = myViewMap.coosToExplore(getModel_link().getMyCoos()
                        , getRadio_range());


                if (coosToExplore.size() == 0) { //regroup on other area to explore
                    coosToExplore = new ArrayList<>();
                    ArrayList<Pair<Integer, Integer>> tempCoos = myViewMap.coosToExplore(getModel_link().getMyCoos(), 999);

                    //get best option
                    coosToExplore.add(myViewMap.getRegroupSite(tempCoos, getModel_link().getMyCoos()));
                    coosToExplore = myViewMap.getPath(getModel_link().getMyCoos(), coosToExplore.get(0));

                    pushToStack(coosToExplore);
                    captainMove = true;

                    for (int i = 0; i < teamSoldiers.size(); i++) {
                        sendOrderToExplore(coosToExplore, i, 0, false);
                    }

                } else {
                    /*ArrayList<Pair<Integer,Integer>> soldCoos = new ArrayList<>();

                    for(Soldier sold : teamSoldiersObject){
                        soldCoos.add(sold.getModel_link().getMyCoos());
                    }

                    ArrayList<Pair<Integer,Integer>> assignCoos = myViewMap.closestPoints(soldCoos,coosToExplore);

                    for (int i = 0; i < assignCoos.size() ; i++) {
                        sendOrderToExplore(assignCoos, i, i, false);
                    }*/

                    for (int i = 0; i < teamSoldiers.size() && i < coosToExplore.size(); i++) {
                        sendOrderToExplore(coosToExplore, i, i, false);
                    }
                }

                state = WAITING_4_TEAM_RESPONSES;
                break;

            case WAITING_4_TEAM_RESPONSES:
                //System.out.println("AT MAP EXIT >>> " + at_map_exit);

                if(found_map_exit && !notifiedExitTeamMembers){
                    state = GIVING_ORDERS;
                    break;
                }

                if (captainMove) {
                    if (this.coosToExplore.size() > 0) {
                        Pair<Integer, Integer> newPos = this.coosToExplore.pop();
                        updatePosition(newPos);
                        getMyViewMap().addViewRange(newPos, Model.getForest(), getVision_range());

                        if (this.coosToExplore.size() == 0) {
                            captainMove = false;

                            if (found_map_exit)
                                at_map_exit = true;

                            if (wentExploringSoldiers.size() == 0) {
                                if (found_map_exit) {
                                    at_map_exit = true;
                                    state = AT_EXIT;
                                } else {
                                    notifyTeam(new InformTeam(getModel_link().getMyCoos(), getMyViewMap()));
                                    state = GIVING_ORDERS;
                                }
                            }
                        }
                    } else { //chegou ao novo destino
                        captainMove = false;
                        if (found_map_exit)
                            at_map_exit = true;

                        if (wentExploringSoldiers.size() == 0) {
                            if (found_map_exit) {
                                at_map_exit = true;
                                state = AT_EXIT;
                                break;
                            } else {
                                notifyTeam(new InformTeam(getModel_link().getMyCoos(), getMyViewMap()));
                                state = GIVING_ORDERS;
                            }
                        }
                    }
                } else {
                    if (wentExploringSoldiers.size() == 0) {
                        notifyTeam(new InformTeam(getModel_link().getMyCoos(), getMyViewMap()));
                        state = GIVING_ORDERS;
                    }
                }
                requestInfoFromAgents();
                break;

            case AT_EXIT:
                break;
        }
    }

    private void requestInfoFromAgents() {
        ArrayList<ExplorerAgent> onRangeAgents = getModel_link().getOnRadioRangeAgents(getRadio_range());

        onRangeAgents.removeIf(ag -> teamSoldiers.indexOf(ag.getAID()) != -1 || ag.getAID() == this.getAID());

        for (Soldier sol : teamSoldiersObject) {
            onRangeAgents.remove(sol);
        }

        commWithAgents(onRangeAgents);
    }

    private void pushToStack(ArrayList<Pair<Integer, Integer>> pathCoos) {
        //Collections.reverse(pathCoos);
        coosToExplore = new Stack<>();
        for (Pair<Integer, Integer> pathCoo : pathCoos) {
            coosToExplore.push(pathCoo);
        }
    }

    private void sendOrderToExplore(ArrayList<Pair<Integer, Integer>> coosToExplore, int soldierIndex, int cooIndex, boolean goToExit) {
        try {
            OrderToExplore order = new OrderToExplore(coosToExplore.get(cooIndex), goToExit);
            ACLMessage msg = new ACLMessage(Message.REQUEST);
            msg.setContentObject(order);
            msg.addReceiver(teamSoldiers.get(soldierIndex));
            send(msg);

            if(found_map_exit){
                //System.out.println(getAID() + "  SENT ORDER TO GO EXIT TO >> " + teamSoldiers.get(soldierIndex));
            }

            wentExploringSoldiers.add(teamSoldiers.get(soldierIndex));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void notifyCaptains(Message message) {
        ACLMessage msg = new ACLMessage(Message.PROPAGATE);

        try {
            msg.setContentObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<AID> captains = getAllCaptains();

        for (AID id : captains) {
            if (id != this.getAID()) {
                msg.addReceiver(id);
                //System.out.println(getAID() + " SENDING EXIT INFORM TO " + id);
            }
        }
        send(msg);
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

    public ArrayList<AID> getAllCaptains() {
        ArrayList<AID> captains = new ArrayList<>();

        for (Agent agent : getModel_link().getAgents_list()) {
            if (agent instanceof Captain && agent.getAID() != getAID()) {
                captains.add(agent.getAID());
            }
        }

        return captains;
    }
}
