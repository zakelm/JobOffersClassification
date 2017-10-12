package classes;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import org.apache.commons.io.FileUtils;

public class GenerateAttr {
	public PrintWriter writer;
	
	public void writeAttr( String[] entires ) {
	    for( int i = 0; i < entires.length; i++ ) {
		writer.println( entires[ i ] );
		writer.flush();
	    }
	}

	public void arffHeader( String file, String classes, String classeType ) throws FileNotFoundException {
	    writer = new PrintWriter(file);
	    writer.println( "@relation textClassifier" );
	    writer.print( "\n" );
	    writer.println( "@attribute textClassifier {" + classes.replace( "[", "" ).replace( "]", "" ).trim() + "}" );
	    writer.println( "@attribute " + classeType.replace( "[", "" ).replace( "]", "" ).replace( ",", "" ) );
	    writer.print( "\n" );
	    writer.println( "@data" );
	    writer.flush();
	}
	
	public String[] getClassNames( String dataPath ) {
	    String[] subdirectories = new File( dataPath ).list();
	    return subdirectories;
	}
		
	public String[] readFiles( String labelPath, String Label ) throws IOException {
	    File folder        = new File( labelPath + File.separator + Label );
	    File[] filesList   = folder.listFiles();
	    String[] lines     = new String[ filesList.length ];

	    for ( int i = 0; i < filesList.length; i++ ) {
		File file = filesList[ i ];

		if ( file.isFile() && file.getName().endsWith( ".txt" ) ) {
		    String content = FileUtils.readFileToString(file);
		    lines[i] = Label + "," + "'" + content.replaceAll( "\n", "" ).replaceAll( "\r", "" ).replaceAll( ",", " " ).replaceAll( "'", " " ).replace( "_", " " ).replace( "-", " " ).replace( "&", " " ).replace( "%", " " ).replaceAll( " +", " " ).trim() + "'";
		}
	    }
	    return lines;
	}
}
