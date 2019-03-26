package pl.aogiri.eventrio.notifi;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Notifi {

    private Integer id;

    private String title;

    private String date;

    private boolean showed;

    private String category;

    public Notifi() {
    }

    public Notifi(Integer id, String title, String date, boolean showed, String category) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.showed = showed;
        this.category = category;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        setTimeText(date);
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isShowed() {
        return showed;
    }

    public void setShowed(boolean showed) {
        this.showed = showed;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    private void setTimeText(String date){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss.SSS'Z'");
        try {
            Date result =  df.parse(date);
            this.date = result.toString();
            long time = (new Date().getTime() - result.getTime()) / 1000;
            if(time<60)
                this.date = "Just now!";
            else if(time<3600)
                this.date = String.valueOf((int)time/60) + "m";
            else if(time<216000)
                this.date = String.valueOf((int)time/60/60) + "h";
            else if(time<5184000)
                this.date = String.valueOf((int)time/60/60/24) + "d";
            else if(time<155520000)
                this.date = String.valueOf((int)time/60/60/24/30) + "month";
            else
                this.date = String.valueOf((int)time/60/60/24/30/365) + "years";


        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
