package Main;

import DrugCrawler.DrugCrawler;

import java.io.IOException;

class Program {
    public static void main(String[] args) throws IOException {
        System.out.println("PROGRAM START");
        DrugCrawler.Crawl();
        System.out.println("PROGRAM END");
    }
}