package timeTableOntology.elements;
import jade.content.Concept;
import jade.content.onto.annotations.Slot;

//Give agents the knowledge that days and time exists in a timeslot

public class TimeTableSlot implements Concept {
	private int day;
	private int time;
	
	// Main constructor
	public TimeTableSlot(int day, int time) {
		this.day = day;
		this.time = time;
	}
	
	//Mandatory constraints to ensure that these values have content
	@Slot (mandatory = true)
	public int getDay() {
		return day;
	}
	@Slot (mandatory = true)
	public int getTime() {
		return time;
	}
	
	public void setDay(int day) {
		this.day = day;
	}
	
	public void setTime(int time) {
		this.time = time;
	}
	
	public TimeTableSlot() {
	}
}