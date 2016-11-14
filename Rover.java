public class Rover{
  private int x;
  private int y;
  private char ore;
  
  public Rover(int x, int y, char r){
    this.x = x;
    this.y = y;
    ore = r; 
  }
  
  public int x(){
    return x;
  }
  
  public int y(){
    return y;
  }
  
  public char ore(){
    return ore;
  }
  //move forward
  public void move(){
    if (ore == 'N'){
      y++;
    }
    else if (ore == 'S'){
      y--;
    }
    else if (ore == 'E'){
      x++;
    }
    else{
      x--;
    }
  }
    //rotate counterclockwise
    public void rotateL(){
    if (ore == 'N'){
      ore = 'W';
    }
    else if (ore == 'W'){
      ore = 'S';
    }
    else if (ore == 'S'){
      ore = 'E';
    }
    else{
      ore = 'N';
    }
  }
    //rotate clockwise  
    public void rotateR(){
    if (ore == 'N'){
      ore = 'E';
    }
    else if (ore == 'E'){
      ore = 'S';
    }
    else if (ore == 'S'){
      ore = 'W';
    }
    else{
      ore = 'N';
    }
  }
    //unit testing, ignore. 
    public static void main(String args[]){
      Rover testrov = new Rover(0, 0, 'N');
      testrov.move();
      
      System.out.println(testrov.x);
      System.out.println(testrov.y);
    }
    
  
}