package com.androidproj.andproj;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private LinearLayout languageLayout, subtitleLayout, playModeLayout,
    delayLayout, koSubtitleLayout, audioLayout, cardCountLayout, resetLayout;

    private String selectedLanguage, selectedSubtitle, selectedPlayMode,
    selectedWaitSpeed, selectedKoSubtitle, selectedAudioMode, selectedCardCount;

    private Button englishButton, frenchButton, japaneseButton, germanButton;
    private Button subtitleOnButton, subtitleOffButton;
    private Button autoButton, manualButton;
    private Button delay1Button, delay2Button, delay5Button, delay10Button;
    private Button koSubtitleOnButton, koSubtitleOffButton;
    private Button audioToggleButton;
    private Button count10Button, count30Button, count50Button, count100Button, countCustomButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        //Right layout Id
        languageLayout = findViewById(R.id.languageLayout);
        subtitleLayout = findViewById(R.id.subTitleLayout);
        playModeLayout = findViewById(R.id.playModeLayout);
        delayLayout = findViewById(R.id.delayLayout);
        koSubtitleLayout = findViewById(R.id.koSubtitleLayout);
        audioLayout = findViewById(R.id.audioLayout);
        cardCountLayout = findViewById(R.id.cardCountLayout);
        resetLayout = findViewById(R.id.resetLayout);

        //menu Button Id
        Button menuLanguageButton = findViewById(R.id.menuLanguageButton);
        Button menuSubtitleButton = findViewById(R.id.menuSubTitleButton);
        Button menuPlayModeButton = findViewById(R.id.menuPlayModeButton);
        Button menuDelayButton = findViewById(R.id.menuDelayButton);
        Button menuKoSubtitleButton = findViewById(R.id.menuKoSubTitleButton);
        Button menuAudioButton = findViewById(R.id.menuAudioButton);
        Button menuCardCountButton = findViewById(R.id.menuCardCountButton);
        Button menuResetButton = findViewById(R.id.menuResetButton);

        //menu in button id
        englishButton = findViewById(R.id.englishButton);
        frenchButton = findViewById(R.id.frenchButton);
        japaneseButton = findViewById(R.id.japaneseButton);
        germanButton = findViewById(R.id.germanButton);

        subtitleOnButton = findViewById(R.id.subTitleOnButton);
        subtitleOffButton = findViewById(R.id.subTitleOffButton);

        autoButton = findViewById(R.id.autoButton);
        manualButton = findViewById(R.id.manualButton);

        delay1Button = findViewById(R.id.delayButton);
        delay2Button = findViewById(R.id.delay2Button);
        delay5Button = findViewById(R.id.delay5Button);
        delay10Button = findViewById(R.id.delay10Button);

        koSubtitleOnButton = findViewById(R.id.koSubTitleOnButton);
        koSubtitleOffButton = findViewById(R.id.koSubTitleOffButton);

        audioToggleButton = findViewById(R.id.audioToggleButton);

        count10Button = findViewById(R.id.count10Button);
        count30Button = findViewById(R.id.count30Button);
        count50Button = findViewById(R.id.count50Button);
        count100Button = findViewById(R.id.count100Button);
        countCustomButton = findViewById(R.id.countCustomButton);

        Button resetActionButton = findViewById(R.id.resetActionButton);
        Button applyButton = findViewById(R.id.applyButton);
        Button cancelButton = findViewById(R.id.cancelButton);

        SharedPreferences pref = getSharedPreferences("AppPref", Context.MODE_PRIVATE);
        selectedLanguage = pref.getString("Language", "영어");
        selectedSubtitle = pref.getString("Subtitle", "켜기");
        selectedPlayMode = pref.getString("PlayMode", "수동 (터치하여 넘김)");
        selectedWaitSpeed = pref.getString("WaitSpeed", "1.0초 뒤 바로");
        selectedKoSubtitle = pref.getString("KoSubtitle", "켜기");
        selectedAudioMode = pref.getString("AudioMode", "ON");
        selectedCardCount = pref.getString("CardCount", "10개");


        updateAllUI();

        menuLanguageButton.setOnClickListener(v -> showLayout(languageLayout));
        menuSubtitleButton.setOnClickListener(v -> showLayout(subtitleLayout));
        menuPlayModeButton.setOnClickListener(v -> showLayout(playModeLayout));
        menuDelayButton.setOnClickListener(v -> showLayout(delayLayout));
        menuKoSubtitleButton.setOnClickListener(v -> showLayout(koSubtitleLayout));
        menuAudioButton.setOnClickListener(v -> showLayout(audioLayout));
        menuCardCountButton.setOnClickListener(v -> showLayout(cardCountLayout));
        menuResetButton.setOnClickListener(v -> showLayout(resetLayout));

        englishButton.setOnClickListener(v -> {
            selectedLanguage = "영어"; updateLanguageUI();
        });
        frenchButton.setOnClickListener(v -> {
            selectedLanguage = "프랑스어"; updateLanguageUI();
        });
        japaneseButton.setOnClickListener(v -> {
            selectedLanguage = "일본어"; updateLanguageUI();
        });
        germanButton.setOnClickListener(v -> {
            selectedLanguage = "독일어"; updateLanguageUI();
        });

        subtitleOnButton.setOnClickListener(v -> {
            selectedSubtitle = "켜기"; updateSubtitleUI();
        });
        subtitleOffButton.setOnClickListener(v -> {
            selectedSubtitle = "끄기"; updateSubtitleUI();
        });

        autoButton.setOnClickListener(v -> {
            selectedPlayMode = "자동 (시간 지정 가능)"; updatePlayModeUI();
        });
        manualButton.setOnClickListener(v -> {
            selectedPlayMode = "수동 (터치하여 넘김)"; updatePlayModeUI();
        });

        delay1Button.setOnClickListener(v -> {
            selectedWaitSpeed = "0.1초 뒤 바로"; updateWaitSpeedUI();
        });
        delay2Button.setOnClickListener(v ->
        {
            selectedWaitSpeed = "0.2초 뒤 바로"; updateWaitSpeedUI();
        });
        delay5Button.setOnClickListener(v -> {
            selectedWaitSpeed = "0.5초 뒤 바로"; updateWaitSpeedUI();
        });
        delay10Button.setOnClickListener(v -> {
            selectedWaitSpeed = "1.0초 뒤 바로"; updateWaitSpeedUI();
        });

        koSubtitleOnButton.setOnClickListener(v -> {
            selectedKoSubtitle = "켜기"; updateKoSubtitleUI();
        });
        koSubtitleOffButton.setOnClickListener(v ->
        {
            selectedKoSubtitle = "끄기"; updateKoSubtitleUI();
        });

        audioToggleButton.setOnClickListener(v -> {
            selectedAudioMode = selectedAudioMode.equals("ON") ? "OFF" : "ON";
            updateAudioUI();
        });

        count10Button.setOnClickListener(v -> {
            selectedCardCount = "10개"; updateCardCountUI();
        });
        count30Button.setOnClickListener(v -> {
            selectedCardCount = "30개"; updateCardCountUI();
        });
        count50Button.setOnClickListener(v -> {
            selectedCardCount = "50개"; updateCardCountUI();
        });
        count100Button.setOnClickListener(v -> {
            selectedCardCount = "100개"; updateCardCountUI();
        });

        countCustomButton.setOnClickListener(v -> showCustomCountInputDialog());

        resetActionButton.setOnClickListener(v -> {
            selectedLanguage = "영어";
            selectedSubtitle = "켜기";
            selectedPlayMode = "수동 (터치하여 넘김)";
            selectedWaitSpeed = "1.0초 뒤 바로";
            selectedKoSubtitle = "켜기";
            selectedAudioMode = "ON";
            selectedCardCount = "10개";
            updateAllUI();
            Toast.makeText(this, "모든 설정이 초기화되었습니다.", Toast.LENGTH_SHORT).show();
        });

        applyButton.setOnClickListener(v -> {
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("Language", selectedLanguage);
            editor.putString("Subtitle", selectedSubtitle);
            editor.putString("PlayMode", selectedPlayMode);
            editor.putString("WaitSpeed", selectedWaitSpeed);
            editor.putString("KoSubtitle", selectedKoSubtitle);
            editor.putString("AudioMode", selectedAudioMode);
            editor.putString("CardCount", selectedCardCount);

            if (selectedPlayMode.contains("수동")) {
                editor.putBoolean("isAutoAdvanceChecked", false);
            }
            editor.apply();

            Toast.makeText(this, "설정이 적용되었습니다.", Toast.LENGTH_SHORT).show();
            finish();
        });

        cancelButton.setOnClickListener(v -> finish());
    }

    private void showCustomCountInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("학습할 카드 수량 입력");
        builder.setMessage("원하는 카드 개수를 숫자로 입력하세요.");

        final EditText input =  new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton("적용", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String inputText = input.getText().toString().trim();
                if (!inputText.isEmpty()) {
                    int customCount = Integer.parseInt(inputText);
                    if (customCount > 0) {
                        selectedCardCount = "사용자 설정 (" + customCount + "개)";
                        updateCardCountUI();
                    }
                    else {
                        Toast.makeText(SettingsActivity.this, "1 이상의 숫자를 입력해주세요", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(SettingsActivity.this, "숫자가 입력되지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("취소", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void showLayout(LinearLayout targetLayout) {
        languageLayout.setVisibility(View.GONE);
        subtitleLayout.setVisibility(View.GONE);
        playModeLayout.setVisibility(View.GONE);
        delayLayout.setVisibility(View.GONE);
        koSubtitleLayout.setVisibility(View.GONE);
        audioLayout.setVisibility(View.GONE);
        cardCountLayout.setVisibility(View.GONE);
        resetLayout.setVisibility(View.GONE);

        targetLayout.setVisibility(View.VISIBLE);
    }

    private void updateAllUI() {
        updateLanguageUI();
        updateSubtitleUI();
        updatePlayModeUI();
        updateWaitSpeedUI();
        updateKoSubtitleUI();
        updateAudioUI();
        updateCardCountUI();
    }

    private void updateLanguageUI() {
        int purple = Color.parseColor("#6750A4");
        int gray = Color.LTGRAY;
        englishButton.setBackgroundTintList(ColorStateList.valueOf(selectedLanguage.equals("영어") ? purple : gray));
        frenchButton.setBackgroundTintList(ColorStateList.valueOf(selectedLanguage.contains("프랑스어") ? purple : gray));
        japaneseButton.setBackgroundTintList(ColorStateList.valueOf(selectedLanguage.contains("일본어") ? purple : gray));
        germanButton.setBackgroundTintList(ColorStateList.valueOf(selectedLanguage.contains("독일어") ? purple : gray));
    }

    private void updateSubtitleUI() {
        int purple = Color.parseColor("#6750A4");
        int gray = Color.LTGRAY;
        subtitleOnButton.setBackgroundTintList(ColorStateList.valueOf(selectedSubtitle.equals("켜기") ? purple : gray));
        subtitleOffButton.setBackgroundTintList(ColorStateList.valueOf(selectedSubtitle.equals("끄기") ? purple : gray));
    }

    private void updatePlayModeUI() {
        int purple = Color.parseColor("#6750A4");
        int gray = Color.LTGRAY;
        autoButton.setBackgroundTintList(ColorStateList.valueOf(selectedPlayMode.contains("자동") ? purple : gray));
        manualButton.setBackgroundTintList(ColorStateList.valueOf(selectedPlayMode.contains("수동") ? purple : gray));
    }

    private void updateWaitSpeedUI() {
        int purple = Color.parseColor("#6750A4");
        int gray = Color.LTGRAY;
        delay1Button.setBackgroundTintList(ColorStateList.valueOf(selectedWaitSpeed.contains("0.1초") ? purple : gray));
        delay2Button.setBackgroundTintList(ColorStateList.valueOf(selectedWaitSpeed.contains("0.2초") ? purple : gray));
        delay5Button.setBackgroundTintList(ColorStateList.valueOf(selectedWaitSpeed.contains("0.5초") ? purple : gray));
        delay10Button.setBackgroundTintList(ColorStateList.valueOf(selectedWaitSpeed.contains("1.0초") ? purple : gray));
    }

    private void updateKoSubtitleUI() {
        int purple = Color.parseColor("#6750A4");
        int gray = Color.LTGRAY;
        koSubtitleOnButton.setBackgroundTintList(ColorStateList.valueOf(selectedKoSubtitle.equals("켜기") ? purple : gray));
        koSubtitleOffButton.setBackgroundTintList(ColorStateList.valueOf(selectedKoSubtitle.equals("끄기") ? purple : gray));
    }

    private void updateAudioUI() {
        if (selectedAudioMode.equals("ON")) {
            audioToggleButton.setText("오디오 켜짐 (ON)");
            audioToggleButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
        }
        else {
            audioToggleButton.setText("오디오 꺼짐(OFF)");
            audioToggleButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F44336")));
        }
    }

    private void updateCardCountUI() {
        int purple = Color.parseColor("#6750A4");
        int gray = Color.LTGRAY;
        count10Button.setBackgroundTintList(ColorStateList.valueOf(selectedCardCount.equals("10개") ? purple : gray));
        count30Button.setBackgroundTintList(ColorStateList.valueOf(selectedCardCount.equals("30개") ? purple : gray));
        count50Button.setBackgroundTintList(ColorStateList.valueOf(selectedCardCount.equals("50개") ? purple : gray));
        count100Button.setBackgroundTintList(ColorStateList.valueOf(selectedCardCount.equals("100개") ? purple : gray));

        if (selectedCardCount.startsWith("사용자 설정")) {
            countCustomButton.setBackgroundTintList(ColorStateList.valueOf(purple));
            countCustomButton.setText(selectedCardCount);
        }
        else {
            countCustomButton.setBackgroundTintList(ColorStateList.valueOf(gray));
            countCustomButton.setText("사용자 설정");
        }
    }
}


