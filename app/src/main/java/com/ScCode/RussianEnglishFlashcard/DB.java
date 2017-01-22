package com.ScCode.RussianEnglishFlashcard;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

import static android.R.attr.id;

/**
 * Created by scottshotgg on 1/17/17.
 */

public class DB extends SQLiteOpenHelper {


    final static int DB_VERSION = 1;
    //final static String DB_NAME = "/res/raw/mydb.s3db";
    private static String DB_PATH = "/data/data/com.ScCode.RussianEnglishFlashcard/databases/";
    final static String DB_NAME = "words.db";
    Context context;
    SQLiteDatabase db;

    public void print(String shit) {
        System.out.println(shit);
    }


    public DB(Context context) {
        super(context, DB_PATH + DB_NAME, null, DB_VERSION);
        // Store the context for later use
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL("CREATE TABLE employees ( _id INTEGER PRIMARY KEY "
               // + "AUTOINCREMENT, name TEXT NOT NULL, ext TEXT NOT NULL, "
              //  + "mob TEXT NOT NULL, age INTEGER NOT NULL DEFAULT '0')");
        //SQLiteDatabase checkdb = null;

        createDatabase();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("DROP TABLE IF EXISTS contacts");
        //onCreate(db);

    }

    public void createDatabase() {
        // TODO find why this shit is making several successful calls and printouts
        SQLiteDatabase checkdb = null;

        try {
            print("trying to open db");
            String myPath = DB_PATH + DB_NAME;
            print(myPath);
			//File dbFile = context.getDatabasePath(DB_NAME);
			//System.out.println(dbFile);
			//File dFile = context.getDatabasePath(myPath);
			//System.out.println(dFile);

            //checkdb = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
            //System.out.println(checkdb);
			File dbFile = context.getDatabasePath(myPath);

			System.out.println("dbfile: " + dbFile.exists());
            //checkdb.close();

        } catch(SQLiteException e) {

            //database does't exist yet.
            print("database doesn't exist yet");

            copyDatabaseFromAssets();
        }

		print("here we are");
        //db = this.getReadableDatabase();
		print("did we get this far");
    }

	// TODO: For some reason this shit is not working anymore
    public void copyDatabaseFromAssets() {
        print("copyDatabaseFromAssets");
        try {
            //Open your local db as the input stream
            InputStream myInput = context.getAssets().open(DB_NAME);

            // Path to the just created empty db
            String outFileName = DB_PATH + DB_NAME;

            //Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);

            //transfer bytes from the inputfile to the outputfile
            print("starting copy...");
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            //Close the streams
			myOutput.flush();
            myOutput.close();
            myInput.close();
        }
        catch (java.io.IOException e) {
            System.out.println("idk some shit happened or something dude" + e);
        }

        print("done copying");
    }

    public ArrayList[] grabWords(int amount) {
        if (amount != 500) {
            // TODO make less that 500 and random stuff
            //random some numbers and pull those from the database, we can also make a thing that saves the days and the verbs studied and pick different ones
        }
		db = this.getReadableDatabase();

		System.out.println("db: " + db);
        Cursor res =  db.rawQuery("select * from main", null);

        ArrayList<String> english = new ArrayList<String>();
        ArrayList<String> russian = new ArrayList<String>();

        // Move to the first cursor value; will crash if you don't do this
        res.moveToFirst();

        for(int i = 0; i < amount; i++) {
            String eng = res.getString(1);
            String rus = res.getString(2);

			//print("English: " + eng);
			//print("Russian: " + rus);

            english.add(eng);
            russian.add(rus);

			res.moveToNext();
        }

		return new ArrayList[] {english, russian};
    }
}