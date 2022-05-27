package businessdirt.dodgecoin.core;

import com.crazzyghost.alphavantage.AlphaVantage;
import com.crazzyghost.alphavantage.Config;
import com.crazzyghost.alphavantage.cryptocurrency.response.CryptoUnit;
import com.crazzyghost.alphavantage.parameters.Interval;
import com.crazzyghost.alphavantage.parameters.OutputSize;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpRequest;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

public class APIHandler {

    private static String apiKey = "2080c478a4msha1042c22dfcfdb0p1f83adjsnf505629c2550";
    public static double dogecoin = 0D, bitcoin = 0D;

    public static void getStockMarketApiValues() {
        Config cfg = Config.builder().key(apiKey).timeOut(10).build();
        AlphaVantage.api().init(cfg);

        List<CryptoUnit> dogeList = AlphaVantage.api().crypto().daily().forSymbol("DOGE").market("USD").fetchSync().getCryptoUnits();
        dogecoin = dogeList.get(0).getLowUSD();

        List<CryptoUnit> bitcoinList = AlphaVantage.api().crypto().daily().forSymbol("BTC").market("USD").fetchSync().getCryptoUnits();
        bitcoin = bitcoinList.get(0).getLowUSD();
    }
}
