import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class PhraseSimilarity {
    private String url = "http://swoogle.umbc.edu/SimService/GetSimilarity";
    private double sim = 0;

    public double phraseSim(String p1, String p2){
        String phrase1 = p1.trim().replaceAll("\\s", "%20");
        String phrase2 =p2.trim().replaceAll("\\s", "%20");

        String sendURL = url + "?operation=api&phrase1="+ phrase1 +"&phrase2="+ phrase2;
        try{
            URL url = new URL(sendURL);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String content;

            while ((content = in.readLine()) != null)
                sim = Double.parseDouble(content);
            in.close();

        }catch (MalformedURLException err){
            err.printStackTrace();
        }catch (IOException err){
            err.printStackTrace();
        }

        return sim;
    }

    public static void main(String[] args){
        PhraseSimilarity sim_obj = new PhraseSimilarity();
        double similarity = sim_obj.phraseSim("I am boy", "I am a boy");
        System.out.println("The similarity is " + similarity);
    }
}
