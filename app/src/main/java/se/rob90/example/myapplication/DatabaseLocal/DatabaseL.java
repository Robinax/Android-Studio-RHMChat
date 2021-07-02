package se.rob90.example.myapplication.DatabaseLocal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import se.rob90.example.myapplication.MessageActivity;
import se.rob90.example.myapplication.Model.DatabaseHelper;
import se.rob90.example.myapplication.Stored_Chats;

public class DatabaseL extends SQLiteOpenHelper {
    public DatabaseL(@Nullable Context context) {
        super(context, "Stored.Chats", null, 1);
    }
        //Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement ="CREATE TABLE STORED_CHATS (ID INTEGER PRIMARY KEY AUTOINCREMENT, MESSAGES TEXT)";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public boolean addOne(DatabaseHelper databaseHelper){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("MESSAGES", databaseHelper.getMessage());

        long insert = db.insert("STORED_CHATS", null, cv);
        if (insert == -1)
        {
            return false;
        }
        else{
            return true;
        }


    }

    public boolean deleteOne(DatabaseHelper databaseHelper){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM STORED_CHATS WHERE ID = "+ databaseHelper.getId();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()){
            return true;
        }else {
            return false;
        }

    }
    public List<DatabaseHelper> getAll(){
        List<DatabaseHelper> returnlist= new ArrayList<>();

        String queryString = "SELECT * FROM STORED_CHATS";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString,null);

        if (cursor.moveToFirst()){
            do {
                int messageID = cursor.getInt(0);
                String message = cursor.getString(1);

                DatabaseHelper databaseHelper = new DatabaseHelper(messageID,message);
                returnlist.add(databaseHelper);

            }while (cursor.moveToNext());

        }
        else{

        }
        cursor.close();
        db.close();
        return returnlist;
    }
}
