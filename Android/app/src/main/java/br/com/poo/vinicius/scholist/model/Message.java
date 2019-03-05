package br.com.poo.vinicius.scholist.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Message implements Parcelable {
    private String text;
    private long timeStamp;
    private String fromId;
    private String id;

    public Message() {}

    protected Message(Parcel in) {
        text = in.readString();
        timeStamp = in.readLong();
        fromId = in.readString();
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fromId);
        dest.writeLong(timeStamp);
        dest.writeString(text);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
