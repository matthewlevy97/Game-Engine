package utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Sprites {
	
	public static BufferedImage loadSpriteSheet(String file) throws FileNotFoundException, IOException {
		return ImageIO.read(new FileInputStream(new File(file)));
	}
	
	public static BufferedImage[] getSpriteStrip(BufferedImage img, int row, int col, int length, int spriteWidth, int spriteHeight) {
		BufferedImage[] images = new BufferedImage[length];
		
		length += col;
		row *= spriteHeight;
		
		int c = 0;
		for(int i = col; c < length; i++) {
			images[c++] = img.getSubimage(i * spriteWidth, row, spriteWidth, spriteHeight);
		}
		
		return images;
	}
}
