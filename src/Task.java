import java.time.format.DateTimeFormatter;

public class Task {
    private int id;
    private String username;
    private String title;
    private String description;
    private String addedDate;

    private String frequency;
    private String dueDate;
    private String tag;
    private String flag;
    private Boolean completed = false;

    public Task(int id, String username, String title, String description, String frequency, String dueDate, String tag, String flag, Boolean completed) {
        this.id = id;
        this.username = username;
        this.title = title;
        this.description = description;
        this.frequency = frequency;
        this.addedDate =
        this.dueDate = dueDate;
        this.tag = tag;
        this.flag = flag;
        this.completed = completed;
    }

    public Task(int id, String username, String title,  String description, String addedDate) {
        this.title = title;
        this.description = description;
        this.username = username;
        this.addedDate = addedDate;
    }

    public Task(int id, String username, String title, String description, String addedDate, String frequency, String dueDate, String tag, String flag, Boolean completed) {
        this.id = id;
        this.username = username;
        this.title = title;
        this.description = description;
        this.addedDate = addedDate;
        this.frequency = frequency;
        this.dueDate = dueDate;
        this.tag = tag;
        this.flag = flag;
        this.completed = completed;
    }

    public void printTaskAttributes() {
        System.out.println(this.title);
        System.out.println(this.username);
        System.out.println(this.description);
        System.out.println(this.addedDate);
        System.out.println(this.dueDate);
        System.out.println(this.tag);
        System.out.println(this.flag);
        System.out.println(this.completed);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUsername() {
        return username;
    }

    public String getFrequency() {
        return frequency;
    }

    public String getAddedDate() {
        return addedDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getTag() {
        return tag;
    }

    public String getFlag() {
        return flag;
    }

    public int getId() {
        return id;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
}
