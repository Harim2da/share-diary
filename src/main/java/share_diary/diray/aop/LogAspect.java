package share_diary.diray.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import share_diary.diray.common.utils.Utils;

@Slf4j
@Aspect
@Component
public class LogAspect {

    @Before("execution(* share_diary.diray..*.*(..)) && " +
            "within(@org.springframework.web.bind.annotation.RestController *)")
    public void requestParamLog(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().toString();
        String methodArgs = Utils.arrayToString(joinPoint.getArgs());
        log.info("request methodName = {}", methodName);
        log.info("request methodArgs = {}", methodArgs);
    }

    @AfterReturning("execution(* share_diary.diray..*.*(..)) &&" +
            "within(@org.springframework.web.bind.annotation.RestController *)")
    public void responseParamLog(JoinPoint joinPoint){
        String methodName = joinPoint.getSignature().toString();
        String methodArgs = Utils.arrayToString(joinPoint.getArgs());
        log.info("response methodName = {}", methodName);
        log.info("response methodArgs = {}", methodArgs);
    }
}
