
import java.io.IOException;
import java.util.Iterator;
import messif.algorithms.Algorithm;
import messif.algorithms.AlgorithmMethodException;
import messif.buckets.BucketStorageException;
import messif.objects.impl.ObjectStringEditDist;
import messif.objects.util.RankedAbstractMetaObject;
import messif.objects.util.RankedAbstractObject;
import messif.objects.util.StreamGenericAbstractObjectIterator;
import messif.operations.data.InsertOperation;
import messif.operations.query.ApproxRangeQueryOperation;
import messif.operations.query.RangeQueryOperation;

public class EightMain {
    public static void eightMain(String[] args) throws BucketStorageException, IllegalArgumentException, IOException, AlgorithmMethodException, NoSuchMethodException {
        Algorithm algorithm = new VPTAlgorithm();
        
        StreamGenericAbstractObjectIterator<ObjectStringEditDist> it = new StreamGenericAbstractObjectIterator<>(
                ObjectStringEditDist.class, "text.txt"
        );
        
        for (int i = 0; i < 100; i++) {
            algorithm.executeOperation(new InsertOperation(it.next()));
        }
        
        RangeQueryOperation range = new RangeQueryOperation(it.next(), 3);
        range = algorithm.executeOperation(range);
        
        Iterator<RankedAbstractObject> answer = range.getAnswer();
        while (answer.hasNext()) {
            System.out.println(answer.next());
        }    
    }
}
