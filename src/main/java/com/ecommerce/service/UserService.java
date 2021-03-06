package com.ecommerce.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import javax.transaction.Transactional;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.dto.ResponseDto;
import com.ecommerce.dto.user.SignInDto;
import com.ecommerce.dto.user.SignInResponseDto;
import com.ecommerce.dto.user.SignupDto;
import com.ecommerce.exceptions.AuthenticationFailException;
import com.ecommerce.exceptions.CustomException;
import com.ecommerce.model.AuthenticationToken;
import com.ecommerce.model.User;
import com.ecommerce.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public ResponseDto signUp(SignupDto signupDto) {

        // check if an user already here
        if (Objects.nonNull(userRepository.findByEmail(signupDto.getEmail()))) {
            throw new CustomException("User Already Present");

        }

        // hash the password

        String encryptedPassword = signupDto.getPassword();

        try {
            encryptedPassword = hashPassword(signupDto.getPassword());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();

        }

        // save the user

        User user = new User();

        user.setFirstName(signupDto.getFirstName());
        user.setLastName(signupDto.getLastName());
        user.setEmail(signupDto.getEmail());
        user.setPassword(encryptedPassword);
        userRepository.save(user);

        // save token

        final AuthenticationToken authenticationToken = new AuthenticationToken(user);
        authenticationService.saveConfirmationToken(authenticationToken);

        ResponseDto response = new ResponseDto("Got", "success");
        return response;
    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        String hash = DatatypeConverter.printHexBinary(digest).toUpperCase();

        return hash;

    }

    public SignInResponseDto signin(SignInDto signInDto) {

        //find the user

        User user=userRepository.findByEmail(signInDto.getEmail());

        //check if the entered email does not exist in the database
        if(Objects.isNull(user)){
            throw new AuthenticationFailException("User is not valid");
        }

        //compare user signin given passsword and hash the password with database stored password


        try {
            if(!user.getPassword().equals(hashPassword(signInDto.getPassword()))){
               throw new AuthenticationFailException("Wrong password");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        //if password match

        //then we will find token

        AuthenticationToken token=authenticationService.getToken(user);


        SignInResponseDto sResponse=new SignInResponseDto("signin success", token.getToken());
        
        
        return sResponse ;
    }

}
