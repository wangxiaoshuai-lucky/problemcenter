package com.kelab.problemcenter.aop;

import com.alibaba.fastjson.JSON;
import com.kelab.info.context.Context;
import com.kelab.problemcenter.constant.enums.CacheBizName;
import com.kelab.problemcenter.dal.redis.RedisCache;
import com.kelab.problemcenter.support.ContextLogger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;

@Aspect
@Component
public class LogAspect {

    private ContextLogger log;

    private final RedisCache redisCache;

    public LogAspect(ContextLogger log,
                     RedisCache redisCache) {
        this.log = log;
        this.redisCache = redisCache;
    }


    @Pointcut("execution(public * com.kelab.problemcenter.controller.*.*(..)) || " +
            "execution(public * com.kelab.problemcenter.support.service.*.*(..))")
    public void LogPointCut() {
    }


    @AfterThrowing(pointcut = "LogPointCut()", throwing = "e")
    public void saveExceptionLog(JoinPoint joinPoint, Throwable e) {
        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        assert requestAttributes != null;
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        assert request != null;
        Object[] args = joinPoint.getArgs();
        // 获取context
        Context context = (Context) args[0];
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        log.error(context, "*************error occurred*************\n" +
                "\tapi:%s\n" +
                "\tlogId:%s\n" +
                "\tuserId:%s\n" +
                "\troleId:%s\n" +
                "\targs:%s\n" +
                "\terr:%s\n" +
                "*****************************************", request.getRequestURL().toString(), context.getLogId(), context.getOperatorId(), context.getOperatorRoleId(), JSON.toJSONString(args), sw.toString());
    }
}
