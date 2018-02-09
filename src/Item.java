/**
 * Item is the class for all the items in
 * a shopping list the user might have.
 * 
 * @author Gonzalo Ortiz
 * @version "%I%, %G%"
 * @since 1.8
 */
public class Item {
    int amount;
    String name;

    /**
     * Constructor for Item-class
     * 
     * Constructor takes an integer and a String
     * and assigns them to the item being created.
     * 
     * @param x      An integer value, the amount of the item
     * 
     * @param s      A String, the name of our item
     * 
     * @since           1.8
     */
    public Item(int x, String s) {
        amount = x;
        name = s;
    }

    /**
     * Adds x to Object's current amount
     *
     * @param x      The amount we want to add to the object
     * 
     * @since           1.8
     */
    public void add(int x) {
        amount = amount + x;
    }

    /**
     * returns the amount of the object
     * 
     * @return the amount-value of the object as an integer.
     * 
     * @since 1.8
     */
    public int getAmount() {
        return amount;
    }
    
    /**
     * sets the amount of the object with x
     * 
     * @param x     the integer-value that we want
     *              amount to be.
     * @since 1.8
     */
    public void setAmount(int x) {
        amount = x;
    }

    /**
     * returns the name of the object
     * 
     * @return the String name of the object.
     * 
     * @since 1.8
     */
    public String getName() {
        return name;
    }
    
    /**
     * Prints the amount and name of the object.
     * 
     * @since 1.8
     */
    @Override
    public String toString() {
        return (amount + " " + name);
    }
    /**
     * Compares two Item-objects' names, returns true if they match
     * else returns false.
     * 
     * @param item   The Item that we want to compare with method caller
     * 
     * @return  <code>true</code> if item names match,
     *          else return <code>false</code>.
     */
    public boolean compare(Item i) {
        if(this.getName().equals(i.getName())){
            return true;
        }
        else {
            return false;
        }
    }
}