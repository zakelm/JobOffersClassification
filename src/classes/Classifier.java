package classes;

/*
 * Class to run the application
 */

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

public class Classifier {

    public static void main(String[] args) {
	// First we generate Attributes in arrf file
	GenerateAttr GA = new GenerateAttr();
	//Folder to save the ARFF File
	String attrPath  = "attributes";
	//Folder containing data.
	String dataPath  = "data";
	String[] classes = GA.getClassNames( dataPath );
	System.out.println( Arrays.toString( classes ) );

	try {
	    GA.arffHeader( attrPath+"/Classes.arff", Arrays.toString( classes ).trim(), "text String" );
	    for( int i = 0; i < classes.length; i++ ) {
	        System.out.println( classes[ i ] );
	        String[] classData = GA.readFiles( dataPath, classes[ i ] );
	        GA.writeAttr( classData );
	    }
	    GA.writer.close();
	} 
	catch (IOException e) {
	    System.err.println( "Problème trouvé" + e.printStackTrace() );
	}
	    
	// Second we train the model
	MlModel learner = new MlModel();
	learner.loadArff( "attributes/Classes.arff" );
	// Evaluation
	learner.evaluate();
	// Training
	learner.train();
	// Saving the model
	learner.saveModel( "mlModel/Classes.dat" );

	// We test our trained classification model
	TestModel classifier;
	String [] testFile = { "informatique", "btp", "tourisme", "commercial" };
	// maximum lines to take into account for test
	int max = 9;
	for(int i = 0; i<testFile.length; i++) {
	    if( testFile[i] == "tourisme" ) {
		max = 1;
	    }
	    
	    for(int j=0 ; j<max; j++){
		classifier = new TestModel();
		// Loading testset
	        classifier.loadInput( "testset/" + testFile[i] + "/" + j + ".txt" );
		// Loading model
		classifier.loadModel( "mlModel/Classes.dat" );
		// MAking instances
		classifier.makeInstance();
		// Classifying
		classifier.classify();
	    }

        }
    }

}
