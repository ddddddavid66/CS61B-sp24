import edu.princeton.cs.algs4.StdOut;

public class Dessert {
    private int flavor;
    private int price;
    private static int numDesserts = 0;

    public Dessert(int flavor, int price) {
        this.flavor = flavor;
        this.price = price;
        Dessert.numDesserts++;
    }
    public void printDessert(){
        StdOut.println("" + this.flavor + " " + this.price +  " "+ this.price+Dessert.numDesserts);
    }
    public static void main(String[] args){
        StdOut.println("I love dessert!");
    }
}
