package ucf.assignments;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TodoController implements Initializable {
    // instance variables
    public ListView<Todo> allTodoList;
    public CheckBox chboxCompleted;
    public TextField titleTF;
    public TextField dateTF;
    public TextArea descTA;
    public TextField itemTF;
    public Button btnAddItem;
    public ListView<Item> itemList;
    public Button btnAddTodo;
    public Button btnUpdateTodo;
    public Button btnDeleteTodo;
    public ComboBox<String> comboTodos;
    private Alert alert;
    public List<Todo> todos;
    private ArrayList<Item> items;
    private int index = -1;
    private Todo todo = null;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> choice = FXCollections.observableArrayList(Arrays.asList("Completed", "Uncompleted"));
        comboTodos.setItems(choice);
        items = new ArrayList<>();
        alert = new Alert(Alert.AlertType.INFORMATION);
        // read todos from an external storage or file
        try {
            todos = ReadWriteFile.readFile();
            ObservableList<Todo> todoObservableList;
            todoObservableList = FXCollections.observableList(todos);
            allTodoList.setItems(todoObservableList);
        } catch (IOException | ClassNotFoundException e) {
            todos = new ArrayList<>();
        }

        allTodoList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            todo = allTodoList.getSelectionModel().getSelectedItem();
            index = allTodoList.getSelectionModel().getSelectedIndex();
            editTodo(todo);
            items = todo.getItems();
            titleTF.setEditable(false);
            btnAddTodo.setDisable(true);
            btnUpdateTodo.setDisable(false);
            btnDeleteTodo.setDisable(false);
        });


        itemList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            int idx = itemList.getSelectionModel().getSelectedIndex();
            items.remove(idx);
            ObservableList<Item> list = FXCollections.observableList(items);
            itemList.setItems(list);
        });

        comboTodos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            String choice1 = comboTodos.getSelectionModel().getSelectedItem();
            populateTodosList(choice1);
        });
    }


    public void handleAddTodo(ActionEvent actionEvent) {
        // get users input
        String title = titleTF.getText();
        String desc = descTA.getText();
        String date = dateTF.getText();
        boolean completed = chboxCompleted.isSelected();
        // check that all fields contain data
        if (title.isEmpty() || desc.isEmpty() || date.isEmpty()) {
            showMessage("Please fill all the fields");
            return;
        }
        // check that this mTodo does not exists
        if (!todoExists(title)) {
            // validate date
            if (!isDateValid(date)) {
                showMessage("Invalid date format");
                return;
            }

            Todo todo = new Todo(title, desc, date, completed);
            todo.setItems(items);
            addTodo(todo);

        } else {
            showMessage("This todo already exists");
        }

    }

    public void addTodo(Todo todo) {
        todos.add(todo);
        showMessage("Todo was added successfully");
        clearFields();
        saveTodo();
    }

    public void handleUpdateTodo(ActionEvent actionEvent) {
        // get users input
        String title = titleTF.getText();
        String desc = descTA.getText();
        String date = dateTF.getText();
        boolean completed = chboxCompleted.isSelected();
        // check that all fields contain data
        if (title.isEmpty() || desc.isEmpty() || date.isEmpty()) {
            showMessage("Please fill all the fields");
            return;
        }

        // validate date
        if (!isDateValid(date)) {
            showMessage("Invalid date format");
            return;
        }

        Todo todo = new Todo(title, desc, date, completed);
        todo.setItems(items);
        updateTodo(todo);
    }

    public void updateTodo(Todo todo) {
        todos.set(index, todo);
        showMessage("Todo was updated successfully");
        clearFields();
        saveTodo();
        btnAddTodo.setDisable(false);
        btnUpdateTodo.setDisable(true);
        btnDeleteTodo.setDisable(true);
        titleTF.setEditable(true);
    }

    public void handleDeleteTodo(ActionEvent actionEvent) {
        deleteTodo();
        clearFields();
    }

    public void handleNewTodo(ActionEvent actionEvent) {
        btnAddTodo.setDisable(false);
        btnUpdateTodo.setDisable(true);
        btnDeleteTodo.setDisable(true);
        titleTF.setEditable(true);
        clearFields();
    }

    public void addTodoItem(ActionEvent actionEvent) {
        // get item
        String item = itemTF.getText();
        if (item.isEmpty()) {
            showMessage("Item is required");
            return;
        }

        if (!itemExists(item)) {
            // add item to array
            items.add(new Item(item));
            // update item list
            updateItemList();
            // delete item
            itemTF.setText("");
        } else {
            showMessage("This item already exists. Try again");
        }
    }

    private void clearFields() {
        // clear
        titleTF.setText("");
        dateTF.setText("");
        descTA.setText("");
        items.clear();
        itemList.setItems(null);
        chboxCompleted.setSelected(false);
    }

    private boolean itemExists(String name) {
        boolean status = false;
        for (Item t : items) {
            if (t.getName().equalsIgnoreCase(name)) {
                status = true;
                break;
            }
        }

        return status;
    }

    private void updateItemList() {
        ObservableList<Item> observableList = FXCollections.observableList(items);
        itemList.setItems(observableList);
    }

    private void editTodo(Todo todo) {
        if (todo != null) {
            titleTF.setText(todo.getTitle());
            dateTF.setText(todo.getDueDate());
            descTA.setText(todo.getDescription());
            chboxCompleted.setSelected(todo.isCompleted());
            ObservableList<Item> list = FXCollections.observableList(todo.getItems());
            itemList.setItems(list);
        }
    }

    private void deleteTodo() {
        if (index != -1) {
            todos.remove(index);
            showMessage("Todo was deleted successfully");
            index = -1;
            saveTodo();
            btnAddTodo.setDisable(false);
            btnUpdateTodo.setDisable(true);
            btnDeleteTodo.setDisable(true);
            titleTF.setEditable(true);
        } else {
            showMessage("You have not selected any Todo");
        }
    }

    private void populateTodosList(String choice) {
        switch (choice.toLowerCase()) {
            case "completed":
                List<Todo> list = new ArrayList<>();
                for (Todo t : todos) {
                    if (t.isCompleted()) {
                        list.add(t);
                    }
                }
                ObservableList<Todo> todoObservableList = FXCollections.observableList(list);
                allTodoList.setItems(todoObservableList);
                break;
            case "uncompleted":
                List<Todo> list2 = new ArrayList<>();
                for (Todo t : todos) {
                    if (!t.isCompleted()) {
                        list2.add(t);
                    }
                }
                ObservableList<Todo> todoObservableList2 = FXCollections.observableList(list2);
                allTodoList.setItems(todoObservableList2);
                break;
            default:
                ObservableList<Todo> todoObservableList3 = FXCollections.observableList(todos);
                allTodoList.setItems(todoObservableList3);
                break;
        }
    }


    private void saveTodo() {
        try {
            ReadWriteFile.writeToFile(todos);
            populateTodosList(comboTodos.getSelectionModel().getSelectedItem());
            index = -1;
            clearFields();
        } catch (IOException e) {
            showMessage("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Check if such mTodo already exists in the list
     *
     * @param title
     * @return true id exists, otherwise false
     */
    private boolean todoExists(String title) {
        boolean status = false;
        for (Todo t : todos) {
            if (t.getTitle().equalsIgnoreCase(title)) {
                status = true;
                break;
            }
        }

        return status;
    }

    /**
     * Validate date format: YYYY-MM-DD
     *
     * @param date
     * @return true id valid, otherwise false
     */
    public boolean isDateValid(String date) {
        String regex = "[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(date);
        return m.matches();
    }

    /**
     * Show alert with the given message passed as parameter
     *
     * @param msg
     */
    private void showMessage(String msg) {
        alert.setContentText(msg);
        alert.showAndWait();
    }


}
