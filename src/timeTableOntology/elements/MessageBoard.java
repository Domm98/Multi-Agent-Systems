package timeTableOntology.elements;

import jade.content.Concept;
import java.util.List;
import java.util.ArrayList;

public class MessageBoard implements Concept {
	
	private List<TutorialGroup> messageBoard = new ArrayList<TutorialGroup>();

	public List<TutorialGroup> getMessageBoard() {
		return messageBoard;
	}

	public void setMessageBoard(List<TutorialGroup> messageBoard) {
		this.messageBoard = messageBoard;
	}
	
	public void removeFromMessageBoard(int Remove) {
		messageBoard.remove(Remove);
	}
	
	public void MessageBoard() {
	}
}
