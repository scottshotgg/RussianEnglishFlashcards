import sqlite3
import os

dirpath = '/mnt/c/Users/Scott/Development/RussianEnglishFlashcards/app/src/main/res/raw/'


chapterArray = range(1,6)

DB = sqlite3.connect('chapters.db')
c = DB.cursor()

try:
	c.execute("create table main(id integer primary key, english text, russian text, chapter integer)")
except sqlite3.OperationalError as oe:
	print oe
	c.execute("drop table main")
	c.execute("create table main(id integer primary key, english text, russian text, chapter integer)")

DB.commit()

for chapter in chapterArray:
	english = open(os.path.join(dirpath, "ec" + str(chapter)))
	russian = open(os.path.join(dirpath, "rc" + str(chapter)))

	for en, ru in zip(english, russian):
		#print en, ru
		c.execute("insert into main(english, russian, chapter) values(?, ?, ?)", (unicode(en, 'utf-8'), unicode(ru, 'utf-8'), chapter))
	DB.commit()

DB.commit()

c.close()
DB.close()