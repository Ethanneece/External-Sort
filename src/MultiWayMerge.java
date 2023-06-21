import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class used to merge multiple runs of Selection Sort together.
 *
 * @author Ethan Neece
 * @version 11/20/2021
 */
public class MultiWayMerge
{

    private final int BLOCK_SIZE = 512;

    private final int RECORD_SIZE = 16;

    private final int MERGE_AMOUNT = 6;
    private ArrayList<Integer> numberOfRuns;
    private ArrayList<Integer> currentPositions;
    private RandomAccessFile reader;
    private RandomAccessFile writer;
    private Record[][] inputs;
    private int[] inputsSize;
    private ByteBuffer outputBuffer;
    private int outputBufferSize;

    /**
     * Creates an object of MultiWayMerge.
     *
     * @param input input File that we are reading from.
     * @param output output File that we are writing to.
     * @param numberOfRuns is the number of runs that was made during
     *                     Selection Sort.
     *
     * @throws FileNotFoundException if input or output do not exist.
     */
    public MultiWayMerge(File input, File output,
                         ArrayList<Integer> numberOfRuns)
            throws FileNotFoundException
    {
        this.reader = new RandomAccessFile(input, "r");
        this.writer = new RandomAccessFile(output, "rw");
        this.numberOfRuns = numberOfRuns;

        inputs = new Record[MERGE_AMOUNT][BLOCK_SIZE];
        inputsSize = new int[MERGE_AMOUNT];

        outputBuffer = ByteBuffer.allocate(BLOCK_SIZE * RECORD_SIZE);
        outputBufferSize = 0;

        currentPositions = new ArrayList<>();
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
                outputBufferSize * RECORD_SIZE));


        outputBufferSize = 0;
        outputBuffer = ByteBuffer.allocate(BLOCK_SIZE * RECORD_SIZE);
    }

    private boolean isOutputFull()
    {
        return outputBufferSize >= BLOCK_SIZE;
    }

    private int readIntoBlock(int block, int start, int end) throws IOException
    {
        byte[] bytes = new byte[RECORD_SIZE * BLOCK_SIZE];

        reader.seek(start);
        int loopAmount = reader.read(bytes) / RECORD_SIZE;

        int i;
        for (i = 0; i < loopAmount && loopAmount * RECORD_SIZE < end; i++)
        {
            inputs[block][i] = new Record(
                    Arrays.copyOfRange(bytes, i * RECORD_SIZE,
                            i * RECORD_SIZE + RECORD_SIZE));
            inputsSize[block]++;
        }
        return i * RECORD_SIZE;
    }

    private Record findNextSmallest() throws IOException
    {
        Record smallest = null;
        int smallestIndex = -1;

        for (int i = 0; i < MERGE_AMOUNT; i++)
        {
            if (inputsSize[i] == 0)
            {
                //Do nothing.
            }
            else if (smallest == null)
            {
                smallest = inputs[i][BLOCK_SIZE - inputsSize[i]];
                smallestIndex = i;
            }
            else if (smallest.compareTo(inputs[i][BLOCK_SIZE - inputsSize[i]]) >
                    0)
            {
                smallest = inputs[i][inputsSize[i]];
                smallestIndex = i;
            }
        }

        if (smallest == null)
        {
            return null;
        }

        inputsSize[smallestIndex]--;

        if (inputsSize[smallestIndex] == 0)
        {
            readIntoBlock(smallestIndex, currentPositions.get(smallestIndex) +
                            numberOfRuns.get(smallestIndex),
                    numberOfRuns.get(smallestIndex + 1));
        }

        return smallest;
    }

    /**
     * Runs MultiWayMerge.
     *
     * @return true if more iterations are needed, false otherwise.
     * @throws IOException if file cannot be read from.
     */
    public boolean run() throws IOException
    {
        int blocks = 0;
        for (int i = 0; i < MERGE_AMOUNT && i < numberOfRuns.size(); i += 2)
        {
            currentPositions.add(readIntoBlock(blocks++, numberOfRuns.get(i),
                    numberOfRuns.get(i + 1)));
        }

        int start = numberOfRuns.get(0);
        int stop = numberOfRuns.get(numberOfRuns.size() - 1);
        if (numberOfRuns.size() > MERGE_AMOUNT)
        {
            stop = numberOfRuns.get(MERGE_AMOUNT - 1);
        }

        while (start < stop)
        {
            Record nextRecord = findNextSmallest();

            addOutputBlock(nextRecord.getCompleteRecord());
            start += 16;
        }

        writeOutputBlock();

        if (numberOfRuns.size() > MERGE_AMOUNT)
        {
            ArrayList<Integer> newRuns = new ArrayList<>();
            newRuns.add(start);
            newRuns.add(stop);

            for (int i = MERGE_AMOUNT; i > numberOfRuns.size(); i++)
            {
                newRuns.add(numberOfRuns.get(i));
            }
            numberOfRuns = newRuns;

            return true;
        }

        return false;
    }
}
