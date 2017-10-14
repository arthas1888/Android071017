package co.edu.aulamatriz.accesscpapplication;

import android.net.Uri;

public class Constantes {
    private static final String AUTHORITY = "com.cajalopez.apimapsapplication.authority.MyContentProvider";
    private static final String TABLE_NAME = "chuck_norris";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + TABLE_NAME);

    public static final Uri INSERT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + TABLE_NAME + "/insert");

    public static final Uri BULK_INSERT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + TABLE_NAME + "/bulk-insert");
}
