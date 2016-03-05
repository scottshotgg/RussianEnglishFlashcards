package com.ScCode.RussianEnglishFlashcard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;

import java.lang.Integer;

public class PracticeActivity extends Activity
{
    RadioButton ae_radioButton;
    RadioButton ar_radioButton;
    RadioButton rae_radioButton;
    RadioButton rar_radioButton;
    RadioButton random_radioButton;
    RadioButton idc_radioButton;
    RadioButton radioButtonCast;
    EditText time_editText;
    EditText now_editText;
    CheckBox tracking_checkBox;

	int buttonId = 0;
    int time = 0;
    int words = 0;
    boolean tracking = false;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);

		time_editText = (EditText)findViewById(R.id.time_editText);
		now_editText = (EditText)findViewById(R.id.now_editText);
		tracking_checkBox = (CheckBox)findViewById(R.id.tracking_checkBox);
		ae_radioButton = (RadioButton)findViewById(R.id.ae_radioButton);
		ar_radioButton = (RadioButton)findViewById(R.id.ar_radioButton);
		rae_radioButton = (RadioButton)findViewById(R.id.rae_radioButton);
		rar_radioButton = (RadioButton)findViewById(R.id.rar_radioButton);
		random_radioButton = (RadioButton)findViewById(R.id.random_radioButton);
		idc_radioButton = (RadioButton)findViewById(R.id.idc_radioButton);
    }

    public void onClickAnyRadio(View view)
    {
        radioButtonCast = (RadioButton)view;

        ae_radioButton.setChecked(false);
        ar_radioButton.setChecked(false);
        rae_radioButton.setChecked(false);
        rar_radioButton.setChecked(false);
        random_radioButton.setChecked(false);
        idc_radioButton.setChecked(false);

        radioButtonCast.setChecked(true);

		// Just send this variable in buttonId.
		switch(view.getId())
		{
			case R.id.ae_radioButton:
				buttonId = 1;
				break;

			case R.id.ar_radioButton:
				buttonId = 2;
				break;

			case R.id.rae_radioButton:
				buttonId = 3;
				break;

			case R.id.rar_radioButton:
				buttonId = 4;
				break;

			case R.id.random_radioButton:
				buttonId = 5;
				break;

			case R.id.idc_radioButton:
				buttonId = 6;
				break;

			default:
				System.out.println("Something dun' brokez yo");
		}
    }

    public void onClickTracking(View view)
    {
        tracking = true;
    }

    public void onClickImAllSet(View view)
    {
        Intent intent = new Intent(this, ChapterActivity.class);

		// Try stuff.
        try
		{
			time = Integer.parseInt(time_editText.getText().toString());
		}
		catch(NumberFormatException e)
		{
			time = 0;
		}
		catch(NullPointerException e)
		{
			time = 0;
		}

		try
		{
			words = Integer.parseInt(now_editText.getText().toString());
		}
		catch(NumberFormatException e)
		{
			words = 0;
		}
		catch(NullPointerException e)
		{
			words = 0;
		}

		System.out.println(time + " " + words);

		intent.putExtra("buttonId", buttonId);
		intent.putExtra("time", time);
		intent.putExtra("words", words);
		intent.putExtra("tracking", tracking);

        startActivity(intent);
    }
}
