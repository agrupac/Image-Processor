import java.io.PrintWriter;

/**
* A class representing a single Tile object, which makes up an Image
* @author Aidan Grupac
*/
public class Tile{

  /**
  * a Pixel array containing the Pixels of a Tile
  */
  private Pixel[] block;

  /**
  * a contructor that creates a Tile and initializes its block to length 16
  */
  public Tile(){

    block = new Pixel[16];

  }

  /**
  * a getter that returns a Pixel at (y,x) coordinates
  * @param y the row location within a Tile
  * @param x the column location within a Tile
  * @return the Pixel at coordinates (y,x)
  */
  public Pixel getPixel(int y, int x){

    //create 4x4 coordinate plane
      int[][] coordPlane = new int[4][4];
      int position = 0;
      //fill with elements corresponding to block index
      for(int row = 0; row < 4; row++){
        for(int column = 0; column < 4; column++){
          coordPlane[row][column] = position;
          position++;
        }
      }

      return block[coordPlane[y][x]];

  }

  /**
  * a setter that replaces a Pixel at (y,x) coordinates with Pixel p
  * @param y the row location within a Tile
  * @param x the column location within a Tile
  * @param p the Pixel to insert
  */
  public void setPixel(int y, int x, Pixel p){

    //create 4x4 coordinate plane
    int[][] coordPlane = new int[4][4];
    int position = 0;
    //fill with elements corresponding to block index
    for(int row = 0; row < 4; row++){
      for(int column = 0; column < 4; column++){
        coordPlane[row][column] = position;
        position++;
      }
    }

    block[coordPlane[y][x]] = p;

  }

  /**
  * A method that checks if the current Tile is equal to another Object - shares contents but not memory location
  * @param other the Object to be compared
  * @return true/false depending on equality
  */
  public boolean equals(Object other){

    //cast Object to Tile, convert both to String, compare with String.equals()
    if(this.toString().equals(((Tile)other).toString())){
      return true;
    }
    else{return false;}

  }

  /**
  * A method that converts Pixel value(s) within to a String
  * @return a comma-separated String of Pixel values
  */
  public String toString(){

    String output = "";

    for(Pixel pixel: block){
      output += pixel.toString() + ",";
    }

    //remove final space and comma and return
    return output.substring(0, output.length()-2);

  }

  /**
  * A method that creates a deep copy of a Tile
  * @return copy a new Tile object
  */
  public Tile clone(){

    Tile copy = new Tile();

      for(int i = 0; i < 16; i++){
        Pixel p = this.block[i].clone();
        copy.block[i] = p;
      }

    return copy;

  }

}
