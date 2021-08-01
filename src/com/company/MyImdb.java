package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class MyImdb {
    private ArrayList<Movie> allMovies;

    public MyImdb(ArrayList<Movie> allMovies){
        this.allMovies = new ArrayList<Movie>(allMovies.size());
        for(int i = 0; i < allMovies.size(); i++) {
            this.allMovies.add(i, new Movie(allMovies.get(i)));
        }
    }

    public MyImdb(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        Scanner s = new Scanner(file);

        int numOfMovies = s.nextInt();
        s.nextLine();
        this.allMovies = new ArrayList<Movie>();
        for(int i = 0; i < numOfMovies; i++) {
            this.allMovies.add(new Movie(s));
        }
        s.close();
    }

    public ArrayList<Movie> getAllMovies() {
        return allMovies;
    }

    public int getLength() {
        return this.allMovies.size();
    }

    public void save(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        PrintWriter pw = new PrintWriter(file);

        pw.println(this.allMovies.size());
        for(int i = 0; i < this.allMovies.size(); i++) {
            this.allMovies.get(i).save(pw);
        }
        pw.close();
    }

    public void saveResult(String fileName, String searchKey) throws FileNotFoundException {
        File file = new File(fileName);
        PrintWriter pw = new PrintWriter(file);

        pw.println("Result file for search key: \"" + searchKey + "\"");
        pw.println("________________________________________________\n");
        for(int i = 0; i < this.allMovies.size(); i++) {
            this.allMovies.get(i).saveResult(pw);
        }
        pw.close();
    }

    @Override
    public String toString() {
        StringBuilder listOfMovies = new StringBuilder();
        for(int i = 0; i < this.getLength(); i++) {
            listOfMovies.append("\t").append(i + 1).append(".\t").append(this.allMovies.get(i).toString()).append("\n");
        }
        return listOfMovies.toString();
    }
}
