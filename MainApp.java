package movieReviewClassification;

import java.io.IOException;
import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;


/**
  @author williams
 */



public class MainApp implements ActionListener {

    // All the GUI are premade as constants, we can use them everywhere not static
    private final JFrame myFrame;
    private final JComboBox<String> myMenu;
    // public static so we can use it to display some information in ReviewHandler as well as here
    public static final JTextArea mainText = new JTextArea();
    private final JScrollPane mainScroll;
    private final JTextField folderName;
    private final JLabel folderNamePrompt;
    private final JComboBox<String> classifyBox;
    private final JLabel classifyBoxPrompt;
    private final JButton confirmFileButton;
    private final JButton saveDataButton;
    private final JTextField deleteId;
    private final JLabel deleteIdPrompt;
    private final JButton deleteButton;
    private final JTextField searchId;
    private final JLabel searchIdPrompt;
    private final JButton searchIdButton;
    private final JTextField searchSubstring;
    private final JLabel searchSubstringPrompt;
    private final JButton searchSubstringButton;
    private final JButton exitButton;


    // used in popup table
    public static final JFrame reviewFrame = new JFrame("Movie Review Table");
    public static final JButton showTableButton = new JButton("Open Review Table");
    public static final JTable dataTable = new JTable();
    public static final JScrollPane scrollPane = new JScrollPane(dataTable);




    // some global constant values to make life easy when multithreading
    public static final ReviewHandler rh = new ReviewHandler();
    final File dataFile;






    // Initialized values for the GUI and file loading goes here
    public MainApp() {
      myFrame = new JFrame("Movie Review Guide");
      myFrame.setBackground(Color.BLACK);
      myFrame.setVisible(true);
      myFrame.setLayout(null);

      dataFile = new File(rh.DATA_FILE_NAME);
      if (dataFile.exists()) {
          rh.loadSerialDB();
      }


      myMenu = new JComboBox<>();
      myMenu.addItem("Please Select");
      myMenu.addItem("0. Exit program.");
      myMenu.addItem("1. Load new movie review collection (given a folder or a file path).");
      myMenu.addItem("2. Delete movie review from database (given its id).");
      myMenu.addItem("3. Search movie reviews in database by id.");
      myMenu.addItem("4. Search movie reviews in database by matching a substring.");
      myMenu.setVisible(true);
      myMenu.setBounds(50, 20, 500, 100);
      myMenu.addActionListener(this);
      myFrame.add(myMenu);



      mainText.append("\n***********************************\nWelcome to the movie database!\n***********************************");
      mainText.setVisible(true);
      mainText.setBounds(20, 400, 960, 360);

      mainScroll = new JScrollPane(mainText);
      mainScroll.setVisible(true);
      mainScroll.createVerticalScrollBar();
      mainScroll.setBounds(20, 400, 960, 360);

      myFrame.add(mainScroll);


      folderName = new JTextField();
      folderName.setVisible(false);
      folderName.setBounds(50, 170, 500, 25);
      myFrame.add(folderName);

      folderNamePrompt = new JLabel("Select Movie Review folder/file:");
      folderNamePrompt.setVisible(false);
      folderNamePrompt.setBounds(50, 140, 500, 25);
      myFrame.add(folderNamePrompt);


      classifyBox = new JComboBox<>();
      classifyBox.addItem("0: Negative review(s)");
      classifyBox.addItem("1: Positive review(s)");
      classifyBox.addItem("2: Unknown");
      classifyBox.setVisible(false);
      classifyBox.setBounds(50, 220, 250, 100);
      myFrame.add(classifyBox);


      classifyBoxPrompt = new JLabel("Select the ground truth of the review:");
      classifyBoxPrompt.setVisible(false);
      classifyBoxPrompt.setBounds(50, 190, 500, 100);
      myFrame.add(classifyBoxPrompt);


      confirmFileButton = new JButton("Load");
      confirmFileButton.setVisible(false);
      confirmFileButton.setBounds(50, 340, 100, 30);
      confirmFileButton.addActionListener(this);
      myFrame.add(confirmFileButton);

      saveDataButton = new JButton("Save Review Database");
      saveDataButton.setVisible(true);
      saveDataButton.setBounds(400, 370, 200, 30);
      saveDataButton.addActionListener(this);
      myFrame.add(saveDataButton);

      deleteId = new JTextField();
      deleteId.setVisible(false);
      deleteId.setBounds(50, 170, 500, 25);
      myFrame.add(deleteId);

      deleteIdPrompt = new JLabel("Select Movie Review to delete (ID):");
      deleteIdPrompt.setVisible(false);
      deleteIdPrompt.setBounds(50, 140, 500, 25);
      myFrame.add(deleteIdPrompt);

      deleteButton = new JButton("Delete");
      deleteButton.setVisible(false);
      deleteButton.setBounds(50, 340, 100, 30);
      deleteButton.addActionListener(this);
      myFrame.add(deleteButton);

      searchId = new JTextField();
      searchId.setVisible(false);
      searchId.setBounds(50, 170, 500, 25);
      myFrame.add(searchId);

      searchIdPrompt = new JLabel("Search Movie Review by ID:");
      searchIdPrompt.setVisible(false);
      searchIdPrompt.setBounds(50, 140, 500, 25);
      myFrame.add(searchIdPrompt);

      searchIdButton = new JButton("Search");
      searchIdButton.setVisible(false);
      searchIdButton.setBounds(50, 340, 100, 30);
      searchIdButton.addActionListener(this);
      myFrame.add(searchIdButton);

      searchSubstring = new JTextField();
      searchSubstring.setVisible(false);
      searchSubstring.setBounds(50, 170, 500, 25);
      myFrame.add(searchSubstring);

      searchSubstringPrompt = new JLabel("Search Movie Review by Substring:");
      searchSubstringPrompt.setVisible(false);
      searchSubstringPrompt.setBounds(50, 140, 500, 25);
      myFrame.add(searchSubstringPrompt);

      searchSubstringButton = new JButton("Search");
      searchSubstringButton.setVisible(false);
      searchSubstringButton.setBounds(50, 340, 100, 30);
      searchSubstringButton.addActionListener(this);
      myFrame.add(searchSubstringButton);

      exitButton = new JButton("Confirm");
      exitButton.setVisible(false);
      exitButton.setBounds(50, 340, 100, 30);
      exitButton.addActionListener(this);
      myFrame.add(exitButton);

      reviewFrame.setBackground(Color.BLACK);
      reviewFrame.setVisible(false);
      reviewFrame.setSize(600, 600);
      reviewFrame.setLayout(new GridLayout(1,1));
      reviewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


      scrollPane.createVerticalScrollBar();

      reviewFrame.add(scrollPane);

      showTableButton.setVisible(true);
      showTableButton.setBounds(700, 370, 200, 30);
      showTableButton.addActionListener(this);
      myFrame.add(showTableButton);



      myFrame.setSize(1000, 800);
      myFrame.setVisible(true);
      myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }

    // Action handler, every main if is for what action is being handled and What
    // Logic it should follow
    @Override
    public void actionPerformed(ActionEvent e) {
          // Variables


          if (e.getSource() == myMenu) {
              String text = ((JComboBox)e.getSource()).getSelectedItem().toString().substring(0, 1);

              // for the default instruction at the top of the box
              int choice = -1;
              if (text.equals("P")) { choice = -1; }
              else {
                choice = Integer.parseInt(text);
              }

              if (choice == 0) {
                folderName.setVisible(false);
                folderNamePrompt.setVisible(false);
                classifyBox.setVisible(false);
                classifyBoxPrompt.setVisible(false);
                confirmFileButton.setVisible(false);
                deleteId.setVisible(false);
                deleteIdPrompt.setVisible(false);
                deleteButton.setVisible(false);
                searchId.setVisible(false);
                searchIdPrompt.setVisible(false);
                searchIdButton.setVisible(false);
                searchSubstring.setVisible(false);
                searchSubstringPrompt.setVisible(false);
                searchSubstringButton.setVisible(false);
                exitButton.setVisible(true);

                mainText.append("\nPress confirm to exit the program and save your database.");


              }



              else if (choice == 1) {

                  // Select file or folder
                  folderName.setVisible(true);
                  folderNamePrompt.setVisible(true);
                  classifyBox.setVisible(true);
                  classifyBoxPrompt.setVisible(true);
                  confirmFileButton.setVisible(true);
                  deleteId.setVisible(false);
                  deleteIdPrompt.setVisible(false);
                  deleteButton.setVisible(false);
                  searchId.setVisible(false);
                  searchIdPrompt.setVisible(false);
                  searchIdButton.setVisible(false);
                  searchSubstring.setVisible(false);
                  searchSubstringPrompt.setVisible(false);
                  searchSubstringButton.setVisible(false);
                  exitButton.setVisible(false);


                  mainText.append("\nLoad new movie review collection (given a folder or a file path).");

              }

              else if (choice == 2) {
                folderName.setVisible(false);
                folderNamePrompt.setVisible(false);
                classifyBox.setVisible(false);
                classifyBoxPrompt.setVisible(false);
                confirmFileButton.setVisible(false);
                deleteId.setVisible(true);
                deleteIdPrompt.setVisible(true);
                deleteButton.setVisible(true);
                searchId.setVisible(false);
                searchIdPrompt.setVisible(false);
                searchIdButton.setVisible(false);
                searchSubstring.setVisible(false);
                searchSubstringPrompt.setVisible(false);
                searchSubstringButton.setVisible(false);
                exitButton.setVisible(false);

                mainText.append("\nDelete a review by entering in it's ID.");


              }

              else if (choice == 3) {
                folderName.setVisible(false);
                folderNamePrompt.setVisible(false);
                classifyBox.setVisible(false);
                classifyBoxPrompt.setVisible(false);
                confirmFileButton.setVisible(false);
                deleteId.setVisible(false);
                deleteIdPrompt.setVisible(false);
                deleteButton.setVisible(false);
                searchId.setVisible(true);
                searchIdPrompt.setVisible(true);
                searchIdButton.setVisible(true);
                searchSubstring.setVisible(false);
                searchSubstringPrompt.setVisible(false);
                searchSubstringButton.setVisible(false);
                exitButton.setVisible(false);

                mainText.append("\nSearch database reviews by ID.");


              }

              else if (choice == 4) {
                folderName.setVisible(false);
                folderNamePrompt.setVisible(false);
                classifyBox.setVisible(false);
                classifyBoxPrompt.setVisible(false);
                confirmFileButton.setVisible(false);
                deleteId.setVisible(false);
                deleteIdPrompt.setVisible(false);
                deleteButton.setVisible(false);
                searchId.setVisible(false);
                searchIdPrompt.setVisible(false);
                searchIdButton.setVisible(false);
                searchSubstring.setVisible(true);
                searchSubstringPrompt.setVisible(true);
                searchSubstringButton.setVisible(true);
                exitButton.setVisible(false);

                mainText.append("\nSearch database reviews by Substring.");

              }
          }

          else if (e.getSource() == confirmFileButton) {

              String file = ((JTextField)folderName).getText();
              int ground = Integer.parseInt(((JComboBox)classifyBox).getSelectedItem().toString().substring(0, 1));
              Thread fileThread = new Thread(new NewThread(0, file, ground));
              fileThread.start();

          }

          else if (e.getSource() == deleteButton) {
              int id;
              try { id = Integer.parseInt(((JTextField)deleteId).getText()); }
              catch(Exception c) { id = -1; }

              if (id < 0) { mainText.append("\nYou must input a non-negative integer."); }
              else if (id > rh.database.size() ) { mainText.append("\nNo review was found with that ID"); }
              else {
                  Thread deleteThread = new Thread(new NewThread(1, id));
                  deleteThread.start();

              }


          }

          else if (e.getSource() == searchIdButton) {

              int id;
              try { id = Integer.parseInt(((JTextField)searchId).getText()); }
              catch(Exception c) { id = -1; }

              if (id < 0) { mainText.append("\nYou must input a non-negative integer."); }
              else if (id > rh.database.size() ) { mainText.append("\nNo review was found with that ID"); }
              else {


                  MovieReview review = new MovieReview();

                  Thread searchIdThread = new Thread(new NewThread(2, id, review));
                  searchIdThread.start();

              }

          }

          else if (e.getSource() == searchSubstringButton) {

              String search;
              search = ((JTextField)searchSubstring).getText();

              List<MovieReview> reviews = new ArrayList<MovieReview>();

              Thread searchSubstringThread = new Thread(new NewThread(3, search, reviews));
              searchSubstringThread.start();
          }


          else if (e.getSource() == saveDataButton) {
              Thread saveThread = new Thread(new NewThread(5));
              saveThread.start();
          }

          else if (e.getSource() == exitButton) {
              Thread exitThread = new Thread(new NewThread(6));
              exitThread.start();

          }

          else if (e.getSource() == showTableButton) {

              List<MovieReview> reviews = new ArrayList<MovieReview>();
              Thread showTableThread = new Thread(new NewThread(4, reviews));
              showTableThread.start();
          }
      }


    // Main menu contains the UI to help the user navigate the program
    public static void main(String[] args) throws IOException {
        new MainApp();
    }
}

/**
  Multithreading class
  @author williams
 */
class NewThread implements Runnable {

    //Input in = new Input();
    //ReviewHandler rh;
    int runLogic = 0;
    String file = "";
    int ground = 0;
    int id = 0;
    MovieReview review = new MovieReview();
    List<MovieReview> reviews = new ArrayList<MovieReview>();
    String search = "";


    public static final DefaultTableModel tableModel = new DefaultTableModel();


    // Constructors
    NewThread(int logic) {
        runLogic = logic;
    }
    NewThread(int logic, String fi, int gr) {
        runLogic = logic;
        file = fi;
        ground = gr;
    }
    NewThread(int logic, int i) {
        runLogic = logic;
        id = i;
    }
    NewThread(int logic, int i, MovieReview rev) {
        runLogic = logic;
        id = i;
        review = rev;
    }
    NewThread(int logic, String srch, List<MovieReview> rev) {
        runLogic = logic;
        search = srch;
        reviews = rev;
    }
    NewThread(int logic, List<MovieReview> rev) {
        runLogic = logic;
        reviews = rev;
    }

    // Runs the threads, every runLogic value is linked to one of the GUI
    // buttons
    public void run() {

        if (runLogic == 0) {
            MainApp.rh.loadReviews(file, ground);

        }

        else if (runLogic == 1) {
            MainApp.rh.deleteReview(id);
            MainApp.mainText.append("\nReview Removed.");

        }

        else if (runLogic == 2) {
            review = MainApp.rh.searchById(id);

            tableModel.setRowCount(0);
            tableModel.setColumnCount(0);

            String[][] dataFormat = new String[4][1];

            try {
                String predictedWord = (review.getPredictedPolarity() == 0) ? "Negative" : "Positive";
                String groundWord = "";
                if (review.getRealPolarity() == 0) {groundWord = "Negative"; }
                else if (review.getRealPolarity() == 1) {groundWord = "Positive"; }
                else { groundWord = "Unknown"; }

                dataFormat[0][0] = String.valueOf(review.getId());
                dataFormat[1][0] = review.getText();
                dataFormat[2][0] = predictedWord;
                dataFormat[3][0] = groundWord;

                tableModel.addColumn("ID", dataFormat[0]);
                tableModel.addColumn("Text", dataFormat[1]);
                tableModel.addColumn("Predicted", dataFormat[2]);
                tableModel.addColumn("Real", dataFormat[3]);

                // Do these here so we don't create the window before the information
                // is done processing
                MainApp.dataTable.setModel(NewThread.tableModel);
                MainApp.reviewFrame.add(MainApp.scrollPane);
                MainApp.reviewFrame.setVisible(true);
            }
            catch(Exception c) { MainApp.mainText.append("\nYou've tried to get a review in a deleted position near the top of the database, there is nothing here."); }


        }

        else if (runLogic == 3) {

            reviews = MainApp.rh.searchBySubstring(search);
            if (reviews.size() == 0) { MainApp.mainText.append("\nNo reviews were found containing that bit of text"); }

            else {
                tableModel.setRowCount(0);
                tableModel.setColumnCount(0);

                String[][] dataFormat = new String[4][reviews.size()];
                for (int i = 0; i < reviews.size(); i++) {

                    String predictedWord = (reviews.get(i).getPredictedPolarity() == 0) ? "Negative" : "Positive";
                    String groundWord = "";
                    if (reviews.get(i).getRealPolarity() == 0) {groundWord = "Negative"; }
                    else if (reviews.get(i).getRealPolarity() == 1) {groundWord = "Positive"; }
                    else { groundWord = "Unknown"; }

                    dataFormat[0][i] = String.valueOf(reviews.get(i).getId());
                    dataFormat[1][i] = reviews.get(i).getText();
                    dataFormat[2][i] = predictedWord;
                    dataFormat[3][i] = groundWord;

                }
                // Make one column at a time
                tableModel.addColumn("ID", dataFormat[0]);
                tableModel.addColumn("Text", dataFormat[1]);
                tableModel.addColumn("Predicted", dataFormat[2]);
                tableModel.addColumn("Real", dataFormat[3]);


                MainApp.dataTable.setModel(tableModel);


                MainApp.reviewFrame.add(MainApp.scrollPane);
                MainApp.reviewFrame.setVisible(true);
            }

        }

        else if (runLogic == 4) {

            reviews = MainApp.rh.getEveryReview();

            tableModel.setRowCount(0);
            tableModel.setColumnCount(0);

            String[][] dataFormat = new String[4][reviews.size()];
            for (int i = 0; i < reviews.size(); i++) {

                String predictedWord = (reviews.get(i).getPredictedPolarity() == 0) ? "Negative" : "Positive";
                String groundWord = "";
                if (reviews.get(i).getRealPolarity() == 0) {groundWord = "Negative"; }
                else if (reviews.get(i).getRealPolarity() == 1) {groundWord = "Positive"; }
                else { groundWord = "Unknown"; }

                dataFormat[0][i] = String.valueOf(reviews.get(i).getId());
                dataFormat[1][i] = reviews.get(i).getText();
                dataFormat[2][i] = predictedWord;
                dataFormat[3][i] = groundWord;

            }
            // Make one column at a time
            tableModel.addColumn("ID", dataFormat[0]);
            tableModel.addColumn("Text", dataFormat[1]);
            tableModel.addColumn("Predicted", dataFormat[2]);
            tableModel.addColumn("Real", dataFormat[3]);


            MainApp.dataTable.setModel(tableModel);


            MainApp.reviewFrame.add(MainApp.scrollPane);
            MainApp.reviewFrame.setVisible(true);
        }

        else if (runLogic == 5) { MainApp.rh.saveSerialDB(); }
        else if (runLogic == 6) {
            MainApp.rh.saveSerialDB();
            System.exit(0);
        }

    }
}
