import java.awt.Rectangle;
import java.awt.image.BufferedImage;

interface OImageListener {
	public void onImageFound(Rectangle foundRect, BufferedImage imageFound);
	public void onImageNotFound();
}
