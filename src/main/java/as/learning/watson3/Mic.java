package as.learning.watson3;

/**
 * Copyright 2017 IBM Corp. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */


import java.io.File;

import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import com.ibm.watson.developer_cloud.alchemy.v1.model.Keyword;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Keywords;
import com.ibm.watson.developer_cloud.http.HttpMediaType;
//import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.KeywordsResult;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.KeywordsResult;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.Transcript;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.BaseRecognizeCallback;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.AudioFormat;

import junit.framework.Assert;

import com.ibm.watson.developer_cloud.speech_to_text.v1.*;



/**
 * Recognize a sample wav file and print the transcript into the console output. Make sure you are using UTF-8 to print
 * messages; otherwise, you will see question marks.
 */
public class Mic {
	
	//static Scanner keyboard  = new Scanner(System.in);
	//static String newKeyWord = keyboard.nextLine();
	
	static DataLine.Info info;
	static int sampleRate;
	static javax.sound.sampled.AudioFormat format;
	
	static int counter = 0;
	
	static String noot;
	
	//static ArrayList<SpeechResults> myResults = new ArrayList<SpeechResults>();
	
	static String[] myKeywords = new String[]{"Housing", "Property", "Transportation"};

  public static void main(String[] args) throws Exception {
	  
	  System.out.println("beggining");
    SpeechToText service = new SpeechToText();
    service.setUsernameAndPassword("3dd61841-2878-43a7-b90c-c4066ecaf3b9", "UilCCXPaaWB6");

    

    // Signed PCM AudioFormat with 16kHz, 16 bit sample size, mono
    int sampleRate = 16000;
   
     format = new javax.sound.sampled.AudioFormat(sampleRate, 16, 1, true, false);
     info = new DataLine.Info(TargetDataLine.class, format);
    
    

    if (!AudioSystem.isLineSupported(info)) {
      System.out.println("Line not supported");
      System.exit(0);
    }

    TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
    openLine(line, format);

    AudioInputStream audio = new AudioInputStream(line);

    RecognizeOptions options = new RecognizeOptions.Builder()		
      .continuous(true)
      .interimResults(true)
      //.timestamps(true)
      .wordConfidence(true)
      .keywords(myKeywords)
      .keywordsThreshold(0.5)
      .inactivityTimeout(-1) // use this to stop listening when the speaker pauses, i.e. for 5s
      .contentType(HttpMediaType.AUDIO_RAW + "; rate=" + sampleRate)
      //.smartFormatting(true)
      .model(null)
      .build();
    
    
    service.recognizeUsingWebSocket(audio, options, new BaseRecognizeCallback() {
    	//String noot;
    	
    	
    	
      public void onTranscription(SpeechResults speechResults) {
    	  
    	  //if(!newKeyWord.isEmpty()) {
    		  //speechResults.getResults().get(0).getKeywordsResult().
    		  
    	  //}
    	   //System.out.println(speechResults);
    	  if(speechResults.getResults().get(0).isFinal()) {
    		  //System.out.println(speechResults);
    		  System.out.println();
    		  System.out.println("- "+speechResults.getResults().get(0).getAlternatives().get(0).getTranscript());
    		  
    		  //System.out.println(speechResults.getResults().get(0).getKeywordsResult().));
    		  
    		  Set<String> myKeys = speechResults.getResults().get(0).getKeywordsResult().keySet();
    		  
    		  System.out.println("These are Keys: " + myKeys);
    		  
    		 // System.out.println("getKeyWordsResult.toString: " + speechResults.getResults().get(0).getKeywordsResult().values());
    		  
    		  Map<String, List<KeywordsResult>> myMap = speechResults.getResults().get(0).getKeywordsResult();
    		 // for(Map.Entry<String, List<KeywordsResult>> entry: myMap.entrySet()) {
    			 // System.out.println("kjaskdjasldhsakdsahd");
    			//  System.out.println(entry.getKey() + "____ : ____"+ entry.getValue());
    			  //System.out.println("the hash code: " + entry.hashCode());
    		  //}
    		  
    		  
    	  }
    	    
    	    
    	    if(speechResults != null ) {
    	    	//myResults.add(speechResults);
    	    	for(Transcript t : speechResults.getResults())
    	    	{
    	    		
    	    		
    	    		if (t.isFinal() == true) {
    	    			
    	    			//noot = t.getAlternatives().get(0).getTranscript();
    	    			//noot = t.getAlternatives().get(0).getWordConfidences().get(0).toString();
    	    			
    	    		}
    	    		 
    	    		
    	    		//String noot = t.getAlternatives().get(0).getTranscript();
    	    		//noot = t.get
    	    		//String noot = t.getKeywordsResult().toString();
    	    		
    	    	//	t.getKeywordsResult().values().
    	    		//t.getKeywordsResult();
    	    		
    	    		//System.out.println(t.getKeywordsResult().values());
    	    		
    	    		
    	    		
    	    		if (t.getKeywordsResult() != null ) {
    	    			
    	    			//String myKeywordResults = t.getKeywordsResult().;
    	    			
    	    			for (int i = 0; i < myKeywords.length; i++) {
    	    				
    	    				if(t.getKeywordsResult().containsKey(myKeywords[i])) {
    	    					System.out.println("Keyword " + myKeywords[i] + " at position " + i );
    	    					//System.out.println(t.getKeywordsResult().containsKey(myKeywords[i]));
    	    				}
							
						}
    	    			
    	    			
    	    			//System.out.println(t.getKeywordsResult().containsKey("Transportation"));
    	    			
    	    			
    	    			
    	    			//System.out.println(t.getKeywordsResult().keySet());
    	    			
    	    			
    	    			//System.out.println(t.getKeywordsResult().values());
    	    			
    	    			
    	    			//System.out.println(t.getKeywordsResult().get(0));
    	    			
    	    			
    	    			
    	    		}
    	    		
    	    			
    	    		
    	    		 	//System.out.print(noot + " - ");
    	    	}
    	    	//System.out.println();
    	    }
    	    
    	    if (speechResults.isFinal()) {
    	    	
    	    	
    	    }
    	    
    	    
    	    
      }
      
     
      
      @Override
      public void onDisconnected() {
    	  
      
      }
      
      
      @Override
      public void onInactivityTimeout(RuntimeException e) {
    	  System.out.println("the connection timedout");
    	  
      }
      
      
      
    });
    
   
    
    
    //System.out.println(abc);
    System.out.println("Listening to your voice for the next 30s...");
    //Thread.sleep(30 * 1000);

   /*
    * turning off the line(websockets) keeps the line open forver forever
    * meaning the end print statements print because the sleep pauses the main code 
    * to give ibm watson to translate it within 30 seconds 
    */
   // line.stop();
    //line.close();

    System.out.println("end.");
    
    
    //KeywordsResult lul = new KeywordsResult();
    //System.out.println(lul.getNormalizedText());
    
    System.out.println("next end");
    
  }
  
  
  private static void openLine(TargetDataLine t, javax.sound.sampled.AudioFormat f) throws Exception {
	  t.open(f);
	  t.start();
  }
}
