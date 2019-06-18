package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Travel extends Model {
    @Id
    private String id;
    @Lob
    private String title,detail;
    private String pic;
    public Travel() {
    }

    public Travel(String id, String title, String detail, String pic) {
        this.id = id;
        this.title = title;
        this.detail = detail;
        this.pic = pic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public static Finder<String,Travel> finder = new Finder<String, Travel>(String.class,Travel.class);

    public static List<Travel> travelList (){
        return finder.all();
    }

    public static void insert (Travel travel){
        travel.save();
    }
    public static void update (Travel travel){
        travel.update();
    }
    public static void delete (Travel travel){
        travel.delete();
    }



}