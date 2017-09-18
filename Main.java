import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {
    private static Map<String, Double> pSpam = new HashMap<String, Double>();
    private static Map<String, Double> pHam = new HashMap<String, Double>();
    private static Double spamRatio = 0.0;
    private static Double hamRatio = 0.0;
    private static Stat stat = new Stat();

    public static void main(String[] args) throws IOException {
        String spamPath = "spam-training-set.txt"; //spam training set
        String hamPath = "ham-training-set.txt"; //ham training set
        String inputPath = "input.txt";
        String outputPath = "output.txt";

        /*
        Initialization
        */
        Init.initializeNew(pSpam, pHam, spamPath, hamPath);

        /*
        Global spam ratio and spam probability calculation
         */

        spamRatio = (double) pSpam.size() / (double) (pHam.size() + pSpam.size());
        hamRatio = 1 - spamRatio;

        for (Map.Entry<String, Double> entry : pSpam.entrySet()) {
            pSpam.put(entry.getKey(), entry.getValue() / (double) (pSpam.size()));
        }
        for (Map.Entry<String, Double> entry : pHam.entrySet()) {
            pHam.put(entry.getKey(), entry.getValue() / (double) (pHam.size()));
        }

        /*
        Data processing
         */
        Scanner in = new Scanner(new FileInputStream(inputPath));
        FileWriter fileWriter = new FileWriter(outputPath);
        BufferedWriter writer = new BufferedWriter(fileWriter);

        while (in.hasNextLine()) {
            String line = in.nextLine();
            boolean res = analyze(line);
            if (res) writer.write("spam" + "\n");
            else writer.write("ham\n");
        }

        /*
         debug info
         */
        double resRatio = 1.0 - (double) (stat.hamTotal) / ((double) (stat.hamTotal + stat.spamTotal));
        System.out.println("spam: " + stat.spamTotal + " " + resRatio);
        resRatio = 1.0 - resRatio;
        System.out.println("ham: " + stat.hamTotal + " " + resRatio);

        in.close();
        writer.close();
        fileWriter.close();
    }

    /**
     * This method analyzes given message and returns true if it is spam and false otherwise
     *
     * @param line
     * @return
     */
    private static boolean analyze(String line) {
        String[] data = line.split("[.,!?:; /_=-]|'s|RE");
        double r = 0.0;
        for (String curr : data) {
            curr = curr.toLowerCase();
            double p = 0.0;
            double q = 0.0;
            if (pSpam.containsKey(curr)) p = pSpam.get(curr);
            if (pHam.containsKey(curr)) q = pHam.get(curr);
            if (p + q != 0) {
                r = r + (p / (p * spamRatio + q * hamRatio));
            } else {
                r = r + 0.5;
            }
        }
        r = r / (double) (data.length);
        if (r > 0.9) {
            stat.incST();
            return true;
        } else {
            stat.incHT();
            return false;
        }
    }

    /**
     * This method works exactly like precious, but calculates local spam ratio for each input.
     * It has low accuracy, but it minimizes false positive results.
     *
     * @param line
     * @return
     */
    private static boolean analyzeLocalSpamRatio(String line) {
        double lspamRatio = 0, lhamRatio = 0;
        String[] data = line.split("[.,!?:; /_=-]|'s|RE");
        int n = data.length;
        double r = 0.0;
        for (String curr : data) {
            int k = 0;
            curr = curr.toLowerCase();
            if (pSpam.containsKey(curr)) {
                lspamRatio++;
                k++;
            }
            if (pHam.containsKey(curr)) {
                k++;
            }
            if (k == 0) {
                n--;
            }
        }
        lspamRatio = lspamRatio / ((double) (n));
        lhamRatio = 1 - lspamRatio;

        for (String curr : data) {
            //curr = curr.toLowerCase();
            double p = 0.0;
            double q = 0.0;
            if (pSpam.containsKey(curr)) {
                //lspamRatio++;
                p = pSpam.get(curr);
            }
            if (pHam.containsKey(curr)) {
                q = pHam.get(curr);
            }
            if (p + q != 0) {
                r = r + (p / (p * lspamRatio + q * lhamRatio));
            } else {
                r = r + 0.5;
            }
        }
        r = r / (double) (data.length);
        if (r > 0.9) {
            stat.incST();
            return true;
        } else {
            stat.incHT();
            return false;
        }
    }
}
