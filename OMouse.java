/**
	* This program works as a template for other mouse programs

	* @author Ofek Gila
	* @since October 16th, 2014
	* @version April 23rd, 2019
*/

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

class OMouse extends Robot {
	public static final int SCREEN_WIDTH = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	public static final int SCREEN_HEIGHT = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();

	public static final Point NOT_FOUND_POINT = new Point(-1, -1);

	public OMouse() throws AWTException {}

	public void mouseMoveDelta(int dx, int dy) {	// moves the mouse by an increment
		mouseMove(getMouseX() + dx, getMouseY() + dy);
	}

	public void mouseMoveDeltaAnimation(int dx, int dy, int pixelsPerJump, long delay) {
		mouseMoveAnimation(getMouseX() + dx, getMouseY() + dy, pixelsPerJump, delay);
	}

	public void mouseMoveAnimation(int x, int y, int pixelsPerJump, long delay) {
		new Timer("Mouse Move Animation: (" + x + ", " + y + ")",
		          false).schedule(new Animousion(x, y, pixelsPerJump), delay);
	}

	public int getMouseX() {
		return (int)MouseInfo.getPointerInfo().getLocation().getX();
	}

	public int getMouseY() {
		return (int)MouseInfo.getPointerInfo().getLocation().getY();
	}

	public BufferedImage getScreen(int startX, int startY, int width, int height) {
		return createScreenCapture(new Rectangle(startX, startY, width, height));
	}

	public BufferedImage getScreen() {
		return getScreen(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
	}

	private static Point findImageDifference(BufferedImage image1, BufferedImage image2,
	                                         int startX, int startY, int width, int height) {
		int endX = startX + width;
		int endY = startY + height;

		for (int x = startX; x < endX; ++x)
			for (int y = startY; y < endY; ++y)
				if (image1.getRGB(x, y) != image2.getRGB(x, y))
					return new Point(x, y);

		return NOT_FOUND_POINT;
	}

	private static Point findImageDifference(BufferedImage image1, BufferedImage image2) {
		int minX = Math.max(image1.getMinX(), image2.getMinX());
		int minY = Math.max(image1.getMinY(), image2.getMinY());
		int maxX = Math.min(image1.getMinX() + image1.getWidth(),
		                    image2.getMinX() + image2.getWidth());
		int maxY = Math.min(image1.getMinY() + image1.getHeight(),
		                    image2.getMinY() + image2.getHeight());

		return findImageDifference(image1, image2, minX, minY, maxX - minX, maxY - minY);
	}

	private Point findImageInImage(BufferedImage outer, BufferedImage inner,
	                          int startX, int startY, int width, int height) {
		int imageWidth = inner.getWidth();
		int imageHeight = inner.getHeight();
		int endX = startX + width;
		int endY = startY + height;

		for (int x = startX; x < endX - imageWidth; ++x)
			for (int y = startY; y < endY - imageHeight; ++y)
				if (findImageDifference(outer, inner, x, y, imageWidth, imageHeight) != NOT_FOUND_POINT)
					return new Point(x, y);

		return NOT_FOUND_POINT;
	}

	private Point findImageInImage(BufferedImage outer, BufferedImage inner) {
		return findImageInImage(outer, inner, outer.getMinX(), outer.getMinY(),
		                        outer.getWidth(), outer.getHeight());
	}

	public Point findImageInScreen(BufferedImage image) {
		return findImageInImage(image, getScreen());
	}

	public Point findImageInScreen(BufferedImage image, int startX, int startY,
	                               int width, int height) {
		return findImageInImage(image, getScreen(), startX, startY, width, height);
	}

	public void typeCharacter(char c) {
		if (c >= 'A' && c <= 'Z')
			typeShiftCharacter(c);
		else if (c >= 'a' && c <= 'z') {
			keyPress(c - 'a' + 'A');
			keyRelease(c -'a' + 'A');
		}
		else switch (c) {
			case '~':	typeShiftCharacter('`');	break;
			case '!':	typeShiftCharacter('1');	break;
			case '@':	typeShiftCharacter('2');	break;
			case '#':	typeShiftCharacter('3');	break;
			case '$':	typeShiftCharacter('4');	break;
			case '%':	typeShiftCharacter('5');	break;
			case '^':	typeShiftCharacter('6');	break;
			case '&':	typeShiftCharacter('7');	break;
			case '*':	typeShiftCharacter('8');	break;
			case '(':	typeShiftCharacter('9');	break;
			case ')':	typeShiftCharacter('0');	break;
			case '_':	typeShiftCharacter('-');	break;
			case '+':	typeShiftCharacter('=');	break;
			case '{':	typeShiftCharacter('[');	break;
			case '}':	typeShiftCharacter(']');	break;
			case '|':	typeShiftCharacter('\\');	break;
			case ':':	typeShiftCharacter(';');	break;
			case '"':	typeShiftCharacter('\'');	break;
			case '<':	typeShiftCharacter(',');	break;
			case '>':	typeShiftCharacter('.');	break;
			case '?':	typeShiftCharacter('/');	break;
			default: {
				keyPress(c);
				keyRelease(c);
			}
		}
	}

	void typeShiftCharacter(char c) {
		keyPress(KeyEvent.VK_SHIFT);
		keyPress(c);
		keyRelease(c);
		keyRelease(KeyEvent.VK_SHIFT);
	}

	void typeString(String str) {
		for (int i = 0; i < str.length(); ++i)
			typeCharacter(str.charAt(i));
	}

	void typeStringAnimation(String str) {
		typeStringAnimation(str, 30);
	}

	void typeStringAnimation(String str, long delay) {
		new Timer("String Animation: " + str, false).schedule(new Strination(str), delay);
	}

	void mouseClick(int buttons) {
		mousePress(buttons);
		mouseRelease(buttons);
	}

	void typeKey(int keycode) {
		keyPress(keycode);
		keyRelease(keycode);
	}

	class Animousion extends TimerTask {
		private final int destX, destY;
		private final int pixelsPerJump;

		public Animousion(int destX, int destY, int pixelsPerJump) {
			this.destX = destX;
			this.destY = destY;
			this.pixelsPerJump = pixelsPerJump;
		}

		@Override
		public void run() {
			int currX = getMouseX();
			int currY = getMouseY();

			int dx = destX - currX;
			int dy = destY - currY;

			double magnitude = Math.sqrt(dx * dx + dy * dy);

			int nextX = currX + (int)(pixelsPerJump * dx / magnitude);
			int nextY = currY + (int)(pixelsPerJump * dy / magnitude);

			mouseMove(nextX, nextY);

			if (nextX == destX && nextY == destY)
				cancel();
		}
	}

	class Strination extends TimerTask {
		private final String anistring;

		private int index;

		public Strination(String toAnimate) {
			anistring = toAnimate;
			index = 0;
		}

		@Override
		public void run() {
			typeCharacter(anistring.charAt(index++));

			if (index == anistring.length())
				cancel();
		}
	}
}