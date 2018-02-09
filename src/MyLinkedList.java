/**
 * This is my implementation of a LinkedList.
 * 
 * A LinkedList is an implementation of List,
 * This implementation links Objects together
 * as Elements, memorizing their previous and
 * next element, like children forming a
 * perfect circle.
 * 
 * @author Gonzalo Ortiz
 * @version "%I%, %G%"
 * @since 1.8
 */
class MyLinkedList<T> implements MyList {

    private Alkio first;
    private Alkio last;
    private int size;

    /**
     * This is an inner class used in conjuction
     * with MyLinkedList-implementation
     * 
     * It holds information about previous
     * and next Alkio, and which Object
     * it holds.
     * 
     * @author Gonzalo Ortiz
     * @version "%I%, %G%"
     * @since 1.8
     */
    class Alkio<T> {
        Alkio next;
        Alkio previous;
        T element;
    }

    /**
     * Constructor for MyLinkedList
     * 
     * Formats the new MyLinkedList with
     * null values and its size as 0.
     * 
     * @since 1.8
     */
    public MyLinkedList() {
        Alkio first = null;
        Alkio last = null;
        size = 0;
    }


    public Alkio getFirst() {
        return first;
    }

    /**
     * Adds a new object to the LinkedList.
     * 
     * @param e The object to be added to the
     *          List.
     * 
     * @since 1.8
     */
    public void add(Object e) {
        if(first == null) {
            first = new Alkio();
            first.next = null;
            first.previous = null;
            first.element = e;
            size++;
        }
        else if(last == null) {
            last = new Alkio();
            last.next = null;
            last.previous = first;
            last.element = e;
            first.next = last;
            size++;
        }
        else {
            // New Alkio
            Alkio tmp = new Alkio();
            // Giving the Object-parameter as its element
            tmp.element = e;
            // Pointing last-Alkio as tmp's previous Alkio
            tmp.previous = last;
            //Pointing last-Alkio's next as tmp Alkio
            last.next = tmp;
            // We do not know this yet, so it is null.
            tmp.next = null;
            // Making last point to tmp now.
            last = tmp;
            // Increasing the int size +1 to keep track of amount of elements in our linkedlist
            size++;
        }
    }

    /**
     * Clears the List back to null-values.
     * 
     * @since 1.8
     */
    public void clear() {
        first = null;
        last = null;
        size = 0;
    }


    /**
     * Checks whether list is empty.
     * 
     * @return  <code>true</code> if list is empty
     *          otherwise returns <code>false</code>
     */
    public boolean isEmpty() {
        if(size <= 0) {
            return true;
        }
        else {
            return false;
        }
    }
    /**
     * Checks which element is in index,
     * returns the element in the index.
     * 
     * @param index The index where the element
     *              we want is.
     * 
     * @return      The alkio in index.
     * 
     * @since 1.8
     */
    public Alkio get(int index) {
        //Checking whether we should start searching
        // from our last element or first.

        //This has to be one of my high points in life.
        Alkio element = first;

         if(index == size) {
            return last;
        }
        
        if(index == 0) {
            return first;
        }
        /*if(index > size/2) {
            element = last;
           for(int i = 0; i < size-index; i++) {
               element = element.previous;
           }
           return element;
        }*/

       // else {
            for(int i = 0; i < index; i++) {
                element = element.next;
            }
           return element;
       // }
    }

    /**
     * Removes the element at the specified position in this list.
     * Returns the removed element.
     * 
     * @param index the element we want to remove.
     * 
     * @return the element removed.
     */
    public Alkio remove(int index) {
        //Checking whether we should start searching
        // from our last element or first.

        //This has to be one of my high points in life.
        Alkio element = first;

        if(index == size) {
            element = last;
            last = last.previous;
            last.next = null;
            size--;
            return element;
        }
        
        if(index == 0) {
            if(size > 1) {
            first = first.next;
            first.previous = null;
            size--;
            return element;
            }
        else {
            first = null;
            size--;
            return element;
        }

        }

        if(index > size/2) {
            element = last;
           for(int i = 0; i < size-index; i++) {
               element = element.previous;
           }
           element.next.previous =  element.previous;
           element.previous.next = element.next;
           size--;
           return element;
        }

        else {
            for(int i = 0; i < index; i++) {
                element = element.next;
            }
           element.next.previous =  element.previous;
           element.previous.next = element.next;
           size--;
           return element;
        }
    }

    public int size() {
        return size;
    }

    public boolean remove(Object o) {
        Alkio element = first;
        for(int i = 0; i < size; i++) {
            if(element.element.equals(o)) {
                if(i == 0) {
                    first = element.next;
                    first.previous = null;
                    return true;
                }
                else if(i == size-1) {
                    last = element.previous;
                    last.next = null;
                    return true;
                }
                else {
                    element.previous.next = element.next;
                    element.next.previous = element.previous;
                    return true;
                }
            }
            element = element.next;
        }
        return false;
    }
}