package model;

import uchicago.src.sim.engine.BasicAction;
import uchicago.src.sim.engine.Schedule;
import uchicago.src.sim.engine.SimInit;
import uchicago.src.sim.engine.SimModelImpl;
import uchicago.src.sim.util.SimUtilities;

/**
 * Created by sergi on 12/11/2016.
 */
public class Model extends SimModelImpl {

    public Model() {
    }

    @Override
    public String[] getInitParam() {
        return new String[0];
    }

    @Override
    public void begin() {

    }

    @Override
    public void setup() {

    }

    @Override
    public Schedule getSchedule() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }


    class MainAction extends BasicAction {

        public void execute() {
            // shuffle agents
            //SimUtilities.shuffle(agentList);

            // iterate through all agents
           /* for(int i = 0; i < agentList.size(); i++) {
                agentList.get(i).step();
            }*/
        }
    }

    private void spread_agents( /*empty_spaces*/){
        //distribui agentes pelos espaços vazios
    }

    private void init_model(){

    }

    private void make_display(){
        //panel graphics + listeners
    }


    public static void main(String args[]){

        /*SimInit init = new SimInit();
        init.loadModel(new Model(), null, false); */
    }
}
