import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MarsRovers{
  private static int xmax; //dimension of our plateau
  private static int ymax;
  private static Rover[] rovers; //holds our rovers!
  private static String[] commands; //holds our command strings
  private static int n; //number of rovers
  private static int t; //time variable
  //switch to let us know if rovers were safe the whole time.
  private static boolean success; 
  
  public static class InvalidInput extends Exception {
    public InvalidInput(String message) {
        super(message);
    }
  }

  //aux function to parse our input file.
  public static void readInput(String input){
    try {
      BufferedReader br = new BufferedReader(new FileReader(input));
      
      //First line will tell us boundaries
      String line = br.readLine();
      String[] values = line.split(" ");
      if(values.length == 2){
      xmax = Integer.parseInt(values[0]);
      ymax = Integer.parseInt(values[1]);
      }
      else{
        throw new InvalidInput("invalid boundary values");
      }
      
      //we'll count lines to know how many rovers we have
      int rovlines = 0;
      while (br.readLine() != null) rovlines++;
      //rover info MUST come in pairs
      if(rovlines%2 != 0){
        throw new InvalidInput("# of lines is not as expected. Reconsider input");
      }   
      
      //initialize rover info
      n = rovlines/2;
      rovers = new Rover[n];
      commands = new String[n];
      
      //we close our bufferred reader and create a new one as a crude way
      //to return to the top of file. A bit odd, but mark() and reset() can break 
      //down for large files, and I want to keep this general.
      //br.close();
      br.close();
      BufferedReader br1 = new BufferedReader(new FileReader(input));
      line = br1.readLine(); //throw away first line
      
      //iterate through remaining lines and create our rover objects
      for(int i = 0; i < n; i++){
        line = br1.readLine(); //get rover start position, make rover
        values = line.split(" ");
        rovers[i] = new Rover(Integer.parseInt(values[0]),
                                Integer.parseInt(values[1]), values[2].charAt(0));
        
        line = br1.readLine(); //get rover commands
        commands[i] = line;
      }
      br1.close();
      
    }catch (FileNotFoundException e) {
      System.err.println("Caught IOException: " + e.getMessage());
    }catch (IOException e) {
      System.err.println("Caught IOException: " + e.getMessage());
    }catch (InvalidInput e) {
      System.err.println("Caught IOException: " + e.getMessage());
    }
  }
  
  //aux function to count the Ms in our commands
  public static int countMoves(){
    int counter = 0;
    for(int i = 0; i < commands.length; i++){ //count Ms in each string
      counter = counter + 
        (commands[i].length() - commands[i].replace("M", "").length());
    }
    return counter;
  }
  
  //see if rover r has collided with another (share coordinates)
  public static void checkCollision(int r){
    for(int i = 0; i < n; i++){
      if(i != r){
        if(rovers[i].x() == rovers[r].x() && rovers[i].y() == rovers[r].y()){
          System.out.println("WARNING! Collision at t = " + t);
          success = false;
        }
      }
    }
  }
  //check if rover r has fallen off of the plateau.
  public static void checkBounds(int r){
    if (rovers[r].x() < 0 || rovers[r].x() > xmax){
      System.out.println("WARNING! out-of-bounds at t = " + t);
      success = false;
    }
    if (rovers[r].y() < 0 || rovers[r].y() > ymax){
      System.out.println("WARNING! out-of-bounds at t = " + t);
      success = false;
    }
  }
  
  public static void main(String[] args){
    //by default we take in from input.txt and we dont maintain a history obj
    String input = "input.txt";
    Boolean history = false;
    
    //we scan through our command line args and adjust settings
    for(int i = 0; i < args.length; i++){
      if(args[i].equals("h")){
        history = true;
      }
      else{
        input = args[i];
      }
    }
    
    readInput(input); //call function to parse through input file
    t = 0; //initialize our clock at 0
    success = true;

    //before we can begin, we'll check our starting positions to make sure
    //that no rovers share a starting point, or are off the plateau.
    for(int i = 0; i < n; i++){
      checkCollision(i);
      checkBounds(i);
    }

    //next we create our history logger.
    //we use countMoves to know how many entries it'll need.
    //we +1 to account for initial state as well.
    RoverHistory hist = new RoverHistory(rovers, countMoves() + 1);

    //iterate through rovers
    for(int i = 0; i < n; i++){
      String com = commands[i];
      int m = com.length();
      
      //iterate through commands
      for(int j = 0; j < m; j++){
        if(com.charAt(j) == 'L'){
          rovers[i].rotateL();
          t++;
        }
        if(com.charAt(j) == 'R'){
          rovers[i].rotateR();
          t++;
        }
        if(com.charAt(j) == 'M'){
          rovers[i].move();
          checkCollision(i); //after every movement, make sure rovers are safe
          checkBounds(i);    
          if(history){       //we log the movement into our history if wanted
            hist.log(t, rovers);
          }
          t++;
        }
      }
    }
    
    //if rovers were able to safely execute their missions, we can print their final positions
    if(success){
      for(int i = 0; i < n; i++){
        System.out.println(rovers[i].x() + " " + rovers[i].y() + " " + rovers[i].ore());
      }
    }
    //if not, we direct user to the history logs to troubleshoot.
    else{
      System.out.println("Rover operations failed. Please review WARNING! messages and history logs for details");
    }
    
    if(history){
      hist.writeHist();
    }
  }
}