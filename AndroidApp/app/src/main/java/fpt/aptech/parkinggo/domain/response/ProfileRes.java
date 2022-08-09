package fpt.aptech.parkinggo.domain.response;



import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.ext.JodaDeserializers;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ProfileRes implements  Serializable{
    private Integer identitycard;
    private String fullname;
//    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
//    @JsonSerialize(using = LocalDateTimeSerializer.class)
    //@JsonFormat(pattern = "yyyy-MM-dd'T'HH: mm: ss.SSS", shape = JsonFormat.Shape.STRING)
    private LocalDate dob;
    private Double balance;
    private String email;
    private Integer phone;
    private byte[] qrcontent;

    public ProfileRes() {
    }

    public Integer getIdentitycard() {
        return identitycard;
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

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = LocalDate.parse(dob);
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
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

    public byte[] getQrcontent() {
        return qrcontent;
    }

    public void setQrcontent(byte[] qrcontent) {
        this.qrcontent = qrcontent;
    }
}
