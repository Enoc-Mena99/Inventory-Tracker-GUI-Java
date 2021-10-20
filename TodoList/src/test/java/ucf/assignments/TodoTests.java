package ucf.assignments;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import org.junit.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class TodoTests {
    // instance variable
    private static TodoController todoController;
    private static List<Todo> todos;

    @BeforeClass
    public static void initialize(){
        todoController = new TodoController();
        try {
            todos = ReadWriteFile.readFile();
        }catch (IOException | ClassNotFoundException e) {
            todos = new ArrayList<>();
        }
    }

    @Test
    public void addTodoMethod(){
        int size = todos.size();
        Todo todo = new Todo("Title1", "Description", "2021-12-2", false);
        Item item1 = new Item("Item1");
        Item item2 = new Item("Item2");
        ArrayList<Item> list = new ArrayList<>();
        list.add(item1);
        list.add(item2);
        todo.setItems(list);
        todos.add(todo);
        assertEquals(size + 1, todos.size());
    }

    @Test
    public void checkValidDateMethod(){
        // valid date format
        Todo todo = new Todo("Title1", "Description", "2021-12-2", false);
        assertEquals(true, todoController.isDateValid(todo.getDueDate()));
        // invalid date format
        Todo todo2 = new Todo("Title2", "Description", "202-12-2", false);
        assertEquals(false, todoController.isDateValid(todo2.getDueDate()));
    }

    @Test
    public void testUpdateMethod(){
        int idx = todos.size() - 1;
        Todo t = todos.get(idx);
        t.setDescription("This is new description");
        todos.set(idx, t);
        assertEquals("This is new description", todos.get(idx).getDescription());
    }

    @Test
    public void testDeleteTodo(){
        int size = todos.size();
        int idx = todos.size() - 1;
        todos.remove(idx);
        assertEquals(size - 1, todos.size());
    }
}
