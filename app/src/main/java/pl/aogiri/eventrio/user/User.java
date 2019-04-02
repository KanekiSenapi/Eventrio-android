package pl.aogiri.eventrio.user;

import java.util.List;

import pl.aogiri.eventrio.event.Event;
import pl.aogiri.eventrio.notifi.Notifi;

public class User {


    private String id;

    private String email;

    private String password;

    private String pseudonym;

    private String gender;

    private String birthday;

    private String fbid;

    private String picture;

    private List<Notifi> notifis;

    private List<Event> events;

    public User() {
    }

    public User(String id, String email, String password, String pseudonym, String gender, String birthday, String fbid, String picture, List<Notifi> notifis, List<Event> events) {
        super();
        this.id = id;
        this.email = email;
        this.password = password;
        this.pseudonym = pseudonym;
        this.gender = gender;
        this.birthday = birthday;
        this.fbid = fbid;
        this.notifis = notifis;
        this.events = events;
        this.picture = picture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPseudonym() {
        return pseudonym;
    }

    public void setPseudonym(String pseudonym) {
        this.pseudonym = pseudonym;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getFbid() {
        return fbid;
    }

    public void setFbid(String fbid) {
        this.fbid = fbid;
    }

    public List<Notifi> getNotifis() {
        return notifis;
    }

    public void setNotifis(List<Notifi> notifis) {
        this.notifis = notifis;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
