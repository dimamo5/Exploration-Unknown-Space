package agent;

import com.bbn.openmap.omGraphics.grid.GridData;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import javafx.util.Pair;
import message.InformViewMap;
import message.Message;
import message.RequestViewMap;
import model.map.AgentModel;
import model.map.MapElement;
import model.map.Obstacle;
import model.map.ViewMap;
import sajas.core.behaviours.Behaviour;
import sajas.core.behaviours.CyclicBehaviour;
import sajas.core.behaviours.TickerBehaviour;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

/**
 * Created by sergi on 16/10/2016.
 */
public class Robot extends ExplorerAgent {

    private Robot_state state = Robot_state.MOVING; //BY default

    public enum Robot_state {MOVING, OUT_OF_ENERGY}

    private static final int DEFAULT_ENERGY = 15; //15 cells/turns by default
    private int energy = DEFAULT_ENERGY;


    public Robot(int id, int vision_range, int energy) {
        super(id, vision_range);
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

        addBehaviour(new TickerBehaviour(this, 1000) { //TODO period nao estar hardcoded
            private static final long serialVersionUID = 1L;

            @Override
            protected void onTick() {
                update();
            }
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
                    if (msg != null && msg.getPerformative() == Message.REQUEST && msg.getContentObject() instanceof RequestViewMap) {
                        System.out.println("Robot#"+getId()+"  sending viewmap");
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
            case MOVING:
                moveRobot();   // --energy > 0 -> moverandomly..
                break;
            case OUT_OF_ENERGY:
                break;
        }
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


    @Override
    protected void takeDown() {
        super.takeDown();
    }

    @Override
    public void addBehaviour(Behaviour b) {
        super.addBehaviour(b);
    }


    private void moveRobot() {

        //se modo ñ eficient estiver ligado:
        if(--energy > 0) {
            move_random();
        }
        //se nao
        //move_efficient
    }

    private void move_random() {

        ArrayList<ViewMap.DIR> dirs = AgentModel.getPossibleDir();

        Random r = new Random();
        int dir = r.nextInt(dirs.size());

        Pair<Integer,Integer> newPos= move(dirs.get(dir));
        getModel_link().move(newPos.getKey(), newPos.getValue());

        AgentModel.setGlobalMap();


        //update this.coords
        //update viewmap
    }

    private ArrayList<MapElement> get_neighbors_empty_spaces(Vector<MapElement> neighbors) {

        ArrayList<MapElement> empty_spaces = new ArrayList<>();

        for (MapElement neighbour : neighbors) {
            if (!(neighbour instanceof Obstacle)) {
                empty_spaces.add(neighbour);
            }
        }

        //TODO: REPETIDOS- podem haver objs na mesma posição e dar aso a que hajam: k empty_spaces, k = nº agentes no mesmo espaço
        return empty_spaces;
    }


}
