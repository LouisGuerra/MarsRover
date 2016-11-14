import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class RoverHistory{
  private int[][] hist; //table to hold [time, x1, y1, x2, y2, x3, y3,...]
  private int c; //counter to track current empty row
  private int n;
  
  //constructor will log initial state of rovers, at t=0;
  public RoverHistory(Rover[] r, int tMax){
    n = r.length;
    
    hist = new int[tMax][2*n + 1];

    //iterate through rovers and log positions
    for(int i = 0; i < n; i = i + 2){ 
      hist[0][2*i + 1] = r[i].x();
      hist[0][2*i + 2] = r[i].y();
    }
    
    c = 1;
  }
  
  public void log(int t, Rover[] r){
    //just as in constructor, iterate through rovers and log positions
    hist[c][0] = t;
    for(int i = 0; i < n; i++){
      hist[c][2*i + 1] = r[i].x();
      hist[c][2*i + 2] = r[i].y();
    }
    c++;
  }
  public void writeHist(){
    try {
      PrintWriter writer = new PrintWriter("RoverHistory.txt", "UTF-8");
      
      //print header
      String header = "time " + '\t';
      for(int i = 1; i <= (n); i++){
        header = header + i + '\t';
      }
      writer.println(header + '\n');
      
      //print lines
      String line = "";
      for(int i = 0; i < hist.length; i++){
        line = "" + hist[i][0] + '\t';
        for(int j = 0; j < n; j++){
          line = line + '(' + hist[i][2*j + 1] + ',' + hist[i][2*j + 2] + ')' + '\t';
        }
        writer.println(line);
      }
      writer.close();
    } catch (FileNotFoundException e) {
      System.err.println("Caught IOException: " + e.getMessage());
    } catch(UnsupportedEncodingException e){
      System.err.println("Caught IOException: " + e.getMessage());
    }
  }
}