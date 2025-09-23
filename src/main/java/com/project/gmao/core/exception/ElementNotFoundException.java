package com.project.gmao.core.exception;


public class ElementNotFoundException extends RuntimeException{
     public ElementNotFoundException (String message){
        super(message);
     }

       public ElementNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
