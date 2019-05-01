import messif.buckets.BucketDispatcher;
import messif.buckets.BucketStorageException;
import messif.buckets.CapacityFullException;
import messif.buckets.LocalBucket;
import messif.buckets.StorageFailureException;
import messif.objects.LocalAbstractObject;
import messif.objects.util.AbstractObjectIterator;
import messif.operations.query.RangeQueryOperation;
import messif.pivotselection.AbstractPivotChooser;
import messif.pivotselection.RandomPivotChooser;

public class GHTNode {
    // Inner node
    private LocalAbstractObject pivot1;
    private LocalAbstractObject pivot2;
    private GHTNode leftSubtree;
    private GHTNode rightSubtree;
    
    // Leaf node
    private LocalBucket bucket;
    
    // Leaf node constructor
    public GHTNode(LocalBucket bucket) {
        this.bucket = bucket;
    }

    public void insert(LocalAbstractObject object, BucketDispatcher bucketDispatcher) throws BucketStorageException, Throwable {
        if (bucket != null) {
            try {
                bucket.addObject(object);
            } catch (CapacityFullException e) {
                split(bucketDispatcher);
                this.insert(object, bucketDispatcher);
            }
        } else {
            float distanceToPivot1 = object.getDistance(pivot1);
            float distanceToPivot2 = object.getDistance(pivot2);
            
            if (distanceToPivot1 <= distanceToPivot2) {
                leftSubtree.insert(object, bucketDispatcher);
            } else {
                rightSubtree.insert(object, bucketDispatcher);
            }
        }
    }
    
    private void split(BucketDispatcher bucketDispatcher) throws BucketStorageException, Throwable {
        AbstractPivotChooser pivotChooser = new RandomPivotChooser();
        pivotChooser.registerSampleProvider(bucket);
        
        this.pivot1 = pivotChooser.getNextPivot();
        this.pivot2 = pivotChooser.getNextPivot();
        
        leftSubtree = new GHTNode(bucketDispatcher.createBucket());
        rightSubtree = new GHTNode(bucketDispatcher.createBucket());
        
        AbstractObjectIterator<LocalAbstractObject> iterator = bucket.getAllObjects();
        
        while (iterator.hasNext()) {
            LocalAbstractObject object = iterator.next();
            
            if (object.getDistance(pivot1) <= object.getDistance(pivot2)) {
                leftSubtree.bucket.addObject(object);
            } else {
                rightSubtree.bucket.addObject(object);
            }
        }
        
        try {
            this.bucket.destroy();
            this.bucket = null;
        } catch (Throwable e) {
            throw new StorageFailureException(e);
        }
    }
    
    /**
     * 
     * @param operation 
     */
    public void rangeSearch(RangeQueryOperation operation) {
        if (bucket != null) {
            bucket.processQuery(operation);
        } else {
            LocalAbstractObject queryObject = operation.getQueryObject();
            float distanceToPivot1 = pivot1.getDistance(queryObject);
            float distanceToPivot2 = pivot2.getDistance(queryObject);
            float radius = operation.getRadius();
            
            if (distanceToPivot1 - radius <= distanceToPivot2 + radius) {
                leftSubtree.rangeSearch(operation);
            }
            if (distanceToPivot1 + radius > distanceToPivot2 - radius) {
                rightSubtree.rangeSearch(operation);
            }
        }
    }
}
