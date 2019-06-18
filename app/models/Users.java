package models;

import org.h2.engine.User;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

/**
 * Created by SpceSmkBrain on 1/6/2562.
 */
@Entity
@Table(name = "Users")
public class Users extends Model {
    @Id
    private String username;
    private String password, name,surname,age,sex,address,tel,email;

    public Users() {
    }

    public Users(String username, String password, String name, String surname, String age, String sex, String address, String tel, String email) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.sex = sex;
        this.address = address;
        this.tel = tel;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static Finder<String,Users> finder = new Finder<String, Users>(String.class, Users.class);

    public static List<Users> usersList (){
        return finder.all();
    }
    public static void insert (Users users){
        users.save();
    }public static void update (Users users){
        users.update();
    }
    public static void delete (Users users){
        users.delete();
    }
}
