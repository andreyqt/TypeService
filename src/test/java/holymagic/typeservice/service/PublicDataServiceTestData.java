package holymagic.typeservice.service;

import holymagic.typeservice.model.publicData.TypingStats;

import java.util.HashMap;
import java.util.Map;

public class PublicDataServiceTestData {

    public static Map<String, Integer> provideHistogram() {
        Map<String, Integer> histogram = new HashMap<>();
        histogram.put("0", 144);
        histogram.put("10", 927);
        histogram.put("20", 3404);
        histogram.put("30", 8860);
        histogram.put("40", 17053);
        histogram.put("50", 28205);
        return histogram;
    }

    public static TypingStats provideTypingStats() {
        return TypingStats.builder()
                .testsCompleted(100L)
                .testsStarted(120L)
                .timeTyping(5.)
                .build();
    }

}
