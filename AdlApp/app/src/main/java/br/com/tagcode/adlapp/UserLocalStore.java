package br.com.tagcode.adlapp;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Adler Pagliarini on 14/01/2016.
 */
public class UserLocalStore {

    public static final String SP_NAME = "userDetails";

    //allow to save data on the phone
    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context){
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    //save user
    public void storeUserData(User user){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("name", user.name);
        spEditor.putInt("age", user.age);
        spEditor.putString("userName", user.userName);
        spEditor.putString("password", user.password);
        spEditor.commit();
    }

    //return user
    public User getLoggedInUser(){
        String name = userLocalDatabase.getString("name", "");
        int age = userLocalDatabase.getInt("age", -1);
        String userName = userLocalDatabase.getString("userName","");
        String password = userLocalDatabase.getString("password","");

        User storeUser = new User(name, age, userName, password);

        return storeUser;
    }

    public void setUserLoggedIn(boolean loggedIn){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("loggedIn", loggedIn);
        spEditor.commit();
    }

    public boolean getUserLoggedIn(){
        if(userLocalDatabase.getBoolean("loggedIn", false) == true){
            return true;
        }else{
            return  false;
        }
    }

    public void clearUserData(){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }
}
