package com.example.taskdeadlinetracker.module;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Date;

public class Entity_Task implements Parcelable {
    private int id;
    private int userId;
    private String title;
    private String description;
    private Date deadline;
    private String priority;
    private boolean isComplete;
    private Date createdAt;
    private Date updatedAt;

    public Entity_Task() {}

    protected Entity_Task(Parcel in) {
        id = in.readInt();
        userId = in.readInt();
        title = in.readString();
        description = in.readString();
        long dl = in.readLong();
        deadline = dl == -1 ? null : new Date(dl);
        priority = in.readString();
        isComplete = in.readByte() != 0;
        long ca = in.readLong();
        createdAt = ca == -1 ? null : new Date(ca);
        long ua = in.readLong();
        updatedAt = ua == -1 ? null : new Date(ua);
    }

    public Entity_Task(int i, int i1, String s, String description, Date deadline, String priority, boolean b) {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(userId);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeLong(deadline != null ? deadline.getTime() : -1);
        dest.writeString(priority);
        dest.writeByte((byte) (isComplete ? 1 : 0));
        dest.writeLong(createdAt != null ? createdAt.getTime() : -1);
        dest.writeLong(updatedAt != null ? updatedAt.getTime() : -1);
    }

    @Override public int describeContents() { return 0; }

    public static final Creator<Entity_Task> CREATOR = new Creator<Entity_Task>() {
        @Override
        public Entity_Task createFromParcel(Parcel in) {
            return new Entity_Task(in);
        }
        @Override
        public Entity_Task[] newArray(int size) {
            return new Entity_Task[size];
        }
    };

    // Getters & setters omitted

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIsComplete(boolean isCompleted) {
        this.isComplete = isCompleted;
    }
}
