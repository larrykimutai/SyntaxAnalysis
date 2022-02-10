import javax.swing.JFileChooser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class LexicalAnalyzer {

  //choose input file
  static File file = fileChooser();


  public static List<Character> CURRENTCHAR = new ArrayList<>();
  //static char[] CURRENTCHAR = {};
  public static Stack<String> Lexeme = new Stack<>();
  static int i = 0;


  public enum Token{
    KEYWORD("float"),
    IDENT(""),
    LEFT_PAREN("("),;

    public final String s;

    Token(String s) {
      this.s = s;
    }
  }

  public static void main(String args[])throws Exception{


    //read from file
    getChar();

    Token test = Token.KEYWORD;
    //System.out.println("Lexeme " + test.s + " Token " + test);
  }

  public static File fileChooser(){

    //create new instance of filechooser and set current directory
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

    //show dialog and return chosen file
    int result = fileChooser.showOpenDialog(fileChooser.getParent());
    return fileChooser.getSelectedFile();
  }

  public static void getChar() throws Exception {

    //choose input file

    BufferedReader r = new BufferedReader(new FileReader(file));

    int ch;
    while((ch = r.read()) != -1){
      LexicalAnalyzer((char)ch);
    }


//    ArrayList<Character> data = new ArrayList<>();
//    while((ch=r.read())!=-1){
//      data.add(i, (char)ch);
//      i++;
//
//    }
//    for(char t: data){
//      System.out.print(t);
//    }
//
  }

  public static boolean whiteSpace(char currentChar){
    return Character.isWhitespace(currentChar);
  }

  public static String stringBuilder(){
    StringBuilder build = new StringBuilder(CURRENTCHAR.size());
    for(Character c: CURRENTCHAR){
      build.append(c);
    }
    return build.toString();
  }

  public static void LexicalAnalyzer(char currentChar) throws Exception {


    while(!whiteSpace(currentChar)){
    if(Character.isLetter(currentChar) || Character.isDigit(currentChar)) {
      //whiteSpace(currentChar);
      CURRENTCHAR.add(i, currentChar);
      i++;

    }
      return;
    }

    System.out.println(stringBuilder());
    i = 0;
    CURRENTCHAR.clear();
  }
}
