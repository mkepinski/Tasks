package training;

public class TaskManager {
	
	private static String welcomeMsg = "Welcome to TaskManager!";
	
	public static void main(String[] args) {
		
		System.out.println(welcomeMsg);
		showMenu();
		
	}
	
	private static void showMenu() {
		System.out.println("Available options:\n" +
						   "1. Add task\n" +
						   "2. Update task\n" +
						   "3. Remove task\n" +
						   "4. Help\n" +
						   "5. Quit program\n");
	}
}