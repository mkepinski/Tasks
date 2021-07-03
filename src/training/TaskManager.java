package training;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
			        removeTask(fileScanner, userInputScanner);
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
		System.out.println("Please choose the task to be updated by entering task number.");
		String[][] tasks = getFileContent();
		String taskNumber;
		
		do {
			taskNumber = userInputScanner.next();
			if(!Character.isDigit(taskNumber.charAt(0)) || ((Integer.valueOf(taskNumber) < 1 || Integer.valueOf(taskNumber) > tasks.length))) {
				System.out.printf("Please enter a correct number.Possible values: [1-%s]\n", tasks.length);
				continue;
			}
		} while(!Character.isDigit(taskNumber.charAt(0)) || ((Integer.valueOf(taskNumber) < 1 || Integer.valueOf(taskNumber) > tasks.length)));
		
		String oldTask = (tasks[Integer.valueOf(taskNumber) - 1][0] + tasks[Integer.valueOf(taskNumber) - 1][1] + tasks[Integer.valueOf(taskNumber) - 1][2]);
		System.out.printf("\nThis task: [%s] will be replaced.\n", oldTask);
		
		System.out.printf("Old description: %s\n", tasks[Integer.valueOf(taskNumber) - 1][0]);
		String description = null;
		Scanner scanner = new Scanner(System.in);
		do {
			System.out.println("Please add task description: [max 40 signs]");
			description = scanner.nextLine();
		} while(description.length() >= 40 || description.length() <= 1);
		
		System.out.printf("Old due date: %s\n", tasks[Integer.valueOf(taskNumber) - 1][1]);
		String date = null;
		scanner = new Scanner(System.in);
		
		do {
			System.out.println("Please add task due date: [YYYY-MM-DD] or type in [today]");
			date = scanner.nextLine();
		} while(!checkDateInputConditions(date));
		
		if(date.equalsIgnoreCase("today")) {
			LocalDate today = LocalDate.now();
			date = today.toString();
		}
		
		String importance = null;
		scanner = new Scanner(System.in);
		do {
			System.out.println("Is your task important? [yes/no]");
			importance = scanner.nextLine().toLowerCase();
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
		scanner = new Scanner(System.in);
		do {
			System.out.println("This task will be saved: \n" + lineToBeSavedToFile);
			System.out.println("Are you sure? [yes/no]");
			confirmation = scanner.nextLine().toLowerCase();
		} while(!confirmation.equals("yes") && !confirmation.equals("no"));
		
		if(confirmation.equalsIgnoreCase("yes")) {
			
			try {
				tasks[Integer.valueOf(taskNumber) - 1][0] = description;
				tasks[Integer.valueOf(taskNumber) - 1][1] = date;
				tasks[Integer.valueOf(taskNumber) - 1][2] = importance;
				FileWriter fw = new FileWriter(fileAbsolutePath, false);
				
				for(int i = 0; i < tasks.length; i++) {
					String line = "";
					for(int j = 0; j < tasks[i].length; j++) {
						line += tasks[i][j].trim();
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
		scanner.close();
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
		System.out.println("1. To execute a command either type in the command's number(if given) or\n whatever the program asks you to. Next you should press ENTER.\n");
	}
}