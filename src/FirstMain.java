import messif.algorithms.impl.SequentialScan;
import messif.objects.impl.ObjectStringEditDist;
import messif.objects.util.RankedAbstractObject;
import messif.objects.util.StreamGenericAbstractObjectIterator;
import messif.operations.AnswerType;
import messif.operations.data.BulkInsertOperation;
import messif.operations.query.KNNQueryOperation;

import java.util.Iterator;

// First lecture Main method
public class FirstMain {
    /**
     * Creates simple search algorithm, loads data, prepares and executes query
     * and displays results.
     *
     * @param args no arguments
     * @throws Exception if something goes wrong (i.e. file is not found, etc.)
     */
    public static void firstMain(String[] args) throws Exception {
        SequentialScan engine = new SequentialScan();

        // Read data using String with Edit Distance measure
        Iterator<ObjectStringEditDist> iterator = new
                StreamGenericAbstractObjectIterator<>(
                ObjectStringEditDist.class,
                "C:\\Users\\David\\Documents\\SimilaritySearch\\text.txt"
        );
        // Insert data into engine
        BulkInsertOperation operation = new BulkInsertOperation(iterator);
        engine.executeOperation(operation);

        KNNQueryOperation query = new KNNQueryOperation(
                new ObjectStringEditDist("stran"),  // Object to search
                10,                                 // Number of results
                AnswerType.ORIGINAL_OBJECTS         // Return original objects
        );

        query = engine.executeOperation(query);

        /////////////////// Alternative 1 ///////////////////
//        Iterator<RankedAbstractObject> answer = query.getAnswer();
//        // Iterate over all results in the answer
//        while (answer.hasNext())
//            System.out.println(answer.next());

        /////////////////// Alternative 2 ///////////////////
        Iterator<RankedAbstractObject> answer = query.getAnswer();
        while (answer.hasNext()) {
            RankedAbstractObject result = answer.next();
            // Print result’s distance
            System.out.print(result.getDistance() + ": ");
            // Print result object’s text
            System.out.println(((ObjectStringEditDist)result.getObject()).getStringData()
            );
        }

        /* LECTURE CODE
        SequentialScan engine = new SequentialScan();

        StreamGenericAbstractObjectIterator<ObjectStringEditDist> iterator =
                new StreamGenericAbstractObjectIterator<>(
                        ObjectStringEditDist.class,
                        "C:\\Users\\David\\Documents\\PV229\\text.txt"
                );

        engine.executeOperation(new BulkInsertOperation(iterator));

        KNNQueryOperation op = engine.executeOperation(
                new KNNQueryOperation(
                        new ObjectStringEditDist("there"), 10, AnswerType.ORIGINAL_OBJECTS)
        );

        Iterator<RankedAbstractObject> answer = op.getAnswer();
        while (answer.hasNext()) {
            RankedAbstractObject next = answer.next();
            System.out.println(next.getDistance() + ": " + ((ObjectStringEditDist) next.getObject()).getStringData());
        }*/
    }
}
