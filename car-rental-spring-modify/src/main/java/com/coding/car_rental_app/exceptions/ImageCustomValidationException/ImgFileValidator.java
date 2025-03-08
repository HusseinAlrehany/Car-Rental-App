package com.coding.car_rental_app.exceptions.ImageCustomValidationException;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;


public class ImgFileValidator implements ConstraintValidator<ImgValidation, MultipartFile> {

    //max file size 5 mb
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;
    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {

        if(file == null || file.isEmpty()){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Image can not be empty")
                    .addConstraintViolation();
            return false;
        }


        if(file.getSize() > MAX_FILE_SIZE){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Image size should be less than 5MB")
                    .addConstraintViolation();
            return false;
        }

        try(InputStream inputStream = file.getInputStream()) {
            if(!FileTypeValidator.isImage(inputStream)){
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("File not a valid image")
                        .addConstraintViolation();
                return false;
            }

        }catch(IOException ex){
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Failed to read the file")
                        .addConstraintViolation();
                return false;
        }

        return true;
    }
}
