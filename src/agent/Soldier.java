package agent;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import javafx.util.Pair;
import message.InformViewMap;
import message.Message;
import message.RequestViewMap;
import model.map.ViewMap;
import sajas.core.behaviours.Behaviour;
import sajas.core.behaviours.CyclicBehaviour;
import sajas.core.behaviours.TickerBehaviour;
import uchicago.src.sim.space.Object2DGrid;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by sergi on 16/10/2016.
 */
public class Soldier extends Human {

    private agent_state state = agent_state.FINDING_EXIT;

    public Soldier(int vision_range, int radio_range) {
        super(vision_range, radio_range);
    }

    @Override
    protected void takeDown() {
        super.takeDown();
    }

    @Override
    protected void setup() {
        /*beginMsgListener();

        addBehaviour(new TickerBehaviour(this, 1000) { //TODO period nao estar hardcoded
            private static final long serialVersionUID = 1L;

            @Override
            protected void onTick() {
                update();
            }
        });*/
    }


    //TODO CREATE beahviour class 4 this
    private void beginMsgListener() {
        addBehaviour(new CyclicBehaviour(this) {
            private static final long serialVersionUID = 1L;

            @Override
            public void action() {
                ACLMessage msg = myAgent.receive();

                try {
                    if (msg != null && msg.getPerformative() == Message.REQUEST && msg.getContentObject() instanceof RequestViewMap) {
                        System.out.println("Robot#"+getAID()+"  sending viewmap");
                        sendMyInfoToAgent(msg);
                    }
                } catch (UnreadableException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void update() {

        switch (state) {
            case FINDING_EXIT:
                move();
                break;
            case AT_EXIT:
                break;
        }
    }


    public void move(){

    }

    private void sendMyInfoToAgent(ACLMessage msg) {
        ACLMessage reply = msg.createReply();
        reply.setPerformative(Message.INFORM);

        try {
            Pair<Integer, Integer> pos = new Pair<>(getModel_link().getX(), getModel_link().getY());
            InformViewMap inform = new InformViewMap(pos,new ViewMap(15)); //TODO get viewmap
            reply.setContentObject(inform);
        } catch (IOException e) {
            e.printStackTrace();
        }

        send(reply);
    }
}
