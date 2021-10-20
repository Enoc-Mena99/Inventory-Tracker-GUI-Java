package ucf.assignments;

import java.io.Serializable;
import java.util.ArrayList;



public class Todo implements Serializable {
    // instance variables
    private String title;
    private String description;
    private String dueDate;
    private boolean completed;
    private ArrayList<Item> items;

    /**
     * Constructor
     * @param title
     * @param description
     * @param dueDate
     */
    public Todo(String title, String description, String dueDate, boolean completed) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.items = new ArrayList<>();
        this.completed = completed;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        for (Item i : items){
            this.items.add(i);
        }
    }

    @Override
    public String toString() {
        return  title;
    }
}
