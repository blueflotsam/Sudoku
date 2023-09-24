/*
Cory Haas - AI project #1- Sudoku Puzzle
This program takes in a board of sudoku returns a completed board back.
Blank spaces in the input can represented by a non numereric or space character.
note: this program requires the object class box.java to work
Example of input // output

INPUT:
a8bc7adsf
fff2a8ccc
97bce18ff
er68ert3a
84xyztr65
a2aaa49aa
aa35aaa18
aaa1a7aaa
aaaa4aa5a

OUTPUT:
[6] [8] [2] [9] [7] [5] [1] [4] [3]
[4] [3] [1] [2] [6] [8] [5] [7] [9]
[9] [7] [5] [4] [3] [1] [8] [2] [6]
[1] [5] [6] [8] [9] [2] [7] [3] [4]
[8] [4] [9] [7] [1] [3] [2] [6] [5]
[3] [2] [7] [6] [5] [4] [9] [8] [1]
[7] [9] [3] [5] [2] [6] [4] [1] [8]
[5] [6] [4] [1] [8] [7] [3] [9] [2]
[2] [1] [8] [3] [4] [9] [6] [5] [7]
 */

import java.util.ArrayList;
import java.util.Scanner;
public class Sudoku {

    //This Function takes in a board state and searchs for any box that,
    //has only one number in it that has not yet been applied for optimization,
    // will be given to simplify() function to simplify the board
    //This would be under the arc consistency heuristic
    private static void optimize(box[][] table) {
        for (int i = 0; i < table[0].length; i++) {
            for (int j = 0; j < table.length; j++) {
                if (table[j][i].size() == 1 && !table[j][i].isConstrain()) {
                    simplify(table, j, i, table[j][i].getBox().get(0));
                    table[j][i].setConstrain(true);
                }
            }
        }
    }

    // This Function is called by optimize() and clears any numbers that are
    //the same as the provided number and are in the (x,y) row column and 3x3 box
    private static void simplify(box[][] table, int x, int y, int num) {
        //simpliffing the boxes in the x cordinate and y cordinate
        for (int i = 0; i < 9; i++) {
            if (i != x) {
                table[i][y].delete(num);
            }
            if (i != y) {
                table[x][i].delete(num);
            }
        }
        //simplifying the 3X3 of boxes
        int i = x;
        int j = y;
        while (!(i == 0 || i == 3 || i == 6)) {
            i--;
        }
        while (!(j == 0 || j == 3 || j == 6)) {
            j--;
        }
        for (int yy = 0; yy < 3; yy++) {
            for (int xx = 0; xx < 3; xx++) {
                if (!(xx + i == x && yy + j == y)) {
                    table[xx + i][yy + j].delete(num);
                }
            }
        }
    }

    //THis function takes in a board state and outputs the graph for easy viewing
    private static void printGraph(box[][] table) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                ArrayList<Integer> arr = table[j][i].getBox();
                System.out.print("[");
                for (Integer anArr : arr) {
                    System.out.print(anArr);
                }
                //System.out.print(arr.get(0));
                System.out.print("] ");
            }
            System.out.println();
        }
    }


    //finds next move and also functions as a checker for complete and invalid states
    // -1=complete -2=invalid other wise cordnate of where to expand next
    //The next move is decided by the box that has the least amount of numbers in it
    //but contain atleast 2 or more numbers
    //THis function short circuits if it finds a box with only two numbers
    //The short circuit can cause an invalid state to be missed.
    //overall more boards will be valid that come through this function thus this short curcuting shuild be fine for effecieny
    private static int[] least(box[][] table) {
        int x = -1;
        int y = -1;
        int yield = 10;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int value = table[j][i].size();
                if (value < yield && value > 1) {
                    if (value == 2) {
                        return new int[]{j, i};
                    }
                    x = j;
                    y = i;
                    yield = value;
                } else if (value == 0)
                    return new int[]{-2, -2};
            }
        }
        return new int[]{x, y};
    }

    //This function returns an entirely new 2d box array that is the same as the one coming in
    //The only reason for this function is that array.clone() does not create a new object
    //Thus a manual version of the function has to be made
    private static box[][] copy(box[][] arr){
        box[][] copy = new box[9][9];
        for (int i = 0; i <9 ; i++) {
            for (int j = 0; j <9 ; j++) {
                ArrayList<Integer> temp=arr[j][i].getBox();
                copy[j][i]=new box();
                for (Integer aTemp : temp) {
                    copy[j][i].put(aTemp);
                }
                    copy[j][i].setConstrain(arr[j][i].isConstrain());
            }
        }
        return copy;
    }


    public static void main(String[] args) {
        Scanner scrn = new Scanner(System.in);
        box[][] table = new box[9][9];
        //reading in the table of numbers and filling in blank spaces with every possible number
        for (int i = 0; i < 9; i++) {
            String line = scrn.nextLine();
            for (int j = 0; j < 9; j++) {

                table[j][i] = new box();
                if (input >= 49 && input <= 57) {
                    table[j][i].put(Character.getNumericValue(input));
                } else {
                    for (int k = 1; k < 10; k++) {
                        table[j][i].put(k);
                    }
                }
            }
        }
        //getting ready for looping through possiblities
        optimize(table);
        //list of current and previous states
        ArrayList<box[][]> backTrack = new ArrayList<>();
        backTrack.add(table);
        //list of previous numbers that were applied for checking
        ArrayList<int[]> num = new ArrayList<>();
        while (true) {
            box[][] curr = backTrack.get(backTrack.size() - 1);
            optimize(curr);
            int[] arr = least(curr);
            //found goal state
            if (arr[0] == -1) {
                printGraph(curr);
                break;
            }
            //found invalid state backtracks and removes the number guessed as correct from the box
            else if (arr[0] == -2) {
                backTrack.remove(curr);
                backTrack.get(backTrack.size() - 1)[num.get(num.size()-1)[1]][num.get(num.size()-1)[2]].delete(num.get(num.size()-1)[0]);
                num.remove(num.size()-1);
            }
            //board state is valid thus takes the first number in the provided box position and removes the other numbers in that box
            else {
                box[][] temp=copy(curr);
                int[] temper={temp[arr[0]][arr[1]].getBox().get(0),arr[0],arr[1]};
                num.add(temper);
                temp[arr[0]][arr[1]].getBox().clear();
                temp[arr[0]][arr[1]].put(num.get(num.size()-1)[0]);
                backTrack.add(temp);
            }
            //printGraph(search(backTrack));
        }
    }
}