import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class TaskApp{
	
	



    public static void main(String[] args){
    	TaskManager manager = new TaskManager();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while(running){
        	printMenu();
        	String choice = scanner.nextLine();

        	switch (choice) {
                case "1":
                    Task newTask = promptForTaskDetails(scanner);
					manager.addTask(newTask);
					System.out.println("Task added!");
                    break;
                case "2":
                    handleRemoveTask(manager, scanner);
                    break;
                case "3":
                    handleTaskCompletion(manager, scanner);
                    break;
                case "4":
                    manager.listAllTasks();
                    break;
                case "5":
                    manager.listTasksByPriority();
                    break;
                case "6":
                    manager.listTasksByDueDate();
                    break;
                case "7":
                    manager.listCompletedTasks();
                    break;
                case "8":
                    manager.listIncompleteTasks();
                    break;
                case "9":
                    manager.saveToFile("tasks.dat");
                    break;
                case "10":
                    manager.loadFromFile("tasks.dat");
                    break;
                case "0":
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        scanner.close();
    }  

    public static void printMenu() {
	    System.out.println("\n===== Task Scheduler Menu =====");
	    System.out.println("1. Add Task");
	    System.out.println("2. Remove Task");
	    System.out.println("3. Mark Task as Done");
	    System.out.println("4. View All Tasks");
	    System.out.println("5. View Tasks by Priority");
	    System.out.println("6. View Tasks by Due Date");
	    System.out.println("7. View Completed Tasks");
	    System.out.println("8. View Incomplete Tasks");
	    System.out.println("9. Save Tasks");
	    System.out.println("10. Load Tasks");
	    System.out.println("0. Exit");
	    System.out.print("Choose an option: ");
	}

	public static Task promptForTaskDetails(Scanner scanner) {
		System.out.println("What is the task title? ");
		String title = scanner.nextLine();
		System.out.println("What is the description? ");
		String description = scanner.nextLine();
		System.out.println("What is the Priority, LOW, MEDIUM, HIGH, URGENT? ");
		Priority priority = promptForPriority(scanner);
		System.out.println("What is your Due Date in YYYY-MM-DDTHH:00 format?");
		LocalDateTime dueDate = promptForDueDate(scanner);
		return new Task(title, description, priority, dueDate, LocalDateTime.now());

	}

	public static Priority promptForPriority(Scanner scanner) {
	    while (true) {
	        for (Priority p : Priority.values()) {
	            System.out.print(p.name() + " ");
	        }
	        System.out.print("): ");

	        String input = scanner.nextLine().trim().toUpperCase();

	        try {
	            return Priority.valueOf(input);
	        } catch (IllegalArgumentException e) {
	            System.out.println("Invalid priority. Please enter one of the listed options.");
	        }
	    }
	}

	public static LocalDateTime promptForDueDate(Scanner scanner) {
	    while (true) {
	        String input = scanner.nextLine().trim();

	        try {
	            return LocalDateTime.parse(input);
	        } catch (DateTimeParseException e) {
	            System.out.println("Invalid format. Please try again.");
	        }
	    }
	}

	public static void handleRemoveTask(TaskManager manager, Scanner scanner) {
		System.out.println("Enter Task id: ");
		String input = scanner.nextLine();
		try {
			int id = Integer.parseInt(input);
	        boolean success = manager.removeTask(id);

	        if (success) {
	            System.out.println("Task removed successfully.");
	        } else {
	            System.out.println("No task found with ID " + id + ".");
	        }

	    } catch (NumberFormatException e) {
	        System.out.println("Invalid input. Please enter a valid numeric task ID.");
	    }
	}

	public static void handleTaskCompletion(TaskManager manager, Scanner scanner) {
		System.out.println("Enter Task id: ");
		String input = scanner.nextLine();
		try {
			int id = Integer.parseInt(input);
	        boolean success = manager.markTaskAsDone(id);

	        if (success) {
	            System.out.println("Task Completed Successfully.");
	        } else {
	            System.out.println("No task found with ID " + id + ".");
	        }

	    } catch (NumberFormatException e) {
	        System.out.println("Invalid input. Please enter a valid numeric task ID.");
	    }
	}

}