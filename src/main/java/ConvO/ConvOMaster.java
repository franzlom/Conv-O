package ConvO;
/*
 * 
 * This is my attempt on making a version of Conv-O that uses a text to speech program
 * 
 * Currently this app is being built for an event in August for a political event
 * There will be pre-existing words and all the bubbles will be pre-created before hand
 * 
 * @author Franz Lomibao
 * @version 1.0
 * 
 * 
 */

import processing.core.*;

import processing.awt.*;
import processing.data.*;
import processing.event.*;
import processing.javafx.*;
import processing.opengl.*;
import processing.opengl.cursors.*;
import processing.opengl.shaders.*;
//import convOMaster.Particle;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle.Control;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.sound.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

import toxi.geom.*;
import toxi.physics2d.*;
import toxi.physics2d.behaviors.*;
import controlP5.*;

//import extendingPApplet.Particle;

import javax.swing.JOptionPane;

import com.ibm.watson.developer_cloud.http.HttpMediaType;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.KeywordsResult;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.Transcript;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.BaseRecognizeCallback;

import ddf.minim.*;
import ddf.minim.analysis.*;

public class ConvOMaster extends PApplet {

	static DataLine.Info info;
	static int sampleRate;
	static javax.sound.sampled.AudioFormat format;
	static int counter = 0;
	static String noot;
	private  PFont font ;

	// static ArrayList<SpeechResults> myResults = new ArrayList<SpeechResults>();

	static String[] myKeywords = new String[] { "Collaboration", "Helping", "Transportation", "fun",
			"Tornado", "Meeting", "Meetings", "Help", "Selection" , "Car", "Boat", "Plane", "Apple"};
	static int[] myKeywordCounter = new int[myKeywords.length];

	public static void main(String[] args) {
		PApplet.main("ConvO.ConvOMaster");

	}

	public void settings() {
		size(1280, 700);
		//fullScreen();

	}

	
	public void setup() {

		
		// System.getProperty("java.classpath");

		/*
		 * try {
		 * 
		 * createHiddenParticles();
		 * 
		 * } catch (Exception e) { // System.err.println("NullPointerException: " +
		 * e.getMessage());
		 * 
		 * println("create hidden particles");
		 * 
		 * e.printStackTrace();
		 * 
		 * }
		 */

		myFont = createFont("CaviarDreams.tff", 24);

		cp5 = new ControlP5(this);
		minim = new Minim(this);
		averageNoise = new FloatList();
		lastCalTime = 0;
		// use the getLine method of minim to get an audio input
		// in = minim.getLineIn();

		// fft = new FFT(in.bufferSize(), in.sampleRate());

		// fft.logAverages(60, 10);

		cp5.addButton("AddBubble")
				.setFont(myFont)
				.setPosition(0, height - 40)
				.setSize(width / 4, 40);

		cp5.addButton("SaveScreen")
				.setFont(myFont)
				.setPosition(width / 4, height - 40)
				.setSize(width / 4, 40);

		clearBubbleButton = cp5.addButton("RemoveBubble")
				.setFont(myFont)
				.setPosition((width / 4) * 3, height - 40)
				.setSize(width / 4, 40);
		
		cp5.addButton("Results")
				.setFont(myFont)
				.setPosition((width/4) * 2 , height - 40)
				.setSize(width / 4, 40);

Textfield  akjhdsajkda = cp5.addTextfield("asa")
				.setPosition(400,400)
				.setSize(100, 50)
				.setAutoClear(false);
		

		 akjhdsajkda.hide();
		 
		 
		 font = createFont("CaviarDreams.tff", 20);
			fill(0);
			textAlign(PConstants.CENTER);
			textFont(font);
			text("12313213131", PConstants.X / 2, PConstants.Y / 2);
		 
		
		 
		// setup physics with 10% drag
		physics = new VerletPhysics2D();
		physics.setDrag(0.05f);
		// physics.setWorldBounds(new Rect(r, r, width - d, height - d)); //dunno why
		// but it just works better this way
		physics.setWorldBounds(new Rect(-width / 2 + r, -height / 2 + r, width - d, height - d - 40));
		// the NEW way to add gravity to the simulation, using behaviors
		physics.addBehavior(new GravityBehavior2D(new Vec2D(0, (float) 0.0))); // i dont need gravity
		new Vec2D();

		// println(width);

		try {

			//System.out.println("beggining");
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

			RecognizeOptions options = new RecognizeOptions.Builder().continuous(true).interimResults(true)
					// .timestamps(true)
					.wordConfidence(true)
					.keywords(myKeywords)
					.keywordsThreshold(0.5)
					.inactivityTimeout(10) // use this
																											// to stop
																											// listening
																											// when the
																											// speaker
																											// pauses,
																											// i.e. for
																											// 5s
					.contentType(HttpMediaType.AUDIO_RAW + "; rate=" + sampleRate)
					// .smartFormatting(true)
					.model(null).build();
			
			println("aadadda");

			service.recognizeUsingWebSocket(audio, options, new BaseRecognizeCallback() {
				// String noot;

				public void onTranscription(SpeechResults speechResults) {

					for (int i = 0; i < physics.particles.size(); i++) {
						Particle abc = (Particle) physics.particles.get(i);
						if (abc.isZoomed()) {
							abc.diameter += 0.25;
							currentClickedParticle = abc;
						}

					}
					
					//println("jklasjdkladsjakldlkja sdjalk sakjdsaklj ");
					
					//println(speechResults.getResults());
					// Particle newClickedParticle = currentClickedParticle;
					// currentClickedParticle.diameter += 0.2;

					// if(!newKeyWord.isEmpty()) {
					// speechResults.getResults().get(0).getKeywordsResult().

					// }
					// System.out.println(speechResults);

					//println("notFInal");
					if (speechResults.getResults().get(0).isFinal()) {
						// System.out.println(speechResults);

						System.out.println("- " + speechResults.getResults().get(0).getAlternatives().get(0).getTranscript());
						
						
						
						

						// System.out.println(speechResults.getResults().get(0).getKeywordsResult().));

						Set<String> myKeys = speechResults.getResults().get(0).getKeywordsResult().keySet();

						//System.out.println("These are Keys: " + myKeys);
						//myKeys.iterator()
						
						
						for (int i = 0; i < physics.particles.size(); i++) {
							Particle abc = (Particle) physics.particles.get(i);
							if (abc.isZoomed()) {
								abc.flashColour();
								abc.diameter += 0.40;
								currentClickedParticle = abc;
							}
							//println("before j loop");
							
							

						}

						// System.out.println("getKeyWordsResult.toString: " +
						// speechResults.getResults().get(0).getKeywordsResult().values());

					}

					if (speechResults != null) {
						// myResults.add(speechResults);
						for (Transcript t : speechResults.getResults()) {

							if (t.isFinal() == true) {

								// noot = t.getAlternatives().get(0).getTranscript();
								// noot = t.getAlternatives().get(0).getWordConfidences().get(0).toString();
 
							}

							// String noot = t.getAlternatives().get(0).getTranscript();
							// noot = t.get
							// String noot = t.getKeywordsResult().toString();

							// t.getKeywordsResult().values().
							// t.getKeywordsResult();

							// System.out.println(t.getKeywordsResult().values());

							if (t.getKeywordsResult() != null) {


								for (int i = 0; i < myKeywords.length; i++) {

									if (t.getKeywordsResult().containsKey(myKeywords[i])) {
										System.out.println("Keyword: " + myKeywords[i]);
										// System.out.println(t.getKeywordsResult().containsKey(myKeywords[i]));
									}

								}

								// System.out.println(t.getKeywordsResult().containsKey("Transportation"));

								// System.out.println(t.getKeywordsResult().keySet());

								// System.out.println(t.getKeywordsResult().values());

								// System.out.println(t.getKeywordsResult().get(0));

							}

							// System.out.print(noot + " - ");
						}
						// System.out.println();
					}

					

				}

				@Override
				public void onDisconnected() {
					println("disconnected too");
					

				}

				@Override
				public void onInactivityTimeout(RuntimeException e) {
					System.out.println("the connection timedout");

				}

			});

			// System.out.println(abc);
			//System.out.println("Listening to your voice for the next 30s...");
			// Thread.sleep(30 * 1000);

			/*
			 * turning off the line(websockets) keeps the line open forver forever meaning
			 * the end print statements print because the sleep pauses the main code to give
			 * ibm watson to translate it within 30 seconds
			 */
			// line.stop();
			// line.close();

			System.out.println("Begin");

			// KeywordsResult lul = new KeywordsResult();
			// System.out.println(lul.getNormalizedText());

			//System.out.println("next end");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void draw() {

		fill(120);
		ellipse(width / 2, height / 2, 50, 50);
		newMouseX = ((mouseX - (width / 2)) / zoomFactor) + zoomCenter.x;
		newMouseY = ((mouseY - (height / 2)) / zoomFactor) + zoomCenter.y;
		
		//private String time = System.nanoTime();
		
		

		// println("X = " + newMouseX);
		// println("y = " + newMouseY);

		background(bg, bg, bg);
		// noStroke();
		stroke(175);
		fill(175);
		rectMode(CENTER);
		
		//long myTime= System.nanoTime() / 1000000000;

		//myTime = myTime % 3;
		
		

		physics.update();
		//text(  myTime ,200,200 );
		
		
		pushMatrix();
		// line(-200, -200, 200, 200);

		translate(width / 2, height / 2);
		scale(zoomFactor);
		translate(-zoomCenter.x, -zoomCenter.y);

		for (int i = 0; i < physics.particles.size(); i++) {
			if (i > 0) {

				VerletParticle2D previous = physics.particles.get(i - 1);
				// VerletParticle2D current = physics.particles.get(i);

				stroke(123, 123, 123);
				// for(VerletParticle2D abc = physics.particles.get(i); abc.distanceTo(previous)
				// < 0 ; abc++){

				// }

				line(physics.particles.get(i).x, physics.particles.get(i).y, previous.x, previous.y);
				// physics.a
				// line(0, 0, width/2, height/2);
			}
		}

		for (int i = 0; i < physics.particles.size(); i++) {
			// println("i = " + i);

			Particle abc = (Particle) physics.particles.get(i);
			// System.out.println(abc.radius);
			// System.out.println("extendingPApplet.particle " + abc);

			// println("is the physics.particles. empty? " + physics.particles.isEmpty());

			try {
				abc.display();

			} catch (NullPointerException e) {
				System.out.println("error at " + e.getMessage());
				e.printStackTrace();

				// System.out.println("caught exception while doing whatever " + e);

			}

			// System.out.println("physics particles size = " + physics.particles.size());
			// System.out.println("int i = " + i);

		}
		// System.out.println("hello, after the if statement in the draw method");

		popMatrix();

		// line(-200, -200, 200, 200);
		

		if (currentClickedParticle != null) {
			// println(currentClickedParticle.diameter);
		}

		// println("in.mix.level() = " + in.mix.level());
		// println("avg noise = " + avgNoise);
		// println();
		// println("millis() = " + millis());
		// println("last call time + 5000 = "+lastCalTime + 5000);

		/*
		 * if (in.mix.level() / inMixDivider > avgNoise /2 && currentClickedParticle !=
		 * null) { for (int i = 0; i < physics.particles.size(); i++) { Particle abc =
		 * (Particle) physics.particles.get(i); if (abc.isZoomed()) { abc.diameter +=
		 * 0.25; currentClickedParticle = abc; }
		 * 
		 * } // Particle newClickedParticle = currentClickedParticle; //
		 * currentClickedParticle.diameter += 0.2;
		 * 
		 * }
		 */

		// if (mReleased) {
		// zoomBubble(currentClickedParticle);
		// //addRemoveBubble();

		// }

		// if

	}

	public void mousePressed() {
		mousePos = new Vec2D(newMouseX, newMouseY);
		// println("newMouseX = " + newMouseX);
		// println("newMouseY = " + newMouseY);
		contains(mousePos);

		mouseAttractor = new AttractionBehavior2D(mousePos, 250, -0.9f);
		physics.addBehavior(mouseAttractor);

	}

	private static void openLine(TargetDataLine t, javax.sound.sampled.AudioFormat f) throws Exception {
		t.open(f);
		t.start();
	}

	public void mouseReleased() {
		if (bubblePress && currentClickedParticle != null && !currentClickedParticle.isZoomed() && !worldIsZoomed
				&& !currentClickedParticle.isLocked()) {

			// if(currentClickedParticle.isZoomed()){
			// return;
			// }
			mReleased = true;
			println("ZOOM!!!");
			// currentClickedParticle.addD(5);
			zoomBubble(currentClickedParticle);

		} else if (bubblePress && currentClickedParticle != null && currentClickedParticle.isZoomed()) {
			zoomBubbleOut(currentClickedParticle);
			println("ZOOM OUT!!!");
		}

		bubblePress = false;

		physics.removeBehavior(mouseAttractor);
	}

	public void addParticle() {
		// System.out.println("add particle");

		// textfield.show();
		// textValue = textfield.getText();

		// boolean nameYet = false;
		// while(nameYet == false){
		// if(textValue!= ""){
		// nameYet = true;

		// }
		// }
		// println("textValue = " + cp5.get(Textfield.class,"input").getText());

		String input = JOptionPane.showInputDialog(null, "Enter Input:", "Dialog for Input",
				JOptionPane.INFORMATION_MESSAGE);
		// System.out.println("input = " + input);

		if (!input.isEmpty()) {
			Particle p = new Particle(Vec2D.randomVector().scale(5).addSelf(0, -height / 2), d, input, this);

			// Particle p = new Particle(Vec2D.randomVector(),d);
			physics.addParticle(p);
			// add a negative attraction force field around the new particle
			physics.addBehavior(new AttractionBehavior2D(p, p.diameter * 6, -1.2f, 0.1f));
		} else {
			addParticle();
		}

	}

	// need to calculate
	private void zoomBubble(VerletParticle2D particleIn) {
		clearBubbleButton.hide();

		if (particleIn != null) {
			worldIsZoomed = true;
			particleIn.lock();
			currentClickedParticle.setZoom(true);
			worldIsZoomed = true;
			zoomCenter.set(particleIn.x, particleIn.y);
			zoomFactor = 3;

		}
	}

	private void zoomBubbleOut(VerletParticle2D particleIn) {
		clearBubbleButton.show();
		if (particleIn != null && particleIn.isLocked()) {
			particleIn.unlock();
			worldIsZoomed = false;
			currentClickedParticle.setZoom(false);
			zoomCenter.set(0, 0);

			zoomFactor = 1;

			// currentClickedParticle.clear();
		}
	}
	
	public void Results() throws IOException{
		
		int counter;
		println("results button");	
		String helloWorld = "Hello World";
		PrintWriter writer = new PrintWriter("Results.txt");
		writer.println("Results and Insights");
		for(int i = 0; i < myKeywords.length; i++ ) {
			counter = i + 1;
			writer.println(counter + ". " + myKeywords[i] + ": " + myKeywordCounter[i]);
			
		}
		
		
		writer.close();
		
	}

	public void AddBubble() {

		// println("particle.size " + physics.particles.size());
		// stroke(0,150);
		if (physics.particles.size() < NUM_PARTICLES) {
			// askForTopic();
			addParticle();

			// println("particle.size " + physics.particles.size());

			for (int i = 0; i < physics.particles.size(); i++) {
				if (i > 0) {

					Particle previous = (Particle) physics.particles.get(i - 1);
					// VerletParticle2D current = physics.particles.get(i);
					VerletSpring2D spring = new VerletSpring2D(physics.particles.get(i), previous, 400, (float) 0.2);
					// VerletSpring2D abv = new VerletSpring2D()
					Particle current = (Particle) physics.particles.get(i);
					// Add the spring to the physics world
					physics.addSpring(spring);
					stroke(123, 123, 123);
					line(current.x, current.y, previous.x, previous.y);
					// physics.a
					// line(0, 0, width/2, height/2);
				}
			}

			// for(int i = 0; i < physics.particles.size(); i++){
			// VerletParticle2D pi = physics.particles.get(i);
			// for(int j = i +1; j <physics.particles.size(); j++){
			// VerletParticle2D pj = physics.particles.get(j);
			// VerletSpring2D spring=new VerletSpring2D(pi,pj,200,0.2);

			// physics.addSpring(spring);
			// //line(pi.x, pi.y, pj.x, pj.y);
			// }
			// }s

		} else {
			JOptionPane.showMessageDialog(frame,
					// "Reached maximum allowed bubbles, please subscribe.",
					"Maximum Bubbles Reached! Start Over by Pressing Remove Bubble.", "Error",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	public void RemoveBubble() {

		physics.particles.clear();
		physics.clear();
		currentClickedParticle.clear();

		lastCalTime = 0;
	
	}

	public void SaveScreen() {
		println("screen saved!!!");
		saveFrame("line-######.tif");
	}

	private void contains(Vec2D posIn) {

		bubblePress = false;
		// VerletParticle2D b;

		for (int i = 0; i < physics.particles.size(); i++) {
			Particle thisParticle = (Particle) physics.particles.get(i);
			if (thisParticle.getPreviousPosition().distanceTo(posIn) < thisParticle.getD() / 2) {
				bubblePress = true;
				currentClickedParticle = thisParticle;
			}
		}

		// for(int i = 0; i < physics.particles.size(); i++){
		// physics.particles.getPreviousLocation();
		// }
	}

	

	

	/*
	 * void createHiddenParticles() { // try{
	 * 
	 * // }catch(Exception e){ // e.printStackTrace(); // }
	 * 
	 * extendingPApplet.Particle hiddenParticle2 = outside.new Particle(new
	 * Vec2D(200, 0), d, ""); extendingPApplet.Particle hiddenParticle3 =
	 * outside.new Particle(new Vec2D(400, 0), d, ""); extendingPApplet.Particle
	 * hiddenParticle1 = outside.new Particle(new Vec2D(1, 1), d, "");
	 * 
	 * physics2.addParticle(hiddenParticle1); physics2.addParticle(hiddenParticle2);
	 * physics2.addParticle(hiddenParticle3);
	 * 
	 * for (int i = 0; i < physics2.particles.size(); i++) {
	 * extendingPApplet.Particle abc = (extendingPApplet.Particle)
	 * physics2.particles.get(i); physics2.addBehavior(new AttractionBehavior2D(abc,
	 * abc.diameter * 6, -1.2f, 0)); }
	 * 
	 * }
	 */

	// processing doesn't like the color class
	// edit: not a class, in processing colour is a primitive type

	// color background = new color(239,239,239);

	// this line is required every time we are trying to access the
	// inner class, Particle, in the Particle class.
	// This is due to the fact that we have to inherit/extends both PApplet and
	// VerletParticle2D

	// extendingPApplet outside = new extendingPApplet();

	private final int bg = 239;
	private int once = 0;
	private int NUM_PARTICLES = 10;
	private float d = 40;
	private float r = 20;
	private float inMixDivider = 2;

	private boolean bubblePress; // to know when a bubble is pressed
	private boolean mReleased;
	// boolean bubblePressAgain; //when pressing the bubble to zoom out

	private float z = 0; // need 3D for describing the nature of zooming
	private final float maxZ = 300;
	private final float minZ = z;
	private FloatList averageNoise;
	private float totalNoise, avgNoise, lastCalTime, threshold;
	private boolean worldIsZoomed = false;
	private float zoomFactor = 1;
	private PVector zoomCenter = new PVector(0, 0);

	private float newMouseX, newMouseY;

	private VerletPhysics2D physics;
	private VerletPhysics2D physics2;// for the static particles, so they dont get cleaned up when bubbles are
	// removed
	private AttractionBehavior2D mouseAttractor;
	private ControlP5 cp5;
	// VerletParticle2D currentClickedParticle;
	private Particle currentClickedParticle;

	private Vec2D mousePos;
	private Minim minim;
	private AudioInput in;
	private FFT fft;
	// Vec2D zoomCenter;

	private controlP5.Textfield textfield;
	private controlP5.Button clearBubbleButton;

	private PFont myFont;

	// Particle p = new Particle(Vec2D.randomVector().scale(5).addSelf(0,
	// -height/2),d, input)

	// if(input != null){
	// Particle p = new Particle(Vec2D.randomVector().scale(5).addSelf(0,
	// -height/2),d, input);

	// //Particle p = new Particle(Vec2D.randomVector(),d);
	// physics.addParticle(p);
	// // add a negative attraction force field around the new particle
	// physics.addBehavior(new AttractionBehavior2D(p, p.diameter * 6, -1.2f,
	// 0.1f));
	// }

}
