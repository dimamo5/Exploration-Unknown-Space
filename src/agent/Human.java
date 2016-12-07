package agent;

/**
 * Created by sergi on 16/10/2016.
 */
public class Human extends ExplorerAgent {

    private static final int DEFAULT_RADIO_RANGE = 10;

    private int radio_range = DEFAULT_RADIO_RANGE;

    public Human(int vision_range, int radio_range) {
        super(vision_range);
        this.radio_range = radio_range;
    }

    public int getRadio_range() {
        return radio_range;
    }

    public void setRadio_range(int radio_range) {
        this.radio_range = radio_range;
    }

    protected enum agent_state {FINDING_EXIT, AT_EXIT}
}
