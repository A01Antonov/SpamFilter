/**
 * This class stores statistics
 */

public class Stat {
    public static int hamCorrect=0, hamTotal=0;
    public static  int spamCorrect=0, spamTotal=0;
    public static  int outSpamTotal=0, outHamTotal=0;
    public static double result=0;

    //TODO: constructor that gets data from file
    public void stat(){

    }

    public void incHC(){
        hamCorrect++;
    }

    public void incSC(){
        spamCorrect++;
    }

    public void incHT(){
        hamTotal++;
    }

    public void incST() {
        spamTotal++;
    }

    public double calculate(){
        result = ((double)(hamCorrect + spamCorrect))/((double)(hamTotal + spamTotal));
        return result;
    }


}
