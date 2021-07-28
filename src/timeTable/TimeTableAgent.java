package timeTable;

import java.util.ArrayList;
import java.util.List;

import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import timeTableOntology.TimeTableOntology;
import timeTableOntology.elements.MessageBoard;
import timeTableOntology.elements.SwapSlot;
import timeTableOntology.elements.TutorialGroup;
import timeTableOntology.elements.sendMessageBoard;

public class TimeTableAgent extends Agent {
	private Codec codec = new SLCodec();
	private Ontology ontology = TimeTableOntology.getInstance();

	private List<AID> studentsList = new ArrayList<AID>();
	private List<SwapSlot> unwantedTutorials = new ArrayList<SwapSlot>();

	protected void setup() { // Initialise agent
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(ontology);
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("timetable-agent");
		sd.setName(getLocalName() + "-timetable-agent");
		dfd.addServices(sd);
		// Register in yellow pages
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		// Begins by finding students in the system
		doWait(2000);
		addBehaviour(new FindStudentsBehaviour(this));
		addBehaviour(new listensForSwaps());
	}

	// Finds the student agents and adds them to an array list
	public class FindStudentsBehaviour extends OneShotBehaviour {
		public FindStudentsBehaviour(Agent a) {
			super(a);
		}

		public void action() {
			DFAgentDescription students = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			// We are looking for all student agents
			sd.setType("student");
			students.addServices(sd);
			try {
				DFAgentDescription[] student = DFService.search(myAgent, students);
				for (int i = 0; i < student.length; i++) {
					studentsList.add(student[i].getName());
				}
				// System.out.println(unhappyAgents);
			} catch (FIPAException e) {
				e.printStackTrace();
			}
		}
	}

	// Waiting for the student agent to send over tutorials it doesn't want
	public class listensForSwaps extends CyclicBehaviour {
		public void action() {
			ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
			if (msg != null) {
				try { // We have received a message
					ContentElement ce = null;
					ce = myAgent.getContentManager().extractContent(msg);
					if (ce instanceof Action) {
						Concept action = ((Action) ce).getAction();
						if (action instanceof SwapSlot) { // Parse the message and add the details to list
							SwapSlot swaps = (SwapSlot) action;
							unwantedTutorials.add(swaps);
							if (unwantedTutorials.size() == studentsList.size()) {
								addBehaviour(new contactStudents());
							}
						}
					}
				} catch (CodecException ce) {
					ce.printStackTrace();
				} catch (OntologyException oe) {
					oe.printStackTrace();
				}
			} else { // No more preferences to collect
				block();
			}
		}
	}

	public class contactStudents extends OneShotBehaviour {
		public void action() { // Every student and unwanted tutorial has been collected
			//System.out.println("Every student is now added");

			List<SwapSlot> tempUnwantedTuts = new ArrayList<SwapSlot>();
			tempUnwantedTuts = unwantedTutorials;
			tempUnwantedTuts.remove(0); //Remove first index as it will send student its own details
			do {
				// Iterate through every student ID, along with the tutorial they don't want
				for (int i = 0; i < tempUnwantedTuts.size(); i++) {
					AID tempID = tempUnwantedTuts.get(i).getSentByAgent();
					TutorialGroup tempTut = tempUnwantedTuts.get(i).getTutorial();
					
					ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
					msg.addReceiver(tempID);
					msg.setConversationId("SwapTutorials");
					msg.setLanguage(codec.getName());
					msg.setOntology(ontology.getName());

					// Set content to pass
					SwapSlot messageBoard = new SwapSlot();
					messageBoard.setTutorial(tempTut);
					messageBoard.setSentByAgent(tempID);
					tempUnwantedTuts.remove(i);
					
					ACLMessage msg2 = new ACLMessage(ACLMessage.INFORM);
					msg2.addReceiver(tempID);
					msg2.setConversationId("SwapTutorials");
					msg2.setLanguage(codec.getName());
					msg2.setOntology(ontology.getName());

					// Set content to pass
					SwapSlot messageBoard2 = new SwapSlot();
					messageBoard.setTutorial(tempTut);
					messageBoard.setSentByAgent(tempID);

					// Action wrapper to be FIPA compliant
					Action action = new Action();
					action.setAction(messageBoard);
					action.setActor(myAgent.getAID());
					try { // Send the message to the student agent
						getContentManager().fillContent(msg, action);
						send(msg);
					} catch (CodecException ce) {
						ce.printStackTrace();
					} catch (OntologyException oe) {
						oe.printStackTrace();
					}
					
					// Action wrapper to be FIPA compliant
					Action action2 = new Action();
					action2.setAction(messageBoard2);
					action2.setActor(myAgent.getAID());
					try { // Send the message to the student agent
						getContentManager().fillContent(msg, action);
						send(msg);
					} catch (CodecException ce) {
						ce.printStackTrace();
					} catch (OntologyException oe) {
						oe.printStackTrace();
					}
				}
			} while (!tempUnwantedTuts.isEmpty()); // While still more tutorials to send
		}
	}
}