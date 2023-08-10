package com.mangoplay.yeezymusic.objects;

import android.content.Context;

import com.mangoplay.yeezymusic.MainActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Settings implements Serializable {

    boolean showVideo;
    static String fileName = "settings.bin";
    public int numberOfOpens = 0;
    public boolean reviewedTheApp = false;

    public Settings(boolean showVideo){
        this.showVideo = showVideo;
    };

    public boolean isShowVideo() {
        return showVideo;
    }

    public void setShowVideo(boolean showVideo) {
        this.showVideo = showVideo;
    }

    public void save(){
        File dir = new File( "mydir");
        if(!dir.exists()){
            dir.mkdir();
        }
//        String fileName= "Test.txt";
        File file = new File(dir, fileName);
        FileOutputStream fos = null;
        try {
            fos = MainActivity.context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Settings load(){

        Settings settings = new Settings(false);
        FileInputStream fin = null;
        try {
            fin = MainActivity.context.openFileInput(fileName);
            ObjectInputStream ois = new ObjectInputStream(fin);
            settings = (Settings) ois.readObject();
            //ois.close();
            ois.close();
            fin.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return settings;
    }
}
