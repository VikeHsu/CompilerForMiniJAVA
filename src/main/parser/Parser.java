package main.parser;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import parser.MiniJavaScanner;
import parser.ParseException;

public class Parser {
    static String outFile = "Debug.txt";
    static String errFile = "Error.txt";
    private static boolean verbose;

    @SuppressWarnings("static-access")
    public static void main (String[] args) {

        String filename = args[0];
        verbose = (System.getProperty("verbose") != null);
        MiniJavaScanner parser;
        int errorNumber = 0;
        PrintStream p = System.out;
        try {
            parser = new MiniJavaScanner(new FileInputStream(filename));
            parser.ReInit(new FileInputStream(filename));
            if (verbose) {
                System.setOut(new PrintStream(new BufferedOutputStream(
                        new FileOutputStream("file.txt")), true));
                parser.enable_tracing();
            } else
                parser.disable_tracing();
            parser.Program(); // void Program()
            if (verbose) {
                System.setOut(p);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            errorNumber++;
            System.err.printf("%s:%03d.%03d:Syntax Error:  expecting :",
                    filename, e.currentToken.beginLine,
                    e.currentToken.beginColumn);
            for (int i = 0; i < e.expectedTokenSequences.length; i++) {
                for (int j = 0; j < e.expectedTokenSequences[i].length; j++) {
                    System.err
                            .printf(e.tokenImage[e.expectedTokenSequences[i][j]]);
                }
                if (e.expectedTokenSequences[i][e.expectedTokenSequences[i].length - 1] != 0) {
                    System.err.printf("...");
                }
                System.err.printf("%n");
            }
        }
        System.out.printf("filename=%s, errors=%d%n", filename, errorNumber);
    }
}
