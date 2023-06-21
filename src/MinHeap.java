/**
 * Class for use of minHeap.
 *
 * @param <T> object being inserted into minHeap.
 *
 * @author Ethan Neece
 * @verison 11/20/2021
 */
public class MinHeap<T extends Comparable<T>>
{
    private T[] Heap;
    private int size;
    private int capacity;
    private int numberOfDeletions;

    /**
     * Creates a minHeap Object.
     *
     * @param capacity total size of minHeap.
     */
    public MinHeap(int capacity)
    {
        this.capacity = capacity;
        this.numberOfDeletions = 0;

        Heap = (T[]) new Comparable[capacity];
        this.size = 0;
    }

    /**
     * Creates a minHeap with capacity 10.
     */
    public MinHeap()
    {
        this(10);
    }

    /**
     * Inserts object into minHeap and then moves it
     *  to the right spot.
     * @param element being inserted.
     * @return true if insertion happens, false otherwise.
     */
    public boolean insertReorder(T element)
    {
        if (size >= capacity)
        {
            return false;
        }

        Heap[size] = element;

        int current = size;
        while (Heap[current].compareTo(Heap[parent(current)]) < 0)
        {
            swapElements(current, parent(current));
            current = parent(current);
        }

        size++;
        return true;
    }

    /**
     * Insert into top of minHeap. sifts down to re-arrange properly.
     * @param element being inserted into minHeap.
     * @return true if insertion happened, false otherwise.
     */
    public boolean insert(T element)
    {
        if (size >= capacity)
        {
            return false;
        }

        Heap[0] = element;

        siftDown(0);

        size++;
        return true;
    }

    /**
     * Inserts object into size of heap then decreases size
     *  for later use.
     * @param element being inserted into minHeap.
     * @return true if inserted, false otherwise.
     */
    public boolean specialInsert(T element)
    {
        if (size >= capacity)
        {
            return false;
        }

        Heap[0] = element;
        swapElements(0, size - 1);
        siftDown(0);
        numberOfDeletions++;
        return true;
    }

    /**
     * Pops the top object off of minHeap.
     * @return T object if it exist, null otherwise.
     */
    public T remove()
    {
        if (size == 0)
        {
            return null;
        }

        size--;
        return Heap[0];
    }

    /**
     * Removes the top of the minHeap, but also reorders it.
     * @return T if the object is removed, null otherwise.
     */
    public T removeReorder()
    {
        if (size == 0)
        {
            return null;
        }

        size--;
        T element = Heap[0];

        swapElements(0, size);
        siftDown(0);
        return element;
    }

    /**
     *
     * @return the size of the minHeap.
     */
    public int size()
    {
        return size;
    }

    private void siftDown(int current)
    {
        while (leftNode(current) < size && current < size && !isLeaf(current) && (Heap[current].compareTo(Heap[leftNode(current)]) > 0 ||
                (rightNode(current) < size && Heap[rightNode(current)] != null &&
                        Heap[current].compareTo(Heap[rightNode(current)]) > 0)))
        {
            if (rightNode(current) >= size || Heap[leftNode(current)].compareTo(Heap[rightNode(current)]) < 0)
            {
                swapElements(current, leftNode(current));
                current = leftNode(current);
            }
            else
            {
                swapElements(current, rightNode(current));
                current = rightNode(current);
            }
        }
    }

    /**
     * Reshuffles everything into the correct position of a minHeap.
     * Makes sure the objects that were saved using specialInsert and
     * inserted back into the minHeap.
     */
    public void minHeap()
    {
        for (int i = 1; i <= numberOfDeletions; i++)
        {
            insertReorder(Heap[capacity - i]);
        }

        numberOfDeletions = 0;

        for (int i = size / 2 - 1; i >= 1; i--)
        {
            siftDown(i);
        }
    }

    private boolean isLeaf(int current)
    {
        return current > size / 2 && current <= size;
    }

    private int parent(int current)
    {
        return (current - 1) / 2;
    }

    private int leftNode(int current)
    {
        return (current * 2) + 1;
    }

    private int rightNode(int current)
    {
        return (current * 2) + 2;
    }

    private void swapElements(int swap1, int swap2)
    {
        T temp = Heap[swap1];

        Heap[swap1] = Heap[swap2];
        Heap[swap2] = temp;
    }

    /**
     *
     * @param index index of object we are looking for.
     * @return T at the index given.
     */
    public T getIndex(int index)
    {
        return Heap[index];
    }

    /**
     *
     * @return number of specialInsert was called.
     */
    public int getNumberOfDeletions()
    {
        return numberOfDeletions;
    }

    /**
     *
     * @return a String representation of minHeap.
     */
    public String toString()
    {
        StringBuilder rtn = new StringBuilder();
        rtn.append("Min Heap Size: " + size + "\n");

        if (size > 0)
        {
            rtn.append("[");
        }

        for (int i = 0; i < size - 1; i++)
        {
            rtn.append(Heap[i] + ", ");
        }

        if (size > 0)
        {
            rtn.append(Heap[size - 1] + "]");
        }

        return rtn.toString();
    }
}
