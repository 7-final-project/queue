package com.qring.queue.infrastructure.docs;

import com.qring.queue.application.v1.dto.QueueGetByIdResDTOv1;
import com.qring.queue.application.v1.dto.QueueGetTableResDTOv1;
import com.qring.queue.application.v1.dto.QueuePostResDTOv1;
import com.qring.queue.application.v1.dto.ResDTO;
import com.qring.queue.presentation.v1.req.PostQueueReqDTOv1;
import com.qring.queue.presentation.v1.req.PutQueueReqDTOv1;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Queues", description = "등록, 조회, 수정, 삭제 관련 대기 API")
public interface QueueControllerSwagger {


    @Operation(summary = "대기 등록", description = "예약 ID를 기준으로 대기를 등록하는 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "대기 등록 성공", content = @Content(schema = @Schema(implementation = ResDTO.class))),
            @ApiResponse(responseCode = "400", description = "대기 등록 실패", content = @Content(schema = @Schema(implementation = ResDTO.class))),
    })
    @PostMapping("/v1/queues")
    public ResponseEntity<ResDTO<QueuePostResDTOv1>> postBy(@RequestBody PostQueueReqDTOv1 dto);



    @Operation(summary = "대기 가게별 조회", description = "가게별로 대기자를 조회하는 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "대기 조회 성공", content = @Content(schema = @Schema(implementation = ResDTO.class))),
            @ApiResponse(responseCode = "400", description = "대기 조회 실패", content = @Content(schema = @Schema(implementation = ResDTO.class))),
    })
    @GetMapping("/v1/queues")
    public ResponseEntity<ResDTO<QueueGetTableResDTOv1>> getBy();



    @Operation(summary = "대기 상세 조회", description = "대기 ID 기준으로 대기 상세 정보를 조회하는 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "대기 조회 성공", content = @Content(schema = @Schema(implementation = ResDTO.class))),
            @ApiResponse(responseCode = "400", description = "대기 조회 실패", content = @Content(schema = @Schema(implementation = ResDTO.class))),
    })
    @GetMapping("/v1/queues/{id}")
    public ResponseEntity<ResDTO<QueueGetByIdResDTOv1>> getBy(@PathVariable Long id);



    @Operation(summary = "대기 상태 수정", description = "대기 ID 기준으로 대기 상태를 수정하는 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "대기 수정 성공", content = @Content(schema = @Schema(implementation = ResDTO.class))),
            @ApiResponse(responseCode = "400", description = "대기 수정 실패", content = @Content(schema = @Schema(implementation = ResDTO.class))),
    })
    @PutMapping("/v1/queues/{id}")
    public ResponseEntity<ResDTO<Object>> putBy(@PathVariable Long id,
                                           @RequestBody PutQueueReqDTOv1 dto);



    @Operation(summary = "대기 취소", description = "대기 ID 기준으로 대기를 취소하는 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "대기 취소 성공", content = @Content(schema = @Schema(implementation = ResDTO.class))),
            @ApiResponse(responseCode = "400", description = "대기 취소 실패", content = @Content(schema = @Schema(implementation = ResDTO.class))),
    })
    @DeleteMapping("/v1/queues/{id}")
    public ResponseEntity<ResDTO<Object>> cancelBy(@PathVariable Long id);
}
