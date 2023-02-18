package me.overlight.powertools.spigot.Libraries;

public class DateTime {
    int year, month, day, hour, minute, second;

    public DateTime(int year, int month, int day, int hour, int minute, int second) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }

    public DateTime plusBy(DateTime b) {
        int year = this.year + b.year,
                month = this.month + b.month,
                day = this.day + b.day,
                hour = this.hour + b.hour,
                minute = this.minute + b.minute,
                second = this.second + b.second;
        while (second >= 60) {
            second -= 60;
            minute++;
        }
        while (minute >= 60) {
            minute -= 60;
            hour++;
        }
        while (hour >= 24) {
            hour -= 24;
            day++;
        }
        while (day >= 30) {
            day -= 30;
            month++;
        }
        while (month >= 12) {
            month -= 12;
            year++;
        }

        return new DateTime(year, month, day, hour, minute, second);
    }

    public boolean isBiggerThan(DateTime b) {
        /*if (this.year > b.year)
            return true;
        else if (this.month > b.month)
            return true;
        else if (this.day > b.day)
            return true;
        else if (this.hour > b.hour)
            return true;
        else if (this.minute > b.minute)
            return true;
        return this.second > b.second;*/
        return this.year >= b.year &&
                this.month >= b.month &&
                this.day >= b.day &&
                this.hour >= b.hour &&
                this.minute >= b.minute &&
                this.second >= b.second;
    }

    @Override
    public String toString() {
        return "{" +
                "" + year +
                "|" + month +
                "|" + day +
                ", " + hour +
                ":" + minute +
                ":" + second +
                '}';
    }
}
