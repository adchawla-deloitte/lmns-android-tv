package com.example.uipractice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MovieList {

    public static List<DirectoryDataItem> MOVIE_CATEGORY;

    public static List<Movie> movieList = new ArrayList<>();

    public static String dirSelected = "";
//    private static long count = 0;
//
//    public static String[] videoUrls;
//
//    public static HashMap<Integer, String> map;
//
//    public MovieList() throws IOException {
//    }
//
//    public static List<Movie> getList() {
//        if (list == null) {
//            list = setupMovies();
//        }
//        return list;
//    }
//
//    public static List<Movie> setupMovies() {
//        list = new ArrayList<>();
//        String title[] = {
//                "Zeitgeist 2010_ Year in Review",
//                "Google Demo Slam_ 20ft Search"
//        };
//
//        String description = "Fusce id nisi turpis. Praesent viverra bibendum semper. "
//                + "Donec tristique, orci sed semper lacinia, quam erat rhoncus massa, non congue tellus est "
//                + "quis tellus. Sed mollis orci venenatis quam scelerisque accumsan. Curabitur a massa sit "
//                + "amet mi accumsan mollis sed et magna. Vivamus sed aliquam risus. Nulla eget dolor in elit "
//                + "facilisis mattis. Ut aliquet luctus lacus. Phasellus nec commodo erat. Praesent tempus id "
//                + "lectus ac scelerisque. Maecenas pretium cursus lectus id volutpat.";
//        String studio[] = {
//                "Studio Zero", "Studio One"
//        };
//        String videoUrl[] = videoUrls;
//        String bgImageUrl[] = {
//                "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/Zeitgeist/Zeitgeist%202010_%20Year%20in%20Review/bg.jpg",
//                "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/Demo%20Slam/Google%20Demo%20Slam_%2020ft%20Search/bg.jpg"
//        };
//        String cardImageUrl[] = {
//                "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/Zeitgeist/Zeitgeist%202010_%20Year%20in%20Review/card.jpg",
//                "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/Demo%20Slam/Google%20Demo%20Slam_%2020ft%20Search/card.jpg"
//        };
//
//        //getDirectoryContent();
//
//        for (int index = 0; index < title.length; ++index) {
//            list.add(
//                    buildMovieInfo(
//                            title[index],
//                            description,
//                            studio[index],
//                            videoUrl[index],
//                            cardImageUrl[index],
//                            bgImageUrl[index]));
//        }
//
//
//       // getDirectories();
//
//        return list;
//    }
//
//    private static Movie buildMovieInfo(
//            String title,
//            String description,
//            String studio,
//            String videoUrl,
//            String cardImageUrl,
//            String backgroundImageUrl) {
//        Movie movie = new Movie();
//        movie.setId(count++);
//        movie.setTitle(title);
////        movie.setDescription(description);
////        movie.setStudio(studio);
//        movie.setCardImageUrl(cardImageUrl);
////        movie.setBackgroundImageUrl(backgroundImageUrl);
//        movie.setVideoUrl(videoUrl);
//        return movie;
//    }


}