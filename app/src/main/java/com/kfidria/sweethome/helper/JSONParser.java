package com.kfidria.sweethome.helper;

/**
 * Created by kfidria on 04.07.2016.
 */

        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;

        import java.net.HttpURLConnection;
        import java.net.URL;

public class JSONParser {

    static String json = "";

    // constructor
    public JSONParser() {

    }

    // function get json from url
    public String makeHttpRequest(String url_string, String method) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {

            URL url = new URL(url_string);

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(method);
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            json = buffer.toString();
        } catch (IOException e) {

            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    ;
                }
            }
        }

        // return JSON String
        return json;
    }
}