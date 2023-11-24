import java.time.LocalDateTime;
import java.util.*;

class Task {
    private String taskId;
    private String taskName;
    private LocalDateTime deadline;
    private List<String> tags;
    private boolean completed;

    public Task(String taskId, String taskName, LocalDateTime deadline, List<String> tags) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.deadline = deadline;
        this.tags = tags;
        this.completed = false;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public List<String> getTags() {
        return tags;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setDeadline(LocalDateTime plusDays) {
    }
}

class TodoList {
    private Map<String, Task> tasks;
    private List<String> activityLog;

    public TodoList() {
        this.tasks = new HashMap<>();
        this.activityLog = new ArrayList<>();
    }

    public void addTask(Task task) {
        tasks.put(task.getTaskId(), task);
        activityLog.add("Task added: " + task.getTaskName());
    }

    public Task getTask(String taskId) {
        return tasks.get(taskId);
    }

    public void modifyTask(Task task) {
        tasks.put(task.getTaskId(), task);
        activityLog.add("Task modified: " + task.getTaskName());
    }

    public void removeTask(String taskId) {
        Task task = tasks.remove(taskId);
        if (task != null) {
            activityLog.add("Task removed: " + task.getTaskName());
        }
    }

    public List<Task> listTasks(String filter, Comparator<Task> sortCriteria) {
        List<Task> filteredTasks = new ArrayList<>();

        for (Task task : tasks.values()) {
            if (task.getTaskName().contains(filter)) {
                filteredTasks.add(task);
            }
        }

        filteredTasks.sort(sortCriteria);
        return filteredTasks;
    }

    public Map<String, Integer> getStatistics(LocalDateTime startTime, LocalDateTime endTime) {
        Map<String, Integer> statistics = new HashMap<>();

        int tasksAdded = 0;
        int tasksCompleted = 0;
        int tasksOverdue = 0;

        for (Task task : tasks.values()) {
            LocalDateTime deadline = task.getDeadline();
            if (deadline.isAfter(startTime) && deadline.isBefore(endTime)) {
                tasksAdded++;

                if (task.isCompleted()) {
                    tasksCompleted++;
                }

                if (deadline.isBefore(LocalDateTime.now()) && !task.isCompleted()) {
                    tasksOverdue++;
                }
            }
        }

        statistics.put("TasksAdded", tasksAdded);
        statistics.put("TasksCompleted", tasksCompleted);
        statistics.put("TasksOverdue", tasksOverdue);

        return statistics;
    }

    public List<String> getActivityLog(LocalDateTime startTime, LocalDateTime endTime) {
        List<String> filteredActivityLog = new ArrayList<>();

        for (String logEntry : activityLog) {
            LocalDateTime logTime = LocalDateTime.parse(logEntry.substring(0, 19));
            if (logTime.isAfter(startTime) && logTime.isBefore(endTime)) {
                filteredActivityLog.add(logEntry);
            }
        }

        return filteredActivityLog;
    }
}

public class TodoApp {
    public static void main(String[] args) {
        TodoList todoList = new TodoList();

        // Creating tasks
        Task task1 = new Task("1", "Task 1", LocalDateTime.now().plusDays(2), Arrays.asList("Tag1", "Tag2"));
        Task task2 = new Task("2", "Task 2", LocalDateTime.now().plusDays(1), Arrays.asList("Tag2", "Tag3"));
        Task task3 = new Task("3", "Task 3", LocalDateTime.now().plusDays(3), Arrays.asList("Tag1", "Tag3"));

        // Adding tasks to the todo list
        todoList.addTask(task1);
        todoList.addTask(task2);
        todoList.addTask(task3);

        // Completing a task
        Task taskToComplete = todoList.getTask("1");
        taskToComplete.setCompleted(true);

        // Modifying a task
        Task taskToModify = todoList.getTask("2");
        taskToModify.setDeadline(LocalDateTime.now().plusDays(2));

        // Removing a task
        todoList.removeTask("3");

        // Listing tasks
        List<Task> filteredTasks = todoList.listTasks("Task", Comparator.comparing(Task::getDeadline));
        for (Task task : filteredTasks) {
            System.out.println(task.getTaskName());
        }

        // Getting statistics
        LocalDateTime startTime = LocalDateTime.now().minusDays(1);
        LocalDateTime endTime = LocalDateTime.now();
        Map<String, Integer> statistics = todoList.getStatistics(startTime, endTime);
        System.out.println("Tasks Added: " + statistics.get("TasksAdded"));
        System.out.println("Tasks Completed: " + statistics.get("TasksCompleted"));
        System.out.println("Tasks Overdue: " + statistics.get("TasksOverdue"));

        // Getting activity log
        List<String> activityLog = todoList.getActivityLog(startTime, endTime);
        for (String logEntry : activityLog) {
            System.out.println(logEntry);
        }
    }
}