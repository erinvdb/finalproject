import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.util.InvalidFormatException;

import javax.swing.*; //access the Jframe 

import com.gtranslate.Language;
import com.gtranslate.Translator;

import com.dropbox.core.*;
import java.util.Locale;


public class main {
	
	JPanel p = new JPanel();
	JTextArea dialog = new JTextArea(20,50);
	JTextArea input = new JTextArea(1,50);
	JScrollPane scroll = new JScrollPane(
			dialog,
			JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
			);
	
	//parts of speech model
	private static POSModel model;
	
	private static DictSkipList<String, String> dictionary;
	
	private DictSkipList<String, String> getDict()
	{
		return dictionary;
	}

	public static Scanner scan = new Scanner(System.in);

	public static void main(String[] args) throws InvalidFormatException, IOException, DbxException {
	
        
        

		//INSERT FACEBOOK STUFF....
		//Facebook fb = new Facebook() 
		
		InputStream is = new FileInputStream( "en-pos-maxent.bin" );
		setModel( new POSModel( is ) ); 
		
		//greet user
		System.out.println("Heyyy, how you doin'?");
		//System.out.println("Heyyyy, want to drink with me?");
		
		//get input
		String input = scan.nextLine();
		
		Translator translate = Translator.getInstance();
		String text = translate.translate(input, Language.ENGLISH, Language.GERMAN);
		System.out.println(text); 
		
		
		
		//return output
		//make dictionary of terms and update it
		
		dictionary = new DictSkipList<String, String>();
		
		fillDictionary(dictionary);
		
		int counter = 0;
		while (!input.equals("exit") && counter < 28)
		{
			if (input.equals("Drunkbot what should I drink"))
			{
				System.out.println("Would you like to see some recippeeesss?");
				//dropbox recipes here....
			}

			if(input.toLowerCase().equals("yes"))
			{

				final String APP_KEY = "xscrhfjtserjazg";
				final String APP_SECRET = "sjio84vd4hdb8ag";

				DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);

				DbxRequestConfig config = new DbxRequestConfig("JavaTutorial/1.0", Locale.getDefault().toString());
				DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(config, appInfo);

				// Have the user sign in and authorize your app.
				String authorizeUrl = webAuth.start();
				System.out.println("1. Go to: " + authorizeUrl);
				System.out.println("2. Click \"Allow\" (you might have to log in first)");
				System.out.println("3. Copy the authorization code.");
				String code = new BufferedReader(new InputStreamReader(System.in)).readLine().trim();

				// This will fail if the user enters an invalid authorization code.
				DbxAuthFinish authFinish = webAuth.finish(code);
				String accessToken = authFinish.accessToken;

				DbxClient client = new DbxClient(config, accessToken);

				System.out.println("Linked account: " + client.getAccountInfo().displayName);

				File inputFile = new File("recipes.doc");
				FileInputStream inputStream = new FileInputStream(inputFile);
				try {
					DbxEntry.File uploadedFile = client.uploadFile("/recipes.doc",
							DbxWriteMode.add(), inputFile.length(), inputStream);
					System.out.println("Uploaded: " + uploadedFile.toString());
				} finally {
					inputStream.close();
				}
				//ends dropbox bit
			}
			else
			System.out.println(response(input) + "\n");
			input = scan.nextLine();
			counter++;
			
		}
		System.out.println("I think it is time for me to go.\n");
		input = scan.nextLine();
		
		System.out.println("Goodbye.\n*Falls off chair*");
		
	}
	
	//fill dictionary initially with predefined values
	public static void fillDictionary(DictSkipList<String, String> dictionary) throws FileNotFoundException
	{
		FileInputStream in = new FileInputStream("dictionary.txt");
		Scanner fin = new Scanner(in);
		
		
		String key, value;
		//read initial dictionary from file dictionary.txt
		while (fin.hasNext())
		{
			//get values
			key = fin.nextLine();
			value = fin.nextLine();
			fin.nextLine();
			
			//add key & value to dictionary in lower case
			dictionary.put(key.toLowerCase(), value.toLowerCase());
		}
		
	}
	
	 private static void setModel( POSModel m ) {
		    model = m;
	  }
	  private static POSModel getModel() {
		    return model;
	  }
	  
		public void addText(String str){
			dialog.setText(dialog.getText() +str);
	}
	  
	  //generate chatbot response
	  public static String response(String input) throws InvalidFormatException, IOException
	  {
		  
		  //check for dictionary, no punctuation
		  if (dictionary.containsKey(input.toLowerCase()))
		  {
			  return dictionary.get(input.toLowerCase());
		  }
		  
		  //generate verb/noun response
		  InputStream is = new FileInputStream( "en-pos-maxent.bin" );
		  inputParser parse = new inputParser(is);

		  String[] verbNoun = parse.getVerbNoun(input);
		  String str = construct(verbNoun[0], verbNoun[1], input);
		  return str;
	  }
	  
	  //construct response sentence
	  public static String construct(String verb, String noun, String input) {
			Random rand = new Random();
			int weight_max = 1000;
			int weight_min = 1;
			int div_max = 10;
			int div_min = 1;
			int weight = rand.nextInt((weight_max - weight_min) + 1) + weight_min;
			int div = rand.nextInt((div_max - div_min) + 1) + div_min;
			div = 9;
			weight = rand.nextInt();
			
			int modulus = weight % div;
			
			if(verb.isEmpty() && !noun.isEmpty()) 
				return noVerb(noun, input);
			if(!verb.isEmpty() && noun.isEmpty()) 
				return noNoun(verb, input);
			if(verb.isEmpty() && noun.isEmpty())
				return noNounVerb(input);
			
			if(input.substring(input.length()-1).equals("?") && (modulus % 2) == 0)
				return "Who are you, comrade question?";
			else if(1 == modulus){ 
				return "I also like to " + verb  + " " + noun + " when I drink.";
			}
			else if(2 == modulus){ 
				return "Oh yeah, absolutely. What do you think of " + noun + "?";
			}
			else if(3 == modulus){ 
				return "Such " + noun + ". Very " + verb + ". Wow.";
			}
			else if(4 == modulus){ 
				return "Maybe next time I'll " + verb + " your mom.... hue hue hue hueeeeeeee.";	
			}
			else if(5 == modulus){ 
				return "HA! YOU ARE A FUNNY BUGGER AREN'T YOU! HA HA HA HA";
			}
			else if(6 == modulus){ 
				return "We are talking about liquor";
			}
			else if(7 == modulus){ 
				return "Stop changing the subject what do you want to drink";
			}
			else if(8 == modulus){
				return "One time I was drunk, do you like tequila shots?";
			}
				return "I... what? What do you mean by " + verb + " and " + noun + "?";
		
		}
	  	
	  public static String noVerb(String noun, String input) {
		  Random rand = new Random();
		  int num = rand.nextInt(2);
		  return "I don't understand you...";
		  //return "I loooooove " + noun + ". I also love this scotch! Scotch is good.";
	  }
	  public static String noNoun(String verb, String input) {
		  Random rand = new Random();
		  int num = rand.nextInt(2);
		  if(0 == num)
			  return "We are talking about liquor";
		  else
			  return "Oh how interestingggggg";
	  }
	  public static String noNounVerb(String input) {
		  Random rand = new Random();
		  int num = rand.nextInt(2);
		  if(0 == num) 
			  return "Huh? What do you mean by that?";
		  else 
			  return "You aren't making any sense, and I have nooooooo idea what you are saying.";
	  }


}
