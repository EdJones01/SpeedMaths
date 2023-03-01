package speed.maths;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.innovattic.rangeseekbar.RangeSeekBar;

import org.w3c.dom.Text;

public class SettingsActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    RangeSeekBar rangeBar;

    TextView questionsTextView;
    TextView numberRangeView;
    TextView highscoreView;

    ImageButton muteButton;

    AppCompatButton addButton;
    AppCompatButton subtractButton;
    AppCompatButton multiplyButton;

    SeekBar questionsBar;

    boolean[] allowedOperators = new boolean[3];

    boolean changesMade = false;

    boolean mute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPreferences = getApplication().getSharedPreferences("speed.maths", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        findViews();

        setupSeekbar();

        setupRangebar();

        if (!(sharedPreferences.getInt("numberOfQuestions", 0) == 0)) {
            load();
        } else {
            allowedOperators = new boolean[]{true, true, true};
            mute = false;
        }

        refreshButtonColors();
    }

    @Override
    public void onBackPressed() {
        if (changesMade)
            new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                    .setTitle("Do you want to discard changes?")
                    .setPositiveButton("Discard", (dialog, which) -> {
                        super.onBackPressed();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        else
            super.onBackPressed();
    }

    void findViews() {
        questionsTextView = findViewById(R.id.number_of_questions_text);
        questionsBar = findViewById(R.id.questions_bar);
        addButton = findViewById(R.id.add_button);
        subtractButton = findViewById(R.id.subtract_button);
        multiplyButton = findViewById(R.id.multiply_button);
        rangeBar = findViewById(R.id.range_bar);
        numberRangeView = findViewById(R.id.number_range_text);
        highscoreView = findViewById(R.id.highscore_text);
        muteButton = findViewById(R.id.mute_button);
    }

    void load() {
        questionsBar.setProgress(sharedPreferences.getInt("numberOfQuestions", 0));
        String allowedOperatorsString = sharedPreferences.getString("allowedOperators", "");
        if (allowedOperatorsString.contains(Game.ADD))
            allowedOperators[0] = true;
        if (allowedOperatorsString.contains(Game.SUBTRACT))
            allowedOperators[1] = true;
        if (allowedOperatorsString.contains(Game.MULTIPLY))
            allowedOperators[2] = true;
        rangeBar.setMinThumbValue(sharedPreferences.getInt("minimumNumber", 1));
        rangeBar.setMaxThumbValue(sharedPreferences.getInt("maximumNumber", 10));
        numberRangeView.setText("Numbers between " + rangeBar.getMinThumbValue() + " and " + rangeBar.getMaxThumbValue());
        mute = sharedPreferences.getBoolean("mute", false);
        refreshMuteButton();
        updateHighscoreText();
    }

    void updateHighscoreText() {
        String savecode = new Game(rangeBar.getMinThumbValue(), rangeBar.getMaxThumbValue(), questionsBar.getProgress(),
                getAllowedOperatorsString().split(",")).getSavecode();
        float bestTime = Float.parseFloat(sharedPreferences.getString(savecode, "0.0"));
        if (bestTime != 0.0f) {
            highscoreView.setText("Best time for this configuration is " + bestTime + "s");
        } else {
            highscoreView.setText("No previous attempts for this configuration");
        }
    }

    void setupRangebar() {
        rangeBar.setSeekBarChangeListener(new RangeSeekBar.SeekBarChangeListener() {
            @Override
            public void onStartedSeeking() {
                changesMade = true;
            }

            @Override
            public void onStoppedSeeking() {
            }

            @Override
            public void onValueChanged(int i, int i1) {
                numberRangeView.setText("Numbers between " + i + " and " + i1);
                updateHighscoreText();

            }
        });
    }

    void setupSeekbar() {
        questionsBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                changesMade = true;
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                questionsTextView.setText("Number of questions: " + progress);
                updateHighscoreText();

            }
        });
    }

    void refreshButtonColors() {
        setButtonColor(addButton, 0);
        setButtonColor(subtractButton, 1);
        setButtonColor(multiplyButton, 2);
    }

    void refreshMuteButton() {
        if(mute) {
            muteButton.setBackgroundColor(getColor(R.color.lightGrey));
            muteButton.setImageResource(R.drawable.mute);
        } else {
            muteButton.setBackgroundColor(getColor(R.color.black));
            muteButton.setImageResource(R.drawable.unmute);
        }
    }

    void setButtonColor(AppCompatButton button, int index) {
        if (allowedOperators[index]) {
            button.setBackgroundColor(getResources().getColor(R.color.black));
        } else {
            button.setBackgroundColor(getResources().getColor(R.color.lightGrey));
        }
    }

    void toggleOperatorButton(int index) {
        if (allowedOperators[index]) {
            if (getNumberOfSelectedOperators() > 1)
                allowedOperators[index] = !allowedOperators[index];
        } else
            allowedOperators[index] = !allowedOperators[index];

    }

    int getNumberOfSelectedOperators() {
        int num = 0;
        for (boolean b : allowedOperators) {
            if (b)
                num++;
        }
        return num;
    }

    public void addButtonAction(View view) {
        buttonActionBlueprint(0);
    }

    public void subtractButtonAction(View view) {
        buttonActionBlueprint(1);
    }

    public void multiplyButtonAction(View view) {
        buttonActionBlueprint(2);
    }

    private void buttonActionBlueprint(int index) {
        toggleOperatorButton(index);
        refreshButtonColors();
        updateHighscoreText();
        changesMade = true;
    }

    public void muteButtonAction(View view) {
        mute = !mute;
        refreshMuteButton();
        changesMade = true;
    }

    private String getAllowedOperatorsString() {
        String allowedOperatorsString = "";
        if (allowedOperators[0])
            allowedOperatorsString += Game.ADD;
        if (allowedOperators[1])
            allowedOperatorsString += Game.SUBTRACT;
        if (allowedOperators[2])
            allowedOperatorsString += Game.MULTIPLY;
        return (allowedOperatorsString);
    }

    public void saveButtonAction(View view) {
        editor.putString("allowedOperators", getAllowedOperatorsString());
        editor.putInt("numberOfQuestions", questionsBar.getProgress());
        editor.putInt("minimumNumber", rangeBar.getMinThumbValue());
        editor.putInt("maximumNumber", rangeBar.getMaxThumbValue());
        editor.putBoolean("mute", mute);
        editor.commit();
        finish();
    }
}