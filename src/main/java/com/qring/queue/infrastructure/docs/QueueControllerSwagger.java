package com.qring.queue.infrastructure.docs;

import com.qring.queue.application.global.dto.ResDTO;
import com.qring.queue.application.v2.res.QueueGetResDTOV2;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Queues", description = "등록, 삭제 관련 대기 API")
@RequestMapping("/v2/queues")
public interface QueueControllerSwagger {

    @Operation(summary = "대기 가게별 조회", description = "예약 ID와 식당 ID를 기준으로 대기 정보를 조회하는 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "대기 조회 성공", content = @Content(schema = @Schema(implementation = ResDTO.class))),
            @ApiResponse(responseCode = "400", description = "대기 조회 실패", content = @Content(schema = @Schema(implementation = ResDTO.class))),
    })
    @GetMapping
    ResponseEntity<ResDTO<QueueGetResDTOV2>> getBy(@RequestParam Long restaurantId,
                                                   @RequestParam Long reservationId);

    @Operation(summary = "대기 취소", description = "예약 ID와 식당를 ID를 기준으로 대기를 삭제하는 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "대기 취소 성공", content = @Content(schema = @Schema(implementation = ResDTO.class))),
            @ApiResponse(responseCode = "400", description = "대기 취소 실패", content = @Content(schema = @Schema(implementation = ResDTO.class))),
    })
    @DeleteMapping
    ResponseEntity<ResDTO<Object>> deleteBy(@RequestParam Long restaurantId,
                                            @RequestParam Long reservationId);
}
