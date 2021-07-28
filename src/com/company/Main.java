package com.company;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;


public class Main {

    final static String FILE_NAME = "db.txt";

    public static void main(String[] args) throws IOException {
        String answer;

        Scanner s = new Scanner(System.in);
        MyImdb myMovies;

        System.out.print("Would you like to load from database y/n? ");
        answer = s.next();

        switch (answer) {
            case "n":
            case "N":
                loadMoviesFromWeb();
            case "y":
            case "Y":
                myMovies = loadMoviesFromDB();
                searchForMovie(s, myMovies);
                break;
        }

    }

    private static void searchForMovie(Scanner s, MyImdb myMovies) {
        System.out.print("What would you like search for? ");
        String searchKey = s.next();
        myMovies.search(searchKey);
    }

    private static MyImdb loadMoviesFromDB() throws FileNotFoundException {
        System.out.print("Loading... ... ... ");
        MyImdb myImdbFromDB = new MyImdb(FILE_NAME);
        System.out.println("Done!");
        System.out.println(myImdbFromDB);

        return myImdbFromDB;

    }

    private static void loadMoviesFromWeb() throws IOException {
        System.out.println("Loading... ... ...");

        Document top250Document = Jsoup.connect("https://www.imdb.com/chart/top/?ref_=nv_mv_250")
                .timeout(6000).get();
        Elements moviesTableElement = top250Document.select("tbody.lister-list");

        int size = moviesTableElement.select("tr").size();
        int i = 0;
        Movie[] movies = new Movie[size];
        String title, genre, rating, duration, director, stars = "";

        for(Element element : moviesTableElement.select("tr")) {
            title = element.select("td.titleColumn a").text();

            Document movieDocument = Jsoup.connect("https://www.imdb.com/"
                    + element.select("td.titleColumn a").attr("href")).get();

            Elements mainContainer = movieDocument.select("div.Hero__MetaContainer__Video-kvkd64-4");
            genre = mainContainer.select("span.ipc-chip__text").text();

            Elements topContainer = movieDocument
                    .select("ul.ipc-inline-list.ipc-inline-list--show-dividers"
                            + ".TitleBlockMetaData__MetaDataList-sc-12ein40-0.dxizHm.baseAlt li");

            try {
                rating = topContainer.select("a").get(1).text();
                duration = topContainer.get(2).text();

            } catch (Exception e) {     //In some movies there is no rating
                rating = "";
                duration = topContainer.get(1).text();
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

            movies[i++] = new Movie(title, genreArray, rating, duration, director, starsArray);

            stars = "";
        }
        MyImdb myImdb = new MyImdb(movies);
        System.out.println("Done!");
        myImdb.save(FILE_NAME);
    }
}
