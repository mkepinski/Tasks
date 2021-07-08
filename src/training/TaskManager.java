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
			
				showMenu();
				input = takeActionFromUser(userInputScanner);
				processMenuSelection(input, fileScanner, userInputScanner);	
				
		} while(!(input == null) && (!input.equals("6")));
			
		userInputScanner.close();
		fileScanner.close();
		quit();
	}
	
	private static void showMenu() {
		System.out.println("Available options:\n" +
						   "1. Add task\n" +
						   "2. Update task\n" +
						   "3. List all tasks\n" +
						   "4. Delete task\n" +
						   "5. Help\n" +
						   "6. Quit program\n");
	}

	
	private static List<String[]> getFileContent() { 
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

		List<String[]> tasks = new ArrayList<>();
		String line = null;
		for(;fileScanner.hasNextLine();) {
			line = fileScanner.nextLine();
			String[] splitLine = line.split(",", -2);
			tasks.add(splitLine);
		}
		return tasks;
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
			        displayTasks(fileScanner, false);
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
		
		List<String[]> tasks = getFileContent();

		int number = 0;
		for(int i = 0; i < tasks.size(); i++) {
			if(linesNumerated) {
				System.out.println(String.format("%s   %s - %s - %s", number++, tasks.get(i)[0].trim(), tasks.get(i)[1].trim(), tasks.get(i)[2].trim()));
			} else {
				System.out.println(String.format("%s, %s, %s", tasks.get(i)[0].trim(), tasks.get(i)[1].trim(), tasks.get(i)[2].trim()));
			}
		}
		
		System.out.println();
		System.out.println("- - -");
		System.out.println();
		
	}
	
	public static boolean dateValid(String dateStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate.parse(dateStr, formatter);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }
	
	private static void quit() {
		System.out.println("Thank you for using TaskManager.");
		System.out.println("Goodbye!");
	}
	
	private static boolean checkTaskNumber(int input) {
		return (input >= 1 && input <= getFileContent().size());
	}
	
	private static boolean checkDate(String date) {
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
		} while(!checkDate(date));
		
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
		List<String[]> tasks = getFileContent();
		
		boolean whileBoolVar = true;
		String string_input = null;
		while(whileBoolVar) {
			System.out.println("Which task you wish to change? Type in its number.");
			string_input = takeActionFromUser(userInputScanner);
			if(checkIfInputIsNumber(string_input) && checkTaskNumber(Integer.valueOf(string_input))) {
				whileBoolVar = false;
			}
		}
		
		int int_input = Integer.valueOf(string_input);
		
		System.out.printf("This task will be changed: %s - %s - %s\n", tasks.get(int_input)[0], tasks.get(int_input)[1], tasks.get(int_input)[2]);

		String description = null;
		do {
			System.out.println("New task description:: [max 40 signs]");
			description = takeActionFromUser(userInputScanner);
		} while(description.length() >= 40 || description.length() <= 1);
		
		String date = null;
		do {
			System.out.println("Please add task due date: [YYYY-MM-DD] or type in [today]");
			date = takeActionFromUser(userInputScanner);
		} while(!checkDate(date));
		
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
			
			tasks.get(int_input)[0] = description;
			tasks.get(int_input)[1] = date;
			tasks.get(int_input)[2] = importance;
			
			writeToFile(tasks);
			System.out.println("Task has been successfully updated!");
		}
	}

	private static void writeToFile(List<String[]> tasks) {
		try {

			FileWriter fw = new FileWriter(fileAbsolutePath, false);
			for (String[] line : tasks) {
				fw.append(String.format("%s,%s,%s",  line[0], line[1], line[2])).append("\n");
			}
			fw.close();
		} catch(IOException ioe) {
			System.out.println(ioExcMsg);
			ioe.getStackTrace();
		}
	}
	
	private static boolean checkIfInputIsNumber(String input) {
		try {
			Integer.valueOf(input);
		} catch(NumberFormatException nfe) {
			return false;
		}
		return true;
	}
	
	private static void deleteTask(Scanner fileScanner, Scanner userInputScanner) {
//		displayTasks(fileScanner, true);
//		boolean whileBoolVar = true;
//		String string_input = null;
//		while(whileBoolVar) {
//			System.out.println("Which task you wish to delete? Type in its number.");
//			string_input = takeActionFromUser(userInputScanner);
//			if(Character.isDigit(string_input.charAt(0))) {
//				whileBoolVar = false;
//			}
//		}
//		int int_input = Integer.valueOf(string_input) - 1;
//		
//		String[][] tasksArray = getFileContent();
//		List<String> tasksList = new ArrayList<>();
//		for(int i = 0; i < tasksArray.length; i++) {
//			String line = "";
//			for(int j = 0; j < tasksArray[i].length; j++) {
//				if(i != int_input) {
//					line += tasksArray[i][j];
//					if(j < 2) {
//						line += ", ";
//					}
//				}
//			}
//			tasksList.add(line);
//		}
//		
//		String confirmation = null;
//		do {
//			System.out.println("Are you sure? [yes/no]");
//			confirmation = takeActionFromUser(userInputScanner).toLowerCase();
//		} while(!confirmation.equals("yes") && !confirmation.equals("no"));
//		
//		if(confirmation.equalsIgnoreCase("yes")) {
//			
//			try {
//
//				FileWriter fw = new FileWriter(fileAbsolutePath, false);
//				for (String s : tasksList) {
//					fw.append(s.trim()).append("\n");
//				}
//				
//				fw.close();
//			} catch(IOException ioe) {
//				System.out.println(ioExcMsg);
//				ioe.getStackTrace();
//			}
//			System.out.println("Task has been successfully deleted!");
//		}
	}
	
	private static void help() {
		System.out.println("*** HELP SECTION ***");
		System.out.println("1. To execute a command either type in the command's number(if given) or\n whatever the program asks you to. Next you should press ENTER.\n");
	}
}