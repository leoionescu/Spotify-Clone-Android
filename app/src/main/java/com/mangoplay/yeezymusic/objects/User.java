package com.mangoplay.yeezymusic.objects;

import android.content.Context;

import com.mangoplay.yeezymusic.MainActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class User implements Serializable {
    String email;

    public User(){}

    public User(String email) {
        this.email = email;
    }

    public void saveUser(Context context) throws IOException {
        String fileName = "user.bin";
        File dir = new File( "mydir");
        if(!dir.exists()){
            dir.mkdir();
        }
//        String fileName= "Test.txt";
        File file = new File(dir, fileName);
        FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(this);
        oos.close();
        System.out.println("email: " + email);
    }

    public static User readUser(Context context) throws ClassNotFoundException {
        String fileName = "user.bin";
        User user = null;
        FileInputStream fin = null;
        try {
            fin = context.openFileInput(fileName);
            ObjectInputStream ois = new ObjectInputStream(fin);
            user = (User) ois.readObject();
            //ois.close();
            ois.close();
            fin.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
