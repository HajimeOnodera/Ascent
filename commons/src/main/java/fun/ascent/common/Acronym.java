package fun.ascent.common;

public enum Acronym {
    TNT,
    ;

    public static boolean  isAcronym(String string) {
        for (Acronym acronym : values()) {
            if (string.equalsIgnoreCase(acronym.name())) return true;
        }
        return false;
    }
}
