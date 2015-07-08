package com.example.andrew.judgetabs.Objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by root on 6/25/15.
 */

public class Token implements Parcelable {

    private int id;
    private String access_token;
    private String expires_at;
    private int user_id;
    private String created_at;
    private String updated_at;

    public Token(int id, String access_token, String expires_at, int user_id, String created_at,
                 String updated_at) {
        this.id = id;
        this.access_token = access_token;
        this.expires_at = expires_at;
        this.user_id = user_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    protected Token(Parcel in) {
        id = in.readInt();
        access_token = in.readString();
        expires_at = in.readString();
        user_id = in.readInt();
        created_at = in.readString();
        updated_at = in.readString();
    }

    public int getId() {
        return id;
    }

    public String getAccess_token() {
        return access_token;
    }

    public String getExpires_at() {
        return expires_at;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(access_token);
        dest.writeString(expires_at);
        dest.writeInt(user_id);
        dest.writeString(created_at);
        dest.writeString(updated_at);
    }

    @SuppressWarnings("unused")
    public static final Creator<Token> CREATOR = new Creator<Token>() {
        @Override
        public Token createFromParcel(Parcel in) {
            return new Token(in);
        }

        @Override
        public Token[] newArray(int size) {
            return new Token[size];
        }
    };
}
