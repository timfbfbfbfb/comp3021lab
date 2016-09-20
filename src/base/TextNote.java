package base;

public class TextNote extends Note
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
}
