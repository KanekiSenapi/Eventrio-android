package pl.aogiri.eventrio.event;

import java.util.List;

import pl.aogiri.eventrio.comment.Comment;
import pl.aogiri.eventrio.tag.Tag;
import pl.aogiri.eventrio.user.User;


public class Event {


    private String id;

    private String name;

    private double lat;

    private double lng;

    private String dateBeg;

    private String dateEnd;

    private String image;

    private String address;

    private int status;

    private String description;

    private User organizer;

    private List<Tag> tags;

    private List<Comment> comments;


    public Event() {
    }

    public Event( String id,  String name,  double lat,  double lng,  String dateBeg,
                  String dateEnd,  String image, String address, int status, String description, User organizer,List<Tag> tags, List<Comment> comments) {
        super();
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.dateBeg = dateBeg;
        this.dateEnd = dateEnd;
        this.image = image;
        this.address = address;
        this.status = status;
        this.image = image;
        this.description = description;
        this.organizer = organizer;
        this.tags = tags;
        this.comments = comments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getDateBeg() {
        return dateBeg;
    }

    public void setDateBeg(String dateBeg) {
        this.dateBeg = dateBeg;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public String getAddress(){
        return this.address;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public String getDescriptionSub() {
        if (description.length()>30)
            return description.substring(0,29)+"...";
        return description;
    }



    public void setDescription(String description) {
        this.description = description;
    }

    public User getOrganizer() {
        return organizer;
    }

    public void setOrganizer(User organizer) {
        this.organizer = organizer;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}

