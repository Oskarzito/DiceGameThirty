package com.bignerdranch.android.dicegamethirty;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Random;

/**
 * Created by Oskar Emilsson
 */

public class Die implements Parcelable {

    private boolean mIsMarked = false;
    private static Random sRandomGenerator;
    private int mValueOfSide;
    private boolean mIsCountend = false;

    public Die(int valueOfSide){
        this.mValueOfSide = valueOfSide;
    }

    //Check if die is marked / saved. If not - Roll!
    public int rollDice() {

        if(!mIsMarked) {
            sRandomGenerator = new Random();
            int rolledValue = sRandomGenerator.nextInt(6) + 1;
            mValueOfSide = rolledValue;
        }
        return mValueOfSide;
    }

    //Getters-setters
    public int getValue(){
        return mValueOfSide;
    }

    public void setMarked(boolean isMarked){
        this.mIsMarked = isMarked;
    }

    public boolean isMarked(){
        return mIsMarked;
    }

    public void setCountend(boolean mIsCountend){
        this.mIsCountend = mIsCountend;
    }

    public boolean isCountend(){
        return mIsCountend;
    }

    //Parcelable below
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.mIsMarked ? (byte) 1 : (byte) 0);
        dest.writeInt(this.mValueOfSide);
        dest.writeByte(this.mIsCountend ? (byte) 1 : (byte) 0);
    }

    protected Die(Parcel in) {
        this.mIsMarked = in.readByte() != 0;
        this.mValueOfSide = in.readInt();
        this.mIsCountend = in.readByte() != 0;
    }

    public static final Creator<Die> CREATOR = new Creator<Die>() {
        @Override
        public Die createFromParcel(Parcel source) {
            return new Die(source);
        }

        @Override
        public Die[] newArray(int size) {
            return new Die[size];
        }
    };
}