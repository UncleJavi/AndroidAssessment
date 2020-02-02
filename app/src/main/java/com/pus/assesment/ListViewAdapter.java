package com.pus.assesment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

public class ListViewAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater inflater;
    private ArrayList<Post> arraylist;

    public ListViewAdapter(Context context ) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Post>();
        this.arraylist.addAll(MainActivity.postArrayList);
    }

    public class ViewHolder {
        TextView title;
        TextView autor;
        ImageView img;

    }

    @Override
    public int getCount() {
        return MainActivity.postArrayList.size();
    }

    @Override
    public Post getItem(int position) {
        return MainActivity.postArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.listview_item, null);
            holder.title = (TextView) view.findViewById(R.id.title);
            holder.img = (ImageView) view.findViewById(R.id.img);
            holder.autor = (TextView) view.findViewById(R.id.author);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.title.setText(MainActivity.postArrayList.get(position).getTitle());
        holder.autor.setText(MainActivity.postArrayList.get(position).getAuthor());
        new DownLoadImageTask(holder.img).execute(MainActivity.postArrayList.get(position).getImageUrl());
        return view;
    }

    public int filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        MainActivity.postArrayList.clear();

        if (charText.length() == 0) {
        } else {
            for (Post wp : arraylist) {
                if (wp.getTitle().toLowerCase(Locale.getDefault()).contains(charText)) {
                    MainActivity.postArrayList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
        return  MainActivity.postArrayList.size();
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

