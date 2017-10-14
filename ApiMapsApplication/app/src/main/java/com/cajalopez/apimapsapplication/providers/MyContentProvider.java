package com.cajalopez.apimapsapplication.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cajalopez.apimapsapplication.databases.DBHelper;
import com.cajalopez.apimapsapplication.utilities.Constantes;

import static com.cajalopez.apimapsapplication.databases.DBHelper.TABLE_NAME;


public class MyContentProvider extends ContentProvider {

    public static final String AUTHORITY = "com.cajalopez.apimapsapplication.authority.MyContentProvider";
    private static final UriMatcher sUriMatcher;
    private static final int DATUM = 1;
    private static final int DATUM_ID = 2;
    private static final int DATUM_INSERT = 3;
    private static final int DATUM_BULK_INSERT = 4;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME, DATUM);
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME + "/#", DATUM_ID);
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME + "/insert", DATUM_INSERT);
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME + "/bulk-insert", DATUM_BULK_INSERT);
    }

    private DBHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_NAME);

        switch (sUriMatcher.match(uri)) {
            case DATUM:
                break;
            case DATUM_ID:
                selection = selection + "_id = " + uri.getLastPathSegment();
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new IllegalArgumentException("Unknown URI " + uri);
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String tableName = "";
        switch (sUriMatcher.match(uri)) {
            case DATUM_INSERT:
                tableName = TABLE_NAME;
                break;
            default:
                break;
        }
        long rId = db.insert(tableName, null, values);
        if (rId > 0) {
            Uri u = ContentUris.withAppendedId(Constantes.CONTENT_URI, rId);
            getContext().getContentResolver().notifyChange(u, null);
            return uri;
        }
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = 0;
        db.beginTransaction();
        try {
            for (ContentValues value : values) {
                db.insert(TABLE_NAME, null, value);
                rows++;
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return rows;
    }
}
