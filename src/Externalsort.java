/**
 * Sorting large files that can not be held fully in memory. 
 */

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The class containing the main method.
 *
 * @author Ethan Neece
 * @version 11/20/2021
 */

public class Externalsort
{

    /**
     * @param args Command line parameters
     */
    public static void main(String[] args) throws IOException
    {

        if (args.length != 1)
        {
            System.out.println("Invoke with Externalsort <records-filename>");
        }

        File inputFile = new File(args[0]);
        InputStream fileReader = new FileInputStream(inputFile);


        File outputFile = new File("temp");
        OutputStream writer = new FileOutputStream(outputFile);

        ReplaceSelection replace = new ReplaceSelection(fileReader, writer);

        int totalAmountOfBytes = fileReader.available();

        int[] records = new int[2];
        ArrayList<Integer> numberOfRuns = new ArrayList<>();
        while (replace.run(records))
        {
            numberOfRuns.add(totalAmountOfBytes - records[0]);
            numberOfRuns.add(totalAmountOfBytes - records[1]);
        }

        if (numberOfRuns.size() == 0)
        {
            outputFile.renameTo(new File("output"));

            printOutBlocks(outputFile);
            return;
        }

        fileReader.close();
        writer.close();


        File newInputFile = new File("temp");
        File newOutputFile = new File("output");

        MultiWayMerge merge = new MultiWayMerge(newInputFile, newOutputFile,
                numberOfRuns);

        while (merge.run()) { ; }

        printOutBlocks(outputFile);
    }

    /**
     * @param file reads file from start to finish and outputs it's records as longs. 
     */
    private static void printOutBlocks(File file) throws IOException
    {
        FileInputStream reader = new FileInputStream(file);

        int newLineTracker = 0;
        while (reader.available() > 0)
        {
            byte[] block = new byte[16];

            reader.read(block);

            Record record = new Record(Arrays.copyOfRange(block, 0, 16));
            long recordId = ByteBuffer.wrap(block, 0, 8).getLong();

            if (newLineTracker != 0 && newLineTracker % 5 == 0)
            {
                System.out.println();
            }

            System.out.print(recordId + " " + record.getKey() + " ");

            reader.skip(8192 - 16);
            newLineTracker++;
        }

        reader.close();
    }

}
