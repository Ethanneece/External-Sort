import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Class used to run ReplaceSelection.
 *
 * @author Ethan Neece
 * @version 11/20/2021
 */
public class ReplaceSelection
{
    // size of bytes in 1 block 
    private static int SIZE_OF_BLOCK = 512;

    // size of records read 
    private static int SIZE_OF_RECORD_BYTES = 16;

    private static int MIN_HEAP_MAX_SIZE = 512 * 6;

    private byte[] inputBuffer;
    private int inputBufferSize;

    private ByteBuffer outputBuffer;
    private int outputBufferSize;

    private MinHeap<Record> heap;

    private InputStream reader;
    private OutputStream writer;

    /**
     * Creates an object of ReplaceSelection.
     *
     * @param reader that is reading the input file.
     * @param writer that is writing to the output file.
     */
    public ReplaceSelection(InputStream reader, OutputStream writer)
    {
        this.reader = reader;
        this.writer = writer;

        inputBuffer = new byte[SIZE_OF_BLOCK * SIZE_OF_RECORD_BYTES];
        inputBufferSize = 0;

        outputBuffer =
                ByteBuffer.allocate(SIZE_OF_BLOCK * SIZE_OF_RECORD_BYTES);
        outputBufferSize = 0;

        heap = new MinHeap<>(MIN_HEAP_MAX_SIZE);
    }

    private boolean addOutputBlock(byte[] record) throws IOException
    {

        if (!isOutputFull())
        {
            outputBuffer.put(record);
            outputBufferSize++;
            return false;
        }

        writeOutputBlock();
        outputBuffer.put(record);
        outputBufferSize++;
        return true;
    }

    private void writeOutputBlock() throws IOException
    {
        writer.write(Arrays.copyOfRange(outputBuffer.array(), 0,
                outputBufferSize * SIZE_OF_RECORD_BYTES));


        outputBufferSize = 0;
        outputBuffer =
                ByteBuffer.allocate(SIZE_OF_BLOCK * SIZE_OF_RECORD_BYTES);
    }

    private Record getRecordFromInput() throws IOException
    {
        if (inputBufferSize <= 0)
        {
            inputBufferSize = reader.read(inputBuffer) / 16;
        }

        if (inputBufferSize == 0)
        {
            return null;
        }

        Record rtn = new Record(Arrays.copyOfRange(inputBuffer,
                (SIZE_OF_BLOCK - inputBufferSize) * SIZE_OF_RECORD_BYTES,
                (SIZE_OF_BLOCK - inputBufferSize + 1) * SIZE_OF_RECORD_BYTES));

        inputBufferSize--;
        return rtn;
    }

    private boolean isOutputFull()
    {
        return outputBufferSize == SIZE_OF_BLOCK;
    }

    /**
     * Runs Selection Sort.
     * @param records used to track the number of runs that occur.
     * @return true if more runs need to be run, false otherwise.
     * @throws IOException if file reading has a problem.
     */
    public boolean run(int[] records) throws IOException
    {
        records[0] = reader.available();
        Record record = getRecordFromInput();

        heap.minHeap();

        while (record != null)
        {
            if (!heap.insertReorder(record))
            {
                break;
            }
            record = getRecordFromInput();
        }

        heap.minHeap();

        Record lastRecord = null;
        if (heap.size() > 0)
        {
            lastRecord = (heap.remove());
            addOutputBlock(lastRecord.getCompleteRecord());
        }

        int amountOfRecords = 1;
        while (heap.size() > 0 && record != null)
        {
            if (lastRecord != null && record.compareTo(lastRecord) < 0)
            {
                heap.specialInsert(record);
            }
            else
            {
                heap.insert(record);
            }

            lastRecord = heap.remove();
            addOutputBlock(lastRecord.getCompleteRecord());
            record = getRecordFromInput();
            amountOfRecords++;
        }

        while (heap.size() != 0)
        {
            record = heap.removeReorder();
            addOutputBlock(record.getCompleteRecord());
            amountOfRecords++;
        }

        records[1] = reader.available();


        writeOutputBlock();

        return heap.getNumberOfDeletions() != 0;
    }
}
