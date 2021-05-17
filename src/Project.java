public class Project {

    private int id;
    private String username;
    private String title;
    private String description;
    private String addedDate;
    private String dueDate;
    private String tag;
    private String flag;
    private Boolean completed = false;

    public Project(int id,
                   String username,
                   String title,
                   String description,
                   String addedDate,
                   String dueDate,
                   String tag,
                   String flag,
                   Boolean completed) {
        this.id = id;
        this.username = username;
        this.title = title;
        this.description = description;
        this.addedDate = addedDate;
        this.dueDate = dueDate;
        this.tag = tag;
        this.flag = flag;
        this.completed = completed;
    }

    public Project(int id,
                   String username,
                   String title,
                   String description,
                   String addedDate) {
        this.title = title;
        this.description = description;
        this.username = username;
        this.addedDate = addedDate;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
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

    public Boolean getCompleted() {
        return completed;
    }
}
