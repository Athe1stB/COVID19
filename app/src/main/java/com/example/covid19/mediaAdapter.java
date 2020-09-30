package com.example.covid19;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.InputStream;
import java.util.ArrayList;

public class mediaAdapter extends ArrayAdapter<cases> {

    private View view;
    public String format(String s)
    {
        String s1="" ,s2="" ,s3="";
        int i;
        s1 = s.replace("T","   ");
        s2 = "Updated on " + s1.replace("Z"," ");
        return s2;
    }

    public mediaAdapter(Activity context ,ArrayList<cases> word){
        super(context,0,word);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        view = convertView;
        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.activity_media,parent,false);
        }

        cases cur = getItem(position);

        String url,title="",updated;
        title = cur.mtitle;
        updated = format(cur.date);
        url = cur.murl;


        TextView media = view.findViewById(R.id.titlemedia);
        media.setText(title);

        TextView media2 = view.findViewById(R.id.updatedmedia);
        media2.setText(updated);

        ImageView image = view.findViewById(R.id.imgviewnews);


        if (cur != null) {
            new DownloadImageTask(image).execute(cur.getmImgurl());
        }





        return view;
    }



    private class DownloadImageTask extends AsyncTask<String, Integer, Bitmap> {
        ImageView bmImage;
        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bmp = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bmp = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bmp;
        }


        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
