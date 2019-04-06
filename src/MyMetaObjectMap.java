import messif.objects.LocalAbstractObject;
import messif.objects.impl.MetaObjectMap;
import messif.objects.nio.BinaryInput;
import messif.objects.nio.BinarySerializator;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class MyMetaObjectMap extends MetaObjectMap {
    private static final String COLOR_STRUCTURE_TYPE_NAME = "ColorStructureType";
    private static final String SCALABLE_COLOR_TYPE_NAME = "ScalableColorType";
    private static final float COLOR_STRUCTURE_TYPE_COEFFICIENT = 8.333333E-5F;
    private static final float SCALABLE_COLOR_TYPE_COEFFICIENT = 3.3333333E-4F;
    
    public MyMetaObjectMap(String locatorURI, Map<String, LocalAbstractObject> objects, boolean cloneObjects) throws CloneNotSupportedException {
        super(locatorURI, objects, cloneObjects);
    }

    public MyMetaObjectMap(String locatorURI, Map<String, LocalAbstractObject> objects) {
        super(locatorURI, objects);
    }

    public MyMetaObjectMap(BufferedReader stream, Set<String> restrictNames) throws IOException {
        super(stream, restrictNames);
    }

    public MyMetaObjectMap(BufferedReader stream, String[] restrictNames) throws IOException {
        super(stream, restrictNames);
    }

    public MyMetaObjectMap(BufferedReader stream) throws IOException {
        super(stream);
    }

    public MyMetaObjectMap(BinaryInput input, BinarySerializator serializator) throws IOException {
        super(input, serializator);
    }
    
    @Override
    protected float getDistanceImpl(LocalAbstractObject obj, float[] metaDistances, float distThreshold)
    {
        LocalAbstractObject thisColorTypeStructure = getObject(COLOR_STRUCTURE_TYPE_NAME);
        LocalAbstractObject thisScalableColorType = getObject(SCALABLE_COLOR_TYPE_NAME);
        
        LocalAbstractObject otherColorTypeStructure = ((MyMetaObjectMap)obj).getObject(COLOR_STRUCTURE_TYPE_NAME);
        LocalAbstractObject otherScalableColorType = ((MyMetaObjectMap)obj).getObject(SCALABLE_COLOR_TYPE_NAME);
        
        float colorStructureTypeDistance = thisColorTypeStructure.getDistance(otherColorTypeStructure);
        float scalableColorTypeDistance = thisScalableColorType.getDistance(otherScalableColorType);

        return COLOR_STRUCTURE_TYPE_COEFFICIENT * colorStructureTypeDistance + SCALABLE_COLOR_TYPE_COEFFICIENT * scalableColorTypeDistance;
    }
}
