package com.ScCode.RussianEnglishFlashcard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/*
 * Created by ScottShottGG on 18-Sep-14.
 */

public class AddingCustomActivity extends Activity
{
	EditText english_editText;
	EditText russian_editText;
	Bundle extras;
	String englishFileName;
	String russianFileName;
	BufferedReader russianReader;
	BufferedReader englishReader;
	PrintWriter eOut;
	PrintWriter rOut;
	String englishCustomWord;
	String russianCustomWord;
	boolean yesClicked = false;
	boolean russianKeyboardShown = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_adding);
		english_editText = (EditText)findViewById(R.id.english_editText);
		russian_editText = (EditText)findViewById(R.id.russian_editText);
		extras = getIntent().getExtras();
		englishFileName = extras.getString("englishfile");
		russianFileName	=  extras.getString("russianfile");
		//File sdcard = Environment.getExternalStorageDirectory();

		try
		{
			englishReader = new BufferedReader(new FileReader(englishFileName));
			russianReader = new BufferedReader(new FileReader(russianFileName));
		}
		catch(FileNotFoundException fne)
		{
			String devError = "Could not find requested files.\nContact developer with " +
					"error code 0x464e. Please remember this situation and contact the developer";
			Toast.makeText(getApplicationContext(), devError, Toast.LENGTH_LONG).show();
			onBackPressed();
		}

		askAboutRussian(null);
		/*russian_editText.setOnFocusChangeListener(new View.OnFocusChangeListener()
		{
			@Override
			public void onFocusChange(View view, boolean hasFocus)
			{
				if(!yesClicked)
				{
					askAboutRussian(null);
				}
			}
		});*/
		// Type out how to instructions on how to get the IME/Keyboard for Russian
		// Type out explanation on how it works

	}

	@Override
	public void onResume()
	{
		super.onResume();
		Toast.makeText(getApplicationContext(), "Press the world button on the keyboard in the bottom left and change to Russian, change back when needed", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		System.gc();
	}

	public void onClickAddWord(View view)
	{
		englishCustomWord = english_editText.getText().toString();
		russianCustomWord = russian_editText.getText().toString();
		english_editText.setText(null);
		russian_editText.setText(null);

		checkNewline();

		try
		{
			eOut = new PrintWriter(new BufferedWriter(new FileWriter(englishFileName, true)));
			rOut = new PrintWriter(new BufferedWriter(new FileWriter(russianFileName, true)));
		}
		catch(IOException e)
		{
			Toast.makeText(getApplicationContext(), "Could not create file writer", Toast.LENGTH_LONG).show();
		}

		if(!englishCustomWord.equals("") && !russianCustomWord.equals(""))
		{
			eOut.println(englishCustomWord);
			rOut.println(russianCustomWord);
		}
		else
		{
			Toast.makeText(getApplicationContext(), "You did not enter a word in one of the text boxes, try again after entering one.", Toast.LENGTH_SHORT).show();
		}

		eOut.close();
		rOut.close();

		Toast.makeText(getApplicationContext(), "Added " + englishCustomWord + " and " + russianCustomWord, Toast.LENGTH_SHORT).show();
}

	public void alertDialog(Context context, String title, String message, int icon)
	{
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setIcon(icon);
		alertDialog.show();
	}

	public void onClickHowTo(View view)
	{
		// Might not use this
		alertDialog(this, "", "", 0);
	}

	public void askAboutRussian(View view)
	{
		yesClicked = false;

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("Russian Keyboard");
		alertDialog.setMessage("Would you like to set up the Russian keyboard?");
		//alertDialog.setIcon(0);
		alertDialog.setPositiveButton("Yes",
				new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
						Toast.makeText(getApplicationContext(), "Enable the Russian keyboard at the bottom - Русский", Toast.LENGTH_LONG).show();
						startActivity(new Intent(android.provider.Settings.ACTION_INPUT_METHOD_SUBTYPE_SETTINGS));
						yesClicked = true;
					}
				});
		alertDialog.setNegativeButton("No",
				new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
					}
				});
		alertDialog.show();
	}

	public void checkNewline()
		{
			try
			{
				englishReader = new BufferedReader(new FileReader(englishFileName));
				russianReader = new BufferedReader(new FileReader(russianFileName));
			}
			catch(FileNotFoundException fne)
			{
				Toast.makeText(getApplicationContext(), "Could not check the files", Toast.LENGTH_SHORT).show();
			}

			String line;
			String lastLine = null;

		try
		{
			while((line = englishReader.readLine()) != null)
			{
				lastLine = line;
			}

			//Toast.makeText(getApplicationContext(), line, Toast.LENGTH_SHORT).show();
			System.out.println("line: " + line + "|" + "\nlastLine: " + lastLine + "|");

			if( lastLine != null && !lastLine.equals("\n"))
			{
				try
				{
					eOut = new PrintWriter(new BufferedWriter(new FileWriter(englishFileName, true)));
					eOut.println();
				}
				catch (IOException e)
				{
					Toast.makeText(getApplicationContext(), "Could not create file writer", Toast.LENGTH_LONG).show();
				}
			}

			while((line = russianReader.readLine()) != null)
			{
				lastLine = line;
			}

			//Toast.makeText(getApplicationContext(), line, Toast.LENGTH_SHORT).show();
			System.out.println("line: " + line + "|" + "\nlastLine: " + lastLine + "|");

			if( lastLine != null && !lastLine.equals("\n"))
			{
				try
				{
					rOut = new PrintWriter(new BufferedWriter(new FileWriter(russianFileName, true)));
					rOut.println();
				}
				catch (IOException e)
				{
					Toast.makeText(getApplicationContext(), "Could not create file writer", Toast.LENGTH_LONG).show();
				}
			}
		}
		catch(IOException e)
		{
			Toast.makeText(getApplicationContext(), "BLANK", Toast.LENGTH_SHORT).show();
		}
	}
}
