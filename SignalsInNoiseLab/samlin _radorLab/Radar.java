import java.util.ArrayList;
import java.util.*;
/**
 * The model for radar scan and accumulator
 * 
 * @author @SamLin
 * @version 12-21-2014
 */
public class Radar
{

    // stores whether each cell triggered detection for the current scan of the radar
    private boolean[][] currentScan;

    // location of the monster
    private int monsterLocationRow;
    private int monsterLocationCol;

    // probability that a cell will trigger a false detection (must be >= 0 and < 1)
    private double noiseFraction;

    // number of scans of the radar since construction
    private int numScans;

    private int[] potentialRow;
    private int[] potentialCol; 

    private ArrayList<Integer> trueRow;
    private ArrayList<Integer> trueCol;
    private ArrayList<Integer> trueRow2;
    private ArrayList<Integer> trueCol2;
    private ArrayList<Integer> potentialDx;
    private ArrayList<Integer> potentialDy;
    private ArrayList<Integer> differenceTable;
    
    private int dy = 0;
    private int dx = 0;


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

    public Radar(int rows, int cols,int dx, int dy, int mRow, int mCol)
    {
        // initialize instance variables
        currentScan = new boolean[rows][cols]; // elements will be set to false

        noiseFraction = 0.01;
        numScans= 0;
        
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

        potentialRow = new int[11];
        potentialCol = new int[11];

        trueRow = new ArrayList<Integer>();
        trueCol = new ArrayList<Integer>();
        trueRow2 = new ArrayList<Integer>();
        trueCol2 = new ArrayList<Integer>();
        potentialDx = new ArrayList<Integer>();
        potentialDy = new ArrayList<Integer>();

        this.monsterLocationRow = mRow;
        this.monsterLocationCol = mCol;

        this.dy = dy;
        this.dx = dx;
        
        this.differenceTable = new ArrayList<Integer>();


    }

    /**
     * Performs a scan of the radar. Noise is injected into the grid and the accumulator is updated.
     * 
     */
    public void scan()
    {
        boolean [][] oldScan = new boolean [100][100];
        // inject noise into the grid
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
        
     
        for(int row = 0; row < oldScan.length; row++)
        {
            for(int col = 0; col < oldScan[0].length; col++)
            {
                if(oldScan[row][col] == true)
                {
                    this.trueRow.add(row);  
                    this.trueCol.add(col);                   
                }
            }
        }
        
        for(int row = 0; row < currentScan.length; row++)
        {
            for(int col = 0; col < currentScan[0].length; col++)
            {
                if(currentScan[row][col] == true)
                {
                    this.trueRow2.add(row);  
                    this.trueCol2.add(col);                   
                }
            }
        }
        
         int currentSize = 0;
        for (int a=0 ; a<trueRow.size(); a++)
        {
            for (int b=0 ; b<trueRow2.size(); b++)
            {
                int changeX = trueCol2.get(b)-trueCol.get(a);
                int changeY = trueRow2.get(b)-trueRow.get(a);
                if (Math.abs(changeY) <= 5 && Math.abs(changeX) <= 5)
                {                   
                    differenceTable.add(currentSize,changeX);
                    differenceTable.add(currentSize,changeY);
                    currentSize++;
                }
            }
        }       
        
    }

    /**
     * Sets the location of the monster
     * 
     * @param   row     the row in which the monster is located
     * @param   col     the column in which the monster is located
     * @pre row and col must be within the bounds of the radar grid
     */

    public void setMonsterLocation()
    {
        if (monsterLocationRow + dy < 100 && monsterLocationCol + dx <100 &&
            monsterLocationRow + dy > 0 && monsterLocationCol + dx > 0)
        {
            this.monsterLocationRow += this.dy;
            this.monsterLocationCol += this.dx;
            currentScan[monsterLocationRow][monsterLocationCol]=true;
            numScans++ ; 
        }
    }

    /**
     * Sets the probability that a given cell will generate a false detection
     * 
     * @param   fraction    the probability that a given cell will generate a flase detection expressed
     *                      as a fraction (must be >= 0 and < 1)
     */
    public void setNoiseFraction(double fraction)
    {
        noiseFraction = fraction;
    }
    
    public void findTheSecret()
    {
        for (int i : differenceTable)
        {
            System.out.println(i);
        }
        
        
        
        

      
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