package org.jpmml.evaluator;

import org.dmg.pmml.*;

import java.util.Collections;
import java.util.Map;

public class PreprocessorContext extends EvaluationContext {
    private Map<FieldName, ?> arguments = Collections.emptyMap();

    private Preprocessor preprocessor = null;

    public PreprocessorContext(Preprocessor preprocessor) {
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

        DerivedField derivedField = preprocessor.getDerivedField(name);
        if (derivedField != null) {
            FieldValue value = ExpressionUtil.evaluateDerivedField(derivedField, this);
            return declare(name, value);
        }

        throw new MissingFieldException(name);
    }

    @Override
    public void reset(boolean purge) {
        super.reset(purge);

        this.arguments = Collections.emptyMap();
    }

    public Preprocessor getPreprocessor() {
        return preprocessor;
    }
}
