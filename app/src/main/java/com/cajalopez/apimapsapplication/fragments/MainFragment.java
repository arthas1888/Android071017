package com.cajalopez.apimapsapplication.fragments;


import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.cajalopez.apimapsapplication.R;
import com.cajalopez.apimapsapplication.adapters.MyAdapter;
import com.cajalopez.apimapsapplication.adapters.MyCursorRecycler;
import com.cajalopez.apimapsapplication.databases.DBHelper;
import com.cajalopez.apimapsapplication.models.MyModel;
import com.cajalopez.apimapsapplication.utilities.MySingleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.net.ssl.HttpsURLConnection;

import static com.cajalopez.apimapsapplication.utilities.Constantes.CONTENT_URI;
import static com.cajalopez.apimapsapplication.utilities.Constantes.INSERT_URI;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView mRecyclerView;


    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Cursor cursor = getActivity().getContentResolver().query(CONTENT_URI, null, null, null, DBHelper.COLUMN_NAME);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Logger.addLogAdapter(new AndroidLogAdapter());
        String url = "http://api.icndb.com/jokes/random/2000";
        //url = "https://plataforma.visionsatelital.co:9050/points/get_distance/?lat=9999&lon=9999";
        Logger.w("isOnline: " + isOnline());

        if (isOnline() && cursor != null && cursor.getCount() == 0)
            new HttpAsynTask(getActivity()).execute(url);
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), CONTENT_URI, null, null, null, DBHelper.COLUMN_NAME);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    private class HttpAsynTask extends AsyncTask<String, Object, String> {

        private final Context mContext;

        HttpAsynTask(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... data) {
            URL url = null;
            return (getDataAPI(data[0])).toString();
           /* try {
                url = new URL(data[0]);
                return downloadUrl(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null)
                processData(s);
            else
                Logger.e("sin datos");
        }


        private JSONObject getDataAPI(String url) {
            //
            RequestFuture<JSONObject> future = RequestFuture.newFuture();
            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, future, future);

            // Access the RequestQueue through your singleton class.
            MySingleton.getInstance(mContext).addToRequestQueue(jsObjRequest);

            try {
                return future.get(10, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException e) {
                // exception handling
                Logger.e("error: " + e.getMessage());
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * Given a URL, sets up a connection and gets the HTTP response body from the server.
         * If the network request is successful, it returns the response body in String form. Otherwise,
         * it will throw an IOException.
         */
        private String downloadUrl(URL url) throws IOException {
            InputStream stream = null;
            HttpURLConnection connection = null;
            String result = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                // Timeout for reading InputStream arbitrarily set to 3000ms.
                connection.setReadTimeout(3000);
                // Timeout for connection.connect() arbitrarily set to 3000ms.
                connection.setConnectTimeout(3000);
                // For this use case, set HTTP method to GET.
                connection.setRequestMethod("GET");
                // Already true by default but setting just in case; needs to be true since this request
                // is carrying an input (response) body.
                connection.setDoInput(true);
                // Open communications link (network traffic occurs here).
                connection.connect();

                int responseCode = connection.getResponseCode();
                if (responseCode != HttpsURLConnection.HTTP_OK) {
                    throw new IOException("HTTP error code: " + responseCode);
                }
                // Retrieve the response body as an InputStream.
                stream = connection.getInputStream();

                if (stream != null) {
                    // Converts Stream to String with max length of 500.
                    result = readStream(stream);
                }
            } finally {
                // Close Stream and disconnect HTTPS connection.
                if (stream != null) {
                    stream.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return result;
        }

        /**
         * Converts the contents of an InputStream to a String.
         */
        public String readStream(InputStream stream) {
            BufferedReader br = null;
            StringBuilder sb = new StringBuilder();

            String line;
            try {

                br = new BufferedReader(new InputStreamReader(stream));
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return sb.toString();
        }
    }

    private void processData(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = jsonObject.getJSONArray("value");

            DBHelper dbHelper = new DBHelper(getActivity());
            dbHelper.deleteRows(0);
            dbHelper.close();
            Gson gson = new Gson();
            ArrayList<MyModel> myModelArrayList = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<MyModel>>() {
            }.getType());
            Date date = new Date();
            ContentValues[] contentValues = new ContentValues[myModelArrayList.size()];
            int count = 0;
            for (MyModel model : myModelArrayList) {
                ContentValues cv = new ContentValues();
                cv.put(DBHelper.COLUMN_SERVER_ID, model.id);
                cv.put(DBHelper.COLUMN_NAME, model.joke);
                cv.put(DBHelper.COLUMN_CAT, Arrays.toString(model.categories));
                contentValues[count] = cv;
                count++;
                //getActivity().getContentResolver().insert(INSERT_URI, cv);
            }
            long rows = getActivity().getContentResolver().bulkInsert(INSERT_URI, contentValues);
            Logger.w("time: " + (new Date().getTime() - date.getTime()) + ", rows: " + rows);

            Cursor cursor = getActivity().getContentResolver().query(CONTENT_URI, null, null, null, DBHelper.COLUMN_NAME);
            // specify an adapter (see also next example)
            MyCursorRecycler mAdapter = new MyCursorRecycler(cursor, mListener);
            mRecyclerView.setAdapter(mAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private MyModelCallBack mListener;

    public interface MyModelCallBack {
        void notify(MyModel model, TextView textView);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MyModelCallBack)
            mListener = (MyModelCallBack) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
