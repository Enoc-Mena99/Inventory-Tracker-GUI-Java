package ucf.assignments;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReadWriteFile {
    private static final String FILENAME = "/data.txt";

    /**
     * @return array list containing the data retrieved from the file
     * @throws ClassNotFoundException Reads from file - data.txt
     * @throws
     */
    public static ArrayList<Todo> readFile() throws IOException, ClassNotFoundException {
        ArrayList<Todo> data = null;

        FileInputStream readData = new FileInputStream(String.valueOf(ReadWriteFile.class.getResource(FILENAME)));
        ObjectInputStream readStream = new ObjectInputStream(readData);

        data = (ArrayList<Todo>) readStream.readObject();
        readStream.close();
        return data;
    }

    /**
     * Appends data to file - data.txt Creates the file if it does not exists
     *
     * @param content
     */
    public static void writeToFile(List<Todo> content) throws IOException {
        FileOutputStream writeData = new FileOutputStream(String.valueOf(ReadWriteFile.class.getResource(FILENAME)));
        ObjectOutputStream writeStream = new ObjectOutputStream(writeData);

        writeStream.writeObject(content);
        writeStream.flush();
        writeStream.close();
        System.out.println("Data was saved to " + FILENAME + " successfully!");
    }
}
