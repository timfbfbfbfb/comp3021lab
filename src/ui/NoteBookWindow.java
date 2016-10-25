package ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import base.Folder;
import base.Note;
import base.NoteBook;
import base.TextNote;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * NoteBook GUI with JAVAFX
 *
 * COMP 3021
 *
 *
 * @author valerio
 *
 */
public class NoteBookWindow extends Application {

	/**
	 * TextArea containing the note
	 */
	final TextArea textAreaNote = new TextArea("");
	/**
	 * list view showing the titles of the current folder
	 */
	final ListView<String> titleslistView = new ListView<String>();
	/**
	 *
	 * Combobox for selecting the folder
	 *
	 */
	final ComboBox<String> foldersComboBox = new ComboBox<String>();
	/**
	 * This is our Notebook object
	 */
	NoteBook noteBook = null;
	/**
	 * current folder selected by the user
	 */
	String currentFolder = "";
	/**
	 * current search string
	 */
	String currentSearch = "";

	String currentNote = "";

	Stage stage;

	public static void main(String[] args) {
		launch(NoteBookWindow.class, args);
	}

	@Override
	public void start(Stage stage) {
		loadNoteBook();
		this.stage = stage;
		// Use a border pane as the root for scene
		BorderPane border = new BorderPane();
		// add top, left and center
		border.setTop(addHBox());
		border.setLeft(addVBox());

		// add save and delete note

		Button saveNote = new Button("Save Note");
		saveNote.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				if (currentFolder == "-----" || currentNote == "") {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warning");
					alert.setContentText("Please select a folder and a note");
					alert.showAndWait();
				} else {
					Folder f = noteBook.getFolders().get(noteBook.getFolders().indexOf(new Folder(currentFolder)));
					TextNote n = new TextNote(currentNote, textAreaNote.getText());
					f.getNotes().remove(n);
					f.getNotes().add(n);
				}
			}
		});
		Button deleteNote = new Button("Delete Note");
		deleteNote.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub

				if (currentFolder == "-----" || currentNote == "") {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warning");
					alert.setContentText("Please select a folder and a note");
					alert.showAndWait();
				} else {
					Folder f = noteBook.getFolders().get(noteBook.getFolders().indexOf(new Folder(currentFolder)));
					f.removeNotes(currentNote);
					updateListView();

					// confirmation
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Succeed!");
					alert.setHeaderText("Confirmation");
					alert.setContentText("Your note has been successfully removed");
					alert.showAndWait();
				}
			}
		});

		HBox hbox1 = new HBox();
		hbox1.setSpacing(8);

		ImageView saveImg = new ImageView(new Image(new File("save.png").toURI().toString()));
		saveImg.setFitHeight(18);
		saveImg.setFitWidth(18);
		hbox1.getChildren().add(saveImg);
		hbox1.getChildren().add(saveNote);

		ImageView delImg = new ImageView(new Image(new File("delete.png").toURI().toString()));
		delImg.setFitHeight(18);
		delImg.setFitWidth(18);
		hbox1.getChildren().add(delImg);
		hbox1.getChildren().add(deleteNote);

		VBox vbox1 = new VBox();
		vbox1.getChildren().add(hbox1);
		vbox1.getChildren().add(addGridPane());

		border.setCenter(vbox1);

		Scene scene = new Scene(border);
		stage.setScene(scene);
		stage.setTitle("NoteBook COMP 3021");
		stage.show();
	}

	/**
	 * This create the top section
	 *
	 * @return
	 */
	private HBox addHBox() {

		HBox hbox = new HBox();
		hbox.setPadding(new Insets(15, 12, 15, 12));
		hbox.setSpacing(10); // Gap between nodes

		Button buttonLoad = new Button("Load");
		buttonLoad.setPrefSize(100, 20);
		// buttonLoad.setDisable(true);
		buttonLoad.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Please Choose An File Which Contains a NoteBook Object!");

				FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Serialized Object File (*.ser)",
						"*.ser");
				fileChooser.getExtensionFilters().add(filter);

				File file = fileChooser.showOpenDialog(stage);

				if (file != null) {
					loadNoteBook(file);
					updateNoteBookInfo();
					currentFolder = "-----";
					currentSearch = "";
				}
			}
		});

		Button buttonSave = new Button("Save");
		buttonSave.setPrefSize(100, 20);
		// buttonSave.setDisable(true);
		buttonSave.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String fileName = "note.ser";
				noteBook.save(fileName);
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Successfully saved");
				alert.setContentText("You file has been saved to file " + fileName);
				alert.showAndWait().ifPresent(rs -> {
					if (rs == ButtonType.OK) {
						System.out.println("Pressed OK.");
					}
				});
			}
		});

		Label searchLabel = new Label("Search : ");
		searchLabel.setStyle("-fx-font-size: 15px; -fx-font-family: Courier, Arial");

		TextField searchBox = new TextField("");
		searchBox.setStyle("-fx-font-size: 15px; -fx-font-family: Courier, Arial");

		Button searchBtn = new Button("Search");
		searchBtn.setStyle("-fx-font-size: 15px; -fx-font-family: Courier, Arial");
		searchBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				currentSearch = searchBox.getText();
				textAreaNote.setText("");

				Folder folder = null;
				for (Folder fd : noteBook.getFolders())
					if (fd.getName().equals(currentFolder)) {
						folder = fd;
						break;
					}

				if (folder != null) {
					ArrayList<Note> searchResult = new ArrayList<Note>(folder.searchNotes(currentSearch));
					titleslistView.getItems().clear();
					for (Note n : searchResult)
						titleslistView.getItems().add(n.getTitle());
				}
			}

		});

		Button clearSearchBtn = new Button("Clear Search");
		clearSearchBtn.setStyle("-fx-font-size: 15px; -fx-font-family: Courier, Arial");
		clearSearchBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				currentSearch = "";
				searchBox.setText("");
				textAreaNote.setText("");
				currentNote = "";
				titleslistView.getSelectionModel().clearSelection();
				updateListView();
			}
		});

		hbox.getChildren().addAll(buttonLoad, buttonSave, searchLabel, searchBox, searchBtn, clearSearchBtn);

		return hbox;
	}

	private void updateNoteBookInfo() {

		foldersComboBox.getItems().clear();
		titleslistView.getItems().clear();
		textAreaNote.setText("");

		for (Folder fd : noteBook.getFolders())
			foldersComboBox.getItems().add(fd.getName());

		foldersComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue ov, Object t, Object t1) {
				if (t1 == null)
					return;
				currentFolder = t1.toString();
				// this contains the name of the folder selected
				// TODO update listview

				updateListView();

			}
		});
	}

	/**
	 * this create the section on the left
	 *
	 * @return
	 */
	private VBox addVBox() {

		VBox vbox = new VBox();
		vbox.setPadding(new Insets(10)); // Set all sides to 10
		vbox.setSpacing(8); // Gap between nodes

		for (Folder fd : noteBook.getFolders())
			foldersComboBox.getItems().add(fd.getName());

		foldersComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue ov, Object t, Object t1) {
				if (t1 == null)
					return;
				currentFolder = t1.toString();
				// this contains the name of the folder selected
				// TODO update listview

				updateListView();

			}

		});

		foldersComboBox.setValue("-----");

		titleslistView.setPrefHeight(100);

		titleslistView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue ov, Object t, Object t1) {
				if (t1 == null)
					return;
				String title = t1.toString();
				currentNote = title;
				// This is the selected title
				// TODO load the content of the selected note in
				// textAreNote
				String content = "";

				Folder folder = null;
				Note note = null;

				for (Folder fd : noteBook.getFolders())
					if (fd.getName().equals(currentFolder)) {
						folder = fd;
						break;
					}

				if (folder != null)
					for (Note n : folder.getNotes())
						if (n.getTitle().equals(title)) {
							note = n;
							break;
						}

				if (note instanceof TextNote)
					content = ((TextNote) note).getContent();

				textAreaNote.setText(content);

			}
		});

		Button addAFolder = new Button("Add a Folder");
		addAFolder.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				TextInputDialog dialog = new TextInputDialog("Add a Folder");
				dialog.setTitle("Input");
				dialog.setHeaderText("Add a new folder for your notebook:");
				dialog.setContentText("Please enter the name you want to create:");
				// Traditional way to get the response value.
				Optional<String> result = dialog.showAndWait();
				if (result.isPresent()) {
					// TODO

					if (result.get().isEmpty() || noteBook.getFolders().contains(new Folder(result.get()))) {
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("Warning");
						if (result.get().isEmpty())
							alert.setContentText("Please input a valid folder name");
						else
							alert.setContentText("You already have a folder named with " + result.get());
						alert.showAndWait();
					} else {
						noteBook.getFolders().add(new Folder(result.get()));
						updateNoteBookInfo();
					}
				}
			}
		});

		Button addANote = new Button("Add a Note");
		addANote.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				if (currentFolder == "" || currentFolder == "-----") {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warning");
					alert.setContentText("Please choose a folder first!");
					alert.showAndWait();
				} else {

					TextInputDialog dialog = new TextInputDialog("Add a Note");
					dialog.setTitle("Input");
					dialog.setHeaderText("Add a new note to the current folder");
					dialog.setContentText("Please enter the name of your note:");
					// Traditional way to get the response value.
					Optional<String> result = dialog.showAndWait();
					if (result.isPresent()) {
						// TODO
						if (result.get().isEmpty()) {
							Alert alert = new Alert(AlertType.WARNING);
							alert.setTitle("Warning");
							alert.setContentText("Please input a valid note name");
							alert.showAndWait();
						} else {
							noteBook.createTextNote(currentFolder, result.get());
							updateListView();
							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("Successful!");
							alert.setContentText(
									"Insert " + result.get() + " to folder " + currentFolder + " successfully!");
							alert.showAndWait();
						}
					}
				}
			}
		});

		HBox hbox1 = new HBox();
		hbox1.setSpacing(8); // Gap between nodes
		hbox1.getChildren().add(foldersComboBox);
		hbox1.getChildren().add(addAFolder);

		vbox.getChildren().add(new Label("Choose folder: "));
		vbox.getChildren().add(hbox1);
		vbox.getChildren().add(new Label("Choose note title"));
		vbox.getChildren().add(titleslistView);
		vbox.getChildren().add(addANote);

		return vbox;
	}

	private void updateListView() {
		ArrayList<String> list = new ArrayList<String>();

		// TODO populate the list object with all the TextNote titles of the
		// currentFolder
		Folder current = null;
		for (Folder fd : noteBook.getFolders())
			if (fd.getName().equals(currentFolder)) {
				current = fd;
				break;
			}

		if (current != null)
			for (Note n : current.getNotes())
				list.add(n.getTitle());

		ObservableList<String> combox2 = FXCollections.observableArrayList(list);
		titleslistView.setItems(combox2);
		textAreaNote.setText("");
	}

	/*
	 * Creates a grid for the center region with four columns and three rows
	 */
	private GridPane addGridPane() {

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(10, 10, 10, 10));
		textAreaNote.setEditable(true);
		textAreaNote.setMaxSize(450, 400);
		textAreaNote.setWrapText(true);
		textAreaNote.setPrefWidth(450);
		textAreaNote.setPrefHeight(400);
		// 0 0 is the position in the grid
		grid.add(textAreaNote, 0, 0);

		return grid;
	}

	private void loadNoteBook(File file) {
		noteBook = new NoteBook(file.getAbsolutePath());
	}

	private void loadNoteBook() {
		NoteBook nb = new NoteBook();
		nb.createTextNote("COMP3021", "COMP3021 syllabus", "Be able to implement object-oriented concepts in Java.");
		nb.createTextNote("COMP3021", "course information",
				"Introduction to Java Programming. Fundamentals include language syntax, object-oriented programming, inheritance, interface, polymorphism, exception handling, multithreading and lambdas.");
		nb.createTextNote("COMP3021", "Lab requirement",
				"Each lab has 2 credits, 1 for attendence and the other is based the completeness of your lab.");

		nb.createTextNote("Books", "The Throwback Special: A Novel",
				"Here is the absorbing story of twenty-two men who gather every fall to painstakingly reenact what ESPN called “the most shocking play in NFL history” and the Washington Redskins dubbed the “Throwback Special”: the November 1985 play in which the Redskins’ Joe Theismann had his leg horribly broken by Lawrence Taylor of the New York Giants live on Monday Night Football. With wit and great empathy, Chris Bachelder introduces us to Charles, a psychologist whose expertise is in high demand; George, a garrulous public librarian; Fat Michael, envied and despised by the others for being exquisitely fit; Jeff, a recently divorced man who has become a theorist of marriage; and many more. Over the course of a weekend, the men reveal their secret hopes, fears, and passions as they choose roles, spend a long night of the soul preparing for the play, and finally enact their bizarre ritual for what may be the last time. Along the way, mishaps, misunderstandings, and grievances pile up, and the comforting traditions holding the group together threaten to give way. The Throwback Special is a moving and comic tale filled with pitch-perfect observations about manhood, marriage, middle age, and the rituals we all enact as part of being alive.");
		nb.createTextNote("Books", "Another Brooklyn: A Novel",
				"The acclaimed New York Times bestselling and National Book Award–winning author of Brown Girl Dreaming delivers her first adult novel in twenty years. Running into a long-ago friend sets memory from the 1970s in motion for August, transporting her to a time and a place where friendship was everything—until it wasn’t. For August and her girls, sharing confidences as they ambled through neighborhood streets, Brooklyn was a place where they believed that they were beautiful, talented, brilliant—a part of a future that belonged to them. But beneath the hopeful veneer, there was another Brooklyn, a dangerous place where grown men reached for innocent girls in dark hallways, where ghosts haunted the night, where mothers disappeared. A world where madness was just a sunset away and fathers found hope in religion. Like Louise Meriwether’s Daddy Was a Number Runner and Dorothy Allison’s Bastard Out of Carolina, Jacqueline Woodson’s Another Brooklyn heartbreakingly illuminates the formative time when childhood gives way to adulthood—the promise and peril of growing up—and exquisitely renders a powerful, indelible, and fleeting friendship that united four young lives.");

		nb.createTextNote("Holiday", "Vietnam",
				"What I should Bring? When I should go? Ask Romina if she wants to come");
		nb.createTextNote("Holiday", "Los Angeles", "Peter said he wants to go next Agugust");
		nb.createTextNote("Holiday", "Christmas", "Possible destinations : Home, New York or Rome");
		noteBook = nb;

	}

}
