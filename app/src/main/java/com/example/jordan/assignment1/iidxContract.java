package com.example.jordan.assignment1;

import android.provider.BaseColumns;


public final class iidxContract {
    private iidxContract() {}

    public static class songEntry implements BaseColumns {
        public static final String TABLE_NAME = "songs";
        public static final String COLUMN_NAME_SONGNAME = "name";
        public static final String COLUMN_NAME_VERSION = "version";
        public static final String COLUMN_NAME_LEVEL = "level";
        public static final String COLUMN_NAME_DIFFICULTY = "difficulty";
        public static final String COLUMN_NAME_CLEAR = "clear";
    }

    public static class playerEntry implements BaseColumns {
        public static final String TABLE_NAME = "player";
        public static final String COLUMN_NAME_NAME = "name";
    }

    public static class goalItemEntry implements BaseColumns {
        public static final String TABLE_NAME = "goalitem";
        public static final String COLUMN_NAME_LIST = "list";
        public static final String COLUMN_NAME_SONG = "song";
        public static final String COLUMN_NAME_DIFFICULTY = "difficulty";
        public static final String COLUMN_NAME_GOAL_CLEAR = "goalclear";
    }

    public static class goalEntry implements BaseColumns {
        public static final String TABLE_NAME = "goal";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_PLAYER = "player";
    }

    public static final String SQL_CREATE_SONG =
                "CREATE TABLE " + songEntry.TABLE_NAME + " (" +
                songEntry.COLUMN_NAME_SONGNAME + " VARCHAR(50) NOT NULL," +
                songEntry.COLUMN_NAME_VERSION + " VARCHAR(25) NOT NULL," +
                songEntry.COLUMN_NAME_LEVEL + " INT NOT NULL," +
                songEntry.COLUMN_NAME_DIFFICULTY + " INT NOT NULL," +
                songEntry.COLUMN_NAME_CLEAR + " INT NOT NULL," +
                "PRIMARY KEY (name,difficulty) )";

    public static final String SQL_CREATE_PLAYER =
            "CREATE TABLE " + playerEntry.TABLE_NAME + " (" +
                    playerEntry.COLUMN_NAME_NAME + " VARCHAR(50) NOT NULL," +
                    "PRIMARY KEY (name) )";

    public static final String SQL_CREATE_GOALITEM =
            "CREATE TABLE " + goalItemEntry.TABLE_NAME + " (" +
                    goalItemEntry.COLUMN_NAME_LIST + " VARCHAR(100) NOT NULL," +
                    goalItemEntry.COLUMN_NAME_SONG + " VARCHAR(50) NOT NULL," +
                    goalItemEntry.COLUMN_NAME_DIFFICULTY + " INT NOT NULL," +
                    "PRIMARY KEY (difficulty,list,song) )";

    public static final String SQL_CREATE_GOAL =
            "CREATE TABLE " + goalEntry.TABLE_NAME + " (" +
                    goalEntry.COLUMN_NAME_NAME+ " VARCHAR(100) NOT NULL," +
                    goalEntry.COLUMN_NAME_PLAYER + " VARCHAR(50) NOT NULL," +
                    "PRIMARY KEY (name) )";

    public static final String SQL_DELETE_SONG =
            "DROP TABLE IF EXISTS " + songEntry.TABLE_NAME;
    public static final String SQL_DELETE_PLAYER =
            "DROP TABLE IF EXISTS " + playerEntry.TABLE_NAME;
    public static final String SQL_DELETE_GOALITEM =
            "DROP TABLE IF EXISTS " + goalItemEntry.TABLE_NAME;
    public static final String SQL_DELETE_GOAL =
            "DROP TABLE IF EXISTS " + goalEntry.TABLE_NAME;
}

