import java.util.Iterator;
import messif.buckets.BucketDispatcher;
import messif.buckets.BucketStorageException;
import messif.buckets.CapacityFullException;
import messif.buckets.LocalBucket;
import messif.buckets.StorageFailureException;
import messif.objects.LocalAbstractObject;
import messif.objects.util.RankedAbstractObject;
import messif.objects.util.RankedSortedCollection;
import messif.operations.query.RangeQueryOperation;
import messif.pivotselection.AbstractPivotChooser;
import messif.pivotselection.RandomPivotChooser;

public class VPTNode {
    // Inner node
    private LocalAbstractObject pivot;
    private float distance;
    private VPTNode leftSubtree;
    private VPTNode rightSubtree;
    
    // Leaf node
    private LocalBucket bucket;
    
    // Leaf node ctor
    public VPTNode(LocalBucket bucket) {
        this.bucket = bucket;
    }
    
    public void insert(LocalAbstractObject object, BucketDispatcher disp) throws BucketStorageException, Throwable {
        if (bucket != null) {
            try {
                bucket.addObject(object);
            } catch (CapacityFullException e) {
                split(disp);
                this.insert(object, disp);
            }
        } else {
            float d = pivot.getDistance(object);
            if (d <= distance) {
                leftSubtree.insert(object, disp);
            } else {
                rightSubtree.insert(object, disp);
            }
        }
    }

    private void split(BucketDispatcher bucketDisp) throws BucketStorageException, Throwable {
        AbstractPivotChooser pivotChooser = new RandomPivotChooser();
        pivotChooser.registerSampleProvider(bucket);
        this.pivot = pivotChooser.getNextPivot();
        
        RankedSortedCollection objs = new RankedSortedCollection(pivot, bucket);
        
        leftSubtree = new VPTNode(bucketDisp.createBucket());
        rightSubtree = new VPTNode(bucketDisp.createBucket());
        
        Iterator<RankedAbstractObject> iterator = objs.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            RankedAbstractObject next = iterator.next();
            LocalAbstractObject obj = (LocalAbstractObject)next.getObject();
            
            if (i < objs.size() / 2) {
                leftSubtree.bucket.addObject(obj);
            } else {
                if (i == objs.size() / 2) {
                    distance = next.getDistance();
                }
                rightSubtree.bucket.addObject(obj);
            }
            i++;
        }
        
        try {
            this.bucket.destroy();
            this.bucket = null;
        } catch (Throwable e) {
            throw new StorageFailureException(e);
        }
    }
    
    public void rangeSearch(RangeQueryOperation op) {
        if (bucket != null) {
            bucket.processQuery(op);
        } else {
            float d = pivot.getDistance(op.getQueryObject());
            
            if (d - op.getRadius() <= distance) {
                leftSubtree.rangeSearch(op);
            }
            if (d + op.getRadius() >= distance) {
                rightSubtree.rangeSearch(op);
            }
        }
    }
}
