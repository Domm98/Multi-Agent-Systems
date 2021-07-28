package timeTableOntology.elements;

import jade.content.AgentAction;
import jade.core.AID;

// Slots to be given to and advertised on the message board concept
public class SwapSlot implements AgentAction {

	private TutorialGroup tutorial;
	private AID sentByAgent;
	
	public TutorialGroup getTutorial() {
		return tutorial;
	}
	public void setTutorial(TutorialGroup tutorial) {
		this.tutorial = tutorial;
	}
	public AID getSentByAgent() {
		return sentByAgent;
	}
	public void setSentByAgent(AID sentByAgent) {
		this.sentByAgent = sentByAgent;
	}
}