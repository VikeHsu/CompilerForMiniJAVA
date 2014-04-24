package global;

public class Verboser {
    private final static String DEC_PREFIX = "DEF:  declaration of ";
    private final static String USE_PREFIX = "USE:  ";
    private final static String ERR_PREFIX = "ERR:  ";
    public static int errCount = 0;
    public static boolean verbose = false;

    public static void ePrint (String str) {
        System.err.println(str);
    }

    public static void print (String str) {
        if (verbose)
            System.out.println(str);
    }

    public static void DecClass (boolean main, String name) {
        String out = DEC_PREFIX;
        if (main) {
            out += "(main) ";
        }
        out += "class '" + name + "'.";
        print(out);
    }

    public static void DecClass (boolean main, String name, String extendName) {
        String out = DEC_PREFIX;
        if (main) {
            out += "(main) ";
        }
        out += "class '" + name + "' extends '" + extendName + "'.";
        print(out);
    }

    public static void DecMethod (String name, String className) {
        String out = DEC_PREFIX + "method '" + name + "' in class '"
                + className + "'.";
        print(out);
    }

    public static void DecFormal (String name, String className,
            String methodName, String typeName) {
        String out = DEC_PREFIX + "formal '" + name + "' in method '"
                + className + "." + methodName + "'; type=" + typeName + ".";
        print(out);
    }

    public static void DecLocal (String name, String className,
            String methodName) {
        String out = DEC_PREFIX + "local '" + name + "' in method '"
                + className + "." + methodName + "'.";
        print(out);
    }

    public static void DecField (String name, String className) {
        String out = DEC_PREFIX + "local '" + name + "' in class '" + className
                + "'.";
        print(out);
    }

    public static void UseNew (String name) {
        String out = USE_PREFIX + "instantiate class '" + name + "'.";
        print(out);
    }

    public static void UseCall (String name, String className, String type) {
        String out = USE_PREFIX + "call method '" + className + "." + name
                + "'. Ret type=" + type + ".";
        print(out);
    }

    public static void UseThis (String className, String methodName) {
        String out = USE_PREFIX + "pseudo variable 'this' in method '"
                + className + "." + methodName + "'.  Type=" + className;
        print(out);
    }

    public static void UseVar (String varInfo) {
        String out = USE_PREFIX + varInfo;
        print(out);
    }

    public static void DefLoop (String class1, String class2) {
        errCount++;
        String out = ERR_PREFIX + "define class '" + class1
                + "' with loop extends: '" + class2 + "'.";
        ePrint(out);
    }
    public static void Err(){
        errCount++;
    }

}
