package timeTable;

import java.util.ArrayList;
import java.util.List;

import jade.core.*;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import timeTableOntology.elements.TimeTableSlot;
import timeTableOntology.elements.TutorialGroup;

public class Main {

	public static void main(String[] args) {
		Profile myProfile = new ProfileImpl();
		Runtime myRuntime = Runtime.instance();

		// To specify which test case we want to initiate
		int testCase = 3;
		// Passing tutorials and time slots to students
		Object[] passToStudent1 = new Object[2];
		Object[] passToStudent2 = new Object[2];

		// Lists to track students and tutorials
		List<String> Student = new ArrayList<>();
		List<TutorialGroup[]> Tutorials = new ArrayList<>();

		// First element in array is day (Mon-Fri)
		int[][] slotPreferences1 = new int[5][9];
		// Second element is time (0900-1700)
		int[][] slotPreferences2 = new int[5][9];

		if (testCase == 1) {
			// A tutorial group for SET010101 on the time slot of Monday morning
			TimeTableSlot slot1 = new TimeTableSlot(0, 0);
			TutorialGroup tutorialGroup1 = new TutorialGroup(slot1, "SET010101", 0);
			TutorialGroup[] group1 = new TutorialGroup[1];
			group1[0] = tutorialGroup1;
			Tutorials.add(group1);

			// A tutorial group for SET010101 on the time slot of Friday morning
			TimeTableSlot slot2 = new TimeTableSlot(2, 2);
			TutorialGroup tutorialGroup2 = new TutorialGroup(slot2, "SET010101", 1);
			TutorialGroup[] group2 = new TutorialGroup[1];
			group2[0] = tutorialGroup2;
			Tutorials.add(group2);

			// Set all timetable slots to preference 2 to start with, meaning happy
			for (int a = 0; a < slotPreferences1.length; a++) {
				for (int b = 0; b < slotPreferences1[0].length; b++) {
					slotPreferences1[a][b] = 2;
					slotPreferences2[a][b] = 2;
				}

				// Pass slot preferences and corresponding tutorial group to student agent
				passToStudent1[0] = slotPreferences1;
				passToStudent1[1] = group1;

				passToStudent2[0] = slotPreferences2;
				passToStudent2[1] = group2;
			}
			// Start the agents, specified amount is in the loop
			try {
				ContainerController myContainer = myRuntime.createMainContainer(myProfile);
				AgentController rma = myContainer.createNewAgent("rma", "jade.tools.rma.rma", null);
				rma.start();

				for (int i = 0; i < 5; i += 2) {
					AgentController student1 = myContainer.createNewAgent("student" + i,
							StudentAgent.class.getCanonicalName(), passToStudent1);
					student1.start();
					Student.add(student1.getName());

					AgentController student2 = myContainer.createNewAgent("student" + (i + 1),
							StudentAgent.class.getCanonicalName(), passToStudent2);
					student2.start();
					Student.add(student2.getName());
				}
				AgentController timetableAgent = myContainer.createNewAgent("timetableAgent",
						TimeTableAgent.class.getCanonicalName(), null);
				timetableAgent.start();
			} catch (Exception e) {
				System.out.println("Exception starting agent: " + e.toString());
			}
		}

		if (testCase == 2) {

			Object[] passToStudent3 = new Object[2];
			int[][] slotPreferences3 = new int[5][9];

			// A tutorial group for SET010101 on the time slot of Monday morning
			TimeTableSlot slot1 = new TimeTableSlot(0, 0);
			TutorialGroup tutorialGroup1 = new TutorialGroup(slot1, "SET010101", 0);
			TutorialGroup[] group1 = new TutorialGroup[1];
			group1[0] = tutorialGroup1;
			Tutorials.add(group1);

			// A tutorial group for SET010001 on the time slot of Friday morning
			TimeTableSlot slot2 = new TimeTableSlot(2, 2);
			TutorialGroup tutorialGroup2 = new TutorialGroup(slot2, "SET010001", 1);
			TutorialGroup[] group2 = new TutorialGroup[1];
			group2[0] = tutorialGroup2;
			Tutorials.add(group2);

			// A tutorial group for SET011101 on the time slot of Friday morning
			TimeTableSlot slot3 = new TimeTableSlot(4, 6);
			TutorialGroup tutorialGroup3 = new TutorialGroup(slot3, "SET011101", 2);
			TutorialGroup[] group3 = new TutorialGroup[1];
			group3[0] = tutorialGroup3;
			Tutorials.add(group3);

			// Set all timetable slots to preference 2 to start with, meaning happy
			for (int a = 0; a < slotPreferences1.length; a++) {
				for (int b = 0; b < slotPreferences1[0].length; b++) {
					slotPreferences1[a][b] = 2;
					slotPreferences2[a][b] = 2;
					slotPreferences3[a][b] = 2;
				}

				// Pass slot preferences and corresponding tutorial group to student agent
				passToStudent1[0] = slotPreferences1;
				passToStudent1[1] = group1;

				passToStudent2[0] = slotPreferences2;
				passToStudent2[1] = group2;

				passToStudent3[0] = slotPreferences3;
				passToStudent3[1] = group3;
			}
			// Start the agents, specified amount is in the loop
			try {
				ContainerController myContainer = myRuntime.createMainContainer(myProfile);
				AgentController rma = myContainer.createNewAgent("rma", "jade.tools.rma.rma", null);
				rma.start();

				for (int i = 0; i < 8; i += 3) {
					AgentController student1 = myContainer.createNewAgent("student" + i,
							StudentAgent.class.getCanonicalName(), passToStudent1);
					student1.start();
					Student.add(student1.getName());

					AgentController student2 = myContainer.createNewAgent("student" + (i + 1),
							StudentAgent.class.getCanonicalName(), passToStudent2);
					student2.start();
					Student.add(student2.getName());

					AgentController student3 = myContainer.createNewAgent("student" + (i + 2),
							StudentAgent.class.getCanonicalName(), passToStudent3);
					student3.start();
					Student.add(student3.getName());
				}
				AgentController timetableAgent = myContainer.createNewAgent("timetableAgent",
						TimeTableAgent.class.getCanonicalName(), null);
				timetableAgent.start();
			} catch (Exception e) {
				System.out.println("Exception starting agent: " + e.toString());
			}
		}

		if (testCase == 3) {

			Object[] passToStudent3 = new Object[2];
			int[][] slotPreferences3 = new int[5][9];

			// A tutorial group for SET010101 on the time slot of Monday morning
			TimeTableSlot slot1 = new TimeTableSlot(0, 0);
			TutorialGroup tutorialGroup1 = new TutorialGroup(slot1, "SET010101", 0);
			TutorialGroup[] group1 = new TutorialGroup[1];
			group1[0] = tutorialGroup1;
			Tutorials.add(group1);

			// A tutorial group for SET010001 on the time slot of Friday morning
			TimeTableSlot slot2 = new TimeTableSlot(2, 2);
			TutorialGroup tutorialGroup2 = new TutorialGroup(slot2, "SET010001", 1);
			TutorialGroup[] group2 = new TutorialGroup[1];
			group2[0] = tutorialGroup2;
			Tutorials.add(group2);

			// A tutorial group for SET011101 on the time slot of Friday morning
			TimeTableSlot slot3 = new TimeTableSlot(4, 6);
			TutorialGroup tutorialGroup3 = new TutorialGroup(slot3, "SET011101", 2);
			TutorialGroup[] group3 = new TutorialGroup[1];
			group3[0] = tutorialGroup3;
			Tutorials.add(group3);

			// Set all timetable slots to preference 2 to start with, meaning happy
			for (int a = 0; a < slotPreferences1.length; a++) {
				for (int b = 0; b < slotPreferences1[0].length; b++) {
					slotPreferences1[a][b] = 2;
					slotPreferences2[a][b] = 2;
					slotPreferences3[a][b] = 2;
				}

				// Pass slot preferences and corresponding tutorial group to student agent
				passToStudent1[0] = slotPreferences1;
				passToStudent1[1] = group1;

				passToStudent2[0] = slotPreferences2;
				passToStudent2[1] = group2;

				passToStudent3[0] = slotPreferences3;
				passToStudent3[1] = group3;
			}
			// Start the agents, specified amount is in the loop
			try {
				ContainerController myContainer = myRuntime.createMainContainer(myProfile);
				AgentController rma = myContainer.createNewAgent("rma", "jade.tools.rma.rma", null);
				rma.start();

				for (int i = 0; i < 100; i += 3) {
					AgentController student1 = myContainer.createNewAgent("student" + i,
							StudentAgent.class.getCanonicalName(), passToStudent1);
					student1.start();
					Student.add(student1.getName());

					AgentController student2 = myContainer.createNewAgent("student" + (i + 1),
							StudentAgent.class.getCanonicalName(), passToStudent2);
					student2.start();
					Student.add(student2.getName());

					AgentController student3 = myContainer.createNewAgent("student" + (i + 2),
							StudentAgent.class.getCanonicalName(), passToStudent3);
					student3.start();
					Student.add(student3.getName());
				}
				AgentController timetableAgent = myContainer.createNewAgent("timetableAgent",
						TimeTableAgent.class.getCanonicalName(), null);
				timetableAgent.start();
			} catch (Exception e) {
				System.out.println("Exception starting agent: " + e.toString());
			}
		}
	}
}