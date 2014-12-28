import java.util.ArrayList;
import java.util.*;
/**
 * The model for radar scan and accumulator
 * 
 * @author @Sam Lin
 * @version 12-27-2014
 */
public class Radar
{

    // make a boolean two dimensional array to store the current scan
    private boolean[][] currentScan;

    // location of the monster
    private int monsterLocationRow;
    private int monsterLocationCol;

    // probability that a cell will trigger a false detection (must be >= 0 and < 1)
    private double noiseFraction;
    
    // monster's movement 
    private int dy = 0;
    private int dx = 0;
    
    // an arraylist for keeping for the potential comination of dx dy 
    private ArrayList<ArrayList> differenceTable; 

    // number of scans 
    private int numScans = 0; 
    
    // constant determined rows and cols of the grid 
    private final int ROW =100;
    private final int COL =100;

    /** 
     * Constructor for objects of class Radar
     * 
     * @param   rows    the number of rows in the radar grid
     * @param   cols    the number of columns in the radar grid
     * @param   dx      change of cols
     * @param   dy      change of rows
     * @param   mrow    initial row of the monster
     * @param   mcol    initial col of the monster
     */

    public Radar(int rows, int cols,int dy, int dx, int Row, int Col)
    {
        // initialize instance variables
        currentScan = new boolean[rows][cols]; // elements will be set to false
        
        // set all elements to false 
        for(int row = 0; row < currentScan.length; row++)
        {
            for(int col = 0; col < currentScan[0].length; col++)
            {
                {
                    currentScan[row][col]=false;
                }
            }
        }
        // inject the noise 
        injectNoise();
        // set monter's position 
        setMonsterLocation();
        
        // detect the monster 
        currentScan[monsterLocationRow][monsterLocationCol]=true;
        
        // initialize monster's position
        this.monsterLocationRow = Row;
        this.monsterLocationCol = Col;
        
        // initialize dx and dy
        this.dy = dy;
        this.dx = dx;
        
        // an arraylist of arraylist to store the potential dx and dy
        differenceTable = new ArrayList<ArrayList>();


    }

    /**
     * Performs a scan of the radar. Noise is injected into the grid and the accumulator is updated.
     * 
     */
    public void scan()
    {
        // local arraylists that store the rows and cols of the old scan
        // So I can use the .size() method instead of partially Filled array
        ArrayList<Integer> trueRow = new ArrayList<Integer>();
        ArrayList<Integer> trueCol = new ArrayList<Integer>();
        
        // local arraylists that store the rows and cols of the new scan
        // So I can use the .size() method instead of partially Filled array
        ArrayList<Integer> trueRow2 = new ArrayList<Integer>();
        ArrayList<Integer> trueCol2 = new ArrayList<Integer>();
        
        // oldScan 
        // referred back to the createNextGeneration() method we did in the game of life lab 
        boolean [][] oldScan = new boolean [ROW][COL];

        for(int row = 0; row < currentScan.length; row++)
        {
            for(int col = 0; col < currentScan[0].length; col++)
            {
                if(currentScan[row][col] == true)
                {
                    oldScan[row][col]=true;
                }
            }
        }
        
        // clear and reconstruct the currentScan 
        for(int row = 0; row < currentScan.length; row++)
        {
            for(int col = 0; col < currentScan[0].length; col++)
            {
                {
                    currentScan[row][col]=false;
                }
            }
        }
        
        injectNoise();
        setMonsterLocation();
        currentScan[monsterLocationRow][monsterLocationCol]=true;
        
        //store all the rows and cols of the true cells(noise and monster) in the (n-1)th scan in two arraylists
        for(int row = 0; row < oldScan.length; row++)
        {
            for(int col = 0; col < oldScan[0].length; col++)
            {
                if(oldScan[row][col] == true)
                {
                    trueRow.add(row);  
                    trueCol.add(col);                   
                }
            }
        }
       
        //store all the rows and cols of the true cells(noise and monster) in the (n)th scan in two arraylists
        for(int row = 0; row < currentScan.length; row++)
        {
            for(int col = 0; col < currentScan[0].length; col++)
            {
                if(currentScan[row][col] == true)
                {
                    trueRow2.add(row);  
                    trueCol2.add(col);                   
                }
            }
        }
        
        // compare and store all the number with abs less or equal to 5(the limit) in a local int arraylist
        // then store all the arraylists in a big arraylist of arraylists
        for (int a=0 ; a<trueRow.size(); a++)
        {
            for (int b=0 ; b<trueRow2.size(); b++)
            {
                int xSpeed = trueCol2.get(b)-trueCol.get(a);
                int ySpeed = trueRow2.get(b)-trueRow.get(a);
                if (Math.abs(xSpeed) <= 5 && Math.abs(ySpeed) <= 5)
                {                   
                    ArrayList<Integer> combinedyx = new ArrayList <Integer>(); 
                    combinedyx.add(xSpeed);
                    combinedyx.add(ySpeed);
                    differenceTable.add(combinedyx); 
                }
            }
        }       
        
    }
    
    /**
     * find the pre-determined dx and dy 
     */
    public void getDx_Dy()
    {
        // int array that stores all the occurrences of a specific dy dx
        int [] occurrences = new int [differenceTable.size()];        
        int found =0; 
        
        // stores all the occurrences of a specific dy dx within the differenceTable in an int array
        for (int i =0; i< differenceTable.size(); i++)
        {
            int occurrence = Collections.frequency(differenceTable,differenceTable.get(i));
            occurrences[i]=occurrence;
        }
        
        //find the index of the most frequently occurred combination of dy dx
        
        int mostOften = occurrences[0];
        for (int i=1; i< occurrences.length; i++)
        {
            if (occurrences[i]>mostOften)
            {
                mostOften = occurrences[i];
                found = i;
            }
        }
        
        // print out the found dy dx 
        System.out.println("Dy: "+differenceTable.get(found).get(0));
        System.out.println("Dx: "+differenceTable.get(found).get(1));
      
    }
    
        /**
     * Sets the location of the monster
     * use dx dy to increment the rows and cols accordingly 
     */

    public void setMonsterLocation()
    {
        {
            monsterLocationRow+=dy;
            monsterLocationCol+=dx;        
        }
    }
    
    public void setNoiseFraction(double noise)
    {
        noiseFraction = noise; 
    }

    /**
     * Returns true if the specified location in the radar grid triggered a detection.
     * 
     * @param   row     the row of the location to query for detection
     * @param   col     the column of the location to query for detection
     * @return true if the specified location in the radar grid triggered a detection
     */
    public boolean isDetected(int row, int col)
    {
        return currentScan[row][col];
    }

    /**
     * Returns the number of rows in the radar grid
     * 
     * @return the number of rows in the radar grid
     */
    public int getNumRows()
    {
        return currentScan.length;
    }

    /**
     * Returns the number of columns in the radar grid
     * 
     * @return the number of columns in the radar grid
     */
    public int getNumCols()
    {
        return currentScan[0].length;
    }

    /**
     * Returns the number of scans that have been performed since the radar object was constructed
     * 
     * @return the number of scans that have been performed since the radar object was constructed
     */
    public int getNumScans()
    {
        return numScans;
    }

    /**
     * Sets cells as falsely triggering detection based on the specified probability
     * 
     */
    private void injectNoise()
    {
        for(int row = 0; row < currentScan.length; row++)
        {
            for(int col = 0; col < currentScan[0].length; col++)
            {
                // each cell has the specified probablily of being a false positive
                if(Math.random() < noiseFraction)
                {
                    currentScan[row][col] = true;
                }
            }
        }
    }

}
