package co.edu.aulamatriz.accesscpapplication;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>, MyCursorRecycler.MyModelCallBack {

    private MyCursorRecycler mAdapter;
    public int error_invalid_email = 0x7f07001b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* Intent intent = new Intent();
                intent.setAction("android.intent.action.MY_CUSTOM_ACTION");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.putExtra("pass", "12345");
                startActivity(intent);*/

                Intent intent = new Intent(MainActivity.this, AnunciosActivity.class);
                startActivity(intent);
            }
        });

        Cursor cursor = getContentResolver().query(Constantes.CONTENT_URI, null, null, null,
                "joke");
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        mAdapter = new MyCursorRecycler(cursor, this);
        mRecyclerView.setAdapter(mAdapter);

        getSupportLoaderManager().initLoader(0, null, this);

        try {
            Context context = createPackageContext(
                    "com.cajalopez.apimapsapplication", 0);
            String value = context.getString(error_invalid_email);
            Log.e("main", value);
            Toast.makeText(this, value, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, Constantes.CONTENT_URI, null, null, null, "joke");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.notifyCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.notifyCursor(null);
    }

    @Override
    public void notify(MyModel model, TextView textView) {

    }
}
