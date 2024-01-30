import java.io.File;
import java.io.PrintWriter;

/**
* Program takes in an input image, performs operations on it, and saves it to an output image
* @author Aidan Grupac
*/
public class ImagingApp{

	/**
	* the Image object that will be constructed from the contents of a file
	*/
	private static Image inputImage;
	/**
	* the Image object that operations are performed on and is saved to a file
	*/
	private static Image outputImage;
	/**
	* the CompressedImage object that is created when compress() is called
	*/
	private static CompressedImage compressedImage;

	/**
	* Method that serves as driver - takes input, performs operation accordingly, and saves
	* @param args the command line arguments input by user
	*/
	public static void main(String[] args){

		//checks if args has enough arguments
		if(args.length > 3){
			//try to create inputImage and outputImage, catch file not found error
			try{
				inputImage = new Image(args[0]);
				outputImage = inputImage.clone();
			}
			catch(Exception e){
				System.out.println("Could not find input file.");
				System.exit(0);
			}

			//perform certain operation according to arguments, save image at the end
			switch(args[2]){
				case "scale":
				if(args.length != 4){
					System.out.println("Incorrect number of parameters.");
				}
				else{
					saveImage(outputImage.scale(Integer.parseInt(args[3])), args[1]);
				}
				break;

				case "crop":
				if(args.length != 7){
					System.out.println("Incorrect number of parameters.");
				}
				else{
					int topY, topX, height, width;
					topY = Integer.parseInt(args[3]);
					topX = Integer.parseInt(args[4]);
					height = Integer.parseInt(args[5]);
					width = Integer.parseInt(args[6]);
					saveImage(outputImage.crop(topY, topX, height, width), args[1]);
				}
				break;

				case "flip":
				if(args.length != 4){
					System.out.println("Incorrect number of parameters.");
				}
				else{
					saveImage(outputImage.flip(args[3]), args[1]);
				}
				break;

				case "rotate":
				if(args.length != 4){
					System.out.println("Incorrect number of parameters.");
				}
				else{
					boolean clockwise = false;
					if(args[3].contains("clockwise") && !args[3].contains("counter")){
						clockwise = true;
					}

					saveImage(outputImage.rotate(clockwise), args[1]);

				}
				break;

				case "compress":
				if(args.length != 5){
					System.out.println("Incorrect number of parameters.");
				}
				else{
					boolean tileComp, pixelComp;
					if(args[3] == "yes" && args[4] == "yes"){
						tileComp = pixelComp = true;
					}
					else if(args[3] == "yes" && args[4] == "no"){
						tileComp = true;
						pixelComp = false;
					}
					else if(args[3] == "no" && args[4] == "yes"){
						tileComp = false;
						pixelComp = true;
					}
					else{
						tileComp = pixelComp = false;
					}

					saveImage(outputImage.compress(tileComp, pixelComp), args[1]);
				}
				break;

				default:
				System.out.println("Invalid argument.");
			}
		}
		else{
			System.out.println("Input at least 4 arguments.");
		}

	}

	/**
	* Method that takes an Image object and saves it to a file
	* @param img the Image object that is saved to a file
	* @param filename the String that is passed into new File() when a new output file is created for saving
	* @return a true/false value stating whether or not the file was sucessfully found, created, and written to
	*/
	public static boolean saveImage(Image img, String filename){

		File imgFile = new File(filename);

		try{
			PrintWriter fileWriter = new PrintWriter(imgFile);

			boolean grayscale = false;
			if(filename.contains(".pgm")){
				grayscale = true;
			}

			//write grayscale image to file
			if(grayscale){

				fileWriter.println("P2\n"+img.getWidth()+" "+img.getHeight()+"\n255");
				//iterate through pixels and print to file
				for(int i = 0; i < img.getHeight(); i++){
					for(int j = 0; j < img.getWidth(); j++){
						Pixel p = img.getPixel(i,j).clone();
						fileWriter.print(p.toString()+" ");
					}
					fileWriter.println();
				}

			}
			//write color image to file
			else{

				fileWriter.println("P3\n"+img.getWidth()+" "+img.getHeight()+"\n255");
				//iterate through pixels and print to file
				for(int i = 0; i < img.getHeight(); i++){
					for(int j = 0; j < img.getWidth(); j++){
						Pixel p = img.getPixel(i,j).clone();
						int[] val = p.getValue();
						fileWriter.println(val[0]+" "+val[1]+" "+val[2]+" ");
					}
				}
			}

			fileWriter.close();
			return true;

		}
		catch(Exception e){
			System.out.println("Couldn't save file.");
			return false;
		}

	}

	/**
	* Method that takes a CompressedImage object and saves it to a file
	* @param img the CompressedImage object that is saved to a file
	* @param filename the String that is passed into new File() when a new output file is created for saving
	* @return a true/false value stating whether or not the file was sucessfully found, created, and written to
	*/
	public static boolean saveImage(CompressedImage img, String filename){

		File imgFile = new File(filename);
		try{

			PrintWriter fileWriter = new PrintWriter(imgFile);

			boolean grayscale = false;
			if(filename.contains(".pgm")){
				grayscale = true;
			}

			if(grayscale){

				fileWriter.println("P2\n"+img.getWidth()+" "+img.getHeight()+"\n255");

				for(int i = 0; i < img.getHeight(); i++){
					for(int j = 0; j < img.getWidth(); j++){
						Pixel p = img.getPixel(i,j).clone();
						fileWriter.print(p.toString()+" ");
					}
					fileWriter.println();
				}

			}
			else{

				fileWriter.println("P3\n"+img.getWidth()+" "+img.getHeight()+"\n255");

				for(int i = 0; i < img.getHeight(); i++){
					for(int j = 0; j < img.getWidth(); j++){
						Pixel p = img.getPixel(i,j).clone();
						int[] val = p.getValue();

						fileWriter.println(val[0]+" "+val[1]+" "+val[2]+" ");
					}
				}
			}

			fileWriter.close();
			return true;

		}
		catch(Exception e){
			System.out.println("Couldn't save file.");
			return false;
		}

	}

}
