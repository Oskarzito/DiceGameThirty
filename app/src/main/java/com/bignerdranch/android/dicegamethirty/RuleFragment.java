package com.bignerdranch.android.dicegamethirty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Oskar Emilsson
 */

public class RuleFragment extends Fragment {

    private Round mRound;
    private Button[] mButtons;
    private ArrayList<Round> mRoundArrayList;
    public static final String CHOSEN_RULE = "chosenRule";
    private final String PRESSED_BUTTON_RULE = "pressedBtnRule";

    private Button mPlayButton;

    //Make the round null when creating a RuleFragment
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null) {
            mRound = savedInstanceState.getParcelable(PRESSED_BUTTON_RULE);
        }
    }

    //onSaveInstanceState
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putParcelable(PRESSED_BUTTON_RULE, mRound);
    }

    //Sets up buttons and gives them a listener
    private void setUpRuleButtonsWithListeners(View v, Bundle savedInstanceState){

        //Finds all view-buttons. Puts them in an array
        mButtons = new Button[]{
                (Button) v.findViewById(R.id.low_button),
                (Button) v.findViewById(R.id.button_4),
                (Button) v.findViewById(R.id.button_5),
                (Button) v.findViewById(R.id.button_6),
                (Button) v.findViewById(R.id.button_7),
                (Button) v.findViewById(R.id.button_8),
                (Button) v.findViewById(R.id.button_9),
                (Button) v.findViewById(R.id.button_10),
                (Button) v.findViewById(R.id.button_11),
                (Button) v.findViewById(R.id.button_12)
        };

        //Gives a RuleButtonListener to all view-buttons
        RuleButtonListener buttonListener = new RuleButtonListener();
        for (Button b : mButtons) {
            b.setOnClickListener(buttonListener);
        }

    }//setUpRuleButtonsWithListeners

    //Listener to all the view-rule buttons
    private class RuleButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {

            //Get button text
            String btnText = ((Button) v).getText().toString();

            //Check if the button clicked is has "Low" text or a number text
            if (btnText.equals(getString(R.string.button_low))){
                mRound = new Round(true);
            }else{
                try {   //A try catch for safety.
                        //Not really necessary since buttons text are either low or a number
                    int btnValue = Integer.parseInt(btnText);
                    mRound = new Round(btnValue);
                }catch (NumberFormatException e){
                    Toast.makeText(getActivity(), "Wrong button text", Toast.LENGTH_SHORT).show();
                }
            }

            //If there is another button pressed when you press new one.
            //Make the first pressed button enabled again. This way only one button can be selected
            for(Button b : mButtons) {
                if(!b.isEnabled())
                    b.setEnabled(true);
            }

            //Disable the button pressed
            v.setEnabled(false);

        }
    }//RuleButtonListener

    //Listener to the view-play button
    private class PlayButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v){
            //Checks if a rule for the next round has been chosen
            boolean ruleIsSelected = false;
            for(Button b : mButtons)
                if(!b.isEnabled())
                    ruleIsSelected = true;

            //When play is pressed and rule is chosen.
            //Send the selected rule to the game fragment
            if(ruleIsSelected && mRound != null) {
                Intent intent = new Intent();
                intent.putExtra(CHOSEN_RULE, mRound);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                getFragmentManager().popBackStack();
            }

        }
    }//PlayButtonListener

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_rule, container, false);

        //Give rule buttons a listener
        setUpRuleButtonsWithListeners(v, savedInstanceState);

        if(mRound != null) {
            for (Button b : mButtons) {
                if (b.getText().equals(mRound.toString()))
                    b.setEnabled(false);
            }
        }

        /* RuleFragment was started with an argument containing a list of all previously played rounds.
        * For each played round, get the toString() and compare with all buttons.
        * If a buttons text matches a previously played rounds toString(), make that button invisible.
        * This makes sure only one of each round is played */
        mRoundArrayList = this.getArguments().getParcelableArrayList(GameFragment.ROUNDS_PLAYED);
        if(mRoundArrayList != null) {

            for (int i = 0; i < mButtons.length; i++) {
                String buttonText = mButtons[i].getText().toString();
                for (Round r : mRoundArrayList) {
                    if (r.toString().equals(buttonText))
                        mButtons[i].setVisibility(View.GONE);
                }
            }
        }

        //Find play buttons id and give it a listener
        mPlayButton = (Button) v.findViewById(R.id.confirm_button);
        mPlayButton.setOnClickListener(new PlayButtonListener());

        return v;
    }

}
