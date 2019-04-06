
import java.util.Iterator;
import messif.algorithms.Algorithm;
import messif.algorithms.AlgorithmMethodException;
import messif.algorithms.impl.SequentialScan;
import messif.objects.impl.ObjectStringEditDist;
import messif.objects.util.AggregationFunction;
import messif.objects.util.RankedAbstractObject;
import messif.operations.RankingQueryOperation;
import messif.operations.data.InsertOperation;
import messif.operations.query.AggregationFunctionQueryOperation;
import messif.operations.query.KNNQueryOperation;

public class FourthMain {

    public static void fourthMain(String args[]) throws Exception {
        Algorithm engine = new SequentialScan();
        
        InsertOperation op = new InsertOperation(createRandom("sample"));
    
        op = engine.executeOperation(op);
        
        if (op.wasSuccessful()) {
            System.out.println("Object inserted");
        } else {
            System.out.println(op.getErrorCode());
        }
        
        MyMetaObject query = createRandom("query");
        
        RankingQueryOperation qop = new AggregationFunctionQueryOperation(
                query,
                5,
                AggregationFunction.valueOf("number + vector")
        );
        executeOperation(qop, engine);
        
        MyOperation mop = new MyOperation(5, query);
        executeOperation(mop, engine);
    }

    private static void executeOperation(RankingQueryOperation qop, Algorithm engine) throws AlgorithmMethodException, NoSuchMethodException {
        qop = engine.executeOperation(qop);
        
        Iterator<RankedAbstractObject> answer = qop.getAnswer();
        
        while (answer.hasNext()) {
            RankedAbstractObject next = answer.next();
            System.out.println("Retrieved " + next.getObject().getLocatorURI() +
                    " with distance " + next.getDistance());
        }
    }
    
    public static MyMetaObject createRandom(String locator) {
        return new MyMetaObject(
                locator,
                new MyObject(locator, (float)Math.random()),
                new MyObjectVect(locator, (float)Math.random(), (float)Math.random())
        );
    }
}
