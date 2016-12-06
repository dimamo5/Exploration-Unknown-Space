package agent;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import sajas.core.AID;
import sajas.core.behaviours.Behaviour;
import sajas.core.behaviours.CyclicBehaviour;
import uchicago.src.sim.space.Object2DGrid;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Base64;

/**
 * Created by sergi on 16/10/2016.
 */
public class Captain extends Human {

    public int cellphone_range;

    private enum agent_state {FINDING_EXIT}

    private agent_state state = agent_state.FINDING_EXIT;

    public Captain(int id, int vision_range, int radio_range, int cellphone_range) {
        super(id, vision_range, radio_range);
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
        addBehaviour(new CyclicBehaviour(this) {
            private static final long serialVersionUID = 1L;

            @Override
            public void action() {
                update();
            }
        });

        beginMsgListener();
    }


    private void beginMsgListener() {
        addBehaviour(new CyclicBehaviour(this) {
            private static final long serialVersionUID = 1L;

            @Override
            public void action() {
                ACLMessage msg = myAgent.receive();

                try {
                    System.out.println((String)msg.getContentObject());
                } catch (UnreadableException e) {
                    e.printStackTrace();
                }

            }

        });
    }

    private void update() {

        switch (state) {
            case FINDING_EXIT:
                sendMyInfo();
                break;
        }

    }

    private void sendMyInfo() {

        try {
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);

            msg.setContentObject("OIX");

            for (ExplorerAgent agent : getModel_link().getAgents_list()) {
                if (agent.getAID() != this.getAID()) {
                    msg.addReceiver(agent.getAID());
                }
            }

            System.out.println("SENDING MSG");
            send(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
