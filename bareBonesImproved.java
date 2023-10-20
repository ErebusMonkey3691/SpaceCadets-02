import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;
import java.util.ArrayList;

import java.io.File;
import java.io.FileNotFoundException;

public class bareBonesImproved {

    static HashMap<String, Integer> variables = new HashMap<>();
    static Stack<Integer> whileStack = new Stack<>();

    public static void main(String[] args) {
        // args[0] should be a file name.
        // Check that all arguments are passed and valid.
        if (args.length != 1) {
            System.out.println("Incorrect parameter number passed in. Please only pass in the input file (include the .txt).");
            System.exit(0); // Stop execution.
        } else if (!args[0].contains(".txt")) {
            args[0] += ".txt";
        }
        try {
            File inputFile = new File(args[0]); // Fetch the file passed into the command.
            Scanner myReader = new Scanner(inputFile).useDelimiter("\\s*;\\s*");; // Initialise a reader to read from the given file.
            while (myReader.hasNext()) {
                String data = myReader.next();
                System.out.println(data);
                parseCommand(data, myReader);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occured.");
            e.printStackTrace();
        }
        System.out.println("Variables hashmap: " + variables);
    }

    public static void parseCommand(String commandString, Scanner myReader) {
        String[] commandSegments = commandString.split(" ");
        System.out.println(commandString + "  |  " + variables);
        
        if (commandSegments[0].equals("clear")) {                  // clear [variable];
            variables.put(commandSegments[1],0);            
        } else if (commandSegments[0].equals("incr")) {            // incr [variable];
            if (variables.get(commandSegments[1]) == null) {
                System.out.println("Variable not initialised.");
                System.exit(2);
            } else {
                variables.put(commandSegments[1],variables.get(commandSegments[1])+1);
            }
        } else if (commandSegments[0].equals("decr")) {            // decr [variable];
            if (variables.get(commandSegments[1]) == null) {
                System.out.println("Variable not initialised.");
                System.exit(2);
            } else {
                variables.put(commandSegments[1],variables.get(commandSegments[1])-1);
            }
        } else if (commandSegments[0].equals("while")) {
            whileLoop(commandString, myReader);
        } else {
            System.out.println("Error. Command not recognised.");
            System.exit(1);
        }

    }

    public static void whileLoop(String firstCommand, Scanner myReader) {
        ArrayList<String> commandList = new ArrayList<>(); // Commands in the while statement.
        commandList.add(firstCommand);
        int whileEndCount = 1;
        String currentCommand;
        while (myReader.hasNext() && whileEndCount != 0) {
            currentCommand = myReader.next();
            commandList.add(currentCommand);
            if (currentCommand.contains("while")) {
                whileEndCount++;
            } else if (currentCommand.contains("end")) {
                whileEndCount++;
            }
        }

        for (int i = 0; i < commandList.size(); i++) {
            currentCommand = commandList.get(i);
            if (currentCommand.contains("while")) {                // while stuff
                whileStack.push(i); // Address to return to when end is reached.
            } else if (currentCommand.contains("end")) {           // end stuff
                // compare variable of the while stack's variable to 0.
                // if not 0 then bam loop back
                String variableToCompare = commandList.get(whileStack.peek()).split(" ")[1];
                if (variables.get(variableToCompare) != 0) {
                    i = whileStack.pop() - 1;
                } else {
                    whileStack.pop();
                }
            } else {
                parseCommand(currentCommand, myReader);
            }
        }

    }

}
