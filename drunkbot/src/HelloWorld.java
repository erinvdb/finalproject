import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
 
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
 
public class HelloWorld {
  private POSModel model;
 
  public HelloWorld( InputStream data ) throws IOException {
    setModel( new POSModel( data ) );
  }
 
  public void run( String sentence ) {
    POSTaggerME tagger = new POSTaggerME( getModel() );
    String[] words = sentence.split( "\\s+" );
    String[] tags = tagger.tag( words );
    double[] probs = tagger.probs();
 
    for( int i = 0; i < tags.length; i++ ) {
      System.out.println( words[i] + " => " + tags[i] + " @ " + probs[i] );
    }
  }
 
  private void setModel( POSModel model ) {
    this.model = model;
  }
 
  private POSModel getModel() {
    return this.model;
  }
 
  public static void main( String args[] ) throws IOException {
//    if( args.length < 2 ) {
//      System.out.println( "HelloWord <file> \"sentence to tag\"" );
//      return;
//    }
 
	String str = "This is Erin, the ruller of all bananas.";
    InputStream is = new FileInputStream( "en-pos-maxent.bin" );
    HelloWorld hw = new HelloWorld( is );
    is.close();
 
    hw.run( str );
  }
}