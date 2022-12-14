package com.service.zerobnb.web.accommodation.service;

import com.service.zerobnb.util.LocationPosition;
import com.service.zerobnb.util.status.HostStatus;
import com.service.zerobnb.util.type.AccommodationType;
import com.service.zerobnb.util.type.PopularFacilityServiceType;
import com.service.zerobnb.web.accommodation.domain.Accommodation;
import com.service.zerobnb.web.accommodation.domain.AccommodationImage;
import com.service.zerobnb.web.accommodation.domain.Event;
import com.service.zerobnb.web.accommodation.domain.PopularFacilityService;
import com.service.zerobnb.web.accommodation.dto.AccommodationInfoDto;
import com.service.zerobnb.web.accommodation.model.AccommodationImageInput;
import com.service.zerobnb.web.accommodation.model.AccommodationInput;
import com.service.zerobnb.web.accommodation.model.EventInput;
import com.service.zerobnb.web.accommodation.model.PopularFacilityServiceInput;
import com.service.zerobnb.web.accommodation.repository.AccommodationImageRepository;
import com.service.zerobnb.web.accommodation.repository.AccommodationRepository;
import com.service.zerobnb.web.accommodation.repository.EventRepository;
import com.service.zerobnb.web.accommodation.repository.PopularFacilityServiceRepository;
import com.service.zerobnb.web.error.model.AccommodationException;
import com.service.zerobnb.web.error.model.ValidationException;
import com.service.zerobnb.web.host.domain.Host;
import com.service.zerobnb.web.host.service.HostService;
import com.service.zerobnb.web.room.domain.Room;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.Optional;

import static com.service.zerobnb.util.type.AccommodationType.PENSION;
import static com.service.zerobnb.web.error.message.ExceptionMessage.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@SpringBootTest
class AccommodationServiceTest {
    @Autowired
    private AccommodationService accommodationService;

    @MockBean
    private AccommodationRepository accommodationRepository;

    @MockBean
    private AccommodationImageRepository accommodationImageRepository;

    @MockBean
    private EventRepository eventRepository;

    @MockBean
    private PopularFacilityServiceRepository popularFacilityServiceRepository;

    @MockBean
    private HostService hostService;

    @Test
    void registerAccommodation() {
        when(hostService.findHostById(any())).thenReturn(Host.builder().id(25L).status(HostStatus.ACTIVE).build());
        when(accommodationRepository.save(any())).thenReturn(Accommodation.builder().build());
        when(accommodationImageRepository.saveAll(any())).thenReturn(Arrays.asList(AccommodationImage.builder().build()));
        when(eventRepository.saveAll(any())).thenReturn(Arrays.asList(Event.builder().build()));
        when(popularFacilityServiceRepository.saveAll(any())).thenReturn(Arrays.asList(PopularFacilityService.builder().build()));

        assertDoesNotThrow(() -> accommodationService.registerAccommodation(AccommodationInput.builder()
                .type("pension")
                .address("???????????????????????????????????????6???23-1")
                .description("????????? ????????? ????????? ?????????????????????. ????????? ?????????????????????.")
                .locationPosition(LocationPosition.builder()
                        .latitude(25.55)
                        .longitude(35.55)
                        .build())
                .name("?????????")
                .notice("?????? ????????? ?????? ??? ????????? ???????????????.")
                .policy("???1???: ????????? ??? ????????? ???????????? ?????????.")
                .eventInputs(Arrays.asList(
                        EventInput.builder().description("???????????? ?????? ?????? ").build(),
                        EventInput.builder().description("????????? ?????? ??????").build(),
                        EventInput.builder().description("?????? ??? ????????? ?????? ?????? ??????").build()
                ))
                .popularFacilityServiceInputs(Arrays.asList(
                        PopularFacilityServiceInput.builder().popularFacilityServiceType(1).build(),
                        PopularFacilityServiceInput.builder().popularFacilityServiceType(2).build(),
                        PopularFacilityServiceInput.builder().popularFacilityServiceType(5).build(),
                        PopularFacilityServiceInput.builder().popularFacilityServiceType(7).build(),
                        PopularFacilityServiceInput.builder().popularFacilityServiceType(8).build(),
                        PopularFacilityServiceInput.builder().popularFacilityServiceType(10).build(),
                        PopularFacilityServiceInput.builder().popularFacilityServiceType(11).build()
                ))
                .accommodationImageInputs(Arrays.asList(
                        AccommodationImageInput.builder().url("https://img.com/test1.png").build(),
                        AccommodationImageInput.builder().url("https://img.com/test2.png").build(),
                        AccommodationImageInput.builder().url("https://img.com/test3.png").build(),
                        AccommodationImageInput.builder().url("https://img.com/test4.png").build(),
                        AccommodationImageInput.builder().url("https://img.com/test5.png").build()
                ))
                .hostId(1L)
                .build()));
        verify(hostService, times(2)).findHostById(any());
        verify(accommodationRepository, times(1)).save(any());
        verify(accommodationImageRepository, times(1)).saveAll(any());
        verify(eventRepository, times(1)).saveAll(any());
        verify(popularFacilityServiceRepository, times(1)).saveAll(any());
    }

    @Test
    void deleteAccommodationTest() {
        when(accommodationRepository.existsById(any())).thenReturn(true);
        when(accommodationRepository.findById(any())).thenReturn(Optional.of(Accommodation.builder().build()));
        assertDoesNotThrow(() -> accommodationService.deleteAccommodation(1L));
        verify(accommodationRepository, times(1)).existsById(any());
        verify(accommodationRepository, times(1)).findById(any());
    }

    @Test
    @DisplayName("lat, lnt ??? null ??? ????????? ?????? - ?????? ?????? ?????? ?????? ??????")
    void nearDistOrTypeAccommodationTest() {
        // given
        Accommodation accommodation = Accommodation.builder()
                .id(1L)
                .name("?????????")
                .locationPosition(LocationPosition.builder()
                        .latitude(36.0)
                        .longitude(34.0)
                        .build())
                .address("??????????????? ????????????")
                .description("??????????????? ????????? ?????????~~")
                .accommodationType(PENSION)
                .roomList(Arrays.asList(Room.builder().id(1L)
                        .standardPeople(3).maxPeople(5)
                        .description("??????").smoke(false).cost(50000).discount(10).build()))
                .accommodationImageList(Arrays.asList(
                        AccommodationImage.builder().url("https://img.com/test1.png").build(),
                        AccommodationImage.builder().url("https://img.com/test2.png").build(),
                        AccommodationImage.builder().url("https://img.com/test3.png").build(),
                        AccommodationImage.builder().url("https://img.com/test4.png").build(),
                        AccommodationImage.builder().url("https://img.com/test5.png").build()
                ))
                .eventList(Arrays.asList(
                        Event.builder().description("???????????? ?????? ?????? ").build(),
                        Event.builder().description("????????? ?????? ??????").build(),
                        Event.builder().description("?????? ??? ????????? ?????? ?????? ??????").build()
                ))
                .popularFacilityServiceList(Arrays.asList(
                        PopularFacilityService.builder().popularFacilityServiceType(PopularFacilityServiceType.BAR).build(),
                        PopularFacilityService.builder().popularFacilityServiceType(PopularFacilityServiceType.FITNESS).build(),
                        PopularFacilityService.builder().popularFacilityServiceType(PopularFacilityServiceType.LAUNDRY).build()
                ))
                .build();
        given(accommodationRepository.findById(anyLong()))
                .willReturn(Optional.of(accommodation));

        // when
        ValidationException exception = assertThrows(ValidationException.class,
                () -> accommodationService.nearDistOrTypeAccommodation(null, 37.0, null));

        // then
        assertEquals(NOT_VALID_INPUT.message(), exception.getMessage());
    }

    @Test
    @DisplayName("????????? ?????? ?????? ????????? ????????? ?????? - ?????? ?????? ?????? ?????? ??????")
    void accommodationInfoTest() {
        // given
        Accommodation accommodation = Accommodation.builder()
                .id(1L)
                .name("?????????")
                .locationPosition(LocationPosition.builder()
                        .latitude(36.0)
                        .longitude(34.0)
                        .build())
                .address("??????????????? ????????????")
                .description("??????????????? ????????? ?????????~~")
                .accommodationType(PENSION)
                .roomList(Arrays.asList(Room.builder().id(1L)
                        .standardPeople(3).maxPeople(5)
                        .description("??????").smoke(false).cost(50000).discount(10).build()))
                .accommodationImageList(Arrays.asList(
                        AccommodationImage.builder().url("https://img.com/test1.png").build(),
                        AccommodationImage.builder().url("https://img.com/test2.png").build(),
                        AccommodationImage.builder().url("https://img.com/test3.png").build(),
                        AccommodationImage.builder().url("https://img.com/test4.png").build(),
                        AccommodationImage.builder().url("https://img.com/test5.png").build()
                ))
                .eventList(Arrays.asList(
                        Event.builder().description("???????????? ?????? ?????? ").build(),
                        Event.builder().description("????????? ?????? ??????").build(),
                        Event.builder().description("?????? ??? ????????? ?????? ?????? ??????").build()
                ))
                .popularFacilityServiceList(Arrays.asList(
                        PopularFacilityService.builder().popularFacilityServiceType(PopularFacilityServiceType.BAR).build(),
                        PopularFacilityService.builder().popularFacilityServiceType(PopularFacilityServiceType.FITNESS).build(),
                        PopularFacilityService.builder().popularFacilityServiceType(PopularFacilityServiceType.LAUNDRY).build()
                ))
                .build();
        given(accommodationRepository.findById(anyLong()))
                .willReturn(Optional.of(accommodation));

        // when
        AccommodationInfoDto accommodationInfoDto = accommodationService.accommodationInfo(1L);

        // then
        assertEquals("?????????", accommodationInfoDto.getName());
    }

    @Test
    @DisplayName("?????? ????????? ?????? ?????? - ?????? ?????? ?????? ?????? ??????")
    void notExistAccommodationTest() {
        // given
        given(accommodationRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // when
        AccommodationException exception = assertThrows(AccommodationException.class,
                () -> accommodationService.accommodationInfo(2L));

        // then
        assertEquals(NOT_EXIST_ACCOMMODATION.message(), exception.getMessage());
    }
}