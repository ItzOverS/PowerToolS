package me.overlight.powertools.Modules.impls;

public class Timer {
    public Timer(int hour, int minute, int second) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public int hour = 0, minute = 0, second = 0;

    public void add(int hour, int minute, int second) {
        this.hour += hour;
        this.minute += minute;
        this.second += second;

        if (this.second >= 60) {
            this.minute += division(this.second);
            this.second = 0;
        }
        if (this.minute >= 60) {
            this.hour += division(this.minute);
            this.minute = 0;
        }
    }

    private static int division(int num1) {
        int num = 0;
        for (int i = 0; i < num1; i += 60)
            num++;
        return num;
    }
}
