package com.example.taskdeadlinetracker.module;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import java.util.Date;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "users")
public class Entity_User implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "username")
    private String username;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "password_hash")
    private String passwordHash;

    @ColumnInfo(name = "created_at")
    private Date createdAt;

    @ColumnInfo(name = "updated_at")
    private Date updatedAt;

    // --- Constructor mặc định (bắt buộc cho Room) ---
    public Entity_User() {}

    // --- Getter & Setter cho tất cả trường ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }

    // --- Parcelable code (nếu muốn truyền qua Intent) ---
    protected Entity_User(Parcel in) {
        id = in.readInt();
        username = in.readString();
        email = in.readString();
        passwordHash = in.readString();
        long tmpCreatedAt = in.readLong();
        createdAt = tmpCreatedAt == -1 ? null : new Date(tmpCreatedAt);
        long tmpUpdatedAt = in.readLong();
        updatedAt = tmpUpdatedAt == -1 ? null : new Date(tmpUpdatedAt);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(username);
        dest.writeString(email);
        dest.writeString(passwordHash);
        dest.writeLong(createdAt != null ? createdAt.getTime() : -1);
        dest.writeLong(updatedAt != null ? updatedAt.getTime() : -1);
    }

    @Override
    public int describeContents() { return 0; }

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
}
