package com.api.global.util;

import java.io.File;
import java.io.IOException;
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
		 
		LocalDateTime date = LocalDateTime.now();
		 
		String year = String.valueOf(date.getYear());
		String month = String.valueOf(date.getMonthValue());
		 
		String subPath = year + "/" + month + "/" + path;
		String urlPath = "/upload/" + subPath;
		String uploadPath = basePath + "/" + subPath;
		 
		File uploadDir = new File(uploadPath);
		if (!uploadDir.exists()) {
			uploadDir.mkdirs();
		}
        
		String uuid = UuidUtil.makeUuid();
        
		String fileName = uuid + "_" + file.getOriginalFilename();
		try {
			file.transferTo(new File(uploadPath + "/" + fileName));
		} catch (IllegalStateException | IOException e) {
			throw new BusinessException(MessageConstants.FILE_UPLOAD_FAILED);
		}
        
		return urlPath + "/" + fileName;
	 }
}
