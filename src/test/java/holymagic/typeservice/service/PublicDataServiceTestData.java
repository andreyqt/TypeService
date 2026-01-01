package holymagic.typeservice.service;

import holymagic.typeservice.model.Response;
import holymagic.typeservice.model.publicData.TypingStats;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class PublicDataServiceTestData {

    public static Response<Map<String, Integer>> provideHistogramResponse() {
        Map<String, Integer> speedData = new HashMap<>();
        speedData.put("0", 144);
        speedData.put("10", 927);
        speedData.put("20", 3404);
        speedData.put("30", 8860);
        speedData.put("40", 17053);
        speedData.put("50", 28205);
        return new Response<>("test message", speedData);
    }

    public static Response<TypingStats> provideTypingStatsResponse() {
        TypingStats stats = TypingStats.builder()
                .testsCompleted(100L)
                .testsStarted(120L)
                .timeTyping(5.)
                .build();
        return new Response<>("test message", stats);
    }

    public static final URI EXPECTED_GET_SPEED_HISTOGRAM_URI =
            URI.create("/public/speedHistogram?language=english&mode=time&mode2=60");
    public static final URI EXPECTED_GET_TYPING_STATS_URI =
            URI.create("/public/typingStats");
}
