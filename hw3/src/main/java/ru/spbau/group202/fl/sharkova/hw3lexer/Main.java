package ru.spbau.group202.fl.sharkova.hw3lexer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Incorrect arguments. Specify a filepath to a file with an arithmetic expression.");
            return;
        }

        File f = new File(args[0]);
        if (!f.exists()) {
            System.out.println("Incorrect arguments. Nonexistent file.");
            return;
        }

        String input;
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(args[0]));
            input = new String(encoded);
            ArrayList<Token> tokens = ArithmeticExpressionLexer.getTokenList(input);
            ArithmeticExpressionParser ar = new ArithmeticExpressionParser(tokens);
            ArrayList<String> result = ar.parse();
            for (String s : result) {
                System.out.println(s);
            }
        } catch (IOException e) {
            System.out.println("Provided file cannot be read; program terminated.");
        } catch (TokenizeException e) {
            System.out.println("Unable to tokenize the expression.");
            System.out.println(e.getLocalizedMessage());
        } catch (ParseException e) {
            System.out.println("Unable to parse the expression.");
            System.out.println(e.getLocalizedMessage());
        }


    }
}
