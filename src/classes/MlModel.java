package classes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Random;

import weka.core.tokenizers.NGramTokenizer;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;
import weka.core.converters.ArffLoader.ArffReader;
import weka.core.converters.ArffSaver;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class MlModel {
	Instances trainData;
	StringToWordVector filter;
	FilteredClassifier classifier;
		
	public void loadArff(String fileName) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			ArffReader arff = new ArffReader(br);
			trainData = arff.getData();
			System.out.println("Loaded dataset  " + fileName);
			br.close();
		}
		catch (IOException e) {
			System.out.println(fileName);
			
		}
	}
	
	public void evaluate() {
		try {
			trainData.setClassIndex(0);			
			filter = new StringToWordVector();			
			filter.setAttributeIndices("last");
			
			NGramTokenizer tokenizer=new NGramTokenizer();
			tokenizer.setNGramMinSize(1);
			tokenizer.setNGramMaxSize(3);
			tokenizer.setDelimiters(" \r\n\t.,;:'\"()?!'");
			
			filter.setTokenizer(tokenizer);
			filter.setUseStoplist(false);
			filter.setLowerCaseTokens(true);
			filter.setOutputWordCounts(true);
			filter.setWordsToKeep(1000);
			
			classifier = new FilteredClassifier();
			classifier.setFilter(filter);
			classifier.setClassifier(new SMO());
			
			Evaluation eval = new Evaluation(trainData);
			eval.crossValidateModel(classifier, trainData, 4, new Random(1));
			
			System.out.println(eval.toSummaryString());
			System.out.println(eval.toClassDetailsString());
			System.out.println("Dataset evaluated");
						
		}
		catch (Exception e) {
			System.err.println(e.getStackTrace());
		}
	}	
	
	public void train() {
		try {
			trainData.setClassIndex(0);
			filter = new StringToWordVector();
			filter.setAttributeIndices("last");
			
			NGramTokenizer tokenizer=new NGramTokenizer();
			tokenizer.setNGramMinSize(1);
			tokenizer.setNGramMaxSize(3);
			tokenizer.setDelimiters(" \r\n\t.,;:'\"()?!'");
			
			filter.setTokenizer(tokenizer);
			filter.setUseStoplist(false);
			filter.setLowerCaseTokens(true);
			filter.setOutputWordCounts(true);
			filter.setWordsToKeep(1000);		
			
			classifier = new FilteredClassifier();
			classifier.setFilter(filter);
			classifier.setClassifier(new SMO());
			classifier.buildClassifier(trainData);
			
			Instances trainingDataFiltered = weka.filters.Filter.useFilter(trainData, filter); // filter training data
			
			ArffSaver saver = new ArffSaver();
			saver.setInstances(trainingDataFiltered);
			saver.setFile(new File("attributes/MyFilteredLearner.arff"));
			saver.writeBatch();
			
			System.out.println("Training dataset");
		}
		catch (Exception e) {
			System.err.println(e.getStackTrace());
		}
	}	
	
	public void saveModel(String fileName) {
		try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));
            out.writeObject(classifier);
            out.close();
        } 
		catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
