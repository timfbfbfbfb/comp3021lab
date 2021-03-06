package base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Folder implements Comparable<Folder>, Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Note> notes;
	private String name;

	public Folder(String s) {
		name = s;
		notes = new ArrayList<Note>();
	}

	public void addNote(Note note) {
		notes.add(note);
	}

	public boolean removeNotes(String title) {
		// TODO
		// Given the title of the note, delete it from the folder.
		// Return true if it is deleted successfully, otherwise return false.
		return notes.remove(new Note(title));
	}

	public String getName() {
		return this.name;
	}

	public ArrayList<Note> getNotes() {
		return this.notes;
	}

	public String toString() {
		String temp = this.name;
		int nText = 0, nImage = 0;

		for (Note n : this.notes) {
			if (n instanceof TextNote)
				nText++;
			if (n instanceof ImageNote)
				nImage++;
		}

		return temp + ":" + nText + ":" + nImage;
	}

	public boolean equals(Object obj) {
		try {
			Folder temp = (Folder) obj;
			return this.name.equals(temp.name);
		} catch (ClassCastException e) {
			return false;
		}
	}

	public int compareTo(Folder o) {
		// TODO Auto-generated method stub
		return this.name.compareTo(o.name);
	}

	public void sortNotes() {
		Collections.sort(this.notes);
	}

	public List<Note> searchNotes(String keywords) {
		List<Note> result = new ArrayList<Note>();
		String keywords2 = keywords.toLowerCase();
		ArrayList<String> andWords = new ArrayList<String>();
		ArrayList<String> orWords = new ArrayList<String>();
		boolean goToAndWords = true;

		String[] words = keywords2.split(" ");

		if (words.length == 0)
			return result;

		for (int i = 0; i < words.length; i++) {
			if (words[i].equals("or")) {
				if (goToAndWords) {
					andWords.remove(words[i - 1]);
					orWords.add(words[i - 1] + " " + words[i + 1]);
				} else {
					String tempStr = orWords.remove(orWords.size() - 1);
					orWords.add(tempStr + " " + words[i + 1]);

				}
				goToAndWords = false;
				i++;
			} else {
				andWords.add(words[i]);
				goToAndWords = true;
			}
		}

		for (Note note : this.notes) {
			boolean match = true;
			for (String orWord : orWords) {
				boolean tempBool = false;
				String[] arr = orWord.split(" ");
				for (String s : arr)
					tempBool = tempBool || note.getTitle().toLowerCase().contains(s);
				match = match && tempBool;
			}
			for (String andWord : andWords)
				match = match && (note.getTitle().toLowerCase().contains(andWord));
			if (note instanceof TextNote) {
				boolean match2 = true;
				TextNote tn = (TextNote) note;
				for (String orWord : orWords) {
					boolean tempBool = false;
					String[] arr = orWord.split(" ");
					for (String s : arr)
						tempBool = tempBool || tn.content.toLowerCase().contains(s);
					match2 = match2 && tempBool;
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
