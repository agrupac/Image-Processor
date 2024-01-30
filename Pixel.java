/**
* A class representing a single Pixel object, which makes up a Tile object
* @author Aidan Grupac
*/
public class Pixel{

  /**
  * An integer array that will contain the grayscale or color information of a Pixel
  */
  private int[] value;

  /**
  * A constructor that creates a grayscale Pixel by filling its value with one grayscale integer
  * @param grayvalue the 0-255 number representing black to white
  */
  public Pixel(int grayvalue){

    value = new int[1];
    value[0] = grayvalue;

  }

  /**
  * A constructor that creates a color Pixel by filling its value with three integers red, green, and blue
  * @param red the 0-255 number representing red
  * @param green the 0-255 number representing green
  * @param blue the 0-255 number representing blue
  */
  public Pixel(int red, int green, int blue){

    value = new int[3];
    value[0] = red;
    value[1] = green;
    value[2] = blue;

  }

  /**
  * A getter that returns the value array of a Pixel
  * @return value the color or grayscale value of a Pixel
  */
  public int[] getValue(){

    return value;

  }

  /**
  * A method that checks if the current Pixel is equal to another Object - shares contents but not memory location
  * @param other the Object to be compared
  * @return true/false depending on equality
  */
  public boolean equals(Object other){

    //cast Object to Pixel, convert both to String, compare with String.equals()
    if(this.toString().equals(((Pixel)other).toString())){
      return true;
    }
    else{return false;}

  }

  /**
  * A method that converts Pixel value(s) to a String
  * @return a String of Pixel value(s)
  */
  public String toString(){

      if(value.length==1){
        return ""+value[0];
      }
      else{
        return "R"+value[0]+"#G"+value[1]+"#B"+value[2];
      }

  }

  /**
  * A method that creates a deep copy of a Pixel
  * @return copy a new Pixel object
  */
  public Pixel clone(){

    Pixel copy;

    if(this.value.length==1){
      int g = this.value[0];
      copy = new Pixel(g);
    }
    else{
      int r = this.value[0];
      int g = this.value[1];
      int b = this.value[2];
      copy = new Pixel(r, g, b);
    }

    return copy;
  }

}
