package se.rob90.example.myapplication.Model;

import se.rob90.example.myapplication.MessageActivity;

public class DatabaseHelper {

    private int id;
    private String Message;

    public DatabaseHelper(int id, String message) {
        this.id = id;
        this.Message = message;
    }

    @Override
    public String toString() {
        return "DatabaseHelper{" +
                "id=" + id +
                ", Message='" + Message + '\'' +
                '}';
    }

    public DatabaseHelper() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
