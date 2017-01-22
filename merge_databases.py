import sqlite3
import os

chaptersDB = sqlite3.connect('app/src/main/assets/chapters.db')
verbsDB = sqlite3.connect('app/src/main/assets/words_500.db')

wordsDB = sqlite3.connect('app/src/main/assets/words.db')

chap_cursor = chaptersDB.cursor()
verbs_cursor = verbsDB.cursor()

words_cursor = wordsDB.cursor()

words_cursor.execute("create table main (id integer primary key, english text, russian text, chapter integer)")
words_cursor.execute("create table verbs (id integer primary key, english text, russian text)")
# Try without this thing first
#words_cursor.execute("create table android_manifest (id integer primary key, english text, russian text, chapter integer)")


chap_grab = chap_cursor.execute('select english, russian, chapter from main')
verbs_grab = verbs_cursor.execute('select english, russian from main')

chap_data = chap_grab.fetchall()
verb_data = verbs_grab.fetchall()

datas = [chap_data, verb_data]

for data in chap_data:
		words_cursor.execute("insert into main (english, russian, chapter) values (?, ?, ?)", (data[0], data[1], data[2]))

wordsDB.commit()

for data in verb_data:
		words_cursor.execute("insert into verbs (english, russian) values (?, ?)", (data[0], data[1]))

wordsDB.commit()

words_cursor.close()
wordsDB.close()