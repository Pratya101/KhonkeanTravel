package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.util.List;

@Entity
public class Culture extends Model {
    @Id
    private String id;
    @Lob
    private String title,detail;
    private String pic;

    public Culture() {
    }

    public Culture(String id, String title, String detail, String pic) {
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

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public static Finder<String,Culture> finder = new Finder<String, Culture>(String.class,Culture.class);

    public static List<Culture> cultureList (){
        return finder.all();
    }

    public static void insert(Culture culture){
        culture.save();
    }
    public static void update(Culture culture){
        culture.update();
    }
    public static void delete(Culture culture){
        culture.delete();
    }


}