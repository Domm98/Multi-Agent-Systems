package timeTableOntology.elements;

import jade.content.AgentAction;

public class sendMessageBoard implements AgentAction{

	private MessageBoard messageBoard;

	public MessageBoard getMessageBoard() {
		return messageBoard;
	}

	public void setMessageBoard(MessageBoard messageBoard) {
		this.messageBoard = messageBoard;
	}
	
}
