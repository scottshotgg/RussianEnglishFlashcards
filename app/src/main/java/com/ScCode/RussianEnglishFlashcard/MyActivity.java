package com.ScCode.RussianEnglishFlashcard;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.TreeMap;

// TODO: Modify the entire class and make English and Russian objects that extract all the information based on the buttonId in setVars

// Not going to use a full class abstraction for now
/*class Language {

	public TextView textView;
	public

}*/

public class MyActivity extends Activity
{
    Random rand = new Random(System.currentTimeMillis());
    int randNum = 0;
    int[] wordAmount = new int[17];
    int chaptersAmount = 0;
	int buttonId;
	int time;
	int words;
	boolean tracking;
	int wordsCount = 0;
	private CountDownTimer timer;
	private long startTime;
	private final long interval = 1000;

	TextView english_textView;
	TextView russian_textView;

	// Used for abstracting the textViews; direct language textViews are kept for visibility and readability of the code
	TextView primary_textView;
	TextView secondary_textView;

	boolean hasCountedDown;
	int cardClickedCount = 0;
	boolean hasEnglishPressed = false;
	boolean hasRussianPressed = false;
	String russianFileName;
	String englishFileName;
	String englishCustomFiles[];
	String russianCustomFiles[];
	int length = 0;
	DB db = null;
	int whichLanguage = 0;

	// Compare by key
	Comparator<String> ascendingSort = new Comparator<String>() {
		@Override
		public int compare(String word1, String word2) {
			return word1.compareToIgnoreCase(word2);
		}
	};

	Comparator<String> descendingSort = new Comparator<String>() {
		@Override
		public int compare(String word1, String word2) {
			return word2.compareToIgnoreCase(word1);
		}
	};

	// This might have to move
	TreeMap<String, String> wordsMap;


	ArrayList<Integer> chaptersArrayList = new ArrayList<Integer>();

    ArrayList<String> rc_list = new ArrayList<String>();
    ArrayList<String> ec_list = new ArrayList<String>();

	Object[] keys;

	public void setVars(int buttonId) {
		switch(buttonId) {
			case 1:
				wordsMap = new TreeMap<String, String>(ascendingSort);
				primary_textView = english_textView;
				secondary_textView = russian_textView;
				break;

			case 2:
				wordsMap = new TreeMap<String, String>(ascendingSort);
				primary_textView = russian_textView;
				secondary_textView = english_textView;
				whichLanguage = 1;
				break;

			case 3:
				wordsMap = new TreeMap<String, String>(descendingSort);
				primary_textView = english_textView;
				secondary_textView = russian_textView;
				break;

			case 4:
				wordsMap = new TreeMap<String, String>(descendingSort);
				primary_textView = russian_textView;
				secondary_textView = english_textView;
				whichLanguage = 1;
				break;

			// TODO: Fix these next two ones to do their stuff while in here
			case 5:
				// This don't do anything yet
				break;

			case 6:
				// This don't do anything yet
				break;
		}

		System.out.println(whichLanguage);
	}

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
		time = extras.getInt("time");
		startTime = time * 1000;
		words = extras.getInt("words");
		tracking = extras.getBoolean("tracking");
		chaptersArrayList = extras.getIntegerArrayList("chaptersArrayList");

		setContentView(R.layout.activity_my);

		english_textView = (TextView) findViewById(R.id.english_textView);
		russian_textView = (TextView) findViewById(R.id.russian_textView);

		setVars(extras.getInt("buttonId"));

		if(chaptersArrayList.get(0) == 0)
		{
			russianFileName = Environment.getExternalStorageDirectory() + "/rcustom.txt";
			englishFileName = Environment.getExternalStorageDirectory() + "/ecustom.txt";
			length = 1;
		}
		else
		{
			try
			{
				englishCustomFiles = extras.getString("englishCustomFile").split(";");
				russianCustomFiles = extras.getString("russianCustomFile").split(";");
				length = englishCustomFiles.length;
			}
			catch(NullPointerException e)
			//if (englishCustomFiles.length == 1)
			{
				System.out.println("Settign the wrong stuff probably " + e);
				russianFileName = Environment.getExternalStorageDirectory() + "/" + extras.getString("russianCustomFile");
				englishFileName = Environment.getExternalStorageDirectory() + "/" + extras.getString("englishCustomFile");
				length = 1;
			}
		}

		db = new DB(this);
		//db.deleteDatabase();
		db.createDatabase();

        makeLanguageArrays(null, chaptersArrayList);

		if(buttonId == 0 || buttonId == 6)
		{
			buttonId = rand.nextInt(4) + 1;
			sortLanguageArray();
		}
		else if(buttonId != 5)
		{
			sortLanguageArray();
		}

		if(time != 0)
		{
			timer = new CountDownTimer(startTime, interval)
			{
				@Override
				public void onTick(long millisUntilFinished){}

				@Override
				public void onFinish()
				{
					hasCountedDown = true;

					onBackPressed();
					// This is temporary, need to make something better that
					// takes into account tracking and kicks out to another screen.
					// kicks out to the end activity
				}
			}.start();
		}
    }

    @Override
    public void onBackPressed()  // Fix this shit.
    {
        super.onBackPressed();

		if(time != 0)
		{
			timer.cancel();
		}

        chaptersAmount = 0;
        rc_list = null;
        ec_list = null;
		chaptersArrayList = null;
        randNum = 0;

        System.gc();
    }

    @Override
    public void onPause()
    {
        super.onPause();

		// Make some Toast.cancellations here and on onBackPressed
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == R.id.action_settings)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

	public void onClickEnglishButton(View view)
	{
		LinearLayout englishLLayout = (LinearLayout) findViewById(R.id.englishLLayout);

		if(hasEnglishPressed == false)
		{
			englishLLayout.setEnabled(false);
			english_textView.setEnabled(false);
			englishLLayout.setClickable(false);
			english_textView.setClickable(false);

			hasEnglishPressed = true;

			english_textView.setText(" ");
		}
		else
		{
			englishLLayout.setEnabled(true);
			english_textView.setEnabled(true);
			englishLLayout.setClickable(true);
			english_textView.setClickable(true);

			hasEnglishPressed = false;

			ifCardClicked(null);
		}
	}

	public void onClickRussianButton(View view)
	{
		russian_textView = (TextView) findViewById(R.id.russian_textView);
		LinearLayout russianLLayout = (LinearLayout) findViewById(R.id.russianLLayout);

		if(hasRussianPressed == false)
		{
			russianLLayout.setEnabled(false);
			russian_textView.setEnabled(false);
			russianLLayout.setClickable(false);
			russian_textView.setClickable(false);

			hasRussianPressed = true;

			russian_textView.setText(" ");
		}
		else
		{
			russianLLayout.setEnabled(true);
			russian_textView.setEnabled(true);
			russianLLayout.setClickable(true);
			russian_textView.setClickable(true);

			hasRussianPressed = false;

			// TODO: Find another way to show the cards
			// Make this not send another card but show the current one
			ifCardClicked(null);
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

    public void ifCardClicked(View view)
    {
		// TODO: Figure out why this is making the other card flip when you block/unblock the card area
		// TODO: Still need to add the checking for the words in here and what about if they don't want to see one of the cards?

		// TODO: Make it either loop around or kick out from a user setting

		primary_textView.setText(keys[cardClickedCount].toString());
		secondary_textView.setText(wordsMap.get(keys[cardClickedCount]));

		cardClickedCount++;
	}

    public void makeLanguageArrays(View view, ArrayList<Integer> chapterArrayList)
    {
        BufferedReader russian_reader = null;
        BufferedReader english_reader = null;
		InputStream russian_InputStream;
		InputStream english_InputStream;
		String russian_word;
		String english_word;

		boolean multipleCustomFiles = false;


		// current DB tester function
		//callThisFunctionDummy();

		for (Integer chapter: chapterArrayList) {
			ArrayList[] arrays = db.getChapter(chapter);

			ec_list.addAll(arrays[0]);
			rc_list.addAll(arrays[1]);

			for (int i = 0; i < arrays[0].size(); i++) {
				wordsMap.put(arrays[0 + whichLanguage].get(i).toString(), arrays[1 - whichLanguage].get(i).toString());
			}

		}

        for (Integer chapter: chapterArrayList)
        {
			switch(chapter)
			{
				case -1:
					if(length == 1)
					{
						chapter = 0;
						try
						{
							// TODO Change this to DB format
							russian_reader = new BufferedReader(new FileReader(new File(russianFileName)));
							english_reader = new BufferedReader(new FileReader(new File(englishFileName)));
						}
						catch(IOException e)
						{
							String sorryCouldNotFind = "Sorry, but the specified custom file could not be found. " +
									"Please try using the integrated custom file maker or editing the default files. " +
									"You may also place both a Russian and English .txt file on the root of your SD card" +
									"and use that.";
							Toast.makeText(getApplicationContext(), sorryCouldNotFind, Toast.LENGTH_LONG).show();
						}

						// This needs fixing.
						// Tell the user that these files do not exist. Ask if they would like to create them using the custom
						// word adder or if they would like to "cancel" (and create it themselves), or if they would like to
						// "dont use" (continue without the custom files)
					}
					else
					{
						processMultipleCustomFiles();
						multipleCustomFiles = true;
					}

					break;

				case 0:
					/*
					russian_InputStream = getResources().openRawResource(R.raw.rcustom);
					russian_reader = new BufferedReader(new InputStreamReader(russian_InputStream));

					english_InputStream = getResources().openRawResource(R.raw.ecustom);
					english_reader = new BufferedReader(new InputStreamReader(english_InputStream));
					*/

					try
					{
						russian_reader = new BufferedReader(new FileReader(new File(russianFileName)));
						english_reader = new BufferedReader(new FileReader(new File(englishFileName)));
					}
					catch(IOException e)
					{
						/*
						String sorryCouldNotFind = "Sorry, but the specified custom file could not be found. " +
								"Please try using the integrated custom file maker or editing the default files. " +
								"You may also place both a Russian and English .txt file on the root of your SD card" +
								"and use that.";
						Toast.makeText(getApplicationContext(), sorryCouldNotFind, Toast.LENGTH_LONG).show();
						*/
						try
						{
							if(chapterArrayList.get(0) == 0)
							{
								// TODO This shit too
								File eFile = new File(englishFileName);
								File rFile = new File(russianFileName);

								eFile.createNewFile();
								rFile.createNewFile();

								russian_reader = new BufferedReader(new FileReader(new File(russianFileName)));
								english_reader = new BufferedReader(new FileReader(new File(englishFileName)));

								BufferedWriter russian_writer = new BufferedWriter(new FileWriter(rFile));
								BufferedWriter english_writer = new BufferedWriter(new FileWriter(eFile));

								russian_writer.write("Add Russian Custom Words");
								english_writer.write("Add English Custom Words");

								russian_writer.close();
								english_writer.close();
							}
						}
						catch(IOException e1)
						{
							String error = "Could not create default files. Contact " +
									"developer with information on your specific device " +
									"and the specific situation in which the error occured.";
							Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
						}

						// This needs fixing.
						/*
						Create the creation of the files if this is their
						time using custom. "Add English Custom Words using the
						custom word adder, or edit the files called "rcustom.txt"
						and "ecustom.txt" that are located on the root of your
						SD card. You can edit them with any text editor.
						*/
					}

					break;

				// Changing all of this stuff to the new DB format
				/*
				case 1:
					russian_InputStream = getResources().openRawResource(R.raw.rc1);
					russian_reader = new BufferedReader(new InputStreamReader(russian_InputStream));

					english_InputStream = getResources().openRawResource(R.raw.ec1);
					english_reader = new BufferedReader(new InputStreamReader(english_InputStream));

					break;

				case 2:
					russian_InputStream = getResources().openRawResource(R.raw.rc2);
					russian_reader = new BufferedReader(new InputStreamReader(russian_InputStream));

					english_InputStream = getResources().openRawResource(R.raw.ec2);
					english_reader = new BufferedReader(new InputStreamReader(english_InputStream));

					break;

				case 3:
					russian_InputStream = getResources().openRawResource(R.raw.rc3);
					russian_reader = new BufferedReader(new InputStreamReader(russian_InputStream));

					english_InputStream = getResources().openRawResource(R.raw.ec3);
					english_reader = new BufferedReader(new InputStreamReader(english_InputStream));

					break;

				case 4:
					russian_InputStream = getResources().openRawResource(R.raw.rc4);
					russian_reader = new BufferedReader(new InputStreamReader(russian_InputStream));

					english_InputStream = getResources().openRawResource(R.raw.ec4);
					english_reader = new BufferedReader(new InputStreamReader(english_InputStream));

					break;

				case 5:
					russian_InputStream = getResources().openRawResource(R.raw.rc5);
					russian_reader = new BufferedReader(new InputStreamReader(russian_InputStream));

					english_InputStream = getResources().openRawResource(R.raw.ec5);
					english_reader = new BufferedReader(new InputStreamReader(english_InputStream));

					break;
				/*
				/*
				case 6:
					russian_InputStream = getResources().openRawResource(R.raw.rc6);
					russian_reader = new BufferedReader(new InputStreamReader(russian_InputStream));

					english_InputStream = getResources().openRawResource(R.raw.ec6);
					english_reader = new BufferedReader(new InputStreamReader(english_InputStream));

					break;

				case 7:
					russian_InputStream = getResources().openRawResource(R.raw.rc7);
					russian_reader = new BufferedReader(new InputStreamReader(russian_InputStream));

					english_InputStream = getResources().openRawResource(R.raw.ec7);
					english_reader = new BufferedReader(new InputStreamReader(english_InputStream));

					break;

				case 8:
					russian_InputStream = getResources().openRawResource(R.raw.rc8);
					russian_reader = new BufferedReader(new InputStreamReader(russian_InputStream));

					english_InputStream = getResources().openRawResource(R.raw.ec8);
					english_reader = new BufferedReader(new InputStreamReader(english_InputStream));

					break;

				case 9:
					russian_InputStream = getResources().openRawResource(R.raw.rc9);
					russian_reader = new BufferedReader(new InputStreamReader(russian_InputStream));

					english_InputStream = getResources().openRawResource(R.raw.ec9);
					english_reader = new BufferedReader(new InputStreamReader(english_InputStream));

					break;

				case 10:
					russian_InputStream = getResources().openRawResource(R.raw.rc10);
					russian_reader = new BufferedReader(new InputStreamReader(russian_InputStream));

					english_InputStream = getResources().openRawResource(R.raw.ec10);
					english_reader = new BufferedReader(new InputStreamReader(english_InputStream));

					break;

				case 11:
					russian_InputStream = getResources().openRawResource(R.raw.rc11);
					russian_reader = new BufferedReader(new InputStreamReader(russian_InputStream));

					english_InputStream = getResources().openRawResource(R.raw.ec11);
					english_reader = new BufferedReader(new InputStreamReader(english_InputStream));

					break;

				case 12:
					russian_InputStream = getResources().openRawResource(R.raw.rc12);
					russian_reader = new BufferedReader(new InputStreamReader(russian_InputStream));

					english_InputStream = getResources().openRawResource(R.raw.ec12);
					english_reader = new BufferedReader(new InputStreamReader(english_InputStream));

					break;

				case 13:
					russian_InputStream = getResources().openRawResource(R.raw.rc13);
					russian_reader = new BufferedReader(new InputStreamReader(russian_InputStream));

					english_InputStream = getResources().openRawResource(R.raw.ec13);
					english_reader = new BufferedReader(new InputStreamReader(english_InputStream));

					break;

				case 14:
					russian_InputStream = getResources().openRawResource(R.raw.rc14);
					russian_reader = new BufferedReader(new InputStreamReader(russian_InputStream));

					english_InputStream = getResources().openRawResource(R.raw.ec14);
					english_reader = new BufferedReader(new InputStreamReader(english_InputStream));

					break;

				case 15:
					russian_InputStream = getResources().openRawResource(R.raw.rc15);
					russian_reader = new BufferedReader(new InputStreamReader(russian_InputStream));

					english_InputStream = getResources().openRawResource(R.raw.ec15);
					english_reader = new BufferedReader(new InputStreamReader(english_InputStream));

					break;

				case 16:
					russian_InputStream = getResources().openRawResource(R.raw.rc16);
					russian_reader = new BufferedReader(new InputStreamReader(russian_InputStream));

					english_InputStream = getResources().openRawResource(R.raw.ec16);
					english_reader = new BufferedReader(new InputStreamReader(english_InputStream));

					break;
				*/

				case 500:
					// add all the words using a cursor here
					multipleCustomFiles = true;

					ArrayList[] arrays = db.getVerbs(500);
					System.out.println(arrays[0]);
					System.out.println(arrays[1]);

					ec_list.addAll(arrays[0]);
					rc_list.addAll(arrays[1]);

					for (int i = 0; i < arrays[0].size(); i++) {
						wordsMap.put(arrays[0 + whichLanguage].get(i).toString(), arrays[1 - whichLanguage].get(i).toString());
					}

					break;
				default:
					//Toast.makeText(getApplicationContext(), "Somethin' dun borkd, yo!", Toast.LENGTH_LONG).show();
					//onBackPressed();
					// TODO Fix this ghetto rigged shit later
					System.out.println("this will pop up a few times for now");
			}
			System.out.println("chapter: " + chapter);
			// TODO: What does this even do? Fix this hack shit later
			multipleCustomFiles = true;
			if(!multipleCustomFiles)
			{
				try
				{
					while (((russian_word = russian_reader.readLine()) != null) && ((english_word = english_reader.readLine()) != null))
					{
						// fix this with error messages and stuff and ask the user about fixing them
						// make auto fixer :)
						if (!russian_word.equals("\n") && !english_word.equals("\n"))
						{
							rc_list.add(russian_word);
						}
						if (!english_word.equals("\n"))
						{
							ec_list.add(english_word);
						}
						wordAmount[chapter]++;
					}

					russian_reader.close();
					english_reader.close();
				}
				catch (IOException e)
				{
					// Make these text views below a Toast or something.
					english_textView.setText("" + e);
					russian_textView.setText("" + e);
					onBackPressed();
				}

				chaptersAmount += wordAmount[chapter];
			}
			multipleCustomFiles = false;
        }

		System.out.println("English: " + ec_list.size() + " Russian: " + rc_list.size());
    }

	public void sortLanguageArray()
	{
		int size = ec_list.size();
		int i = 0;
		boolean runAgain = true;
		String first;
		String second;
		int compared;

//		if(buttonId == 1 || buttonId == 3) {
//			wordsMap.putAll(englishWordsMap);
//		} else if(buttonId == 2 || buttonId == 4) {
//			wordsMap.putAll(russianWordsMap);
//		}
		keys = wordsMap.keySet().toArray();
		System.out.println(Arrays.asList(keys));
		wordsCount = keys.length - 1;

	}


	public void processMultipleCustomFiles()
	{
		BufferedReader russian_reader = null;
		BufferedReader english_reader = null;
		InputStream russian_InputStream;
		InputStream english_InputStream;
		String russian_word;
		String english_word;

		for(int i = 0; i < englishCustomFiles.length; i++)
		{
			try
			{
				russianFileName = Environment.getExternalStorageDirectory() + "/" + russianCustomFiles[i];
				englishFileName = Environment.getExternalStorageDirectory() + "/" + englishCustomFiles[i];

				russian_reader = new BufferedReader(new FileReader(new File(russianFileName)));
				english_reader = new BufferedReader(new FileReader(new File(englishFileName)));
				System.out.println("English: " + englishCustomFiles[i] + " Russian: " + russianCustomFiles[i]);
			}
			catch(IOException e)
			{
				System.out.println("Nothing to handle this for now");
			}

			try
			{
				while (((russian_word = russian_reader.readLine()) != null) && ((english_word = english_reader.readLine()) != null))
				{
					// fix this with error messages and stuff and ask the user about fixing them
					// make auto fixer :)
					if(!russian_word.equals("\n") && !english_word.equals("\n"))
					{
						rc_list.add(russian_word);
					}
					if(!english_word.equals("\n"))
					{
						ec_list.add(english_word);
					}
					wordAmount[0]++;
				}

				russian_reader.close();
				english_reader.close();
			}
			catch(IOException e)
			{
				// Make these text views below a Toast or something.
				english_textView.setText("" + e);
				russian_textView.setText("" + e);
				onBackPressed();
			}
		}
		chaptersAmount += wordAmount[0];
	}
}


