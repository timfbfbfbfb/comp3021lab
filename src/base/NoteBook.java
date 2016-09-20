package base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NoteBook
{
	private ArrayList<Folder> folders;

	public NoteBook()
	{
		this.folders = new ArrayList<Folder>();
	}

	public boolean createTextNote(String s1, String s2)
	{
		TextNote note = new TextNote(s2);
		return insertNote(s1, note);
	}

	public boolean createImageNote(String s1, String s2)
	{
		ImageNote note = new ImageNote(s2);
		return insertNote(s1, note);
	}

	private boolean insertNote(String s, Note n)
	{

		boolean exist = false;
		Folder tempFolder = null;
		for (Folder f : folders)
		{
			if (f.getName().equals(s))
			{
				exist = true;
				tempFolder = f;
			}
		}

		if (!exist)
		{
			tempFolder = new Folder(s);
			tempFolder.addNote(n);
			this.folders.add(tempFolder);
		} else
		{
			boolean existN = false;
			for (Note temp : tempFolder.getNotes())
			{
				if (temp.equals(n))
					existN = true;
			}
			if (!existN)
			{
				tempFolder.addNote(n);

			} else
			{
				existN = true;
				System.out.println("Create note" + n.getTitle() + " under folder " + s + " failed");
				return false;
			}
		}
		return true;
	}

	public ArrayList<Folder> getFolders()
	{
		return this.folders;
	}

	public void sortFolders()
	{
		Collections.sort(this.folders);
		for (Folder f : this.folders)
		{
			f.sortNotes();
		}
	}

	public List<Note> searchNotes(String keywords)
	{
		List<Note> result = new ArrayList<Note>();
		for (Folder f : this.folders)
			result.addAll(f.searchNotes(keywords));
		return result;
	}

	public boolean createTextNote(String folderName, String title, String content)
	{
		TextNote note = new TextNote(title, content);
		return insertNote(folderName, note);
	}
}
