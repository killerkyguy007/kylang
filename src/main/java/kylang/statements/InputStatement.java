// Kyran Day, 12/07/2025
package kylang.statements;
import java.util.Scanner;
import kylang.memory.Memory;

/**
 * The InputStatement class represents an executable statement that prompts the user
 * for input and stores the input value into memory under a specified variable name.
 * It extends the abstract Statement class by providing an implementation for
 * the execute method.
 */
public class InputStatement extends Statement {

    private String id;
    private static final Scanner in = new Scanner(System.in);

    public InputStatement(String id) {
        this.id = id;
    }

    public void execute(Memory memory) {
        System.out.print("Enter value for " + id + ": ");
        String inputStr = in.nextLine().trim();
        try {
            int value = Integer.parseInt(inputStr);
            memory.put(id, value);
        } catch (NumberFormatException e) {
            System.err.println("Invalid integer input. Program terminated.");
            System.exit(1);
        }
    }
}
