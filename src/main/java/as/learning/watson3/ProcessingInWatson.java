package as.learning.watson3;

import processing.core.PApplet;

import java.io.File;

import com.ibm.watson.developer_cloud.alchemy.v1.model.Keyword;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.*;

public class ProcessingInWatson extends PApplet{
	
	public static void main(String[] args) {
		PApplet.main("as.learning.watson3.ProcessingInWatson");

	}
	
	public void settings() {
		
	size(200,200);
	}
	
	
	public void setup() {
		
		SpeechToText service = new SpeechToText();
	    service.setUsernameAndPassword("3dd61841-2878-43a7-b90c-c4066ecaf3b9", "UilCCXPaaWB6");

	    File audio = new File("test.wav");
	    SpeechResults transcript = service.recognize(audio).execute();

	    System.out.println(transcript);
		
		
	}
	
	public void draw() {
		
	}

}
