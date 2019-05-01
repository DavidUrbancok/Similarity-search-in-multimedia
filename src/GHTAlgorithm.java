import messif.algorithms.Algorithm;
import messif.buckets.BucketDispatcher;
import messif.buckets.BucketStorageException;
import messif.buckets.impl.MemoryStorageBucket;
import messif.operations.data.InsertOperation;
import messif.operations.query.RangeQueryOperation;

public class GHTAlgorithm extends Algorithm {
    private final BucketDispatcher bucketDispatcher;
    private final GHTNode root;
    
    public GHTAlgorithm() throws BucketStorageException {
        super("GHT");
        
        this.bucketDispatcher = new BucketDispatcher(Integer.MAX_VALUE, 5, Integer.MAX_VALUE, 0, false, MemoryStorageBucket.class);
        this.root = new GHTNode(bucketDispatcher.createBucket());
    }
    
    public void insert(InsertOperation operation) throws Throwable {
        root.insert(operation.getInsertedObject(), bucketDispatcher);
        operation.endOperation();
    }
    
    public void rangeSearch(RangeQueryOperation operation) {
        root.rangeSearch(operation);
        operation.endOperation();
    }
}
