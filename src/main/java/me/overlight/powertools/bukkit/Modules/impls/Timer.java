package me.overlight.powertools.bukkit.Modules.impls;

public class Timer {
    public Timer(int hour, int minute, int second) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public Timer(int year, int month, int day, int hour, int minute, int second) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public int year = 0, month = 0, day = 0, hour = 0, minute = 0, second = 0;

    public void add(int hour, int minute, int second) {
        this.hour += hour;
        this.minute += minute;
        this.second += second;

        while (this.second >= 60) {
            this.second -= 60;
            this.minute++;
        }
        while (this.minute >= 60) {
            this.minute -= 60;
            this.hour++;
        }
        while (this.hour >= 24) {
            this.hour -= 24;
            this.day++;
        }
        while (this.day >= 30) {
            this.day -= 30;
            this.month++;
        }
        while (this.month >= 12) {
            this.month -= 12;
            this.hour++;
        }
    }

    public void add(int year, int month, int day, int hour, int minute, int second) {
        this.year += year;
        this.month += month;
        this.day += day;
        this.hour += hour;
        this.minute += minute;
        this.second += second;

        while (this.second >= 60) {
            this.second -= 60;
            this.minute++;
        }
        while (this.minute >= 60) {
            this.minute -= 60;
            this.hour++;
        }
        while (this.hour >= 24) {
            this.hour -= 24;
            this.day++;
        }
        while (this.day >= 30) {
            this.day -= 30;
            this.month++;
        }
        while (this.month >= 12) {
            this.month -= 12;
            this.hour++;
        }
    }
}
