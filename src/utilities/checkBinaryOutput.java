import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Check to make sure the binary of two files are equal. 
 *  Useful for comparing expected output to output that occured. 
 */
public class checkBinaryOutput
{

    public static void main(String[] args) throws IOException
    {

        File inputFile = new File(args[0]);
        File inputFile2 = new File(args[1]);

        FileInputStream reader = new FileInputStream(inputFile);
        FileInputStream reader2 = new FileInputStream(inputFile2);

        byte[] holder = new byte[reader.available()];

        byte[] holder2 = new byte[reader2.available()];

        if (reader.available() != reader2.available())
        {
            System.out.println("Not Equal");
        }

        if (!Arrays.equals(holder, holder2))
        {
            System.out.println("Not Equal");
        }
    }
}
