package base;

import java.util.ArrayList;

public class Folder {
	private ArrayList<Note> notes;
	private String name;

	public Folder(String s) {
		name = new String(s);
		notes = new ArrayList<Note>();
	}

	public void addNote(Note note) {
		notes.add(note);
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

		return temp + ":" + nText + ":"
				+ nImage;
	}

	public boolean equals(Object obj) {
		try {
			Folder temp = (Folder) obj;
			return this.name.equals(temp);
		} catch (ClassCastException e) {
			return false;
		}
	}
}
