package base;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NoteBook implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<Folder> folders;

    public NoteBook() {
        this.folders = new ArrayList<Folder>();
    }

    public NoteBook(String file) {
        try {
            // TODO
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fis);

            NoteBook n = (NoteBook) in.readObject();
            //TODO
            this.folders = n.folders;
            in.close();

        } catch (IOException e) {
            System.out.println(e.toString());
        } catch (ClassNotFoundException e) {
            System.out.println(e.toString());
        }
    }

    public boolean createTextNote(String s1, String s2) {
        TextNote note = new TextNote(s2);
        return insertNote(s1, note);
    }

    public boolean createImageNote(String s1, String s2) {
        ImageNote note = new ImageNote(s2);
        return insertNote(s1, note);
    }

    private boolean insertNote(String s, Note n) {

        boolean exist = false;
        Folder tempFolder = null;
        for (Folder f : folders) {
            if (f.getName().equals(s)) {
                exist = true;
                tempFolder = f;
            }
        }

        if (!exist) {
            tempFolder = new Folder(s);
            tempFolder.addNote(n);
            this.folders.add(tempFolder);
        } else {
            boolean existN = false;
            for (Note temp : tempFolder.getNotes()) {
                if (temp.equals(n))
                    existN = true;
            }
            if (!existN) {
                tempFolder.addNote(n);

            } else {
                existN = true;
                System.out.println("Create note" + n.getTitle() + " under folder " + s + " failed");
                return false;
            }
        }
        return true;
    }

    public ArrayList<Folder> getFolders() {
        return this.folders;
    }

    public void sortFolders() {
        Collections.sort(this.folders);
        for (Folder f : this.folders) {
            f.sortNotes();
        }
    }

    public List<Note> searchNotes(String keywords) {
        List<Note> result = new ArrayList<Note>();
        for (Folder f : this.folders)
            result.addAll(f.searchNotes(keywords));
        return result;
    }

    public boolean createTextNote(String folderName, String title, String content) {
        TextNote note = new TextNote(title, content);
        return insertNote(folderName, note);
    }

    public boolean save(String file) {
        //TODO
        try {
            //TODO
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.close();
        } catch (Exception e) {
//            System.out.println(e.toString());
            return false;
        }
        return true;
    }
}
