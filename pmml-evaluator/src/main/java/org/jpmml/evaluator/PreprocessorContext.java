package org.jpmml.evaluator;

import org.dmg.pmml.*;

import java.util.Collections;
import java.util.Map;

public class PreprocessorContext extends EvaluationContext {
    private Map<FieldName, ?> arguments = Collections.emptyMap();

    private Preprocessor preprocessor = null;

    PreprocessorContext(Preprocessor preprocessor) {
        setPreprocessor(preprocessor);
    }

    private void setPreprocessor(Preprocessor preprocessor) {
        this.preprocessor = preprocessor;
    }

    @Override
    protected FieldValue createFieldValue(FieldName name, Object value) {
        throw new NotImplementedException();
    }

    public Map<FieldName, ?> getArguments() {
        return this.arguments;
    }

    public void setArguments(Map<FieldName, ?> arguments) {
        this.arguments = arguments;
    }

    @Override
    protected FieldValue resolve(FieldName name) {
        Preprocessor preprocessor = getPreprocessor();
        DataField dataField = preprocessor.getDataField(name);
        // Fields that either need not or must not be referenced in the MiningSchema element
        if (dataField == null) {
            DerivedField derivedField = preprocessor.getDerivedField(name);
            if (derivedField != null) {
                FieldValue value = ExpressionUtil.evaluateTypedExpressionContainer(derivedField, this);
                return declare(name, value);
            }
        } else

        // Fields that must be referenced in the DataDictionary element
        {
            Map<FieldName, ?> arguments = getArguments();
            Object value = arguments.get(name);
            return declare(name, value);
        }

        throw new MissingFieldException(name);
    }

    @Override
    public void reset(boolean purge) {
        super.reset(purge);

        this.arguments = Collections.emptyMap();
    }

    private Preprocessor getPreprocessor() {
        return preprocessor;
    }
}
