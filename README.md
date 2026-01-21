# Kylang

Kylang is a custom programming language implemented in Java. 
This project implements a complete compilation pipeline including lexical analysis, parsing, and parse tree / AST construction. 
Programs are compiled and executed by providing a source file path via the command line.

## Features

- Custom programming language syntax
- Lexical analysis (tokenization)
- Recursive-descent parsing
- Parse tree / AST representation
- Command-line driven compilation and execution
- Modular compiler-style architecture

## Project Structure

kylang/
├── pom.xml  
├── README.md  
├── src/  
│   └── main/  
│       ├── java/  
│       │   └── kylang/  
│       │       ├── Kylang.java        # Main entry point  
│       │       ├── lexer/             # Lexical analyzer  
│       │       ├── parser/            # Parser and parse tree  
│       │       ├── ast/               # AST node definitions  
│       │       └── statements/        # Statement representations  
│       └── resources/  
│           └── test-inputs/           # Sample Kylang programs

## Entry Point

The main entry point of the program is:

kylang.Kylang

This class reads a source file path from the command line, compiles the program, and executes it.

## Requirements

- Java JDK 17 or later (set `JAVA_HOME`)
- Maven 3.6+ (see install instructions below)
- IntelliJ IDEA (recommended)

## Install Maven (Windows)

1. Download the binary zip from https://maven.apache.org/download.cgi (choose the Binary zip archive).
2. Extract to a folder, e.g. `C:\Program Files\apache-maven-3.x.y`.
3. Set environment variables:
    - `JAVA_HOME` → path to your JDK (e.g. `C:\Program Files\Java\jdk-17`)
    - `M2_HOME` → `C:\Program Files\apache-maven-3.x.y`
    - Add `%M2_HOME%\bin` to `PATH`
4. Open a new Command Prompt and verify:
    - `mvn -v` (should show Maven version and Java home)
    - `java -version` (should show JDK 17)

Alternatively use Chocolatey: `choco install maven` (requires Chocolatey).

## Build

From project root:

- Compile:
    mvn compile

- Run (using the exec plugin):
    mvn exec:java -Dexec.mainClass="kylang.Kylang" -Dexec.args="src/main/resources/test-inputs/V5TestCode.txt"

## How to Run

### Run from IntelliJ IDEA (or other IDE)

1. Open the project in the IDE
2. Open Kylang.java
3. Click Run
4. Set program arguments to a source file, for example:

src/main/resources/test-inputs/V5TestCode.txt

### Run with Maven (Command Line)

Compile the project:

mvn compile

Run the compiler:

mvn exec:java -Dexec.mainClass="kylang.Kylang" -Dexec.args="src/main/resources/test-inputs/V5TestCode.txt"

## Example Usage

java Kylang <source-file>

Example:

java Kylang src/main/resources/test-inputs/CalcCustomFib.txt

## Troubleshooting

- If Maven complains about Java version, ensure `JAVA_HOME` points to JDK 17 and `mvn -v` reports the correct Java.
- Clean and rebuild if class mismatch issues appear:
    mvn clean compile

## Design Overview

Lexer  
Converts raw source code into a stream of tokens.

Parser  
Validates program syntax and builds a parse tree.

ParseTree  
Data structure representing the hierarchical syntax of the program, in
the form of custom ParseTreeNode objects.
Represents the structured form of the program based on grammar rules.

Tree Nodes
Defines various node types for the parse tree, such as expressions,
statements, and program structure.

Statements  
Encapsulate executable language constructs.

Driver (Kylang.java)  
Coordinates the full compilation and execution pipeline.
Accepts source file input, invokes the lexer and parser, and executes the program.

## Future Improvements

- Nested loops and scopes
- Function definitions and calls
- Strings
- Arrays
- Standard library functions
- Improved error reporting and recovery

## License

This project is released under the license specified in the LICENSE file.
