package onto;

import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;

/**
 * Created by sergi on 05/12/2016.
 */
public class ExplorationOntology extends BeanOntology{

    public static final String ONTOLOGY_NAME = "ontology"; //todo

    // Singleton instance of this ontology
    private static Ontology theInstance = new ExplorationOntology();

    // Method to access the singleton ontology object
    public static Ontology getInstance() {
        return theInstance;
    }

    // Private constructor
    private ExplorationOntology() {
        super(ONTOLOGY_NAME);

       /* try {
            // add all Concept, Predicate and AgentAction
            //add(classname.class);
        } catch(BeanOntologyException boe) {
            boe.printStackTrace();
        }*/
    }
}
