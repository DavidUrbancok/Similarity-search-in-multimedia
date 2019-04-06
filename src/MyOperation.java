
import messif.objects.LocalAbstractObject;
import messif.objects.util.AbstractObjectIterator;
import messif.operations.AbstractOperation;
import messif.operations.RankingQueryOperation;

@AbstractOperation.OperationName("Multi-query object KNN operation")
public class MyOperation extends RankingQueryOperation {
    private LocalAbstractObject[] queryObjects;
    
    @OperationConstructor({"number of objects to retrieve", "list of query objects"})
    public MyOperation(int k, LocalAbstractObject... objects) {
        super(k);
        this.queryObjects = objects.clone();
    }

    @Override
    public int evaluate(AbstractObjectIterator<? extends LocalAbstractObject> objects) {
        int count = 0;
        while (objects.hasNext()) {
            LocalAbstractObject object = objects.next();
            
            float minDist = LocalAbstractObject.MAX_DISTANCE;
            for (LocalAbstractObject queryObject : queryObjects) {
                float dist = queryObject.getDistance(object);
                if (dist < minDist) {
                    minDist = dist;
                }
            }
            addToAnswer(object, minDist, null);
            count++;
        }
        
        return count;
    }

    @Override
    protected boolean dataEqualsImpl(AbstractOperation operation) {
        if (!(operation instanceof MyOperation)) {
            return false;
        }
        
        LocalAbstractObject[] otherObjects = ((MyOperation)operation).queryObjects;
        if (queryObjects.length != otherObjects.length) {
            return false;
        }
        
        for (int i = 0; i < queryObjects.length; i++) {
            if (!queryObjects[i].dataEquals(otherObjects[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int dataHashCode() {
        int hc = 0;
        for (LocalAbstractObject qo : queryObjects) {
            hc += qo.dataHashCode();
        }
        
        return hc;
    }
}
