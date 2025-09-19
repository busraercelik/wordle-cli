package b.s.wordle.util;

public final class ArgUtils {
    private ArgUtils(){}

    public static String readArg(String[] args, String argName, String defaultValue) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(argName)) {
                if (i + 1 < args.length) {
                    return args[i + 1];
                }
            }
        }
        return defaultValue;
    }

}
