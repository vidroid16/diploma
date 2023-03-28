package com.shalya.diploma.exceptions;

public class NoPermissionException extends Exception{
    public NoPermissionException(String username) {
        super(String.format("User %s has no permission to access this object", username));
    }
}
