package com.xiongxin.app;

import com.xiongxin.app.lexing.Lexer;
import com.xiongxin.app.lexing.Token;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        String username = System.getProperty("user.name");

        System.out.println("Hello " + username + "! This is the Monkey programming language!");
        System.out.println("Feel free to type in command");

        Scanner scanner = new Scanner(System.in);

        System.out.print(">>");
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Lexer lexer = new Lexer(line);
            for (Token token = lexer.nextToken(); !token.type.equals(Token.EOF); token = lexer.nextToken()) {
                System.out.println(token);
            }

            System.out.print(">>");
        }
    }
}
