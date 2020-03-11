package uk.co.tcork.contentproviderdemo;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class CompanyProvider extends ContentProvider {
    public CompanyProvider() {
    }

    public static final String AUTHORITY = "uk.co.tcork.contentproviderdemo.companyprovider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/emp");

    public static int EMP = 1;
    public static int EMP_ID = 2;

    private static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, "emp", EMP);
        uriMatcher.addURI(AUTHORITY, "emp/#", EMP_ID);
    }

    private SQLiteDatabase myDatabase;



    private static final String DATABASE_NAME = "company";
    private static final String DATABASE_TABLE = "emp";
    private static final int DATABASE_VERSION = 1;

    /**
     * This is my class defining the SqlLite database and how to create/upgrade it.
     */
    private class MyOwnDatabase extends SQLiteOpenHelper {

        public MyOwnDatabase(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table " + DATABASE_TABLE + "(_id integer primary key autoincrement, emp_name text, profile text);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists "+ DATABASE_TABLE );
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // Insert row into the sqllite database.
        long row = myDatabase.insert(DATABASE_TABLE, null, values);

        if (row > 0) {
            // Generate the content uri for the newly inserted row.
            uri = ContentUris.withAppendedId(CONTENT_URI, row);

            // Notify the content resolver of a change.
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return uri;
    }

    @Override
    public boolean onCreate() {
        MyOwnDatabase myHelper = new MyOwnDatabase(getContext());

        myDatabase = myHelper.getWritableDatabase();

        if (myDatabase != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(DATABASE_TABLE);

        if (sortOrder == null || sortOrder == "") {
            sortOrder = "_id";
        }

        Cursor cursor = queryBuilder.query(myDatabase, projection, selection, selectionArgs, null, null, sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
