package org.aksw.twig;

import org.aksw.twig.automaton.messageCount.MessageCounter;
import org.aksw.twig.automaton.time.TimeCounter;
import org.aksw.twig.automaton.tweets.WordMatrix;
import org.aksw.twig.parsing.Twitter7Parser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

public class Main {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        if (args.length == 0) {
            LOGGER.error("No arguments given. To get an overview please use the argument --help.");
            return;
        }

        switch (args[0]) {
            case "--help": LOGGER.info("Use 'Twitter7Parser arg0 arg1 ...' to parse twitter data."); break; // TODO: alter help
            case "Twitter7Parser": Twitter7Parser.main(Arrays.copyOfRange(args, 1, args.length)); break;
            default: LOGGER.info("No argument recognized. To get an overview please use the argument --help.");
        }
    }
}
