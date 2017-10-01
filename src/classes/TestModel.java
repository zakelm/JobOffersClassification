package classes;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import weka.classifiers.meta.FilteredClassifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

public class TestModel {
	String text;
	Instances instances;
	FilteredClassifier classifier;
	
	public void loadInput(String fileName) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String line;
			text = "";
			while ((line = reader.readLine()) != null) {
                text = text + " " + line;
            }
			System.out.println("Loaded data: " + fileName );
			reader.close();
		}
		catch (IOException e) {
			System.err.println("Problem found " + fileName);
		}
	}
	
	public void loadModel(String fileName) {
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
            Object tmp = in.readObject();
			classifier = (FilteredClassifier) tmp;
            in.close();
       } 
		catch (Exception e) {
			System.err.println(fileName);
		}
	}
	
	public void makeInstance() {
		ArrayList<String> atts = new ArrayList<String>(2);
		atts.add("commercial");
		atts.add("informatique");
		atts.add("btp");
		atts.add("tourisme");

		Attribute label = new Attribute("class", atts);
		Attribute attribute = new Attribute("text",(List<String>) null);
		ArrayList<Attribute> fvWekaAttributes = new ArrayList<Attribute>(2);
		fvWekaAttributes.add(label);
		fvWekaAttributes.add(attribute);
		instances = new Instances("Test relation", fvWekaAttributes, 1);   
		
		// Set class index
		instances.setClassIndex(0);
		
		// Create and add the instance
		DenseInstance instance = new DenseInstance(2);
		instance.setValue(attribute, text);
		instances.add(instance);
	}
	
	public void classify() {
		try {
			double pred = classifier.classifyInstance(instances.instance(0));
			System.out.println("Predicted class: " + instances.classAttribute().value((int) pred));
		}
		catch (Exception e) {
			System.out.println("Problem found " + e.getStackTrace());
		}		
	}

}
