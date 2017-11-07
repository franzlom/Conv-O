

package ConvO;

import processing.core.*;
import processing.awt.*;
import processing.data.*;
import processing.event.*;
import processing.javafx.*;
import processing.opengl.*;
import processing.opengl.cursors.*;
import processing.opengl.shaders.*;
import toxi.geom.Vec2D;
import toxi.physics2d.VerletParticle2D;

class Particle extends VerletParticle2D {
	private final int CENTER = 3;
	public float diameter, radius;
	private final float textSize = 15;
	
	private PFont myFont;
	// just because java doesnt have color as a primitive

	// color randomC1 = color(242, 103, 103); //r
	// color randomC2 = color(162, 206, 82); //g
	// color randomC3 = color(56, 153, 213); //b

	// rgb values for the red blue and green
	float randomRed1 = 242;
	float randomRed2 = 103;
	float randomRed3 = 103;

	float randomGreen1 = 162;
	float randomGreen2 = 206;
	float randomGreen3 = 82;

	float randomBlue1 = 56;
	float randomBlue2 = 153;
	float randomBlue3 = 213;

	float thisRandomColour;

	float particleColour1;
	float particleColour2;
	float particleColour3;
	String bubbleTopic;

	boolean isZoomed;
	
	PApplet parent;
	
	/**
	 * Creates new Particle that controls its drawing
	 * @param the particle
	 * @param the diameter
	 * @param String name of the bubble
	 * @param type this
	 * 
	 */
	
	Particle(Vec2D p, float d, String name, PApplet otherP) {
		
		super(p);
		
		parent = otherP;
		diameter = d;
		bubbleTopic = name;

		thisRandomColour = (int) parent.random(1, 4);
		if (thisRandomColour == 1) {
			particleColour1 = randomRed1;
			particleColour2 = randomRed2;
			particleColour3 = randomRed3;
		} else if (thisRandomColour == 2) {
			particleColour1 = randomGreen1;
			particleColour2 = randomGreen2;
			particleColour3 = randomGreen3;
		} else if (thisRandomColour == 3) {
			particleColour1 = randomBlue1;
			particleColour2 = randomBlue2;
			particleColour3 = randomBlue3;
		}

		radius = diameter / 2;
	}
	
	public void display(){
	    //println("x = " + x);
	    //println("y = " + y);
	    //println("d = " + diameter);
	    //println("x = " + diameter);
	    //fill(175);
	    parent.noStroke();
	    parent.ellipseMode(CENTER);
	    //parent.ell
	    parent.fill(particleColour1, particleColour2, particleColour3);
	    
	    
	    parent.ellipse(x, y, diameter, diameter);
	    //println("bubbleTopic = "+ bubbleTopic);
	    //println("x = "+ x);
	    //println("y = "+ y);
	    
	    myFont = parent.createFont("CaviarDreams.tff", 20);
	    parent.fill(0);
	    parent.textAlign(PConstants.CENTER);
	    parent.textFont(myFont);
	    //parent.textSize(textSize);
	    parent.text(bubbleTopic, x, y + 5);
	  }
	  //void display(int x, int y){
	    
	  //}
	  
	 public void flashColour() {
		 parent.fill(0);
		 
		 
		// parent.fill(particleColour1, particleColour2, particleColour3);
		 
	 }
	
	  public float getD(){
	    return diameter;
	  }
	  
	  public void setD(float inD){
	    diameter = inD;	
	  }
	  
	  public void addD(float inD){
	    diameter+= inD;
	  }
	  
	  public void setBubbleName(String name){
	    bubbleTopic = name;
	  }
	  
	  public boolean isZoomed(){
	    return isZoomed;
	  }
	  
	  public void setZoom(boolean newZoomBoolean){
	    isZoomed = newZoomBoolean;
	  }
	  
	  public String getName() {
		  return bubbleTopic;
	  }
  
  /*Particle(Vec2D p, float d){
    super(p);
    diameter = d;
  }*/
  
  

}


