package com.example.emailsender.DTO;

public class BookExpiredDTO {
    private String title;
    private String ownerEmail;

    public BookExpiredDTO() {
    }

    public BookExpiredDTO(String title, String ownerEmail) {
        this.title = title;
        this.ownerEmail = ownerEmail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }
}
