package oop.ex6;

public class Variables {


    private String name;
    private String type;

    public boolean isInitialized() {
        return isInitialized;
    }

    public void setInitialized(boolean initialized) {
        isInitialized = initialized;
    }

    private boolean isInitialized;
    private boolean isFinal;

    public Variables(String name, String type, boolean isInitialized,boolean isFinal) {
        this.name = name;
        this.type = type;
        this.isInitialized = isInitialized;
        this.isFinal = isFinal;
    }

    public String getName(){
        return name;
    }

    public boolean getIsFinal() {
        return isFinal;
    }

    public String getType() {
        return type;
    }


}
