package com.whaletide.common.exception.handler;

import com.whaletide.common.api.CommonResult;
import com.whaletide.common.exception.auth.AccountDisabledException;
import com.whaletide.common.exception.auth.AccountLockedException;
import com.whaletide.common.exception.auth.AuthenticationException;
import com.whaletide.common.exception.auth.CredentialsException;
import com.whaletide.common.exception.auth.TokenInvalidException;
import com.whaletide.common.exception.auth.VerificationCodeException;
import com.whaletide.common.exception.base.BusinessException;
import com.whaletide.common.exception.base.ConcurrentOperationException;
import com.whaletide.common.exception.base.LimitExceededException;
import com.whaletide.common.exception.base.ResourceNotFoundException;
import com.whaletide.common.exception.base.ValidationException;
import com.whaletide.common.exception.config.ConfigurationException;
import com.whaletide.common.exception.file.FileOperationException;
import com.whaletide.common.exception.order.OrderNotFoundException;
import com.whaletide.common.exception.order.OrderStatusException;
import com.whaletide.common.exception.product.ProductNotFoundException;
import com.whaletide.common.exception.product.ProductStockException;
import com.whaletide.common.exception.service.RemoteServiceException;
import com.whaletide.common.exception.user.UserExistsException;
import com.whaletide.common.exception.user.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult<String> handleBusinessException(BusinessException e, HttpServletRequest request) {
        logger.error("业务异常: {}, 请求路径: {}", e.getMessage(), request.getRequestURI(), e);
        return CommonResult.failed(e.getMessage());
    }

    /**
     * 处理资源未找到异常
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonResult<String> handleResourceNotFoundException(ResourceNotFoundException e, HttpServletRequest request) {
        logger.error("资源未找到: {}, 请求路径: {}", e.getMessage(), request.getRequestURI(), e);
        return CommonResult.failed(e.getMessage());
    }
    
    /**
     * 处理特定资源未找到异常
     */
    @ExceptionHandler({UserNotFoundException.class, OrderNotFoundException.class, ProductNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonResult<String> handleSpecificResourceNotFoundException(BusinessException e, HttpServletRequest request) {
        logger.error("资源未找到: {}, 请求路径: {}", e.getMessage(), request.getRequestURI(), e);
        return CommonResult.failed(e.getMessage());
    }

    /**
     * 处理参数验证异常
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult<String> handleValidationException(ValidationException e, HttpServletRequest request) {
        logger.error("参数验证异常: {}, 请求路径: {}", e.getMessage(), request.getRequestURI(), e);
        return CommonResult.validateFailed(e.getMessage());
    }

    /**
     * 处理认证异常
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public CommonResult<String> handleAuthenticationException(AuthenticationException e, HttpServletRequest request) {
        logger.error("认证异常: {}, 请求路径: {}", e.getMessage(), request.getRequestURI(), e);
        return CommonResult.unauthorized(e.getMessage());
    }

    /**
     * 处理用户名密码错误异常
     */
    @ExceptionHandler(CredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public CommonResult<String> handleCredentialsException(CredentialsException e, HttpServletRequest request) {
        logger.error("凭证错误: {}, 请求路径: {}", e.getMessage(), request.getRequestURI(), e);
        return CommonResult.unauthorized(e.getMessage());
    }

    /**
     * 处理账户禁用异常
     */
    @ExceptionHandler(AccountDisabledException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public CommonResult<String> handleAccountDisabledException(AccountDisabledException e, HttpServletRequest request) {
        logger.error("账户已禁用: {}, 请求路径: {}", e.getMessage(), request.getRequestURI(), e);
        return CommonResult.forbidden(e.getMessage());
    }

    /**
     * 处理账户锁定异常
     */
    @ExceptionHandler(AccountLockedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public CommonResult<String> handleAccountLockedException(AccountLockedException e, HttpServletRequest request) {
        logger.error("账户已锁定: {}, 请求路径: {}", e.getMessage(), request.getRequestURI(), e);
        return CommonResult.forbidden(e.getMessage());
    }

    /**
     * 处理令牌无效异常
     */
    @ExceptionHandler(TokenInvalidException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public CommonResult<String> handleTokenInvalidException(TokenInvalidException e, HttpServletRequest request) {
        logger.error("令牌无效: {}, 请求路径: {}", e.getMessage(), request.getRequestURI(), e);
        return CommonResult.unauthorized(e.getMessage());
    }

    /**
     * 处理验证码错误异常
     */
    @ExceptionHandler(VerificationCodeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult<String> handleVerificationCodeException(VerificationCodeException e, HttpServletRequest request) {
        logger.error("验证码错误: {}, 请求路径: {}", e.getMessage(), request.getRequestURI(), e);
        return CommonResult.failed(e.getMessage());
    }

    /**
     * 处理用户已存在异常
     */
    @ExceptionHandler(UserExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult<String> handleUserExistsException(UserExistsException e, HttpServletRequest request) {
        logger.error("用户已存在异常: {}, 请求路径: {}", e.getMessage(), request.getRequestURI(), e);
        return CommonResult.failed(e.getMessage());
    }

    /**
     * 处理商品库存异常
     */
    @ExceptionHandler(ProductStockException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult<String> handleProductStockException(ProductStockException e, HttpServletRequest request) {
        logger.error("商品库存异常: {}, 请求路径: {}", e.getMessage(), request.getRequestURI(), e);
        return CommonResult.failed(e.getMessage());
    }

    /**
     * 处理订单状态异常
     */
    @ExceptionHandler(OrderStatusException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult<String> handleOrderStatusException(OrderStatusException e, HttpServletRequest request) {
        logger.error("订单状态异常: {}, 请求路径: {}", e.getMessage(), request.getRequestURI(), e);
        return CommonResult.failed(e.getMessage());
    }

    /**
     * 处理并发操作异常
     */
    @ExceptionHandler(ConcurrentOperationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public CommonResult<String> handleConcurrentOperationException(ConcurrentOperationException e, HttpServletRequest request) {
        logger.error("并发操作异常: {}, 请求路径: {}", e.getMessage(), request.getRequestURI(), e);
        return CommonResult.failed(e.getMessage());
    }

    /**
     * 处理限制超出异常
     */
    @ExceptionHandler(LimitExceededException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public CommonResult<String> handleLimitExceededException(LimitExceededException e, HttpServletRequest request) {
        logger.error("限制超出异常: {}, 请求路径: {}", e.getMessage(), request.getRequestURI(), e);
        return CommonResult.failed(e.getMessage());
    }

    /**
     * 处理文件操作异常
     */
    @ExceptionHandler(FileOperationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult<String> handleFileOperationException(FileOperationException e, HttpServletRequest request) {
        logger.error("文件操作异常: {}, 请求路径: {}", e.getMessage(), request.getRequestURI(), e);
        return CommonResult.failed(e.getMessage());
    }

    /**
     * 处理远程服务异常
     */
    @ExceptionHandler(RemoteServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResult<String> handleRemoteServiceException(RemoteServiceException e, HttpServletRequest request) {
        logger.error("远程服务异常: {}, 请求路径: {}", e.getMessage(), request.getRequestURI(), e);
        return CommonResult.failed(e.getMessage());
    }

    /**
     * 处理配置异常
     */
    @ExceptionHandler(ConfigurationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResult<String> handleConfigurationException(ConfigurationException e, HttpServletRequest request) {
        logger.error("配置异常: {}, 请求路径: {}", e.getMessage(), request.getRequestURI(), e);
        return CommonResult.failed(e.getMessage());
    }

    /**
     * 处理Spring参数绑定异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder message = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            message.append(fieldError.getField()).append(": ").append(fieldError.getDefaultMessage()).append(", ");
        }
        String errorMessage = message.length() > 0 ? message.substring(0, message.length() - 2) : "参数验证失败";
        logger.error("参数绑定异常: {}, 请求路径: {}", errorMessage, request.getRequestURI(), e);
        return CommonResult.validateFailed(errorMessage);
    }

    /**
     * 处理Spring参数绑定异常
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult<String> handleBindException(BindException e, HttpServletRequest request) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder message = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            message.append(fieldError.getField()).append(": ").append(fieldError.getDefaultMessage()).append(", ");
        }
        String errorMessage = message.length() > 0 ? message.substring(0, message.length() - 2) : "参数绑定失败";
        logger.error("参数绑定异常: {}, 请求路径: {}", errorMessage, request.getRequestURI(), e);
        return CommonResult.validateFailed(errorMessage);
    }

    /**
     * 处理约束违反异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult<String> handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        logger.error("约束违反异常: {}, 请求路径: {}", e.getMessage(), request.getRequestURI(), e);
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        StringBuilder sb = new StringBuilder();
        for (ConstraintViolation<?> violation : violations) {
            sb.append(violation.getMessage()).append("; ");
        }
        return CommonResult.validateFailed(sb.toString());
    }

    /**
     * 处理所有未预期的异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResult<String> handleException(Exception e, HttpServletRequest request) {
        logger.error("系统异常: {}, 请求路径: {}", e.getMessage(), request.getRequestURI(), e);
        return CommonResult.failed("系统异常，请联系管理员");
    }
} 