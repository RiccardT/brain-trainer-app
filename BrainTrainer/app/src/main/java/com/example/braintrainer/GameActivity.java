package com.example.braintrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class GameActivity extends AppCompatActivity {

    private TextView timeLeftView;
    private TextView questionView;
    private TextView scoreView;
    private TextView questionResultView;
    private Button topLeftButton;
    private Button topRightButton;
    private Button bottomLeftButton;
    private Button bottomRightButton;
    private Button playAgainButton;

    private Integer currentAnswer;
    private Integer correctAnswerCount;
    private Integer totalQuestionsCount;

    public void onGameBoardButtonClick(View clickedButton) {
        Integer buttonValue = (Integer) clickedButton.getTag();
        if (buttonValue.equals(currentAnswer)) {
            correctAnswerCount++;
            questionResultView.setText("Correct!");
        }
        else {
            questionResultView.setText("Wrong!");
        }
        questionResultView.setVisibility(View.VISIBLE);
        totalQuestionsCount++;
        updateScoreView();
        createNextQuestionState();
    }

    private void updateScoreView() {
       scoreView.setText(
               String.format(
                       "%s" + "/" + "%s",
                       correctAnswerCount,
                       totalQuestionsCount
               )
       );
    }

    private void createNextQuestionState() {
        int maxOperandValue = 40;
        int minOperandValue = 0;
        Integer randomLeftOperand = getRandomIntegerWithinRange(minOperandValue, maxOperandValue);
        Integer randomRightOperand = getRandomIntegerWithinRange(minOperandValue, maxOperandValue);
        questionView.setText(
                String.format("%s + %s", randomLeftOperand, randomRightOperand)
        );
        currentAnswer = randomLeftOperand + randomRightOperand;
        ArrayList<Button> gameButtons = getGridButtonsAsList();
        Integer correctAnswerIndex = getRandomIntegerWithinRange(0, 3);
        for (Integer index = 0; index < gameButtons.size(); index++) {
            Button currentButton = gameButtons.get(index);
            if (index.equals(correctAnswerIndex)) {
                currentButton.setText(String.valueOf(currentAnswer));
                currentButton.setTag(currentAnswer);
                continue;
            }
            Integer randomValue = getRandomIntegerWithinRange(
                    maxOperandValue,
                    minOperandValue
            );
            currentButton.setText(String.valueOf(randomValue));
            currentButton.setTag(randomValue);
        }
    }

    private int getRandomIntegerWithinRange(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    private ArrayList<Button> getGridButtonsAsList() {
        return new ArrayList<>(
                Arrays.asList(
                        topLeftButton,
                        topRightButton,
                        bottomLeftButton,
                        bottomRightButton
                )
        );
    }

    public void onPlayAgainButtonClick(View playAgainButton) {
        restartGameState();
    }

    private void restartGameState() {
        correctAnswerCount = 0;
        totalQuestionsCount = 0;
        updateScoreView();
        createNextQuestionState();
        turnOnButtons();
        hideLateGameUI();
        startTimer();
    }

    private void turnOnButtons() {
        ArrayList<Button> gameButtons = getGridButtonsAsList();
        for (Button gameButton : gameButtons) {
            gameButton.setOnClickListener(this::onGameBoardButtonClick);
        }
    }

    private void startTimer() {
        Long countDownDuration = 31*1000L;
        new CountDownTimer(countDownDuration, 1000) {
            @Override
            public void onTick(long millisLeft) {
                timeLeftView.setText(
                        String.format(Locale.US, "%02d" + "s", millisLeft/1000)
                );
            }

            @Override
            public void onFinish() {
                playAgainButton.setVisibility(View.VISIBLE);
                turnOffButtons();
                questionResultView.setText("Done!");

            }
        }.start();
    }

    private void turnOffButtons() {
        ArrayList<Button> gameButtons = getGridButtonsAsList();
        for (Button gameButton : gameButtons) {
            gameButton.setOnClickListener(null);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        correctAnswerCount = 0;
        totalQuestionsCount = 0;
        connectUIComponents();
        hideLateGameUI();
        createNextQuestionState();
        turnOnButtons();
        startTimer();
    }



    private void connectUIComponents() {
        timeLeftView = findViewById(R.id.timeLeftView);
        questionView = findViewById(R.id.questionView);
        scoreView = findViewById(R.id.scoreView);
        questionResultView = findViewById(R.id.questionResultView);
        topLeftButton = findViewById(R.id.topLeftButton);
        topRightButton = findViewById(R.id.topRightButton);
        bottomLeftButton = findViewById(R.id.bottomLeftButton);
        bottomRightButton = findViewById(R.id.bottomRightButton);
        playAgainButton = findViewById(R.id.playAgainButton);
    }

    private void hideLateGameUI() {
        playAgainButton.setVisibility(View.INVISIBLE);
        questionResultView.setVisibility(View.INVISIBLE);
    }
}