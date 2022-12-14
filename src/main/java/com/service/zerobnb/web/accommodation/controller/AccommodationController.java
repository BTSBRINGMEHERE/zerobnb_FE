package com.service.zerobnb.web.accommodation.controller;

import com.service.zerobnb.web.accommodation.dto.AccommodationForHostDto;
import com.service.zerobnb.web.accommodation.dto.AccommodationInfoDto;
import com.service.zerobnb.web.accommodation.model.AccommodationInput;
import com.service.zerobnb.web.accommodation.service.AccommodationService;
import com.service.zerobnb.web.error.message.ExceptionMessage;
import com.service.zerobnb.web.error.model.ValidationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Tag(name = "숙박업소", description = "숙박업소 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/accommodation")
@Slf4j
public class AccommodationController {
    private final AccommodationService accommodationService;

    @Operation(summary = "특정 호스트의 숙소 정보 반환", description = "숙소 정보 반환 수행 메서드")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "숙소 정보 반환 완료"),
            @ApiResponse(responseCode = "500", description = "네트워크, 데이터베이스 저장 실패 등의 이유로 숙소 정보 반환 실패")
    })
    @GetMapping("/search/{hostId}")
    public ResponseEntity<List<AccommodationForHostDto>> hostAccommodation(@PathVariable Long hostId, Principal principal) {
        // TODO 추후에 principal 객체를 통한 로그인 여부, hostId <-> parameter hostId 비교 및 인증
        return ResponseEntity.status(HttpStatus.OK).body(accommodationService.findAccommodationByHostId(hostId));
    }


    @Operation(summary = "숙소 정보 등록", description = "숙소 정보 등록 수행 메서드")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "숙소 정보 등록 완료"),
            @ApiResponse(responseCode = "500", description = "네트워크, 데이터베이스 저장 실패 등의 이유로 숙소 정보 등록 실패")
    })
    @PostMapping("/register")
    public ResponseEntity<Long> registerAccommodation(@RequestBody @Valid AccommodationInput accommodationInput, BindingResult bindingResult, Principal principal) {
        // TODO 추후에 principal 객체를 통한 로그인 여부, hostId <-> parameter hostId 비교 및 인증
        if (bindingResult.hasErrors()) {
            throw new ValidationException(ExceptionMessage.NOT_VALID_INPUT);
        }
        return ResponseEntity.status(HttpStatus.OK).body(accommodationService.registerAccommodation(accommodationInput));
    }

    @Operation(summary = "숙소 정보 수정", description = "숙소 정보 수정 수행 메서드")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "숙소 정보 수정 완료"),
            @ApiResponse(responseCode = "500", description = "네트워크, 데이터베이스 저장 실패 등의 이유로 숙소 정보 수정 실패")
    })
    @PutMapping("/update/{accommodationId}")
    public ResponseEntity<Long> updateAccommodation(@RequestBody @Valid AccommodationInput accommodationInput, @PathVariable Long accommodationId,
                                                    BindingResult bindingResult, Principal principal) {
        // TODO principal 객체를 이용한 guest email 반환 및 이메일 인증에 사용
        if (bindingResult.hasErrors()) {
            throw new ValidationException(ExceptionMessage.NOT_VALID_INPUT);
        }
        return ResponseEntity.status(HttpStatus.OK).body(accommodationService.updateAccommodation(accommodationInput, accommodationId));
    }

    @Operation(summary = "숙소 정보 삭제", description = "숙소 정보 삭제 수행 메서드")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "숙소 정보 삭제 완료"),
            @ApiResponse(responseCode = "500", description = "네트워크, 데이터베이스 저장 실패 등의 이유로 숙소 정보 삭제 실패")
    })
    @DeleteMapping("/delete/{accommodationId}")
    public ResponseEntity<Long> deleteAccommodation(@PathVariable Long accommodationId, Principal principal) {
        // TODO principal 객체를 이용한 guest email 반환 및 이메일 인증에 사용
        return ResponseEntity.status(HttpStatus.OK).body(accommodationService.deleteAccommodation(accommodationId));
    }


    @Operation(summary = "숙소 상세 정보 반환", description = "숙소 상세 정보 반환 메서드")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "숙소 상세 정보 반환 완료"),
            @ApiResponse(responseCode = "500", description = "숙소 상세 정보 반환 실패")
    })
    @GetMapping("/info/{accommodationId}")
    public ResponseEntity<AccommodationInfoDto> accommodationInfo(@PathVariable Long accommodationId) {
        return ResponseEntity.ok(accommodationService.accommodationInfo(accommodationId));
    }
}
