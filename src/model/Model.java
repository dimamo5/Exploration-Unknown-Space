package model;

import agent.Captain;
import agent.ExplorerAgent;
import agent.Robot;
import agent.Soldier;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.StaleProxyException;
import model.map.AgentModel;
import model.map.Map;
import model.map.MapExit;
import model.map.Obstacle;
import sajas.core.Runtime;
import sajas.sim.repast3.Repast3Launcher;
import sajas.wrapper.ContainerController;
import uchicago.src.sim.engine.BasicAction;
import uchicago.src.sim.engine.Schedule;
import uchicago.src.sim.engine.SimInit;
import uchicago.src.sim.gui.DisplaySurface;
import uchicago.src.sim.gui.Object2DDisplay;
import uchicago.src.sim.space.Object2DGrid;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by sergi on 12/11/2016.
 */
public class Model extends Repast3Launcher {

    private static final boolean BATCH_MODE = true;
    private static int NUM_CAP = 2;
    private static int NUM_SOL = 2;
    private static int NUM_ROBOT = 3;

    public DisplaySurface dsurf;
    public DisplaySurface dsurf2;
    private Object2DGrid forest_space;
    private Object2DGrid heat_map_space;
    private Object2DDisplay heat_map_display;

    private ContainerController agentContainer;

    private ArrayList<Object> display_list;
    public static long tick = 0;
    private int numCap = NUM_CAP;
    private int numSol = NUM_SOL;
    private int numRobot = NUM_ROBOT;

    private ArrayList<ExplorerAgent> agents_list;
    private Map forest;

    public Model() {
        super();
    }

    @Override
    public String[] getInitParam() {
        return new String[]{"numCap", "numRobot", "numSol"};
    }

    @Override
    public void begin() {
        createModel();
        createDisplay();
        buildSchedule();
        super.begin();
    }

    private void createModel() {

        display_list = new ArrayList<>();

        this.forest = new Map(15, 15);

        forest.print(); //prints map on console

        //Map model
        forest_space = new Object2DGrid(forest.getWidth(), forest.getHeight());
        heat_map_space = new Object2DGrid(forest.getWidth(), forest.getHeight());


        for (int y = 0; y < forest_space.getSizeY(); y++) {
            for (int x = 0; x < forest_space.getSizeX(); x++) {

                if (forest.getMap_in_array()[y][x] == 1) {
                    Obstacle tree = new Obstacle(x, y, forest_space);
                    try {
                        tree.setImage(ImageIO.read(new File("res/icons/tree.png")));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    display_list.add(tree);
                    forest_space.putObjectAt(x, y, tree);

                } else if (forest.getMap_in_array()[y][x] == 2) {
                    MapExit exit = new MapExit(x, y, forest_space);
                    try {
                        exit.setImage(ImageIO.read(new File("res/icons/door.png")));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    display_list.add(exit);
                    forest_space.putObjectAt(x, y, exit);
                } else if (forest.getMap_in_array()[y][x] == 0) {

                }
                //ForestHeat fh = new ForestHeat(i,j);
                //HeatSpace.putObjectAt(i, j, fh);
            }
        }
    }

    private void createDisplay() {
        Object2DDisplay display = new Object2DDisplay(forest_space);
        display.setObjectList(display_list);
        //heat_map_display = new Object2DDisplay(heat_map_space);

        /*dsurf2.addDisplayableProbeable(heat_map_display,"ExplorerAgent View");
        addSimEventListener(dsurf2);
        //dsurf2.setBackground(Color.GREEN);
        dsurf2.setSize(400,50);
        dsurf2.setLocation(280,80);
        //dsurf2.print();*/

        dsurf.addDisplayableProbeable(display, "ExplorerAgent Space");
        dsurf.setBackground(Color.LIGHT_GRAY);
        dsurf.setLocation(10, 10);
        dsurf.display();
    }

    private void buildSchedule() {
        getSchedule().scheduleActionAtInterval(1, dsurf, "updateDisplay", Schedule.LAST);
    }

    @Override
    public void setup() {
        super.setup();
        //change properties of gui

        if (dsurf != null) dsurf.dispose();
        dsurf = new DisplaySurface(this, "Forest Display");
        registerDisplaySurface("Forest Display", dsurf);

       /* if (dsurf2 != null) dsurf2.dispose();
        dsurf2 = new DisplaySurface(this, "Heat Display");
        registerDisplaySurface("Heat Display", dsurf2);*/
    }



    @Override
    public String getName() {
        return "Exploration of Unknown Space -- SAJaS Repast3 Test";
    }

    public int getNumCap() {
        return numCap;
    }

    public void setNumCap(int numCap) {
        this.numCap = numCap;
    }

    public int getNumSol() {
        return numSol;
    }

    public void setNumSol(int numSol) {
        this.numSol = numSol;
    }

    public int getNumRobot() {
        return numRobot;
    }

    public void setNumRobot(int numRobot) {
        this.numRobot = numRobot;
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

    private void spread_agents( /*empty_spaces*/) {
        //distribui agentes pelos espaços vazios
    }

    private void init_model() {

    }

    private void make_display() {
        //panel graphics + listeners
    }


    @Override
    protected void launchJADE() {
        Runtime rt = Runtime.instance();
        Profile p1 = new ProfileImpl();

        agentContainer = rt.createMainContainer(p1);

        launchAgents();
    }

    private void launchAgents() {

        agents_list = new ArrayList<>();

        //Gerar Capitães
        ArrayList<int[]> capitains = forest.createCapitainsPosition(numCap, 15);


        for (int i = 0; i < capitains.size(); i++) {

            Captain cap = new Captain(5 + i, 5, 5, 5);
            cap.setModel_link(new AgentModel(capitains.get(i)[0],
                    capitains.get(i)[1],
                    forest_space,
                    AgentModel.agent_type.CAPTAIN,
                    agents_list));


            try {
                agentContainer.acceptNewAgent("Captain #" + i, cap).start();
            } catch (StaleProxyException e) {
                e.printStackTrace();
            }
            agents_list.add(cap);

            display_list.add(cap.getModel_link());
            forest_space.putObjectAt(capitains.get(i)[0], capitains.get(i)[1], cap.getModel_link());
        }

        for (int i = 0; i < capitains.size(); i++) {

            ArrayList<int[]> soldiers = forest.createSoldiersPosition(capitains.get(i), numSol, 5);

            //Gerar Soldados
            for (int j = 0; j < soldiers.size(); j++) {
                Soldier sol = new Soldier(5 + j, 5, 5);
                sol.setModel_link(new AgentModel(soldiers.get(j)[0],
                        soldiers.get(j)[1],
                        forest_space,
                        AgentModel.agent_type.SOLDIER,
                        agents_list));

                try {
                    agentContainer.acceptNewAgent("Soldier #" + j, sol).start();
                } catch (StaleProxyException e) {
                    e.printStackTrace();
                }
                agents_list.add(sol);

                display_list.add(sol.getModel_link());
                forest_space.putObjectAt(soldiers.get(j)[0], soldiers.get(j)[1], sol.getModel_link());
            }
        }


        ArrayList<int[]> robots = forest.createRobotsPosition(numRobot);

        //Gerar Robot
        for (int i = 0; i < robots.size(); i++) {
            Robot robot = new Robot(5 + i, 5, 5);
            robot.setModel_link(new AgentModel(robots.get(i)[0],
                    robots.get(i)[1],
                    forest_space,
                    AgentModel.agent_type.ROBOT,
                    agents_list));

            try {
                agentContainer.acceptNewAgent("Robot #" + i, robot).start();
            } catch (StaleProxyException e) {
                e.printStackTrace();
            }
            agents_list.add(robot);

            display_list.add(robot.getModel_link());
            forest_space.putObjectAt(robots.get(i)[0], robots.get(i)[1], robot.getModel_link());
        }
    }


    public static void main(String args[]) {

        boolean runMode = !BATCH_MODE;   // BATCH_MODE or !BATCH_MODE = GUI_MODE

        SimInit init = new SimInit();
        //init.setNumRuns(1);   // works only in batch mode

        init.loadModel(new Model(), null, runMode);  //setting last param to true only displays surfaces
    }
}
