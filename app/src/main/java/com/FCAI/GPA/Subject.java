package com.FCAI.GPA;

import android.os.Parcel;
import android.os.Parcelable;

public class Subject implements Parcelable {
    String name;
    int hours;
    Double grade;

    protected Subject(Parcel in) {
        name = in.readString();
        hours = in.readInt();
        if (in.readByte() == 0) {
            grade = null;
        } else {
            grade = in.readDouble();
        }
    }

    public static final Creator<Subject> CREATOR = new Creator<Subject>() {
        @Override
        public Subject createFromParcel(Parcel in) {
            return new Subject(in);
        }

        @Override
        public Subject[] newArray(int size) {
            return new Subject[size];
        }
    };

    public Subject() {

    }

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(hours);
        if (grade == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(grade);
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }
}
