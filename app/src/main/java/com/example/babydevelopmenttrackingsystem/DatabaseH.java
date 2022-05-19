package com.example.babydevelopmenttrackingsystem;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

class DatabaseH extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "babyDevTracking.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "baby_Details";
    private static final String LASTSAVEDID = "last_saved_id";
    private static final String TABLE3 = "vaccines";
    private static final String TABLE4 = "notify";

    private static final String COLUMN_ID = "baby_id";
    private static final String COLUMN_FNAME = "first_name";
    private static final String COLUMN_LNAME = "last_name";
    private static final String COLUMN_BIRTHDATE = "birth_date";

    private static final String COLUMN_WEIGHT = "current_weight";
    private static final String COLUMN_HEIGHT = "current_height";

    //Column names of the table3
    private static final String ID = "id";
    //private static final String AGE = "age";
    private static final String VACCINE = "vaccine";
    //private static final String REMARKS = "remarks";
    private static final String DONE = "done";
    private static final String USERID = "userId";

    //Column names for table4
    private static final String TIMEPERIOD = "timePeriod";
  

    public DatabaseH(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query1 = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FNAME + " TEXT, " +
                COLUMN_LNAME + " TEXT, " +
                COLUMN_BIRTHDATE + " TEXT, " +
                COLUMN_WEIGHT + " INTEGER, " +

                COLUMN_HEIGHT + " INTEGER, " +

                "first_nameG" + " TEXT, " +
                "last_nameG" + " TEXT, " +
                "birth_dateG" + " TEXT, " +
                "NIC" + " TEXT, " +
                "address" + " TEXT, " +
                "noofchildren" + " INTEGER);";
//                COLUMN_HEIGHT + " INTEGER);";

        String query2 = "CREATE TABLE " + LASTSAVEDID +
                " (" + COLUMN_ID + " INTEGER);";

        //Query to add vaccination data
        String query3 = "CREATE TABLE " + TABLE3 + " ("
                +ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +USERID + " Integer references " + TABLE_NAME + "(" + COLUMN_ID + "),"
                +VACCINE + " TEXT, "
                +DONE + " TEXT);" ;
      
        //Query to add notification settings data
        String query4 = "CREATE TABLE " + TABLE4 +
                " (" + TIMEPERIOD + " TEXT);";
      
        db.execSQL(query1);
        db.execSQL(query2);
        db.execSQL(query3);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        // drop if existing table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + LASTSAVEDID);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE3);
        onCreate(db);
    }

    // Add new baby using register form
    public void addBaby(String fname, String lname, String bdate, int weight, int height){
        SQLiteDatabase db = this.getWritableDatabase();                 // get writeable database
        ContentValues cv = new ContentValues();                         // create content values

        cv.put(COLUMN_FNAME, fname);
        cv.put(COLUMN_LNAME, lname);
        cv.put(COLUMN_BIRTHDATE, bdate);
        cv.put(COLUMN_WEIGHT, weight);
        cv.put(COLUMN_HEIGHT, height);

        long result = db.insert(TABLE_NAME, null, cv);      // insert data into database at relevant table

        if (result == -1){
            Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Added successfully !!", Toast.LENGTH_SHORT).show();
        }
    }

    public void addVaccineData(String name, String done, int vaccined/*int userId*/){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();


        contentValues.put(VACCINE, name);
        //contentValues.put(USERID, userId);
        contentValues.put(DONE, done);

        /* Save to the table*/

        if(vaccined == 0) {
            long result = db.insert(TABLE3, null, contentValues);

            if (result == -1) {
                Toast.makeText(context, "Failed!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Added successfully !!", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(context, "This vaccine is already given:)", Toast.LENGTH_SHORT).show();
        }

    }

    //==================================================================================
    // updating details according to Details page window

    public Boolean updateuserdata (String first_name, String last_name, String birth_date, int current_weight, int current_height){
        SQLiteDatabase myDB= this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("first_name", first_name);
        contentValues.put("last_name", last_name);
        contentValues.put("birth_date", birth_date);
        contentValues.put("current_weight", current_weight);
        contentValues.put("current_height", current_height);

        int IDwant = 3;                     // ------------------------->>>>>> remove later
        // cursor : like selecting the row , loaded to ths
        Cursor cursor = myDB.rawQuery("Select * from baby_Details where baby_id=?", new String[]{String.valueOf(IDwant)});
        // if cursor has some data
        if (cursor.getCount() > 0) {

            long result = myDB.update("baby_Details",  contentValues, "baby_id=?", new String[]{String.valueOf(IDwant)});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    // get tha latest baby using readLastSavedID

    public Cursor readLastBabyData(){
        int IDwant = 3;                     // ------------------------->>>>>> remove later
//        if (readLastSavedID() >= 1) {
//            IDwant = readLastSavedID();

            String query = "SELECT * FROM " + "baby_Details " + "WHERE " + "baby_id" + " = ?";

//        Toast.makeText(context, "Id 1 selected!", Toast.LENGTH_SHORT).show();
//        String query = "SELECT * FROM " + "baby_Details " + "ORDER BY baby_id DESC LIMIT 1";

            SQLiteDatabase db = this.getReadableDatabase();


            // cursor : retain all data in the db table,  selecting the row , loaded to this
            Cursor cursor = null;
            // check if db is not null
            if (db != null) {
                cursor = db.rawQuery(query, new String[]{String.valueOf(IDwant)});
            }

        return cursor;
//        }
//        return null;

    }
    // to store in array  ------------------------>>>> later

    public Cursor readBabyData(){
        String query = "SELECT * FROM " + "baby_Details " + "WHERE baby_id='2'";
        SQLiteDatabase db = this.getReadableDatabase();

        // cursor : retain all data in the db table,  selecting the row , loaded to this
        Cursor cursor = null;
        // check if db is not null
        if (db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }


    Cursor readAccounts(){
        String query = "SELECT " + COLUMN_FNAME + ", " + COLUMN_LNAME + " FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }


    public int readLastSavedID(){
        String query = "SELECT * FROM " + LASTSAVEDID;
        SQLiteDatabase db = this.getReadableDatabase();


        if(db != null){
            Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();

            if(cursor.getCount() == 0){
                return -1;
            } else {
                return cursor.getInt(0);
            }
        }
        return -2;
    }

    void updateLastSavedID(int id){
        int last_saved_id = readLastSavedID();
        String query = null;

        if (last_saved_id > 0){
            query = "UPDATE " + LASTSAVEDID + " SET " + COLUMN_ID + " = " + id + " WHERE " + COLUMN_ID + " = " + readLastSavedID();
        }else if(last_saved_id == -1){
            query = "INSERT INTO " + LASTSAVEDID + " VALUES (" + id + ");";
        }

        SQLiteDatabase db = this.getWritableDatabase();
        db.rawQuery(query, null);

    }

    public int findID(String Fname, String Lname){
        String query = "SELECT " + COLUMN_ID + " FROM " + TABLE_NAME + " WHERE " + COLUMN_FNAME + " = " + Fname + " && " + COLUMN_LNAME + " = " + Lname;
        SQLiteDatabase db = this.getReadableDatabase();

        if(db != null){
            Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();

            if(cursor.getCount() == 0){
                return -1;
            } else {
                return cursor.getInt(0);
            }
        }
        return -2;
    }

    public String getDate(int ID){
        String query = "SELECT " + COLUMN_BIRTHDATE + " FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = " + ID ;
        SQLiteDatabase db = this.getReadableDatabase();

        if(db != null){
            Cursor cursor = db.rawQuery(query, null);
            if(cursor.getCount() == 0){
                return null;
            } else {
                return cursor.getString(0);
            }
        }
        return null;

    }

}
