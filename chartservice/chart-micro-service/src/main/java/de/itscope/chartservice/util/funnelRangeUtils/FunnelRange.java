package de.itscope.chartservice.util.funnelRangeUtils;

import org.joda.time.DateTime;
import org.joda.time.Days;

import static java.time.temporal.ChronoUnit.DAYS;

public enum FunnelRange {
    Months_3{
        @Override
        public long getDateRange() {
            //90 Days are the maximum, Maxipanels API can provide as a range
            return 90;

        }
    }, Month_1{
        @Override
        public long getDateRange() {
            return Days.daysBetween(DateTime.now().minusMonths(1).plusDays(1).toLocalDate(), DateTime.now().toLocalDate()).getDays();
        }
    }, Week {
        @Override
        public long getDateRange() {
            return Days.daysBetween(DateTime.now().minusWeeks(1).plusDays(1).toLocalDate(), DateTime.now().toLocalDate()).getDays();
        }
    };


    public abstract long getDateRange();
}
