package com.vibo.GenAI.dto;

public class Movie {
    private String movieName;
    private String director;
    private String year;
    private String ibmRating;

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getIbmRating() {
        return ibmRating;
    }

    public void setIbmRating(String ibmRating) {
        this.ibmRating = ibmRating;
    }
}
