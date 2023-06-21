import student.TestCase;

/**
 * @author Ethan Neece
 * @version 11/20/2021
 */
public class ExternalsortTest extends TestCase
{


    /**
     * set up for tests
     */
    public void setUp()
    {
        //nothing to set up.
    }

    /**
     * Get code coverage of the class declaration.
     */
    public void testExternalsortInit()
    {
        Externalsort sorter = new Externalsort();
        assertNotNull(sorter);
        //Externalsort.main(null);
    }

}
