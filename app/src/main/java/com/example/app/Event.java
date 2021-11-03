package com.example.app;

import com.google.firebase.firestore.Exclude;

public class Event {

    private String eventTitle;
    private String eventLocation;
    private String eventStartTimeStamp;
    private String eventEndTimeStamp; // Not used
    private String eventJoinLink; // Not used
    private String eventRecLink;
    private String eventShortDescription;
    private String eventLongDescription;
    private String eventImageUrl;
    private String id;

    public Event(String eventTitle, String eventLocation, String eventStartTimeStamp, String eventEndTimeStamp, String eventJoinLink, String eventRecLink, String eventShortDescription, String eventLongDescription, String eventImageUrl) {
        this.eventTitle = eventTitle;
        this.eventLocation = eventLocation;
        this.eventStartTimeStamp = eventStartTimeStamp;
        this.eventEndTimeStamp = eventEndTimeStamp;
        this.eventJoinLink = eventJoinLink;
        this.eventRecLink = eventRecLink;
        this.eventShortDescription = eventShortDescription;
        this.eventLongDescription = eventLongDescription;
        this.eventImageUrl = eventImageUrl;
    }

    public Event(){

    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventStartTimeStamp() {
        return eventStartTimeStamp;
    }

    public void setEventStartTimeStamp(String eventStartTimeStamp) {
        this.eventStartTimeStamp = eventStartTimeStamp;
    }

    public String getEventEndTimeStamp() {
        return eventEndTimeStamp;
    }

    public void setEventEndTimeStamp(String eventEndTimeStamp) {
        this.eventEndTimeStamp = eventEndTimeStamp;
    }

    public String getEventJoinLink() {
        return eventJoinLink;
    }

    public void setEventJoinLink(String eventJoinLink) {
        this.eventJoinLink = eventJoinLink;
    }

    public String getEventRecLink() {
        return eventRecLink;
    }

    public void setEventRecLink(String eventRecLink) {
        this.eventRecLink = eventRecLink;
    }

    public String getEventShortDescription() {
        return eventShortDescription;
    }

    public void setEventShortDescription(String eventShortDescription) {
        this.eventShortDescription = eventShortDescription;
    }

    public String getEventLongDescription() {
        return eventLongDescription;
    }

    public void setEventLongDescription(String eventLongDescription) {
        this.eventLongDescription = eventLongDescription;
    }

    public String getEventImageUrl() {
        return eventImageUrl;
    }

    public void setEventImageUrl(String eventImageUrl) {
        this.eventImageUrl = eventImageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
