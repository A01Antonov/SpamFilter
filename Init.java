import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public class Init {
    /**
     * Collects information from file
     *
     * @param nSpam
     * @param nHam
     * @param spamPath
     * @param hamPath
     */
    public static void initializeNew(Map <String, Double > nSpam, Map <String, Double > nHam, String spamPath, String hamPath){
        try {
            Scanner in = new Scanner(new FileInputStream(spamPath));
            while (in.hasNextLine()){
                String line = in.nextLine();
                String[] data = line.split("[.,!?:; /_=-]|'s|RE");
                for (String curr: data){
                    curr = curr.toLowerCase();
                    if (nSpam.containsKey(curr)){
                        nSpam.put(curr, nSpam.get(curr) + 1);
                    } else {
                        nSpam.put(curr, 1.0);
                    }
                }
            }
            in.close();
            in = new Scanner(new FileInputStream(hamPath));
            while (in.hasNextLine()){
                String line = in.nextLine();
                String[] data = line.split("[.,!?:; /_=]|'s|RE");
                for (String curr: data){
                    curr = curr.toLowerCase();
                    if (nHam.containsKey(curr)){
                        nHam.put(curr, nHam.get(curr) + 1);
                    } else {
                        nHam.put(curr, 1.0);
                    }
                }
            }
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}