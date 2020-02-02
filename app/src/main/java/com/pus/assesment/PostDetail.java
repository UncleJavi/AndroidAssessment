package com.pus.assesment;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;

public class PostDetail extends AppCompatActivity {
    TextView title;
    TextView author;
    TextView date;
    TextView description;
    ImageView img;
    Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        getSupportActionBar().hide();
        title = (TextView) findViewById(R.id.dtitle);
        author = (TextView) findViewById(R.id.dauthor);
        date = (TextView) findViewById(R.id.ddate);
        description = (TextView) findViewById(R.id.ddescription);
        img = (ImageView) findViewById(R.id.dimg);

        new DownLoadImageTask(img).execute(MainActivity.postArrayList.get(MainActivity.selectedIndex).getImageUrl());
        title.setText(MainActivity.postArrayList.get(MainActivity.selectedIndex).getTitle());
        author.setText(MainActivity.postArrayList.get(MainActivity.selectedIndex).getAuthor());
        date.setText(MainActivity.postArrayList.get(MainActivity.selectedIndex).getDate());
        description.setText(MainActivity.postArrayList.get(MainActivity.selectedIndex).getDescription());
        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });

    }
    private class DownLoadImageTask extends AsyncTask<String,Void, Bitmap> {
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView){
            this.imageView = imageView;
        }

        /*
            doInBackground(Params... params)
                Override this method to perform a computation on a background thread.
         */
        protected Bitmap doInBackground(String...urls){
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();
                /*
                    decodeStream(InputStream is)
                        Decode an input stream into a bitmap.
                 */
                logo = BitmapFactory.decodeStream(is);
            }catch(Exception e){ // Catch the download exception
                e.printStackTrace();
            }
            return logo;
        }

        /*
            onPostExecute(Result result)
                Runs on the UI thread after doInBackground(Params...).
         */
        protected void onPostExecute(Bitmap result){
            imageView.setImageBitmap(result);
        }
    }
}
