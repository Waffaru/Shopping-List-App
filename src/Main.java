/**
 * This is the main-class of the program. Everything starts from here.
 * 
 * The program is a shopping list. The user can write
 * a shopping list by using text input.
 * 
 * @author Gonzalo Ortiz
 * @version "%I%, %G%"
 * @since 1.8
 */
import java.util.Scanner;
import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.users.*;
import com.dropbox.core.v2.users.DbxUserUsersRequests;
import com.dropbox.core.DbxRequestConfig;
class Main {
    public static MyLinkedList<Item> tmpList = new MyLinkedList();
    public static MyLinkedList<Item> itemList = new MyLinkedList();
    public static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        
        //Command Line Interface thread
        Thread cli = new Thread(() -> {
            boolean running = true;
            System.out.println("SHOPPING LIST");
            System.out.println("Tampere University of Applied Sciences");

            
            while(running) {
                System.out.println("Give shopping list (example: 1 milk;2 tomato;3 carrot;)");
                String input = scan.nextLine();
                if(input.equals("exit")) {
                    running = false;
                    System.exit(0);
                    break;
                }
                try {
                    String[] inputArray = input.split(";");
                    for(int i = 0; i < inputArray.length; i++) {
                        String[] individualItem = inputArray[i].split(" ");
                        Item tmp = new Item(Integer.parseInt(individualItem[0]), individualItem[1]);
                        //Adds the item if it doesn't exist
                        if(!compareListToItem(tmpList, tmp)) {
                            tmpList.add(tmp);
                        }
                    }
                    itemList = tmpList;
                    GUI.controller.print();
                    printListItems(itemList);

                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
    

        //GUI Thread
        Thread gui = new Thread(() -> {
            Application.launch(GUI.class,args);
        });

        cli.start();
        gui.start();

    }

    /**
     * Prints all items to the CLI
     * 
     * @param list the list we want to print
     * @see MyLinkedList
     * @see Item
     * @since 1.8
     */
    public static void printListItems(MyLinkedList<Item> list) {
        System.out.println("Your Shopping List now:");
        for(int i = 0; i < list.size(); i++) {
        System.out.println("  " + list.get(i).element);
       }
       System.out.println("");
    }


        /**
     * Compares elements on a MyLinkedList to the Item-object.
     * 
     * Compares whether the list already contains item input in parameter
     * if found, it adds the amount of item to the list, then returns true.
     * Otherwise returns false.
     * 
     * @param list      the list we're comparing item to.
     * 
     * @param item      the item we're trying to find from the list.
     * 
     * @return          <code>true</code> if the item exists on the list,
     *                  otherwise returns <code>false</code>
     * @see             Item
     * @see             MyLinkedList
     * @since           1.8
     */

    public static boolean compareListToItem(MyLinkedList list, Item item) {
        for(int i = 0; i < list.size(); i++) {
            if(item.compare(((Item)list.get(i).element))) {
                //TODO: Change this to add-method
                ((Item)list.get(i).element).setAmount(((Item)list.get(i).element).getAmount()+item.getAmount());
                return true;
            }
        }
        return false;
    }
}