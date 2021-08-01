package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {

    final static String DB_FILE_NAME = "db.txt";
    final static String RESULT_FILE_NAME = "result.txt";
    final static String SEARCH_KEY = "star trek";

    public static void main(String[] args) throws IOException {
        String answer, searchKey;
        Scanner s = new Scanner(System.in);

        System.out.print("Would you like to load from database y/n (n - New search, y - Star trek search)? ");
        answer = s.next();
        s.nextLine();

        switch (answer) {
            case "n":
            case "N":
                do {
                    System.out.print("What would you like search for? ");
                    searchKey = s.nextLine();
                } while (!checkSearchKey(searchKey));

                loadMoviesFromIMDB(searchKey);
                loadMoviesFromDB(searchKey);
                break;
            case "y":
            case "Y":
                loadMoviesFromDB(SEARCH_KEY);
                break;
        }
    }


    private static boolean checkSearchKey(String searchKey) {
        String[] searchArray = searchKey.split(" ");

        for(int i = 0; i < searchArray.length; i++) {
            if(searchArray[i].length() <= 1 && !searchArray[i].equalsIgnoreCase("I")){
                System.out.println("Please enter another search key...");
                return false;
            }
        }
        return true;
    }

    private static void loadMoviesFromDB(String searchKey) throws FileNotFoundException {
        System.out.println("Loading result file by search key: \"" + searchKey + "\"");
        System.out.println("___________________________________________");
        MyImdb myImdbFromDB = new MyImdb(DB_FILE_NAME);
        System.out.println(myImdbFromDB);
        System.out.println("Done!");
    }

    private static void loadMoviesFromIMDB(String searchKey) throws IOException {
        Document searchResultDocument = Jsoup.connect(
                "https://www.imdb.com/find?q=" + searchKey + "&s=tt&ref_=fn_al_tt_mr").get();
        Elements moviesSearchTable = searchResultDocument.select("table.findList");

        ArrayList<Movie> movies = new ArrayList<>();
        String title, genre, rating = null, duration = null, director, stars = "", note;

        for(Element element : moviesSearchTable.select("tr")) {
            title = element.select("td.result_text a").text();

            note = element.select("td.result_text").text();
            if(note.contains(("in development"))) {
                System.err.println("Title: " + title + "Note: " + note);
                continue;
            }

            Document movieDocument = Jsoup.connect("https://www.imdb.com/"
                    + element.select("td.result_text a").attr("href")).get();

            Elements mainContainer = movieDocument.select("div.Hero__MetaContainer__Video-kvkd64-4");

            // In some movies the element has another class
            if(mainContainer.size() == 0) {
                mainContainer = movieDocument.select("div.Hero__MetaContainer__NoVideo-kvkd64-8");
            }

            genre = mainContainer.select("span.ipc-chip__text").text();
            Elements topContainer = movieDocument
                    .select("ul.ipc-inline-list.ipc-inline-list--show-dividers"
                            + ".TitleBlockMetaData__MetaDataList-sc-12ein40-0.dxizHm.baseAlt li");

            try {
                rating = topContainer.select("a").get(1).text();
                duration = topContainer.get(2).text();

            } catch (Exception e) {     //In some Movies lack some information.
                if(rating == null) {
                    rating = "";
                    try {
                        duration = topContainer.get(1).text();
                    } catch (Exception durationError) { //Rating and Duration are NULL
                        duration = "";
                    }
                }
                else if(duration == null) { //Duration is NULL
                    duration = "";
                }
            }

            //Get the star list
            for(Element li : mainContainer
                    .select("div.PrincipalCredits__PrincipalCreditsPanelWideScreen-hdn81t-0.iGxbgr")
                    .select("li:contains(Stars) li")) {

                stars += li.select("li.ipc-inline-list__item a").text() + ", ";
            }

            director = mainContainer
                    .select("div.PrincipalCredits__PrincipalCreditsPanelWideScreen-hdn81t-0.iGxbgr")
                    .select("li:contains(Director) li").text();

            //Converting string to array
            String[] genreArray = genre.split(" ");
            String[] starsArray = stars.split(", ");

            movies.add(/*i++, */new Movie(title, genreArray, rating, duration, director, starsArray));

            stars = "";
        }

        MyImdb myImdb = new MyImdb(movies);
        System.out.println("Done!");
        myImdb.save(DB_FILE_NAME);
        myImdb.saveResult(RESULT_FILE_NAME, searchKey);
    }
}
