//Cory Haas
//Object class for Sudoku.java for AI project #1 - Sudoku
import java.util.ArrayList;
public class box {
    //list of all possible numbers a single box could be
    private ArrayList<Integer> arr=new ArrayList<>();
    //if the box only has one number in arr and has been used by optimize then constrain=true
    //Makes it so this box is reduces the board each time the function is called
    private boolean constrain=false;
    //returns the arraylist of possible numbers of the current box
    public ArrayList<Integer> getBox(){
        return arr;
    }
    //adds a number to arr
    public void put(int i){
        arr.add(i);
    }
    //deletes the specified number in arr
    public void delete(int i){
        int j=arr.indexOf(i);
        if(j>=0)
        arr.remove(j);
    }
    //returns the size of arr
    public int size(){
        return arr.size();
    }
    //returns whether this box has been used for optimization yet
    public boolean isConstrain(){
        return constrain;
    }
    //sets constrain to inputed value
    public void setConstrain(boolean boo){
        constrain=boo;
    }
}
