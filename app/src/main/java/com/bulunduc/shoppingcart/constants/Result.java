package com.bulunduc.shoppingcart.constants;

public enum Result {
    CANCEL(0), OK(1), DELETE(2);
    private final int code;
    Result(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
