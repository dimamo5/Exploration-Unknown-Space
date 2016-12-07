package agent;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import javafx.util.Pair;
import message.InformViewMap;
import message.Message;
import message.RequestViewMap;
import sajas.core.behaviours.CyclicBehaviour;
import sajas.core.behaviours.TickerBehaviour;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by sergi on 16/10/2016.
 */
public class Captain extends Human {

    public int cellphone_range;

    private enum agent_state {FINDING_EXIT}

    private agent_state state = agent_state.FINDING_EXIT;


    public Captain(int vision_range, int radio_range, int cellphone_range) {
        super(vision_range, radio_range);
        this.cellphone_range = cellphone_range;

    }

    public int getCellphone_range() {
        return cellphone_range;
    }

    public void setCellphone_range(int cellphone_range) {
        this.cellphone_range = cellphone_range;
    }

    public void move() {

    }

    @Override
    protected void setup() {

        beginMsgListener();

        addBehaviour(new TickerBehaviour(this, 10000) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onTick() {
                update();
            }
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
                                System.out.println("Received response from robot. COOs:" + ((InformViewMap) msg
                                        .getContentObject()).getPosition());
                            }
                        } catch (UnreadableException e) {
                            e.printStackTrace();
                        }
                    }

                /*
                try {
                    if (msg != null)
                        System.out.println((String) msg.getContentObject());
                } catch (UnreadableException e) {
                    e.printStackTrace();
                }*/
                }
            }
        });
    }

    private void update() {

        switch (state) {
            case FINDING_EXIT:

                //TODO buscar robo mais proximo

                ArrayList<jade.core.AID> robots = getModel_link().getRobotsFromAgentList();

                System.out.println("Captain#" + getAID() + "  Requesting info from robots.");
                requestRobotForInfo(robots);


                /*for (ExplorerAgent agent : getModel_link().getAgents_list()) {
                    if (agent.getAID() != this.getAID()) {

                        requestForInfo(agent.getAID());

                        break;
                    }
                }*/

                break;
        }
    }

    private void requestRobotForInfo(ArrayList<AID> robots) {
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
            reply.setContentObject(new Message(pos));
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
