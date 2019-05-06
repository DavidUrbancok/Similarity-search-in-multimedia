import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import messif.buckets.BucketDispatcher;
import messif.buckets.BucketErrorCode;
import messif.buckets.BucketStorageException;
import messif.buckets.CapacityFullException;
import messif.buckets.LocalBucket;
import messif.buckets.impl.DiskBlockBucket;
import messif.buckets.impl.MemoryStorageBucket;
import messif.objects.LocalAbstractObject;
import messif.objects.impl.ObjectStringEditDist;
import messif.objects.util.AbstractObjectIterator;

public class SeventhMain {
    public static void seventhMain(String[] args) throws BucketStorageException, IOException, Throwable {
        /*
        LocalBucket bucket = new DiskBlockBucket(500, 8, 0, new File("diskBucket.dbb"));
        
        for (int i = 0; i < 5; i++) {
            LocalAbstractObject object = new ObjectStringEditDist("object " + i);   
            BucketErrorCode errorCode = bucket.addObjectErrCode(object);
            
            System.out.println(errorCode);
        }
        
        System.out.println(bucket.getObjectCount());
        System.out.println(bucket.getOccupation());
        
        AbstractObjectIterator<LocalAbstractObject> iterator = bucket.getAllObjects();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
        
        bucket.finalize();
        */
        
        Map<String, Object> params = new HashMap<>();
        params.put("dir", ".");
        BucketDispatcher dispatcher = new BucketDispatcher(Integer.MAX_VALUE, 30, 8, 0, true, DiskBlockBucket.class, params);
        
        LocalBucket bucket1 = dispatcher.createBucket();
        for (int i = 0; i < 5; i++) {
            LocalAbstractObject object = new ObjectStringEditDist("Obj " + i);   
            BucketErrorCode errorCode = bucket1.addObjectErrCode(object);
            
            if (errorCode.equals(BucketErrorCode.HARDCAPACITY_EXCEEDED)) {
                System.out.println(errorCode);
            }
        }
        
        LocalBucket bucket2 = dispatcher.createBucket();
        for (int i = 0; i < 5; i++) {
            LocalAbstractObject object = new ObjectStringEditDist("Obj " + i);   
            try {
                bucket2.addObject(object);
            } catch (CapacityFullException ex) {
                System.out.println(ex);   
            }
        }
        
        System.out.println(dispatcher.getAllBuckets());
        System.out.println(dispatcher.getObjectCount());
        System.out.println(dispatcher.getOccupation());
        
        dispatcher.finalize();
    }
}
