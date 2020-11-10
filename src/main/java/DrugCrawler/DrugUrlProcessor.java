package DrugCrawler;

import Helper.DrugLogger;
import Helper.FileHelper;
import Helper.HtmlHelper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DrugUrlProcessor {
    public ArrayList<String> getAllDrugUrls(String url) throws IOException {
        DrugLogger.Log("GetAllDrugUrls Start --------------------------------------------------------------------------");
        Document doc = HtmlHelper.getDocument(url);
        Elements list = doc.select("ul[class=ddc-paging] > li");

        // subAlphabet list
        ArrayList<String> subAlphabets = new ArrayList<String>();
        for (Element item : list) {

            subAlphabets.addAll(getSubAlphabetList(item.text().toLowerCase()));
            DrugLogger.Log(Integer.toString(subAlphabets.size()));

        }

        // get drug list in subAlphabet
        ArrayList<String> listUrls = new ArrayList<String>();

        for (String item : subAlphabets) {
            listUrls.addAll(getDrugUrlsInSubAlphabet(item.toLowerCase()));
            DrugLogger.Log(Integer.toString(listUrls.size()));

        }

        FileHelper.writeToFile(listUrls, "drugUrls.txt");
        DrugLogger.Log("GetAllDrugUrls End --------------------------------------------------------------------------");
        return listUrls;
    }

    public List<String> getSubAlphabetList(String alphabet) {
        String url = String.format("https://www.drugs.com/alpha/%s.html", alphabet).toString();
        Document doc = HtmlHelper.getDocument(url);

        Elements items = doc.select("ul[class=\"ddc-paging\"] a");
        ArrayList<String> listBeginNames = new ArrayList<String>();


        for (Element item : items) {
            if (item.attr("href").equals("") == false)
                listBeginNames.add(item.text());
        }
        return listBeginNames;
    }

    public List<String> getDrugUrlsInSubAlphabet(String subAlphabet) throws IOException {
        String url = String.format("https://www.drugs.com/alpha/%s.html", subAlphabet);
        DrugLogger.Log(url);
        Document doc = HtmlHelper.getDocument(url);

        Element element = doc.select("ul[class*=ddc-list]").first();
        if (element != null) {
            ArrayList<String> listBeginNames = new ArrayList<String>();
            Elements links = element.select("a");
            for (Element item : links) {
                if (item.attr("href").contains(".html") == true) {

                    // DrugLogger.Log(item.Attributes["href"].Value);
                    String drugUrl = item.attr("href");
                    System.out.println(drugUrl);
                    listBeginNames.add(drugUrl);
                }
            }
            return listBeginNames;
        }
        DrugLogger.Log("No drug urls in " + subAlphabet);
        return new ArrayList<String>();

    }
}
