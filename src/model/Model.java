package model;

import agent.Captain;
import agent.ExplorerAgent;
import agent.Robot;
import agent.Soldier;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.StaleProxyException;
import javafx.util.Pair;
import model.map.*;
import sajas.core.Runtime;
import sajas.sim.repast3.Repast3Launcher;
import sajas.wrapper.ContainerController;
import uchicago.src.sim.engine.Schedule;
import uchicago.src.sim.engine.SimInit;
import uchicago.src.sim.gui.DisplaySurface;
import uchicago.src.sim.gui.Object2DDisplay;
import uchicago.src.sim.space.Object2DGrid;
import utilities.Utilities;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by sergi on 12/11/2016.
 */
public class Model extends Repast3Launcher {

    private static final boolean BATCH_MODE = true;
    private static final int NUM_CAP = 5;
    private static final int NUM_SOL = 2;
    private static final int NUM_ROBOT = 5;
    private static final int ROBOT_ENERGY = 20;

    private static final int RADIO_RANGE = 5;
    private static final int VISION_RANGE_ROBOT = 10;
    private static final int VISION_RANGE = 5;
    private static final int MAP_SIZE = 15;
    public static final int HUMAN_UPD_TICK_PERIOD = 1;
    public static final int ROBOT_UPD_TICK_PERIOD = 5;


    public DisplaySurface dsurf;
    public DisplaySurface dsurf2;
    private Object2DGrid forest_space;
    private Object2DGrid heat_map_space;
    private Object2DDisplay heat_map_display;
    private JFrame agentFrame;
    protected JList agentList;

    private ContainerController agentContainer;

    private ArrayList<Object> display_list;
    public static long tick = 0;
    private int numCap = NUM_CAP;
    private int numSol = NUM_SOL;
    private int numRobot = NUM_ROBOT;
    private int energyRobot = ROBOT_ENERGY;

    private int radioRange = RADIO_RANGE;
    private int visionRangeRobot = VISION_RANGE_ROBOT;
    private int visionRange = VISION_RANGE;
    private int mapSize = MAP_SIZE;
    private boolean loadMap = false;
    public int humanUpdTickPeriod = HUMAN_UPD_TICK_PERIOD;

    public int getHumanUpdTickPeriod() {
        return humanUpdTickPeriod;
    }

    public void setHumanUpdTickPeriod(int humanUpdTickPeriod) {
        this.humanUpdTickPeriod = humanUpdTickPeriod;
    }

    public int getRobotUpdTickPeriod() {
        return robotUpdTickPeriod;
    }

    public void setRobotUpdTickPeriod(int robotUpdTickPeriod) {
        this.robotUpdTickPeriod = robotUpdTickPeriod;
    }

    public int robotUpdTickPeriod = ROBOT_UPD_TICK_PERIOD;

    private ArrayList<ExplorerAgent> agents_list;
    private static Map forest;

    public Model() {
        super();
    }

    @Override
    public String[] getInitParam() {
        return new String[]{"numCap", "numRobot", "numSol", "energyRobot", "radioRange", "visionRange",
                "visionRangeRobot", "mapSize", "loadMap", "humanUpdTickPeriod", "robotUpdTickPeriod"};
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
        if (this.loadMap)
            forest = Map.loadMap();
        else
            forest = new Map(this.getMapSize(), this.getMapSize());
        //Map.saveMap(forest);
        forest.print(); //prints globalMap on console

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

                HeatElement vm = new HeatElement(x, y);
                heat_map_space.putObjectAt(x, y, vm);
            }
        }
    }

    private void createDisplay() {
        Object2DDisplay display = new Object2DDisplay(forest_space);
        display.setObjectList(display_list);
        heat_map_display = new Object2DDisplay(heat_map_space);

        dsurf2.addDisplayableProbeable(heat_map_display, "ExplorerAgent View");
        dsurf2.setBackground(Color.LIGHT_GRAY);
        dsurf2.setLocation(800, 10);
        dsurf2.display();

        dsurf.addDisplayableProbeable(display, "ExplorerAgent Space");
        dsurf.setBackground(Color.LIGHT_GRAY);
        dsurf.setLocation(10, 10);
        dsurf.display();
    }

    private void buildSchedule() {
        getSchedule().scheduleActionBeginning(1, this, "step");
        getSchedule().scheduleActionAtInterval(1, dsurf, "updateDisplay", Schedule.LAST);
        getSchedule().scheduleActionAtInterval(1, dsurf2, "updateDisplay", Schedule.LAST);
    }

    public void step() {
        tick++;
    }


    @Override
    public void setup() {
        super.setup();
        //change properties of gui

        if (dsurf != null) dsurf.dispose();
        dsurf = new DisplaySurface(this, "Forest Display");
        registerDisplaySurface("Forest Display", dsurf);

        if (dsurf2 != null) dsurf2.dispose();
        dsurf2 = new DisplaySurface(this, "Heat Display");
        registerDisplaySurface("Heat Display", dsurf2);
    }


    public static Map getForest() {
        return forest;
    }

    public static void setForest(Map forest) {
        Model.forest = forest;
    }


    @Override
    public String getName() {
        return "";
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


    private void spread_agents( /*empty_spaces*/) {
        //distribui agentes pelos espaços vazios
    }

    private void init_model() {

    }

    private void make_display() {
        //panel graphics + listeners
    }

    public int getEnergyRobot() {
        return energyRobot;
    }

    public void setEnergyRobot(int energyRobot) {
        this.energyRobot = energyRobot;
    }

    public int getRadioRange() {
        return radioRange;
    }

    public void setRadioRange(int radioRange) {
        this.radioRange = radioRange;
    }

    public int getVisionRangeRobot() {
        return visionRangeRobot;
    }

    public void setVisionRangeRobot(int visionRangeRobot) {
        this.visionRangeRobot = visionRangeRobot;
    }

    public int getVisionRange() {
        return visionRange;
    }

    public void setVisionRange(int visionRange) {
        this.visionRange = visionRange;
    }

    public int getMapSize() {
        return mapSize;
    }

    public boolean isLoadMap() {
        return loadMap;
    }

    public void setMapSize(int mapSize) {
        this.mapSize = mapSize;
    }

    public void setLoadMap(boolean loadMap) {
        this.loadMap = loadMap;
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
        ArrayList<int[]> capitains = forest.createCapitainsPosition(numCap, mapSize / numCap);

        for (int i = 0; i < capitains.size(); i++) {
            ArrayList<int[]> soldiers = forest.createSoldiersPosition(capitains, capitains.get(i), numSol,
                    visionRange, mapSize / numCap);

            Captain cap = new Captain(visionRange, radioRange);

            AgentModel agModel = new AgentModel(soldiers.get(0)[0],
                    soldiers.get(0)[1],
                    forest_space,
                    AgentModel.agent_type.CAPTAIN,
                    agents_list);

            cap.setModel_link(agModel);

            cap.setMyViewMap(new ViewMap(Model.forest.getWidth()));
            cap.getMyViewMap().addViewRange(new Pair<>(agModel.getX(), agModel.getY()), Model.getForest(), cap
                    .getVision_range());
            cap.setHumanUpdTickPeriod(this.humanUpdTickPeriod);

            try {
                agentContainer.acceptNewAgent("Captain #" + i, cap).start();
            } catch (StaleProxyException e) {
                e.printStackTrace();
            }
            agents_list.add(cap);
            display_list.add(cap.getModel_link());

            //Gerar Soldados
            for (int j = 1; j < soldiers.size(); j++) {
                Soldier sol = new Soldier(visionRange, radioRange);

                agModel = new AgentModel(soldiers.get(j)[0],
                        soldiers.get(j)[1],
                        forest_space,
                        AgentModel.agent_type.SOLDIER,
                        agents_list);

                sol.setModel_link(agModel);
                sol.setMyViewMap(new ViewMap(Model.forest.getWidth()));
                sol.getMyViewMap().addViewRange(new Pair<>(agModel.getX(), agModel.getY()), Model.getForest(), sol
                        .getVision_range());
                sol.setHumanUpdTickPeriod(this.humanUpdTickPeriod);

                try {
                    agentContainer.acceptNewAgent("Soldier #" + (i * soldiers.size() + j), sol).start();
                    sol.setTeamLeader(cap.getAID());
                    cap.addSoldierToTeam(sol);
                } catch (StaleProxyException e) {
                    e.printStackTrace();
                }

                agents_list.add(sol);
                display_list.add(sol.getModel_link());
            }
        }

        for (ExplorerAgent ag : agents_list) {
            if (ag instanceof Captain) {
                ArrayList<Soldier> solds = ((Captain) ag).getTeamSoldiersObject();

                for (Soldier sol : solds) {
                    ArrayList<Soldier> team = new ArrayList<>(solds);
                    team.remove(sol);
                    sol.setTeamMembers(team);
                }
            }
        }

        ArrayList<int[]> robots = forest.createRobotsPosition(numRobot);

        //Gerar Robot
        for (int i = 0; i < robots.size(); i++) {
            Robot robot = new Robot(visionRangeRobot, energyRobot);

            AgentModel agModel = new AgentModel(robots.get(i)[0],
                    robots.get(i)[1],
                    forest_space,
                    AgentModel.agent_type.ROBOT,
                    agents_list);

            robot.setModel_link(agModel);
            robot.setMyViewMap(new ViewMap(Model.forest.getWidth()));
            robot.getMyViewMap().addViewRange(new Pair<>(agModel.getX(), agModel.getY()), Model.getForest(), robot
                    .getVision_range());

            robot.setRobotUpdTickPeriod(this.robotUpdTickPeriod);

            try {
                agentContainer.acceptNewAgent("Robot #" + i, robot).start();
            } catch (StaleProxyException e) {
                e.printStackTrace();
            }
            agents_list.add(robot);

            display_list.add(robot.getModel_link());
        }

        agentFrame = new JFrame("Select an Agent");
        ArrayList<String> names = new ArrayList<String>();

        for (int i = 0; i < agents_list.size(); i++) {
            names.add(agents_list.get(i).getName().substring(0, agents_list.get(i).getName().indexOf('@')));
        }
        agentList = new JList(names.toArray());
        agentFrame.add(agentList);
        agentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        agentFrame.setVisible(true);


        JScrollPane scroll = new JScrollPane(agentList);
        agentFrame.add(scroll);
        agentFrame.setSize(300, 350);
        agentFrame.setLocation(600, 10);
        agentFrame.setVisible(true);
        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = agentList.locationToIndex(e.getPoint());
                System.out.println("Showing map from agent #" + i);
                heat_map_display.setObjectList(Utilities.twoDArrayToList(agents_list.get(i).getMyViewMap().getMap()));
            }
        };
        agentList.addMouseListener(mouseListener);


    }


    public static void main(String args[]) {

        boolean runMode = !BATCH_MODE;   // BATCH_MODE or !BATCH_MODE = GUI_MODE

        SimInit init = new SimInit();
        //init.setNumRuns(1);   // works only in batch mode

        init.loadModel(new Model(), null, runMode);  //setting last param to true only displays surfaces
    }
}
