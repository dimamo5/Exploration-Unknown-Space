package model;

import model.map.Map;
import model.map.MapElement;
import model.map.Obstacle;
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

/**
 * Created by sergi on 12/11/2016.
 */
public class Model extends Repast3Launcher {

    private static final boolean BATCH_MODE = true;

    public DisplaySurface dsurf;
    public DisplaySurface dsurf2;
    private Object2DGrid forest_space;
    private Object2DGrid heat_map_space;
    private Object2DDisplay heat_map_display;

    private Schedule schedule;

    private ArrayList<MapElement> assets_list;
    public static long tick = 0;

    private ContainerController agentContainer;

    public Model() {
    }


    @Override
    public String[] getInitParam() {
        return new String[]{};
    }

    @Override
    public void begin() {
        super.begin();
        forest_space = new Object2DGrid(10 ,10);
        heat_map_space = new Object2DGrid(10, 10);

        createModel();
        createDisplay();
    }



    private void createDisplay(){

        Object2DDisplay display = new Object2DDisplay(forest_space);
        display.setObjectList(assets_list);

        //heat_map_display = new Object2DDisplay(heat_map_space);

       /* dsurf2.addDisplayableProbeable(heat_map_display,"ExplorerAgent View");
        addSimEventListener(dsurf2);
        //dsurf2.setBackground(Color.GREEN);
        dsurf2.setSize(400,50);
        dsurf2.setLocation(280,80);
        dsurf2.print();*/

        dsurf.addDisplayableProbeable(display,"ExplorerAgent Space");
        dsurf.setBackground(Color.LIGHT_GRAY);
        dsurf.setLocation(10,10);
        dsurf.display();

        /*dsurf.addDisplayableProbeable(getPrettyMap, "Agents Space");
        dsurf.setBackground(new Color(50,100,50));
        dsurf.setSize(100, 100);
        dsurf.setVisible(true);
        dsurf.getPrettyMap();
        dsurf.setVisible(true);*/
    }

    private void createModel(){
        assets_list = new ArrayList<>();

        Map forest = new Map(10,10);

        //forest.print(); //prints map on console

        //Map model
        forest_space = new Object2DGrid(forest.getWidth()-1,forest.getHeight()); //-1 ignores '\n'
        heat_map_space = new Object2DGrid(forest.getWidth()-1,forest.getHeight());


        //alter this to show paredes e assim ter em atenção os sizes
        //correr o Model.main para perceber


        for (int y = 0; y < forest_space.getSizeY(); y++) {
            for (int x = 0; x < forest_space.getSizeX(); x++) {

                char c = forest.getMap_in_array()[y][x];

                if (c != ' ' && c != '\n') {
                    Obstacle tree = new Obstacle(x,y,forest_space);
                    assets_list.add(tree);
                    forest_space.putObjectAt(x, y, tree);
                }
                else if(c!= '\n')
                {
                    //Node n = new Node(i,j);
                    //prevgraph.add(n);
                    //EmptySpace empty_space = new EmptySpace(i,j, forest_space);
                    //displayList.add(empty_space);
                }
                //ForestHeat fh = new ForestHeat(i,j);
                //HeatSpace.putObjectAt(i, j, fh);
            }
        }

        setMapImages();
    }

    @Override
    public void setup() {

        //change properties of gui

        schedule = new Schedule();
        if (dsurf != null) dsurf.dispose();
        dsurf = new DisplaySurface(this, "Forest Display");
        registerDisplaySurface("Forest Display", dsurf);

        if (dsurf2 != null) dsurf2.dispose();
        dsurf2 = new DisplaySurface(this, "Heat Display");
        registerDisplaySurface("Heat Display", dsurf2);
    }

    @Override
    public Schedule getSchedule() {
        return schedule;
    }

    @Override
    public String getName() {
        return "Exploration of Unknown Space -- SAJaS Repast3 Test";
    }


    private void buildSchedule() {
        //schedule.scheduleActionBeginning(0, new MainAction());
        schedule.scheduleActionBeginning (1, this, "step");
        schedule.scheduleActionAtInterval(1, dsurf, "updateDisplay", Schedule.LAST);
        schedule.scheduleActionAtInterval(1, dsurf2, "updateDisplay", Schedule.LAST);
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

    public void step () {
        tick++;
    }

    private void spread_agents( /*empty_spaces*/){
        //distribui agentes pelos espaços vazios
    }

    private void init_model(){

    }

    private void make_display(){
        //panel graphics + listeners
    }

    private void setMapImages(){
        try {
            Obstacle.setImage(ImageIO.read(new File("res/icons/tree.png")));

           /* MapExit.setImage(ImageIO.read(new File()));
            Captain.setImage(ImageIO.read(new File()));
            Soldier.setImage(ImageIO.read(new File()));
            Robot.setImage(ImageIO.read(new File()));*/
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void launchJADE() {  //TODO

    }


    public static void main(String args[]){

        boolean runMode = !BATCH_MODE;   // BATCH_MODE or !BATCH_MODE = GUI_MODE

        SimInit init = new SimInit();
        init.setNumRuns(1);   // works only in batch mode

        init.loadModel(new Model(), null, runMode);  //setting last param to true only displays surfaces
    }
}
