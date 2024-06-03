package com.ph.utils;

import com.ph.exception.customs.ImageException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ImageUtil {

    public static byte[] compressImage(byte[] data) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length)) {
            Deflater deflater = new Deflater();
            deflater.setLevel(Deflater.BEST_COMPRESSION);
            deflater.setInput(data);
            deflater.finish();


            byte[] tmp = new byte[4 * 1024];
            while (!deflater.finished()) {
                int size = deflater.deflate(tmp);
                outputStream.write(tmp, 0, size);
            }
            deflater.end();
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new ImageException("Cannot compress image");
        }
    }

    public static byte[] decompressImage(byte[] data) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length)) {

            Inflater inflater = new Inflater();
            inflater.setInput(data);
            byte[] tmp = new byte[4 * 1024];

            while (!inflater.finished()) {
                int count = inflater.inflate(tmp);
                outputStream.write(tmp, 0, count);
            }
             inflater.end();
            return outputStream.toByteArray();
        } catch (IOException | java.util.zip.DataFormatException e) {
           throw new ImageException("Cannot decompress image");
        }
    }

}
