package com.xiongxin.app;

import com.xiongxin.app.ast.Program;
import com.xiongxin.app.evaluator.Eval;
import com.xiongxin.app.lexer.Lexer;
import com.xiongxin.app.lexer.Token;
import com.xiongxin.app.obj.Obj;
import com.xiongxin.app.parser.Parser;

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

        System.out.print(">> ");
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Lexer lexer = new Lexer(line);
            Parser parser = new Parser(lexer);
            Program program = parser.parseProgram();

            if (parser.getErrors().size() > 0) {
                parser.getErrors().forEach(System.out::println);
                continue;
            }

            Eval eval = new Eval();
            Obj obj = eval.eval(program);
            if (obj == null) {
                System.out.print(">>");
                continue;
            }

            System.out.println(obj.inspect());

            System.out.print(">>");
        }
    }
}
