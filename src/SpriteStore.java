
/* SpriteStore.java
 * Manages the sprites in the game.  
 * Caches them for future use.
 */

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class SpriteStore {

	// one instance of this class will exist
	// this instance will be accessed by Game.java
	private static SpriteStore single = new SpriteStore();
	private HashMap sprites = new HashMap(); // key,value pairs that stores
												// the three sprites (alien, ship, shot)

	// returns the single instance of this class
	public static SpriteStore get() {
		return single;
	} // get

	/*
	 * getSprite input: a string specifying which sprite image is required output: a
	 * sprite instance containing an accelerated image of the requested image
	 * purpose: to return a specific sprite
	 */
	public Sprite getSprite(String ref) {

		// if the sprite is already in the HashMap
		// then return it
		// Note:
		if (sprites.get(ref) != null) {
			return (Sprite) sprites.get(ref);
		} // if

		// else, load the image into the HashMap off the
		// hard drive (and hence, into memory)

		BufferedImage sourceImage = null;

		try {
			// get the image location
			URL url = this.getClass().getClassLoader().getResource(ref);
			if (url == null) {
				System.out.println("Failed to load: " + ref);
				System.exit(0); // exit program if file not found
			}
			sourceImage = ImageIO.read(url); // get image
		} catch (IOException e) {
			System.out.println("Failed to load: " + ref);
			System.exit(0); // exit program if file not loaded
		} // catch

		// create an accelerated image (correct size) to store our sprite in
		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
				.getDefaultConfiguration();
		Image image = gc.createCompatibleImage(sourceImage.getWidth(), sourceImage.getHeight(), Transparency.BITMASK);

		// draw our source image into the accelerated image
		image.getGraphics().drawImage(sourceImage, 0, 0, null);

		// create a sprite, add it to the cache and return it
		Sprite sprite = new Sprite(image);
		sprites.put(ref, sprite);

		return sprite;
	} // getSprite

} // SpriteStore