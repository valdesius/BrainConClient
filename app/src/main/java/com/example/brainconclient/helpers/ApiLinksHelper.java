package com.example.brainconclient.helpers;

public class ApiLinksHelper {
    private static final String BASE_URL = "http://192.168.0.35:8080/api/v1/";

    public static String authUserApiUri(){
        return BASE_URL + "auth/authenticate";
    }
    public static String updateUserApiUri(){
        return BASE_URL + "auth/updateUser";
    }
    // END OF AUTHENTICATE USER API URI METHOD.

    public static  String registerUserApiUri(){
        return BASE_URL + "auth/register";
    }
    // END OF REGISTER USER API URI METHOD.

    public static String getMyNotesApiUri(){
        return BASE_URL + "note/all";
    }
    // END OF GET MY NOTES API URI METHOD.

    public static String createNoteApiUri(){
        return BASE_URL + "note/create";
    }
    // END OF CREATE NOTES API URI METHOD.

    public static String deleteNoteApiUri(){
        return BASE_URL + "note/delete";
    }
    // END OF DELETE NOTE API URI METHOD.


    public static String getMyTestsApiUri(){
        return BASE_URL + "test/my_tests";
    }
    // END OF GET MY NOTES API URI METHOD.

    public static String createTestApiUri(){
        return BASE_URL + "test/create";
    }
    // END OF CREATE NOTES API URI METHOD.

    public static String deleteTestApiUri(){
        return BASE_URL + "test/delete";
    }


    public static String getFavorites() {
        return BASE_URL + "note/favorites";
    }


    public static String updateFavorite(int noteId, boolean newStatus) {
        return BASE_URL + "note/favorite";
    }


    public static String createQuestionApiUri(){
        return BASE_URL + "questions/create";
    }
    // END OF CREATE NOTES API URI METHOD.

    public static String deleteQuestionApiUri(){
        return BASE_URL + "questions/delete";
    }
    // END OF DELETE NOTE API URI METHOD.



    public static String getMyCommentsApiUri(){
        return BASE_URL + "comment/all";
    }
    public static String createCommentApiUri(){
        return BASE_URL + "comment/createComment";
    }

}