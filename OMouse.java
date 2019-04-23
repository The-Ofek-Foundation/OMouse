/**
 * This program works as a template for other mouse programs
 *
 * @author Ofek Gila
 * @since October 16th, 2014
 * @version April 23rd, 2019
*/

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

class OMouse extends Robot {
	public OMouse() throws AWTException {}

	public Rectangle getScreenRectangle() {
		Dimension screenDims = Toolkit.getDefaultToolkit().getScreenSize();
		return new Rectangle(0, 0, (int)screenDims.getWidth(), (int)screenDims.getHeight());
	}

	public void mouseMoveDelta(int dx, int dy) { // moves the mouse by an increment
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

	public BufferedImage createScreenCapture(int startX, int startY, int width, int height) {
		return createScreenCapture(new Rectangle(startX, startY, width, height));
	}


	public BufferedImage createScreenCapture() {
		return createScreenCapture(getScreenRectangle());
	}

	public void typeCharacter(char c) {
		if (c >= 'A' && c <= 'Z')
			typeShiftCharacter(c);
		else if (c >= 'a' && c <= 'z') {
			keyPress(c - 'a' + 'A');
			keyRelease(c -'a' + 'A');
		} else switch (c) {
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