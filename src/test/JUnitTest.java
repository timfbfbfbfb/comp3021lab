package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;

import base.Note;
import base.NoteBook;
import base.TextNote;

public class JUnitTest {

	@Test
	public void testSearchNote() {
		NoteBook nb = new NoteBook();
		nb.createTextNote("Note1", "Java", "comp3021");
		nb.createTextNote("Note2", "Assignment", "due on 2016-10-16");
		nb.createTextNote("Note3", "lab", "need to attend weekly");
		nb.createTextNote("Note4", "lab4", "testing");
		List<Note> notes = nb.searchNotes("java or DUE or testing");
		System.out.println(notes.size());
		assertEquals("The size of the search results is not match", 3, notes.size(), 0.0);
		HashSet<String> titles = new HashSet<String>();
		for (Note note : notes) {
			titles.add(note.getTitle());
		}
		HashSet<String> expectedOutputs = new HashSet<String>();
		expectedOutputs.add("Java");
		expectedOutputs.add("Assignment");
		expectedOutputs.add("lab4");
		assertEquals("The search results is not match", expectedOutputs, titles);
	}

	// To do
	// Design the second test case which reveals the bug in function
	// unknownFunction()

	@Test
	public void testCountLetter() {
		ArrayList<TextNote> tn = new ArrayList<TextNote>();
		tn.add(new TextNote("aaa", "bbc"));
		tn.add(new TextNote("bbb", "ca"));
		tn.add(new TextNote("cdc", "c"));
		tn.add(new TextNote("dk", "k"));
		tn.add(new TextNote("kkkKk", "KKKKKKKKK"));
		tn.add(new TextNote("sad!@#$%^&", "a$%^&*^%$%^&*a"));
		tn.add(new TextNote("ssaFFFFFFFd!@#$%^&", "a$%^&*^%$%^&*a"));
		tn.add(new TextNote("", ""));
		tn.add(new TextNote("\0\n\t\baaaa", "aaa"));
		tn.add(new TextNote("filename"));

		ArrayList<Character> result = new ArrayList<>();
		ArrayList<Character> expectedResult = new ArrayList<>();
		expectedResult.add('a');
		expectedResult.add('b');
		expectedResult.add('c');
		expectedResult.add('k');
		expectedResult.add('K');
		expectedResult.add('a');
		expectedResult.add('F');
		expectedResult.add(' ');
		expectedResult.add('a');
		expectedResult.add('e');

		for (TextNote tn1 : tn)
			result.add(tn1.countLetters());
		assertEquals("The count result is not match", expectedResult, result);
	}
}
