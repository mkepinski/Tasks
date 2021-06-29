package training;

import java.util.Scanner;

public class TaskManager {
	
	private static String welcomeMsg = "Welcome to TaskManager!";
	
	public static void main(String[] args) {
		
		System.out.println(welcomeMsg);
		String input = null;
		Scanner userInputScanner = new Scanner(System.in);
		
			do {
				input = null;
				showMenu();
				input = takeActionFromUser(userInputScanner);
				processMenuSelection(input);
			} while(Integer.valueOf(input) != 6);
			
		userInputScanner.close();
		quit();
	}
	
	private static void showMenu() {
		System.out.println("Available options:\n" +
						   "1. Add task\n" +
						   "2. Update task\n" +
						   "3. List all tasks\n" +
						   "4. Remove task\n" +
						   "5. Help\n" +
						   "6. Quit program\n");
	}
	
	private static String takeActionFromUser(Scanner userInputScanner) {
		String response = userInputScanner.next();
		return response;
	}
	
	private static void processMenuSelection(String input) {
		switch (Integer.valueOf(input)) {
		    case 1:
		        addTask();
		        break;
		    case 2:
		        updateTask();
		        break;
		    case 3:
		        listAllTasks();
		        break;
		    case 4:
		        removeTask();
		        break;
		    case 5:
		        help();
		        break;
		    case 6:
		    	break;
		    default:
		        System.out.println("Please select a correct option.");
		}
	}
	
	private static void quit() {
		System.out.println("Thank you for using TaskManager.\nGoodbye!");;
	}
	
	private static void addTask() {
		System.out.println("add");
	}
	private static void updateTask() {
		System.out.println("update");
	}
	private static void listAllTasks() {
		System.out.println("list");
	}
	private static void removeTask() {
		System.out.println("remove");
	}
	private static void help() {
		System.out.println("help");
	}
}