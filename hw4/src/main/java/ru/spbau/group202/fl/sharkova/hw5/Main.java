package ru.spbau.group202.fl.sharkova.hw4lexer;

import ru.spbau.group202.fl.sharkova.hw4lexer.lexer.Lexer;
import ru.spbau.group202.fl.sharkova.hw4lexer.lexer.LexerException;
import ru.spbau.group202.fl.sharkova.hw4lexer.parser.ParseException;
import ru.spbau.group202.fl.sharkova.hw4lexer.parser.Parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Incorrect usage. Provide a path to a file with a L language expression.");
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
            //System.out.println(Lexer.getTokens(input));
            Parser parser = new Parser(Lexer.getTokensList(input));
            System.out.println(parser.parse());
        } catch (IOException e) {
            System.out.println("Unable to process file; program terminated");
        } catch (LexerException|ParseException e) {
            System.out.println(e.getMessage());
        }
    }
}
