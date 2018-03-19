package com.example.weronika.geoquiz;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE = "com.example.weronika.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.example.weronika.geoquiz.answer_shown";
    private static final String KEY_CHEATER = "cheater";
    private boolean mAnswerIsTrue;
    private boolean mIsAnswernIsShown;

    private TextView mAnswerTextView;
    private Button mShowAnswerBitton;

    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }

    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        if(savedInstanceState != null) {
            mIsAnswernIsShown = savedInstanceState.getBoolean(KEY_CHEATER, false);
        }

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);

        mShowAnswerBitton = (Button) findViewById(R.id.show_answer_button);
        mShowAnswerBitton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mIsAnswernIsShown) {
                    if (mAnswerIsTrue) {
                        mAnswerTextView.setText(R.string.true_button);
                    } else {
                        mAnswerTextView.setText(R.string.false_button);
                    }
                    setAnswerShownResult(true);
                }
            }
        });

        if(mIsAnswernIsShown) {
            if (mAnswerIsTrue) {
                mAnswerTextView.setText(R.string.true_button);
            } else {
                mAnswerTextView.setText(R.string.false_button);
            }
            setAnswerShownResult(true);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(KEY_CHEATER, mIsAnswernIsShown);
        Log.d("CheatActivity", "mIsAnswer: " + mIsAnswernIsShown);
    }

    private void setAnswerShownResult(boolean isAnswerShown) {
        mIsAnswernIsShown = true;
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }
}
