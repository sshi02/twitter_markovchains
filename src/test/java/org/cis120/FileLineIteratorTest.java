package org.cis120;

import org.junit.jupiter.api.Test;
import java.io.StringReader;
import java.io.BufferedReader;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/** Tests for FileLineIterator */
public class FileLineIteratorTest {

    /*
     * Here's a test to help you out, but you still need to write your own.
     */

    @Test
    public void testHasNextAndNext() {

        // Note we don't need to create a new file here in order to test out our
        // FileLineIterator if we do not want to. We can just create a
        // StringReader to make testing easy!
        String words = "0, The end should come here.\n"
                + "1, This comes from data with no duplicate words!";
        StringReader sr = new StringReader(words);
        BufferedReader br = new BufferedReader(sr);
        FileLineIterator li = new FileLineIterator(br);
        assertTrue(li.hasNext());
        assertEquals("0, The end should come here.", li.next());
        assertTrue(li.hasNext());
        assertEquals("1, This comes from data with no duplicate words!", li.next());
        assertFalse(li.hasNext());
    }

    /* **** ****** **** WRITE YOUR TESTS BELOW THIS LINE **** ****** **** */

    @Test
    public void testFileLineIteratorNullReader() {
        BufferedReader br = null;
        IllegalArgumentException thrown =
                assertThrows(IllegalArgumentException.class,
                    () -> new FileLineIterator(br));
        assertEquals("FileLineIterator(): Reader is null", thrown.getMessage());
    }

    @Test
    public void testFileToReaderNullInput() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
            () -> FileLineIterator.fileToReader(null));
        assertEquals("fileToReader(): Input String filePath is null", thrown.getMessage());
    }

    /*
     * Below test the behavior of both: (expected: throw IOException)
     *  - fileLineIterator(String filePath) construction
     *  - fileToReader()
     *  fileToReader() will be invoked through fileLineIterator and throw the same error.
     */
    @Test
    public void testFileLineIteratorIOException() {
        assertThrows(IllegalArgumentException.class,
            () -> new FileLineIterator("poggers"));
    }

    @Test
    public void testFileLineIteratorFilePathConstructor() {
        String path = ("files/simple_test_data.csv");
        FileLineIterator li = new FileLineIterator(path);
        assertTrue(li.hasNext());
        assertEquals("0, The end should come here.", li.next());
        assertTrue(li.hasNext());
        assertEquals("1, This comes from data with no duplicate words!", li.next());
        assertFalse(li.hasNext());
    }

    // java.util.* had to be imported for the below test case
    @Test
    public void testNextNoMoreDataInFile() {
        String path = ("files/simple_test_data.csv");
        FileLineIterator li = new FileLineIterator(path);
        assertEquals("0, The end should come here.", li.next());
        assertEquals("1, This comes from data with no duplicate words!", li.next());

        assertFalse(li.hasNext());
        assertThrows(NoSuchElementException.class,
            () -> li.next());
    }
}
