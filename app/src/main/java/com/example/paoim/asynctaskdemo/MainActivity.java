package com.example.paoim.asynctaskdemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView tvResult;
    private Button btnRollDice;
    private EditText etRollDice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        tvResult = (TextView) findViewById(R.id.tvResult);
        etRollDice = (EditText) findViewById(R.id.etRollDice);
        btnRollDice = (Button) findViewById(R.id.btnRollDice);

        tvResult.setVisibility(View.GONE);

        btnRollDice.setOnClickListener((View v) -> {
            int numberOfTimes = toInt(etRollDice.getText().toString().trim());
            new ProcessRollDiceInBackground().execute(numberOfTimes);
        });
    }

    // Create Inner Class
    public class ProcessRollDiceInBackground extends AsyncTask<Integer, Integer, String> {

        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setMax(toInt(etRollDice.getText().toString().trim()));
            dialog.show();
        }

        @Override
        protected String doInBackground(Integer... integers) {
            int ones = 0, twos = 0, threes = 0, fours = 0, fives = 0, sixes = 0, randomNumber;

            double currentProgress = 0;
            double previousProgress = 0;
            Random random = new Random();
            int numberOfTimes = integers[0];

            for (int i = 0; i < numberOfTimes; i++) {
                currentProgress = (double) i / numberOfTimes;
                double remainingProgress = currentProgress - previousProgress;
                if (remainingProgress >= 0.03) {// Load 3% of 100%
                    publishProgress(i);
                    previousProgress = currentProgress;
                }
                randomNumber = random.nextInt(6) + 1; // random from 1 to 6
                switch (randomNumber) {
                    case 1:
                        ones++;
                        break;
                    case 2:
                        twos++;
                        break;
                    case 3:
                        threes++;
                        break;
                    case 4:
                        fours++;
                        break;
                    case 5:
                        fives++;
                        break;
                    default:
                        sixes++;
                }
            }

            String result = "Results: \n1: " + ones + "\n2: " + twos + "\n3: " + threes + "\n4: " + fours + "\n5: " + fives + "\n6: " + sixes;
            return result;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            dialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            dialog.dismiss();
            tvResult.setText(s);
            tvResult.setVisibility(View.VISIBLE);

            Toast.makeText(MainActivity.this, "Process Done!", Toast.LENGTH_SHORT).show();
        }
    }

    protected int toInt(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            return 0;
        }
    }
}
