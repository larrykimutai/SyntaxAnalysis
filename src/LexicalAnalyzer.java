import javax.swing.JFileChooser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class LexicalAnalyzer {

  char[] lexeme = new char[50];


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
    //choose input file
    File file = fileChooser();

    //read from file
    ReadFile(file);

    Token test = Token.KEYWORD;
    System.out.println("Lexeme " + test.s + " Token " + test);
  }

  public static File fileChooser(){

    //create new instance of filechooser and set current directory
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

    //show dialog and return chosen file
    int result = fileChooser.showOpenDialog(fileChooser.getParent());
    return fileChooser.getSelectedFile();
  }

  public static void ReadFile(File file) throws Exception {
    Scanner read = new Scanner(file);
    BufferedReader r=new BufferedReader(new FileReader(file));
    int ch;
    int i = 0;
    ArrayList<Character> data = new ArrayList<>();
    while((ch=r.read())!=-1){
      data.add(i, (char)ch);
      i++;

    }


//    for(char t: data){
//      System.out.print(t);
//    }
//
  }
}
