import javax.swing.*;
import java.io.*;
import java.util.*;
import static sun.nio.ch.IOStatus.EOF;


public class LexicalAnalyzer {

  //open file and process content
  static File in_fp = fileChooser();
  static FileReader fr;
  static {
    try {
      fr = new FileReader(in_fp);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }
  static BufferedReader br = new BufferedReader(fr);



  //Global Static Variables
  static char nextChar;
  static int c, charClass, nextToken, lexLen;
  static char[] lexeme = new char[100];

  //create map with integer key and Token for value
  static Map<Integer, String> TOKENS;

  //Token mapping
  static {
    TOKENS = new LinkedHashMap<>();
    TOKENS.put(0, "LETTER");
    TOKENS.put(1, "DIGIT");
    TOKENS.put(99, "UNKNOWN");
    TOKENS.put(27, "KEYWORD");
    TOKENS.put(10, "INT_LIT");
    TOKENS.put(25, "LEFT_PAREN");
    TOKENS.put(26, "RIGHT_PAREN");
    TOKENS.put(21, "ADD_OP");
    TOKENS.put(22, "SUB_OP");
    TOKENS.put(23, "MULT_OP");
    TOKENS.put(24, "DIV_OP");
    TOKENS.put(28, "COMMA");
    TOKENS.put(30, "RIGHT_CURLY_BRACE");
    TOKENS.put(31, "ASSIGN_OP");
    TOKENS.put(32, "SEMI_COLON");
    TOKENS.put(29, "LEFT_CURLY_BRACE");
    TOKENS.put(11, "IDENTIFIER");
  }

  //main driver
  public static void main(String[] args) throws IOException {
    getChar();
    do{
      lex();
      program();
    } while(nextToken != EOF);
    System.out.println("done");
    JOptionPane.showMessageDialog(null, "The Test Source Code is correct");
  }

  //a function to skip white spaces by calling getChar() until no white space is found
  public static void getNonBlank() throws IOException {
    while(Character.isWhitespace(nextChar)) getChar();
  }

  //adds char(nextChar) to lexeme array
  public static void addChar(){
    if(lexLen <= 98){
      lexeme[lexLen] = nextChar;
      lexLen++;

      //used to clear up to 7 elements of lexeme array to avoid carrying over previous lexeme to current lexeme
      lexeme[lexLen] = 0;
      lexeme[lexLen + 1] = 0;
      lexeme[lexLen + 2] = 0;
      lexeme[lexLen + 3] = 0;
      lexeme[lexLen + 4] = 0;
      lexeme[lexLen + 5] = 0;
      lexeme[lexLen + 6] = 0;
    }else System.out.println("Error - lexeme is too long \n");
  }


  //lexical analyzer
  public static void lex() throws IOException {
    lexLen = 0;
    getNonBlank();
    switch (charClass) {
      //parse words
      case 0 -> { // LETTER
        addChar();
        getChar();
        while (charClass == 0 || charClass == 1) {
          addChar();
          getChar();
        }

        //parse "float" keywords
        if (String.valueOf(lexeme).trim().equals("float")) nextToken = 27;
        else nextToken = 11;
      }
      //parse literals
      case 1 -> { //DIGIT
        addChar();
        getChar();
        while (charClass == 1) {
          addChar();
          getChar();
        }
        nextToken = 10;
      }

      //parse other non-digit non-letter characters
      case 99 -> { //unknown
        lookup(nextChar);
        getChar();
      }

      //end of file
      case EOF -> {
        nextToken = EOF;
        lexeme[0] = 'E';
        lexeme[1] = 'O';
        lexeme[2] = 'F';
        lexeme[3] = 0;
        lexeme[4] = 0;
        lexeme[5] = 0;
        lexeme[6] = 0;
      }
    }
    System.out.println("Token is " + TOKENS.get(nextToken) + ", Lexeme is " + String.valueOf(lexeme).trim());
  }

  //function to look up non-digit non-letter char and set token
  public static void lookup(char ch){
    switch (ch) {
      case '(' -> {
        addChar();
        nextToken = 25;
      }
      case ')' -> {
        addChar();
        nextToken = 26;
      }
      case '+' -> {
        addChar();
        nextToken = 21;
      }
      case '-' -> {
        addChar();
        nextToken = 22;
      }
      case '*' -> {
        addChar();
        nextToken = 23;
      }
      case '/' -> {
        addChar();
        nextToken = 24;
      }
      case '{' -> {
        addChar();
        nextToken = 29;
      }
      case '}' -> {
        addChar();
        nextToken = 30;
      }
      case ',' -> {
        addChar();
        nextToken = 28;
      }
      case '=' -> {
        addChar();
        nextToken = 31;
      }
      case ';' -> {
        addChar();
        nextToken = 32;
      }
      default -> {
        addChar();
        nextToken = EOF;
      }
    }
  }

  //gets the next character in file while !EOF
  public static void getChar() throws IOException {
    if((c = br.read()) != -1){
      nextChar = (char) c;
      if(Character.isLetter(nextChar))charClass = 0;
      else if(Character.isDigit(nextChar)) charClass = 1;
      else charClass = 99;
    }
    else charClass = EOF;
  }


  //opens a user-friendly file chooser window to allow ease of selecting file
  public static File fileChooser(){

    //create new instance of file-chooser and set current directory
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

    //show dialog and return chosen file
    fileChooser.showOpenDialog(fileChooser.getParent());
    return fileChooser.getSelectedFile();
  }


  //Parse in language generated by: <program> -> <keyword><ident>(){<declares><stmt>}
  public static void program() throws IOException {
    System.out.println("Enter <program>");

    //parse keyword and ident, if not keyword or indet return error respectively
    if(nextToken != 27) error(27);
    keyword();
    if(nextToken != 11) error(11);
    ident();

    //parse left paren, and error if following token is not right paren
    if(nextToken != 25) error(25);
    if(nextToken == 25){ //(
      lex();
      if(nextToken != 26) error(26);
      if(nextToken == 26){//)
        lex();
      }

      //parse left curly brace, error if token is not left curley brace
      if(nextToken != 29) error(29);  //{
    }if(nextToken == 29){
      lex();
    }

    //parse declares and stmts, if not declares, stmts, return error respectively
    if(nextToken != 27) error(27);
    declares();
    if(nextToken != 11) error(11);
    stmts();

    //parse right carly brace
    if(nextToken == 30) lex();  //}

    System.out.println("Exit <program>");
  }

  //Parse in language generated by: <stmts> -> <assign>;<stmts>
  //                                         | <assign>;
  public static void stmts() throws IOException {
    System.out.println("Enter <stmts>");

    assign();

    if(nextToken == 32) lex(); // ;

    System.out.println("Exit <stmts>");
  }

  //Parse in language generated by: <assign> -> <ident>=<expr>
  public static void assign() throws IOException {
    System.out.println("Enter <assign>");

    ident();

    if(nextToken == 31) lex(); // =

    expr();

    System.out.println("Exit assign");
  }

  //Parse in language generated by: <expr> -> <ident> {*|/} <expr>
  //                                        | <ident>
  public static void expr() throws IOException {
    System.out.println("Enter <expr>");

    ident();

    if(nextToken == 23) lex(); //*
    if(nextToken == 24) lex(); // div
    if(nextToken != 32)expr();

    System.out.println("Exit <expr>");
  }


  //Parse in language generated by: <declares> -> <keyword><ident>;
  //                                            | <keyword><ident>;<declares>
  public static void declares() throws IOException{
    System.out.println("Enter <declares>");
    keyword();
    ident();
    if(nextToken == 32) lex();
    if(nextToken == 27) declares();
    System.out.println("Exit <declares>");
  }

  //Parse in language generated by: <ident> -> a<ident>
  //                                         | b<ident> ...
  //                                         | z<ident>
  //                                         | e
  public static void ident() throws IOException {
    System.out.println("Enter <ident>");
    if(nextToken == 11){
      lex();
    }
    System.out.println("Exit <ident>");
  }

  ////Parse in language generated by: <keyword> -> float
  public static void keyword() throws IOException {
    System.out.println("Enter <keyword>");
    if(nextToken == 27){ // keyword
      lex();
    }
    System.out.println("Exit <keyword>");
  }

  //error method to display syntax error and diagnostics
  public static void error(int error){
    JOptionPane.showMessageDialog(null, "The Test Source Code " +
            "cannot be generated by the Sample BNF Defined Language");
    JOptionPane.showMessageDialog(null, "Check console for diagnostic and recovery options");

    System.out.println("Per BNF Grammar Rule: Token " + TOKENS.get(error) + " is required.");
    System.exit(0);
  }
}


