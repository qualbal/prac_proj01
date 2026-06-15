package com.androidproj.andproj;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StartFlashCards extends AppCompatActivity {
    private ImageView cardImageView;
    private TextView cardContentTextView;
    private TextView koSubtitleTextView;
    private Button prevButton, nextButton, menuMoreButton, playPauseButton;

    private final int[] images = { R.drawable.item_001_apple, R.drawable.item_002_chair, R.drawable.item_003_desk, R.drawable.item_004_library, R.drawable.item_005_water,
            R.drawable.item_006_book, R.drawable.item_007_clock, R.drawable.item_008_sun, R.drawable.item_009_tree, R.drawable.item_010_shirt,
            R.drawable.item_011_bag, R.drawable.item_012_car, R.drawable.item_013_house, R.drawable.item_014_dog, R.drawable.item_015_cat,
            R.drawable.item_016_door, R.drawable.item_017_window, R.drawable.item_018_rice, R.drawable.item_019_bread, R.drawable.item_020_milk,
            R.drawable.item_021_coffee, R.drawable.item_022_tea, R.drawable.item_023_flower, R.drawable.item_024_park, R.drawable.item_025_airplane,
            R.drawable.item_026_bicycle, R.drawable.item_027_computer, R.drawable.item_028_phone, R.drawable.item_029_camera, R.drawable.item_030_gift,
            R.drawable.item_031_map, R.drawable.item_032_medicine, R.drawable.item_033_bed, R.drawable.item_034_bookshelf, R.drawable.item_035_glasses,
            R.drawable.item_036_umbrella, R.drawable.item_037_key, R.drawable.item_038_scissors, R.drawable.item_039_knife, R.drawable.item_040_spoon,
            R.drawable.item_041_chopstick, R.drawable.item_042_plate, R.drawable.item_043_cup, R.drawable.item_044_refrigerator, R.drawable.item_045_washing_machine,
            R.drawable.item_046_clothes, R.drawable.item_047_shoes, R.drawable.item_048_socks, R.drawable.item_049_hat, R.drawable.item_050_gloves,
            R.drawable.item_051_mirror, R.drawable.item_052_soap, R.drawable.item_053_toothpaste, R.drawable.item_054_towel, R.drawable.item_055_picture,
            R.drawable.item_056_photo, R.drawable.item_057_letter, R.drawable.item_058_pencil, R.drawable.item_059_eraser, R.drawable.item_060_paper,
            R.drawable.item_061_market, R.drawable.item_062_stairs, R.drawable.item_063_elevator, R.drawable.item_064_roof, R.drawable.item_065_garden,
            R.drawable.item_066_table, R.drawable.item_067_sofa, R.drawable.item_068_light, R.drawable.item_069_radio, R.drawable.item_070_television,
            R.drawable.item_071_hospital, R.drawable.item_072_novel, R.drawable.item_073_magazine, R.drawable.item_074_newspaper, R.drawable.item_075_ship,
            R.drawable.item_076_train, R.drawable.item_077_bus, R.drawable.item_078_taxi, R.drawable.item_079_motorcycle, R.drawable.item_080_bridge,
            R.drawable.item_081_trafficlight, R.drawable.item_082_ticket, R.drawable.item_083_passport, R.drawable.item_084_luggage, R.drawable.item_085_menu,
            R.drawable.item_086_calculator, R.drawable.item_087_fruit, R.drawable.item_088_vegetable, R.drawable.item_089_meat, R.drawable.item_090_fish,
            R.drawable.item_091_sugar, R.drawable.item_092_salt, R.drawable.item_093_pepper, R.drawable.item_094_oil, R.drawable.item_095_juice,
            R.drawable.item_096_beer, R.drawable.item_097_wine, R.drawable.item_098_icecream, R.drawable.item_099_chocolate, R.drawable.item_100_candy};

    private final List<String> koreanWords = new ArrayList<>();
    private final List<String> englishWords = new ArrayList<>();
    private final List<String> japaneseWords = new ArrayList<>();
    private final List<String> germanWords = new ArrayList<>();
    private final List<String> frenchWords = new ArrayList<>();
    private int currentIndex = 0;

    private Handler autoHandler;
    private Runnable autoRunnable;
    private boolean isAutoMode = false;
    private boolean isPlaying = false;
    private boolean isRepeatMode = false;
    private boolean isAutoAdvanceChecked = false;
    private long delayMillis = 1000;

    private TextToSpeech tts;
    private boolean isTtsReady = false;
     @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flashcards);

         SharedPreferences pref = getSharedPreferences("AppPref", Context.MODE_PRIVATE);
         isRepeatMode = pref.getBoolean("isRepeatMode", false);
         isAutoAdvanceChecked = pref.getBoolean("isAutoAdvanceChecked", false);

         Button backFlashcardsButton = findViewById(R.id.backFlashcardsButton);
         backFlashcardsButton.setOnClickListener(v -> {
             stopAutoTimer();
             if (tts != null)
                 tts.stop();
             finish();
         });
         cardImageView = findViewById(R.id.cardImageView);
         cardContentTextView = findViewById(R.id.cardContentTextView);
         koSubtitleTextView = findViewById(R.id.koSubtitleTextView);
         prevButton = findViewById(R.id.prevButton);
         nextButton = findViewById(R.id.nextButton);
         menuMoreButton = findViewById(R.id.menuMoreButton);
         playPauseButton = findViewById(R.id.playPauseButton);

         loadCsvData();

         autoHandler = new Handler(Looper.getMainLooper());
         autoRunnable = new Runnable() {
             @Override
             public void run() {
                 if (currentIndex < getEffectiveMaxCount()) {
                     currentIndex++;
                     updateCard();
                 }
                 else {
                     if (isRepeatMode) {
                         currentIndex = 0;
                         updateCard();
                     }
                     else {
                         stopAutoTimer();
                         isPlaying = false;
                         playPauseButton.setText("▶");
                         Toast.makeText(StartFlashCards.this, "마지막 페이지 입니다.", Toast.LENGTH_SHORT).show();
                     }
                 }
             }
         };

         tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                isTtsReady = true;

                tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    @Override
                    public void onDone(String utteranceId) {}

                    @Override
                    public void onError(String utteranceId) {
                        if (isAutoMode && isPlaying) {
                            triggerNextCardDelay();
                        }
                    }

                    @Override
                    public void onStart(String utteranceId) {}
                });

                updateCard();
            }
            else {
                Toast.makeText(this, "TTS 초기화에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
         });

         playPauseButton.setOnClickListener(v -> {
             if (isPlaying) {
                 stopAutoTimer();
                 isPlaying = false;
                 playPauseButton.setText("▶");
                 if (tts != null)
                     tts.stop();
                 Toast.makeText(this, "일시 정지되었습니다.", Toast.LENGTH_SHORT).show();
             }
             else {
                 if (currentIndex >= getEffectiveMaxCount() - 1 && !isRepeatMode) {
                     currentIndex = 0;
                 }
                 isPlaying = true;
                 playPauseButton.setText("||");
                 Toast.makeText(this, "다시 자동 재생합니다.", Toast.LENGTH_SHORT).show();
                 updateCard();
             }
         });

         prevButton.setOnClickListener(v -> {
             if (!isAutoMode) {
                 if (currentIndex == 0) {
                     if (isRepeatMode) {
                         currentIndex = getEffectiveMaxCount() - 1;
                         updateCard();
                     }
                     else {
                         Toast.makeText(StartFlashCards.this, "처음 페이지 입니다.", Toast.LENGTH_SHORT).show();
                     }
                 }
                 else {
                     currentIndex--;
                     updateCard();
                    }
                 }
             else {
                 Toast.makeText(StartFlashCards.this, "자동 모드 실행 중에는 버튼을 누를 수 없습니다.", Toast.LENGTH_SHORT).show();

             }
         });

         nextButton.setOnClickListener(v -> {
             if (!isAutoMode) {
                 if (currentIndex >= getEffectiveMaxCount() - 1) {
                    if (isRepeatMode) {
                        currentIndex = 0;
                        updateCard();
                    }
                    else {
                        Toast.makeText(StartFlashCards.this, "마지막 페이지 입니다.", Toast.LENGTH_SHORT).show();
                    }
                 }
                 else {
                     currentIndex++;
                     updateCard();
                 }
             }
             else {
                 Toast.makeText(StartFlashCards.this, "자동 모드 실행 중에는 버튼을 누를 수 없습니다.", Toast.LENGTH_SHORT).show();
             }
         });

         menuMoreButton.setOnClickListener(v -> {
             stopAutoTimer();
             if (tts != null)
                 tts.stop();

             PopupMenu popup = new PopupMenu(StartFlashCards.this, menuMoreButton);
             popup.getMenu().add(Menu.NONE, 1, Menu.NONE, "1. 설정");
             popup.getMenu().add(Menu.NONE, 2, Menu.NONE, "2. 처음으로");

             MenuItem item3 = popup.getMenu().add(Menu.NONE, 3, Menu.NONE, "3. 반복하기");
             item3.setCheckable(true);
             item3.setChecked(isRepeatMode);

             MenuItem item4 = popup.getMenu().add(Menu.NONE, 4, Menu.NONE, "4. 자동 넘기기");
             item4.setCheckable(true);
             item4.setChecked(isAutoAdvanceChecked);

             popup.setOnMenuItemClickListener(item -> {
                 SharedPreferences.Editor editor = getSharedPreferences("AppPref", Context.MODE_PRIVATE).edit();

                 if (item.getItemId() == 1) {
                     Intent intent = new Intent(StartFlashCards.this, SettingsActivity.class);
                     startActivity(intent);
                     return true;
                 }
                 else if (item.getItemId() == 2) {
                     currentIndex = 0;
                     updateCard();
                     Toast.makeText(StartFlashCards.this, "첫 번째 카드로 되돌아갔습니다.", Toast.LENGTH_SHORT).show();

                     if (isAutoMode && isPlaying) {
                         playPauseButton.setText("||");
                     }
                     else if (isAutoMode && !isPlaying){
                         playPauseButton.setText("▶");
                     }
                     return true;
                 }
                 else if (item.getItemId() == 3) {
                     isRepeatMode = !isRepeatMode;

                     if (isRepeatMode && isAutoAdvanceChecked) {
                         isAutoAdvanceChecked = false;
                         editor.putString("PlayMode", "수동 (터치하여 넘김)");
                         isAutoMode = false;
                         isPlaying = false;
                         playPauseButton.setVisibility(View.GONE);
                         stopAutoTimer();
                     }

                     item.setChecked(isRepeatMode);
                     editor.putBoolean("isRepeatMode", isRepeatMode);
                     editor.putBoolean("isAutoAdvaceChecked", isAutoAdvanceChecked);
                     editor.apply();

                     if (isAutoMode && isPlaying)
                         updateCard();
                     return true;
                 }
                 else if(item.getItemId() == 4) {
                     isAutoAdvanceChecked = !isAutoAdvanceChecked;

                     if (isAutoAdvanceChecked) {
                         isRepeatMode = false;
                         editor.putString("PlayMode", "자동 (시간 지정 가능)");
                         editor.putString("CardCount", "100개");

                         isAutoMode = true;
                         isPlaying = true;
                         playPauseButton.setVisibility(View.VISIBLE);
                         playPauseButton.setText("||");

                         editor.putBoolean("isRepeatMode", isRepeatMode);
                         editor.putBoolean("isAutoAdvanceChecked", isAutoAdvanceChecked);
                         editor.apply();

                         updateCard();
                     }
                     else {
                         editor.putString("PlayMode", "수동 (터치하여 넘김)");

                         isAutoMode = false;
                         isPlaying = false;
                         playPauseButton.setVisibility(View.GONE);
                         stopAutoTimer();

                         editor.putBoolean("isAutoAdvanceChecked", isAutoAdvanceChecked);
                         editor.apply();
                     }
                     item.setChecked(isAutoAdvanceChecked);
                     return true;
                 }
                 return false;
             });

             popup.setOnDismissListener(menu -> {
                 if (isAutoMode && isPlaying) {
                     updateCard();
                 }
             });

             popup.show();
         });
    }

    private void loadCsvData() {
         try {
             BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("words100.csv"), "UTF-8"));
             String line;
             reader.readLine();

             while ((line = reader.readLine()) != null) {
                 String[] tokens = line.split(",");
                 if (tokens.length >= 6) {
                     koreanWords.add(tokens[1].trim());
                     englishWords.add(tokens[2].trim());
                     japaneseWords.add(tokens[3].trim());
                     germanWords.add(tokens[4].trim());
                     frenchWords.add(tokens[5].trim());
                 }
             }

             reader.close();
         } catch (Exception e) {
             e.printStackTrace();
         }
    }

    private int getEffectiveMaxCount() {
         SharedPreferences sharedPreferences = getSharedPreferences("AppPref", Context.MODE_PRIVATE);
         String cardCountStr = sharedPreferences.getString("CardCount", "100개");
         int maxCount = englishWords.size();

         if (cardCountStr != null) {
             try{
                 String numStr = cardCountStr.replaceAll("[^0-9]", "");
                 if (!numStr.isEmpty()) {
                     maxCount = Integer.parseInt(numStr);
                 }
             } catch (Exception e) {
                 e.printStackTrace();
             }
         }

         if (maxCount > englishWords.size() || maxCount <= 0) {
             maxCount = englishWords.size();
         }
         return maxCount;


    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getSharedPreferences("AppPref", Context.MODE_PRIVATE);

        isRepeatMode = sharedPreferences.getBoolean("isRepeatMode", false);
        isAutoAdvanceChecked = sharedPreferences.getBoolean("isAutoAdvaneChecked", false);

        String playMode = sharedPreferences.getString("PlayMode", "수동");
        String waitSpeed = sharedPreferences.getString("WaitSpeed", "1.0초 뒤");

        if (waitSpeed != null) {
            if (waitSpeed.contains("0.1초"))
                delayMillis = 100;
            else if(waitSpeed.contains("0.2초"))
                delayMillis = 200;
            else if(waitSpeed.contains("0.5초"))
                delayMillis = 500;
            else if(waitSpeed.contains("1.0초"))
                delayMillis = 1000;
        }

        if (playMode.contains("수동")) {
            isAutoAdvanceChecked = false;
            sharedPreferences.edit().putBoolean("isAutoAdvanceChecked", false).apply();
        }

        if (currentIndex >= getEffectiveMaxCount()) {
            currentIndex = 0;
        }

        if (playMode != null && playMode.contains("자동")) {
            isAutoMode = true;
            playPauseButton.setVisibility(View.VISIBLE);
            isPlaying = true;
            playPauseButton.setText("||");
            updateCard();
        }
        else {
            isAutoMode = false;
            isPlaying = false;
            playPauseButton.setVisibility(View.GONE);
            stopAutoTimer();
            updateCard();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopAutoTimer();
        if (tts != null)
            tts.stop();
    }

    @Override
    protected void onDestroy() {
         if (tts != null) {
             tts.stop();
             tts.shutdown();
         }
        super.onDestroy();
    }

    private void stopAutoTimer() {
        if (autoHandler != null && autoRunnable != null) {
            autoHandler.removeCallbacks(autoRunnable);
        }
    }

    private void triggerNextCardDelay() {
         if (isPlaying) {
             autoHandler.removeCallbacks(autoRunnable);
             autoHandler.postDelayed(autoRunnable, delayMillis);
         }
    }

    private void updateCard() {
         if (englishWords.isEmpty()) return;

         if (currentIndex >= getEffectiveMaxCount()) {
             currentIndex = getEffectiveMaxCount() - 1;
         }

         if (cardImageView != null) {
             if (images.length > 0 && currentIndex < images.length) {
                 cardImageView.setVisibility(View.VISIBLE);
                 cardImageView.setImageResource(images[currentIndex]);
             }
             else {
                 cardImageView.setVisibility(View.GONE);
             }
         }

         SharedPreferences sharedPreferences = getSharedPreferences("AppPref", Context.MODE_PRIVATE);
         String language = sharedPreferences.getString("Language", "영어");
         String subtitle = sharedPreferences.getString("Subtitle", "켜기");
         String koSubtitle = sharedPreferences.getString("KoSubtitle", "켜기");
         String audioMode = sharedPreferences.getString("AudioMode", "ON");

         String textToShow = "";
         String textToSpeak = "";
         Locale ttsLocale = Locale.US;

         if (language.equals("영어")) {
             textToShow = englishWords.get(currentIndex);
             textToSpeak = englishWords.get(currentIndex);
             ttsLocale = Locale.US;
         }
         else if (language.contains("프랑스어")) {
             textToShow = frenchWords.get(currentIndex);
             textToSpeak = frenchWords.get(currentIndex);
             ttsLocale = Locale.FRANCE;
         }
         else if(language.contains("일본어")) {
             textToShow = japaneseWords.get(currentIndex);
             textToSpeak = japaneseWords.get(currentIndex);
             ttsLocale = Locale.JAPAN;
         }
         else if(language.contains("독일어")) {
             textToShow = germanWords.get(currentIndex);
             textToSpeak = germanWords.get(currentIndex);
             ttsLocale = Locale.GERMAN;
         }
         else if(language.contains("한국어")) {
             textToShow = koreanWords.get(currentIndex);
             textToSpeak = koreanWords.get(currentIndex);
             ttsLocale = Locale.KOREA;
         }

         if ("켜기".equals(subtitle)) {
             cardContentTextView.setText(textToShow);
         }
         else {
             cardContentTextView.setText("");
         }

         if (koSubtitleTextView != null) {
             if ("켜기".equals(koSubtitle) && !language.contains("한국어")) {
                 koSubtitleTextView.setVisibility(View.VISIBLE);
                 koSubtitleTextView.setText("(" + koreanWords.get(currentIndex) + ")");
             }
             else {
                 koSubtitleTextView.setVisibility(View.GONE);
             }
         }

         if (isTtsReady && tts != null) {
             tts.stop();

             if ("OFF".equals(audioMode) || "끄기".equals(audioMode)) {
                 if (isAutoMode && isPlaying) {
                     triggerNextCardDelay();
                 }
             }
             else if (!textToSpeak.isEmpty()) {
                 tts.setLanguage(ttsLocale);
                 Bundle params = new Bundle();
                 params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "flash_card_audio");
                 tts.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, params, "flash_card_audio");
             }
         }


    }
}
