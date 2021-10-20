package ucf.assignments;

import java.io.Serializable;

public class Item implements Serializable {
    // instance variables
    private String name;

    /**
     * constructor
     * @param name
     */
    public Item(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return  name;
    }
}
