package Model;

import Responses.ValidateParameters;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class Validation {
    private Boolean strict_mode = true;
    private String bad_parameter = null;
    private String message;
    public Boolean getStrict_mode() {
        return strict_mode;
    }

    public String getBad_parameter() {
        return bad_parameter;
    }

    public String getMessage(){
        return message;
    }

    public void validate_username(String username){
        if(username != null){
            if(username.length() < 4){
                bad_parameter = "Имя пользователя";
                message = "Имя пользователя не может быть меньше пяти символов";
            }
        }else{
            bad_parameter = "Имя пользователя";
            message = "Имя пользователя не заполнено";
        }

    }

    public void validate_pwd(String pwd){
        if(pwd != null){
            if(pwd.length() < 8){
                bad_parameter = "Пароль";
                message = "Пароль слишком слабый. Он должен содержать как минимум восемь символов";
            }
        }else{
            bad_parameter = "Пароль";
            message = "Пароль не заполнен";
        }

    }

    public void validate_isu(Integer isu){
        if(isu != null){
            if(isu < 100000){
                bad_parameter = "Номер ИСУ";
                message = "Номер ИСУ не может состоять меньше, чем из шести цифр";
            }
        }else{
            bad_parameter = "Номер ИСУ";
            message = "Номер ИСУ не заполнен";
        }

    }

    public void validate_email(String email){
        if(email != null){
            try {
                InternetAddress emailAddr = new InternetAddress(email);
                emailAddr.validate();
            } catch (AddressException ex) {
                bad_parameter = "Адрес электронной почты";
                message = "Адрес почты некорректен";
            }
        }else{
            bad_parameter = "Адрес электронной почты";
            message = "Адрес почты не заполнен";
        }


    }

    public void validate_repeat(String pwd, String pwd_repeat) {
        if(pwd_repeat != null && pwd != null){
            if(!pwd.equals(pwd_repeat)){
                bad_parameter = "Пароли";
                message = "Пароли не совпадают";
            }
        }else{
            bad_parameter = "Повторный пароль";
            message = "Повторный пароль или сам пароль не заполнены 0_0";
        }

    }
}
