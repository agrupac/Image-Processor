import java.util.Scanner;
import java.io.File;

/**
* A class representing a single Image object, where operations are performed
* @author Aidan Grupac
*/
public class Image{

	/**
	* an array of Tile objects
	*/
	private Tile[] tileArray;
	/**
	* integers representing the height and width of the Image
	*/
	private int width, height;

	/**
	* boolean representing whether Image is grayscale or not
	*/
	private boolean grayscale;

	/**
	* a constructor that creates an Image with with defined dimensions and color scheme but undefined contents
	* @param height the height of the Image
	* @param width the width of the Image
	* @param grayscale the color scheme of the Image
	*/
	public Image(int height, int width, boolean grayscale){

		this.height = height;
		this.width = width;
		this.grayscale = grayscale;
		this.tileArray = new Tile[(height / 4) * (width / 4)];
		//fill tileArray with empty tiles
		for(int i = 0; i < tileArray.length; i++){
			tileArray[i] = new Tile();
		}

	}

	/**
	* a constructor that creates an Image by reading a file's contents
	* @param filename the name of the source file
	*/
	public Image(String filename){

		File imageFile = new File(filename);
		try{
			Scanner scanner = new Scanner(imageFile);

			if(scanner.nextLine().contains("P2")){
				grayscale = true;
			}
			else{grayscale = false;}

			this.width = scanner.nextInt();
			this.height = scanner.nextInt();
			scanner.next();
			tileArray = new Tile[(height / 4) * (width / 4)];

			//if grayscale image, each number is a pixel
			if(grayscale){
				int[][] allPixels = new int[height][width];
				while(scanner.hasNext()){
					for(int r = 0; r < height; r++){
						for(int c = 0; c < width; c++){
							allPixels[r][c] = scanner.nextInt();
						}
					}
				}

				for(int i = 0; i < height; i++){
					for(int j = 0; j < width; j++){
						Pixel p = new Pixel(allPixels[i][j]);
						setPixel(i, j, p);
					}
				}
			}
			//if color image, each line of three numbers is a pixel
			else{
				while(scanner.hasNext()){
					for(int i = 0; i < height; i++){
						for(int j = 0; j < width; j++){
							int r = scanner.nextInt();
							int g = scanner.nextInt();
							int b = scanner.nextInt();
							Pixel p = new Pixel(r,g,b);
							setPixel(i,j,p);
						}
					}
				}
			}

			scanner.close();

		}
		catch(Exception e){
			System.out.println(e);//debug
		}

	}

	/**
	* a method that creates a deep copy of an Image
	* @return copy a new Image object
	*/
	public Image clone(){

		Image copy = new Image(this.height, this.width, this.grayscale);

		copy.tileArray = this.tileArray.clone();

		return copy;

	}

	/**
	* a getter that returns the width of an Image
	* @return width the width of an Image
	*/
	public int getWidth(){

		return width;

	}

	/**
	* a getter that returns the height of an Image
	* @return width the height of an Image
	*/
	public int getHeight(){

		return height;

	}

	/**
	* a getter that returns a Pixel at (y,x) coordinates of an Image
	* @param y the row location within the Image
	* @param x the column location within the Image
	* @return the Pixel at coordinates (y,x)
	*/
	public Pixel getPixel(int y, int x){

		//holds indexes of tiles in image
		int[][] coordPlane = new int[this.height / 4][this.width / 4];

		int position, min, max, r, c, tileRow, tileCol;
		position = min = max = r = c = tileRow = tileCol = 0;

		//fill array with tile indexes
		for(int row = 0; row < height/4; row++){
			for(int column = 0; column < width/4; column++){
				coordPlane[row][column] = position;
				position++;
			}
		}
		//calculate the coordinates relative to an individual tile
		for(int row = 0; row < height; row++){
			min = 4 * row;
			max = 4 * (row + 1);
			tileRow = y - min;
			if(y >= min && y < max){
				for(int column = 0; column < width; column++){
					min = 4 * column;
					max = 4 * (column + 1);
					if(x >= min && x < max){
						r = row;
						c = column;
						tileCol = x - min;
						break;
					}
				}
				break;
			}
		}

		return tileArray[coordPlane[r][c]].getPixel(tileRow, tileCol);

	}

	/**
	* a setter that replaces a Pixel at (y,x) coordinates of an Image with Pixel p
	* @param y the row location within the Image
	* @param x the column location within the Image
	* @param p the Pixel to insert
	*/
	public void setPixel(int y, int x, Pixel p){

		//holds indexes of tiles in image
		int[][] coordPlane = new int[this.getHeight() / 4][this.getWidth() / 4];
		int position, min, max, r, c, tileRow, tileCol;
		position = min = max = r = c = tileRow = tileCol = 0;

		//fill array with tile indexes
		for(int row = 0; row < this.height/4; row++){
			for(int column = 0; column < this.width/4; column++){
				coordPlane[row][column] = position;
				position++;
			}
		}
		//calculate the coordinates relative to an individual tile
		for(int row = 0; row < height; row++){
			min = 4 * row;
			max = 4 * (row + 1);
			tileRow = y - min;
			if(y >= min && y < max){
				for(int column = 0; column < width; column++){
					min = 4 * column;
					max = 4 * (column + 1);
					if(x >= min && x < max){
						r = row;
						c = column;
						tileCol = x - min;
						break;
					}
				}
				break;
			}
		}

		int location = coordPlane[r][c];

		//if tile doesn't exist, create new one
		if(tileArray[location] == null){
			tileArray[location] = new Tile();
		}

		tileArray[location].setPixel(tileRow, tileCol, p);

	}

	/*
	* a method that creates a new Image, using the contents of original, whose scale has been changed by a factor
	* @param factor the scale by which the Image is changed
	* @return output a new Image
	*/
	public Image scale(int factor){

		Image output;

		//negative factor
		if(factor < 0){

			factor *= -1;

			output = new Image(height/factor, width/factor, this.grayscale);

			//iterate through original image and add pixels at coordinates that are multiples of factor to new image
			int y = 0;
			int x;
			for(int i = 0; i < height; i+=factor){
				x = 0;
				for(int j = 0; j < width; j+=factor){
					Pixel p = this.getPixel(i,j).clone();
					output.setPixel(y,x,p);
					x++;
				}
				y++;
			}

		}

		//positive factor
		else{
			output = new Image(height*factor, width*factor, this.grayscale);

			int row, col, counter;
			row = counter = 0;

			//iterate through enlarged image and add pixel from original image while still within specific bounds
			for(int i = 0; i < height*factor; i++){
				col = 0;
				for(int j = 0; j < width; j++){
					for(int k = 0; k < factor; k++){
						Pixel p = this.getPixel(row,j).clone();
						output.setPixel(i,col,p);
						col++;
					}
				}
				counter++;
				if(counter >= factor){
					row++;
					counter = 0;
				}
			}

		}

		return output;

	}

	/*
	* a method that creates a new Image, using the contents of original, whose dimensions are determined by four bounds
	* @param topY the starting row location within original Image
	* @param topX the starting column location within original Image
	* @param height the height of the new Image
	* @param width the width of the new Image
	* @return output a new Image
	*/
	public Image crop(int topY, int topX, int height, int width){

		int cropHeight = height;
		int cropWidth = width;

		Image output = new Image(cropHeight, cropWidth, this.grayscale);

		//add pixels within bounds from original image to new image
		int x, y;
		x = y = 0;
		for(int i = topY; i < cropHeight+topY; i++){
			x = 0;
			for(int j = topX; j < cropWidth+topX; j++){
				Pixel p = this.getPixel(i,j).clone();
				output.setPixel(y,x,p);
				x++;
			}
			y++;
		}

		return output;

	}

	/*
	* a method that creates a new Image, using the contents of original, which has been mirrored across an axis
	* @param direction the vertical or horizontal direction over which the Image will be flipped
	* @return output a new Image
	*/
	public Image flip(String direction){

		Image output = this.clone();

		//vertical: invert columns
		if(direction.contains("vertical")){
			for(int i = 0; i < height/2; i++){
				for(int j = 0; j < width; j++){
					Pixel p = output.getPixel(i,j).clone();
					Pixel q = output.getPixel(height-1-i,j).clone();
					output.setPixel(i,j,q);
					output.setPixel(height-1-i,j,p);
				}
			}
		}
		//horizontal: invert rows
		else{
			for(int i = 0; i < height; i++){
				for(int j = 0; j < width/2; j++){
					Pixel p = output.getPixel(i,j).clone();
					Pixel q = output.getPixel(i,width-1-j).clone();
					output.setPixel(i,j,q);
					output.setPixel(i,width-1-j,p);
				}
			}
		}

		return output;

	}

	/*
	* a method that creates a new Image, using the contents of original, which has been rotated
	* @param rotate the clockwise or counterclockwise direction the Image will be rotated
	* @return output a new Image
	*/
	public Image rotate(boolean clockwise){

		int nHeight = this.width;
		int nWidth = this.height;

		Image output = new Image(nHeight, nWidth, this.grayscale);

		//first transpose pixels from original image to new image
		for(int i = 0; i < this.height; i++){
			for(int j = 0; j < this.width; j++){
				Pixel p = this.getPixel(i,j).clone();
				output.setPixel(j,i,p);
			}
		}
		//if clockwise, invert rows
		if(clockwise){
			for(int i = 0; i < nHeight; i++){
				for(int j = 0; j < nWidth/2; j++){
					Pixel p = output.getPixel(i,j).clone();
					Pixel q = output.getPixel(i,nWidth-1-j).clone();
					output.setPixel(i,j,q);
					output.setPixel(i,nWidth-1-j,p);
				}
			}
		}
		//if counterclockwise, invert columns
		else{
			for(int i = 0; i < nHeight/2; i++){
				for(int j = 0; j < nWidth; j++){
					Pixel p = output.getPixel(i,j).clone();
					Pixel q = output.getPixel(nHeight-1-i,j).clone();
					output.setPixel(i,j,q);
					output.setPixel(nHeight-1-i,j,p);
				}
			}
		}

		return output;

	}

	/*
	* a method that creates a new CompressedImage, using the contents of original, which has certain equal Pixels and/or Tiles replaced with aliases
	* @param tileCompression whether or not Tile compression will be applied
	* @param pixelCompression whether or not Pixel compression will be applied
	* @return output a new CompressedImage
	*/
	public CompressedImage compress(boolean tileCompression, boolean pixelCompression){

		//returns new CompressedImage object built from current image, operations are done here

		CompressedImage output = new CompressedImage(this.height, this.width, this.grayscale);

		//build compressedimage object from original image
		for(int i = 0; i < this.height; i++){
			for(int j = 0; j < this.width; j++){
				Pixel p = this.getPixel(i,j).clone();
				output.setPixel(i,j,p);
			}
		}

		//if pixel compression is on
		if(pixelCompression){
			//iterate through each tile of image
			for(int n = 0; n < output.getTileArray().length; n++){
				//get pixel from tile
				for(int i = 0; i < 4; i++){
					for(int j = 0; j < 4; j++){
						//search through pixels in tile
						for(int k = 0; k < 4; k++){
							for(int l = 0; l < 4; l++){
								//if equal, replace with alias
								if(output.getTileArray()[n].getPixel(i,j).equals(output.getTileArray()[n].getPixel(k,l))){

									output.getTileArray()[n].setPixel(i, j, (output.getTileArray()[n].getPixel(k,l)));
								}
							}
						}
					}
				}
			}
		}
		//if tile compression is on
		if(tileCompression){
			//go through each tile while counting index
			for(int i = 0; i < output.getTileArray().length; i++){
				//go through each tile again
				for(int j = i+1; j < output.getTileArray().length; j++){
					//if tile a equals tile b replace with alias
					if(output.getTileArray()[i].equals(output.getTileArray()[j])){
						output.getTileArray()[i] = output.getTileArray()[j];
					}
				}
			}
		}

		return output;

	}

	/**
	* A method that checks if the current Image is equal to another Object - shares contents but not memory location
	* @param other the Object to be compared
	* @return true/false depending on equality
	*/
	public boolean equals(Object other){

		//check if other is instance of image
		if(other instanceof Image){
			Image compare = (Image) other;
			//if tileArray lengths don't match
			if(compare.tileArray.length != this.tileArray.length){
				return false;
			}
			//if tileArray elements don't match
			for(int i = 0; i < this.tileArray.length; i++){
				if(!compare.tileArray[i].equals(this.tileArray[i])){
					return false;
				}
				else{
					return true;
				}
			}
		}
		return false;

	}

}
