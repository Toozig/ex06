package src;

abstract public class Variables<T> {


    private String name;
    private String type;
    private T data;

    public Variables(String name, String type, T data) {
        this.name = name;
        this.type = type;
        this.data = data;
    }

    public String getName(){
        return name;
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
