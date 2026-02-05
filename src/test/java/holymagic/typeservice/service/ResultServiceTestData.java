package holymagic.typeservice.service;

import holymagic.typeservice.dto.ResultDto;
import holymagic.typeservice.mapper.ResultMapper;
import holymagic.typeservice.model.result.Result;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

public class ResultServiceTestData {

    public static List<Result> provideResults() {
        List<Result> results = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Result result = Result.builder()
                    .id("test_id" + i)
                    .acc(80. + i)
                    .timestamp(1664536400000L + 1000 * i)
                    .mode("quote")
                    .mode2("test_id_quote_" + i)
                    .testDuration(10. + i)
                    .uid("test_uid" + 10*i)
                    .charStats(List.of(100,2,3,4))
                    .wpm(100. + Math.random() * 100)
                    .build();
            results.add(result);
        }
        return results;
    }

    public static List<ResultDto> provideResultDtos(List<Result> results) {
        ResultMapper resultMapper = Mappers.getMapper(ResultMapper.class);
        return resultMapper.toDto(results);
    }

}
