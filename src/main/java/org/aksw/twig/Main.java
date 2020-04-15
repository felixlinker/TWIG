package org.aksw.twig;

import java.util.Arrays;

import org.aksw.twig.application.LoadModels;
import org.aksw.twig.automaton.Automaton;
import org.aksw.twig.automaton.data.MessageCounterHandler;
import org.aksw.twig.automaton.data.TimeCounterHandler;
import org.aksw.twig.automaton.data.WordMatrixHandler;
import org.aksw.twig.automaton.data.WordSampler;
import org.aksw.twig.parsing.Twitter7Parser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class to wrap main classes for the .jar.
 */
public class Main {

  private static final Logger LOGGER = LogManager.getLogger(Main.class);

  public static void main(final String[] args) {
    if (args.length == 0) {
      LOGGER.error("No arguments given. To get an overview please use the argument --help.");
      return;
    }

    switch (args[0]) {
      case "--help":
        LOGGER.info(//
            "\nUsage: <command>".concat(System.lineSeparator())//
                + "Commands:".concat(System.lineSeparator())//
                + "Twitter7Parser arg0 arg1\t\t to parse twitter7 data files arg0 and arg1"
                    .concat(System.lineSeparator())//
                + "WordSampler \t\t".concat(System.lineSeparator())//
                + "Automaton \t\t".concat(System.lineSeparator())//
                + "MessageCounterHandler \t\t".concat(System.lineSeparator())//
                + "WordMatrixHandler \t\t".concat(System.lineSeparator())//
                + "TimeCounterHandler \t\t".concat(System.lineSeparator())//
                + "MergeModels \t\t".concat(System.lineSeparator())//
        );
        break;

      /*
       * parses twitter data into an RDF model
       *
       */
      case "Twitter7Parser":
        Twitter7Parser.main(Arrays.copyOfRange(args, 1, args.length));
        break;

      /**
       *
       */
      case "WordSampler":
        WordSampler.main(Arrays.copyOfRange(args, 1, args.length));
        break;

      /*
       * Mimicking approach
       */
      case "Automaton":
        Automaton.main(Arrays.copyOfRange(args, 1, args.length));
        break;

      /*
       * creates models
       *
       */
      case "MessageCounterHandler":
        MessageCounterHandler.main(Arrays.copyOfRange(args, 1, args.length));
        break;

      case "WordMatrixHandler":
        WordMatrixHandler.main(Arrays.copyOfRange(args, 1, args.length));
        break;

      case "TimeCounterHandler":
        TimeCounterHandler.main(Arrays.copyOfRange(args, 1, args.length));
        break;

      /*
      *
      */
      case "MergeModels":
        LoadModels.main(Arrays.copyOfRange(args, 1, args.length));
        break;

      default:
        LOGGER.info("No argument recognized. To get an overview please use the argument --help.");
    }
  }
}
