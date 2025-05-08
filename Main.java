import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();
        SwingUtilities.invokeLater(() -> {
            String[] columnNames = {"Title", "Priority", "Due Date", "Completed"};
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            JTable taskTable = new JTable(tableModel);
            taskTable.setRowHeight(24);
            taskTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            taskTable.getColumnModel().getColumn(0).setPreferredWidth(150); // Title
            taskTable.getColumnModel().getColumn(1).setPreferredWidth(80);  // Priority
            taskTable.getColumnModel().getColumn(2).setPreferredWidth(180); // Due Date
            taskTable.getColumnModel().getColumn(3).setPreferredWidth(80);
            JScrollPane tableScrollPane = new JScrollPane(taskTable);
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            taskTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

            // Add table to frame

            // Frame setup
            JFrame frame = new JFrame("Task Scheduler");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 450);
            frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
            frame.add(tableScrollPane);
            // --- Step 1: Title input ---
            JLabel titleLabel = new JLabel("Title:");
            JTextField titleField = new JTextField(20);
            JButton addButton = new JButton("Add Task");

            // Panel to group them
            JPanel inputPanel = new JPanel();
            inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));
            inputPanel.add(titleLabel);
            inputPanel.add(titleField);
            inputPanel.add(addButton);

            // Add panel to the frame
            frame.add(inputPanel);
            JButton markDoneButton = new JButton("Mark as Done");
            frame.add(markDoneButton);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            JLabel descLabel = new JLabel("Description:");
            JTextArea descArea = new JTextArea(3, 30);
            descArea.setLineWrap(true);
            descArea.setWrapStyleWord(true);

            JScrollPane descScrollPane = new JScrollPane(descArea);

            JPanel descPanel = new JPanel();
            descPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            descPanel.setLayout(new BoxLayout(descPanel, BoxLayout.Y_AXIS));
            descPanel.add(descLabel);
            descPanel.add(descScrollPane);

            frame.add(descPanel);

            JLabel priorityLabel = new JLabel("Priority");
            JComboBox<Priority> priorityBox = new JComboBox<>(Priority.values());

            JPanel priorityPanel = new JPanel();
            priorityPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            priorityPanel.setLayout(new BoxLayout(priorityPanel, BoxLayout.X_AXIS));
            priorityPanel.add(priorityLabel);
            priorityPanel.add(priorityBox);

            frame.add(priorityPanel);

            JLabel dueDateLabel = new JLabel("Due Date:");

            JSpinner dueDateSpinner = new JSpinner(new SpinnerDateModel());
            JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dueDateSpinner, "yyyy-MM-dd'T'HH:mm");
            dueDateSpinner.setEditor(dateEditor);
            dueDateSpinner.setValue(new Date());


            JPanel dueDatePanel = new JPanel();
            dueDatePanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            dueDatePanel.add(dueDateLabel);
            dueDatePanel.add(dueDateSpinner);

            frame.add(dueDatePanel);

            Date selectedDate = (Date) dueDateSpinner.getValue();
            LocalDateTime dueDate = selectedDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
            addButton.addActionListener(e -> {
                try {
                    String title = titleField.getText().trim();
                    String description = descArea.getText().trim();
                    Priority priority = (Priority) priorityBox.getSelectedItem();
                    
                    Date taskDate = (Date) dueDateSpinner.getValue();
                    LocalDateTime taskDueDate = taskDate.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();

                    // Create the task and add it
                    Task newTask = new Task(title, description, priority, taskDueDate, LocalDateTime.now());
                    manager.addTask(newTask);
                    tableModel.addRow(new Object[] {
                        title,
                        priority,
                        taskDueDate.toString(),
                        false  // not completed yet
                    });


                    JOptionPane.showMessageDialog(frame, "Task added successfully!");

                    // Optionally: Clear input fields
                    titleField.setText("");
                    descArea.setText("");
                    priorityBox.setSelectedIndex(0);
                    dueDateSpinner.setValue(new Date());

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Failed to add task: " + ex.getMessage());
                }
            });
            markDoneButton.addActionListener(e -> {
                int selectedRow = taskTable.getSelectedRow();

                if (selectedRow != -1) {
                    // Update the Completed column to true
                    tableModel.setValueAt(true, selectedRow, 3);  // column index 3 = "Completed"

                    // Optional: update your TaskManager if tracking by title or ID
                    String title = (String) tableModel.getValueAt(selectedRow, 0);
                    Task matchedTask = manager.getAllTasks().stream()
                        .filter(t -> t.getTitle().equals(title))
                        .findFirst().orElse(null);

                    if (matchedTask != null) {
                        matchedTask.markCompleted();  // if you stored ID somewhere
                    }

                } else {
                    JOptionPane.showMessageDialog(frame, "Select a task to mark as completed.");
                }
            });
            JButton deleteButton = new JButton("Delete");
            frame.add(deleteButton);
            deleteButton.addActionListener(e -> {
                int selectedRow = taskTable.getSelectedRow();

                if (selectedRow != -1) {
                    // Remove row from table
                    String title = (String) tableModel.getValueAt(selectedRow, 0);
                    tableModel.removeRow(selectedRow);

                    // Optionally remove from TaskManager
                    Task matchedTask = manager.getAllTasks().stream()
                        .filter(t -> t.getTitle().equals(title))
                        .findFirst().orElse(null);

                    if (matchedTask != null) {
                        manager.removeTask(matchedTask); // if using Option 2; otherwise use ID
                    }

                } else {
                    JOptionPane.showMessageDialog(frame, "Select a task to delete.");
                }
            });
            JButton saveButton = new JButton("Save");
            JButton loadButton = new JButton("Load");

            frame.add(saveButton);
            frame.add(loadButton);
            saveButton.addActionListener(e -> {
                try {
                    manager.saveToFile("tasks.dat");
                    JOptionPane.showMessageDialog(frame, "Tasks saved successfully.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Failed to save tasks: " + ex.getMessage());
                }
            });
            loadButton.addActionListener(e -> {
                try {
                    manager.loadFromFile("tasks.dat");

                    // Clear table
                    tableModel.setRowCount(0);

                    // Re-populate from loaded tasks
                    for (Task task : manager.getAllTasks()) {
                        tableModel.addRow(new Object[] {
                            task.getTitle(),
                            task.getPriority(),
                            task.getDueDate().toString(),
                            task.isCompleted()
                        });
                    }

                    JOptionPane.showMessageDialog(frame, "Tasks loaded successfully.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Failed to load tasks: " + ex.getMessage());
                }
            });



        });
        
    }
}
