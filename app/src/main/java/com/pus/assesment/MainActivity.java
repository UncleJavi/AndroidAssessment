package com.pus.assesment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private ListView list;
    private ListViewAdapter adapter;
    private SearchView editsearch;
    private LinearLayout noData;
    public  static  int selectedIndex = -1;
    public static ArrayList<Post> postArrayList = new ArrayList<Post>();
    private Exception mException;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = (ListView) findViewById(R.id.listview);
        noData =(LinearLayout) findViewById(R.id.empty);
        getSupportActionBar().hide();
        String jsonurl = Config.APIUrl;
        new JsonTask().execute(jsonurl);


    }
    private void settingAdapter()
    {
        adapter = new ListViewAdapter(this);
        list.setAdapter(adapter);
        editsearch = (SearchView) findViewById(R.id.search);
        editsearch.setOnQueryTextListener(this);
        adapter.filter("");
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.selectedIndex = position;
                Intent intent = new Intent(MainActivity.this, PostDetail.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        int size = adapter.filter(text);
        if(size == 0)
        {
            noData.setVisibility(View.VISIBLE);
        }
        else
        {
            noData.setVisibility(View.GONE);
        }
        return false;
    }
    private void setResult(String jsonstring)
    {
        try {
            JSONObject obj = new JSONObject(jsonstring);
            String result_OK = obj.getString("stat");
            if(result_OK.endsWith("ok"))
            {
                JSONObject gallery_obj = obj.getJSONObject("galleries");
                JSONArray jsonArray = gallery_obj.getJSONArray("gallery");

                for (int i = 0; i < jsonArray.length() - 1; i++) {
                    obj = jsonArray.getJSONObject(i);
                    String title = obj.getJSONObject("title").getString("_content");
                    String imageUrl = obj.getJSONObject("primary_photo_extras").getString("url_sq");
                    String author = obj.getJSONObject("primary_photo_extras").getString("ownername");
                    long time  = (long) Double.parseDouble(obj.getJSONObject("primary_photo_extras").getString("lastupdate"))*1000L;
                    Date u_date = new Date(time);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy ");
                    String date = sdf.format(u_date).toString();
                    String description = obj.getJSONObject("description").getString("_content");
                    Post post = new Post(title, author,  imageUrl, date, description);
                    postArrayList.add(post);
                }
                settingAdapter();
            }


        } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON: \"");
        }
    }
    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if(responseCode == 200) {

                    InputStream stream = connection.getInputStream();

                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();
                    String line = "";

                    while ((line = reader.readLine()) != null) {
                        buffer.append(line+"\n");
                    }

                    return buffer.toString();
                }
                else
                {
                    return "";
                }

            } catch (IOException e) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setMessage("Loading Error!")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //do things
                                dialog.dismiss();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result=="")
            {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(MainActivity.this);
                }
                builder.setTitle("Loading error....")
                        .setMessage("Your Account can't get data , please call customers service")

                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
            else {
                setResult(result);
            }
        }
    }
}
