package DrugCrawler;

import Helper.DrugLogger;
import Helper.FileHelper;
import Helper.HtmlHelper;
import Helper.StringHelper;
import Models.Drug;
import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class DrugDetailProcessor {
    private Drug drug;

    public DrugDetailProcessor() {
        this.drug = new Drug();
    }

    public void getDrugsDetails(ArrayList<String> drugUrls) throws IOException {
        DrugLogger.Log("{GetDrugsDetailsLog:[");
        for (String url : drugUrls) {
            DrugLogger.Log("{");
            DrugLogger.Log(String.format("Url:%s,", url));
            try {
                System.out.println("Processing url;" + url);
                processOverview("https://www.drugs.com" + url);
                drug.setUrl(url);

                String[] splitsUrl = drug.getUrl().split("/");
                Gson gson = new Gson();
                String content = gson.toJson(drug);
                FileHelper.writeToFile(content, String.format("DrugsData/%s.json", splitsUrl[splitsUrl.length - 1]));
                System.out.println(String.format("DrugsData/%s.json", splitsUrl[splitsUrl.length - 1]));
            } catch (Exception ex) {
                DrugLogger.Log(String.format("Error: %s,", ex.getMessage()));
                DrugLogger.Log(String.format("Stacktrace: %s,", ex.getStackTrace().toString()));
            }

            DrugLogger.Log("},");
        }
        DrugLogger.Log("]}");
    }

    public void processOverview(String drugUrl) throws Exception {
        Document doc = HtmlHelper.getDocument(drugUrl);

        Element contentBox = doc.select("div[class=contentBox]").first();


        String innerHtmlOfOverviewContentBox = contentBox.html();
        String[] splits = innerHtmlOfOverviewContentBox.split("Important information");
        if (splits.length > 0) {
            Document overViewDoc = Jsoup.parse(splits[0]);

            // we don't need menu html node
            Element menuNode = overViewDoc.select("ul[class=nav-tabs nav-tabs-collapse nav-tabs-pill]").first();
            if (menuNode != null) {
                menuNode.remove();
            }

            // get drug's generic name and branch name
            setGenericNameAndBranchName(overViewDoc);

            Element drugSubtitle = overViewDoc.select("p[class=drug-subtitle]").first();
            if (drugSubtitle != null)
                drugSubtitle.remove();

            Element ddcReviewedBy = overViewDoc.select("p[class=ddc-reviewed-by]").first();
            if (ddcReviewedBy != null)
                ddcReviewedBy.remove();

            Element moreInfo = overViewDoc.select("div[class=moreResources]").first();
            if (moreInfo != null)
                moreInfo.remove();

            // get drug's overview
            StringBuilder overviewSpringBuilder = new StringBuilder();
            Elements overviewParagraphs = overViewDoc.select("p");

            for (Element item : overviewParagraphs) {
                overviewSpringBuilder.append(item.text());
            }
            drug.setOverview(overviewSpringBuilder.toString());
        }
    }

    private void setGenericNameAndBranchName(Element node) throws Exception {
        // get generic name , branch name
        Element drugSubtitle = node.select("p[class=drug-subtitle]").first();
        String str = drugSubtitle.text();
        String[] splits = str.split("Brand Names:");
        if (splits.length == 1)
            splits = str.split("Brand Name:");

        if (splits.length == 2) {
            drug.setGenericName(StringHelper.removeNewlineAndTrim(splits[0].replace("Generic Name:", "")));

            Element allBrands = drugSubtitle.select("span[id='all_brands']").first();
            if (allBrands != null) {
                drug.setBranchNames(Arrays.asList(allBrands.text().split(",")));
            } else
                drug.setBranchNames(Arrays.asList(StringHelper.removeNewlineAndTrim(splits[1]).split(",")));
        } else {
            throw new Exception("Missing brand name/ generic name");
        }
    }
}
