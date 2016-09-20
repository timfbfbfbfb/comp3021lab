package base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Folder implements Comparable<Folder>
{
	private ArrayList<Note> notes;
	private String name;

	public Folder(String s)
	{
		name = new String(s);
		notes = new ArrayList<Note>();
	}

	public void addNote(Note note)
	{
		notes.add(note);
	}

	public String getName()
	{
		return this.name;
	}

	public ArrayList<Note> getNotes()
	{
		return this.notes;
	}

	public String toString()
	{
		String temp = this.name;
		int nText = 0, nImage = 0;

		for (Note n : this.notes)
		{
			if (n instanceof TextNote)
				nText++;
			if (n instanceof ImageNote)
				nImage++;
		}

		return temp + ":" + nText + ":" + nImage;
	}

	public boolean equals(Object obj)
	{
		try
		{
			Folder temp = (Folder) obj;
			return this.name.equals(temp);
		} catch (ClassCastException e)
		{
			return false;
		}
	}

	@Override
	public int compareTo(Folder o)
	{
		// TODO Auto-generated method stub
		return this.name.compareTo(o.name);
	}

	public void sortNotes()
	{
		Collections.sort(this.notes);
	}

	public List<Note> searchNotes(String keywords)
	{
		List<Note> result = new ArrayList<Note>();
		String keywords2 = keywords.toLowerCase();
		ArrayList<String> andWords = new ArrayList<String>();
		ArrayList<String> orWords = new ArrayList<String>();

		String[] words = keywords2.split(" ");

		if (words.length == 0)
			return result;

		for (int i = 0; i < words.length; i++)
		{
			if (words[i].equals("or"))
			{
				andWords.remove(words[i - 1]);
				orWords.add(words[i - 1] + " " + words[i + 1]);
				i++;
			} else
				andWords.add(words[i]);
		}

		for (Note note : this.notes)
		{
			boolean match = true;
			for (String orWord : orWords)
			{
				String[] arr = orWord.split(" ");
				match = match && (note.getTitle().toLowerCase().contains(arr[0])
						|| note.getTitle().toLowerCase().contains(arr[1]));
			}
			for (String andWord : andWords)
				match = match && (note.getTitle().toLowerCase().contains(andWord));
			if (note instanceof TextNote)
			{
				boolean match2 = true;
				TextNote tn = (TextNote) note;
				for (String orWord : orWords)
				{
					String[] arr = orWord.split(" ");
					match2 = match2
							&& (tn.content.toLowerCase().contains(arr[0]) || tn.content.toLowerCase().contains(arr[1]));
				}
				for (String andWord : andWords)
					match2 = match2 && (tn.content.toLowerCase().contains(andWord));
				match = match || match2;
			}
			if (match)
				result.add(note);
		}

		return result;
	}
}
