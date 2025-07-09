package com.example.taskdeadlinetracker.module;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Date;

/*
* Trong Android, Parcelable là một interface
* dùng để tuần tự hóa (serialize) đối tượng —
* giúp bạn truyền dữ liệu giữa các Activity, Fragment,
* hoặc Service thông qua Intent.
* */
public class Entity_User implements Parcelable {
    private int id;
    private String userName;
    private String email;
    private String passwordHash;
    private Date createdAt;
    private Date updatedAt;

    public Entity_User() {}

    // Parcelable constructor
    protected Entity_User(Parcel in) {
        id = in.readInt();
        userName = in.readString();
        email = in.readString();
        passwordHash = in.readString();
        long ca = in.readLong();
        createdAt = ca == -1 ? null : new Date(ca);
        long ua = in.readLong();
        updatedAt = ua == -1 ? null : new Date(ua);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(userName);
        dest.writeString(email);
        dest.writeString(passwordHash);
        dest.writeLong(createdAt != null ? createdAt.getTime() : -1);
        dest.writeLong(updatedAt != null ? updatedAt.getTime() : -1);
    }

    @Override public int describeContents() { return 0; }

    public static final Creator<Entity_User> CREATOR = new Creator<Entity_User>() {
        @Override
        public Entity_User createFromParcel(Parcel in) {
            return new Entity_User(in);
        }
        @Override
        public Entity_User[] newArray(int size) {
            return new Entity_User[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
