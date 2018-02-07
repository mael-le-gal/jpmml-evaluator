package org.jpmml.evaluator;

import java.util.*;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.dmg.pmml.*;

public class Preprocessor {

    private PMML pmml = null;

    private Map<FieldName, DataField> dataFields = Collections.emptyMap();

    private Map<FieldName, DerivedField> derivedFields = Collections.emptyMap();


    public Preprocessor(PMML pmml) {
        setPMML(Objects.requireNonNull(pmml));

        DataDictionary dataDictionary = pmml.getDataDictionary();
        if (dataDictionary == null) {
            throw new MissingElementException(pmml, PMMLElements.PMML_DATADICTIONARY);
        } // End if

        if (dataDictionary.hasDataFields()) {
            this.dataFields = CacheUtil.getValue(dataDictionary, Preprocessor.dataFieldCache);
        }

        TransformationDictionary transformationDictionary = pmml.getTransformationDictionary();
        if (transformationDictionary != null && transformationDictionary.hasDerivedFields()) {
            this.derivedFields = CacheUtil.getValue(transformationDictionary, Preprocessor.derivedFieldCache);
        } // End if
    }

    public DataField getDataField(FieldName name) {
        return this.dataFields.get(name);
    }

    public DerivedField getDerivedField(FieldName name) {
        return this.derivedFields.get(name);
    }

    private void setPMML(PMML pmml) {
        this.pmml = pmml;
    }

    private static final LoadingCache<DataDictionary, Map<FieldName, DataField>> dataFieldCache = CacheUtil.buildLoadingCache(new CacheLoader<DataDictionary, Map<FieldName, DataField>>() {

        @Override
        public Map<FieldName, DataField> load(DataDictionary dataDictionary) {
            return IndexableUtil.buildMap(dataDictionary.getDataFields());
        }
    });

    private static final LoadingCache<TransformationDictionary, Map<FieldName, DerivedField>> derivedFieldCache = CacheUtil.buildLoadingCache(new CacheLoader<TransformationDictionary, Map<FieldName, DerivedField>>() {

        @Override
        public Map<FieldName, DerivedField> load(TransformationDictionary transformationDictionary) {
            return IndexableUtil.buildMap(transformationDictionary.getDerivedFields());
        }
    });

    /**
     * <p>
     * Gets the preprocessing input fields.
     * </p>
     */
    private List<FieldName> getTransformedFields() {
        return new LinkedList<>(this.derivedFields.keySet());
    }

    public List<DataField> getArgumentFields() {
        return new LinkedList<>(this.dataFields.values());
    }

    public Map<FieldName, ?> evaluate(Map<FieldName, ?> arguments) {
        PreprocessorContext context = new PreprocessorContext(this);
        context.setArguments(arguments);

        return evaluate(context);
    }

    public Map<FieldName, ?> evaluate(PreprocessorContext context) {
        Map<FieldName, Object> result = new LinkedHashMap<>();
        List<FieldName> fieldNames = new LinkedList<>(getTransformedFields());
        for (FieldName fieldName : fieldNames) {
            FieldValue value = context.evaluate(fieldName);
            result.put(fieldName, value);
        }
        return result;
    }

    public PMML getPmml() {
        return pmml;
    }
}
