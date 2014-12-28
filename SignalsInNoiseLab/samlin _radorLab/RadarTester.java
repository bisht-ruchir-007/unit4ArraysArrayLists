import java.util.ArrayList;
/**
 * Write a description of class RadarTester here.
 * 
 * @author @Sam Lin 
 * @version 12-27-2014
 */
public class RadarTester
{
    public static void main(String [] args)
    {
        // rows of the grid, col of the grid, dy , dx 
        // original row and col 
        Radar checking = new Radar(100,100,1,2,0,0);
        
        // set the nosie fraction to .005
        checking.setNoiseFraction(.005);
        for (int i =0; i<20; i++)
        {
            checking.scan();
        }
        checking.getDx_Dy();
        
        // rows of the grid, col of the grid, dy , dx 
        // original row and col 
        Radar checking2 = new Radar(100,100,5,3,0,0);
        
        // set the nosie fraction to .005
        checking2.setNoiseFraction(.005);
        for (int i =0; i<20; i++)
        {
            checking2.scan();
        }
        checking2.getDx_Dy();
        
    }
    
}
