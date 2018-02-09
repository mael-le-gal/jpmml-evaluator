package org.jpmml.evaluator;

import org.dmg.pmml.*;

import java.util.Collections;
import java.util.Map;

public class TransformerContext extends EvaluationContext {
    private Map<FieldName, ?> arguments = Collections.emptyMap();

    private Transformer transformer = null;

    TransformerContext(Transformer transformer) {
        setTransformer(transformer);
    }

    private void setTransformer(Transformer transformer) {
        this.transformer = transformer;
    }

    @Override
    protected FieldValue createFieldValue(FieldName name, Object value) {
        throw new UnsupportedOperationException("Not implemented createFieldValue");
    }

    public Map<FieldName, ?> getArguments() {
        return this.arguments;
    }

    public void setArguments(Map<FieldName, ?> arguments) {
        this.arguments = arguments;
    }

    @Override
    protected FieldValue resolve(FieldName name) {
        Transformer transformer = getTransformer();
        DataField dataField = transformer.getDataField(name);
        // Fields that either need not or must not be referenced in the MiningSchema element
        if (dataField == null) {
            DerivedField derivedField = transformer.getDerivedField(name);
            if (derivedField != null) {
                FieldValue value = ExpressionUtil.evaluate(derivedField, this);
                return declare(name, value);
            }
        } else

        // Fields that must be referenced in the DataDictionary element
        {
            Map<FieldName, ?> arguments = getArguments();
            Object value = arguments.get(name);
            if (value == null) {
                throw new MissingValueException(name);
            }
            return declare(name, value);
        }

        throw new MissingFieldException(name);
    }

    private Transformer getTransformer() {
        return transformer;
    }
}
