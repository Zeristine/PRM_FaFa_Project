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

import day01.huy.imagechoosing.Models.Celebrity;


public class DBManager extends SQLiteOpenHelper {
    private Context mContext;
    public static final String DATABASE_NAME ="celebrity_database";
    public static final String CELEBRITY_TABLE_NAME ="celebrity_table";
    public static final String HISTORY_TABLE_NAME ="history_table";
    public static final String ID ="id";
    public static final String NAME ="name";
    public static final String DESCRIPTION ="description";

    public DBManager(Context context){
        super(context, DATABASE_NAME, null, 1);
        this.mContext= context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQuery = "CREATE TABLE \"celebrity_table\" (\n" +
                "\t\"id\"\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t\"name\"\tINTEGER NOT NULL,\n" +
                "\t\"description\"\tINTEGER\n" +
                ")";
        db.execSQL(sqlQuery);
        sqlQuery = "CREATE TABLE \"history_table\" (\n" +
                "\t\"id\"\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t\"celebrityId\"\tINTEGER NOT NULL,\n" +
                "\t\"search_date\"\tTEXT NOT NULL,\n" +
                "\tFOREIGN KEY(\"celebrityId\") REFERENCES \"celebrity_table\"(\"id\")\n" +
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


    //celebrity table
    public Celebrity getCelebrity(String name) {
        Celebrity celebrity = null;
        SQLiteDatabase db = this.getReadableDatabase();
        /*
        String query = "select * from celebrity_table where name = '"+name+"'";
        Cursor cursor = db.rawQuery(query, null);
        */
        String selection = "name = ?";
        String[] selectionArgs = new String[]{name};
        Cursor cursor = db.query(CELEBRITY_TABLE_NAME, null, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            celebrity = new Celebrity();
            celebrity.setId(cursor.getInt(0));
            celebrity.setName(cursor.getString(1));
            celebrity.setDescription(cursor.getString(2));
        }
        cursor.close();
        db.close();
        return celebrity;
    }

    public List<Celebrity> getAllCelebrity() {
        List<Celebrity> list = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        /*
        String selectQuery = "SELECT  * FROM " + CELEBRITY_TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        */
        Cursor cursor = db.query(CELEBRITY_TABLE_NAME, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Celebrity celebrity = new Celebrity();
                celebrity.setId(cursor.getInt(0));
                celebrity.setName(cursor.getString(1));
                celebrity.setDescription(cursor.getString(2));
                list.add(celebrity);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    public boolean addCelebrity(Celebrity celebrity){
        boolean check= false;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, celebrity.getName());
        values.put(DESCRIPTION, celebrity.getDescription());
        check= db.insert(CELEBRITY_TABLE_NAME,null,values) >0;
        db.close();
        return check;
    }
    //history table
    public List<String[]> getHistory() {
        List<String[]> list = new ArrayList<>();
        String[] str = new String[2];
        String query = "select * from celebrity_table join history_table on celebrity_table.id = history_table.celebrityId ORDER by history_table.search_date DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                //get name
                str[0] = cursor.getString(1);
                //get search_date
                str[1] = cursor.getString(5);
                list.add(str);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public boolean addHistory(Celebrity celebrity) {
        int celebrityId = celebrity.getId();
        //get current date
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String today = dateFormat.format(date);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("celebrityId", celebrityId);
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
