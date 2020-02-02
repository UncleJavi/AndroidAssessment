package com.pus.assesment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class PostDetail extends AppCompatActivity {
    TextView title;
    TextView author;
    TextView date;
    TextView description;
    ImageView img;
    Button back;
    Button shareBtn;
    Button dZoom;
    public static String showImageUrl = "";
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
        PostDetail.showImageUrl = MainActivity.postArrayList.get(MainActivity.selectedIndex).getLargeImage();
        new DownLoadImageTask(img).execute(MainActivity.postArrayList.get(MainActivity.selectedIndex).getImageUrl());
        title.setText(MainActivity.postArrayList.get(MainActivity.selectedIndex).getTitle());
        author.setText(MainActivity.postArrayList.get(MainActivity.selectedIndex).getAuthor());
        date.setText(MainActivity.postArrayList.get(MainActivity.selectedIndex).getDate());
        description.setText(MainActivity.postArrayList.get(MainActivity.selectedIndex).getDescription());
        back = (Button) findViewById(R.id.back);
        shareBtn = (Button) findViewById(R.id.shareImg);
        dZoom = (Button) findViewById(R.id.dzoom);
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable myDrawable = img.getDrawable();
                Bitmap bitmap = ((BitmapDrawable)myDrawable).getBitmap();
                try{
                    File file = new File(PostDetail.this.getExternalCacheDir(),"myImage.jpeg");
                    FileOutputStream fout = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG,80,fout);
                    fout.flush();
                    fout.close();
                    file.setReadable(true,false);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                    intent.setType("image/*");
                    startActivity(Intent.createChooser(intent,"Share Image Via"));
                }catch (FileNotFoundException e){
                    e.printStackTrace();
                    Toast.makeText(PostDetail.this,"File Nott Found",Toast.LENGTH_SHORT).show();
                }catch (IOException e){
                    e.printStackTrace();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        dZoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostDetail.this, ZoomInZoomOut.class);
                startActivity(intent);
            }
        });
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

        protected Bitmap doInBackground(String...urls){
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();
                logo = BitmapFactory.decodeStream(is);
            }catch(Exception e){ // Catch the download exception
                e.printStackTrace();
            }
            return logo;
        }

        protected void onPostExecute(Bitmap result){
            imageView.setImageBitmap(result);
        }
    }
}
