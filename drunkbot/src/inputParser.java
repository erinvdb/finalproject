import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.util.InvalidFormatException;

//parser for user input
public class inputParser 
{
	//set part of speech model
	private POSModel model;
	
	//get POS file
	public inputParser(InputStream data) throws InvalidFormatException, IOException
	{
		setModel( new POSModel( data ) );
	}
	//set model
	private void setModel( POSModel model ) {
		this.model = model;
	}
	//get model
	private POSModel getModel() {
		return this.model;
	}
	
	//return true if input has a verb and noun
	public boolean hasVerbNoun(String s)
	{
		String[] ans = getVerbNoun(s);
		if (ans[0].equals("") || ans[1].equals(""))
		{
			return false;
		}
		return true;
	}
	//get array of verb and noun. If cannot be found, return null element
	public String[] getVerbNoun(String str)
	{
		//remove punctuation
		String s = removePunct(str);
		POSTaggerME tagger = new POSTaggerME( getModel() );
		String[] words = s.split( "\\s+" );
		String[] tags = tagger.tag( words );
		
		String verb = "", verbTag = "", noun = "", nounTag="";
		
		for(int i = 0; i < tags.length; i++)
		{
			if (tags[i].startsWith("VB"))
			{
				verb = words[i];
				verbTag = tags[i];
			}
			else if (tags[i].startsWith("NN"))
			{
				noun = words[i];
				nounTag = tags[i];
			}
		}
		
		String[] r = {verb, /*verbTag,*/ noun /*, nounTag*/};
		return r;
		
	}
	
	//return input without punctuation
	public String removePunct(String str)
	{
		return str.replaceAll("\\p{Punct}+", "");
	}
	
	

}
