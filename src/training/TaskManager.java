package training;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;
import training.ConsoleColors;

public class TaskManager {
	
	private static String welcomeMsg = "Welcome to TaskManager!";
	
	public static void main(String[] args) {
		
		System.out.println(welcomeMsg);
		String input = null;
		Scanner userInputScanner = new Scanner(System.in);
		Path file = Paths.get("C:\\Users\\Kempinov\\git\\Tasks\\src\\training\\tasks.csv");
		Scanner fileScanner = null;
		
		try {
			fileScanner = new Scanner(file);
		} catch(FileNotFoundException fnfe) {
			System.out.println("File could not be found!");
		} catch(IOException ioe) {
			ioe.getStackTrace();
			System.out.println("There is a problem with reading from/writing the file!");
		}
		
		do {
			input = null;
			showMenu();
			input = takeActionFromUser(userInputScanner);
			processMenuSelection(input, fileScanner, userInputScanner);
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
	
	private static String[][] getFileContent(Scanner fileScanner) {
		String[][] tasks = new String[0][3];
		String line = null;
		for(int i = 0; fileScanner.hasNextLine(); i++) {
			line = fileScanner.nextLine();
			String[] splitLine = line.split(",", -2);
			tasks = increaseArraySize(tasks);
			for(int j = 0; j < 3; j++) {
				tasks[i][j] = splitLine[j];
			}
		}
		
		return tasks;
	}
	
	private static String[][] increaseArraySize(String[][] orgArray) {
		String[][] newArray = new String[orgArray.length + 1][3];
		for(int i = 0; i < orgArray.length; i++) {
			for(int j = 0; j < orgArray[i].length; j++) {
				newArray[i][j] = orgArray[i][j];
			}
		}
		return newArray;
	}
	
	private static String takeActionFromUser(Scanner userInputScanner) {
		String response = userInputScanner.next();
		return response;
	}
	
	private static void processMenuSelection(String input, Scanner fileScanner, Scanner userInputScanner) {
		switch (Integer.valueOf(input)) {
		    case 1:
		        addTask(userInputScanner);
		        break;
		    case 2:
		        updateTask();
		        break;
		    case 3:
		        listAllTasks(fileScanner);
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
	
	private static void displayTasks(Scanner fileScanner) {
		String[][] tasks = getFileContent(fileScanner);
		for(int i = 0; i < tasks.length; i++) {
			String line = "";
			for(int j = 0; j < tasks[i].length; j++) {
				line += tasks[i][j];
				if(j < 2) {
					line += ", ";
				}
			}
			System.out.println(line);
		}
	}
	
	private static void quit() {
		System.out.println("Thank you for using TaskManager.");
		System.out.println("Goodbye!");
	}
	
	private static void addTask(Scanner userInputScanner) {
		System.out.println("Please add task description:");
		String description = takeActionFromUser(userInputScanner);
		
		System.out.println("Please add task due date: [YYYY-MM-DD]");
		String date = takeActionFromUser(userInputScanner);
		
		String importance = null;
		do {
			System.out.println("Is your task important? [yes/no]");
			importance = takeActionFromUser(userInputScanner).toLowerCase();
		} while(importance != "yes" || importance != "no");
		
		switch(importance) {
			case "yes":
				importance = "true";
				break;
			case "no":
				importance = "false";
				break;
		}
		
		System.out.printf("%s, %s, %s", description, date, importance);
	}
	
	private static void updateTask() {
		System.out.println("update");
	}
	
	private static void listAllTasks(Scanner fileScanner) {
		displayTasks(fileScanner);
	}
	
	private static void removeTask() {
		System.out.println("remove");
	}
	
	private static void help() {
		System.out.println("help");
	}
}