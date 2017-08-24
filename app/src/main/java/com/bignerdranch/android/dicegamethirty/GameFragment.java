package com.bignerdranch.android.dicegamethirty;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Oskar Emilsson
 */

public class GameFragment extends Fragment {

    private Button mThrowButton;
    private Button mRuleButton;
    private Button mResultButton;
    private Button mAddValuesButton;
    private Button mFinishRoundButton;
    private Button mDoneThrowingButton;
    private final int AMOUNT_OF_DICE = 6;
    private static final int RULE_FRAGMENT = 0;
    private static final int RESULT_FRAGMENT = 1;
    public static final String RESULTS_BUNDLE_TO_RESULT_ACTIVITY = "resBundle";
    public static final String ROUNDS_PLAYED = "roundsPlayed";
    public static final String RESULTS_LIST = "resultsList";
    private final String ACTIVE_ROUND = "activeRound";
    private final String CALCULATE_BUTTON_STATE = "calcBtnState";
    private final String ALL_DICE = "allDice";
    private ArrayList<Round> mRounds = new ArrayList<>();
    private TextView mThrowCounter;
    private TextView reminderLabel;
    private boolean doneThrowingButtonIsClicked = false;
    private Round mActiveRound = null;
    private ImageView[] mDiceViews;

    //Create 6 dice and adds them in an array.
    private Die[] mDice = {
            new Die(1),
            new Die(2),
            new Die(3),
            new Die(4),
            new Die(5),
            new Die(6)
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Restore all dice, the active round and the list of previously played rounds
        if (savedInstanceState != null) {
            mRounds = savedInstanceState.getParcelableArrayList(RESULTS_LIST);

            //This is to check so that onCreate doesn't overwrite the active round that was set in
            //onActivityResult if the screen was rotated while the user was in RuleFragment
            if(mActiveRound == null) {
                mActiveRound = savedInstanceState.getParcelable(ACTIVE_ROUND);
            }

            //Continue from above. If the round is NOT null and is NOT in the list of rounds, add it
            if (!mRounds.contains(mActiveRound) && mActiveRound != null) {
                mRounds.add(mActiveRound);
            }

            mDice = (Die[]) savedInstanceState.getParcelableArray(ALL_DICE);
            doneThrowingButtonIsClicked = savedInstanceState.getBoolean(CALCULATE_BUTTON_STATE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        //Save all dice state, all previously played rounds and the active round if there is one
        savedInstanceState.putParcelableArray(ALL_DICE, mDice);
        savedInstanceState.putParcelableArrayList(RESULTS_LIST, mRounds);
        savedInstanceState.putBoolean(CALCULATE_BUTTON_STATE, doneThrowingButtonIsClicked);
        savedInstanceState.putParcelable(ACTIVE_ROUND, mActiveRound);

    }

    //Get the ImageView. Adds a listener to it. Sets the correct image corresponding die value
    private void setUpDiceWithListenersAndImages(View v) {
        //Add all ImageViews (Dice on screen) to an array.
        mDiceViews = new ImageView[]{
                (ImageView) v.findViewById(R.id.iv_die1),
                (ImageView) v.findViewById(R.id.iv_die2),
                (ImageView) v.findViewById(R.id.iv_die3),
                (ImageView) v.findViewById(R.id.iv_die4),
                (ImageView) v.findViewById(R.id.iv_die5),
                (ImageView) v.findViewById(R.id.iv_die6)
        };

        //Add a listener to each ImageView.
        //If a die has been counted in a round.
        //Don't give it a listener
        DieListener dl = new DieListener();
        for (int i = 0; i < AMOUNT_OF_DICE; i++) {
            if (mDice[i].isCountend())
                continue;
            else
                mDiceViews[i].setOnClickListener(dl);
        }

        //Set up correct ImageView on screen
        //with the value of the die
        for (int i = 0; i < AMOUNT_OF_DICE; i++) {
            ImageView imageDie = mDiceViews[i];
            int dieValue = mDice[i].getValue();

            if (mDice[i].isMarked())
                setInActiveDieImage(dieValue, imageDie);
            else
                setActiveDieImage(dieValue, imageDie);

            if(mDice[i].isCountend())
                setCountedDieImage(dieValue, imageDie);
        }
    }

    //Listener for the Dice
    private class DieListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            //Only react if there is an active round and the dice has been thrown at least one time
            if (mActiveRound != null && mActiveRound.getThrowsLeft() < 3) {

                //Match clicked die with the die in the dice array.
                //Make the clicked die marked / unmarked and set the appropriate image (white or red)
                for (int i = 0; i < AMOUNT_OF_DICE; i++) {
                    if (v.getId() == mDiceViews[i].getId()) {
                        Die d = mDice[i];
                        if (d.isMarked()) {
                            d.setMarked(false);
                            setActiveDieImage(d.getValue(), mDiceViews[i]);
                        } else {
                            d.setMarked(true);
                            setInActiveDieImage(d.getValue(), mDiceViews[i]);
                        }
                    }
                }
            } else {
                if(mActiveRound == null) {
                    int allRoundsPlayed = 10;
                    if(mRounds.size() != allRoundsPlayed)
                        Toast.makeText(getActivity(), "Chose rule first!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }//DieListener

    //Parameters are die value and imageview corresponding that die
    private void setActiveDieImage(int value, ImageView imageDie) {
        switch (value) {
            case 1:
                imageDie.setImageResource(R.drawable.white1);
                break;
            case 2:
                imageDie.setImageResource(R.drawable.white2);
                break;
            case 3:
                imageDie.setImageResource(R.drawable.white3);
                break;
            case 4:
                imageDie.setImageResource(R.drawable.white4);
                break;
            case 5:
                imageDie.setImageResource(R.drawable.white5);
                break;
            case 6:
                imageDie.setImageResource(R.drawable.white6);
                break;
        }
    }

    //Parameters are die value and imageview corresponding that die
    private void setInActiveDieImage(int value, ImageView imageDie) {
        switch (value) {
            case 1:
                imageDie.setImageResource(R.drawable.red1);
                break;
            case 2:
                imageDie.setImageResource(R.drawable.red2);
                break;
            case 3:
                imageDie.setImageResource(R.drawable.red3);
                break;
            case 4:
                imageDie.setImageResource(R.drawable.red4);
                break;
            case 5:
                imageDie.setImageResource(R.drawable.red5);
                break;
            case 6:
                imageDie.setImageResource(R.drawable.red6);
                break;
        }
    }

    //Parameters are die value and imageview corresponding that die
    private void setCountedDieImage(int value, ImageView imageDie){
        switch (value) {
            case 1:
                imageDie.setImageResource(R.drawable.grey1);
                break;
            case 2:
                imageDie.setImageResource(R.drawable.grey2);
                break;
            case 3:
                imageDie.setImageResource(R.drawable.grey3);
                break;
            case 4:
                imageDie.setImageResource(R.drawable.grey4);
                break;
            case 5:
                imageDie.setImageResource(R.drawable.grey5);
                break;
            case 6:
                imageDie.setImageResource(R.drawable.grey6);
                break;
        }
    }

    //Manages the amount of throws left TextView-counter.
    private void setThrowsLeftTextView(int throwsLeft) {
        int count = R.string.throws_left_counter;

        switch (throwsLeft) {
            case 3:
                count = R.string.throws_left_3;
                break;
            case 2:
                count = R.string.throws_left_2;
                break;
            case 1:
                count = R.string.throws_left_1;
                break;
            case 0:
                count = R.string.throws_left_0;
                break;
        }

        mThrowCounter.setText(count);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_game, container, false);

        mThrowCounter = (TextView) v.findViewById(R.id.throws_left_counter);

        // Restore the value and textView (counter) of throw counter
        if (mActiveRound != null)
            setThrowsLeftTextView(mActiveRound.getThrowsLeft());


        //Connect die with image on screen
        setUpDiceWithListenersAndImages(v);

        reminderLabel = (TextView) v.findViewById(R.id.round_aim_reminder);
        if(mActiveRound != null)
            reminderLabel.setText("Aim for: " + mActiveRound.toString());

        //Sets up all buttons in the game view (their states, with listeners etc)
        setUpThrowButton(v);
        setUpResultButton(v);
        setUpRuleButton(v);
        setUpDoneThrowingButton(v);
        setUpAddValuesButton(v);
        setUpFinishRoundButton(v);

        return v;
    }

    //Sets up state for Throw button
    private void setUpThrowButton(View v){
        mThrowButton = (Button) v.findViewById(R.id.throw_button);

        if(doneThrowingButtonIsClicked || mActiveRound == null || !mActiveRound.hasThrowsLeft())
            mThrowButton.setEnabled(false);

        mThrowButton.setOnClickListener(new ThrowButtonListener());
    }

    //Sets up state for Result button
    private void setUpResultButton(View v){
        mResultButton = (Button) v.findViewById(R.id.result_button);
        if(doneThrowingButtonIsClicked || mActiveRound != null)
            mResultButton.setEnabled(false);
        mResultButton.setOnClickListener(new ResultButtonListener());
    }

    //Sets up state for Ruel button
    private void setUpRuleButton(View v){
        mRuleButton = (Button) v.findViewById(R.id.rule_button);
        int allRoundsPlayed = 10;
        if(doneThrowingButtonIsClicked || mActiveRound != null || mRounds.size() == allRoundsPlayed)
            mRuleButton.setEnabled(false);

        mRuleButton.setOnClickListener(new RuleButtonListener());
    }

    //Sets up state for Done Throwing button
    private void setUpDoneThrowingButton(View v){
        mDoneThrowingButton = (Button) v.findViewById(R.id.calculate_result_button);
        if(mActiveRound == null || mActiveRound.getThrowsLeft() == 3 || mActiveRound.getThrowsLeft() == 0) {
            mDoneThrowingButton.setEnabled(false);
        }

        mDoneThrowingButton.setOnClickListener(new DoneThrowingListener());
    }

    //Sets up state for Add Dice Value button
    private void setUpAddValuesButton(View v){
        mAddValuesButton = (Button) v.findViewById(R.id.add_result_button);
        if(mActiveRound == null || mActiveRound.getThrowsLeft() > 0)
            mAddValuesButton.setEnabled(false);

        mAddValuesButton.setOnClickListener(new AddValuesListener());
    }

    //Sets up state for Finish Round button
    private void setUpFinishRoundButton(View v){
        mFinishRoundButton = (Button) v.findViewById(R.id.cancel_calculation_button);
        if(mActiveRound == null || mActiveRound.getThrowsLeft() > 0)
            mFinishRoundButton.setEnabled(false);

        mFinishRoundButton.setOnClickListener(new FinishRoundListener());
    }

    //When round is ended. Unmark all dice (prepare for calculation of result)
    private void roundEnded() {
        for (int i = 0; i < AMOUNT_OF_DICE; i++) {
            setActiveDieImage(mDice[i].getValue(), mDiceViews[i]);
            mDice[i].setMarked(false);
        }

        mThrowButton.setEnabled(false);
        mDoneThrowingButton.setEnabled(false);
        mAddValuesButton.setEnabled(true);
        mFinishRoundButton.setEnabled(true);
    }

    //New Game chosen in ResultFragment. Clear the list of previously played rounds and
    //make it possible to choose new rule
    public void newGame(){
        mRounds.clear();
        mRuleButton.setEnabled(true);
    }

    /* When returning back from RuleFragment where a round rule has been chosen,
    * get the chosen rule/round and set it as GameFragments active Round and then
    * add the chosen rule / round to the list of played rounds */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RULE_FRAGMENT && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                mActiveRound = data.getParcelableExtra(RuleFragment.CHOSEN_RULE);
                if (mActiveRound != null) {
                    mRounds.add(mActiveRound);
                }
            }
        }

        if(requestCode == RESULT_FRAGMENT && resultCode == Activity.RESULT_OK){
            newGame();
        }
    }

    //ButtonListeners below
    private class ThrowButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v){
            //Only react if there is an active round chosen
            if (mActiveRound != null) {

                //If no throws left, tell the user and do nothing
                if (!mActiveRound.hasThrowsLeft()) {
                    mThrowButton.setEnabled(false);
                    return;
                }

                //Roll each die and set the new ImageView corresponding the new die value
                for (int i = 0; i < AMOUNT_OF_DICE; i++) {
                    Die d = mDice[i];
                    if (!d.isMarked()) {
                        d.rollDice();
                        setActiveDieImage(d.getValue(), mDiceViews[i]);

                        Animation rotate = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
                        mDiceViews[i].startAnimation(rotate);

                    }
                }

                //Lower the rounds thorws left by one
                mActiveRound.decreaseThrowsLeft();

                //Set the counters TextView
                setThrowsLeftTextView(mActiveRound.getThrowsLeft());

                //Make it possible for user to finish throws
                mDoneThrowingButton.setEnabled(true);

                //If no throws left, prepare dice for calculation
                if (!mActiveRound.hasThrowsLeft()) {
                    roundEnded();
                }
            }
        }
    }

    private class ResultButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v){
            //Starts new activity that displays the result for each round when clicked
            if(mActiveRound == null) {
                //Sort the played rounds by Rule (String name (Low / 4 / 5 ...))
                Collections.sort(mRounds, new ResultComparator());

                Intent intent = new Intent(getActivity(), ResultActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(RESULTS_LIST, mRounds);

                intent.putExtra(RESULTS_BUNDLE_TO_RESULT_ACTIVITY, bundle);

                startActivityForResult(intent, RESULT_FRAGMENT);
            }
        }
    }

    private class RuleButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v){
            //Press the button to get to the "Choose rule-fragment"
            RuleFragment ruleFragment = new RuleFragment();

            ruleFragment.setTargetFragment(GameFragment.this, RULE_FRAGMENT);

            FragmentManager fm = getActivity().getSupportFragmentManager();

            //Sets the list of previously played rounds as an argument for the new fragment
            //This makes sure only one of each round is played. See RuleFragment for more
            Bundle b = new Bundle();
            b.putParcelableArrayList(ROUNDS_PLAYED, mRounds);
            ruleFragment.setArguments(b);

            fm.beginTransaction().replace(R.id.fragment_container, ruleFragment).addToBackStack(null).commit();
        }
    }

    private class DoneThrowingListener implements View.OnClickListener{
        @Override
        public void onClick(View v){
            //If there is an acive round and dice has been throwns once, react by setting buttons enabled/disabled
            if (mActiveRound != null && mActiveRound.getThrowsLeft() != 3) {

                //Sets the round objects counter to 0 and changes the TextView counter to 0
                mActiveRound.finishRound();
                setThrowsLeftTextView(mActiveRound.getThrowsLeft());
                roundEnded();

                doneThrowingButtonIsClicked = true;

                mAddValuesButton.setEnabled(true);
                mFinishRoundButton.setEnabled(true);
                mThrowButton.setEnabled(false);
                mRuleButton.setEnabled(false);
                mResultButton.setEnabled(false);
            }
        }
    }

    private class AddValuesListener implements View.OnClickListener{
        List<Integer> tempList = new ArrayList<>();

        @Override
        public void onClick(View v){
            tempList.clear();

            //Only react if there is an active round and the users amount of
            //throws left is 0 (this is set by pressing the calculate button)
            if (mActiveRound != null && mActiveRound.getThrowsLeft() == 0) {

                //Add the marked dice values (the ones to add for a result) to a temporary List
                for (Die d : mDice) {
                    if (d.isMarked()) {
                        tempList.add(d.getValue());
                    }
                }

                //If the user has a correct calculation, add result to Round objects total result
                if (mActiveRound.calculationIsCorrect(tempList)) {
                    int sum = 0;
                    for (int i : tempList)
                        sum += i;
                    mActiveRound.addResultSum(sum);
                    Toast.makeText(getActivity(), "Sum of " + sum + " added", Toast.LENGTH_SHORT).show();

                    //For the counted dice, unmark the die, remove the ImageView listener, mark die as counted
                    //See Done button for restoration of this
                    for (int i = 0; i < AMOUNT_OF_DICE; i++) {
                        int value;
                        ImageView dieImage;
                        if (mDice[i].isMarked()) {
                            value = mDice[i].getValue();
                            dieImage = mDiceViews[i];

                            mDiceViews[i].setOnClickListener(null);
                            mDice[i].setMarked(false);
                            setCountedDieImage(value, dieImage);
                            mDice[i].setCountend(true);
                        }
                    }

                } else {
                    Toast.makeText(getActivity(), "WRONG!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private class FinishRoundListener implements View.OnClickListener{
        @Override
        public void onClick(View v){
            //Only react if there is an active round and the users amount of
            //throws left is 0 (this is set by pressing the calculate button)
            if (mActiveRound != null && mActiveRound.getThrowsLeft() == 0) {

                //Set active round ass null and enable/disable the buttons
                mActiveRound = null;

                doneThrowingButtonIsClicked = false;

                mAddValuesButton.setEnabled(false);
                mFinishRoundButton.setEnabled(false);
                mRuleButton.setEnabled(true);
                mResultButton.setEnabled(true);

                //Restore all dice by giving back their DieListener
                DieListener dl = new DieListener();
                for (int i = 0; i < AMOUNT_OF_DICE; i++) {
                    int value = mDice[i].getValue();
                    ImageView dieImage = mDiceViews[i];

                    dieImage.setOnClickListener(dl);
                    mDice[i].setMarked(false);
                    mDice[i].setCountend(false);
                    setActiveDieImage(value, dieImage);
                }

                reminderLabel.setText("");

                int amountOfRounds = 10;
                if(mRounds.size() == amountOfRounds){
                    mRuleButton.setEnabled(false);
                    reminderLabel.setText(R.string.game_over);
                }
            }
        }
    }


}
