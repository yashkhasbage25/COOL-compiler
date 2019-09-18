package java.cool;

public class CoolUtils {
  public static final String OBJECT_TYEP_STR = "Object";
  public static final String IO_TYPE_STR = "IO";
  public static final String INT_TYPE_STR = "Int";
  public static final String BOOL_TYPE_STR = "Bool";
  public static final String STRING_TYEP_STR = "String";
  public static final String MAIN_TYPE = "Main";

  public static String filename;

  public static InheritanceGraph inheritanceGraph = new InheritanceGraph();

    public void reportError(String filename, int lineNo, String error){
  		System.err.println(filename+":"+lineNo+": "+error);
  }
}
