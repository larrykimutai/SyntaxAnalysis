
//https://www.baeldung.com/java-enum-values

public enum Token {

  FLOAT("float"),
  LPARANTH("("),
  RPARENTH(")"),
  EQUAL_OP("="),
  MULT_OP("*"),
  DIV_OP("/"),
  SEMICOLIN(";"),
  LCBRACKET("{"),
  RCBRACKET("}"),
  COMMA(",");

  public final String tkn;

  private Token(String tkn){
    this.tkn = tkn;
  }

}
