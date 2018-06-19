package oop.ex6;

public class Variables<T> {


    private String name;
    private String type;
    private T data;
    private boolean isFinal;

    public Variables(String name, String type, T data,boolean isFinal) {
        this.name = name;
        this.type = type;
        this.data = data;
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


    public T getData() {
        return data;
    }

    public void setData(T data) {

        this.data = data;
    }
}
