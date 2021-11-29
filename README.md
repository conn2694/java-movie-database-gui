# java-movie-database-gui
Allows for manipulation of a movie review database using the Swing GUI library.
Parses movie reviews and organizes them into a hashset database. Every review has an ID, review text, predicted polarity using a black box algorithm, and a real polarity that’s preassigned. The program uses multithreading to allow for the GUI to render updates in the background, allowing the program to still be navigated through without hangs or freezes while parsing data.
