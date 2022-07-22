package com.ex.assignment.model;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

public class UnSplashPicture {
    public int total;
    public int total_pages;
    public ArrayList<Result> results;

    public static class BusinessWork {
        public String status;
        public Date approved_on;
    }
    public static class ArchitectureInterior{
        public String status;
        public Date approved_on;
    }
    public static class Covid19 {
        public String status;
        public Date approved_on;
    }

    public static class Interiors {
        public String status;
        public Date approved_on;
    }

    public static class Links {
        public String self;
        public String html;
        public String download;
        public String download_location;
        public String photos;
        public String likes;
        public String portfolio;
        public String following;
        public String followers;
    }

    public static class ProfileImage {
        public String small;
        public String medium;
        public String large;
    }

    public static class Result {
        public String id;
        public Date created_at;
        public Date updated_at;
        public Date promoted_at;
        public int width;
        public int height;
        public String color;
        public String blur_hash;
        public String description;
        public String alt_description;
        public Urls urls;
        public Links links;
        public ArrayList<Object> categories;
        public int likes;
        public boolean liked_by_user;
        public ArrayList<Object> current_user_collections;
        public Object sponsorship;
        public TopicSubmissions topic_submissions;
        public User user;
        public ArrayList<Tag> tags;

        public String getAlt_description() {
            return alt_description == null ? description : alt_description;
        }
        public String getThumbnail() {
            if(urls == null)
                return "";
            if(urls.thumb == null || urls.thumb.isEmpty())
                return urls.small;
            return urls.thumb;
        }
        public String getName(){
            if(user == null ||user.getName() == null)
                return "No Name";
            return user.getName();
        }

        public boolean isEmpty() {
            return id == null || id.isEmpty();
        }
    }


    public static class Social {
        public String instagram_username;
        public String portfolio_url;
        public String twitter_username;
        public Object paypal_email;
    }

    public static class Tag {
        public String type;
        public String title;
    }

    public class TopicSubmissions {
        @SerializedName("business-work")
        public BusinessWork businessWork;
        public Interiors interiors;
        public Wallpapers wallpapers;
        @SerializedName("covid-19")
        public Covid19 covid19;
        @SerializedName("architecture-interior")
        public ArchitectureInterior architectureInterior;
    }

    public static class Urls {
        public String raw;
        public String full;
        public String regular;
        public String small;
        public String thumb;//could be null
        public String small_s3;
    }

    public static class User {
        public String id;
        public Date updated_at;
        public String username;
        public String name;
        public String first_name;
        public String last_name;
        public String twitter_username;
        public String portfolio_url;
        public String bio;
        public String location;
        public Links links;
        public ProfileImage profile_image;
        public String instagram_username;
        public int total_collections;
        public int total_likes;
        public int total_photos;
        public boolean accepted_tos;
        public boolean for_hire;
        public Social social;

        public String getName() {
            return name == null ? first_name + " " + last_name : name;
        }

        public String getPortfolio_url() {
            return portfolio_url;
        }
        public boolean isPortfolio_url() {
            return portfolio_url != null && !portfolio_url.isEmpty();
        }
    }

    public static class Wallpapers {
        public String status;
        public Date approved_on;
    }
}

