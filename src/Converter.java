import com.google.gson.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Properties;
public class Converter {
    public static Scanner scanner = new Scanner(System.in);
    public static String getAPIKey(){
        Properties prop = new Properties();
        String key = "";
        try {
            prop.load(new FileInputStream("config.properties"));
            key = prop.getProperty("api.key");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return key;
    }
    private String APIKey = getAPIKey();
    public static void main(String[] arguments) throws IOException { //TESTING ONLY
        ArrayList<Currency> currencies = initialize();
        System.out.println("Welcome to the currency converter!");
        System.out.print("Type the 3 letter code of your input currency: ");
        String input = scanner.nextLine();
        System.out.print("Type the 3 letter code of your output currency: ");
        String output = scanner.nextLine();
        System.out.print("Type the amount of money you wish to convert: ");
        double amount = scanner.nextDouble();
        System.out.print(amount + " " + input.toUpperCase() + " is " + convert(input.toUpperCase(), output.toUpperCase(), amount) + " " + output.toUpperCase() + ".");
    }
    public static double convert(String ipc, String opc, double amount) throws IOException {
        /*Check for invalid inputs*/
        if(amount<=0){
            return -3.0;
        }
        else if (ipc.length() != 3 || opc.length() != 3) { //Currency has to be 3 letters in length
            return -2.0;
        }
        else if(!valid(ipc) || !valid(opc)){
           return -1.0;
        }
        /*Convert*/
        String request = "https://v6.exchangerate-api.com/v6/" + getAPIKey() + "/pair/" + ipc.toUpperCase() + "/" + opc.toUpperCase() + "/" + amount; //Set the URL
        //*Sending the request to the API*/
        @SuppressWarnings("deprecation")
        URL url = new URL(request);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        //*Convert the returned request to JSON*/
        @SuppressWarnings("deprecation")
        JsonParser parser = new JsonParser();
        @SuppressWarnings("deprecation")
        JsonElement root = parser.parse(new InputStreamReader((InputStream) connection.getContent()));
        JsonObject object = root.getAsJsonObject();
        return object.get("conversion_result").getAsDouble(); //Access & return the 'conversion_result' key from the JSON object
    }
    public static boolean valid(String currency) throws IOException{
        /*Send the Request*/
        String request = "https://v6.exchangerate-api.com/v6/" + getAPIKey() + "/codes";
        @SuppressWarnings("deprecation")
        URL url = new URL(request);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        @SuppressWarnings("deprecation")
        JsonParser parser = new JsonParser();
        @SuppressWarnings("deprecation")
        JsonElement root = parser.parse(new InputStreamReader((InputStream) connection.getContent())); //error happens here
        JsonObject object = root.getAsJsonObject();
        JsonArray array = object.getAsJsonArray("supported_codes");
        for(int i=0; i<array.size(); i++){
            String id = array.get(i).getAsJsonArray().get(0).getAsString();
            //System.out.println(id);
            if(currency.equals(id)){
                return true;
            }
        }
        return false;
    }
    public static ArrayList<Currency> initialize() throws IOException { //Initialize Method to create a list of currencies, used in creating the dropdown
        /*Send the request and store the 'supported_codes' into a JSON Array*/
        String request = "https://v6.exchangerate-api.com/v6/" + getAPIKey() + "/codes";
        @SuppressWarnings("deprecation")
        URL url = new URL(request);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        @SuppressWarnings("deprecation")
        JsonParser parser = new JsonParser();
        @SuppressWarnings("deprecation")
        JsonElement root = parser.parse(new InputStreamReader((InputStream) connection.getContent())); 
        JsonObject object = root.getAsJsonObject();
        JsonArray array = object.getAsJsonArray("supported_codes");
        /*Store these values in an arrayList of currencies*/
        ArrayList<Currency> currencies = new ArrayList<>();
        for (int i = 0; i< array.size(); i++){
            currencies.add(new Currency(array.get(i).getAsJsonArray().get(0).getAsString(), array.get(i).getAsJsonArray().get(1).getAsString()));
        }
        return currencies;
    }
}
/*
https://www.exchangerate-api.com/docs/java-currency-api
https://stleary.github.io/JSON-java/index.html
https://github.com/google/gson
https://www.tutorialspoint.com/json/json_java_example.htm
https://jar-download.com/artifacts/io.github.palexdev/gson/2.9.0/source-code
 */
