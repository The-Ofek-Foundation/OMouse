/*	Ofek Gila
	October 16th, 2014
	OMouse.java
	This program works as a template for other mouse programs
*/
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.util.ArrayList;

class OMouse implements ActionListener	{
	Robot mouse;
	Point point;
	Timer animousion;
	Timer mousation;
	Timer anistrintion;
	int x, y, xt, yt;
	double r;
	boolean moving;
	boolean screenChanged;
	int changeX, changeY;
	boolean stoppedMoving;
	int width;
	int height;
	int animouse;
	int anistr;
	String anistring;
	int mbx, mby, mex, mey;
	BufferedImage Screen, Screent;
	BufferedImage image;
	int imageStartX, imageStartY, imageEndX, imageEndY;
	double startTime;
	boolean detectScreen;
	JFrame Loading;
	ArrayList<BufferedImage> images;

	OMouse()	{
		try {
			mouse = new Robot();        
		} catch (AWTException e) {
            e.printStackTrace();
        }
        width = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        mousation = new Timer(100, this);
        detectScreen = false;
        images= new ArrayList<BufferedImage>();
        animousion = new Timer(10, new Animousion());
        anistrintion = new Timer(30, new Strination());
	}
	void mouseMoveInc(int xinc, int yinc)	{	// moves the mouse by an increment
		x += xinc;
		y += yinc;
		mouse.mouseMove(x, y);
	}
	void mouseMoveIncAnimation(int xinc, int yinc)	{	// moves the mouse by an increment
		mouseMoveIncAnimation(xinc, yinc, 10);
	}
	void mouseMoveIncAnimation(int xinc, int yinc, int delay)	{
		int x = this.x + xinc;
		int y = this.y + yinc;
		mouseMoveAnimation(x, y, delay);
	}
	void mouseMove(int x, int y)	{	// moves mouse to a specific xy location
		this.x = x;
		this.y = y;
		mouse.mouseMove(x, y);
	}
	void mouseMoveAnimation(int x, int y)	{
		mouseMoveAnimation(x, y, 10);
	}
	void startTime()	{
		startTime = System.nanoTime();
	}
	double elapsedTime()	{
		return (System.nanoTime() - startTime)/1E9;
	}
	void mouseMoveAnimation(int x, int y, int delay)	{
		animouse = 1;
		mbx = getX();
		mby = getY();
		mex = x;
		mey = y;
		if (mex < 0)	mex = 0;
		if (mex > width)	mex = width;
		if (mey < 0)	mey = 0;
		if (mey > height)	mey = height;
		animousion.setDelay(delay);
		while (animousion.isRunning())	animousion.stop();
		animousion.start();
	}
	int getX()	{
		return (int)MouseInfo.getPointerInfo().getLocation().getX();
	}
	int getY()	{
		return (int)MouseInfo.getPointerInfo().getLocation().getY();
	}
	int ran(int min, int max)	{	// generates a random number between a min and max 
		int diff = max - min;
		return (int)(Math.random() * diff) + min;
	}
	public void actionPerformed(ActionEvent evt)	{
		computeActions();
	}
	void computeActions()	{
		point = MouseInfo.getPointerInfo().getLocation();
		xt = (int)point.getX();	// x and y values of mouse
		yt = (int)point.getY();
		if (detectScreen)	Screent = getScreen();
		if (detectScreen)	if (Screen != null)	screenChanged = screenChanged();
		r = Math.sqrt(Math.pow(xt-x, 2) + Math.pow(yt-y, 2));	// distance mouse moved in the last 100 milliseconds
		x = xt;	// replace old xy values of mouse
		y = yt;
		if (detectScreen)	Screen = Screent;
		if (moving && r == 0)	stoppedMoving = true;	// checks if mouse stopped moving
		else stoppedMoving = false;
		if (r > 0)	moving = true;	// checks if mouse is moving
		else moving = false;
		if (moving && animousion.isRunning())	{
			mbx = x;
			mby = y;
			animouse = 1;
		}
	}
	void computeMouseActions()	{
		point = MouseInfo.getPointerInfo().getLocation();
		xt = (int)point.getX();	// x and y values of mouse
		yt = (int)point.getY();
		r = Math.sqrt(Math.pow(xt-x, 2) + Math.pow(yt-y, 2));	// distance mouse moved in the last 100 milliseconds
		x = xt;	// replace old xy values of mouse
		y = yt;
		if (moving && r == 0)	stoppedMoving = true;	// checks if mouse stopped moving
		else stoppedMoving = false;
		if (r > 0)	moving = true;	// checks if mouse is moving
		else moving = false;
		if (moving && animousion.isRunning())	{
			mbx = x;
			mby = y;
			animouse = 1;
		}
	}
	BufferedImage getScreen(int startX, int startY, int width, int height)	{
		return mouse.createScreenCapture(new Rectangle(startX, startY, width, height));
	}
	BufferedImage getScreen()	{
		return getScreen(0, 0, width, height);
	}
	boolean screenChanged(int startX, int startY, int endX, int endY)	{
		for (int i = startX; i < endX; i++)
			for (int a = startY; a < endY; a++)
				if (Screen.getRGB(i, a) != Screent.getRGB(i, a))	{
					changeX = i;
					changeY = a;
					return true;
				}
		return false;
	}
	boolean screenChanged()	{
		return screenChanged(0, 0, width, height);
	}
	void addImage(String imageloc)	{
		try {
			image = ImageIO.read(this.getClass().getResource(imageloc));
		}	catch(Exception e)	{	
			System.err.println("File Not Found:\t" + imageloc);
			System.exit(5);
		}
		images.add(image);
	}
	void addImage(BufferedImage image)	{
		images.add(image);
	}
	BufferedImage getImage(int index)	{
		return images.get(index);
	}
	boolean imageInImage(BufferedImage image, BufferedImage Screen)	{
		for (int i = 0; i < width - image.getWidth(); i++)
			for (int a = 0; a < height - image.getHeight(); a++)
				if (imageHere(i, a, image, Screen))	{
					imageStartX = i;
					imageEndX = i + image.getWidth();
					imageStartY = a;
					imageEndY = a + image.getHeight();
					return true;
				}
		return false;
	}
	boolean imageInScreen()	{
		return imageInImage(images.get(images.size()-1), getScreen());
	}
	boolean imageInScreen(int index)	{
		return imageInImage(images.get(index), getScreen());
	}
	boolean imageInScreen(int index, int startX, int startY, int endX, int endY)	{
		return imageInImage(images.get(index), getScreen(startX, startY, endX, endY));
	}
	boolean imageInScreen(String imageloc)	{
		return imageInImage(imageloc, getScreen());
	}
	boolean imageInImage(int index, BufferedImage Screen)	{
		return imageInImage(images.get(index), Screen);
	}
	boolean imageInImage(int index1, int index2)	{
		return imageInImage(images.get(index1), images.get(index2));
	}
	boolean imageInImage(String imageloc, BufferedImage Screen)	{
		try {
			image = ImageIO.read(this.getClass().getResource(imageloc));
		}	catch(Exception e)	{	
			System.err.println("File Not Found:\t" + imageloc);
			System.exit(5);
		}
		return imageInImage(image, Screen);
	}
	boolean imageInScreen(String imageloc, int startX, int startY, int endX, int endY)	{
		return imageInImage(imageloc, getScreen(startX, startY, endX, endY));
	}
	boolean imageHere(int x, int y, BufferedImage image, BufferedImage Screen)	{
		for (int i = 0; i < image.getWidth(); i++)
			for (int a = 0; a < image.getHeight(); a++)
				if ((image.getRGB(i, a) != Screen.getRGB(i+x, a+y)) && image.getRGB(i, a) != 0)
					return false;
		return true;
	}
	class Animousion implements ActionListener	{
		public void actionPerformed(ActionEvent e)	{
			computeMouseActions();
			int dx = mex - mbx;
			int dy = mey - mby;
			double divides = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
			x = (int)(dx / divides * animouse) + mbx;
			y = (int)(dy / divides * animouse) + mby;
			mouse.mouseMove(x, y);
			if (x == mex && y == mey)	animousion.stop();
			animouse++;
		}
	}
	class Strination implements ActionListener	{
		public void actionPerformed(ActionEvent e)	{
			pressCharacter(anistring.charAt(anistr));
			anistr++;
			if (anistr == anistring.length())
				anistrintion.stop();
		}
	}
	void printCoords()	{
		System.out.println("X:\t" + x + "\tY:\t" + y);
	}
	void pressCharacter(char c)	{
		if (c >= 'A' && c <= 'Z')	pressShiftCharacter(c);
		else if (c >= 'a' && c <= 'z')	{
			mouse.keyPress(c - 'a' + 'A');
			mouse.keyRelease(c-'a' + 'A');
		}
		else switch (c)	{
			case '~':	pressShiftCharacter('`');	break;
			case '!':	pressShiftCharacter('1');	break;
			case '@':	pressShiftCharacter('2');	break;
			case '#':	pressShiftCharacter('3');	break;
			case '$':	pressShiftCharacter('4');	break;
			case '%':	pressShiftCharacter('5');	break;
			case '^':	pressShiftCharacter('6');	break;
			case '&':	pressShiftCharacter('7');	break;
			case '*':	pressShiftCharacter('8');	break;
			case '(':	pressShiftCharacter('9');	break;
			case ')':	pressShiftCharacter('0');	break;
			case '_':	pressShiftCharacter('-');	break;
			case '+':	pressShiftCharacter('=');	break;
			case '{':	pressShiftCharacter('[');	break;
			case '}':	pressShiftCharacter(']');	break;
			case '|':	pressShiftCharacter('\\');	break;
			case ':':	pressShiftCharacter(';');	break;
			case '"':	pressShiftCharacter('\'');	break;
			case '<':	pressShiftCharacter(',');	break;
			case '>':	pressShiftCharacter('.');	break;
			case '?':	pressShiftCharacter('/');	break;
			default:	{
				mouse.keyPress(c);
				mouse.keyRelease(c);
			}
		}
	}
	void pressShiftCharacter(char c)	{
		mouse.keyPress(16);	//shift
		mouse.keyPress(c);
		mouse.keyRelease(c);
		mouse.keyRelease(16);
	}
	void typeString(String str)	{
		anistring = str;
		for (int i = 0; i < str.length(); i++)
			pressCharacter(str.charAt(i));
	}
	void typeStringAnimation(String str)	{
		typeStringAnimation(str, 30);
	}
	void typeStringAnimation(String str, int delay)	{
		anistr = 0;
		anistring = str;
		anistrintion.setDelay(delay);
		while (anistrintion.isRunning())	anistrintion.stop();
		anistrintion.start();
	}
	void releaseCharacter(char c)	{
		mouse.keyRelease(c);
	}
	void mouseClick(int clicktype)	{
		mouse.mousePress(clicktype);
    	mouse.mouseRelease(clicktype);
	}
	void keyClick(int clicktype)	{
		mouse.keyPress(clicktype);
    	mouse.keyRelease(clicktype);
	}
	int left_click = 16;
	int backspace = 8;
	int right_click = 4096;
}