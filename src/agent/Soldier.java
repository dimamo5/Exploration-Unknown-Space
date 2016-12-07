package agent;

import sajas.core.behaviours.Behaviour;

/**
 * Created by sergi on 16/10/2016.
 */
public class Soldier extends Human {

    public Soldier(int vision_range, int radio_range) {
        super(vision_range, radio_range);
    }

    @Override
    protected void setup() {
        super.setup();
    }

    @Override
    protected void takeDown() {
        super.takeDown();
    }

    @Override
    public void addBehaviour(Behaviour b) {
        super.addBehaviour(b);
    }

    public void move(){

    }
}
