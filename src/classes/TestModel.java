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
	String text, line;
	Instances instances;
	FilteredClassifier classifier;
	
	// loading input data
	public void loadInput ( String fileNam e ) {
            try {
			BufferedReader reader = new BufferedReader( new FileReader( fileName ) );
			text = "";
			while ( ( line = reader.readLine() ) != null ) {
			    text = text + " " + line;
			}
			System.out.println("Data chargés: " + fileName );
			reader.close();
		}
		catch (IOException e) {
			System.err.println( "Problème trouvé à " + fileName );
		}
	}
	
	// loading model
	public void loadModel( String fileName ) {
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
	
	// Create attributes
	public void makeInstance() {
		ArrayList<String> atts = new ArrayList<String>( 2 );
		atts.add( "commercial" );
		atts.add( "informatique" );
		atts.add( "btp" );
		atts.add( "tourisme" );

		Attribute label = new Attribute( "class", atts );
		Attribute attribute = new Attribute( "text",(List<String>) null );
		ArrayList<Attribute> fvWekaAttributes = new ArrayList<Attribute>( 2 );
		fvWekaAttributes.add( label );
		fvWekaAttributes.add( attribute );
		instances = new Instances( "Test relation", fvWekaAttributes, 1 ); 
		instances.setClassIndex( 0 );
		DenseInstance instance = new DenseInstance( 2 );
		instance.setValue( attribute, text );
		instances.add( instance );
	}
	
	// classification method
	public void classify() {
	    try {
		double pred = classifier.classifyInstance(instances.instance(0));
		System.out.println("Classe prédit: " + instances.classAttribute().value((int) pred));
	    }
	    catch (Exception e) {
		System.out.println("Problème trouvé à " + e.getStackTrace());
	    }		
	}

}
