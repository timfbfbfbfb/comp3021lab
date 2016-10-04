package base;

import java.io.*;

public class TextNote extends Note implements Serializable
{
	protected String content;

	public TextNote(String s)
	{
		super(s);
	}

	public TextNote(String title, String content)
	{
		super(title);
		this.content = content;
	}

	public TextNote(File f) throws IOException, ClassNotFoundException
	{
		super(f.getName());
		this.content = getTextFromFile(f.getAbsolutePath());
	}

	private String getTextFromFile(String absolutePath) throws IOException, ClassNotFoundException
	{
		String result = "";
		// TODO
		File file = new File(absolutePath);
		FileInputStream fis = new FileInputStream(file);
		ObjectInputStream ois = new ObjectInputStream(fis);
		TextNote tn = (TextNote) ois.readObject();
		ois.close();
		return tn.content;
	}

	public void exportTextToFile(String pathFolder)
	{
		// TODO
		if (pathFolder.isEmpty())
			pathFolder = ".";
		File file = new File(pathFolder + File.separator + super.getTitle().replaceAll(" ", "_") + ".txt");
		// TODO
		FileOutputStream fos = null;
		try
		{
			fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(this);
			oos.close();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
