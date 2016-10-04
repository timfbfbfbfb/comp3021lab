package base;

import java.io.*;

public class TextNote extends Note implements Serializable {
    protected String content;

    public TextNote(String s) {
        super(s);
    }

    public TextNote(String title, String content) {
        super(title);
        this.content = content;
    }

    public TextNote(File f) throws IOException {
        super(f.getName());
        this.content = getTextFromFile(f.getAbsolutePath());
    }

    private String getTextFromFile(String absolutePath) throws IOException {
        String result = "";
        // TODO
        BufferedReader br = new BufferedReader(new FileReader(new File(absolutePath)));
        result = br.readLine();
        return result;
    }

    public void exportTextToFile(String pathFolder) {
        //TODO
        File file = new File(pathFolder + File.separator + super.getTitle().replaceAll(" ", "_") + ".txt");
        // TODO
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
