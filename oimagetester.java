import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.FileInputStream;
import java.util.Timer;
import javax.imageio.ImageIO;

public class oimagetester implements OImageListener, OMouseListener {

	Timer timer;
	OMouse omouse;
	OImageFinder finder;

	public oimagetester() throws AWTException, IOException {
		omouse = new OMouse(this);
		timer = new Timer("oimagetester", false);
		finder = new OImageFinder(this, 100, 200, 1200, 800);
		finder.addImages(getImage("/home/ofekih/Pictures/Selection_003.png"));
		finder.addImages(getImage("/home/ofekih/Pictures/Selection_004.png"));
		finder.addImages(getImage("/home/ofekih/Pictures/Selection_006.png"));
		timer.scheduleAtFixedRate(finder, 0, 1000);
	}

	public static BufferedImage getImage(String imageloc) throws IOException {
		return ImageIO.read(new FileInputStream(imageloc));
	}

	public static void main(String... pumpkins) throws AWTException, IOException {
		oimagetester tester = new oimagetester();
	}

	@Override
	public void onImageFound(Rectangle foundRect, BufferedImage foundImage) {
		System.out.println("Image Found " + foundRect);
		omouse.mouseMoveCenterDuration(foundRect, 10l, 100l);
	}

	@Override
	public void onMouseMoveAnimationEnd(long animationId) {
		omouse.mouseLeftClick();
		omouse.charType('z');
	}

	@Override
	public void onImageNotFound() {
		System.out.println("Not Found");
	}
}