package com.skysoft.slobodyanuk.timekeeper.util;

/**
 * Created by Serhii Slobodyanuk on 14.09.2016.
 */
public class Globals {

    /*
    * Notification constants
    * */
    public static int MESSAGE_NOTIFICATION_ID = 435345;
    public static final String EVENT_KEY = "isPassage";
    public static final String DATE_KEY = "date";
    public static final String NAME_KEY = "name";

    /*
    * Request constants
    * */
    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 12523;

    /*
    * Fonts constants
    * */
    public final static int OPEN_SANS_REGULAR = 0;
    public final static int OPEN_SANS_SEMI_BOLD = 1;
    public final static int OPEN_SANS_LIGHT = 2;
    public final static int OPEN_SANS_BOLD = 3;

    /*
    * EditText type handling const
    * */
    public static final int EDITTEXT_EMAIL_TYPE = 0;
    public static final int EDITTEXT_URL_TYPE = 1;
    public static final int EDITTEXT_PASSWORD_TYPE = 2;

    /*
    * ViewPager constants
    * */
    public final static String PAGE_KEY = "page_key";
    public final static String START_RANGE_DATE_SELECTED = "start_date_range";
    public final static String END_RANGE_DATE_SELECTED = "end_date_range";
    public final static int TODAY = 0;
    public final static int WEEK = 1;
    public final static int MONTH = 2;
    public final static int DATE_RANGE = 3;

    /*
    * Time constants
    * */
    public final static int DAY_IN_MILLIS = 24 * 60 * 60 * 1000;
    public final static float START_WORK_TIME = 6f;
    public final static float END_WORK_TIME = 21f;
    public final static String UNDEFINED = "undefined";

    public enum TimeState {
        TODAY, WEEK, MONTH, DATE_RANGE
    }

    /*
     * Main ViewPager
     * */
    public final static int CLOCKERS = 0;
    public final static int ACTIVITY = 1;
    public final static int SETTINGS = 2;

    /*
    * Fragment arguments
    * */
    public static final String EMPLOYEE_ID_ARGS = "id";
    public static final String SINGLE_CHART_ARGS = "single_chart";

    public static final String OUT_PASSAGE_KEY = "Passage OUT";
    public static final String IN_PASSAGE_KEY = "Passage IN";

}
