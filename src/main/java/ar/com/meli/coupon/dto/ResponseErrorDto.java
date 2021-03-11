package ar.com.meli.coupon.dto;

public class ResponseErrorDto {

    private String code;
    private String message;

    public ResponseErrorDto() { }

    public ResponseErrorDto(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
