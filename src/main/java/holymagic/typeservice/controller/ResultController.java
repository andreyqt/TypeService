package holymagic.typeservice.controller;

import holymagic.typeservice.dto.ResultDto;
import holymagic.typeservice.service.ResultService;
import holymagic.typeservice.validator.ResultRequestValidator;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/results")
@RequiredArgsConstructor
public class ResultController {

    private final ResultService resultService;
    private final ResultRequestValidator resultRequestValidator;

    @Operation(summary = "Gets up to 1000 results")
    @GetMapping("/")
    public ResponseEntity<List<ResultDto>> getResults(
            @RequestParam(required = false, defaultValue = "1589428800000") Long onOrAfterTimestamp,
            @RequestParam(required = false, defaultValue = "50") Integer limit) {
        resultRequestValidator.validateLimitAndTimestamp(onOrAfterTimestamp, limit);
        return ResponseEntity.ok(resultService.getResults(onOrAfterTimestamp, limit));
    }

    @Operation(summary = "Gets result by id")
    @GetMapping("/id/{id}")
    public ResponseEntity<ResultDto> getResultById(@PathVariable String id) {
        return ResponseEntity.ok(resultService.getResultById(id));
    }

    @Operation(summary = "Gets last result")
    @GetMapping("/last")
    public ResponseEntity<ResultDto> getLastResult() {
        return ResponseEntity.ok(resultService.getLastResult());
    }

}
