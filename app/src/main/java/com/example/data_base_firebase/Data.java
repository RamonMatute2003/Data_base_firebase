package com.example.data_base_firebase;

public class Data{
    private String names;
    private String surnames;
    private String forum;
    private String birthdate;
    private String gender;

    public Data(){

    }

    public Data(String names, String surnames, String forum, String birthdate, String gender) {
        this.names = names;
        this.surnames = surnames;
        this.forum = forum;
        this.birthdate = birthdate;
        this.gender = gender;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getSurnames() {
        return surnames;
    }

    public void setSurnames(String surnames) {
        this.surnames = surnames;
    }

    public String getForum() {
        return forum;
    }

    public void setForum(String forum) {
        this.forum = forum;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
