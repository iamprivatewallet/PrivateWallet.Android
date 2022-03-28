package com.fanrong.frwallet.wallet.cwv.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class BaseResponse<T> {

    @SerializedName("err_code")
    public String code;

    public String msg;

    public T data;

    public Status status;

    public BaseResponse() {

    }

    private BaseResponse(@NonNull Status status, @Nullable String message, @Nullable T data) {
        this.status = status;
        this.msg = message;
        this.data = data;
    }

    public static <T> BaseResponse<T> success(@NonNull T data) {
        return new BaseResponse<>(Status.SUCCESS, null, data);
    }

    public static <T> BaseResponse<T> error(String msg, @Nullable T data) {
        return new BaseResponse<>(Status.ERROR, msg, data);
    }

    public static <T> BaseResponse<T> loading(@Nullable T data) {
        return new BaseResponse<>(Status.LOADING, null, data);
    }

    public enum Status {SUCCESS, ERROR, LOADING}

}
