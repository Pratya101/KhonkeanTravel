package controllers;

import com.avaje.ebean.Expr;
import models.*;
import play.*;
import play.data.DynamicForm;
import play.data.Form;
import play.api.templates.Html;
import play.mvc.*;
import views.html.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Application extends Controller {
    public static Result main(Html content){
        travelList = Travel.travelList();
        travel = travelList.get(travelList.size()-1);
        cultureList = Culture.cultureList();
        return ok(index.render(content,travel,cultureList));
    }

    public static Result mainPage (){
        session().remove("sunHeader");
        return ok(mainPage.render());
    }
    public static Result login (){
        return ok(loginPage.render());
    }
    public static Users users;

    public static Result loinOk(){
        DynamicForm data = Form.form().bindFromRequest();
        users = Users.finder.where().and(Expr.eq("username",data.get("username")),Expr.eq("password",data.get("pass"))).findUnique();
        if (users==null){
            flash("loginError","username หรือ password ไม่ถูกต้องกรุณษตรวจสอบใหม่ด้วยค่ะ!");
            return ok(loginPage.render());
        }else{
            session("username",data.get("username"));
            return main(indexAdmin.render());
        }
    }

    public static Result indexAdmin (){
        return main(indexAdmin.render());
    }

    public static Result logout (){
        session().clear();
        return redirect("/");
    }

    public static Result history(){
        session("sunHeader","1");
        travelList = Travel.travelList();
        cultureList = Culture.cultureList();
        return main(history.render(travelList,cultureList));
    }

    public static Result about (){
        session().remove("sunHeader");
        return  main(about.render());
    }
    public static Result contact (){
        session().remove("sunHeader");
        return main(contact.render());
    }
    
    public static List<Culture> cultureList = new ArrayList<Culture>();
    public static Culture culture;
    public static Form<Culture> cultureForm = Form.form(Culture.class);
    public static String camPicPath = Play.application().configuration().getString("cam_pic_path");

    public static Result listCulture() {
        session().remove("sunHeader");
        cultureList = Culture.cultureList();
        return main(listCulture.render(cultureList));
    }

    public static Result addCulture() {
        session().remove("sunHeader");
        cultureForm = Form.form(Culture.class);
        return main(formAddCulture.render(cultureForm));
    }

    public static Result insertCulture() {
        session().remove("sunHeader");
        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart picture = body.getFile("pic");
        String fileName, contentType;
        Form<Culture> newProduct = cultureForm.bindFromRequest();
        if (newProduct.hasErrors()) {
            flash("msgError", "คุณป้อมข้อมูลไม่ถูกต้อง กรุณาตรวจสอบใหม่ด่วยค่ะ!");
            return main(formAddCulture.render(cultureForm));
        } else {
            culture = newProduct.get();
            if (picture != null) {

                contentType = picture.getContentType();
                File file = picture.getFile();
                fileName = picture.getFilename();
                if (!contentType.startsWith("image")) {
                    flash("msgError", "นามสกุลภาพไม่ตรงตามชนิด กรุณาตรวจสอบใหม่ด่วยค่ะ!");
                    return main(formAddCulture.render(cultureForm));
                }
                cultureList = Culture.cultureList();
                int numlist = cultureList.size();
                int id, idNum;
                String lastNum;
                String sId;
                if (cultureList.size() == 0) {
                    sId = "CUL-000001";
                } else {
                    lastNum = cultureList.get(numlist - 1).getId();
                    lastNum = lastNum.substring(4);
                    idNum = Integer.parseInt(lastNum);
                    id = idNum + 1;
                    if (id < 10) {
                        sId = "CUL-00000" + id;
                    } else if (id < 100) {
                        sId = "CUL-0000" + id;
                    } else if (id < 1000) {
                        sId = "CUL-000" + id;
                    } else if (id < 10000) {
                        sId = "CUL-00" + id;
                    } else if (id < 100000) {
                        sId = "CUL-0" + id;
                    } else {
                        sId = "CUL-" + id;
                    }
                }
                fileName = sId + fileName.substring(fileName.lastIndexOf("."));
                file.renameTo(new File(camPicPath, fileName));
                culture.setId(sId);
                culture.setPic(fileName);
                Culture.insert(culture);
            }
            return redirect("/listCulture");
        }
    }

    public static Result editCulture(String id) {
        session().remove("sunHeader");
        culture = Culture.finder.byId(id);
        if (culture == null) {
            flash("msgError", "อย่าเเสดงมาเเก้ URL!");
            return redirect("/listCulture");
        } else {
            cultureForm = Form.form(Culture.class).fill(culture);
            return main(editCulture.render(cultureForm));
        }
    }
    public static Result updateCulture (){
        session().remove("sunHeader");
        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart myfile = body.getFile("pic");
        Form<Culture> dataupdata = cultureForm.bindFromRequest();
        if (dataupdata.hasErrors()) {
            flash("msgError", "ข้อมูลไม่ถูกต้อง กรุณาตรวจสอบใหม่ด่วยค่ะ!");
            return main(editCulture.render(cultureForm));
        } else {
            culture = dataupdata.get();
            if (myfile != null) {
                String fileName = myfile.getFilename();
                String extension = fileName.substring(fileName.indexOf("."));
                String realName = culture.getId() + extension;
                File file = myfile.getFile();
                File temp = new File("public/images/" + realName);
                if (temp.exists()) {
                    temp.delete();
                }
                file.renameTo(new File("public/images/" + realName));
                culture.setPic(realName);
            }
            Culture.update(culture);
            return redirect("/listCulture");
        }
    }

    public static Result deleteCulture(String id){
        session().remove("sunHeader");
        culture = Culture.finder.byId(id);
        if (culture != null) {
            File temp = new File("public/images/" + culture.getPic());
            temp.delete();
            Culture.delete(culture);
        }
        return redirect("/listCulture");
    }

    public static Result showListCulture (){
        session().remove("sunHeader");
        cultureList = Culture.cultureList();
        return main(views.html.culture.render(cultureList));
    }

    public static Result detailCulture(String id){
        session().remove("sunHeader");
        culture = Culture.finder.byId(id);
        return main(detailCulture.render(culture));

    }

    public static List<Travel> travelList = new ArrayList<Travel>();
    public static Travel travel;
    public static Form<Travel > travelForm = Form.form(Travel.class);

    public static Result listTravel (){
        session().remove("sunHeader");
        travelList = Travel.travelList();
        return main(listTravel.render(travelList));
    }

    public static Result addTravel (){
        session().remove("sunHeader");
        travelForm = Form.form(Travel.class);
        return main(formAddTravel.render(travelForm));
    }

    public static Result insertTravel() {
        session().remove("sunHeader");
        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart picture = body.getFile("pic");
        String fileName, contentType;
        Form<Travel> newProduct = travelForm.bindFromRequest();
        if (newProduct.hasErrors()) {
            flash("msgError", "คุณป้อมข้อมูลไม่ถูกต้อง กรุณาตรวจสอบใหม่ด่วยค่ะ!");
            return main(formAddTravel.render(travelForm));
        } else {
            travel = newProduct.get();
            if (picture != null) {

                contentType = picture.getContentType();
                File file = picture.getFile();
                fileName = picture.getFilename();
                if (!contentType.startsWith("image")) {
                    flash("msgError", "นามสกุลภาพไม่ตรงตามชนิด กรุณาตรวจสอบใหม่ด่วยค่ะ!");
                    return main(formAddTravel.render(travelForm));
                }
                travelList = Travel.travelList();
                int numlist = travelList.size();
                int id, idNum;
                String lastNum;
                String sId;
                if (travelList.size() == 0) {
                    sId = "sss-000001";
                } else {
                    lastNum = travelList.get(numlist - 1).getId();
                    lastNum = lastNum.substring(4);
                    idNum = Integer.parseInt(lastNum);
                    id = idNum + 1;
                    if (id < 10) {
                        sId = "sss-00000" + id;
                    } else if (id < 100) {
                        sId = "sss-0000" + id;
                    } else if (id < 1000) {
                        sId = "sss-000" + id;
                    } else if (id < 10000) {
                        sId = "sss-00" + id;
                    } else if (id < 100000) {
                        sId = "sss-0" + id;
                    } else {
                        sId = "sss-" + id;
                    }
                }
                fileName = sId + fileName.substring(fileName.lastIndexOf("."));
                file.renameTo(new File(camPicPath, fileName));
                travel.setId(sId);
                travel.setPic(fileName);
                Travel.insert(travel);
            }
            return redirect("/listTravel");
        }
    }

    public static Result editTravel(String id) {
        session().remove("sunHeader");
        travel = Travel.finder.byId(id);
        if (travel == null) {
            flash("msgError", "อย่าเเสดงมาเเก้ URL!");
            return redirect("/listTravel");
        } else {
            travelForm = Form.form(Travel.class).fill(travel);
            return main(editTravel.render(travelForm));
        }
    }

    public static Result updateTravel (){
        session().remove("sunHeader");
        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart myfile = body.getFile("pic");
        Form<Travel> dataupdata = travelForm.bindFromRequest();
        if (dataupdata.hasErrors()) {
            flash("msgError", "ข้อมูลไม่ถูกต้อง กรุณาตรวจสอบใหม่ด่วยค่ะ!");
            return main(editTravel.render(travelForm));
        } else {
            travel = dataupdata.get();
            if (myfile != null) {
                String fileName = myfile.getFilename();
                String extension = fileName.substring(fileName.indexOf("."));
                String realName = travel.getId() + extension;
                File file = myfile.getFile();
                File temp = new File("public/images/" + realName);
                if (temp.exists()) {
                    temp.delete();
                }
                file.renameTo(new File("public/images/" + realName));
                travel.setPic(realName);
            }
            Travel.update(travel);
            return redirect("/listTravel");
        }
    }

    public static Result deleteTravel(String id){
        session().remove("sunHeader");
        travel = Travel.finder.byId(id);
        if (travel != null) {
            File temp = new File("public/images/" + travel.getPic());
            temp.delete();
            Travel.delete(travel);
        }
        return redirect("/listTravel");
    }

    public static Result showListTravel (){
        session().remove("sunHeader");
        travelList = Travel.travelList();
        return main(attractions.render(travelList));
    }

    public static Result detailTravel(String id){
        session().remove("sunHeader");
        travel = Travel.finder.byId(id);
        return main(detailTravel.render(travel));
    }

}
