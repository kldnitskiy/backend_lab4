package Responses;

public class ValidateParameters {
    private String status;
    private String message;
    private String parameter;

    public ValidateParameters(String status, String message, String parameter){
        this.status = status;
        this.parameter = parameter;
        this.message = message;
    }
    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getParameter() {
        return parameter;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
}
