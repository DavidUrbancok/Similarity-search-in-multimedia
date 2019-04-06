
import java.util.Iterator;
import messif.objects.impl.ObjectStringEditDist;
import messif.objects.util.RankedAbstractObject;
import messif.operations.AnswerType;
import messif.operations.query.KNNQueryOperation;
import messif.operations.query.RangeQueryOperation;
import messif.statistics.OperationStatistics;
import messif.statistics.StatisticCounter;
import messif.statistics.Statistics;

public class FifthMain {
    public static void fifthMain(String[] args) throws Exception {
        MyAlgorithm engine = new MyAlgorithm("C:\\Users\\David\\Documents\\SimilaritySearch\\text.txt", ObjectStringEditDist.class);
        
        System.out.println(engine.getSupportedOperations());
        
        RangeQueryOperation operation = new RangeQueryOperation(new ObjectStringEditDist("test"), 2, AnswerType.ORIGINAL_OBJECTS);
        
        
        StatisticCounter dcStat = StatisticCounter.getStatistics("DistanceComputations");
        operation = engine.executeOperation(operation);
        System.out.println(dcStat);
                
        
        OperationStatistics statRegistry = OperationStatistics.getLocalThreadStatistics();
        statRegistry.registerBoundStat("DistanceComputations");
        System.out.println("Operation stats:\n" + statRegistry.printStatistics());
        System.out.println("----------------------------");
        
        System.out.println(Statistics.printStatistics());
        
        Iterator<RankedAbstractObject> answer = operation.getAnswer();
        
        while(answer.hasNext()) {
            System.out.println(answer.next());
        }
    }
}
