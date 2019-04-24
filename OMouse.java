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
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

class OMouse extends Robot {
	public static final long DEFAULT_ANIMATION_DELAY = 0l;
	private OMouseListener listener;

	private static long animationIdIndex = 0l;


	public OMouse() throws AWTException {}
	public OMouse(OMouseListener listener) throws AWTException {
		this.listener = listener;
	}

	public Rectangle getScreenRectangle() {
		Dimension screenDims = Toolkit.getDefaultToolkit().getScreenSize();
		return new Rectangle(0, 0, (int)screenDims.getWidth(), (int)screenDims.getHeight());
	}

	public void mouseMove(Point p) {
		mouseMove((int)p.getX(), (int)p.getY());
	}

	public void mouseMoveCenter(Rectangle r) {
		mouseMove((int)(r.getX() + r.getWidth() / 2), (int)(r.getY() + r.getHeight() / 2));
	}

	public void mouseMoveDelta(int dx, int dy) { // moves the mouse by an increment
		mouseMove(getMouseX() + dx, getMouseY() + dy);
	}

	public long mouseMoveDeltaAnimation(int dx, int dy, int pixelsPerJump, long delay, long period) {
		return mouseMoveAnimation(getMouseX() + dx, getMouseY() + dy, pixelsPerJump, delay, period);
	}

	public long mouseMoveDeltaAnimation(int dx, int dy, int pixelsPerJump, long period) {
		return mouseMoveAnimation(getMouseX() + dx, getMouseY() + dy, pixelsPerJump, period);
	}

	public long mouseMoveAnimation(int x, int y, int pixelsPerJump, long delay, long period) {
		long animationId = getNextAnimationId();
		new Timer("Mouse Move Animation: (" + x + ", " + y + ")",
		          false).scheduleAtFixedRate(new Animousion(x, y, pixelsPerJump, animationId),
		          delay, period);
		return animationId;
	}

	public long mouseMoveAnimation(int x, int y, int pixelsPerJump, long period) {
		return mouseMoveAnimation(x, y, pixelsPerJump, DEFAULT_ANIMATION_DELAY, period);
	}

	public long mouseMoveAnimation(Point p, int pixelsPerJump, long delay, long period) {
		return mouseMoveAnimation((int)p.getX(), (int)p.getY(), pixelsPerJump, delay, period);
	}

	public long mouseMoveAnimation(Point p, int pixelsPerJump, long period) {
		return mouseMoveAnimation((int)p.getX(), (int)p.getY(), pixelsPerJump, period);
	}

	public long mouseMoveCenterAnimation(Rectangle r, int pixelsPerJump, long delay, long period) {
		return mouseMoveAnimation((int)(r.getX() + r.getWidth() / 2),
		                          (int)(r.getY() + r.getHeight() / 2),
		                          pixelsPerJump, delay, period);
	}

	public long mouseMoveCenterAnimation(Rectangle r, int pixelsPerJump, long period) {
		return mouseMoveAnimation((int)(r.getX() + r.getWidth() / 2),
		                          (int)(r.getY() + r.getHeight() / 2),
		                          pixelsPerJump, period);
	}

	public long mouseMoveDuration(int x, int y, long delay, long period, long duration) {
		long animationId = getNextAnimationId();
		new Timer("Mouse Move Duration: (" + x + ", " + y + ")",
		          false).scheduleAtFixedRate(new AnimousionDuration(x, y, duration / period, animationId),
		          delay, period);
		return animationId;
	}

	public long mouseMoveDuration(int x, int y, long period, long duration) {
		return mouseMoveDuration(x, y, DEFAULT_ANIMATION_DELAY, period, duration);
	}

	public long mouseMoveDuration(Point p, long delay, long period, long duration) {
		return mouseMoveDuration((int)p.getX(), (int)p.getY(), delay, period, duration);
	}

	public long mouseMoveDuration(Point p, long period, long duration) {
		return mouseMoveDuration((int)p.getX(), (int)p.getY(), period, duration);
	}

	public long mouseMoveCenterDuration(Rectangle r, long delay, long period, long duration) {
		return mouseMoveDuration((int)(r.getX() + r.getWidth() / 2),
		                         (int)(r.getY() + r.getHeight() / 2),
		                         delay, period, duration);
	}

	public long mouseMoveCenterDuration(Rectangle r, long period, long duration) {
		return mouseMoveDuration((int)(r.getX() + r.getWidth() / 2),
		                         (int)(r.getY() + r.getHeight() / 2),
		                         period, duration);
	}

	public long mouseMoveDeltaDuration(int dx, int dy, long delay, long period, long duration) {
		return mouseMoveDuration(getMouseX() + dx, getMouseY() + dy, delay, period, duration);
	}

	public long mouseMoveDeltaDuration(int dx, int dy, long period, long duration) {
		return mouseMoveDuration(getMouseX() + dx, getMouseY() + dy, period, duration);
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

	public void charType(char c) {
		if (c >= 'A' && c <= 'Z')
			shiftCharType(c);
		else if (c >= 'a' && c <= 'z') {
			keyPress(c - 'a' + 'A');
			keyRelease(c -'a' + 'A');
		} else switch (c) {
			case '~':	shiftCharType('`');	break;
			case '!':	shiftCharType('1');	break;
			case '@':	shiftCharType('2');	break;
			case '#':	shiftCharType('3');	break;
			case '$':	shiftCharType('4');	break;
			case '%':	shiftCharType('5');	break;
			case '^':	shiftCharType('6');	break;
			case '&':	shiftCharType('7');	break;
			case '*':	shiftCharType('8');	break;
			case '(':	shiftCharType('9');	break;
			case ')':	shiftCharType('0');	break;
			case '_':	shiftCharType('-');	break;
			case '+':	shiftCharType('=');	break;
			case '{':	shiftCharType('[');	break;
			case '}':	shiftCharType(']');	break;
			case '|':	shiftCharType('\\');	break;
			case ':':	shiftCharType(';');	break;
			case '"':	shiftCharType('\'');	break;
			case '<':	shiftCharType(',');	break;
			case '>':	shiftCharType('.');	break;
			case '?':	shiftCharType('/');	break;
			default: {
				keyPress(c);
				keyRelease(c);
			}
		}
	}

	private void shiftCharType(char c) {
		keyPress(KeyEvent.VK_SHIFT);
		keyPress(c);
		keyRelease(c);
		keyRelease(KeyEvent.VK_SHIFT);
	}

	public void typeString(String str) {
		for (int i = 0; i < str.length(); ++i)
			charType(str.charAt(i));
	}

	public long stringTypeAnimation(String str, long delay, long period) {
		long animationId = getNextAnimationId();
		new Timer("String Animation: " + str, false).scheduleAtFixedRate(
		          new Strination(str, animationId), delay, period);
		return animationId;
	}

	public long stringTypeAnimation(String str, long period) {
		return stringTypeAnimation(str, DEFAULT_ANIMATION_DELAY, period);
	}

	public void mouseClick(int buttons) {
		mousePress(buttons);
		mouseRelease(buttons);
	}

	public void mouseLeftClick() {
		mouseClick(InputEvent.BUTTON1_DOWN_MASK);
	}

	public void mouseMiddleClick() {
		mouseClick(InputEvent.BUTTON2_DOWN_MASK);
	}

	public void mouseRightClick() {
		mouseClick(InputEvent.BUTTON3_DOWN_MASK);
	}

	public void keyType(int keycode) {
		keyPress(keycode);
		keyRelease(keycode);
	}

	// unique ids, thread safe
	private static synchronized long getNextAnimationId() {
		return animationIdIndex++;
	}

	private int roundUp(double d) {
		if (d == (int)d) {
			return (int)d;
		}

		return d > 0.0 ? (int)(d + 1.0) : (int)(d - 1.0);
	}

	private class Animousion extends TimerTask {
		private final int destX, destY;
		private final int pixelsPerJump;
		private final long id;

		private boolean init;

		public Animousion(int destX, int destY, int pixelsPerJump, long id) {
			this.destX = destX;
			this.destY = destY;
			this.pixelsPerJump = pixelsPerJump;
			this.id = id;
			this.init = true;
		}

		@Override
		public void run() {
			if (init && listener != null) {
				listener.onAnimationStart(id);
				listener.onMouseMoveAnimationStart(id);
			}

			init = false;


			int currX = getMouseX();
			int currY = getMouseY();

			int dx = destX - currX;
			int dy = destY - currY;

			double magnitude = Math.sqrt(dx * dx + dy * dy);

			int nextX = currX + roundUp(pixelsPerJump * dx / magnitude);
			int nextY = currY + roundUp(pixelsPerJump * dy / magnitude);

			mouseMove(nextX, nextY);

			if (nextX == destX && nextY == destY) {
				cancel();

				if (listener != null) {
					listener.onAnimationEnd(id);
					listener.onMouseMoveAnimationEnd(id);
				}
			}
		}
	}

	private class AnimousionDuration extends TimerTask {
		private final int destX, destY;
		private final long id;

		private long intervalsLeft;
		private boolean init;

		public AnimousionDuration(int destX, int destY, long totalIntervals, long id) {
			this.destX = destX;
			this.destY = destY;
			this.intervalsLeft = totalIntervals;
			this.id = id;
			this.init = true;
		}

		@Override
		public void run() {
			if (init && listener != null) {
				listener.onAnimationStart(id);
				listener.onMouseMoveAnimationStart(id);
			}

			init = false;


			int currX = getMouseX();
			int currY = getMouseY();

			int dx = destX - currX;
			int dy = destY - currY;

			int nextX = currX + roundUp(dx * 1.0 / intervalsLeft);
			int nextY = currY + roundUp(dy * 1.0 / intervalsLeft);

			mouseMove(nextX, nextY);

			--intervalsLeft;

			System.out.printf("Next: (%d, %d), Dest: (%d, %d)\n",
			                   nextX, nextY, destX, destY);

			if ((nextX == destX && nextY == destY) || intervalsLeft == 0) {
				cancel();

				if (listener != null) {
					listener.onAnimationEnd(id);
					listener.onMouseMoveAnimationEnd(id);
				}
			}
		}
	}

	private class Strination extends TimerTask {
		private final String anistring;
		private final long id;

		private int index;
		private boolean init;

		public Strination(String toAnimate, long id) {
			anistring = toAnimate;
			index = 0;
			this.id = id;
			this.init = true;
		}

		@Override
		public void run() {
			if (init && listener != null) {
				listener.onAnimationStart(id);
				listener.onStringAnimationStart(id);
			}

			init = false;


			charType(anistring.charAt(index++));

			if (index == anistring.length()) {
				cancel();

				if (listener != null) {
					listener.onAnimationEnd(id);
					listener.onStringAnimationEnd(id);
				}
			}
		}
	}
}