package com.cajalopez.apimapsapplication.utilities;

import android.net.Uri;

import com.cajalopez.apimapsapplication.providers.MyContentProvider;

import static com.cajalopez.apimapsapplication.databases.DBHelper.TABLE_NAME;

public class Constantes {
    public static final Uri CONTENT_URI = Uri.parse("content://" + MyContentProvider.AUTHORITY
            + "/" + TABLE_NAME);

    public static final Uri INSERT_URI = Uri.parse("content://" + MyContentProvider.AUTHORITY
            + "/" + TABLE_NAME + "/insert");

    public static final Uri BULK_INSERT_URI = Uri.parse("content://" + MyContentProvider.AUTHORITY
            + "/" + TABLE_NAME + "/bulk-insert");
}
