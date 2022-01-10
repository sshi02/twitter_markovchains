package org.cis120;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

/** Tests for TweetParser */
public class TweetParserTest {

    // A helper function to create a singleton list from a word
    private static List<String> singleton(String word) {
        List<String> l = new LinkedList<String>();
        l.add(word);
        return l;
    }

    // A helper function for creating lists of strings
    private static List<String> listOfArray(String[] words) {
        List<String> l = new LinkedList<String>();
        for (String s : words) {
            l.add(s);
        }
        return l;
    }

    // Cleaning and filtering tests -------------------------------------------
    @Test
    public void removeURLsTest() {
        assertEquals("abc . def.", TweetParser.removeURLs("abc http://www.cis.upenn.edu. def."));
        assertEquals("abc", TweetParser.removeURLs("abc"));
        assertEquals("abc ", TweetParser.removeURLs("abc http://www.cis.upenn.edu"));
        assertEquals("abc .", TweetParser.removeURLs("abc http://www.cis.upenn.edu."));
        assertEquals(" abc ", TweetParser.removeURLs("http:// abc http:ala34?#?"));
        assertEquals(" abc  def", TweetParser.removeURLs("http:// abc http:ala34?#? def"));
        assertEquals(" abc  def", TweetParser.removeURLs("https:// abc https``\":ala34?#? def"));
        assertEquals("abchttp", TweetParser.removeURLs("abchttp"));
    }

    @Test
    public void testCleanWord() {
        assertEquals("abc", TweetParser.cleanWord("abc"));
        assertEquals("abc", TweetParser.cleanWord("ABC"));
        assertNull(TweetParser.cleanWord("@abc"));
        assertEquals("ab'c", TweetParser.cleanWord("ab'c"));
    }

    @Test
    public void testExtractColumnGetsCorrectColumn() {
        assertEquals(
                " This is a tweet.",
                TweetParser.extractColumn(
                        "wrongColumn, wrong column, wrong column!, This is a tweet.", 3
                )
        );
    }

    @Test
    public void parseAndCleanSentenceNonEmptyFiltered() {
        List<String> sentence = TweetParser.parseAndCleanSentence("abc #@#F");
        List<String> expected = new LinkedList<String>();
        expected.add("abc");
        assertEquals(expected, sentence);
    }

    @Test
    public void testParseAndCleanTweetRemovesURLS1() {
        List<List<String>> sentences = TweetParser
                .parseAndCleanTweet("abc http://www.cis.upenn.edu");
        List<List<String>> expected = new LinkedList<List<String>>();
        expected.add(singleton("abc"));
        assertEquals(expected, sentences);
    }

    @Test
    public void testCsvDataToTrainingDataSimpleCSV() {
        StringReader sr = new StringReader(
                "0, The end should come here.\n" +
                        "1, This comes from data with no duplicate words!"
        );
        BufferedReader br = new BufferedReader(sr);
        List<List<String>> tweets = TweetParser.csvDataToTrainingData(br, 1);
        List<List<String>> expected = new LinkedList<List<String>>();
        expected.add(listOfArray("the end should come here".split(" ")));
        expected.add(listOfArray("this comes from data with no duplicate words".split(" ")));
        assertEquals(expected, tweets);
    }

    @Test
    public void testCsvDataToTweetsSimpleCSV() {
        StringReader sr = new StringReader(
                "0, The end should come here.\n" +
                        "1, This comes from data with no duplicate words!"
        );
        BufferedReader br = new BufferedReader(sr);
        List<String> tweets = TweetParser.csvDataToTweets(br, 1);
        List<String> expected = new LinkedList<String>();
        expected.add(" The end should come here.");
        expected.add(" This comes from data with no duplicate words!");
        assertEquals(expected, tweets);
    }

    /* **** ****** **** WRITE YOUR TESTS BELOW THIS LINE **** ****** **** */

    @Test
    public void testExtractColumnColumnOutOfBounds() {
        String line = "wrongColumn, wrong column, wrong column!, This is a tweet.";
        assertNull(TweetParser.extractColumn(line, -1));
        assertNull(TweetParser.extractColumn(line, 4));
    }

    @Test
    public void testCsvDataToTweetsAllNull() {
        StringReader sr = new StringReader(
                "0\n" +
                        "1\n" +
                        "2\n");
        BufferedReader br = new BufferedReader(sr);
        List<String> tweets = TweetParser.csvDataToTweets(br, 1);
        List<String> expected = new LinkedList<>();
        assertEquals(expected, tweets);
    }

    @Test
    public void testCsvDataToTweetsIrregularColumns() {
        StringReader sr = new StringReader(
                "0,poggies,this should be in here!\n" +
                        "1,this should not be in here!\n" +
                        "2,poggies,this should also be in here!\n" +
                        "3,this should also not be in here!\n" +
                        "4,poggies,the end should come here.");
        BufferedReader br = new BufferedReader(sr);
        List<String> tweets = TweetParser.csvDataToTweets(br, 2);
        List<String> expected = new LinkedList<>();
        expected.add("this should be in here!");
        expected.add("this should also be in here!");
        expected.add("the end should come here.");
        assertEquals(expected, tweets);
    }

    @Test
    public void testParseAndCleanSentenceAllBad() {
        List<String> sentence = TweetParser.parseAndCleanSentence("$%$$ #@#F");
        List<String> expected = new LinkedList<String>();
        assertEquals(expected, sentence);
    }

    @Test
    public void testParseAndCleanSentenceLongerSomeBad() {
        List<String> sentence = TweetParser.parseAndCleanSentence("Meme ..   hehe #s## poggers");
        List<String> expected = new LinkedList<>();
        expected.add("meme");
        expected.add("hehe");
        expected.add("poggers");
        assertEquals(expected, sentence);
    }

    @Test
    public void testParseAndCleanSentenceNoPunctuation() {
        List<String> sentence = TweetParser.parseAndCleanSentence("Meme .. #  hehe #s## poggers.");
        List<String> expected = new LinkedList<>();
        expected.add("meme");
        expected.add("hehe");
        expected.add("poggers");
        assertEquals(expected, sentence);
    }

    @Test
    public void testParseAndCleanTweetMultipleSentencesWithURLs() {
        List<List<String>> sentences = TweetParser.parseAndCleanTweet(
                "abc http://www.cis.upenn.edu . poggers . http://www.cis.upenn.edu ." +
                "pog http://www.cis.upenn.edu" +
                "http://www.cis.upenn.edu");
        List<List<String>> expected = new LinkedList<>();
        expected.add(singleton("abc"));
        expected.add(singleton("poggers"));
        expected.add(singleton("pog"));
        assertEquals(expected, sentences);
    }

    @Test
    public void testParseAndCleanTweetEmptyTweet() {
        List<List<String>> sentences = TweetParser.parseAndCleanTweet("");
        List<List<String>> expected = new LinkedList<>();
        assertEquals(expected, sentences);
    }

    @Test
    public void testParseAndCleanTweetOnlyUrl() {
        List<List<String>> sentences = TweetParser.parseAndCleanTweet("http://www.cis.upenn.edu");
        List<List<String>> expected = new LinkedList<>();
        assertEquals(expected, sentences);
    }

    @Test
    public void testCsvDataToTrainingDataComplexTweets() {
        StringReader sr = new StringReader(
                "0,abc %#$  http://www.cis.upenn.edu.\n" +
                        "this should not be here!\n" +
                        "1,This comes from data with no duplicate words!"
        );
        BufferedReader br = new BufferedReader(sr);
        List<List<String>> tweets = TweetParser.csvDataToTrainingData(br, 1);
        List<List<String>> expected = new LinkedList<List<String>>();
        expected.add(listOfArray("abc".split(" ")));
        expected.add(listOfArray("this comes from data with no duplicate words".split(" ")));
        assertEquals(expected, tweets);
    }

    @Test
    public void testCsvDataToTrainingDataEmptySentences() {
        StringReader sr = new StringReader(
                "0,abc %#$  http://www.cis.upenn.edu.\n" +
                        "this should not be here!\n" +
                        "1, .\n" +
                        "2,This comes from data with no duplicate words!\n" +
                        "3, ###"
        );
        BufferedReader br = new BufferedReader(sr);
        List<List<String>> tweets = TweetParser.csvDataToTrainingData(br, 1);
        List<List<String>> expected = new LinkedList<List<String>>();
        expected.add(listOfArray("abc".split(" ")));
        expected.add(listOfArray("this comes from data with no duplicate words".split(" ")));
        assertEquals(expected, tweets);
    }
}
