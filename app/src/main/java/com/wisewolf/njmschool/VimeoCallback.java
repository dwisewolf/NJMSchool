package com.wisewolf.njmschool;


import com.wisewolf.njmschool.Exceptions.VimeoException;


public interface VimeoCallback {
    public void vimeoURLCallback(String callback);

    public void videoExceptionCallback(VimeoException exceptionCallback);
}
