package com.example.rathana.facebook_logiin;

public class User {

    public String fbId;
    public String lastName;
    public String firstName;
    public String email;
    public String profile;
    public String dob;

    public User(String fbId, String lastName, String firstName, String email, String profile, String dob) {
        this.fbId = fbId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.profile = profile;
        this.dob = dob;
    }

    @Override
    public String toString() {
        return "User{" +
                "fbId='" + fbId + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", email='" + email + '\'' +
                ", profile='" + profile + '\'' +
                ", dob='" + dob + '\'' +
                '}';
    }
}

