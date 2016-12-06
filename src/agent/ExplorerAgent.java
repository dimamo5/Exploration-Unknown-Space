package agent;

import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import model.map.AgentModel;
import sajas.core.Agent;
import jade.content.lang.Codec;
import jade.content.onto.Ontology;
import sajas.domain.DFService;
import sajas.proto.SubscriptionInitiator;


/**
 * Created by sergi on 16/10/2016.
 */
public class ExplorerAgent extends Agent {

    /*
    * Dir:
    * North- 0
    * West- 1
    * South- 2
    * East- 3
   * */

    private static final int DEFAULT_VISION_RANGE = 5;

    private int id, vision_range = DEFAULT_VISION_RANGE,    //cells count
            current_dir = -1, previous_dir = -1;            //no current/previous direction at the begining

    // TODO: 16/10/2016 add agent's own map

    private boolean at_map_exit = false, found_map_exit = false;
    private AgentModel model_link;

    public AgentModel getModel_link() {
        return model_link;
    }

    public void setModel_link(AgentModel model_link) {
        this.model_link = model_link;
    }


    //==================================== METHODS ====================================================//

    public ExplorerAgent(int id, int vision_range) {
        this.id = id;
        this.vision_range = vision_range;
    }


    public int getCurrent_dir() {
        return current_dir;
    }

    public void setCurrent_dir(int current_dir) {
        this.current_dir = current_dir;
    }

    public int getPrevious_dir() {
        return previous_dir;
    }

    public void setPrevious_dir(int previous_dir) {
        this.previous_dir = previous_dir;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVision_range() {
        return vision_range;
    }

    public void setVision_range(int vision_range) {
        this.vision_range = vision_range;
    }

    public boolean is_At_map_exit() {
        return at_map_exit;
    }

    public void setAt_map_exit(boolean at_map_exit) {
        this.at_map_exit = at_map_exit;
    }

    public boolean isAt_map_exit() {
        return at_map_exit;
    }

    public boolean isFound_map_exit() {
        return found_map_exit;
    }

    public void setFound_map_exit(boolean found_map_exit) {
        this.found_map_exit = found_map_exit;
    }


}
