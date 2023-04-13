package com.example.schedule.presentation.login;

import java.util.Objects;

public class LoginState {

    private LoginError loginError;
    private boolean userExists;

    public LoginState(LoginError loginError, boolean userExists) {
        this.loginError = loginError;
        this.userExists = userExists;
    }

    public LoginState() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginState that = (LoginState) o;
        return userExists == that.userExists && Objects.equals(loginError, that.loginError);
    }

    @Override
    public int hashCode() {
        return Objects.hash(loginError, userExists);
    }

    public LoginError getLoginError() {
        return loginError;
    }

    public void setLoginError(LoginError loginError) {
        this.loginError = loginError;
    }

    public boolean isUserExists() {
        return userExists;
    }

    public void setUserExists(boolean userExists) {
        this.userExists = userExists;
    }
}
