package org.cis120;

import java.util.Iterator;
import java.io.BufferedReader;
import java.util.*;
import java.io.*;

/**
 * FileLineIterator provides a useful wrapper around Java's provided
 * BufferedReader and provides practice with implementing an Iterator. Your
 * solution should not read the entire file into memory at once, instead reading
 * a line whenever the next() method is called.
 * <p>
 * Note: Any IOExceptions thrown by readers should be caught and handled
 * properly. Do not use the ready() method from BufferedReader.
 */
public class FileLineIterator implements Iterator<String> {

    // Add the fields needed to implement your FileLineIterator
    private BufferedReader reader;
    String nextString;

    /**
     * Creates a FileLineIterator for the reader. Fill out the constructor so
     * that a user can instantiate a FileLineIterator. Feel free to create and
     * instantiate any variables that your implementation requires here. See
     * recitation and lecture notes for guidance.
     * <p>
     * If an IOException is thrown by the BufferedReader, then hasNext should
     * return false.
     * <p>
     * The only method that should be called on BufferedReader is readLine() and
     * close(). You cannot call any other methods.
     *
     * @param reader - A reader to be turned to an Iterator
     * @throws IllegalArgumentException if reader is null
     */
    public FileLineIterator(BufferedReader reader) {
        // below throws exception at null input
        if (reader == null) {
            throw new IllegalArgumentException("FileLineIterator(): Reader is null");
        }
        this.reader = reader;
        try {
            nextString = reader.readLine();
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(e);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Creates a FileLineIterator from a provided filePath by creating a
     * FileReader and BufferedReader for the file.
     * 
     * @param filePath - a string representing the file
     * @throws IllegalArgumentException if filePath is null or if the file
     *                                  doesn't exist
     */
    public FileLineIterator(String filePath) {
        this(fileToReader(filePath));
    }

    /**
     * Takes in a filename and creates a BufferedReader.
     * See Java's documentation for BufferedReader to learn how to construct one
     * given a path to a file.
     *
     * @param filePath - the path to the CSV file to be turned to a
     *                 BufferedReader
     * @return a BufferedReader of the provided file contents
     * @throws IllegalArgumentException if filePath is null or if the file
     *                                  doesn't exist
     */
    public static BufferedReader fileToReader(String filePath) {
        // below catches null String input
        if (filePath == null) {
            throw new IllegalArgumentException("fileToReader(): Input String filePath is null");
        }

        BufferedReader in;
        try {
            in = new BufferedReader(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
        return in;
    }

    /**
     * Returns true if there are lines left to read in the file, and false
     * otherwise.
     * <p>
     * If there are no more lines left, this method should close the
     * BufferedReader.
     *
     * @return a boolean indicating whether the FileLineIterator can produce
     *         another line from the file
     */
    @Override
    public boolean hasNext() {
        if (nextString == null) {
            try {
                reader.close();
                return false;
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }
        return true;
    }

    /**
     * Returns the next line from the file, or throws a NoSuchElementException
     * if there are no more strings left to return (i.e. hasNext() is false).
     * <p>
     * This method also advances the iterator in preparation for another
     * invocation. If an IOException is thrown during a next() call, your
     * iterator should make note of this such that future calls of hasNext()
     * will return false and future calls of next() will throw a
     * NoSuchElementException
     *
     * @return the next line in the file
     * @throws java.util.NoSuchElementException if there is no more data in the
     *                                          file
     */
    @Override
    public String next() {
        // catches and throws error if next() is called on with no lines left
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        String line = nextString;

        // Advancing String Iterator
        try {
            nextString = reader.readLine();
        } catch (IOException e) {
            nextString = null;
        }

        return line;
    }
}
