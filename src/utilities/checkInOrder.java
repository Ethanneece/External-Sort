import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Check that the records are in order when output 
 *  by the sort. 
 */
public class checkInOrder
{
    public static void main(String[] args) throws IOException
    {
        File inputFile = new File(args[0]);
        FileInputStream reader = new FileInputStream(inputFile);

        byte[] bytes = new byte[reader.available()];

        reader.read(bytes);
        int counter = 0;
        for (int i = 0; i < bytes.length / 16; i += 2)
        {
            Record record = new Record(Arrays.copyOfRange(bytes, i * 16,
                    i * 16 + 16));

            Record record2 = new Record(Arrays.copyOfRange(bytes, (i + 1) * 16,
                    (i + 1) * 16 + 16));

            if (record.compareTo(record2) > 0)
            {
                System.out.println("Not Equal");
                System.out.println(i * 16);
                System.out.println(record);
                System.out.println(record2);
                counter++;
            }
        }

        System.out.println(counter);
    }
}
