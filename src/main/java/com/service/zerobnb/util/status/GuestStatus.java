package com.service.zerobnb.util.status;


public enum GuestStatus {
    /**
     * 이메일 미인증 회원
     */
    NOT_AUTH("NOT_AUTH"),

    /**
     * 활성(이용중) 회원
     */
    ACTIVE("ACTIVE"),

    /**
     * 탈퇴 된 회원
     */
    WITHDRAW("WITHDRAW"),

    /**
     * 정지 된 회원
     */
    STOP("STOP"),

    /**
     * 관리자
     */
    ADMIN("ADMIN");

    private final String status;

    GuestStatus(String status) {
        this.status = status;
    }
}
