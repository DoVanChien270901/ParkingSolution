package fpt.aptech.parkinggo.domain.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

public class EditProfileReq implements Serializable {
    private String identitycard;
    private String fullname;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private String dob;
    private String email;
    private String phone;

    public EditProfileReq() {
    }

    public String getIdentitycard() {
        return identitycard;
    }

    public EditProfileReq(String identitycard, String fullname, String dob, String email, String phone) {
        this.identitycard = identitycard;
        this.fullname = fullname;
        this.dob = dob;
        this.email = email;
        this.phone = phone;
    }

    public void setIdentitycard(String identitycard) {
        this.identitycard = identitycard;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
