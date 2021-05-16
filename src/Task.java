public class Task {
    private String title;
    private String username;
    private String description;
    private String addedDate;
    private String dueDate;
    private String tag;
    private String flag;
    private Boolean completed = false;

    public Task(String username, String title,  String description, String addedDate) {
        this.title = title;
        this.description = description;
        this.username = username;
        this.addedDate = addedDate;
    }

    public Task(String username,
                String title,
                String description,
                String addedDate,
                String dueDate,
                String tag,
                String flag,
                boolean completed) {
        this.title = title;
        this.username = username;
        this.description = description;
        this.addedDate = addedDate;
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
}
