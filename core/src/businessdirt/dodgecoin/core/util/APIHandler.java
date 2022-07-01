package businessdirt.dodgecoin.core.util;

import com.crazzyghost.alphavantage.AlphaVantage;
import com.crazzyghost.alphavantage.AlphaVantageException;
import com.crazzyghost.alphavantage.Config;
import com.crazzyghost.alphavantage.cryptocurrency.response.CryptoUnit;

import java.util.List;

public class APIHandler {

    private static final String apiKey = "2080c478a4msha1042c22dfcfdb0p1f83adjsnf505629c2550";

    // values set here are the default values
    // these will only be used if the API request fails
    public static double dogecoin = 0.5D, bitcoin = 25000D;

    public static void getStockMarketApiValues() {
        try {
            // library handles all the http requests
            // the handler needs a config containing header information
            Config cfg = Config.builder().key(apiKey).timeOut(10).build();
            AlphaVantage.api().init(cfg);

            // get a list of all dogecoin trade data
            // set the lowest one to the value used
            List<CryptoUnit> dogeList = AlphaVantage.api().crypto().daily().forSymbol("DOGE").market("USD").fetchSync().getCryptoUnits();
            dogecoin = dogeList.get(0).getLowUSD();

            // get a list of all bitcoin trade data
            // set the lowest one to the value used
            List<CryptoUnit> bitcoinList = AlphaVantage.api().crypto().daily().forSymbol("BTC").market("USD").fetchSync().getCryptoUnits();
            bitcoin = bitcoinList.get(0).getLowUSD();

        } catch (AlphaVantageException e) {
            // log an error if not connected to the internet
            Util.logEvent("Could not fetch the API value (" + e.getCause() + "). No internet Connection");
        }
    }
}
