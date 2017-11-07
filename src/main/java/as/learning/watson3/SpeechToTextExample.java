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
package as.learning.watson3;

import java.io.File;

import com.ibm.watson.developer_cloud.alchemy.v1.model.Keyword;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.*;


/**
 * Recognize a sample wav file and print the transcript into the console output. Make sure you are using UTF-8 to print
 * messages; otherwise, you will see question marks.
 */
public class SpeechToTextExample {

  public static void main(String[] args) {
    SpeechToText service = new SpeechToText();
    service.setUsernameAndPassword("3dd61841-2878-43a7-b90c-c4066ecaf3b9", "UilCCXPaaWB6");

    File audio = new File("test.wav");
    SpeechResults transcript = service.recognize(audio).execute();

    System.out.println(transcript);
    
    
    //System.out.println(keywords_result);
  }
}
