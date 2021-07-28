package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class MyImdb {
    private Movie[] allMovies;

    public MyImdb(Movie[] allMovies){
        this.allMovies = new Movie[allMovies.length];
        for(int i = 0; i < this.allMovies.length; i++) {
            this.allMovies[i] = new Movie(allMovies[i]);
        }
    }

    public MyImdb(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        Scanner s = new Scanner(file);

        int numOfMovies = s.nextInt();
        s.nextLine();
        this.allMovies = new Movie[numOfMovies];
        for(int i = 0; i < numOfMovies; i++) {
            this.allMovies[i] = new Movie(s);
        }
        s.close();
    }

    public Movie[] getAllMovies() {
        return allMovies;
    }

    public int getLength() {
        return this.allMovies.length;
    }

    public void search(String str) {
        for(int i = 0; i < this.allMovies.length; i++) {
            if(this.allMovies[i].checkIfContains(str) != null)
                System.out.println(this.allMovies[i]);
        }
    }
    public void save(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        PrintWriter pw = new PrintWriter(file);

        pw.println(this.allMovies.length);
        for(int i = 0; i < this.allMovies.length; i++) {
            this.allMovies[i].save(pw);
        }
        pw.close();
    }

    @Override
    public String toString() {
        StringBuilder listOfMovies = new StringBuilder("IMDB Movies List:\n");
        for(int i = 0; i < this.getLength(); i++) {
            listOfMovies.append("\t").append(i + 1).append(".\t").append(this.allMovies[i].toString()).append("\n");
        }
        return listOfMovies.toString();
    }
}
