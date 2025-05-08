import java.util.HashMap;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.time.LocalDateTime;
import java.io.*;
public class TaskManager{
	private HashMap<Integer, Task> tasks = new HashMap<>();
	int nextId = 0;

	public void addTask(Task task){
		tasks.put(nextId++, task);
	}

	public void listAllTasks(){
		tasks.forEach((key, value) -> System.out.println("Task Number " + key + ": " + value));
	}

	public boolean removeTask(int id){
		if (tasks.containsKey(id)) {
			tasks.remove(id);
			return true;
		}
		else{
			return false;
		}
		
	}
	public boolean removeTask(Task task) {
	    return tasks.values().remove(task);
	}

	public boolean markTaskAsDone(int id) {
	    if (tasks.containsKey(id)) {
	        Task task = tasks.get(id);
	        task.markCompleted();
	        return true;
	    } 
	    else {
	        return false;
	    }
	}

	public Task getTask(int id){
		return tasks.get(id);
	}

	public void listTasksByPriority() {
	    List<Task> taskList = new ArrayList<>(tasks.values());

	    // Sort by priority: CRITICAL > HIGH > MEDIUM > LOW
	    taskList.sort((t1, t2) -> t2.getPriority().compareTo(t1.getPriority()));

	    for (Task task : taskList) {
	        System.out.println(task);
	    }
	}

	public void listTasksByDueDate() {
		List<Task> taskList = new ArrayList<>(tasks.values());

		taskList.sort((t1, t2) -> t2.getDueDate().compareTo(t1.getDueDate()));

		for (Task task : taskList){
			System.out.println(task);
		}
	}

	public void listCompletedTasks() {
		List<Task> taskList = new ArrayList<>(tasks.values());

		for (Task task : taskList){
			if(task.isCompleted() == true){
				System.out.println(task);
			}
		}
	}

	public void listIncompleteTasks() {
		List<Task> taskList = new ArrayList<>(tasks.values());

		for (Task task : taskList){
			if(task.isCompleted() == false){
				System.out.println(task);
			}
		}
	}

	public void saveToFile(String fileName){
		try {
			FileOutputStream fileOut = new FileOutputStream("tasks.dat");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(tasks);
			out.close();
			fileOut.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void loadFromFile(String filename) {
	    try {
	        FileInputStream fileIn = new FileInputStream(filename);
	        ObjectInputStream in = new ObjectInputStream(fileIn);
	        Object obj = in.readObject();

	        if (obj instanceof HashMap) {
	            tasks = (HashMap<Integer, Task>) obj;

	            // Update nextId to avoid duplicates
	            int maxId = tasks.keySet().stream().mapToInt(id -> id).max().orElse(0);
	            nextId = maxId + 1;
	        }

	        in.close();
	        fileIn.close();
	    } catch (FileNotFoundException e) {
	        System.out.println("No existing task file found. Starting fresh.");
	    } catch (EOFException e) {
	        System.out.println("Task file is empty. No tasks loaded.");
	    } catch (IOException | ClassNotFoundException e) {
	        e.printStackTrace(); // Replace with more graceful handling if you prefer
	    }
	}

	public Collection<Task> getAllTasks() {
	    return tasks.values();
	}




}