class Solution {
    public double angleClock(int hour, int minutes) {
        // Angle covered by minute hand from 12 o'clock position
        // 360 degrees / 60 minutes = 6 degrees per minute
        double minuteAngle = minutes * 6.0;

        // Angle covered by hour hand from 12 o'clock position
        // 360 degrees / 12 hours = 30 degrees per hour
        // Hour hand also moves with minutes: 30 degrees / 60 minutes = 0.5 degrees per minute
        // Adjust hour to 12 for 0 degrees for calculation simplicity
        double actualHour = hour % 12;
        double hourAngle = (actualHour * 30.0) + (minutes * 0.5);

        // Calculate the absolute difference between the two angles
        double diff = Math.abs(hourAngle - minuteAngle);

        // The smaller angle is either 'diff' or '360 - diff'
        return Math.min(diff, 360.0 - diff);
    }
}
// Time Complexity: O(1)
// Space Complexity: O(1)