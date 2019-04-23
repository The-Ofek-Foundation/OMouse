/**
 * Find images in the display
 *
 * @author Ofek Gila
 * @since April 23rd, 2019
 * @version April 23rd, 2019
 */

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TimerTask;

public class OImageFinder extends TimerTask {
	private final OImageListener listener;
	private final OMouse omouse;

	private static final Point NOT_FOUND_POINT = new Point(-1, -1);

	private Rectangle viewingArea;
	private ArrayList<BufferedImage> images;

	public OImageFinder(OImageListener listener, Rectangle viewingArea, BufferedImage... images)
	                    throws AWTException {
		omouse = new OMouse();
		this.listener = listener;
		setViewingArea(viewingArea);
		this.images = new ArrayList<BufferedImage>(Arrays.asList(images));
	}

	public OImageFinder(OImageListener listener, int startX, int startY, int width,
	                    int height, BufferedImage... images) throws AWTException {
		this(listener, new Rectangle(startX, startY, width, height), images);
	}

	public OImageFinder(OImageListener listener, BufferedImage... images) throws AWTException {
		this(listener, 0, 0, 0, 0, images);
		setViewingArea(omouse.getScreenRectangle());
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

	@Override
	public void run() {
		BufferedImage screenCapture = omouse.createScreenCapture(viewingArea);

		for (BufferedImage image : images) {
			Point foundLoc = findImageInImage(screenCapture, image);

			if (foundLoc != NOT_FOUND_POINT) {
				listener.onImageFound(foundLoc, image);
				return;
			}
		}

		listener.onImageNotFound();
	}

	public void setViewingArea(Rectangle viewingArea) { this.viewingArea = viewingArea; }
	public void setViewingArea(int startX, int startY, int width, int height) {
		setViewingArea(new Rectangle(startX, startY, width, height));
	}

	public void addImages(BufferedImage... images) {
		for (BufferedImage image : images)
			if (!this.images.contains(image))
				this.images.add(image);
	}

	public Rectangle getViewingArea() { return viewingArea; }
}
