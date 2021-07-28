package timeTableOntology.elements;
import jade.content.Concept;
import jade.content.onto.annotations.Slot;

public class TutorialGroup implements Concept {
	private TimeTableSlot slot;
	private String tutorialID; // Module number (e.g. SET10101)
	private int tutNum; //Number of tutorial
	
	// Used by main
	public TutorialGroup(TimeTableSlot slot, String tutorialID, int tutNum) {
		this.slot = slot;
		this.tutorialID = tutorialID;
		this.setTutNum(tutNum);
	}

	//Mandatory constraints to ensure that these values have content
	@Slot (mandatory = true)
	public TimeTableSlot getTimeslot() {
		return slot;
	}
	
	@Slot (mandatory = true)
	public String getTutorialID() {
		return tutorialID;
	}	
	
	@Slot (mandatory = true)
	public int getTutNum() {
		return tutNum;
	}
	
	public void setTimeslot(TimeTableSlot slot) {
		this.slot = slot;
	}
	
	public void setTutorialID(String tutorialID) {
		this.tutorialID = tutorialID;
	}

	public void setTutNum(int tutNum) {
		this.tutNum = tutNum;
	}
	
	public TutorialGroup() {
	}
}