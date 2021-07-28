package com.company;

import java.io.PrintWriter;
import java.util.Scanner;

public class Movie {

    private String title;
    private String[] genre;
    private String rating;
    private String duration;
    private String director;
    private String[] stars;

    public Movie(String title, String[] genre, String rating, String duration, String director, String[] stars) {
        this.title = title;
        this.genre = new String[genre.length];
        setGenre(genre);
        this.rating = rating;
        this.duration = duration;
        this.director = director;
        this.stars = new String[stars.length];
        setStars(stars);
    }

    // Copy constructor
    public Movie(Movie otherMovie) {
        this(otherMovie.title,
                otherMovie.genre,
                otherMovie.rating,
                otherMovie.duration,
                otherMovie.director,
                otherMovie.stars);
    }

    // Constructor - Read obj from file
    public Movie(Scanner s) {
        this.title = s.nextLine();
        this.genre = stringToArray(s.nextLine());
        this.rating = s.nextLine();
        this.duration = s.nextLine();
        this.director = s.nextLine();
        this.stars = stringToArray(s.nextLine());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getGenre() {
        return genre;
    }

    public void setGenre(String[] genre) {
        for(int i = 0; i < genre.length; i++)
            this.genre[i] = genre[i];
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String[] getStars() {
        return stars;
    }

    public void setStars(String[] stars) {
        for(int i = 0; i < stars.length; i++)
            this.stars[i] = stars[i];
    }

    public void save(PrintWriter pw) {
        pw.println(this.title);
        pw.println(arrayToString(this.genre));
        pw.println(this.rating);
        pw.println(this.duration);
        pw.println(this.director);
        pw.println(arrayToString(this.stars));
    }

    public Movie checkIfContains(String str) {
        if(this.title.toLowerCase().contains(str.toLowerCase())){
            return this;
        }
        return null;
    }

    @Override
    public String toString() {
        return this.title + " |"
                + arrayToString(this.genre) + "|"
                + this.rating + " |"
                + this.duration + " |"
                + this.director + " |"
                + arrayToString(this.stars);
    }

    private StringBuilder arrayToString(String[] arr) {
        StringBuilder result = new StringBuilder();

        for(int i = 0; i < arr.length; i++) {
            if(arr.length <= 1 || i == arr.length - 1)
                result.append(arr[i]).append(" ");
            else
                result.append(arr[i]).append(", ");
        }
        return result;
    }

    private String[] stringToArray(String str) {
        return str.split(", ");
    }
}
