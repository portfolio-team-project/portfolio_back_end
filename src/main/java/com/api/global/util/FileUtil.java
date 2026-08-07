package com.api.global.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.api.global.constants.MessageConstants;
import com.api.global.exception.BusinessException;

@Component
public class FileUtil {
	
	@Value("${file.upload.path}")
    private String basePath;
	
	 public String fileUpload(MultipartFile file, String path) {
		 
		String originalName = file.getOriginalFilename();
		 
		if (originalName == null || originalName.isBlank()) {
			throw new BusinessException(MessageConstants.INVALID_FILE_TYPE);
        }
		String ext = originalName.substring(originalName.lastIndexOf(".") + 1).toLowerCase();
		if (!List.of("jpg", "jpeg", "png", "gif", "webp").contains(ext)) {
			throw new BusinessException(MessageConstants.INVALID_FILE_TYPE);
		}
		
	    String contentType = file.getContentType();
	    if (contentType == null || !contentType.startsWith("image/")) {
	        throw new BusinessException(MessageConstants.INVALID_FILE_TYPE);
	    }

	    validateImageSignature(file, ext);

        LocalDateTime date = LocalDateTime.now();

        String year = String.valueOf(date.getYear());
        String month = String.valueOf(date.getMonthValue());

        String subPath = year + "/" + month + "/" + path;
        String urlPath = "/api/upload/" + subPath;
        String uploadPath = basePath + "/" + subPath;

        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        
		String uuid = UuidUtil.makeUuid();
        
		String safeName = Paths.get(file.getOriginalFilename()).getFileName().toString();
		String fileName = uuid + "_" + safeName;

		try {
			file.transferTo(new File(uploadPath + "/" + fileName));
		} catch (IllegalStateException | IOException e) {
			throw new BusinessException(MessageConstants.FILE_UPLOAD_FAILED);
		}
        
		return urlPath + "/" + fileName;
	 }
	 
	 private void validateImageSignature(MultipartFile file, String ext) {
	        byte[] header;
	        try (InputStream is = file.getInputStream()) {
	            header = is.readNBytes(12);
	        } catch (IOException e) {
	            throw new BusinessException(MessageConstants.INVALID_FILE_TYPE);
	        }

	        boolean valid = switch (ext) {
	            case "jpg", "jpeg" -> header.length >= 3
	                    && (header[0] & 0xFF) == 0xFF
	                    && (header[1] & 0xFF) == 0xD8
	                    && (header[2] & 0xFF) == 0xFF;
	            case "png" -> header.length >= 8
	                    && (header[0] & 0xFF) == 0x89 && header[1] == 0x50
	                    && header[2] == 0x4E && header[3] == 0x47
	                    && header[4] == 0x0D && header[5] == 0x0A
	                    && header[6] == 0x1A && header[7] == 0x0A;
	            case "gif" -> header.length >= 6
	                    && header[0] == 'G' && header[1] == 'I' && header[2] == 'F'
	                    && header[3] == '8' && (header[4] == '7' || header[4] == '9') && header[5] == 'a';
	            case "webp" -> header.length >= 12
	                    && header[0] == 'R' && header[1] == 'I' && header[2] == 'F' && header[3] == 'F'
	                    && header[8] == 'W' && header[9] == 'E' && header[10] == 'B' && header[11] == 'P';
	            default -> false;
	        };

	        if (!valid) {
	            throw new BusinessException(MessageConstants.INVALID_FILE_TYPE);
	        }
	    }
}
