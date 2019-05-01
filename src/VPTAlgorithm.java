import messif.algorithms.Algorithm;
import messif.buckets.BucketDispatcher;
import messif.buckets.BucketStorageException;
import messif.buckets.impl.MemoryStorageBucket;
import messif.operations.data.InsertOperation;
import messif.operations.query.RangeQueryOperation;


public class VPTAlgorithm extends Algorithm {
    private final BucketDispatcher bucketDisp;
    private final VPTNode root;
    
    public VPTAlgorithm() throws BucketStorageException {
        super("VP Tree");
        this.bucketDisp = new BucketDispatcher(Integer.MAX_VALUE, 5, Integer.MAX_VALUE, 0, false, MemoryStorageBucket.class);
        
        this.root = new VPTNode(bucketDisp.createBucket());
    }
    
    public void insert(InsertOperation op) throws Throwable {
        root.insert(op.getInsertedObject(), bucketDisp);
        op.endOperation();
    }
    
    public void rangeSearch(RangeQueryOperation op) {
        root.rangeSearch(op);
        op.endOperation();
    }
}
