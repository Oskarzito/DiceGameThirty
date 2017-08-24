package com.bignerdranch.android.dicegamethirty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by Oskar Emilsson
 */

public class ResultFragment extends Fragment {

    private TextView mResultList, mRoundList, mTotalScore;
    private ArrayList<Round> mRounds = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstaceState){
        super.onCreate(savedInstaceState);

        //Get the list of all previously played rounds
        Intent intent = getActivity().getIntent();
        Bundle b = intent.getBundleExtra(GameFragment.RESULTS_BUNDLE_TO_RESULT_ACTIVITY);
        mRounds = b.getParcelableArrayList(GameFragment.RESULTS_LIST);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_result, container, false);

        //Get reference to the views displaying round, result and total score
        mResultList = (TextView) v.findViewById(R.id.result_list);
        mRoundList = (TextView) v.findViewById(R.id.round_list);
        mTotalScore = (TextView) v.findViewById(R.id.total_score);


        String resultList = "";
        String roundList = "";
        int totalScore = 0;

        //For each played round, get the Rule (e.g. Low / 4 / 5 ...) and get the result.
        //Then add these to the respective textViews
        for(Round r : mRounds){

            String round = r.toString();

            String result = "" + r.getResult();

            roundList = roundList + round + "\n";
            resultList = resultList + result + "\n";

            totalScore += r.getResult();

        }

        mRoundList.setText(roundList);
        mResultList.setText(resultList);
        mTotalScore.setText("Total score: " + totalScore);

        int allRoundsPlayed = 10;
        Button newGameButton = (Button) v.findViewById(R.id.new_game_button);
        if(!(mRounds.size() == allRoundsPlayed))
            newGameButton.setVisibility(View.INVISIBLE);
        newGameButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            }
        });

        return v;
    }
}
