package com.mangoplay.yeezymusic.ui.settings;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mangoplay.yeezymusic.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import androidx.appcompat.app.AppCompatActivity;

public class PrivacyPolicy extends AppCompatActivity {

    TextView text;
    ImageView backButtonTop;
    Button backButtonBottom;
    String url = "https://mangoplayservices.blogspot.com/2021/08/privacy-policy-this-privacy-policy.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        text = findViewById(R.id.text);
//        text.setText(termsAndConditions);
        backButtonTop = findViewById(R.id.back_button_top);
        backButtonBottom = findViewById(R.id.back_button_bottom);

        backButtonBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("back pressed");
                finish();
            }
        });

        backButtonTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("back pressed");
                finish();
            }
        });

        new Thread(() -> {
            Document doc = null;
            try {
                doc = Jsoup.connect(url).get();
                Element ps = doc.select("#post-body-674990984373979364").first();
                if(ps != null) {
                    String result = br2nl(ps.toString());
                    String parse = Jsoup.parse(result).text();
                    String string = parse.replace("\\n", "\n");
                    System.out.println("ps: ");
                    System.out.println(string);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            text.setText(string);
                        }
                    });
                } else throw new IOException();
            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String string = "Sorry can't load the Privacy Policy right now.Please try again.";
                        text.setText(string);
                    }
                });
            }
        }).start();

    }

    public String br2nl(String html) {
        if(html==null)
            return html;
        Document document = Jsoup.parse(html);
        document.outputSettings(new Document.OutputSettings().prettyPrint(false));//makes html() preserve linebreaks and spacing
        document.select("br").append("\\n");
        document.select("article").append("\\n");
        document.select("p").prepend("\\n\\n");
        String s = document.html();
        return Jsoup.clean(s, "", Safelist.none(), new Document.OutputSettings().prettyPrint(false));
    }


    static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
