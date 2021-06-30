package training;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Scanner;
import training.ConsoleColors;

public class TaskManager {
	
	private static String welcomeMsg = "Welcome to TaskManager!";
	private static String ioExcMsg = "There is a problem with reading from/writing the file!";
	private static String fileNotFoundExcMsg = "File could not be found!";
	private static String fileAbsolutePath = "C:\\Users\\Kempinov\\git\\Tasks\\src\\training\\tasks.csv";
	
	public static void main(String[] args) {
		
		System.out.println(welcomeMsg);
		String input = null;
		Scanner userInputScanner = new Scanner(System.in);
		Path file = Paths.get(fileAbsolutePath);
		Scanner fileScanner = null;
		
		try {
			fileScanner = new Scanner(file);
		} catch(FileNotFoundException fnfe) {
			System.out.println(fileNotFoundExcMsg);
		} catch(IOException ioe) {
			ioe.getStackTrace();
			System.out.println(ioExcMsg);
		}
		
		do {
			input = null;
			showMenu();
			input = takeActionFromUser(userInputScanner);
			processMenuSelection(input, fileScanner, userInputScanner);
		} while(Integer.valueOf(input) != 6);
			
		userInputScanner.close();
		fileScanner.close();
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
	
	private static String[][] getFileContent() { 
		Path file = Paths.get(fileAbsolutePath);
		Scanner fileScanner = null;
		
		try {
			fileScanner = new Scanner(file);
		} catch(FileNotFoundException fnfe) {
			System.out.println(fileNotFoundExcMsg);
		} catch(IOException ioe) {
			ioe.getStackTrace();
			System.out.println(ioExcMsg);
		}
		
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
		String response = userInputScanner.nextLine();
		return response;
	}
	
	private static void processMenuSelection(String input, Scanner fileScanner, Scanner userInputScanner) {
		switch (Integer.valueOf(input)) {
		    case 1:
		        addTask(userInputScanner, fileScanner);
		        break;
		    case 2:
		        updateTask();
		        break;
		    case 3:
		        listAllTasks(fileScanner);
		        break;
		    case 4:
		        removeTask(fileScanner, userInputScanner);
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
	
	private static void displayTasks(Scanner fileScanner, boolean linesNumerated) {
		
		System.out.println();
		System.out.println("- - -");
		System.out.println();
		
		String[][] tasks = getFileContent();
		int number = 1;
		for(int i = 0; i < tasks.length; i++) {
			String line = "";
			for(int j = 0; j < tasks[i].length; j++) {
				line += tasks[i][j];
				if(j < 2) {
					line += "  ";
				}
			}
			
			if(linesNumerated) {
				System.out.printf("%s  %s\n",number++ , line);
			} else {
				System.out.println(line);
			}
		}
		
		System.out.println();
		System.out.println("- - -");
		System.out.println();
		
	}
	
	private static void quit() {
		System.out.println("Thank you for using TaskManager.");
		System.out.println("Goodbye!");
	}
	
	private static void addTask(Scanner userInputScanner, Scanner fileScanner) {
		System.out.println("Please add task description:");
		String description = takeActionFromUser(userInputScanner);
		
		System.out.println("Please add task due date: [YYYY-MM-DD] or type in [today]");
		String date = takeActionFromUser(userInputScanner);
		if(date.equalsIgnoreCase("today")) {
//			LocalDate now = new LocalDate();
//			date = LocalDate.now();
		}
		
		String importance = null;
		do {
			System.out.println("Is your task important? [yes/no]");
			importance = takeActionFromUser(userInputScanner).toLowerCase();
		} while(importance == "yes" || importance == "no");
		
		switch(importance) {
			case "yes":
				importance = "true";
				break;
			case "no":
				importance = "false";
				break;
		}
		String sthg;
		String lineToBeSavedToFile = description + ", " + date + ", " + importance + "\n";
		
		String confirmation = null;
		do {
			System.out.println("This task will be saved: \n" + lineToBeSavedToFile);
			System.out.println("Are you sure? [yes/no]");
			confirmation = takeActionFromUser(userInputScanner).toLowerCase();
		} while(confirmation == "yes" || confirmation == "no");
		
		if(confirmation.equalsIgnoreCase("yes")) {
			
			try {
				FileWriter fw = new FileWriter(fileAbsolutePath, true);
				fw.append(lineToBeSavedToFile);
				fw.close();
			} catch(IOException ioe) {
				System.out.println(ioExcMsg);
				ioe.getStackTrace();
			}
			System.out.println("Task has been successfully added!");
		}
		
	}
	
	private static void updateTask() {
		System.out.println("update");
	}
	
	private static void listAllTasks(Scanner fileScanner) {
		displayTasks(fileScanner, false);
	}
	
	private static void removeTask(Scanner fileScanner, Scanner userInputScanner) {
		displayTasks(fileScanner, true);
		System.out.println("Which task you wish to be removed? Type in the number on the left.");
		String taskNumber = takeActionFromUser(userInputScanner);
	}
	
	private static void help() {
		System.out.println("*** HELP SECTION ***");
		System.out.println("1. To execute a command either type in the command's number(if given) or\n whatever the program asks you to. Next you should press ENTER.");
	}
}