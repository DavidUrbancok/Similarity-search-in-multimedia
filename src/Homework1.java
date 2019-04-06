import messif.objects.LocalAbstractObject;
import messif.objects.util.RankedAbstractObject;
import messif.objects.util.RankedSortedCollection;
import messif.objects.util.StreamGenericAbstractObjectIterator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.NoSuchElementException;

public class Homework1 {
    private static final int NUMBER_OF_COMPARISONS = 5;
    
    public static void main(String[] args) throws IOException, NoSuchElementException {
        StreamGenericAbstractObjectIterator<MyMetaObjectMap> iterator =
                new StreamGenericAbstractObjectIterator<>(MyMetaObjectMap.class, "images.data");

        LocalAbstractObject query = new MyMetaObjectMap(new BufferedReader(new FileReader("query.data")));

        RankedSortedCollection collection = new RankedSortedCollection();


        while (iterator.hasNext()) {
            LocalAbstractObject localAbstractObject = iterator.next();
            RankedAbstractObject rankedAbstractObject = new RankedAbstractObject(localAbstractObject, localAbstractObject.getDistance(query));
            collection.add(rankedAbstractObject);
        }

        for (int i = 0; i < NUMBER_OF_COMPARISONS; i++) {
            RankedAbstractObject rankedObj = collection.removeFirst();
            System.out.println(rankedObj.getObject().getLocatorURI() + " " + rankedObj.getDistance());
        }
    }
}
