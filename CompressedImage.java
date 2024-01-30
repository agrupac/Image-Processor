/**
* A class representing a single Image object, where operations are performed
* @author Aidan Grupac
*/
public class CompressedImage{

  /**
  * an array of Tile objects
  */
  private Tile[] tileArray;
  /**
  * integers representing the height and width of the CompressedImage
  */
  private int width, height;
  /**
  * boolean representing whether CompressedImage is grayscale or not
  */
  private boolean grayscale;

  /**
  * a constructor that creates a CompressedImage with with defined dimensions and color scheme but undefined contents
  * @param height the height of the CompressedImage
  * @param width the width of the CompressedImage
  * @param grayscale the color scheme of the CompressedImage
  */
  public CompressedImage(int height, int width, boolean grayscale){

    this.height = height;
    this.width = width;
    this.grayscale = grayscale;
    tileArray = new Tile[(height / 4) * (width / 4)];

    //fill tileArray with empty tiles
    for(int i = 0; i < tileArray.length; i++){
      tileArray[i] = new Tile();
    }

  }

  /**
  * a getter that returns the width of a CompressedImage
  * @return width the width of a CompressedImage
  */
  public int getWidth(){

    return width;

  }

  /**
  * a getter that returns the tileArray of a CompressedImage
  * @return the tileArray of a CompressedImage
  */
  public Tile[] getTileArray(){

    return tileArray;
  }

  /**
  * a getter that returns the height of a CompressedImage
  * @return height the height of a CompressedImage
  */
  public int getHeight(){

    return height;
  }

  /**
  * a getter that returns a Pixel at (y,x) coordinates of a CompressedImage
  * @param y the row location within the CompressedImage
  * @param x the column location within the CompressedImage
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
  * a setter that replaces a Pixel at (y,x) coordinates of an CompressedImage with Pixel p
  * @param y the row location within the CompressedImage
  * @param x the column location within the CompressedImage
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

  /**
  * A method that checks if the current CompressedImage is equal to another Object - shares contents but not memory location
  * @param other the Object to be compared
  * @return true/false depending on equality
  */
  public boolean equals(Object other){

    //check if other is instance of compressedimage
    if(other instanceof CompressedImage){
      CompressedImage compare = (CompressedImage) other;
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
