package ua.com.itproekt.gup.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;

/**
 * see@ http://oauth.vk.com/authorize?client_id=5612442&scope=12&redirect_uri=http://api.vk.com/blank.html&display=touch&response_type=token
 * http://api.vk.com/blank.html#access_token=386550907e4f00529704d89164ba227331bf67c135208f16cfe262b5cc3df2ffe31a9108ae52a6a37ddf5&expires_in=86400&user_id=381966870
 *
 * https://api.vkontakte.ru/method/getProfiles?uid=18791&access_token=386550907e4f00529704d89164ba227331bf67c135208f16cfe262b5cc3df2ffe31a9108ae52a6a37ddf5
 * https://api.vkontakte.ru/method/getProfiles?uid=381966870&access_token=386550907e4f00529704d89164ba227331bf67c135208f16cfe262b5cc3df2ffe31a9108ae52a6a37ddf5
 */

public class VK_api {

    private String     client_id = "5612442"; // ID_приложения
    private String         scope = "12"; // "messages"
    private String  redirect_uri = "http://oauth.vk.com/blank.html";
    private String       display = "popup";
    private String response_type = "token";
    private String  access_token;
    private String         email = "sashakmets@yandex.ru"; // email
    private String          pass = "a1l9i7e8n"; // пароль

    /* Код получения token'a */
    public void setConnection() throws IOException, URISyntaxException {
        HttpClient httpClient = new DefaultHttpClient();

        HttpPost post = new HttpPost("http://oauth.vk.com/authorize?" +
                "client_id="+client_id+
                "&scope="+scope+
                "&redirect_uri="+redirect_uri+
                "&display="+display+
                "&response_type="+response_type);

        HttpResponse response;
        response = httpClient.execute(post);
        post.abort();

        String HeaderLocation = response.getFirstHeader("location").getValue();
        URI       RedirectUri = new URI(HeaderLocation);
        String           ip_h = RedirectUri.getQuery().split("&")[2].split("=")[1];
        String           to_h = RedirectUri.getQuery().split("&")[4].split("=")[1];

        post = new HttpPost("https://login.vk.com/?act=login&soft=1"+
                "&q=1"+
                "&ip_h="+ip_h+
                "&from_host=oauth.vk.com"+
                "&to="+to_h+
                "&expire=0"+
                "&email="+email+
                "&pass="+pass);

        response = httpClient.execute(post);
        post.abort();

        HeaderLocation = response.getFirstHeader("location").getValue();
        post           = new HttpPost(HeaderLocation);

        response = httpClient.execute(post);
        post.abort();

        HeaderLocation = response.getFirstHeader("location").getValue();

        post     = new HttpPost(HeaderLocation);
        response = httpClient.execute(post);
        post.abort();

        HeaderLocation = response.getFirstHeader("location").getValue();
        access_token   = HeaderLocation.split("#")[1].split("&")[0].split("=")[1];
    }

    /* Код получения списка сообщений */
    public String getNewMessage() throws ClientProtocolException, IOException, NoSuchAlgorithmException, URISyntaxException {
        String url = "https://api.vk.com/method/"+
                "messages.get"+
                "?out=0"+
                "&access_token="+access_token
                ;

        String line = "";
        try {
            URL url2 = new URL(url);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url2.openStream()));
            line = reader.readLine();
            reader.close();

        } catch (MalformedURLException e) {
            // ...
        } catch (IOException e) {
            // ...
        }
        return line;
    }

}