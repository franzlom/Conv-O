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

import java.util.List;

import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;

public class TextToSpeechExample {


  public static void main(String[] args) {
    TextToSpeech service = new TextToSpeech();
    service.setUsernameAndPassword("757bfd70-e1a5-4c37-a844-db6be0b06663", "zvdSLFPxG1dH");

    List<Voice> voices = service.getVoices().execute();
    System.out.println(voices);
  }

}
