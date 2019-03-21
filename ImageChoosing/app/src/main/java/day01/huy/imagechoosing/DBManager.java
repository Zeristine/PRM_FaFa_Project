package day01.huy.imagechoosing;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import day01.huy.imagechoosing.Models.Cele;
import day01.huy.imagechoosing.Models.HistoryDTO;


public class DBManager extends SQLiteOpenHelper {
    private Context mContext;
    public static final String DATABASE_NAME = "Cele_database";
    public static final String Cele_TABLE_NAME = "Cele_table";
    public static final String HISTORY_TABLE_NAME = "history_table";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String URL = "imgurl";

    public DBManager(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQuery = "CREATE TABLE \"Cele_table\" (\n" +
                "\t\"id\"\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t\"name\"\tTEXT NOT NULL,\n" +
                "\t\"description\"\tTEXT,\n" +
                "\t\"imgurl\"\tTEXT\n" +
                ")";
        db.execSQL(sqlQuery);
        sqlQuery = "CREATE TABLE \"history_table\" (\n" +
                "\t\"id\"\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t\"CeleId\"\tINTEGER NOT NULL,\n" +
                "\t\"search_date\"\tTEXT NOT NULL,\n" +
                "\tFOREIGN KEY(\"CeleId\") REFERENCES \"Cele_table\"(\"id\")\n" +
                ")";
        db.execSQL(sqlQuery);
        sqlQuery = "CREATE TABLE \"user_table\" (\n" +
                "\t\"id\"\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t\"username\"\tINTEGER NOT NULL,\n" +
                "\t\"password\"\tINTEGER NOT NULL\n" +
                ")";
        db.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    //Cele table
    public Cele getCele(String name) {
        Cele cele = null;
        SQLiteDatabase db = this.getReadableDatabase();
        /*
        String query = "select * from Cele_table where name = '"+name+"'";
        Cursor cursor = db.rawQuery(query, null);
        */
        String selection = "name = ?";
        String[] selectionArgs = new String[]{name};
        Cursor cursor = db.query(Cele_TABLE_NAME, null, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            cele = new Cele();
            cele.setId(cursor.getInt(0));
            cele.setName(cursor.getString(1));
            cele.setDescription(cursor.getString(2));
            cele.setUrl(cursor.getString(3));
        }
        cursor.close();
        db.close();
        return cele;
    }

    public List<Cele> getAllCele() {
        List<Cele> list = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        /*
        String selectQuery = "SELECT  * FROM " + Cele_TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        */
        Cursor cursor = db.query(Cele_TABLE_NAME, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Cele cele = new Cele();
                cele.setId(cursor.getInt(0));
                cele.setName(cursor.getString(1));
                cele.setDescription(cursor.getString(2));
                cele.setUrl(cursor.getString(3));
                list.add(cele);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    public boolean addCele(Cele cele) {
        boolean check = false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, cele.getName());
        values.put(DESCRIPTION, cele.getDescription());
        values.put(URL,cele.getUrl());
        check = db.insert(Cele_TABLE_NAME, null, values) > 0;

        db.close();
        return check;
    }

    //history table
    public List<HistoryDTO> getHistory() {
        List<HistoryDTO> list = new ArrayList<>();

        String query = "select Cele_table.name, history_table.search_date from Cele_table join history_table on Cele_table.id = history_table.CeleId ORDER by history_table.search_date DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {

                //get name
                String name = cursor.getString(0);
                //get search_date
                String search_date = cursor.getString(1);
                list.add(new HistoryDTO(name,search_date));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public boolean addHistory(Cele cele) {
        int celeId = cele.getId();
        //get current date
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String today = dateFormat.format(date);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("CeleId", celeId);
        values.put("search_date", today);

        boolean result = db.insert("history_table", null, values) > 0;
        db.close();
        return result;
    }

    //login table
    public boolean checkLogin(String username, String password) {
//        String query = "select * from user_table where username = bbb and password = bbb";
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = new String[]{"username", "password"};
        String whereClause = "username = ? and password = ?";
        String[] whereArgs = new String[]{username, password};
        Cursor cursor = db.query("user_table", columns, whereClause, whereArgs, null, null, null);

        boolean result = cursor.moveToFirst();
        cursor.close();
        db.close();
        return result;
    }

    public boolean registed(String username, String password) {
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        SQLiteDatabase db = this.getWritableDatabase();
        boolean result = db.insert("user_table", null, values) > 0;
        db.close();
        return result;
    }

}
