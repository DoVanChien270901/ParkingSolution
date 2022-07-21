package fpt.aptech.parkinggo.domain.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

public class EditProfileReq implements Serializable {
    private Integer identitycard;
    private String fullname;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private String dob;
    private String email;
    private Integer phone;

    public EditProfileReq() {
    }

    public Integer getIdentitycard() {
        return identitycard;
    }

    public EditProfileReq(Integer identitycard, String fullname, String dob, String email, Integer phone) {
        this.identitycard = identitycard;
        this.fullname = fullname;
        this.dob = dob;
        this.email = email;
        this.phone = phone;
    }

    public void setIdentitycard(Integer identitycard) {
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

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }
}
