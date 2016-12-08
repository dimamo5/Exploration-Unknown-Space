package agent;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import javafx.util.Pair;
import message.InformViewMap;
import message.Message;
import message.RequestViewMap;
import model.Model;
import model.map.AgentModel;
import model.map.MapElement;
import model.map.Obstacle;
import model.map.ViewMap;
import sajas.core.behaviours.CyclicBehaviour;

import java.io.IOException;
import java.util.*;

import static agent.Robot.Robot_state.OUT_OF_ENERGY;

/**
 * Created by sergi on 16/10/2016.
 */
public class Robot extends ExplorerAgent {

    private Robot_state state = Robot_state.MOVING; //BY default

    public enum Robot_state {MOVING, OUT_OF_ENERGY}

    private static final int DEFAULT_ENERGY = 15; //15 cells/turns by default
    private int energy = DEFAULT_ENERGY;

    private ArrayList<AID> outOfEnergyCommAgents;

    public Robot(int vision_range, int energy) {
        super(vision_range);
        this.energy = energy;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public void getInfo() {

    }

    @Override
    protected void setup() {
        beginMsgListener();

        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                tick++;

                if (tick % 100 == 0) { //TODO period nao estar hardcoded
                    update();
                }
            }

            private static final long serialVersionUID = 1L;


        });
    }


    //TODO CREATE beahviour class 4 this
    private void beginMsgListener() {
        addBehaviour(new CyclicBehaviour(this) {
            private static final long serialVersionUID = 1L;

            @Override
            public void action() {
                ACLMessage msg = myAgent.receive();

                try {
                    if (msg != null && msg.getPerformative() == Message.REQUEST && msg.getContentObject() instanceof
                            RequestViewMap) {

                        boolean send = true;
                        if (state == OUT_OF_ENERGY) {

                            if(outOfEnergyCommAgents == null)
                                outOfEnergyCommAgents = new ArrayList<>();

                            /*quando energy==0 -> guarda agentes com quem comunicou para evitar
                            responder com a mesma informação*/
                            if (!outOfEnergyCommAgents.contains(msg.getSender())) {
                                System.out.println("O AGENTE\n"+msg.getSender()+"\nNAO ESTA NA LISTA, VOU ENVIAR INFO");
                                outOfEnergyCommAgents.add(msg.getSender());
                            }
                            else
                                send = false;
                        }

                        if(send) {
                            System.out.println(getAID() + " sending info to " + msg.getSender());
                            sendMyInfoToAgent(msg);
                        }
                    }
                } catch (UnreadableException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void update() {

        switch (state) {
            case MOVING:
                moveRobot();   // --energy > 0 -> moverandomly..
                break;
            case OUT_OF_ENERGY:
                if (outOfEnergyCommAgents == null) {
                    outOfEnergyCommAgents = new ArrayList<>();
                }
                break;
        }
    }

    private void sendMyInfoToAgent(ACLMessage msg) {
        ACLMessage reply = msg.createReply();
        reply.setPerformative(Message.INFORM);

        try {
            Pair<Integer, Integer> pos = new Pair<>(getModel_link().getX(), getModel_link().getY());
            InformViewMap inform = new InformViewMap(pos, getMyViewMap());
            reply.setContentObject(inform);
        } catch (IOException e) {
            e.printStackTrace();
        }

        send(reply);
    }

    private void moveRobot() {

        if (--energy >= 0) {
            move_random();
        } else {
            state = OUT_OF_ENERGY;
        }
    }

}
