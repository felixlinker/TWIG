package org.aksw.twig.parsing;

import com.google.common.util.concurrent.FutureCallback;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

public class Twitter7ReaderTest {

    private String sampleDataPath;
    private String brokenSampleDataPath;
    private String emptySampleDataPath;

    private final Set<String> allBlocks = new HashSet<>();

    @Before
    public void setSampleDataPath() {
        this.sampleDataPath = getClass().getClassLoader().getResource("data/sample.txt").getPath();
        this.brokenSampleDataPath = getClass().getClassLoader().getResource("data/sample_broken.txt").getPath();
        this.emptySampleDataPath = getClass().getClassLoader().getResource("data/sample_empty.txt").getPath();
    }

    /**
     * Tests reading two blocks of twitter7 data.
     */
    @Test
    public void testBlockReading() {
        File f = new File(sampleDataPath);
        try (BufferedReader fileReader = new BufferedReader(new FileReader(f))) {
            Twitter7Reader<String> reader = new Twitter7Reader<>(f, Callback::new, Parser::new );
            Triple<String, String, String> triple = reader.readTwitter7Block(fileReader);
            Assert.assertEquals("       2009-09-30 23:55:53", triple.getLeft());
            Assert.assertEquals("       http://twitter.com/user1", triple.getMiddle());
            Assert.assertEquals("       I'm starting to feel really sick, hope is not the S**** flu! (That's the new S-word)", triple.getRight());

            triple = reader.readTwitter7Block(fileReader);
            Assert.assertEquals("       2009-09-30 23:55:53", triple.getLeft());
            Assert.assertEquals("       http://twitter.com/user2", triple.getMiddle());
            Assert.assertEquals("       soooo i got sum advice from the 1 i love most...he goes drop all those lame ass birds uno unot like them...lol here it goes", triple.getRight());
        } catch (IOException e) {
            Assert.fail("Class creation failed.");
        }
    }

    /**
     * Tests skipping broken blocks of twitter7 data.
     */
    @Test
    public void testBrokenBlockReading() {
        File f = new File(brokenSampleDataPath);
        try (BufferedReader fileReader = new BufferedReader(new FileReader(f))) {
            Twitter7Reader<String> reader = new Twitter7Reader<>(f, Callback::new, Parser::new );
            Triple<String, String, String> triple = reader.readTwitter7Block(fileReader);
            Assert.assertEquals("       2009-09-30 23:55:53", triple.getLeft());
            Assert.assertEquals("       http://twitter.com/user7", triple.getMiddle());
            Assert.assertEquals("       I'm writing my first twitter!!", triple.getRight());
        } catch (IOException e) {
            Assert.fail("Class creation failed.");
        }
    }

    /**
     * Tests correct termination on empty file.
     */
    @Test
    public void testEmptyBlockReading() {
        File f = new File(emptySampleDataPath);
        try (BufferedReader fileReader = new BufferedReader(new FileReader(f))) {
            Twitter7Reader<String> reader = new Twitter7Reader<>(f, Callback::new, Parser::new );
            Triple<String, String, String> triple = reader.readTwitter7Block(fileReader);
            Assert.assertNull(triple);

        } catch (IOException e) {
            Assert.fail("Class creation failed.");
        }
    }

    /**
     * Tests reading a whole file.
     */
    @Test
    public void testReading() {
        allBlocks.add("T       2009-09-30 23:55:53\n" +
                "U       http://twitter.com/user1\n" +
                "W       I'm starting to feel really sick, hope is not the S**** flu! (That's the new S-word)\n");
        allBlocks.add("T       2009-09-30 23:55:53\n" +
                "U       http://twitter.com/user2\n" +
                "W       soooo i got sum advice from the 1 i love most...he goes drop all those lame ass birds uno unot like them...lol here it goes\n");
        allBlocks.add("T       2009-09-30 23:55:53\n" +
                "U       http://twitter.com/user3\n" +
                "W       @user8 PAAAUULLLL! I miss youuu! Lol I thought I sent you an email, but it was placed in my Drafts so I'll resend that to you! =]\n");
        allBlocks.add("T       2009-09-30 23:55:53\n" +
                "U       http://twitter.com/user4\n" +
                "W       I'm sad. I'm in pain. I want to cry.\n");
        allBlocks.add("T       2009-09-30 23:55:53\n" +
                "U       http://twitter.com/user5\n" +
                "W       @user9 haha yeah I made my own Twitter client! :-)\n");
        allBlocks.add("T       2009-09-30 23:55:53\n" +
                "U       http://twitter.com/user6\n" +
                "W       Get this-I caught my mom listening to \"Slippery When Wet\" by Bon Jovi...and she said she LIKES IT! Winger next? ;)\n");
        allBlocks.add("T       2009-09-30 23:55:53\n" +
                "U       http://twitter.com/user7\n" +
                "W       I'm writing my first twitter!!\n");

        try {
            Twitter7Reader<String> reader = new Twitter7Reader<>(new File(sampleDataPath), Callback::new , Parser::new );
            reader.read();
            while (!reader.isFinished());
            Assert.assertTrue(allBlocks.isEmpty());
        } catch (IOException e) {
            Assert.fail("Class creation failed.");
        }
    }

    private class Callback implements FutureCallback<String> {

        @Override
        public void onSuccess(String result) {
            synchronized (allBlocks) {
                allBlocks.remove(result);
            }
        }

        @Override
        public void onFailure(Throwable t) {
            Assert.fail(t.toString());
        }
    }

    private class Parser implements Callable<String> {

        private String result;

        Parser(Triple<String, String, String> parsingResult) {
            this.result = "T"
                    .concat(parsingResult.getLeft())
                    .concat("\nU")
                    .concat(parsingResult.getMiddle())
                    .concat("\nW")
                    .concat(parsingResult.getRight())
                    .concat("\n");
        }

        @Override
        public String call() throws Exception {
            return this.result;
        }
    }
}
