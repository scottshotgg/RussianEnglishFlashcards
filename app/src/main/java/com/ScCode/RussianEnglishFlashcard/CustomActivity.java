package com.ScCode.RussianEnglishFlashcard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.io.IOException;
import java.io.File;
/*
import android.widget.TextView;
import android.os.CountDownTimer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import android.widget.Button;
*/

/**
 * Created by ScottShotGG on 13-Sep-14.
 */
public class CustomActivity extends Activity
{
	EditText english_editText;
	EditText russian_editText;
	File sdcard = Environment.getExternalStorageDirectory();
	String ecustom;
	String rcustom;
	boolean eCreated = false;
	boolean rCreated = false;
	//Context context;

	public void alertDialog(Context context1, String title, String message, int icon)
	{
		AlertDialog alertDialog = new AlertDialog.Builder(context1).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setIcon(icon);
		alertDialog.show();
	}

	@Override
	public void onCreate(Bundle savedInstanceBundle)
	{
		super.onCreate(savedInstanceBundle);
		Bundle extras = getIntent().getExtras();
		setContentView(R.layout.activity_custom);
		english_editText = (EditText)findViewById(R.id.english_editText);
		english_editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

		russian_editText = (EditText)findViewById(R.id.russian_editText);
		russian_editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

		// This might need to onResume
		Toast.makeText(getApplicationContext(), "Leave the custom file names blank to use the default custom files", Toast.LENGTH_LONG).show();
	}

	public void onClickHowTo(View view)
	{
		String message = "To make a custom language file, the easiest way is to put two .txt files on the root" +
				" of your SD card and select them within the app by typing the name into the name slot on this" +
				" screen. When making a custom file, you need to make both a Russian and an English file. For" +
				" example, you could make two files, r_custom.txt and e_custom.txt, and place them onto the root" +
				" of your SD card. After doing this, type the name for the Russian file in the app as r_custom.txt," +
				" and English as e_custom.txt. Since the files are already there, the app will recognize that they" +
				" are and use your premade files instead of using the Custom File Wizard within the app. If you would" +
				" like to add words to your premade text file, the Custom Words Wizard can provide that function as well. (maybe)";

		alertDialog(this, "How To Make a Custom File", message, 0);
	}

	public void onClickUseFile(View view)
	{
		rCreated = false;
		eCreated = false;

		String russianFileName = russian_editText.getText().toString();
		String englishFileName = english_editText.getText().toString();

		if(!englishFileName.equals("") && !russianFileName.equals(""))
		{
			System.out.println(englishFileName + "	" + russianFileName);
			if (englishFileName.length() > 4)
			{
				String englishTxt = englishFileName.substring(englishFileName.length() - 4);
				if(englishTxt.equals(".txt"))
				{
					ecustom = sdcard + "/" + englishFileName;
					System.out.println(ecustom);
					final File eFile = new File(ecustom);
					if(eFile.exists())
					{
						eCreated = true;
					}
					else
					{
						AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
						alertDialog.setTitle("Error: Invalid File");
						alertDialog.setMessage("Could not find " + englishFileName + "\n\nWould you like one to be created for you?");
						alertDialog.setPositiveButton("Yes",
								new DialogInterface.OnClickListener()
								{
									@Override
									public void onClick(DialogInterface dialog, int which)
									{
										try
										{
											System.out.println(ecustom + "	i had to create a new one");
											eFile.createNewFile();
											eCreated = true;

											if(rCreated)
											{
												Intent intent = new Intent(getApplicationContext(), AddingCustomActivity.class);
												intent.putExtra("englishfile", ecustom);
												intent.putExtra("russianfile", rcustom);
												startActivity(intent);
											}
										}
										catch (IOException IOe)
										{
											alertDialog(getApplicationContext(), "Error: Invalid File Permission", "Could not create the specified file", 0);
										}

										dialog.dismiss();
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
				}
				else
				{
					alertDialog(this, "Error: Name Format", "File format was not found. Please check that you have included .txt in the name", 0);
				}
			}
			else
			{
				alertDialog(this, "Error: Invalid English File Name", "The English file name that you have entered in invalid", 0);
			}

			if (russianFileName.length() > 4)
			{
				String russianTxt = russianFileName.substring(russianFileName.length() - 4);
				if(russianTxt.equals(".txt"))
				{
					rcustom = sdcard + "/" + russianFileName;
					System.out.println(rcustom);
					final File rFile = new File(rcustom);
					if(rFile.exists())
					{
						rCreated = true;
					}
					else
					{
						AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
						alertDialog.setTitle("Error: Invalid File");
						alertDialog.setMessage("Could not find " + russianFileName + "\n\nWould you like one to be created for you?");
						alertDialog.setPositiveButton("Yes",
								new DialogInterface.OnClickListener()
								{
									@Override
									public void onClick(DialogInterface dialog, int which)
									{
										try
										{
											System.out.println(rcustom + "	i had to create a new one too");
											rFile.createNewFile();
											rCreated = true;

											if(eCreated)
											{
												Intent intent = new Intent(getApplicationContext(), AddingCustomActivity.class);
												intent.putExtra("englishfile", ecustom);
												intent.putExtra("russianfile", rcustom);
												startActivity(intent);
											}

										}
										catch (IOException IOe)
										{
											alertDialog(getApplicationContext(), "Error: Invalid File Permission", "Could not create the specified file", 0);
										}
										dialog.dismiss();
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
				}
				else
				{
					alertDialog(this, "Error: Name Format", "File format was not found. Please check that you have included .txt in the name", 0);
				}
			}
			else
			{
				alertDialog(this, "Error: Invalid Russian File Name", "The Russian file name that you have entered in invalid", 0);
			}
		}
		else
		{
			try
			{
				ecustom = sdcard + "/" + "ecustom.txt";
				rcustom = sdcard + "/" + "rcustom.txt";

				File eFile = new File(ecustom);
				File rFile = new File(rcustom);

				if (!eFile.exists())
				{
					eFile.createNewFile();
				}
				if (!rFile.exists())
				{
					rFile.createNewFile();
				}

				eCreated = true;
				rCreated = true;
			}
			catch(IOException e)
			{
				Toast.makeText(getApplicationContext(), "Files could not be created, sorry.", Toast.LENGTH_SHORT).show();
			}
		}

		if(rCreated && eCreated)
		{
			Intent intent = new Intent(getApplicationContext(), AddingCustomActivity.class);
			intent.putExtra("englishfile", ecustom);
			intent.putExtra("russianfile", rcustom);
			startActivity(intent);
		}
	}
}
