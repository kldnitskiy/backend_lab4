package Responses;

public class OperationResult {
    private String status;
    private String message;
    private Boolean point_status;

    public OperationResult(String status, String message, Boolean point_status){
        this.status = status;
        this.message = message;
        this.point_status = point_status;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public Boolean getPoint_status() {
        return point_status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPoint_status(Boolean point_status) {
        this.point_status = point_status;
    }
}
