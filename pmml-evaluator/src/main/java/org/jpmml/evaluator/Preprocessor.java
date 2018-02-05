package org.jpmml.evaluator;

import java.util.*;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.dmg.pmml.*;

public class Preprocessor {

    private PMML pmml = null;
    private ValueFactoryFactory valueFactoryFactory = null;

    private ValueFactory<?> valueFactory = null;

    private Map<FieldName, DataField> dataFields = Collections.emptyMap();

    private Map<FieldName, DerivedField> derivedFields = Collections.emptyMap();

    private Map<String, DefineFunction> defineFunctions = Collections.emptyMap();

    protected Preprocessor(PMML pmml) {
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

        if (transformationDictionary != null && transformationDictionary.hasDefineFunctions()) {
            this.defineFunctions = CacheUtil.getValue(transformationDictionary, Preprocessor.defineFunctionCache);
        }
    }

    protected void configure(ModelEvaluatorFactory modelEvaluatorFactory) {
        ValueFactoryFactory valueFactoryFactory = modelEvaluatorFactory.getValueFactoryFactory();

        setValueFactoryFactory(valueFactoryFactory);
    }

    public MathContext getMathContext() {
        return MathContext.DOUBLE;
    }

    public ValueFactory<?> getValueFactory() {

        if (this.valueFactory == null) {
            this.valueFactory = createValueFactory();
        }

        return this.valueFactory;
    }

    protected ValueFactory<?> createValueFactory() {
        ValueFactoryFactory valueFactoryFactory = getValueFactoryFactory();

        if (valueFactoryFactory == null) {
            valueFactoryFactory = ValueFactoryFactory.newInstance();
        }

        MathContext mathContext = getMathContext();

        return valueFactoryFactory.newValueFactory(mathContext);
    }

    public DataField getDataField(FieldName name) {

        if (Objects.equals(Evaluator.DEFAULT_TARGET_NAME, name)) {
            return DEFAULT_TARGET_CONTINUOUS_DOUBLE;
        }

        return this.dataFields.get(name);
    }

    public DerivedField getDerivedField(FieldName name) {
        return this.derivedFields.get(name);
    }

    public DefineFunction getDefineFunction(String name) {
        return this.defineFunctions.get(name);
    }

    protected TypeDefinitionField resolveField(FieldName name) {
        TypeDefinitionField result = getDataField(name);

        if (result == null) {
            result = resolveDerivedField(name);
        }

        return result;
    }

    protected DerivedField resolveDerivedField(FieldName name) {
        DerivedField result = getDerivedField(name);

        return result;
    }

    private void setPMML(PMML pmml) {
        this.pmml = pmml;
    }

    public ValueFactoryFactory getValueFactoryFactory() {
        return this.valueFactoryFactory;
    }

    private void setValueFactoryFactory(ValueFactoryFactory valueFactoryFactory) {
        this.valueFactoryFactory = valueFactoryFactory;
    }

    private static final DataField DEFAULT_TARGET_CONTINUOUS_FLOAT = new DataField(Evaluator.DEFAULT_TARGET_NAME, OpType.CONTINUOUS, DataType.FLOAT);
    private static final DataField DEFAULT_TARGET_CONTINUOUS_DOUBLE = new DataField(Evaluator.DEFAULT_TARGET_NAME, OpType.CONTINUOUS, DataType.DOUBLE);
    private static final DataField DEFAULT_TARGET_CATEGORICAL_STRING = new DataField(Evaluator.DEFAULT_TARGET_NAME, OpType.CATEGORICAL, DataType.STRING);

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

    private static final LoadingCache<TransformationDictionary, Map<String, DefineFunction>> defineFunctionCache = CacheUtil.buildLoadingCache(new CacheLoader<TransformationDictionary, Map<String, DefineFunction>>() {

        @Override
        public Map<String, DefineFunction> load(TransformationDictionary transformationDictionary) {
            return IndexableUtil.buildMap(transformationDictionary.getDefineFunctions());
        }
    });

    /**
     * <p>
     * Gets the preprocessing input fields.
     * </p>
     */
    List<DataField> getArgumentFields(){
        return new LinkedList<>(this.dataFields.values());
    }

//    /**
//     * <p>
//     * Gets the preprocessed fields.
//     * </p>
//     */
//    abstract List<Field> getResultFields();

//    /**
//     * <p>
//     * Evaluates the model with the specified arguments.
//     * </p>
//     *
//     * @param arguments Map of {@link #getArgumentFields() input field} values.
//     * @return Map of {@link #getResultFields() target field} values.
//     * A result field could be mapped to a complex value or a simple value.
//     * Complex values are represented as instances of {@link Computable} that return simple values.
//     * Simple values are represented using the Java equivalents of PMML data types (eg. String, Integer, Float, Double etc.).
//     * A missing value is represented by <code>null</code>.
//     * @throws EvaluationException         If the evaluation fails.
//     * @throws InvalidFeatureException
//     * @throws UnsupportedFeatureException
//     * @see Computable
//     */
    public Map<FieldName, ?> evaluate(Map<FieldName, ?> arguments) {
        PreprocessorContext context = new PreprocessorContext(this);
        context.setArguments(arguments);

        return evaluate(context);
    }

    public Map<FieldName, ?> evaluate(PreprocessorContext context) {
        ValueFactory<?> valueFactory;

        MathContext mathContext = getMathContext();
        switch (mathContext) {
            case FLOAT:
            case DOUBLE:
                valueFactory = getValueFactory();
                break;
            default:
                throw new UnsupportedAttributeException(this.pmml, mathContext);
        }

        return evaluatePreprocessor(valueFactory, context);
    }

    Map<FieldName,?> evaluatePreprocessor(ValueFactory<?> valueFactory, PreprocessorContext context){
        return null;
    }

}
