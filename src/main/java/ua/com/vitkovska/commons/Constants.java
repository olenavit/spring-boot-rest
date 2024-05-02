package ua.com.vitkovska.commons;


public final class Constants {
    private Constants(){

    }
    public final static class Player {
        public static final int MIN_YEAR_OF_BIRTH = 1901;
        public static final int MAX_YEAR_OF_BIRTH = 2010;
        public static final String NAME_PATTERN = "^[A-Z][a-zA-Z]*$";
        public static final String SURNAME_PATTERN = "^[A-Z][a-zA-Z]*$";

        public static final class ValidationMessages {
            public static final String NAME_BLANK = "Name is required";
            public static final String SURNAME_BLANK = "Surname is required";
            public static final String MIN_YEAR_OF_BIRTH_NOT_VALID = "Player year of birth should be grater than 1900";
            public static final String MAX_YEAR_OF_BIRTH_NOT_VALID = "Player year of birth should be less than 2011";
            public static final String NAME_PATTERN_NOT_VALID = "Name must contain only letters of the Latin alphabet and starts with uppercase";
            public static final String SURNAME_PATTERN_NOT_VALID = "Surname must contain only letters of the Latin alphabet and starts with uppercase";

        }
    }


    public static final class Team {
        public static final class ValidationMessages {
            public static final String NAME_BLANK = "Name is required";
            public static final String NAME_NOT_UNIQUE = "Team is already exist";
        }

    }


    public static final class Path {
        public static final String REPORT = "/report";
        public static final String LIST = "/_list";
        public static final String UPLOAD = "/upload";
        public static final String ID_PATH_VARIABLE = "/{id}";
        public static final String API = "/api";
        public static final String PLAYER_API = API + "/player";
        public static final String TEAM_API = API + "/team";

    }

    public static final class Exceptions {
        public static final String PLAYER_NOT_FOUND_MESSAGE = "PLAYER WITH ID = %s not found";
        public static final String TEAM_NOT_FOUND_MESSAGE = "TEAM WITH ID = %s not found";
    }


}
