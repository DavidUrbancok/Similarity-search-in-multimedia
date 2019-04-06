
import java.io.IOException;
import messif.algorithms.Algorithm;
import messif.objects.LocalAbstractObject;
import messif.objects.util.StreamGenericAbstractObjectIterator;
import messif.operations.QueryOperation;
import messif.operations.query.KNNQueryOperation;
import messif.operations.query.RangeQueryOperation;
import messif.statistics.StatisticCounter;

public class MyAlgorithm extends Algorithm {
    private final String fileName;
    private final Class<? extends LocalAbstractObject> objectClass;
    
    private static final StatisticCounter fileAcssessStat =
            StatisticCounter.getStatistics("FileAccesses");
    
    @AlgorithmConstructor(
        description = "Creates a simple algorithm on a given file",
        arguments = {
            "name of the file with data",
            "class of the object used to read the data"
        }
    )
    public MyAlgorithm(String fileName, Class<? extends LocalAbstractObject> objectClass) {
        super("My Simple Algorithm running on " + fileName);
        this.fileName = fileName;
        this.objectClass = objectClass;
    }
    
    public void search(QueryOperation<?> operation) throws IOException {
        try (StreamGenericAbstractObjectIterator<? extends LocalAbstractObject> iterator = 
                new StreamGenericAbstractObjectIterator<>(objectClass, fileName)) {
            fileAcssessStat.add();
            operation.evaluate(iterator);
        }
        
        operation.endOperation();
    }
    
    public void knnSearch(KNNQueryOperation operation) throws IOException {
        System.out.println("KNN");
        search(operation);
    }
}
