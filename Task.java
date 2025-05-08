import java.io.Serializable;
import java.time.LocalDateTime;


public class Task implements Serializable{
	private	String title;
	private	String description;
	private	Priority priority;
	private	LocalDateTime dueDate;
	private	LocalDateTime createdAt;
	private	boolean isCompleted;

	public Task(String title, String description, Priority priority, LocalDateTime dueDate, LocalDateTime isCompleted){
		this.title = title;
		this.description = description;
		this.priority = priority;
		this.dueDate = dueDate;
		this.createdAt = LocalDateTime.now();
		this.isCompleted = false;
	}

	public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Priority getPriority() { return priority; }
    public LocalDateTime getDueDate() { return dueDate; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public boolean isCompleted() { return isCompleted; }

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setPriority(Priority priority) { this.priority = priority; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }
    public void markCompleted() { this.isCompleted = true; }

    public int compareTo(Task other){
    	int dateCompare = this.dueDate.compareTo(other.dueDate);
    	if (dateCompare != 0) return dateCompare;
    	return this.priority.compareTo(other.priority);
    	
    }
    public String toString() {
        return String.format(
            "[%s] %s (Priority: %s, Due: %s)%s",
            isCompleted ? "✓" : "✗",
            title,
            priority,
            dueDate,
            (description.isEmpty() ? "" : "\n  Description: " + description)
        );
    }
    
}