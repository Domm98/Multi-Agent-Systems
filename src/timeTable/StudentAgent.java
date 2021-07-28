package timeTable;

import java.util.ArrayList;
import java.util.Arrays;
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
import timeTable.TimeTableAgent.contactStudents;
import timeTableOntology.TimeTableOntology;
import timeTableOntology.elements.TimeTableSlot;
import timeTableOntology.elements.TutorialGroup;
import timeTableOntology.elements.sendMessageBoard;
import timeTableOntology.elements.MessageBoard;
import timeTableOntology.elements.SwapSlot;

public class StudentAgent extends Agent {
	private Codec codec = new SLCodec();
	private Ontology ontology = TimeTableOntology.getInstance();
	private AID timeTableAID;
	private int[][] slotPreferences;
	private List<String> HappyWith = new ArrayList<String>();
	private List<sendMessageBoard> messageBoard = new ArrayList<sendMessageBoard>();
	private List<SwapSlot> listOfAvailableSlots = new ArrayList<SwapSlot>();
	private List<SwapSlot> desiredTutorial = new ArrayList<SwapSlot>();
	private TutorialGroup[] tutorials;
	private SwapSlot unwantedSlot;
	private int utility;
	
	protected void setup() { // Initialise agent
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(ontology);
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("student");
		sd.setName(getLocalName() + "-student-agent");
		dfd.addServices(sd);
		// Register in yellow pages
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			e.printStackTrace();
		}

		// Get argument data from main
		Object[] arguments = getArguments();
		slotPreferences = (int[][]) arguments[0];
		tutorials = (TutorialGroup[]) arguments[1];

		// Generate preferences of each student randomly
		int Min = 0, Max = 2;
		slotPreferences[0][0] = Min + (int) (Math.random() * ((Max - Min) + 1));
		slotPreferences[2][2] = Min + (int) (Math.random() * ((Max - Min) + 1));
		slotPreferences[4][6] = Min + (int) (Math.random() * ((Max - Min) + 1));
		//System.out.println("Student has chosen the following slot prefs " + Arrays.deepToString(slotPreferences));

		if (slotPreferences[0][0] == 2) {
			HappyWith.add("Student happy");
		}
		if (slotPreferences[2][2] == 2) {
			HappyWith.add("Student happy");
		}
		
		doWait(4000);

		// First thing student should do is add unwanted tutorial slots to message board
		addBehaviour(new advertiseSlots(this));
		addBehaviour(new listenForMessageBoard());
	}

	public class advertiseSlots extends OneShotBehaviour {
		public advertiseSlots(Agent a) {
			super(a);
		}

		public void action() {
			DFAgentDescription timetableTemplate = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			sd.setType("timetable-agent");
			timetableTemplate.addServices(sd);
			try {
				DFAgentDescription[] agentsType1 = DFService.search(myAgent, timetableTemplate);
				for (int i = 0; i < agentsType1.length; i++) { // Store the AID of the timetable agent
					timeTableAID = agentsType1[i].getName();
				}
			} catch (FIPAException e) {
				e.printStackTrace();
			}
			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			msg.addReceiver(timeTableAID);
			msg.setConversationId("swap");
			msg.setLanguage(codec.getName());
			msg.setOntology(ontology.getName());

			// Add the tutorial which the student would like to swap
			SwapSlot swapThis = new SwapSlot();
			swapThis.setTutorial(tutorials[0]);
			swapThis.setSentByAgent(myAgent.getAID());

			// Action wrapper to be FIPA compliant
			Action action = new Action();
			action.setAction(swapThis);
			action.setActor(myAgent.getAID());
			try { // Send the message to the timetable agent
				getContentManager().fillContent(msg, action);
				send(msg);
			} catch (CodecException ce) {
				ce.printStackTrace();
			} catch (OntologyException oe) {
				oe.printStackTrace();
			}
		}
	}

	// Waiting for the student agent to send over tutorials it doesn't want
	public class listenForMessageBoard extends CyclicBehaviour {
		public void action() {
			ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
			if (msg != null) {
				try { // We have received a message
					ContentElement ce = null;
					ce = myAgent.getContentManager().extractContent(msg);
					if (ce instanceof Action) {
						Concept action = ((Action) ce).getAction();
						if (action instanceof SwapSlot) { // Parse the message and add the details to list
							SwapSlot mb = (SwapSlot) action;
							listOfAvailableSlots.add(mb);
							for (int i = 0; i < listOfAvailableSlots.size(); i++) {
								// Loop through available slot and store in a list
								SwapSlot tempSlot = new SwapSlot();
								tempSlot.setTutorial(listOfAvailableSlots.get(0).getTutorial());
								tempSlot.setSentByAgent(listOfAvailableSlots.get(0).getSentByAgent());
								// Set the day and time of the current tutorial being considered
								int tempDay = tempSlot.getTutorial().getTimeslot().getDay();
								int tempTime = tempSlot.getTutorial().getTimeslot().getTime();
								if (slotPreferences[tempDay][tempTime] != 0) { // Add as a target if matches preference
									desiredTutorial.add(tempSlot);
									tutorials[0] = desiredTutorial.get(0).getTutorial();
									System.out.println("Agent " + myAgent.getLocalName() + " Has swapped tutorial. " + "Agent ended with: " + slotPreferences[tutorials[0].getTimeslot().getDay()][tutorials[0].getTimeslot().getTime()] + " happiness" );
									utility = slotPreferences[tutorials[0].getTimeslot().getDay()][tutorials[0].getTimeslot().getTime()];
									myAgent.doDelete(); //Leave the system
									//addBehaviour(new gatherSwapRequests());
								} else {
									System.out.println(
											"Agent " + myAgent.getLocalName() + " Does not want the tutorials offered");
									utility = slotPreferences[tutorials[0].getTimeslot().getDay()][tutorials[0].getTimeslot().getTime()];
									// None of these tutorial in the message board were good
								}
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
	
	//Behaviour to swap, not added into the student agent as it currently throws errors.
	public class gatherSwapRequests extends CyclicBehaviour {
		int step = 0;
		AID swapWith;
		SwapSlot proposedSlot = new SwapSlot();
		MessageTemplate mt;

		public void action() {
			switch (step) {
			case 0:
				ACLMessage msg = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.PROPOSE));
				if (msg != null) {
					try { // Recieve a message with a proposal for swap
						ContentElement ce = null;
						ce = myAgent.getContentManager().extractContent(msg);
						if (ce instanceof Action) {
							Concept action = ((Action) ce).getAction();
							if (action instanceof SwapSlot) { // Parse the tutorial they want to swap
								proposedSlot = (SwapSlot) action;
								TimeTableSlot proposedTime = proposedSlot.getTutorial().getTimeslot();
								int tempDay = proposedTime.getDay();
								int tempTime = proposedTime.getTime();
								if (slotPreferences[tempDay][tempTime] == 2) { // If happy then swap
									step = 1;
									swapWith = proposedSlot.getSentByAgent();
									break;
								}
							} else {
								break;
							}
						}
					} catch (CodecException ce) {
						ce.printStackTrace();
					} catch (OntologyException oe) {
						oe.printStackTrace();
					}
				} else {
					block();
				}
				break;
			case 1:
				ACLMessage msg2 = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
				msg2.addReceiver(swapWith);
				msg2.setConversationId("swapProposal");
				msg2.setLanguage(codec.getName());
				msg2.setOntology(ontology.getName());
				myAgent.send(msg2);
				// Prepare the template to get the swap proposal reply
				mt = MessageTemplate.and(MessageTemplate.MatchConversationId("swapProposal"),
						MessageTemplate.MatchInReplyTo(msg2.getReplyWith()));
				step = 2;
				break;
			case 2:
				// Receive the purchase order reply
				ACLMessage reply = myAgent.receive(mt);
				if (reply != null) {
					// Swap response received
					if (reply.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
						// Swap successful, update tutorial
						tutorials[0] = desiredTutorial.get(0).getTutorial();
						desiredTutorial.remove(0);
						System.out.println("Swapped with agent  " + reply.getSender().getName());
						myAgent.doDelete();
					} else {
						System.out.println("Swap failed");
					}
					step = 3;
				} else {
					block();
				}
				break;
			}
		}
	}

	@Override
	protected void takeDown() {
		// De-register from yellow pages
		try {
			DFService.deregister(this);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
	}
}