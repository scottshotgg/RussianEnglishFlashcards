package com.ScCode.RussianEnglishFlashcard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import static android.R.id.checkbox;


public class ChapterActivity extends Activity
{
	ArrayList<Integer> chaptersArrayList = new ArrayList<Integer>();

	// TODO Use an oncick for these things so that we don't have to do any processing for them
    CheckBox allchapters_checkbox;
    CheckBox random_checkbox;
    CheckBox custom_checkbox;
    CheckBox c1_checkbox;
    CheckBox c2_checkbox;
    CheckBox c3_checkbox;
    CheckBox c4_checkbox;
    CheckBox c5_checkbox;
    CheckBox c6_checkbox;
    CheckBox c7_checkbox;
    CheckBox c8_checkbox;
    CheckBox c9_checkbox;
    CheckBox c10_checkbox;
    CheckBox c11_checkbox;
    CheckBox c12_checkbox;
    CheckBox c13_checkbox;
    CheckBox c14_checkbox;
    CheckBox c15_checkbox;
	CheckBox c16_checkbox;
	CheckBox words500_checkbox;
	EditText englishCustom_editText;
	EditText russianCustom_editText;

	int buttonId;
	int time;
	int words;
	boolean toastShown = false;
	boolean tracking;
	int numberOfSupportFiles = 5;  // For now, this is hardcoded. Make this an auto count.

	String ecustom;
	String rcustom;
	boolean eCreated = false;
	boolean rCreated = false;
	File sdcard = Environment.getExternalStorageDirectory();

	String englishCustomFiles[];
	String russianCustomFiles[];

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);

		Bundle extras = getIntent().getExtras();

        allchapters_checkbox = (CheckBox)findViewById(R.id.allchapters_checkBox);
        random_checkbox = (CheckBox)findViewById(R.id.random_checkBox);
        custom_checkbox = (CheckBox)findViewById(R.id.custom_checkBox);
        c1_checkbox = (CheckBox)findViewById(R.id.c1_checkBox);
        c2_checkbox = (CheckBox)findViewById(R.id.c2_checkBox);
        c3_checkbox = (CheckBox)findViewById(R.id.c3_checkBox);
        c4_checkbox = (CheckBox)findViewById(R.id.c4_checkBox);
        c5_checkbox = (CheckBox)findViewById(R.id.c5_checkBox);
        c6_checkbox = (CheckBox)findViewById(R.id.c6_checkBox);
        c7_checkbox = (CheckBox)findViewById(R.id.c7_checkBox);
        c8_checkbox = (CheckBox)findViewById(R.id.c8_checkBox);
        c9_checkbox = (CheckBox)findViewById(R.id.c9_checkBox);
        c10_checkbox = (CheckBox)findViewById(R.id.c10_checkBox);
        c11_checkbox = (CheckBox)findViewById(R.id.c11_checkBox);
        c12_checkbox = (CheckBox)findViewById(R.id.c12_checkBox);
        c13_checkbox = (CheckBox)findViewById(R.id.c13_checkBox);
        c14_checkbox = (CheckBox)findViewById(R.id.c14_checkBox);
        c15_checkbox = (CheckBox)findViewById(R.id.c15_checkBox);
        c16_checkbox = (CheckBox)findViewById(R.id.c16_checkBox);
		words500_checkbox = (CheckBox)findViewById(R.id.words500_checkBox);
		englishCustom_editText = (EditText)findViewById(R.id.englishCustom_editText);
		russianCustom_editText = (EditText)findViewById(R.id.russianCustom_editText);

		buttonId = extras.getInt("buttonId");
		words = extras.getInt("words");
		time = extras.getInt("time");
    	tracking = extras.getBoolean("tracking");
	}

    @Override
    protected void onResume()
	{
		super.onResume();

		allchapters_checkbox.setChecked(false);
		random_checkbox.setChecked(false);
		custom_checkbox.setChecked(false);
		c1_checkbox.setChecked(false);
		c2_checkbox.setChecked(false);
		c3_checkbox.setChecked(false);
		c4_checkbox.setChecked(false);
		c5_checkbox.setChecked(false);
		c6_checkbox.setChecked(false);
		c7_checkbox.setChecked(false);
		c8_checkbox.setChecked(false);
		c9_checkbox.setChecked(false);
		c10_checkbox.setChecked(false);
		c11_checkbox.setChecked(false);
		c12_checkbox.setChecked(false);
		c13_checkbox.setChecked(false);
		c14_checkbox.setChecked(false);
		c15_checkbox.setChecked(false);
		c16_checkbox.setChecked(false);
		words500_checkbox.setChecked(false);

		if (!toastShown)
		{
			Toast.makeText(getApplicationContext(), "Leave the custom file names blank to use the default custom files", Toast.LENGTH_LONG).show();
			toastShown = true;
		}
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
        // Create other languages here and more custom files
    }

    public void onClickImReady(View view)
    {
		chaptersArrayList = null;
		chaptersArrayList = new ArrayList<Integer>();
		boolean proceed = true;

        Intent intent = new Intent(this, MyActivity.class);
        Random rand = new XSRandom(System.currentTimeMillis());
        int randNum;

		// This will need fixing, can produce duplicate chapter amounts.
        if(allchapters_checkbox.isChecked()) // Need to handle both the rando mand all chapters checked at the same time
        {
			/*for(int i = 1; i < 17; i++)
			{
				chaptersArrayList.add(i);
			}*/

			Toast.makeText(getApplicationContext(), "Sorry, this feature is currently not available.\nNot all chapters have been implemented yet.\nOnly chapters 1 - 5 are available, please select from one of those.", Toast.LENGTH_LONG).show();

			allchapters_checkbox.setChecked(false); // Maybe I'll have to fix the Toast spam messages
        }
        else if(random_checkbox.isChecked())
        {
            randNum = rand.nextInt(numberOfSupportFiles) + 1;

            if(randNum < 3)
            {
                randNum += 3;
            }

            int save_randNum = randNum;

            for(int i = 0; i < save_randNum; i++)
            {
                randNum = rand.nextInt(numberOfSupportFiles) + 1;
                chaptersArrayList.add(randNum);

                // Thinking about making a pop-up message that says which chapters got picked. and making a settings tab for that too.
                // I could leave the boxes checked when they come out at onResume to show them the chapters.
            }
        }

		// Make onClicks for these so that the checking isn't involved. Maybe, maybe.
        if(custom_checkbox.isChecked())
		{
			proceed = false;

			String russianCustomFile = russianCustom_editText.getText().toString();
			String englishCustomFile = englishCustom_editText.getText().toString();

			if(!russianCustomFile.equals("") && !englishCustomFile.equals(""))
			{
				// Need to add .txt processing here and stuff
				if(checkFileNames(englishCustomFile, russianCustomFile))
				{
					Toast.makeText(getApplicationContext(), "Using specified custom files\nEnglish: " + englishCustomFile + "\nRussian: " + russianCustomFile, Toast.LENGTH_LONG).show();
					proceed = true;
					intent.putExtra("russianCustomFile", russianCustomFile);
					intent.putExtra("englishCustomFile", englishCustomFile);

					// For multiple custom files, "increment" this value everytime and add those in there
					// under the name RCF1, ECF1, RCF2, ECF2... etc etc
					// We need to splice them and analyse the strings.
					chaptersArrayList.add(-1);
				}
			}
			else if(russianCustomFile.equals("") && !englishCustomFile.equals(""))
			{
				Toast.makeText(getApplicationContext(), "You did not enter a Russian custom file name", Toast.LENGTH_LONG).show();
			}
			else if(!russianCustomFile.equals("") && englishCustomFile.equals(""))
			{
				Toast.makeText(getApplicationContext(), "You did not enter an English custom file name", Toast.LENGTH_LONG).show();
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Using default files\nEnglish: ecustom.txt\nRussian: rcustom.txt", Toast.LENGTH_LONG).show();
				proceed = true;
				chaptersArrayList.add(0);
			}


        }
        if(c1_checkbox.isChecked())
        {
			chaptersArrayList.add(1);
        }
        if(c2_checkbox.isChecked())
        {
			chaptersArrayList.add(2);
        }
        if(c3_checkbox.isChecked())
        {
			chaptersArrayList.add(3);
        }
        if(c4_checkbox.isChecked())
        {
			chaptersArrayList.add(4);
        }
        if(c5_checkbox.isChecked())
        {
			chaptersArrayList.add(5);
        }
        if(c6_checkbox.isChecked())
        {
			chaptersArrayList.add(6);
        }
        if(c7_checkbox.isChecked())
        {
			chaptersArrayList.add(7);
        }
        if(c8_checkbox.isChecked())
        {
			chaptersArrayList.add(8);
        }
        if(c9_checkbox.isChecked())
        {
			chaptersArrayList.add(9);
        }
        if(c10_checkbox.isChecked())
        {
			chaptersArrayList.add(10);
        }
        if(c11_checkbox.isChecked())
        {
			chaptersArrayList.add(11);
        }
        if(c12_checkbox.isChecked())
        {
			chaptersArrayList.add(12);
        }
        if(c13_checkbox.isChecked())
        {
			chaptersArrayList.add(13);
        }
        if(c14_checkbox.isChecked())
        {
			chaptersArrayList.add(14);
        }
        if(c15_checkbox.isChecked())
        {
			chaptersArrayList.add(15);
        }
		if(c16_checkbox.isChecked())
		{
			chaptersArrayList.add(16);
		}
		if(words500_checkbox.isChecked())
		{
			chaptersArrayList.add(500);
			// Can't do this right now because the array does not go that far
			//chaptersArrayList.add(17);
		}


		if(chaptersArrayList.size() != 0 && proceed == true)
		{
			intent.putExtra("buttonId", buttonId);
			intent.putExtra("time", time);
			intent.putExtra("words", words);
			intent.putExtra("tracking", tracking);
			intent.putIntegerArrayListExtra("chaptersArrayList", chaptersArrayList);

			startActivity(intent);
		}
		else
		{
			Toast.makeText(getApplicationContext(), "You did not select any chapters!", Toast.LENGTH_SHORT).show();
		}
    }

	public int verifyFileName(String englishFileName, String russianFileName)
	{
		eCreated = false;
		rCreated = false;
		System.out.println(englishFileName + "	" + russianFileName);
		int verification = 0;

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

		if(eCreated)
		{
			verification++;
		}
		if(rCreated)
		{
			verification += 2;
		}

		// THIS NEED TO BE REWORKED

		return verification;
		/// parseInt this shit after you get it
	}

	public void alertDialog(Context context1, String title, String message, int icon)
	{
		AlertDialog alertDialog = new AlertDialog.Builder(context1).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setIcon(icon);
		alertDialog.show();
	}

	public boolean checkFileNames(String englishFiles, String russianFiles)
	{

		String englishCustomFiles[] = englishFiles.split(";");
		String russianCustomFiles[] = russianFiles.split(";");

		int verificationSum = 0;

		if(englishCustomFiles.length == russianCustomFiles.length)
		{
			int verificationTracking[] = new int[englishCustomFiles.length];

			for(int i = 0; i < englishCustomFiles.length; i++)
			{
				verificationTracking[i] = verifyFileName(englishCustomFiles[i], englishCustomFiles[i]);

				if(verificationTracking[i] == 3)
				{
					verificationSum++;
					System.out.println("Verified!");
				}
				else if(verificationTracking[i] == 2)
				{
					alertDialog(getApplicationContext(), "Error: Russian Custom File " + i, "Russian Custom File " + i + " does not exist.", Toast.LENGTH_LONG);
				}
				else if(verificationTracking[i] == 1)
				{
					alertDialog(getApplicationContext(), "Error: English Custom File " + i, "English Custom File " + i + " does not exist.", Toast.LENGTH_LONG);
				}
				else
				{
					alertDialog(getApplicationContext(), "Error: Custom Files " + i, "Russian and English Custom File " + i + " do not exist.", Toast.LENGTH_LONG);
				}
			}

			if(verificationSum == englishCustomFiles.length)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}
}
