package main.scanner;

import global.FieldNames;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import parser.MiniJavaScanner;
import parser.MiniJavaScannerConstants;
import parser.ParseException;
import parser.Token;

public class Scanner {
    static String outFile = "Debug.txt";
    static String errFile = "Error.txt";

    @SuppressWarnings("static-access")
    public static void main (String[] args) {

        String filename = args[0];
        FieldNames fn = new FieldNames("parser.MiniJavaScannerConstants");
        MiniJavaScanner parser;
        try {
            parser = new MiniJavaScanner(new FileInputStream(filename));
            //parser.token=parser.getToken(0);
            //parser.Program(); // void Program()
            int errorNumber = 0;
            int tokenNumber = 0;
            while (true) {
                tokenNumber++;
                final Token t = parser.getNextToken();
                if (t.kind == MiniJavaScannerConstants.INVALID) {
                    errorNumber++;
                    System.err.printf(
                            "%s: %03d. %03d: %s -- illegal character %s%n",
                            filename, t.beginLine, t.beginColumn, "ERROR", t);
                } else if (System.getProperty("verbose") != null)
                    writeFile(outFile, String.format(
                            "%s: %03d. %03d: %s \"%s\"%n", filename,
                            t.beginLine, t.beginColumn, fn.get(t.kind), t));

                if (t.kind == MiniJavaScannerConstants.EOF)
                    break;
            }

            System.out.printf("filename=%s, errors=%d//%d%n", filename, errorNumber,tokenNumber);
            
            parser.ReInit(new FileInputStream(filename));
            parser.Program(); // void Program()

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            System.err.printf("%s: %03d. %03d: %s -- Parser Error %s%n",
                    filename, e.currentToken.beginLine,
                    e.currentToken.beginColumn, "ERROR", e.currentToken);
            e.printStackTrace();
        }

        // if (verbose) parser.enable_tracing(); else parser.disable_tracing();

    }

    public static void writeFile (String path, String content) {
        File f = new File(path);
        try {
            if (!f.exists()) {
                if (!f.createNewFile()) {
                    System.out.println("Create Ffile error!");
                }
            }

            BufferedWriter output = new BufferedWriter(new FileWriter(f, true));
            output.write(content);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
