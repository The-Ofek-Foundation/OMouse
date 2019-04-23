import java.awt.Point;
import java.awt.image.BufferedImage;

interface OImageListener {
	void onImageFound(Point loc, BufferedImage imageFound);
	void onImageNotFound();
}
