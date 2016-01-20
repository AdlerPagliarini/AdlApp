package br.com.tagcode.adlapp;

/**
 * Created by Adler Pagliarini on 14/01/2016.
 */
public class User {
    String name, userName, password;
    int age;

    public User(String name, int age, String userName, String password){
        this.name = name;
        this.age = age;
        this.userName = userName;
        this.password = password;
    }
    public User(String userName, String password){
        this.userName = userName;
        this.password = password;
        this.age = -1;
        this.name = "";
    }
    public User(String userName){
        this.userName = userName;
        this.password = "";
        this.age = -1;
        this.name = "";
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
