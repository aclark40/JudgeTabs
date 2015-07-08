package com.example.andrew.judgetabs.Objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by root on 6/24/15.
 */
public class User implements Parcelable {

    private int id;
    private String email;
    private String name;
    private boolean admin;
    private Token token;

    public User(int id, String email, String name, boolean admin, Token token) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.admin = admin;
        this.token = token;
    }


    protected User(Parcel in) {
        id = in.readInt();
        email = in.readString();
        name = in.readString();
        admin = in.readByte() != 0x00;
        token = (Token) in.readValue(Token.class.getClassLoader());
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public boolean isAdmin() {
        return admin;
    }

    public Token getToken() {
        return token;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(email);
        dest.writeString(name);
        dest.writeByte((byte) (admin ? 0x01 : 0x00));
        dest.writeValue(token);
    }

    @SuppressWarnings("unused")
    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}