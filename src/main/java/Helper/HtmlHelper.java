package Helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

import java.net.*;
import javax.net.ssl.*;
import java.security.*;
import java.security.cert.*;

class TrustModifier {
    private static final TrustingHostnameVerifier
            TRUSTING_HOSTNAME_VERIFIER = new TrustingHostnameVerifier();
    private static SSLSocketFactory factory;

    /**
     * Call this with any HttpURLConnection, and it will
     * modify the trust settings if it is an HTTPS connection.
     */
    public static void relaxHostChecking(URLConnection conn)
            throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {

        if (conn instanceof HttpsURLConnection) {
            HttpsURLConnection httpsConnection = (HttpsURLConnection) conn;
            SSLSocketFactory factory = prepFactory(httpsConnection);
            httpsConnection.setSSLSocketFactory(factory);
            httpsConnection.setHostnameVerifier(TRUSTING_HOSTNAME_VERIFIER);
        }
    }

    static synchronized SSLSocketFactory
    prepFactory(HttpsURLConnection httpsConnection)
            throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        if (factory == null) {
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, new TrustManager[]{new AlwaysTrustManager()}, null);
            factory = ctx.getSocketFactory();
        }
        return factory;
    }

    private static final class TrustingHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    private static class AlwaysTrustManager implements X509TrustManager {
        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

}


public class HtmlHelper {

    public static String getHtmlString(String webUrl) {
        String output = "";
        try {
            // get URL content
            URL url = new URL(webUrl);
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.tma.com.vn", 8080));

            URLConnection conn = url.openConnection(proxy);
            TrustModifier.relaxHostChecking(conn);
            // open the stream and put it into BufferedReader
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));

            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                output += (inputLine);
            }
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    public static Document getDocument(String webUrl) {
        String html = getHtmlString(webUrl);
        Document doc = Jsoup.parse(html);
        return doc;
    }

    public static String getInnerText(Element htmlNode) {
    /*    var sb = new StringBuilder();
        foreach (var node in htmlNode.DescendantsAndSelf())
        {
            if (!node.HasChildNodes)
            {
                string text = node.InnerText;
                text = text.Trim();
                if (!string.IsNullOrEmpty(text))
                    sb.AppendLine(text);
            }
        }
        return sb.ToString();*/
        return null;
    }
}

