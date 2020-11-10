package DrugCrawler;

import Helper.FileHelper;

import java.io.IOException;
import java.util.ArrayList;

public class DrugCrawler {
    public static void Crawl() throws IOException {
        String urlFilePath = "drugUrls.txt";


        ArrayList<String> drugUrls = null;
        if (FileHelper.isFileExist(urlFilePath) && FileHelper.readAllLines(urlFilePath).size() > 1) {
            System.out.println("Get urls from url file");
            ArrayList<String> urls = FileHelper.readAllLines(urlFilePath);
            drugUrls = urls;
        } else {
            System.out.println("Get urls from drugs.com start");

            //get all drug urls
            DrugUrlProcessor urlsProcessor = new DrugUrlProcessor();
            drugUrls = urlsProcessor.getAllDrugUrls("https://www.drugs.com/drug_information.html");

            System.out.println("Get urls from drugs.com end");

        }

        //get all drug details
        DrugDetailProcessor drugDetailProcessor = new DrugDetailProcessor();
        drugDetailProcessor.getDrugsDetails(drugUrls);
    }
}
