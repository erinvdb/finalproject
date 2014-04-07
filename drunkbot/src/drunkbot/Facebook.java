package drunkbot;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


//Java code: send to external api to do some work: 
//	get response back in Java. 
//	You should think of the Api as just a method in your code. 
//	It needs some parameters to do work and it's got a return type which is a json string.



public class Facebook {
	
	public Facebook() {

	}
	
	public static void main(String args[]) throws IOException {

		String baseUrl = "https://graph.facebook.com/";
		String userTimeline = "erin.v.brink";
		String fullURL = baseUrl + userTimeline;
//		fullURL += "?screen_name=" + URLEncoder.encode("AlexYellowShoes");
		URL myURL = new URL(fullURL);
		

		
		InputStream is = myURL.openStream();

		JSONTokener tok = new JSONTokener(is);
		JSONObject result = new JSONObject(tok);
		
		is.close();
		
		System.out.println(result.get("last_name"));
		
		
		
//		BufferedReader br = new BufferedReader(new InputStreamReader(is));
//		
//		String line = br.readLine();
//		
//		while (line!=null)
//		{
//			System.out.println(line);
//			line = br.readLine();
//		}
	}
}


//Have an interface
//Stick into actuallyResponder
//If sentence contains facebook then ... response using scanner



//https://graph.facebook.com/<username>?oauth=authentication
//Have appId and Secret as Strings
//string auth =response; (of get oauth)

/* make the API call */
FB.api(
    "/{friendlist-id}",
    function (response) {
      if (response && !response.error) {
        /* handle the result */
      }
    }
);

//this will display the friend list of the person

