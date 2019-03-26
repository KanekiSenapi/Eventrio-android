package pl.aogiri.eventrio.comment;


import pl.aogiri.eventrio.user.User;

public class Comment {

    private String id;

    private String content;

    private String date;

    private User commentator;


    public Comment() {
        super();
    }

    public Comment(String id, String content, String date, User commentator) {
        this.id = id;
        this.content = content;
        this.date = date;
        this.commentator = commentator;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public User getCommentator() {
        return commentator;
    }

    public void setCommentator(User commentator) {
        this.commentator = commentator;
    }
}
