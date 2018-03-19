package com.example.weronika.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private Button mPrewiousButton;
    private Button mNextButton;
    private TextView mQuestionTextView;

    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true)
    };
    private int mCurrentIndex = 0;
    private double mAverageResult;
    private boolean mIsCheater;

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_AVERAGE = "average";
    private static final String KEY_CHEATER = "cheater";
    private static final int REQUEST_CODE_CHEAT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Wywołanie metody: onCreate(Bundle)");
        setContentView(R.layout.activity_quiz);

        if(savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mAverageResult = savedInstanceState.getDouble(KEY_AVERAGE, 0);
            mIsCheater = savedInstanceState.getBoolean(KEY_CHEATER, false);
        }

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                checkAnswer(true);
                mTrueButton.setEnabled(false);
                mFalseButton.setEnabled(false);
            }
        });
        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                checkAnswer(false);
                mTrueButton.setEnabled(false);
                mFalseButton.setEnabled(false);
            }
        });

        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(QuizActivity.this, CheatActivity.class);
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
                //startActivity(intent);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });

        mPrewiousButton = (Button) findViewById(R.id.prewious_button);
        mPrewiousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + mQuestionBank.length - 1) % mQuestionBank.length;
                mIsCheater = false;
                updateQuestion();
            }
        });

        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                mIsCheater = false;
                updateQuestion();
            }
        });
        updateQuestion();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "Wywołanie metody: onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "Wywołanie metody: onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "Wywołanie metody: onPause()");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "Wywołanie metody: onSaveInstanceState(Bundle)");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putDouble(KEY_AVERAGE, mAverageResult);
        savedInstanceState.putBoolean(KEY_CHEATER, mIsCheater);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "Wywołanie metody: onStop()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Wywołanie metody: onDestroy()");
    }

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
        mTrueButton.setEnabled(true);
        mFalseButton.setEnabled(true);
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;
        if (mIsCheater) {
            messageResId = R.string.judgment_toast;
        } else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
                mAverageResult += 1.0;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
        if(mCurrentIndex == mQuestionBank.length - 1) {
            Toast.makeText(this, "Twój wynik to " + mAverageResult / mQuestionBank.length * 100
                    + "% dobrych odpowiedzi!", Toast.LENGTH_SHORT).show();
            mAverageResult = 0;
        }

    }
}
