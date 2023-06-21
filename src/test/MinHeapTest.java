import student.TestCase;

/**
 * Class for testing MinHeap.
 *
 * @author Ethan Neece
 * @version 11/20/2021
 */
public class MinHeapTest extends TestCase
{
    private MinHeap<Integer> empty;
    private MinHeap<Integer> small;
    private MinHeap<Integer> medium;
    private MinHeap<Integer> big;
    private MinHeap<Integer> full;

    /**
     * Sets up min heaps for testing.
     */
    public void setUp()
    {
        empty = new MinHeap<>();

        small = new MinHeap<>(15);
        for (int i = 0; i < 10; i++)
        {
            small.insertReorder(i);
        }

        medium = new MinHeap<>(150);
        for (int i = 0; i < 100; i++)
        {
            medium.insertReorder(i);
        }

        big = new MinHeap<>(1500);
        for (int i = 0; i < 1000; i++)
        {
            big.insertReorder(i);
        }

        full = new MinHeap<>();
        for (int i = 0; i < 10; i++)
        {
            full.insertReorder(i);
        }

    }

    /**
     * Test the insert() methods in minHeap.
     */
    public void testInsert()
    {
        assertTrue(empty.insertReorder(5));
        assertEquals(1, empty.size());
        assertEquals(5, empty.getIndex(0).intValue());

        assertTrue(empty.insertReorder(4));
        assertEquals(2, empty.size());
        assertEquals(4, empty.getIndex(0).intValue());

        assertTrue(small.insertReorder(10));
        assertEquals(10, small.getIndex(10).intValue());

        assertTrue(small.insertReorder(15));
        assertEquals(15, small.getIndex(11).intValue());

        assertTrue(small.insertReorder(13));
        assertEquals(13, small.getIndex(12).intValue());

        assertFalse(full.insert(1));
    }

    /**
     * Test the remove() methods in minHeap.
     */
    public void testRemove()
    {
        assertNull(empty.remove());

        assertEquals(0, small.remove().intValue());

        assertEquals(0, medium.remove().intValue());
        assertEquals(0, full.remove().intValue());
        assertEquals(0, big.remove().intValue());
    }

    /**
     * test the size() method in minHeap.
     */
    public void testSize()
    {
        assertEquals(0, empty.size());
    }

    /**
     * test the minHeap() method in minHeap.
     */
    public void testMinHeap()
    {
        small.minHeap();
        assertEquals("Min Heap Size: 10\n" +
                "[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]", small.toString());
    }

    /**
     * test the toString() method in minHeap.
     */
    public void testToString()
    {
        assertEquals("Min Heap Size: 0\n", empty.toString());

        empty.insert(1);

        assertEquals("Min Heap Size: 1\n" +
                "[1]", empty.toString());

        assertEquals("Min Heap Size: 10\n" +
                "[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]", small.toString());
    }
}
