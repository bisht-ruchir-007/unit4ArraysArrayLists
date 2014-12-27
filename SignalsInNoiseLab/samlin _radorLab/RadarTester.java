import java.util.ArrayList;
/**
 * Write a description of class RadarTester here.
 * 
 * @author Sam Lin 
 * @version 12-21-2014
 */
public class RadarTester
{
    public static void main(String [] args)
    {
        Radar checking = new Radar(100,100,2,2,0,0);
        
        checking.setNoiseFraction(.01);
        for (int i =0; i<10; i++)
        {
            checking.scan();
        }
        checking.findTheSecret();
    }
    
}