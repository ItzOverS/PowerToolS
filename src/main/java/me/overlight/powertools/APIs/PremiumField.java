package me.overlight.powertools.APIs;

public enum PremiumField {
    TRUE("YES"),
    FALSE("NO"),
    NONE("N/A")
    ;

    private String f;
    PremiumField(String v){
        f = v;
    }

    public String get(){
        return f;
    }
}
