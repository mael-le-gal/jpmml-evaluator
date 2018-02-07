package org.jpmml.evaluator.dev;

import org.dmg.pmml.DataField;
import org.dmg.pmml.FieldName;
import org.dmg.pmml.PMML;
import org.jpmml.evaluator.Example;
import org.jpmml.evaluator.FieldValue;
import org.jpmml.evaluator.FieldValueUtil;
import org.jpmml.evaluator.Preprocessor;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MyTest {
    public static void main(String[] args) {
        try {
//            PMML pmml = Example.readPMML(new File("/home/crannou/pmml-samples/regression.pmml"));
//            RegressionModelEvaluator evaluator = new RegressionModelEvaluator(pmml);
//            List<InputField> inputFields = evaluator.getInputFields();
//            Map<FieldName, FieldValue> arguments = new LinkedHashMap<>();
//            for(InputField inputField : inputFields){
//                FieldName inputFieldName = inputField.getName();
//
//                // The raw (ie. user-supplied) value could be any Java primitive value
//                Object rawValue = 2;
//
//                // The raw value is passed through: 1) outlier treatment, 2) missing value treatment, 3) invalid value treatment and 4) type conversion
//                FieldValue inputFieldValue = inputField.prepare(rawValue);
//
//                arguments.put(inputFieldName, inputFieldValue);
//            }
//            Map<FieldName, ?> results = evaluator.evaluate(arguments);
//            for(FieldName fName : results.keySet()){
//                Double tf = (Double)results.get(fName);
//                System.out.println(fName.getValue()+": "+tf);
//            }

            PMML pmml = Example.readPMML(new File("/home/crannou/pmml-samples/basic.pmml"));
            Preprocessor prep = new Preprocessor(pmml);
            Map<FieldName, FieldValue> arguments = new LinkedHashMap<>();
            List<DataField> fields = prep.getArgumentFields();
            for (DataField field : fields) {
                FieldName fName = field.getName();
                Object rawValue = 4;
                FieldValue fValue = FieldValueUtil.create(field.getDataType(), field.getOpType(), rawValue);
                arguments.put(fName, fValue);
            }
            Map<FieldName, ?> results = prep.evaluate(arguments);
            for (FieldName result : results.keySet()) {
                System.out.println(result.getValue());
                System.out.println(results.get(result));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
