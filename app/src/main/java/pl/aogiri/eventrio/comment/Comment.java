package pl.aogiri.eventrio.comment;


import java.time.Instant;

import pl.aogiri.eventrio.user.User;

public class Comment {

    private String id;

    private User user;

    private String content;

    private String date;



    public Comment() {
        super();
    }

    public Comment(String id, User user, String content, String date) {
        super();
        this.id = id;
        this.user = user;
        this.content = content;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
