package businessdirt.dodgecoin.core;

import businessdirt.dodgecoin.gui.AssetPool;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class APIHandler() {


    public void APIHandler() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://alpha-vantage.p.rapidapi.com/query?function=TIME_SERIES_DAILY&symbol=MSFT&outputsize=compact&datatype=json"))
                .header("X-RapidAPI-Host", "alpha-vantage.p.rapidapi.com")
                .header("X-RapidAPI-Key", "2080c478a4msha1042c22dfcfdb0p1f83adjsnf505629c2550")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }
}