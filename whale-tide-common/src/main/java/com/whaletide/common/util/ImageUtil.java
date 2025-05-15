package com.whaletide.common.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 图片处理工具类
 */
public class ImageUtil {

    // 允许的图片类型
    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList("image/jpeg", "image/jpg", "image/png", "image/gif");
    
    // 图片类型后缀名
    private static final List<String> ALLOWED_IMAGE_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif");

    /**
     * 对图片文件进行Base64编码
     * @param imageFile 图片文件
     * @return Base64编码后的字符串
     */
    public static String base64Encode(File imageFile) {
        if (imageFile == null || !imageFile.exists()) {
            return null;
        }
        byte[] fileBytes = FileUtil.readBytes(imageFile);
        return Base64.encode(fileBytes);
    }

    /**
     * 对MultipartFile进行Base64编码
     * @param file 上传的文件
     * @return Base64编码后的字符串
     */
    public static String base64Encode(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }
        byte[] fileBytes = file.getBytes();
        return Base64.encode(fileBytes);
    }

    /**
     * 对Base64字符串解码为字节数组
     * @param encodedImg Base64编码的图片字符串
     * @return 解码后的字节数组
     */
    public static byte[] base64Decode(String encodedImg) {
        if (encodedImg == null || encodedImg.isEmpty()) {
            return null;
        }
        return Base64.decode(encodedImg);
    }

    /**
     * 保存Base64图片到文件
     * @param base64Image Base64编码的图片字符串
     * @param outputFile 输出文件
     * @return 是否保存成功
     */
    public static boolean saveBase64Image(String base64Image, File outputFile) {
        if (base64Image == null || outputFile == null) {
            return false;
        }
        try {
            byte[] imageBytes = Base64.decode(base64Image);
            FileUtil.writeBytes(imageBytes, outputFile);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 检查文件是否为图片
     * @param file 上传的文件
     * @return 是否为图片
     */
    public static boolean isImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }
        String contentType = file.getContentType();
        String filename = file.getOriginalFilename();
        
        // 检查Content-Type
        boolean typeValid = ALLOWED_IMAGE_TYPES.contains(contentType);
        
        // 检查文件后缀
        boolean extValid = false;
        if (filename != null && filename.contains(".")) {
            String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
            extValid = ALLOWED_IMAGE_EXTENSIONS.contains(extension);
        }
        
        return typeValid && extValid;
    }

    /**
     * 压缩图片
     * @param imageBytes 原图片字节数组
     * @param format 图片格式（jpg, png, gif）
     * @param maxWidth 最大宽度
     * @param maxHeight 最大高度
     * @param quality 质量(0-1)
     * @return 压缩后的图片字节数组
     */
    public static byte[] compressImage(byte[] imageBytes, String format, int maxWidth, int maxHeight, float quality) throws IOException {
        if (imageBytes == null || imageBytes.length == 0) {
            return null;
        }
        
        // 读取原图片
        BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
        if (originalImage == null) {
            return null;
        }
        
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();
        
        // 计算新的尺寸
        int newWidth = originalWidth;
        int newHeight = originalHeight;
        
        if (originalWidth > maxWidth) {
            newWidth = maxWidth;
            newHeight = (int) (originalHeight * ((double) maxWidth / originalWidth));
        }
        
        if (newHeight > maxHeight) {
            newHeight = maxHeight;
            newWidth = (int) (newWidth * ((double) maxHeight / newHeight));
        }
        
        // 如果尺寸没变，且不需要调整质量，则直接返回
        if (newWidth == originalWidth && newHeight == originalHeight && quality >= 1.0f) {
            return imageBytes;
        }
        
        // 创建缩放后的图片
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, originalImage.getType());
        Graphics2D g = resizedImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        g.dispose();
        
        // 输出为字节数组
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, format, outputStream);
        
        return outputStream.toByteArray();
    }
    
    /**
     * 获取图片尺寸
     * @param imageBytes 图片字节数组
     * @return 图片尺寸数组 [width, height]
     */
    public static int[] getImageDimensions(byte[] imageBytes) throws IOException {
        if (imageBytes == null || imageBytes.length == 0) {
            return new int[]{0, 0};
        }
        
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
        if (image == null) {
            return new int[]{0, 0};
        }
        
        return new int[]{image.getWidth(), image.getHeight()};
    }
}
