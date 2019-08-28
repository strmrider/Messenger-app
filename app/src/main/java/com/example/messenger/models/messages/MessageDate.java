package com.example.messenger.models.messages;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageDate {
    private class Time{
        int hour;
        int minute;
        int second;

        Time(String date)
        {
            String hour = "";
            String minute = "";
            String second = "";

            hour += date.charAt(11);
            hour += date.charAt(12);
            minute += date.charAt(14);
            minute += date.charAt(15);

            second += date.charAt(17);
            second += date.charAt(18);

            this.hour = Integer.parseInt(hour);
            this.minute = Integer.parseInt(minute);
            this.second = Integer.parseInt(second);
        }

        String shortFormat()
        {
            return hour + ":" + minute;
        }

        int compare(Time time)
        {
            if (this.hour > time.hour)
                return 1;
            else if (this.hour < time.hour)
                return -1;
            else
            {
                if (this.minute > time.minute)
                    return 1;
                else if (this.minute < time.minute)
                    return -1;
                else
                {
                    if (this.second > time.second)
                        return 1;
                    else if (this.second < time.second)
                        return -1;
                    else
                        return 0;
                }
            }
        }
    }

    // actual date
    private class SubDate {
        int day;
        int month;
        int year;

        SubDate(String date)
        {
            String year = "";
            String month = "";
            String day = "";

            year += date.charAt(0);
            year += date.charAt(1);
            year += date.charAt(2);
            year += date.charAt(3);

            month += date.charAt(5);
            month += date.charAt(6);

            day += date.charAt(8);
            day += date.charAt(9);

            this.year = Integer.parseInt(year);
            this.month = Integer.parseInt(month);
            this.day = Integer.parseInt(day);

        }

        String shortFormat()
        {
            String monthStr = "";
            switch (month)
            {
                case 1:
                    monthStr = "Jan";
                    break;
                case 2:
                    monthStr = "Feb";
                    break;
                case 3:
                    monthStr = "Mar";
                    break;
                case 4:
                    monthStr = "Apr";
                    break;
                case 5:
                    monthStr = "May";
                    break;
                case 6:
                    monthStr = "Jun";
                    break;
                case 7:
                    monthStr = "Jul";
                    break;
                case 8:
                    monthStr = "Aug";
                    break;
                case 9:
                    monthStr = "Sep";
                    break;
                case 10:
                    monthStr = "Oct";
                    break;
                case 11:
                    monthStr = "Nov";
                    break;
                case 12:
                    monthStr = "Dec";
                    break;
            }

            return monthStr + " " + day;
        }

        int compare(SubDate subDate)
        {
            if (this.year > subDate.year)
                return 1;
            else if (this.year < subDate.year)
                return -1;
            else
            {
                if (this.month > subDate.month)
                    return 1;
                else if (this.month < subDate.month)
                    return -1;
                else
                {
                    if (this.year > subDate.year)
                        return 1;
                    else if (this.year < subDate.year)
                        return -1;
                    else
                        return 0;
                }
            }
        }
    }

    private Time time;
    private SubDate subDate;
    private String dateAsStr;

     public MessageDate()
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        init(dateFormat.format(date));
    }

    public MessageDate(String date)
    {
        init(date);
    }

    private void init(String date)
    {
        if ( date != null ) {
            dateAsStr = date;
            subDate = new SubDate(dateAsStr);
            time = new Time(dateAsStr);
        }
    }

     public String getDate()
    {
        return dateAsStr;
    }

    private Time getTimeObj()
    {
        return time;
    }

    private SubDate getSubDateObj()
    {
        return subDate;
    }

    public String getDateFormat()
    {
        return time.shortFormat() + " " + subDate.shortFormat();
    }
    public int compare(MessageDate messageDate)
    {
        int subDateCmpResult = this.subDate.compare(messageDate.getSubDateObj());
        if (subDateCmpResult == 0) {
            return this.time.compare(messageDate.getTimeObj());
        }
        else
            return subDateCmpResult;
    }
}