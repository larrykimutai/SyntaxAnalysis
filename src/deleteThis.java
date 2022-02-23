import javax.swing.*;
import java.io.*;
import java.util.*;

//if anything breaks...it's because of EOF
import static sun.nio.ch.IOStatus.EOF;

public class deleteThis {

  static File in_fp = fileChooser();
  static FileReader fr;

  static {
    try {
      fr = new FileReader(in_fp);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  public static Map<Integer, String> TOKENS;

  static BufferedReader br = new BufferedReader(fr);
  static char nextChar;
  static int c = 0;
  static int charClass = 0;
  static int nextToken;
  static int lexLen;
  static char[] lexeme = new char[100];

  public static final int LETTER = 0;
  public static final int DIGIT = 1;
  public static final int UNKNOWN = 99;

  //public static final int IDENT = 11;
  static {
    TOKENS = new LinkedHashMap<>();
    TOKENS.put(27, "FLOAT_KEYWORD");
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

  //public static Recursive_Descent Syntax = new Recursive_Descent();
  //public static final int INT_LIT = 10;

  //public static final int LEFT_PAREN = 25;
  //public static final int RIGHT_PAREN = 26;
  //public static final int ADD_OP = 21;
  //public static final int SUB_OP = 22;
  //public static final int MULT_OP = 23;
  //public static final int DIV_OP = 24;
  //public static final int FLOAT_KEYWORD = 27;
  //public static final int COMMA = 28;
  //public static final int LEFT_CURLY_BRACE = 29;
  //public static final int RIGHT_CURLY_BRACE = 30;
  //public static final int ASSIGN_OP = 31;
  //public static final int SEMI_COLON = 32;

  public deleteThis() throws FileNotFoundException {
  }

  public static void main(String[] args) throws IOException {
    getChar();
    do{
      lex();
      expr();
    } while(nextToken != EOF);


    System.out.println("done");


  }

  public static void getNonBlank() throws IOException {
    while(Character.isWhitespace(nextChar)) getChar();
  }

  public static void addChar(){
    if(lexLen <= 98){
      lexeme[lexLen] = nextChar;
      lexLen++;
      lexeme[lexLen] = 0;
      lexeme[lexLen + 1] = 0;
      lexeme[lexLen + 2] = 0;
      lexeme[lexLen + 3] = 0;
      lexeme[lexLen + 4] = 0;
      lexeme[lexLen + 5] = 0;
      lexeme[lexLen + 6] = 0;
    }else System.out.println("Error - lexeme is too long \n");
  }

  public static void checkKW(){
  }

//  public static Set<String> getKeys(Integer value) {
//
//    Set<String> result = new HashSet<>();
//    if (TOKENS.containsValue(value)) {
//      for (Map.Entry<String, Integer> entry : TOKENS.entrySet()) {
//        if (Objects.equals(entry.getValue(), value)) {
//          result.add(entry.getKey());
//        }
//        // we can't compare like this, null will throws exception
//              /*(if (entry.getValue().equals(value)) {
//                  result.add(entry.getKey());
//              }*/
//      }
//    }
//    return result;
//
//  }

//  public static int getValue(String key) {
//
//    Set<Integer> result = new HashSet<>();
//    if (TOKENS.containsValue(key)) {
//      for (Map.Entry<String, Integer> entry : TOKENS.entrySet()) {
//        if (Objects.equals(entry.getValue(), key)) {
//          result.add(entry.getValue());
//        }
//        // we can't compare like this, null will throws exception
//              /*(if (entry.getValue().equals(value)) {
//                  result.add(entry.getKey());
//              }*/
//      }
//    }
//    return result;
//
//  }




  public static void lex() throws IOException {
    lexLen = 0;
    getNonBlank();
    switch (charClass){
      case LETTER:
        addChar();
        getChar();
        while(charClass == LETTER || charClass == DIGIT){
          addChar();
          getChar();
        }

        //if(current lexeme == float) set nextToken = getValue("FLOAT_KEYWORD")
        //Object key = TOKENS.keySet().toArray()[27];
        if(String.valueOf(lexeme).trim().equals("float")) nextToken = 27;
        else nextToken = 11;
        break;
      case DIGIT:
        addChar();
        getChar();
        while(charClass == DIGIT){
          addChar();
          getChar();
        }
        nextToken = 10;
        break;
      case UNKNOWN:
        lookup(nextChar);
        getChar();
        break;
      case EOF:
        nextToken = EOF;
        lexeme[0] = 'E';
        lexeme[1] = 'O';
        lexeme[2] = 'F';
        lexeme[3] = 0;
        lexeme[4] = 0;
        lexeme[5] = 0;
        lexeme[6] = 0;
        break;
    }
//I need to get key, given value
    System.out.println("Token is " + TOKENS.get(nextToken) + ", Lexeme is " + String.valueOf(lexeme).trim());
  }

  public static void lookup(char ch){
    switch (ch){
      case '(':
        addChar();
        nextToken = 25;
        break;
      case ')':
        addChar();
        nextToken = 26;
        break;
      case '+':
        addChar();
        nextToken = 21;
        break;
      case '-':
        addChar();
        nextToken = 22;
        break;
      case '*':
        addChar();
        nextToken = 23;
        break;
      case '/':
        addChar();
        nextToken = 24;
        break;
      case '{':
        addChar();
        nextToken = 29;
        break;
      case '}':
        addChar();
        nextToken = 30;
        break;
      case ',':
        addChar();
        nextToken = 28;
        break;
      case '=':
        addChar();
        nextToken = 31;
        break;
      case ';':
        addChar();
        nextToken = 32;
        break;
      default:
        addChar();
        nextToken = EOF;
        break;
    }
  }

  public static void getChar() throws IOException {
    if((c = br.read()) != -1){
      nextChar = (char) c;
      if(Character.isLetter(nextChar))charClass = LETTER;
      else if(Character.isDigit(nextChar)) charClass = DIGIT;
      else charClass = UNKNOWN;
    }
    else charClass = EOF;
  }


  public static File fileChooser(){

    //create new instance of filechooser and set current directory
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

    //show dialog and return chosen file
    int result = fileChooser.showOpenDialog(fileChooser.getParent());
    return fileChooser.getSelectedFile();
  }

  public static void expr() throws IOException {
    System.out.println("Enter <expr> \n");

    term();

    while (deleteThis.nextToken == 21 || deleteThis.nextToken == 22){
      deleteThis.lex();
      term();
    }

    System.out.println("Exit <expr>\n");
  }

  public static void term() throws IOException {
    System.out.println("Enter <term>\n");

    factor();
    while(deleteThis.nextToken == 23 || deleteThis.nextToken == 24){
      deleteThis.lex();
      factor();
    }
    System.out.println("Exit <term>\n");
  }

  public static void factor() throws IOException {
    System.out.println("Enter <factor>\n");

    if(deleteThis.nextToken == 11 || deleteThis.nextToken == 10){  //11 ID          10 Int_Lit
      deleteThis.lex();
    } else{
      if(deleteThis.nextToken == 25){          //25 LEFT PAREN
        deleteThis.lex();
        expr();
        if(deleteThis.nextToken == 26){               //26 RIGHT PAREN
          deleteThis.lex();
        }else {
          System.out.println("Error");
        }
      }else {
        System.out.println("Error");
      }
    }
    System.out.println("Exit <factor>\n");
  }
}



//_________________________________________________________________________________________________________________________________________//
//class Recursive_Descent {


//}
