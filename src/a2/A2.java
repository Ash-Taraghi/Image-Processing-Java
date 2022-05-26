package a2;

import java.net.URI;
import java.awt.Color;
import java.io.File;
import java.net.URL;

public class A2 {
	/**
	 * The original image
	 */
	private static Picture orig;

	/**
	 * The image viewer class
	 */
	private static A2Viewer viewer;

	/**
	 * Returns a 300x200 image containing the Queen's flag (without the crown).
	 * 
	 * @return an image containing the Queen's flag
	 */
	public static Picture flag() {
		Picture img = new Picture(300, 200);
		int w = img.width();
		int h = img.height();

		// set the pixels in the blue stripe
		Color blue = new Color(0, 48, 95);
		for (int col = 0; col < w / 3; col++) {
			for (int row = 0; row < h - 1; row++) {
				img.set(col, row, blue);
			}
		}

		// set the pixels in the yellow stripe
		Color yellow = new Color(255, 189, 17);
		for (int col = w / 3; col < 2 * w / 3; col++) {
			for (int row = 0; row < h - 1; row++) {
				img.set(col, row, yellow);
			}
		}

		// set the pixels in the red stripe
		Color red = new Color(185, 17, 55);
		for (int col = 2 * w / 3; col < w; col++) {
			for (int row = 0; row < h - 1; row++) {
				img.set(col, row, red);
			}
		}
		return img;
	}

	public static Picture copy(Picture p) {
		Picture result = new Picture(p.width(), p.height());
		int w = result.width();
		int h = result.height();
		for (int col = 0; col < w; col++) { // iterate through the 2d array of image and set each pixel color to p
			for (int row = 0; row < h; row++) {
				result.set(col, row, p.get(col, row));
			}
		}

		// complete the method
		return result;

	}

	// ADD YOUR METHODS HERE

	public static Picture border(Picture q, int thickness) {
		int h = q.height();
		int w = q.width();
		for (int row = 0; row < h; row++) {
			for (int col = 0; col < thickness; col++) {
				q.set(col, row, Color.BLUE);
				q.set((w - 1) - col, row, Color.BLUE);
			}
		}

		for (int col = 0; col < w; col++) {
			for (int row = 0; row < thickness; row++) {
				q.set(col, row, Color.BLUE);
				q.set(col, (h - 1) - row, Color.BLUE);
			}
		}

		return q;
	}

	public static Picture toGray(Picture p) {
		int w = p.width();
		int h = p.height();
		Picture grey = new Picture(w, h);
		for (int col = 0; col < w; col++) {
			for (int row = 0; row < h; row++) {
				int red = (p.get(col, row)).getRed();
				int green = (p.get(col, row)).getGreen();
				int blue = (p.get(col, row)).getBlue();
				int greyScale = (int) Math.round((0.2989 * red) + (0.5870 * green) + (0.1140 * blue));
				Color finalCol = new Color(greyScale, greyScale, greyScale);
				grey.set(col, row, (finalCol));
			}
		}
		return grey;
	}

	public static Picture binary(Picture p, Color c1, Color c2) {
		int w = p.width();
		int h = p.height();
		Picture binary = new Picture(w, h);
		for (int col = 0; col < w; col++) {
			for (int row = 0; row < h; row++) {
				int red = (p.get(col, row)).getRed();
				int green = (p.get(col, row)).getGreen();
				int blue = (p.get(col, row)).getBlue();
				int greyScale = (int) Math.round((0.2989 * red) + (0.5870 * green) + (0.1140 * blue));
				if (greyScale < 128) {
					Color finalCol = c1;
					binary.set(col, row, (finalCol));
				} else if (greyScale > 128) {
					Color finalCol = c2;
					binary.set(col, row, (finalCol));
				}

			}
		}
		return binary;
	}

	public static Picture flipVertical(Picture p) {
		int w = p.width();
		int h = p.height();
		Picture flip = new Picture(w, h);
		for (int row = 0; row < h; row++) {
			for (int col = 0; col < w; col++) {
				flip.set(col, row, p.get(col, (h - 1) - row));
			}
		}
		return flip;
	}

	public static Picture rotateImage(Picture p) {
		int h = p.width();
		int w = p.height();
		Picture rotate = new Picture(w, h);
		for (int row = 0; row < h; row++) {
			for (int col = 0; col < w; col++) {
				rotate.set(col, row, p.get(row, (w - 1) - col));
			}
		}
		return rotate;
	}

	public static Picture redEye(Picture copy) {
		// I noticed that for a color to be predominantly red, its R value is always >
		// than about threshold * green value
		// I played around with threshold value and found that 2.2 seems to work most
		// accuratley for both pics :)
		int w = copy.width();
		int h = copy.height();
		for (int row = 0; row < h; row++) {
			for (int col = 0; col < w; col++) {
				int red = copy.get(col, row).getRed();
				int blue = copy.get(col, row).getBlue();
				int green = copy.get(col, row).getGreen();
				if (red > ((2.2 * green))) { // used pattern noticed to identify red pixels and replace them
					copy.set(col, row, Color.BLACK);
				}
			}
		}
		return copy;
	}

	public static Picture blur(Picture p, int radius) {
		Picture result = new Picture(p.width(), p.height());
		int h = p.height();
		int w = p.width();
		int divider = (int) Math.pow(((2 * radius) + 1), 2);
		int redSum = 0;
		int greenSum = 0;
		int blueSum = 0;
		// complete the method
		for (int row = 0; row < h; row++) {
			for (int col = 0; col < w; col++) {
				redSum = 0;
				greenSum = 0;
				blueSum = 0;
				divider = (int) Math.pow(((2 * radius) + 1), 2);
				// these two for loops iterate through pixels around target pixel to get average
				// colors
				for (int row2 = row - radius; row2 <= row + radius; row2++) {
					for (int col2 = col - radius; col2 <= col + radius; col2++) {
						if ((0 <= col2 & col2 < w) & (0 <= row2 & row2 < h)) { // checking boundaries
							redSum = redSum + p.get(col2, row2).getRed();
							greenSum = greenSum + p.get(col2, row2).getGreen();
							blueSum = blueSum + p.get(col2, row2).getBlue();
						} else {
							// this else statement accounts for boxes along boundary not counted
							// this makes sure divider is total boxes counted and not just always (2n+1)^2
							divider = divider - 1;
						}
					}
				}

				Color pixCol = new Color((redSum / divider), (greenSum / divider), (blueSum / divider));
				result.set(col, row, pixCol);
			}

		}

		return result;
	}

	/**
	 * A2Viewer class calls this method when a menu item is selected. This method
	 * computes a new image and then asks the viewer to display the computed image.
	 * 
	 * @param op the operation selected in the viewer
	 */
	public static void processImage(String op) {

		switch (op) {
		case A2Viewer.FLAG:
			// create a new image by copying the original image
			Picture p = A2.flag();
			A2.viewer.setComputed(p);
			break;
		case A2Viewer.COPY:
			// create a new image by copying the original image
			p = A2.copy(A2.orig);
			A2.viewer.setComputed(p);
			break;
		case A2Viewer.BORDER_1:
			// create a new image by adding a border of width 1 to the original image
			Picture q = A2.copy(A2.orig); // making copy to add border too
			p = A2.border(q, 1);
			A2.viewer.setComputed(p);
			break;
		case A2Viewer.BORDER_5:
			// create a new image by adding a border of width 5 the original image
			q = A2.copy(A2.orig);
			p = A2.border(q, 5);
			A2.viewer.setComputed(p);

			break;
		case A2Viewer.BORDER_10:
			// create a new image by adding a border of width 10 the original image
			q = A2.copy(A2.orig);
			p = A2.border(q, 10);
			A2.viewer.setComputed(p);

			break;
		case A2Viewer.TO_GRAY:
			// create a new image by converting the original image to grayscale
			p = A2.toGray(A2.orig);
			A2.viewer.setComputed(p);

			break;
		case A2Viewer.TO_BINARY:
			// create a new image by converting the original image to black and white
			Color c1 = new Color(0, 0, 0);
			Color c2 = new Color(0, 0, 0);
			c1 = Color.BLACK;
			c2 = Color.WHITE;
			p = A2.binary(A2.orig, c1, c2);
			A2.viewer.setComputed(p);

			break;
		case A2Viewer.FLIP_VERTICAL:
			// create a new image by flipping the original image vertically
			p = A2.flipVertical(A2.orig);
			A2.viewer.setComputed(p);
			break;
		case A2Viewer.ROTATE_RIGHT:
			// create a new image by rotating the original image to the right by 90 degrees
			p = A2.rotateImage(A2.orig);
			A2.viewer.setComputed(p);
			break;
		case A2Viewer.RED_EYE:
			// create a new image by removing the redeye effect in the original image
			q = A2.copy(A2.orig);
			p = A2.redEye(q);
			A2.viewer.setComputed(p);
			break;
		case A2Viewer.BLUR_1:
			// create a new image by blurring the original image with a box blur of radius 1
			p = A2.blur(A2.orig, 1);
			A2.viewer.setComputed(p);

			break;
		case A2Viewer.BLUR_3:
			// create a new image by blurring the original image with a box blur of radius 3
			p = A2.blur(A2.orig, 3);
			A2.viewer.setComputed(p);

			break;
		case A2Viewer.BLUR_5:
			// create a new image by blurring the original image with a box blur of radius 5
			p = A2.blur(A2.orig, 5);
			A2.viewer.setComputed(p);

			break;
		default:
			// do nothing
		}
	}

	/**
	 * Starting point of the program. Students can comment/uncomment which image to
	 * use when testing their program.
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {
		A2.viewer = new A2Viewer();
		A2.viewer.setVisible(true);

		URL img;
		// uncomment one of the next two lines to choose which test image to use (person
		// or cat)
		//img = A2.class.getResource("redeye-400x300.jpg");
		 img = A2.class.getResource("cat.jpg");

		try {
			URI uri = new URI(img.toString());
			A2.orig = new Picture(new File(uri.getPath()));
			A2.viewer.setOriginal(A2.orig);
		} catch (Exception x) {
			// do nothing
		}
	}

}
