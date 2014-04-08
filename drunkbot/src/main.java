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
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


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
	
		//before chatting with drunkbot you must enter either his or your valid user Facebook timeline ID
		//implemented with Facebook API and use of JSON classes
		
		//beginning of API implementation #1 - Facebook API
		
		System.out.println("To talk to Drunkbot enter his valid user Facebook timeline ID (durkin.bob): ");
		//the facebook timeline ID is last section of the url assiciated with your profile
		//ie mine is "erin.v.brink" taken from the url --> https://www.facebook.com/erin.v.brink
		
		//scan's the facebook ID that you entered 
		String fb = scan.nextLine();//comment out to not enter drunkbot's facebook id
        
		String baseUrl = "https://graph.facebook.com/";
		
		//or manually can be put in like
		//String userTimeline = "durkin.bob";
		//if this method is chosen comment out String fb and String userTimeline = fb;
		
		String userTimeline = fb;//comment out to not enter drunkbot's facebook id
		String fullURL = baseUrl + userTimeline;
		URL myURL = new URL(fullURL);
		InputStream ist = myURL.openStream();

		JSONTokener tok = new JSONTokener(ist);
		JSONObject result = new JSONObject(tok);
		
		//prints out information associated with the facebook timeline id you provided (either drunkbots or your own)
		
		ist.close();
		System.out.println("");
		System.out.println("User: ");
		System.out.println(result.get("first_name"));
		System.out.println(result.get("last_name"));
		System.out.println("");
		System.out.println("Gender: ");
		System.out.println(result.get("gender"));
		System.out.println("");
		System.out.println("Facebook ID: ");
		System.out.println(result.get("id"));
		System.out.println("");
		
		System.out.println("Your chat has begun!");
		System.out.println("");
	
		
		InputStream is = new FileInputStream( "en-pos-maxent.bin" );
		setModel( new POSModel( is ) ); 
		
		//greet user
		System.out.println("Heyyy, how you doin'?");
		
		//get input
		String input = scan.nextLine();		
	
		//checks inputs with dictionary.txt file
		dictionary = new DictSkipList<String, String>();
		
		fillDictionary(dictionary);
		
		int counter = 0;
		while (!input.equals("exit") && counter < 28)//exit prompts to quit chat
		{
			
//how to access dropbox API implementation #2
			if(input.toLowerCase().equals("yes")) //the simple answer yes brings up a recipe file from dropbox
				//used the keyword yes to be associated with this because drunkbot regualrily asks you if you'd 
				//like a drink or would like to join him.
			{
//keys to access dropbox
				final String APP_KEY = "xscrhfjtserjazg";
				final String APP_SECRET = "sjio84vd4hdb8ag";

				DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);

				DbxRequestConfig config = new DbxRequestConfig("JavaTutorial/1.0", Locale.getDefault().toString());
				DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(config, appInfo);

				// Instructions for accessing drunkbot
				
				String authorizeUrl = webAuth.start();
				System.out.println("Lovellyyy here are my recipes fromy my dropbox");
				System.out.println("");
				System.out.println("1. Copy this URL into your browser: ");
				System.out.println("");
				System.out.println(""+ authorizeUrl);
				System.out.println("");
				System.out.println("2. Then click \"Allow\"....you might have to log in first");
				System.out.println("3. Copy the authorization code.");
				System.out.println("4. Then paste it below:");
				String code = new BufferedReader(new InputStreamReader(System.in)).readLine().trim();

				// This will fail if the user enters an invalid authorization code.
				DbxAuthFinish authFinish = webAuth.finish(code);
				String accessToken = authFinish.accessToken;

				DbxClient client = new DbxClient(config, accessToken);

				System.out.println("Account: " + client.getAccountInfo().displayName);

				FileOutputStream outputStream = new FileOutputStream("Recipes.docx");
				
				try {
					DbxEntry.File downloadedFile = client.getFile("/Recipes.docx",
							null, outputStream);
					System.out.print("Dropbox has been accessed: ");
					System.out.print(downloadedFile);
					System.out.println("");
					System.out.println("I hope you're enjoyingggg your tasty drink my friend!");
					System.out.println("");
				} finally {
					outputStream.close();
				}
				//ends dropbox bit
			}
			else
			System.out.println(response(input) + "\n");
			input = scan.nextLine();
			counter++;
			
		}
		//prints if exit is inputed but user
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
			
			//API #3 - Google translate as a random response drunkbot will respond in French
			else if(7 == modulus){ 
				  Translator translate = Translator.getInstance();
					String text = translate.translate(input, Language.ENGLISH, Language.FRENCH);
					  System.out.println(text);
				return "Try and figure out what I said there suckkkerrr";
			}
			else if(8 == modulus){
				return "One time I was drunk, do you like tequila shots?";
			}
				return "I... what? What do you mean by " + verb + " and " + noun + "?";
		
		}
	  	
	  //API #3 Google Translate - For unknown responses drunkbot will speak either Spanish or Latin
	  
	  public static String noVerb(String noun, String input) {
		  Random rand = new Random();
		  int num = rand.nextInt(2);
		  Translator translate = Translator.getInstance();
			String text = translate.translate(input, Language.ENGLISH, Language.SPANISH);
			  System.out.println(text);
		  return "I don't understand you...So I spoke Spanish instead"; 
	  }
	  public static String noNoun(String verb, String input) {
		  Random rand = new Random();
		  int num = rand.nextInt(2);
		  /*Translator translate = Translator.getInstance();
			String text = translate.translate(input, Language.ENGLISH, Language.GERMAN);
			  System.out.println(text);*/
		  //if(0 == num)
			return "We are talking about liquor";
		  //else
			  //return "I don't understand you...So I spoke German instead";
	  }
	  public static String noNounVerb(String input) {
		  Random rand = new Random();
		  int num = rand.nextInt(2);
		  Translator translate = Translator.getInstance();
			String text = translate.translate(input, Language.ENGLISH, Language.LATIN);
			  System.out.println(text);
		  //if(0 == num) 
			  //return "You aren't making any sense, and I have nooooooo idea what you are saying.";
		 // else 
			  return "That was latin...I'm smart like that...";
	  }


}
