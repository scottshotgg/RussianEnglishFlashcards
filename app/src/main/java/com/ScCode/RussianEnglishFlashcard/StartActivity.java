package com.ScCode.RussianEnglishFlashcard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class StartActivity extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    public void onClickPractice(View view)
    {
        Intent intent = new Intent(this, PracticeActivity.class);
        startActivity(intent);
    }

    public void onClickAddCustomWords(View view)
    {
		Intent intent = new Intent(this, CustomActivity.class);
		startActivity(intent);
    }

    public void onClickGame(View view)
    {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
        // Create other languages here and more custom files
    }


}
