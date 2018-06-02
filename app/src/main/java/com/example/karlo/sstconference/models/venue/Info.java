
package com.example.karlo.sstconference.models.venue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Info {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;

    @SerializedName("link")
    @Expose
    private String link;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("marker")
    @Expose
    private VenueMarker marker = null;

    public Info() {
    }

    public Info(Integer id, String description, String imageUrl, String link, String title, VenueMarker marker) {
        this.id = id;
        this.description = description;
        this.imageUrl = imageUrl;
        this.link = link;
        this.title = title;
        this.marker = marker;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public VenueMarker getMarker() {
        return marker;
    }

    public void setMarker(VenueMarker markers) {
        this.marker = markers;
    }

}
