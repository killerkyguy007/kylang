// Kyran Day, 12/07/2025.
package kylang;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import kylang.memory.Memory;
import kylang.tree_nodes.StatementList;
import kylang.parser.Parser;
import kylang.parser.ParseTree;

/**
 * Main entry point for the interpreter program.
 *
 * Reads a source text file from the command line argument, parses its contents
 * into a parse tree, and executes the program in a memory environment.
 *
 * @author Kyran Day
 * @version 5.0
 */
public class Kylang {

    public static void main(String[] args) {

        if (args.length == 0) { // verify a command line argument exists
            System.err.println("Error: No file path provided.");
            System.err.println("Usage: java Main <file_path>");
            System.exit(1);
        }

        String filePath = args[0];
        ArrayList<String> programLines = new ArrayList<>();

        try { // Read the source file line by line
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = br.readLine()) != null) programLines.add(line);
        } catch (IOException e) {
            System.err.println("Error reading file \"" + filePath + "\": " + e.getMessage());
            System.exit(1);
        }

        Parser parser = new Parser();
        Memory memory = new Memory(); // create a memory environment for variable storage
        StatementList program = parser.parse(programLines);

        ParseTree tree = new ParseTree(program); // build a parse tree (Program ::= Stmt_List)
        tree.execute(memory); // execute the program in the memory environment
    }
}
