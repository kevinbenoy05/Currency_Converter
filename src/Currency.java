public class Currency {
    private String id, name;
    public Currency(String id, String name){
        this.id = id;
        this.name = name;
    }
    public String id(){
        return id;
    }
    public String name(){
        return name;
    }
    public String toString(){
        return this.name() + "(" + this.id() + ")";
    }
}
