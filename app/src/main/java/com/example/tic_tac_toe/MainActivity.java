package com.example.tic_tac_toe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    SharedPreferences settings, statistic;
    SharedPreferences.Editor editor;
    ImageButton switchButton;

    Button[][] buttons;

    TextView stats;

    ImageButton reset;

    int xCount, oCount, drawCount, btnCount = 0;

    boolean isX = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settings = getSharedPreferences("SETTINGS", MODE_PRIVATE);

        if (settings.contains("MODE_NIGHT_ON")){
            getCurrentTheme();
        }else {
            editor = settings.edit();
            editor.putBoolean("MODE_NIGHT_ON", false);
            editor.apply();
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
        }

        statistic = getSharedPreferences("STATISTIC", MODE_PRIVATE);

        if (statistic.contains("x") && statistic.contains("o") && statistic.contains("draw")){
            xCount = statistic.getInt("x", 0);
            oCount = statistic.getInt("o", 0);
            drawCount = statistic.getInt("draw", 0);
        } else {
            UpdateStats();
        }

        setContentView(R.layout.activity_main);

        switchButton = findViewById(R.id.switch_button);

        if (settings.getBoolean("MODE_NIGHT_ON", true)){
            switchButton.setImageResource(R.drawable.baseline_wb_sunny_24);
        }else{
            switchButton.setImageResource(R.drawable.baseline_nightlight_24);
        }
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor = settings.edit();
                if (settings.getBoolean("MODE_NIGHT_ON", true)){
                    editor.putBoolean("MODE_NIGHT_ON", false);
                }else{
                    editor.putBoolean("MODE_NIGHT_ON", true);
                }
                editor.apply();

                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });


        buttons = new Button[3][3];
        for (int i = 0; i < 3; i++){
            for (int j = 0; j <3; j++){
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                UpdateStats();
                buttons[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(isX == true && ((Button) view).getText().equals(""))
                        {
                            ((Button) view).setText("X");
                            ((Button) view).setEnabled(false);
                            isX = false;
                        } else if (isX == false && ((Button) view).getText().equals("")){
                            ((Button) view).setText("O");
                            ((Button) view).setEnabled(false);
                            isX = true;
                        }
                        btnCount++;

                        if(btnCount <= 9 && checkForWin() == "X"){
                            Toast.makeText(MainActivity.this, "Победили крестики!", Toast.LENGTH_SHORT).show();
                            btnCount = 0;
                            xCount++;
                        } else if (btnCount <= 9 && checkForWin() == "O"){
                            Toast.makeText(MainActivity.this, "Победили нолики!", Toast.LENGTH_SHORT).show();
                            btnCount = 0;
                            oCount++;
                        } else if (btnCount == 9 && checkForWin() == "draw"){
                            Toast.makeText(MainActivity.this, "Ничья!", Toast.LENGTH_SHORT).show();
                            btnCount = 0;
                            drawCount++;
                        }
                        if (checkForWin() != "draw")
                        {
                            for (int x = 0; x < 3; x++){
                                for(int y = 0; y < 3; y++){
                                    if (buttons[x][y].isEnabled() == true){
                                        buttons[x][y].setEnabled(false);
                                    }
                                }
                            }
                        }
                        UpdateStats();
                    }


                });
            }
        }

        reset = findViewById(R.id.button_reset);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Reset();
            }
        });
    }

    private String checkForWin(){
        String[][] fields = new String[3][3];
        for (int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                fields[i][j] = buttons[i][j].getText().toString();
            }
        }

        for (int i = 0; i < 3; i++){
            if (fields[i][0].equals(fields[i][1]) && fields[i][1].equals(fields[i][2]) && fields[i][0].equals("X")) {
                return "X";
            } else if (fields[i][0].equals(fields[i][1]) && fields[i][1].equals(fields[i][2]) && fields[i][0].equals("O")) {
                return "O";
            }

            if (fields[0][i].equals(fields[1][i]) && fields[1][i].equals(fields[2][i]) && fields[0][i].equals("X")){
                return "X";
            } else if (fields[0][i].equals(fields[1][i]) && fields[1][i].equals(fields[2][i]) && fields[0][i].equals("O")) {
                return "O";
            }
        }

        if (fields[0][0].equals(fields[1][1]) && fields[1][1].equals(fields[2][2]) && fields[0][0].equals("X")){
            return "X";
        } else if (fields[0][0].equals(fields[1][1]) && fields[1][1].equals(fields[2][2]) && fields[0][0].equals("O")){
            return "O";
        }

        if (fields[2][0].equals(fields[1][1]) && fields[1][1].equals(fields[0][2]) && fields[2][0].equals("X")){
            return "X";
        } else if (fields[2][0].equals(fields[1][1]) && fields[1][1].equals(fields[0][2]) && fields[2][0].equals("O")){
            return "O";
        }


        return "draw";
    }

    private void UpdateStats(){
        editor = statistic.edit();
        editor.putInt("x", xCount);
        editor.putInt("o", oCount);
        editor.putInt("draw", drawCount);
        editor.apply();
        xCount = statistic.getInt("x", 0);
        oCount = statistic.getInt("o", 0);
        drawCount = statistic.getInt("draw", 0);
        stats = findViewById(R.id.stats);
        stats.setText("X - " + xCount + " | O - " + oCount + " | DRAW - " + drawCount);
    }
    private void getCurrentTheme() {
        if (settings.getBoolean("MODE_NIGHT_ON", true)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void Reset(){
        for (int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                buttons[i][j].setText("");
                buttons[i][j].setEnabled(true);
            }
        }
    }
}