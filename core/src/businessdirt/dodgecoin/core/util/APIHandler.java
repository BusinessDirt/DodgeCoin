package businessdirt.dodgecoin.core.util;

import businessdirt.dodgecoin.DodgeCoin;
import com.crazzyghost.alphavantage.AlphaVantage;
import com.crazzyghost.alphavantage.AlphaVantageException;
import com.crazzyghost.alphavantage.Config;
import com.crazzyghost.alphavantage.cryptocurrency.response.CryptoUnit;

import java.util.List;

public class APIHandler {

    private static final String apiKey = "2080c478a4msha1042c22dfcfdb0p1f83adjsnf505629c2550";

    // Values set here are the default values if not connected to the internet
    public static double dogecoin = 0.5D, bitcoin = 25000D;

    public static void getStockMarketApiValues() {
        try {
            Config cfg = Config.builder().key(apiKey).timeOut(10).build();
            AlphaVantage.api().init(cfg);

            List<CryptoUnit> dogeList = AlphaVantage.api().crypto().daily().forSymbol("DOGE").market("USD").fetchSync().getCryptoUnits();
            dogecoin = dogeList.get(0).getLowUSD();

            List<CryptoUnit> bitcoinList = AlphaVantage.api().crypto().daily().forSymbol("BTC").market("USD").fetchSync().getCryptoUnits();
            bitcoin = bitcoinList.get(0).getLowUSD();
        } catch (AlphaVantageException e) {
            Util.logEvent("Could not fetch the API value (" + e.getCause() + "). No internet Connection");
        }
    }
}
