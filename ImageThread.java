import java.awt.image.BufferedImage;

public class ImageThread extends Thread{
	private BufferedImage Original;
	private int[] workDimensions;
	private String[] characterMap;
	public void run() {
		String[] characters = {"@", "&", "%", "#", "*", "+", "=", "-", ":", ".", " "};
		for(int y = workDimensions[0]; y < workDimensions[1]; y++) {
			int rgb = Original.getRGB(1, y);
			characterMap[y] = characters[(int) Math.round((((rgb >> 16) & 0xFF) + ((rgb >> 8 ) & 0xFF) + ((rgb) & 0xFF)) / 76.5)];
			for(int x = 1; x < Original.getWidth(); x++) {
				rgb = Original.getRGB(x, y);
				characterMap[y] += characters[(int) Math.round((((rgb >> 16) & 0xFF) + ((rgb >> 8 ) & 0xFF) + ((rgb) & 0xFF)) / 76.5)];
			}
			//System.out.println(workDimensions[0] + "-" + workDimensions[1] + " did " + y);
			//For debugging but it massively slows it down
		}
	}

	public ImageThread(BufferedImage Original, String[] characterMap, int[] workDimensions) {
		this.Original = Original; this.workDimensions = workDimensions; this.characterMap = characterMap;
	}
}
