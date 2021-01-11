package Responses;

public class RegisterUser {
    private String status;
    private String message;
    private String username;
    private String group_number;

    public RegisterUser(String status, String message, String username, String group_number){
        this.status = status;
        this.message = message;
        this.username = username;
        this.group_number = group_number;
    }
    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getUsername() {
        return username;
    }

    public String getGroup_number() {
        return group_number;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setGroup_number(String group_number) {
        this.group_number = group_number;
    }
}
