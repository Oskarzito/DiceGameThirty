package com.bignerdranch.android.dicegamethirty;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

/**
 * Created by Oskar Emilsson
 */

public class Round implements Parcelable {

    private int mSumToReach;
    private int mReachedSum = 0;
    private boolean isLowRound = false;

    private int mThrowsLeft = 3;

    //Constructor for a sum to aim for
    public Round(int sumToReach){
        this.mSumToReach = sumToReach;
    }

    //Constructor if the rule chosen is "Low" (isLowRound == true).
    //If you send "false", IllegalArgumentException is thrown
    public Round(boolean isLowRound) throws IllegalArgumentException{
        if(!isLowRound)
            throw new IllegalArgumentException();
        else
            this.isLowRound = isLowRound;
    }

    //Recieves unknown amount of numbers (dice values).
    //Checks if the sum of these numbers equals
    public boolean calculationIsCorrect(List<Integer> numbersToAdd){

        boolean userIsCorrect = true;

        int temp = 0;
        for(int number : numbersToAdd){
            temp += number;
        }

        if(isLowRound){
            for(int n : numbersToAdd){
                if(n > 3){
                    userIsCorrect = false;
                }
            }

        }else{

            return temp == mSumToReach;

        }
        return userIsCorrect;
    }

    //Add value to the result reached this round
    public void addResultSum(int result){
        mReachedSum += result;
    }

    //Lower the amount of throws left by one
    public void decreaseThrowsLeft(){
        mThrowsLeft--;
    }

    //Return throws left
    public int getThrowsLeft(){
        return mThrowsLeft;
    }

    //Used to compare and sort the results
    public int getSumToReach(){
        return mSumToReach;
    }

    //Returns round result
    public int getResult(){
        return mReachedSum;
    }

    //See if the user has throws left
    public boolean hasThrowsLeft(){
        if(mThrowsLeft > 0)
            return true;
        else
            return false;
    }

    //Sets the amount of throws left to 0. Makes user unable to throw again
    public void finishRound(){
        mThrowsLeft = 0;
    }

    //Return what kind of round this instance is
    public String toString(){
        if(isLowRound)
            return "Low";
        else
            return "" + mSumToReach;
    }

    //Everything below makes the class "Round" Parcelable.
    //You can now pass them in a Bundle to save the state
    @Override
    public int describeContents() {
        return 0;
    }

    //Compare Rounds
    @Override
    public boolean equals(Object other) {
        if(other instanceof Round){
            Round r = (Round) other;
            return (this.mThrowsLeft == r.mThrowsLeft && this.isLowRound == r.isLowRound &&
                    this.mSumToReach == r.mSumToReach && mReachedSum == r.mReachedSum);
        } else {
            return false;
        }
    }

    //Override hashCode for consistensy and convention when you override equals.
    @Override
    public int hashCode() {
        int result = mSumToReach;
        result = 31 * result + mReachedSum;
        result = 31 * result + (isLowRound ? 1 : 0);
        result = 31 * result + mThrowsLeft;
        return result;
    }

    //Parcelable below
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mThrowsLeft);
        dest.writeInt(this.mReachedSum);
        dest.writeByte(this.isLowRound ? (byte) 1 : (byte) 0);
        dest.writeInt(this.mSumToReach);
    }

    protected Round(Parcel in) {
        this.mThrowsLeft = in.readInt();
        this.mReachedSum = in.readInt();
        this.isLowRound = in.readByte() != 0;
        this.mSumToReach = in.readInt();
    }

    public static final Parcelable.Creator<Round> CREATOR = new Parcelable.Creator<Round>() {
        @Override
        public Round createFromParcel(Parcel source) {
            return new Round(source);
        }

        @Override
        public Round[] newArray(int size) {
            return new Round[size];
        }
    };
}
