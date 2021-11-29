package movieReviewClassification;


import java.io.*;
import java.util.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;
import java.text.*;



/**
    @author williams
    @author bowers
 */
public class ReviewHandler extends AbstractReviewHandler {



    /**
     * Loads reviews from a given path. If the given path is a .txt file, then
     * a single review is loaded. Otherwise, if the path is a folder, all reviews
     * in it are loaded.
     * @param filePath The path to the file (or folder) containing the review(sentimentModel).
     * @param realClass The real class of the review (0 = Negative, 1 = Positive
     * 2 = Unknown).
     * @return A list of reviews as objects.
     */
    public void loadReviews(String filePath, int realClass) {
        // Portable seperator for directories
        String fileSeparatorChar = System.getProperty("file.separator");
        // the current size of the database, keeps duplicates from happening
        int currentSize = database.size();
        // Is used to get the accuracy percentage at the end
        int correctPolarity = 0;

        // Text file
        if (filePath.indexOf(".txt") != -1) {

            MovieReview review = new MovieReview();
            try {
                review = readReview(filePath, realClass);
            }
            // File doesn't exist
            catch(IOException e) {
                e.printStackTrace();
            }

            // Assign review an ID and place it into the permenent database
            review.setId(currentSize);
            database.put((currentSize), review);
            // Check for ground and predicted match
            if (review.getRealPolarity() == review.getPredictedPolarity()) { correctPolarity++; }
            DecimalFormat dc = new DecimalFormat("0.0");
            String str = dc.format((double) correctPolarity / 1 * 100);
            MainApp.mainText.append("\nCorrect classification rate: " + str + "%");
            //System.out.format("Correct classification rate: %.1f%%%n", (float) correctPolarity / 1 * 100);


        }

        //Folder
        else {

            File folder = new File(filePath);
            // Get all files in the folder and go through them
            String[] folderList = folder.list();
            try {
                for (int i = 0; i < folderList.length; i++) {

                    // only accept .txt files
                    if (folderList[i].indexOf(".txt") != -1) {
                        MovieReview review = new MovieReview();

                        try {
                            review = readReview(filePath + fileSeparatorChar + folderList[i], realClass);
                        }
                        catch(IOException e) {
                            e.printStackTrace();
                        }

                        review.setId(currentSize + i);
                        database.put((currentSize + i), review);

                        if (review.getRealPolarity() == review.getPredictedPolarity()) { correctPolarity++; }

                    }
                }
                //System.out.format("Correct classification rate: %.1f%%%n", (float) correctPolarity / folderList.length * 100);
                DecimalFormat dc = new DecimalFormat("0.0");
                String str = dc.format((double) correctPolarity / folderList.length * 100);
                MainApp.mainText.append("\nCorrect classification rate: " + str + "%");
            }
            // No file was found
            catch(Exception c) { MainApp.mainText.append("\nThe previous path you entered did not find a file or folder."); }


          }

      }

    /**
     * Reads a single review file and returns it as a MovieReview object.
     * This method also calls the method classifyReview to predict the polarity
     * of the review.
     * @param reviewFilePath A path to a .txt file containing a review.
     * @param realClass The real class entered by the user.
     * @return a MovieReview object.
     * @throws IOException if specified file cannot be openned.
     */
    public MovieReview readReview(String reviewFilePath, int realClass) throws IOException {

        MovieReview movieRev = new MovieReview();
        // Read through and scan the file for its text
        FileReader file = new FileReader(reviewFilePath);
        Scanner sc = new Scanner(file);
        // Just for output
        String fileSeparatorChar = System.getProperty("file.separator");

        // Store all the text in the file, including new lines if they exist
        String revText = "";
        while (sc.hasNextLine()) {
            revText += sc.nextLine();
        }
        movieRev.setText(revText);
        // Ground truth entered by the user earlier.
        movieRev.setRealPolarity(realClass);
        // Use the .jar file to generate a predicted polarity
        classifyReview(movieRev);

        close(file);
        // Output confirming that the file name was successfully uploaded uploaded
        int fileIndex = reviewFilePath.lastIndexOf(fileSeparatorChar);
        MainApp.mainText.append("\n" + reviewFilePath.substring(fileIndex + 1) + "...Uploaded");

        return movieRev;

      }

    /**
     * Deletes a review from the database, given its id.
     * @param id The id value of the review.
     */
    public void deleteReview(int id) {
      database.remove(id);

      // Shift the movie review's following the removed one down by 1 ID
      // to fill in the space left by the deleted data, this prevents duplicates.
      // It will create a new hole on to the top of the database though.
      for (int i = id; i < database.size(); i++) {
          database.put(i, database.get(i + 1));
          database.get(i).setId(database.get(i).getId() - 1);
          database.remove(i + 1);
      }


    }

    /**
     * Loads review database.
     */
    @SuppressWarnings("unchecked")
    public void loadSerialDB() {
        MainApp.mainText.append("Loading database...");
        //Deserialize the database
        InputStream file = null;
        InputStream buffer = null;
        ObjectInput input = null;
        try {
            file = new FileInputStream(DATA_FILE_NAME);
            buffer = new BufferedInputStream(file);
            input = new ObjectInputStream(buffer);

            try {
                database = (HashMap) input.readObject();
            }
            catch(ClassNotFoundException c) {
                c.printStackTrace();
            }

            input.close();
        } catch (IOException ex) {
            System.err.println(ex.toString());
            ex.printStackTrace();
        } finally {
            close(file);
        }
        MainApp.mainText.append("Done.");
    }



    /**
     * Searches the review database by id.
     * @param id The id to search for.
     * @return The review that matches the given id or null if the id does not
     * exist in the database.
     */
    public MovieReview searchById(int id) { return database.get(id); }


    /**
     * Searches the review database for reviews matching a given substring.
     * @param substring The substring to search for.
     * @return A list of review objects matching the search criterion.
     */
    public List<MovieReview> searchBySubstring(String substring) {
        // Create a list of movie reviews of return at the end
        List<MovieReview> matches = new ArrayList<MovieReview>();
        // Iterator to iterate through our database
        Iterator dataIterator = database.entrySet().iterator();
        // Look for reviews containing our substring
        while (dataIterator.hasNext()) {
            Map.Entry dataElement = (Map.Entry)dataIterator.next();
            // Add to the list to return if we find a match
            if (database.get(dataElement.getKey()).getText().contains(substring)) {
                matches.add(database.get(dataElement.getKey()));

            }
          }

        return matches;
    }

    public List<MovieReview> getEveryReview() {
        // Create a list of movie reviews of return at the end
        List<MovieReview> reviews = new ArrayList<MovieReview>();
        // Iterator to iterate through our database
        Iterator dataIterator = database.entrySet().iterator();
        // Look for reviews containing our substring
        while (dataIterator.hasNext()) {
            Map.Entry dataElement = (Map.Entry)dataIterator.next();
            // Add to the list to return if we find a match
            reviews.add(database.get(dataElement.getKey()));

        }

        return reviews;

    }



}
