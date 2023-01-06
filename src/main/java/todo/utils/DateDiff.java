package todo.utils;

import java.util.Date;

public class DateDiff {
    public static int dayDiff(Date dateStart, Date dateEnd) {
        return (int) (dateEnd.getTime() - dateStart.getTime()) / 1000 / 60 / 60 / 24;
    }
}
