package org.jpmml.evaluator.dev;

import org.dmg.pmml.*;
import org.jpmml.evaluator.Example;
import org.jpmml.evaluator.FieldValue;
import org.jpmml.evaluator.FieldValueUtil;
import org.jpmml.evaluator.Transformer;

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

//            PMML pmml = Example.readPMML(new File("/home/crannou/pmml-samples/map.pmml"));
//            Transformer prep = new Transformer(pmml);
//            Map<FieldName, FieldValue> arguments = new LinkedHashMap<>();
//            arguments.put(
//                    FieldName.create("WHOIS_DOMAINE"),
//                    FieldValueUtil.create(DataType.STRING, OpType.CATEGORICAL, "US")
//            );
//            arguments.put(
//                    FieldName.create("WINDOW_BILLING"),
//                    FieldValueUtil.create(DataType.DOUBLE, OpType.CONTINUOUS, 3)
//            );
//            arguments.put(
//                    FieldName.create("TOTAL_REVENUE"),
//                    FieldValueUtil.create(DataType.DOUBLE, OpType.CONTINUOUS, 37)
//            );
            PMML pmml = Example.readPMML(new File("/home/crannou/pmml-samples/less-basic.pmml"));
            Transformer prep = new Transformer(pmml);
            Map<FieldName, FieldValue> arguments = new LinkedHashMap<>();
            arguments.put(
                    FieldName.create("X"),
                    FieldValueUtil.create(DataType.DOUBLE, OpType.CONTINUOUS, "10.0")
            );
//            arguments.put(
//                    FieldName.create("y"),
//                    FieldValueUtil.create(DataType.DOUBLE, OpType.CONTINUOUS, "42.0")
//            );
            for(FieldValue fValue : arguments.values()){
                System.out.println(fValue instanceof FieldValue);
                System.out.println(fValue);
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
