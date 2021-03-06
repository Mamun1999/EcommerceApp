package com.ecommerce.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="tokens")
public class AuthenticationToken {
    
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name="Id")  
 private Integer id;

 private String token;

 private Date createDate;

 @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
 @JoinColumn(nullable = false , name = "user_id" )
 private User user;

public Integer getId() {
    return id;
}

public void setId(Integer id) {
    this.id = id;
}

public String getToken() {
    return token;
}

public void setToken(String token) {
    this.token = token;
}

public Date getCreateDate() {
    return createDate;
}

public void setCreateDate(Date createDate) {
    this.createDate = createDate;
}

public User getUser() {
    return user;
}

public void setUser(User user) {
    this.user = user;
}

public AuthenticationToken() {
}

public AuthenticationToken(User user){
    this.user=user;
    this.createDate=new Date();
    this.token=UUID.randomUUID().toString();
}
 



}
