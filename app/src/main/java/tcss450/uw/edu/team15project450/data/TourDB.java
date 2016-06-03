package tcss450.uw.edu.team15project450.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import tcss450.uw.edu.team15project450.model.Tour;

public class TourDB {

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "Tour.db";
    private static String TOUR_TABLE = "Tour";
    private TourDBHelper mTourDBHelper;
    private SQLiteDatabase mSQLiteDatabase;

    public TourDB(Context context) {
        mTourDBHelper = new TourDBHelper(
                context, DB_NAME, null, DB_VERSION);
        mSQLiteDatabase = mTourDBHelper.getWritableDatabase();
    }

    public boolean insertTour(String title, String createdBy, String description, boolean bPublic, boolean bPublished,
                              String dateCreated, String dateModified, String audio) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("createdBy", createdBy);
        contentValues.put("description", description);
        contentValues.put("bPublic", bPublic);
        contentValues.put("bPublished", bPublished);
        contentValues.put("dateCreated", dateCreated);
        contentValues.put("dateModified", dateModified);
        contentValues.put("audio", audio);

        long rowId = mSQLiteDatabase.insert("Tour", null, contentValues);
        return rowId != -1;
    }

    public List<Tour> getTours() {
        String[] columns = {"title", "createdBy", "description", "bPublic", "bPublished", "dateCreated", "dateModified", "audio"};

        Cursor c = mSQLiteDatabase.query(
                TOUR_TABLE,  // The table to query
                columns,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        c.moveToFirst();
        List<Tour> list = new ArrayList<Tour>();
        for (int i = 0; i < c.getCount(); i++) {
            String title = c.getString(0);
            String createdBy = c.getString(1);
            String description = c.getString(2);
            boolean isPublic = c.getInt(3) >= 0;
            boolean isPublished = c.getInt(4) >= 0;
            String dateCreated = c.getString(5);
            String dateModified = c.getString(6);
            String audio = c.getString(7);
            Tour tour = new Tour(title, description, audio, isPublic, isPublished, createdBy, dateCreated, dateModified);
            list.add(tour);
            c.moveToNext();
        }

        return list;

    }

    /**
     * Delete all the data from the TOUR_TABLE
     */
    public void deleteTours() {
        mSQLiteDatabase.delete(TOUR_TABLE, null, null);
    }

    public void closeDB() {
        mSQLiteDatabase.close();
    }

    class TourDBHelper extends SQLiteOpenHelper {

        private static final String CREATE_TOUR_SQL =
                "CREATE TABLE IF NOT EXISTS TOUR "
                        + "(title TEXT, createdBy TEXT, description TEXT, bPublic INT, bPublished INT," +
                        "dateCreated TEXT, dateModified TEXT)";

        private static final String DROP_TOUR_SQL =
                "DROP TABLE IF EXISTS Tour";


        public TourDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_TOUR_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(DROP_TOUR_SQL);
            onCreate(sqLiteDatabase);
        }

    }


}
