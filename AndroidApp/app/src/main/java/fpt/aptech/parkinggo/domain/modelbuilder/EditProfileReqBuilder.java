package fpt.aptech.parkinggo.domain.modelbuilder;

import com.fasterxml.jackson.annotation.JsonFormat;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

import fpt.aptech.parkinggo.domain.request.EditProfileReq;

public class EditProfileReqBuilder {
    private Integer identitycard;
    private String fullname;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private String dob;
    private String email;
    private Integer phone;

    public EditProfileReqBuilder setIdentitycard(Integer identitycard) {
        this.identitycard = identitycard;
        return this;
    }

    public EditProfileReqBuilder setFullname(String fullname) {
        this.fullname = fullname;
        return this;
    }

    public EditProfileReqBuilder setDob(String dob) {
        this.dob = dob;
        return this;
    }

    public EditProfileReqBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public EditProfileReqBuilder setPhone(Integer phone) {
        this.phone = phone;
        return this;
    }

    public EditProfileReq createEditProfileReq() {
        return new EditProfileReq(identitycard, fullname, dob, email, phone);
    }
}