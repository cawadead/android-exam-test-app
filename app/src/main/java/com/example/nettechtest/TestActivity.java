package com.example.nettechtest;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;



import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class TestActivity extends AppCompatActivity {
    private TextView test_lable;
    private Button test_to_menu;
    private LinearLayout test_ll;
    private TextView test_question_text;
    private RadioGroup test_rg;
    private Button test_next;
    private TextView test_score;
    private ImageView test_question_image;

    private ArrayList<Ans> ans;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    private Integer questions_number;
    private Integer current_question_number;
    private Integer current_question_type;
    private Integer score;
    private Integer next_status;
    private Integer selected_mode;

    private ArrayList<Integer> mode_2and3_queue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getApplicationContext().deleteDatabase("network_tech.db");
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity); //установка отображаемого слоя

        test_to_menu = findViewById(R.id.test_to_menu); //"соединение" элемента в java и xml
        test_lable = findViewById(R.id.test_lable); //"соединение" элемента в java и xml
        test_ll = findViewById(R.id.test_ll);
        test_rg = findViewById(R.id.test_rg);
        test_score = findViewById(R.id.test_score);
        test_question_text = findViewById(R.id.test_question_text);
        test_next = findViewById(R.id.test_next);
        test_question_image = findViewById(R.id.test_question_image);

        selected_mode = Integer.parseInt(getIntent().getExtras().get("mode").toString());

        test_to_menu.setOnClickListener(new View.OnClickListener() { //смена активити
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //подключаем бд
        mDBHelper = new DatabaseHelper(this);

        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }

        Cursor cursor = mDb.rawQuery("SELECT count(questions._id) FROM questions", null); //получаем количество вопросов
        cursor.moveToFirst();
        questions_number = Integer.parseInt(cursor.getString(0));
        cursor.close();

        current_question_number = 1;
        if (selected_mode == 2 || selected_mode == 3) {
            mode_2and3_queue = new ArrayList<Integer>();
            for (int i = 1; i <= questions_number; i++) {
                mode_2and3_queue.add(i);
            }
            Collections.shuffle(mode_2and3_queue);
        }
        if (selected_mode == 3) questions_number = 30;


        set_lalbe_string(); //вставляем надпись о вопросе на котором находимся
        setTestPageData(); //заполнение страницы

        next_status=0;
        score = 0;
        set_score_string();

        test_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (next_status == 0){
                    if(current_question_type == 1){
                        int selected_correct_answers=0;
                        int overall_correct_answers=0;
                        for (int i=3; i<=ans.size()+2; i++){
                            View view = test_ll.getChildAt(i);
                            if(view instanceof CheckBox) {
                                if(ans.get(i-3).getCorrect()==1){
                                    ((CheckBox) view).setTextColor(Color.rgb(26, 153, 30));
                                    overall_correct_answers++;
                                    if(((CheckBox) view).isChecked()==true) selected_correct_answers++;
                                }
                                else ((CheckBox) view).setTextColor(Color.rgb(217, 61, 61));;

                            }
                        }
                        if (selected_correct_answers == overall_correct_answers) {
                            test_score.setText("Верно!");
                            score++;
                        }
                        else if (selected_correct_answers != overall_correct_answers && selected_correct_answers != 0) test_score.setText("Было близко!");
                        else test_score.setText("Неверно!");
                    }
                    else {
                        for (int i=0; i<ans.size(); i++){
                            View view = test_rg.getChildAt(i);
                            if(view instanceof RadioButton) {
                                if(ans.get(i).getCorrect()==1){
                                    ((RadioButton) view).setTextColor(Color.rgb(26, 153, 30));
                                    if (((RadioButton) view).isChecked() == true){
                                        test_score.setText("Верно!");
                                        score++;
                                    } else test_score.setText("Неверно!");
                                }
                                else {
                                    ((RadioButton) view).setTextColor(Color.rgb(217, 61, 61));
                                };
                            }
                        }
                    }
                    test_next.setText("Далее");
                    next_status = 1;
                }
                else {
                    if(current_question_number < questions_number){
                        current_question_number++;
                        if(current_question_type == 1){
                            int childs_count = test_ll.getChildCount();
                            for (int i=childs_count; i>0; i--){
                                View view = test_ll.getChildAt(i);
                                if(view instanceof CheckBox) {
                                    test_ll.removeView(view);
                                }

                            }
                        }
                        else {
                            test_rg.removeAllViews();
                        }
                        ans.clear();
                        set_score_string();
                        test_next.setText("Ответить");
                        set_lalbe_string();
                        setTestPageData();
                        next_status = 0;
                    }
                    else {
                        if(current_question_type == 1){
                            int childs_count = test_ll.getChildCount();
                            for (int i=childs_count; i>0; i--){
                                View view = test_ll.getChildAt(i);
                                if(view instanceof CheckBox) {
                                    test_ll.removeView(view);
                                }
                            }
                        }
                        else {
                            test_rg.removeAllViews();
                        }
                        ans.clear();
                        test_next.setVisibility(View.INVISIBLE);
                        test_lable.setVisibility(View.INVISIBLE);
                        test_score.setVisibility(View.INVISIBLE);
                        test_question_image.setImageResource(0);
                        test_question_text.setText("Тест завершен с результатом:\n"+score+" правильных ответов из "+questions_number);
                    }
                }
            }
        });
    }

    protected void set_lalbe_string(){ //функция для составляения строки (честно говоря не знаю зачем выделил отдельно)
        test_lable.setText("Вопрос " + current_question_number + " из " + questions_number);
    }

    protected void set_score_string(){
        test_score.setText(score+" из "+questions_number);
    }

    protected void setTestPageData(){
        ans = new ArrayList<Ans>();
        Cursor cursor;
        if (selected_mode == 1) {
            cursor = mDb.rawQuery("SELECT * FROM questions WHERE questions._id = " + current_question_number, null);
        }
        else {
            cursor = mDb.rawQuery("SELECT * FROM questions WHERE questions._id = " + mode_2and3_queue.get(current_question_number-1), null);
        }
        cursor.moveToFirst();
        test_question_text.setText(cursor.getString(1));
        current_question_type = Integer.parseInt(cursor.getString(2));
        if (selected_mode == 1) {
            test_question_image.setImageResource(this.getResources().getIdentifier("p"+current_question_number, "drawable", this.getPackageName()));
        }
        else {
            test_question_image.setImageResource(this.getResources().getIdentifier("p"+mode_2and3_queue.get(current_question_number-1), "drawable", this.getPackageName()));
        }

        cursor.close();

        if (selected_mode == 1) {
            cursor = mDb.rawQuery("SELECT * FROM answers WHERE answers.a_q_id = " + current_question_number, null);
        }
        else {
            cursor = mDb.rawQuery("SELECT * FROM answers WHERE answers.a_q_id = " + mode_2and3_queue.get(current_question_number-1), null);
        }
        cursor.moveToFirst();
        if(current_question_type == 1){
            while (!cursor.isAfterLast()) {
                String a = cursor.getString(2);
                Integer c = Integer.parseInt(cursor.getString(3));
                ans.add(new Ans(a, c));
                cursor.moveToNext();
            }
            cursor.close();
            if (selected_mode != 1) Collections.shuffle(ans);
            for (int i = 0; i < ans.size(); i++){
                CheckBox checkBox = new CheckBox(getApplicationContext());
                checkBox.setText(ans.get(i).getAns_text());
                checkBox.setTextSize(20);
                test_ll.addView(checkBox);
            }
        }
        else {
            while (!cursor.isAfterLast()) {
                String a = cursor.getString(2);
                Integer c = Integer.parseInt(cursor.getString(3));
                ans.add(new Ans(a, c));
                cursor.moveToNext();
            }
            cursor.close();
            if (selected_mode != 1) Collections.shuffle(ans);
            for (int i = 0; i < ans.size(); i++){
                RadioButton radioButton = new RadioButton(getApplicationContext());
                radioButton.setText(ans.get(i).getAns_text());
                radioButton.setTextSize(20);
                test_rg.addView(radioButton);
            }
        }
    }
}