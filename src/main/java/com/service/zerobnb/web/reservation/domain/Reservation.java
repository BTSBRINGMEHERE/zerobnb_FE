package com.service.zerobnb.web.reservation.domain;

import com.service.zerobnb.util.BaseTimeEntity;
import com.service.zerobnb.util.type.TransportationType;
import com.service.zerobnb.web.guest.domain.Guest;
import com.service.zerobnb.web.payment.domain.Payment;
import com.service.zerobnb.web.reservation.model.ReservationForm;
import com.service.zerobnb.web.room.domain.Room;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@ToString(exclude = {"guest", "room", "payment"})
@NoArgsConstructor
@AllArgsConstructor
public class Reservation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_id")
    private Guest guest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "reservation", cascade = CascadeType.ALL)
    private Payment payment;

    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;

    private int days;
    private long cost;
    private int peopleCount;

    @Enumerated(EnumType.STRING)
    private TransportationType transportationType;

    private String bookerName;
    private String bookerPhone;

    private boolean isReview;

    public static Reservation from(ReservationForm form, Guest guest, Room room, Payment payment) {
        return Reservation.builder()
                .guest(guest)
                .room(room)
                .payment(payment)
                .checkInTime(form.getCheckInTime())
                .checkOutTime(form.getCheckOutTime())
                .days(form.getDays(form.getCheckInTime(), form.getCheckOutTime()))
                .cost(payment.getReservationCost())
                .peopleCount(form.getPeopleCount())
                .transportationType(form.getTransportationType())
                .bookerName(guest.getName())
                .bookerPhone(guest.getPhone())
                .isReview(false)
                .build();
    }


}
