package com.EX.examcreator.Models;

public class ImageProfile {
    public String idProfile;
    public String idDr_Profile;
    public String url;

    public ImageProfile() {
    }

    public ImageProfile(String idProfile, String idDr_Profile, String url) {
        this.idProfile = idProfile;
        this.idDr_Profile = idDr_Profile;
        this.url = url;
    }

    public String getIdProfile() {
        return idProfile;
    }

    public String getIdDr_Profile() {
        return idDr_Profile;
    }

    public String getUrl() {
        return url;
    }
}
