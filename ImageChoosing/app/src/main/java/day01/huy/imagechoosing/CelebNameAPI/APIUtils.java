package day01.huy.imagechoosing.CelebNameAPI;

import android.net.Uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class APIUtils {

    private static final String LOG_TAG = APIUtils.class.getSimpleName();

    //Base URL
    private static final String BASE_URL = "http://en.wikipedia.org/w/api.php?";

    //Param for Celeb name
    private static final String CELEB_NAME = "titles";

    //Param with value
    private static final String QUERY_ACTION = "action";
    private static final String PROP_EXTRACT = "prop";
    private static final String REDIRECTS = "redirects";
    private static final String FORMAT = "format";

    //Param without value
    private static final String EXINTRO = "exintro";
    private static final String PLAINTEXT = "explaintext";

    static String getCelebInfo(String queryString){

        HttpURLConnection urlConnection= null;
        BufferedReader reader = null;
        String celebNameJSONString = "";

        try{
            //Build a full query string with required parameter to connect to API.
            Uri builtURI = Uri.parse(BASE_URL).buildUpon().
                    appendQueryParameter(QUERY_ACTION,"query").
                    appendQueryParameter(PROP_EXTRACT,"extracts").
                    appendQueryParameter(REDIRECTS,"1").
                    appendQueryParameter(CELEB_NAME,queryString).
                    appendQueryParameter(EXINTRO,"").
                    appendQueryParameter(PLAINTEXT,"").
                    appendQueryParameter(FORMAT,"json").build();
            // Convert URI to URL
            URL requestURL = new URL(builtURI.toString());

            //Open network connection

            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //Get the InputStream
            InputStream inputStream = urlConnection.getInputStream();

            //Create a buffered reader to read that input
            reader = new BufferedReader(new InputStreamReader(inputStream));

            //Use a StringBuilder to handle the incoming response
            StringBuilder builder = new StringBuilder();

            String line;
            while ((line = reader.readLine())!=null){
                // Add the current line to the string.
                builder.append(line);

                // Since this is JSON, adding a newline isn't necessary (it won't
                // affect parsing) but it does make debugging a *lot* easier
                // if you print out the completed buffer for debugging.
                builder.append("\n");
            }

            if (builder.length() ==0){
                return null;
            }

            celebNameJSONString = builder.toString();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return celebNameJSONString;
    }


}
