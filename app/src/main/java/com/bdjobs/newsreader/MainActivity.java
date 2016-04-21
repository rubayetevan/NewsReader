package com.bdjobs.newsreader;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    TextView txttitle,detailstxt;
    String url = "http://www.prothom-alo.com/";
    ProgressDialog mProgressDialog;
    String detailsLink1,title_1,details_1;
    ImageView imageView;
    String a[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txttitle = (TextView) findViewById(R.id.titletxt);
        detailstxt= (TextView) findViewById(R.id.detailstxt);
        imageView = (ImageView) findViewById(R.id.imageView);

        new Title().execute();


    }
    private class Title extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setTitle("News is Loading");
            mProgressDialog.setMessage("Please Wait...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document document = Jsoup.connect(url).get();
                Elements element = document.select("div[id=widget_50158]");
                Elements title1 = element.select(".title");

                title_1 = Html.fromHtml(title1.toString()).toString();
                Elements details1_link = title1.select("a[href]");
                detailsLink1 = url + link(details1_link.toString());
                System.out.println(detailsLink1);

                Document document1 = Jsoup.connect(detailsLink1).get();
                Elements elements1 = document1.select("div[itemprop=articleBody]");
                details_1 = Html.fromHtml(elements1.toString()).toString();

                Elements elements = elements1.select("img[itemprop=image]");
                String sa= elements.toString();
                a = sa.split("\"");

                /*for(int i =0;i<a.length;i++)
                System.out.println(a[i]+"\nPosition:"+String.valueOf(i));*/




            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Set title into TextView

            detailstxt.setText(details_1);
            txttitle.setText(title_1);
            Glide.with(getApplicationContext()).load(a[13]).into(imageView);
            mProgressDialog.dismiss();
        }
    }


    public static String link(String data) {

        String regex = "<a href=(\"[^\"]*\")[^<]*</a>";

        Pattern p = Pattern.compile(regex);

        Matcher m = p.matcher(data);
        System.out.println(m.replaceAll("$1"));
        String d = m.replaceAll("$1").toString();
        return d.replaceAll("\"", "");
    }
}
