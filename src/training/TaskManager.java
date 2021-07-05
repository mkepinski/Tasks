package training;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
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
//				userInputScanner = new Scanner(System.in);
				showMenu();
				Scanner testScanner = new Scanner(System.in);
//				if(userInputScanner.hasNextLine()) {
//					input = takeActionFromUser(userInputScanner);
				input = testScanner.nextLine();
					processMenuSelection(input, fileScanner, userInputScanner);
//				} else {
//					Scanner testScanner = new Scanner(System.in);
//					input = testScanner.nextLine();
//				}
				
		} while(!(input == null) || (!input.equals("6")));
			
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
			if(!line.isBlank()) {
				String[] splitLine = line.split(",", -2);
				tasks = increaseArraySize(tasks);
				for(int j = 0; j < 3; j++) {
					tasks[i][j] = splitLine[j];
				}
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
		if(!input.isBlank()) {
			switch (input) {
			    case "1":
			        addTask(userInputScanner, fileScanner);
			        break;
			    case "2":
			        updateTask(fileScanner, userInputScanner);
			        break;
			    case "3":
			        listAllTasks(fileScanner);
			        break;
			    case "4":
			        deleteTask(fileScanner, userInputScanner);
			        break;
			    case "5":
			        help();
			        break;
			    case "6":
			    	break;
			    default:
			        System.out.println("Please select a correct option.");
			}
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
	
	public static boolean dateValid(String dateStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(dateStr, formatter);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }
	
	private static void quit() {
		System.out.println("Thank you for using TaskManager.");
		System.out.println("Goodbye!");
	}
	
	private static boolean checkDateInputConditions(String date) {
		if(date.equalsIgnoreCase("today")) {
			return true;
		} else if(dateValid(date)) {
			return true;
		}
		return false;
	}
	
	private static void addTask(Scanner userInputScanner, Scanner fileScanner) {
		
		String description = null;
		do {
			System.out.println("Please add task description: [max 40 signs]");
			description = takeActionFromUser(userInputScanner);
		} while(description.length() >= 40 || description.length() <= 1);
		
		String date = null;
		do {
			System.out.println("Please add task due date: [YYYY-MM-DD] or type in [today]");
			date = takeActionFromUser(userInputScanner);
		} while(!checkDateInputConditions(date));
		
		if(date.equalsIgnoreCase("today")) {
			LocalDate today = LocalDate.now();
			date = today.toString();
		}
		
		String importance = null;
		do {
			System.out.println("Is your task important? [yes/no]");
			importance = takeActionFromUser(userInputScanner).toLowerCase();
		} while(!importance.equals("yes") && !importance.equals("no"));
		
		switch(importance) {
			case "yes":
				importance = "true";
				break;
			case "no":
				importance = "false";
				break;
		}

		String lineToBeSavedToFile = description + ", " + date + ", " + importance + "\n";
		
		String confirmation = null;
		do {
			System.out.println("This task will be saved: \n" + lineToBeSavedToFile);
			System.out.println("Are you sure? [yes/no]");
			confirmation = takeActionFromUser(userInputScanner).toLowerCase();
		} while(!confirmation.equals("yes") && !confirmation.equals("no"));
		
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
	
	private static void updateTask(Scanner fileScanner, Scanner userInputScanner) {
		displayTasks(fileScanner, true);
		List<String[]> tasksList = new ArrayList<>();
		String[][] tasksArray = getFileContent();
		for(int i = 0; i < tasksArray.length; i++) {
			tasksList.add(tasksArray[i]);
		}
		
		boolean whileBoolVar = true;
		String string_input = null;
		while(whileBoolVar) {
			System.out.println("Which task you wish to change? Type in its number.");
			string_input = takeActionFromUser(userInputScanner);
			if(Character.isDigit(string_input.charAt(0))) {
				whileBoolVar = false;
			}
		}
		int int_input = Integer.valueOf(string_input) - 1;
		String[] oldTaskArr = tasksList.get(int_input);
		String oldTask = "";
		for(String s : oldTaskArr) {
			oldTask += s.trim();
			oldTask += " ";
		}
		System.out.printf("This task will be changed: %s\n", oldTask);

		String description = null;
		do {
			System.out.println("New task description:: [max 40 signs]");
			description = takeActionFromUser(userInputScanner);
		} while(description.length() >= 40 || description.length() <= 1);
		
		String date = null;
		do {
			System.out.println("Please add task due date: [YYYY-MM-DD] or type in [today]");
			date = takeActionFromUser(userInputScanner);
		} while(!checkDateInputConditions(date));
		
		if(date.equalsIgnoreCase("today")) {
			LocalDate today = LocalDate.now();
			date = today.toString();
		}
		
		String importance = null;
		do {
			System.out.println("Is your task important? [yes/no]");
			importance = takeActionFromUser(userInputScanner).toLowerCase();
		} while(!importance.equals("yes") && !importance.equals("no"));
		
		switch(importance) {
			case "yes":
				importance = "true";
				break;
			case "no":
				importance = "false";
				break;
		}
		
		String confirmation = null;
		do {
			System.out.println("Are you sure? [yes/no]");
			confirmation = takeActionFromUser(userInputScanner).toLowerCase();
		} while(!confirmation.equals("yes") && !confirmation.equals("no"));
		
		if(confirmation.equalsIgnoreCase("yes")) {
			
			try {
				tasksArray[int_input][0] = description;
				tasksArray[int_input][1] = date;
				tasksArray[int_input][2] = importance;
				FileWriter fw = new FileWriter(fileAbsolutePath, false);
				for(int i = 0; i < tasksArray.length; i++) {
					String line = "";
					for(int j = 0; j < tasksArray[i].length; j++) {
						line += tasksArray[i][j];
						if(j < 2) {
							line += ", ";
						}
					}
					fw.append(line).append("\n");
				}
				fw.close();
			} catch(IOException ioe) {
				System.out.println(ioExcMsg);
				ioe.getStackTrace();
			}
			System.out.println("Task has been successfully updated!");
		}
	}
	
	private static void listAllTasks(Scanner fileScanner) {
		displayTasks(fileScanner, false);
	}
	
	private static void deleteTask(Scanner fileScanner, Scanner userInputScanner) {
		displayTasks(fileScanner, true);
		boolean whileBoolVar = true;
		String string_input = null;
		while(whileBoolVar) {
			System.out.println("Which task you wish to delete? Type in its number.");
			string_input = takeActionFromUser(userInputScanner);
			if(Character.isDigit(string_input.charAt(0))) {
				whileBoolVar = false;
			}
		}
		int int_input = Integer.valueOf(string_input) - 1;
		
		String[][] tasksArray = getFileContent();
		List<String> tasksList = new ArrayList<>();
		for(int i = 0; i < tasksArray.length; i++) {
			String line = "";
			for(int j = 0; j < tasksArray[i].length; j++) {
				if(i != int_input) {
					line += tasksArray[i][j];
					if(j < 2) {
						line += ", ";
					}
				}
			}
			tasksList.add(line);
		}
		
		String confirmation = null;
		do {
			System.out.println("Are you sure? [yes/no]");
			confirmation = takeActionFromUser(userInputScanner).toLowerCase();
		} while(!confirmation.equals("yes") && !confirmation.equals("no"));
		
		if(confirmation.equalsIgnoreCase("yes")) {
			
			try {

				FileWriter fw = new FileWriter(fileAbsolutePath, false);
				for (String s : tasksList) {
					fw.append(s).append("\n");
				}
				
				fw.close();
			} catch(IOException ioe) {
				System.out.println(ioExcMsg);
				ioe.getStackTrace();
			}
			System.out.println("Task has been successfully updated!");
		}
	}
	
	private static void help() {
		System.out.println("*** HELP SECTION ***");
		System.out.println("1. To execute a command either type in the command's number(if given) or\n whatever the program asks you to. Next you should press ENTER.\n");
	}
}