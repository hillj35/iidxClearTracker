package com.example.jordan.assignment1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jordan on 4/22/2017.
 */

public class databaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "iidx.db";
    private static Context c;
    private static databaseHelper instance;
    public static SQLiteDatabase dbInstance;

    public static synchronized  databaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new databaseHelper(context);
            dbInstance = instance.getWritableDatabase();
        }
        return instance;
    }

    private databaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        c = context;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(iidxContract.SQL_CREATE_SONG);
        db.execSQL(iidxContract.SQL_CREATE_GOAL);
        db.execSQL(iidxContract.SQL_CREATE_GOALITEM);
        db.execSQL(iidxContract.SQL_CREATE_PLAYER);
        ClearTracker.populateDatabase(c, db, R.array.songs);
    }

    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
        /*db.execSQL(iidxContract.SQL_DELETE_SONG);
        db.execSQL(iidxContract.SQL_DELETE_GOAL);
        db.execSQL(iidxContract.SQL_DELETE_GOALITEM);
        db.execSQL(iidxContract.SQL_DELETE_PLAYER);
        onCreate(db);*/
        //add new songs
        ClearTracker.populateDatabase(c, db, R.array.newSongs);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public static void reset() {
        dbInstance.execSQL(iidxContract.SQL_DELETE_GOAL);
        dbInstance.execSQL(iidxContract.SQL_DELETE_GOALITEM);
        dbInstance.execSQL(iidxContract.SQL_DELETE_PLAYER);

        dbInstance.execSQL(iidxContract.SQL_CREATE_GOAL);
        dbInstance.execSQL(iidxContract.SQL_CREATE_GOALITEM);
        dbInstance.execSQL(iidxContract.SQL_CREATE_PLAYER);

        ContentValues values = new ContentValues();
        values.put(iidxContract.songEntry.COLUMN_NAME_CLEAR, 0);

        String selection = "1=1";
        String[] selectionArgs = {};

        int count = dbInstance.update(iidxContract.songEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    public static void addGoalItem(String songName, String difficulty, String listName) {
        ContentValues values = new ContentValues();
        values.put(iidxContract.goalItemEntry.COLUMN_NAME_SONG, songName);
        values.put(iidxContract.goalItemEntry.COLUMN_NAME_DIFFICULTY, difficulty);
        values.put(iidxContract.goalItemEntry.COLUMN_NAME_LIST, listName);
        dbInstance.insert(iidxContract.goalItemEntry.TABLE_NAME, null, values);
    }

    public static void deleteGoalItem(String songName, String difficulty, String listName) {
        String where = iidxContract.goalItemEntry.COLUMN_NAME_SONG + "=? AND " +
                iidxContract.goalItemEntry.COLUMN_NAME_DIFFICULTY + "=" + difficulty +
                 " AND " + iidxContract.goalItemEntry.COLUMN_NAME_LIST + "=?";
        String[] args = {songName, listName};
        dbInstance.delete(iidxContract.goalItemEntry.TABLE_NAME, where, args);
    }

    public static Cursor searchSongs(String version, String level, Boolean normal, Boolean hyper, Boolean another, String match) {
        String[] projection = new String[4];
        projection[0] = iidxContract.songEntry.COLUMN_NAME_SONGNAME;
        projection[1] = iidxContract.songEntry.COLUMN_NAME_CLEAR;
        projection[2] = iidxContract.songEntry.COLUMN_NAME_LEVEL;
        projection[3] = iidxContract.songEntry.COLUMN_NAME_DIFFICULTY;

        String versionSelection = "1=1";
        if (!version.equals("ALL"))
            versionSelection = iidxContract.songEntry.COLUMN_NAME_VERSION + " ='" + version + "'";

        String levelSelection = "1=1";
        if (!level.equals("ALL"))
            levelSelection = iidxContract.songEntry.COLUMN_NAME_LEVEL + " ='" + level + "'";

        String difficultySelection = "1=1";
        if (normal && !hyper && !another)
            difficultySelection = iidxContract.songEntry.COLUMN_NAME_DIFFICULTY + "=0";
        else if (normal && hyper && !another)
            difficultySelection = "(" + iidxContract.songEntry.COLUMN_NAME_DIFFICULTY + "=0 OR " + iidxContract.songEntry.COLUMN_NAME_DIFFICULTY + "=1)";
        else if (!normal && hyper && !another)
            difficultySelection = iidxContract.songEntry.COLUMN_NAME_DIFFICULTY + "=1";
        else if (!normal && hyper && another)
            difficultySelection = "(" + iidxContract.songEntry.COLUMN_NAME_DIFFICULTY + "=1 OR " + iidxContract.songEntry.COLUMN_NAME_DIFFICULTY + "=2)";
        else if (!normal && !hyper && another)
            difficultySelection = iidxContract.songEntry.COLUMN_NAME_DIFFICULTY + "=2";
        else if (normal && !hyper && another)
            difficultySelection = "(" + iidxContract.songEntry.COLUMN_NAME_DIFFICULTY + "=0 OR " + iidxContract.songEntry.COLUMN_NAME_DIFFICULTY + "=2)";
        else if (!normal && !hyper && !another)
            difficultySelection = iidxContract.songEntry.COLUMN_NAME_DIFFICULTY + "=3";

        String[] selectionArgs;

        String nameSelection = "1=1";
        if (!match.equals("")) {
            nameSelection = "(" + iidxContract.songEntry.COLUMN_NAME_SONGNAME + " LIKE ?" +
                    " OR " + iidxContract.songEntry.COLUMN_NAME_SONGNAME + " LIKE ?" +
                    " OR " + iidxContract.songEntry.COLUMN_NAME_SONGNAME + " LIKE ?" +
                    " OR " + iidxContract.songEntry.COLUMN_NAME_SONGNAME + " LIKE ?)";

            selectionArgs = new String[]{match + "%", "%" + match, "%" + match + "%", match};
        }
        else
            selectionArgs = new String[]{};

        String selection = versionSelection +
                " AND " + levelSelection +
                " AND " + difficultySelection +
                " AND " + nameSelection;




        String sortOrder = iidxContract.songEntry.COLUMN_NAME_SONGNAME + " COLLATE NOCASE ASC";
        Cursor cursor = dbInstance.query(iidxContract.songEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

         return cursor;
    }

    public static void updateSongClear(String songName, int difficulty, int newClear) {
        ContentValues values = new ContentValues();
        values.put(iidxContract.songEntry.COLUMN_NAME_CLEAR, newClear);

        String selection = iidxContract.songEntry.COLUMN_NAME_SONGNAME + " LIKE ? AND " + iidxContract.songEntry.COLUMN_NAME_DIFFICULTY + " = ?";
        String[] selectionArgs = {songName, Integer.toString(difficulty)};

        int count = dbInstance.update(iidxContract.songEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    public static void addList(String name) {
        ContentValues values = new ContentValues();
        values.put(iidxContract.goalEntry.COLUMN_NAME_NAME, name);
        values.put(iidxContract.goalEntry.COLUMN_NAME_PLAYER, "Player");
        dbInstance.insert(iidxContract.goalEntry.TABLE_NAME, null, values);
    }

    public static void deleteList(String name) {
        Cursor cursor = getSongsFromGoalList(name, 0);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            deleteGoalItem(cursor.getString(0), Integer.toString(cursor.getInt(3)), name);
            cursor.moveToNext();
        }

        String where = iidxContract.goalEntry.COLUMN_NAME_NAME + "=?";
        String[] args = new String[] {name};
        dbInstance.delete(iidxContract.goalEntry.TABLE_NAME, where, args);
    }

    public static boolean isInList(String songName, int difficulty, String listName) {
        String query = "SELECT " + iidxContract.songEntry.COLUMN_NAME_SONGNAME +
                 " FROM " + iidxContract.goalItemEntry.TABLE_NAME +
                " a INNER JOIN " + iidxContract.songEntry.TABLE_NAME + " b ON " +
                "a." + iidxContract.goalItemEntry.COLUMN_NAME_SONG + "=b." +
                iidxContract.songEntry.COLUMN_NAME_SONGNAME + " AND a." +
                iidxContract.goalItemEntry.COLUMN_NAME_DIFFICULTY + "=b." +
                iidxContract.songEntry.COLUMN_NAME_DIFFICULTY  + " WHERE " +
                "a." + iidxContract.goalItemEntry.COLUMN_NAME_LIST + "=?" +
                " AND a." + iidxContract.goalItemEntry.COLUMN_NAME_SONG + "=?" +
                " AND a." + iidxContract.goalItemEntry.COLUMN_NAME_DIFFICULTY+ "=" + difficulty;

        String[] args = {listName, songName};
        Cursor cursor = dbInstance.rawQuery(query, args);
        if (cursor.getCount() > 0)
            return true;
        cursor.close();
        return false;
    }

    public static void updateListSpinner(Spinner listSpinner, Context context) {
        String[] projection = {iidxContract.goalEntry.COLUMN_NAME_NAME};
        String sortOrder = iidxContract.goalEntry.COLUMN_NAME_NAME + " ASC";
        Cursor cursor = dbInstance.query(iidxContract.goalEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder);

        ArrayList<String> listNames = new ArrayList<String>();
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            listNames.add(cursor.getString(0));
            cursor.moveToNext();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_list_item_1, listNames);

        listSpinner.setAdapter(adapter);
    }

    public static String[] getLists() {
        String[] projection = {iidxContract.goalEntry.COLUMN_NAME_NAME};
        String sortOrder = iidxContract.goalEntry.COLUMN_NAME_NAME + " COLLATE NOCASE ASC";
        Cursor cursor = dbInstance.query(iidxContract.goalEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder);
        ArrayList<String> listNames = new ArrayList<String>();
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            listNames.add(cursor.getString(0));
            cursor.moveToNext();
        }
        return listNames.toArray(new String[listNames.size()]);
    }

    public static Cursor getSongsFromGoalList(String listName, int sort) {
        String sortValues = "";
        switch (sort) {
            case 0:
                break;
            case 1:
                sortValues = "b." + iidxContract.songEntry.COLUMN_NAME_LEVEL + " ASC,";
                break;
            case 2:
                sortValues = "b." + iidxContract.songEntry.COLUMN_NAME_CLEAR + iidxContract.songEntry.COLUMN_NAME_LEVEL + " ASC,";
                break;
            default:
                break;
        }

        //select all songs in list
        String query = "SELECT " + iidxContract.songEntry.COLUMN_NAME_SONGNAME + ", " +
                iidxContract.songEntry.COLUMN_NAME_CLEAR + ", " +
                iidxContract.songEntry.COLUMN_NAME_LEVEL + ", b." +
                iidxContract.songEntry.COLUMN_NAME_DIFFICULTY + " FROM " + iidxContract.goalItemEntry.TABLE_NAME +
                " a INNER JOIN " + iidxContract.songEntry.TABLE_NAME + " b ON " +
                "a." + iidxContract.goalItemEntry.COLUMN_NAME_SONG + "=b." +
                iidxContract.songEntry.COLUMN_NAME_SONGNAME + " AND a." +
                iidxContract.goalItemEntry.COLUMN_NAME_DIFFICULTY + "=b." +
                iidxContract.songEntry.COLUMN_NAME_DIFFICULTY  + " WHERE " +
                "a." + iidxContract.goalItemEntry.COLUMN_NAME_LIST + "='" + listName + "' ORDER BY " + sortValues +
                " a." + iidxContract.goalItemEntry.COLUMN_NAME_SONG + " COLLATE NOCASE ASC";

        Cursor cursor = dbInstance.rawQuery(query, null);
        return cursor;
    }

    public static String getClearCount(Cursor cursor) {
        int total = cursor.getCount();
        int cleared = 0;

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (cursor.getInt(1) > 1) {
                cleared++;
            }
            cursor.moveToNext();
        }

        return Integer.toString(cleared) + "/" + Integer.toString(total);
    }

    public static Cursor getSongsFromVersion(String version, int difficulty, int sort) {
        String sortValues = "";
        switch (sort) {
            case 0:
                break;
            case 1:
                sortValues = iidxContract.songEntry.COLUMN_NAME_LEVEL + " ASC,";
                break;
            case 2:
                sortValues = iidxContract.songEntry.COLUMN_NAME_CLEAR + " ASC," + iidxContract.songEntry.COLUMN_NAME_LEVEL + " ASC,";
                break;
            default:
                break;
        }
        String[] projection = new String[4];
        projection[0] = iidxContract.songEntry.COLUMN_NAME_SONGNAME;
        projection[1] = iidxContract.songEntry.COLUMN_NAME_CLEAR;
        projection[2] = iidxContract.songEntry.COLUMN_NAME_LEVEL;
        projection[3] = iidxContract.songEntry.COLUMN_NAME_DIFFICULTY;

        String selection = iidxContract.songEntry.COLUMN_NAME_VERSION + " = ? AND " +
                iidxContract.songEntry.COLUMN_NAME_DIFFICULTY + " = ?";
        String[] selectionArgs = {version, Integer.toString(difficulty)};
        String sortOrder = sortValues + " " + iidxContract.songEntry.COLUMN_NAME_SONGNAME + " COLLATE NOCASE ASC";
        Cursor cursor = dbInstance.query(iidxContract.songEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

        return cursor;
    }

    public static Cursor getSongsFromLevel(String level, int sort) {
        String sortValues = "";
        switch (sort) {
            case 0:
                break;
            case 1:
                sortValues = iidxContract.songEntry.COLUMN_NAME_LEVEL + " ASC,";
                break;
            case 2:
                sortValues = iidxContract.songEntry.COLUMN_NAME_CLEAR + " ASC," + iidxContract.songEntry.COLUMN_NAME_LEVEL + " ASC,";
                break;
            default:
                break;
        }
        String[] projection = new String[4];
        projection[0] = iidxContract.songEntry.COLUMN_NAME_SONGNAME;
        projection[1] = iidxContract.songEntry.COLUMN_NAME_CLEAR;
        projection[2] = iidxContract.songEntry.COLUMN_NAME_LEVEL;
        projection[3] = iidxContract.songEntry.COLUMN_NAME_DIFFICULTY;

        String selection = iidxContract.songEntry.COLUMN_NAME_LEVEL + "=?";
        String[] selectionArgs = {level};
        String sortOrder = sortValues + " " + iidxContract.songEntry.COLUMN_NAME_SONGNAME + " COLLATE NOCASE ASC";
        Cursor cursor = dbInstance.query(iidxContract.songEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

        return cursor;
    }

    public static Cursor getSongsFromClear(String clear, int sort) {
        String sortValues = "";
        switch (sort) {
            case 0:
                break;
            case 1:
                sortValues = iidxContract.songEntry.COLUMN_NAME_LEVEL + " ASC,";
                break;
            case 2:
                sortValues = iidxContract.songEntry.COLUMN_NAME_CLEAR + " ASC," + iidxContract.songEntry.COLUMN_NAME_LEVEL + " ASC,";
                break;
            default:
                break;
        }
        String[] projection = new String[4];
        projection[0] = iidxContract.songEntry.COLUMN_NAME_SONGNAME;
        projection[1] = iidxContract.songEntry.COLUMN_NAME_CLEAR;
        projection[2] = iidxContract.songEntry.COLUMN_NAME_LEVEL;
        projection[3] = iidxContract.songEntry.COLUMN_NAME_DIFFICULTY;

        String selection = iidxContract.songEntry.COLUMN_NAME_CLEAR + "=?";
        String[] selectionArgs = {clear};
        String sortOrder = sortValues + " " + iidxContract.songEntry.COLUMN_NAME_SONGNAME + " COLLATE NOCASE ASC";
        Cursor cursor = dbInstance.query(iidxContract.songEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

        return cursor;
    }
}

