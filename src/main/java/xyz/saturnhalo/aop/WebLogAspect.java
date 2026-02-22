package xyz.saturnhalo.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import xyz.saturnhalo.anno.MethodDescription;

import java.lang.reflect.Method;

/**
 * 全局 Web 请求日志切面类
 */
@Slf4j
@Aspect
@Component
public class WebLogAspect {

    // ANSI 颜色常量
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private final ObjectMapper objectMapper = new ObjectMapper();

    // AOP 切入点
    @Pointcut("execution(public * xyz.saturnhalo.controller..*.*(..))")

    public void webLog() {
    }

    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        StringBuilder logBuilder = new StringBuilder();

        // 1. 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;

        String method = request != null ? request.getMethod() : "UNKNOWN";
        String uri = request != null ? request.getRequestURI() : "UNKNOWN";
        String ip = request != null ? request.getRemoteAddr() : "UNKNOWN";

        String description = "未注明业务信息";
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method targetMethod = signature.getMethod();
            MethodDescription methodDescription = targetMethod.getAnnotation(MethodDescription.class);
            if (methodDescription != null && !methodDescription.value().isEmpty()) {
                description = methodDescription.value();
            }
        } catch (Exception ignored) {
        }

        // 2. 拼接请求日志
        logBuilder.append("\n").append(ANSI_BLUE).append("========================================== Web Log ==========================================").append(ANSI_RESET).append("\n");
        logBuilder.append(ANSI_CYAN).append("Description  : ").append(ANSI_RESET).append(description).append("\n");
        logBuilder.append(ANSI_CYAN).append("URL          : ").append(ANSI_RESET).append(uri).append("\n");
        logBuilder.append(ANSI_CYAN).append("HTTP Method  : ").append(ANSI_RESET).append(method).append("\n");
        logBuilder.append(ANSI_CYAN).append("IP           : ").append(ANSI_RESET).append(ip).append("\n");
        logBuilder.append(ANSI_CYAN).append("Class Method : ").append(ANSI_RESET).append(ANSI_YELLOW).append(joinPoint.getSignature().getDeclaringTypeName())
                .append(".").append(joinPoint.getSignature().getName()).append(ANSI_RESET).append("\n");

        // 3. 拼接请求参数
        try {
            Object[] args = joinPoint.getArgs();
            if (args.length > 0) {
                boolean hasFile = false;
                for (Object arg : args) {
                    if (arg instanceof MultipartFile || arg instanceof MultipartFile[]) {
                        hasFile = true;
                        break;
                    }
                }
                if (!hasFile) {
                    logBuilder.append(ANSI_CYAN).append("Request Args : ").append(ANSI_RESET).append(objectMapper.writeValueAsString(args)).append("\n");
                } else {
                    logBuilder.append(ANSI_CYAN).append("Request Args : ").append(ANSI_RESET).append("[File Upload]\n");
                }
            }
        } catch (Exception e) {
            logBuilder.append(ANSI_CYAN).append("Request Args : ").append(ANSI_RESET).append("Parse Failed\n");
        }

        // 4. 执行目标方法
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            long timeCost = System.currentTimeMillis() - startTime;
            logBuilder.append(ANSI_CYAN).append("Time Cost    : ").append(ANSI_RESET).append(ANSI_RED).append(timeCost).append(" ms").append(ANSI_RESET).append("\n");
            logBuilder.append(ANSI_RED).append("Exception    : ").append(e.getMessage()).append(ANSI_RESET).append("\n");
            logBuilder.append(ANSI_BLUE).append("=============================================================================================").append(ANSI_RESET);
            log.error(logBuilder.toString());
            throw e;
        }

        // 5. 拼接响应日志
        long timeCost = System.currentTimeMillis() - startTime;
        String costColor = timeCost > 1000 ? ANSI_RED : ANSI_GREEN;

        logBuilder.append(ANSI_CYAN).append("Time Cost    : ").append(ANSI_RESET).append(costColor).append(timeCost).append(" ms").append(ANSI_RESET).append("\n");

        try {
            String resp = objectMapper.writeValueAsString(result);
            if (resp.length() > 1000) {
                resp = resp.substring(0, 1000) + "...";
            }
            logBuilder.append(ANSI_CYAN).append("Response     : ").append(ANSI_RESET).append(resp).append("\n");
        } catch (Exception ignored) {
            logBuilder.append(ANSI_CYAN).append("Response     : ").append(ANSI_RESET).append("Parse Failed\n");
        }

        logBuilder.append(ANSI_BLUE).append("=============================================================================================").append(ANSI_RESET);

        log.info(logBuilder.toString());

        return result;
    }
}