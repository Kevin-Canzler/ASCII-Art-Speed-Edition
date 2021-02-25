import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Main {
	public static void main(String[] args) {
		String path = "<PathToImage>";
		writeImage(makeItASCII(readImage(path), 1), path);
	}
	
	//Creates the threads that processes a BufferedImage object and returns a String array
	public static String[] makeItASCII(BufferedImage Original, int Threads) {
		int Height = Original.getHeight(); String[] characterMap = new String[Height]; ImageThread ThreadArray[] = new ImageThread[Threads];
		if(Height % Threads == 0) {
			for(int i = 0; i < Threads; i++) {
				int[] workDimensions = new int[2];
				workDimensions[0] = (Height / Threads) * i;
				workDimensions[1] = (Height / Threads) * (i + 1);
				ThreadArray[i] = new ImageThread(Original, characterMap, workDimensions);
				ThreadArray[i].start();
			}
		} else {
			int tempHeight = Height - (Height % Threads);
			int[] tempWorkDimensions = new int[2];
			tempWorkDimensions[0] = (tempHeight / Threads) * (Threads - 1); tempWorkDimensions[1] = Height;
			ThreadArray[Threads - 1] = new ImageThread(Original, characterMap, tempWorkDimensions);
			ThreadArray[Threads - 1].start();
			for(int i = 0; i < Threads - 1; i++){
				int[] workDimensions = new int[2];
				workDimensions[0] = (tempHeight / Threads) * i;
				workDimensions[1] = (tempHeight / Threads) * (i + 1);
				ThreadArray[i] = new ImageThread(Original, characterMap, workDimensions);
				ThreadArray[i].start();
			}
		}
		
		for(int i = 0; i < Threads; i++) {
			try {
				ThreadArray[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return characterMap;
	}
	
	//Reads the file and returns a BufferedImage object
	public static BufferedImage readImage(String path) {
		BufferedImage readImage = null;
		File Image = new File(path);
		try {
			ImageIO.setUseCache(false);
			readImage = ImageIO.read(Image);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return readImage;
	}
	
	//Writes the ASCIIified image to a text file in the same directory
	public static void writeImage(String[] characters, String path) {
		FileWriter temp = null;
		try {
			temp = new FileWriter(new File(new File(path).getParent() + "\\output.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedWriter writer = new BufferedWriter(temp);
		for(int i = 0; i < characters.length; i++) {
			try {
				writer.write(characters[i], 0, 1024);
				writer.newLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
