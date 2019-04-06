import messif.objects.LocalAbstractObject;
import messif.objects.util.StreamGenericAbstractObjectIterator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

public class ThirdMain {
    /**
     * Creates a data file for {@link MyObject} and {@link MyObjectVect}
     * and then reads the data and computes the distances to a given object.
     * @param args no arguments are expected
     * @throws IOException if there was an error writing/reading the data file
     */
    public static void thirdMain(String[] args) throws IOException {
        writeRandomMyMetaObjects("myobject.data", 10);

        readDataGetDistance(new StreamGenericAbstractObjectIterator<>(MyMetaObject.class, "myobject.data"), createRandomMyMetaObject("query"));
    }

    /**
     * Writes {@code count} randomly generated {@link MyMetaObject}s
     * to the given {@code file}.
     * @param file the file into which to write the data
     * @param count number of objects to generate
     * @throws IOException if there was a problem writing to file
     */
    public static void writeRandomMyMetaObjects(String file, int count) throws IOException {
        FileOutputStream out = new FileOutputStream(file);
        for (int i = 1; i <= count; i++) {
            createRandomMyMetaObject(Integer.toString(i)).write(out);
        }
        out.close();
    }

    /**
     * Creates a new {@link MyMetaObject} with random number and vector descriptors.
     * @param locator the object locator to use
     * @return the new {@link MyMetaObject}
     */
    public static MyMetaObject createRandomMyMetaObject(String locator) {
        MyObject obj = new MyObject((float)Math.random());
        MyObjectVect objVect = new MyObjectVect((float)Math.random(), (float)Math.random(), (float)Math.random());
        return new MyMetaObject(locator, obj, objVect);
    }

    /**
     * Read each object from the iterator and compute its distance to the given {@code distObject}.
     * @param iterator the iterator over all objects
     * @param distObj the object against which to get the distance
     */
    public static void readDataGetDistance(Iterator<? extends LocalAbstractObject> iterator, LocalAbstractObject distObj) {
        while (iterator.hasNext()) {
            // Read next object from the iterator
            LocalAbstractObject obj = iterator.next();
            // Write the result to the output
            System.out.println(
                    "Distance of " + obj + " to " + distObj + " = " +
                            distObj.getDistance(obj)
            );
        }
    }
}
