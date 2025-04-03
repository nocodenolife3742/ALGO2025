import java.util.Random;
import java.util.Vector;
import java.util.Collections;

public class ColorPalette {

	static Picture round64(Picture pic) {
		for (int i = 0; i < pic.width(); i++) {
			for (int j = 0; j < pic.height(); j++) {
				int c = pic.getRGB(i, j);
				int[] cc = new int[] { (c >> 16) & 255, (c >> 8) & 255, c & 255 };
				for (int k = 0; k < 3; k++)
					cc[k] = (int) (Math.round(cc[k] / 51.) * 51);
				pic.setRGB(i, j, (cc[0] << 16) | (cc[1] << 8) | cc[2]);
			}
		}
		return pic;
	}

	static Picture simplify(Picture pic, int maxpoints) {
		
		System.out.println("--Test of palette ... ");
		long startTime = System.currentTimeMillis();
		Random r = new Random(0);

		KDTree tree = null;
		int iter = 20000;
		for (int i = 0; i < iter; i++) {
			int row = r.nextInt(pic.height()), col = r.nextInt(pic.width());
			int c = pic.getRGB(col, row);
			// construction of the node labeled by a point which contains the 3 color coordinates
			double[] point = new double[3];
			point[0] = (c >> 16) & 255;
			point[1] = (c >> 8) & 255;
			point[2] = c & 255;
			tree = KDTree.insert(tree, point);
		}
		
		Vector<double[]> palette = KDTree.palette(tree, maxpoints);
		assert palette.size() == maxpoints: "the palette is not of the requested size";
		Collections.shuffle(palette); // to avoid degenerate trees
		tree = null;
		for (double[] c : palette)
			tree = KDTree.insert(tree, c);
		
		double[] tmp = new double[3];

		double score = 0.0;

		for (int i = 0; i < pic.width(); i++) {
			for (int j = 0; j < pic.height(); j++) {
				int c = pic.getRGB(i, j);
				tmp[0] = (c >> 16) & 255;
				tmp[1] = (c >> 8) & 255;
				tmp[2] = c & 255;
				double[] closest = KDTree.closest(tree, tmp);
				
				score += KDTree.sqDist(closest, tmp);

				pic.setRGB(i, j, ((int) closest[0] << 16) | ((int) closest[1] << 8) | (int) closest[2]);
	
			}
		}

		long endTime = System.currentTimeMillis();

		System.out.println("Simplifying the image takes " + (endTime - startTime) / 1000. + " seconds.");
		System.out.printf("The total score is %f.%n", Math.sqrt(score / pic.width() / pic.height()));
		
		return pic;
	}
	
	
	
	public static void main(String[] args) {
		//Witness Palette
		Picture orig = new Picture("photo.jpg");
		orig.show();
		
		//Palette web
		 Picture pic64 = round64(new Picture(orig));
		 pic64.show();
		 
		//Palette optimized
		 Picture pic = simplify(new Picture(orig), 256);
		 pic.show();		 
	}

}
