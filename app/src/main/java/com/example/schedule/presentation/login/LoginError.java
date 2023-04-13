package com.example.schedule.presentation.login;

public class LoginError {

    private String message;

    public LoginError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
