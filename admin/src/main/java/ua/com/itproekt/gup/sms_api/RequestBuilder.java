package ua.com.itproekt.gup.sms_api;

/**
 * Created by Optical Illusion on 30.10.2015.
 */
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RequestBuilder {
    String URL;

    public RequestBuilder(String URL) {
        this.URL = URL;
    }

    public String doXMLQuery(String xml) {
        StringBuilder responseString = new StringBuilder();

        Map   params=new HashMap();
        params.put("XML", xml);
        try {
            Connector.sendPostRequest(this.URL, params);
            String[] response = Connector.readMultipleLinesRespone();
            for (String line : response) {
                responseString.append(line);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Connector.disconnect();
        return responseString.toString();
    }
}
