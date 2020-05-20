package com.wisewolf.njmschool.Exceptions;


public class VimeoException extends Exception {

    VimeoExceptionEnumType vimeoException;

    public VimeoException(String message, VimeoExceptionEnumType type) {

        super(message);
        vimeoException = type;
    }

    public VimeoExceptionEnumType getType() {
        return vimeoException;
    }

}
