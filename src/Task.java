public class Task {
    private String title;
    private String username;
    private String description;
    private String addedDate;
    private String dueDate;
    private String tag;
    private String flag;
    private Boolean completed;

    public Task(String title, String description, String username, String addedDate) {
        this.title = title;
        this.description = description;
        this.username = username;
        this.addedDate = addedDate;
    }

    public Task(String title,
                String username,
                String description,
                String addedDate,
                String dueDate,
                String tag,
                String flag,
                Boolean completed) {
        this.title = title;
        this.username = username;
        this.description = description;
        this.addedDate = addedDate;
        this.dueDate = dueDate;
        this.tag = tag;
        this.flag = flag;
        this.completed = completed;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
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
}
