package com.coding.car_rental_app.validation.imgcustomvalidation;

import java.io.IOException;
import java.io.InputStream;


public class FileTypeValidator {

    // Magic numbers for common image file types
    private static final byte[] JPEG_SIGNATURE = {(byte) 0xFF, (byte) 0xD8}; // JPEG starts with FF D8
    private static final byte[] PNG_SIGNATURE = {(byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A}; // PNG starts with 89 50 4E 47 0D 0A 1A 0A
    private static final byte[] GIF_SIGNATURE = {'G', 'I', 'F'}; // GIF starts with 'GIF'

    //checking if the file is a valid img based on it's content
    //inputStream -> the file input stream
    //return true if the file is valid image , else false

    public static boolean isImage(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return false;
        }

        // Read the file signature of the input file(first few bytes)
        byte[] header = new byte[8];
        int bytesRead = inputStream.read(header);

        //if the bytes less than 2 bytes , it is not an image
        if (bytesRead < 2) {
            return false;
        }

        // Check for JPEG
        if (header[0] == JPEG_SIGNATURE[0] && header[1] == JPEG_SIGNATURE[1]) {
            return true;
        }

        // Check for PNG
        if (bytesRead >= 8 && matchesSignature(header, PNG_SIGNATURE)) {
            return true;
        }

        // Check for GIF
        if (bytesRead >= 3 && matchesSignature(header, GIF_SIGNATURE)) {
            return true;
        }

        return false; // Not a supported image type
    }

    //check if the file header matches the expected signature
    //header-> is the file header
    //signature-> is the expected signature
    //return true if header matches signature , else false.

    private static boolean matchesSignature(byte[] header, byte[] signature) {
        for (int i = 0; i < signature.length; i++) {
            if (header[i] != signature[i]) {
                return false;
            }
        }
        return true;
    }
}