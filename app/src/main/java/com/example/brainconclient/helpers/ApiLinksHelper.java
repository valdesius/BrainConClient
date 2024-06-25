package com.example.brainconclient.helpers;

public class ApiLinksHelper {
    private static final String BASE_URL = "http://158.160.168.98:8080/api/v1/";

    public static String authUserApiUri(){
        return BASE_URL + "auth/authenticate";
    }
    public static String updateUserApiUri(){
        return BASE_URL + "auth/updateUser";
    }

    public static  String registerUserApiUri(){
        return BASE_URL + "auth/register";
    }

    public static String getMyCoursesApiUri(){
        return BASE_URL + "course/all";
    }

    public static String createCourseApiUri(){
        return BASE_URL + "course/create";
    }

    public static String deleteCourseApiUri(){
        return BASE_URL + "course/delete";
    }

    public static String updateCourseApiUri(){
        return BASE_URL + "course/update";
    }


    public static String getMyTestsApiUri(int courseId){
        return BASE_URL + "course/" + courseId + "/tests";
    }

    public static String createTestApiUri(){
        return BASE_URL + "test/create";
    }

    public static String deleteTestApiUri(){
        return BASE_URL + "test/delete";
    }


    public static String getFavorites() {
        return BASE_URL + "course/favorites";
    }


    public static String updateFavorite(int courseId, boolean newStatus) {
        return BASE_URL + "course/favorite";
    }


    public static String createQuestionApiUri(){
        return BASE_URL + "questions/create";
    }

    public static String deleteQuestionApiUri(){
        return BASE_URL + "questions/delete";
    }



    public static String getMyCommentsApiUri(){
        return BASE_URL + "comment/all";
    }
    public static String createCommentApiUri(){
        return BASE_URL + "comment/createComment";
    }

}