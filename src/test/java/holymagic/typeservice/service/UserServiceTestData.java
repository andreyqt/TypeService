package holymagic.typeservice.service;

import holymagic.typeservice.model.user.PersonalBest;
import holymagic.typeservice.model.user.CurrentTestActivity;
import holymagic.typeservice.model.user.Profile;
import holymagic.typeservice.model.user.Streak;
import holymagic.typeservice.model.user.UserStats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceTestData {

    public static Map<String, List<PersonalBest>> provideMapOfPersonalBest() {
        Map<String, List<PersonalBest>> records = new HashMap<>();
        records.put("15", generatePersonalBests());
        records.put("30", generatePersonalBests());
        records.put("60", generatePersonalBests());
        records.put("120", generatePersonalBests());
        return records;
    }

    public static List<PersonalBest> generatePersonalBests() {
        List<PersonalBest> pbs = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            PersonalBest pb = PersonalBest.builder()
                    .timestamp(1767214800000L + i * 1000)
                    .wpm(100 + Math.random() * i)
                    .acc(100 - Math.random() * i)
                    .consistency(80 + Math.random() * i)
                    .difficulty("normal")
                    .punctuation(false)
                    .numbers(false)
                    .language("english")
                    .build();
            pbs.add(pb);
        }
        return pbs;
    }

    public static UserStats provideUserStats() {
        return UserStats.builder()
                .id("Test_id_123")
                .startedTests(1200)
                .completedTests(1190)
                .timeTyping(200.)
                .build();
    }

    public static Profile provideProfile() {
        return Profile.builder()
                .name("test_name_123")
                .uid("test_id_123")
                .addedAt(1643738836457L)
                .typingStats(provideUserStats())
                .personalBests(Map.of("time", provideMapOfPersonalBest()))
                .xp(23085486)
                .streak(471)
                .maxStreak(471)
                .isPremium(false)
                .build();
    }

    public static CurrentTestActivity provideCurrentTestActivity() {
        return CurrentTestActivity.builder()
                .testsByDays(List.of(10,20,30))
                .lastDay(1743738836457L)
                .build();
    }

    public static Streak provideStreak() {
        return Streak.builder()
                .maxLength(100)
                .lastResultTimestamp(1743738836457L)
                .hourOffset(null)
                .build();
    }

}
