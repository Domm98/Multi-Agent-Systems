package timeTableOntology;

import jade.content.onto.BeanOntology;
import jade.content.onto.BeanOntologyException;
import jade.content.onto.Ontology;

//This is taken from the workbook directly

// Use a singleton pattern to initialise our ontology
public class TimeTableOntology extends BeanOntology{
	
private static Ontology theInstance = new TimeTableOntology("my_ontology"); 
	
	public static Ontology getInstance(){
		return theInstance;
	}
	
	private TimeTableOntology(String name) { // Private constructor setter, Public getter
		super(name);
		try { //Add all of the ontological  elements into the ontology
			add("timeTableOntology.elements");
		} catch (BeanOntologyException e) {
			e.printStackTrace();
		}
	}
}