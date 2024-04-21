package com.ruanyi.mifish.kernel.check;

import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;

import com.ruanyi.mifish.kernel.check.chain.SimpleMifishCheckChain;
import com.ruanyi.mifish.kernel.utils.ApplicationContextHolder;
import com.ruanyi.mifish.kernel.utils.MifishHttpUtils;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2024-04-21 15:08
 */
@Order(-99)
@Aspect
public class MifishCheckChainAspect {

    /** mifishCheckAspect */
    @Pointcut("@within(com.ruanyi.mifish.kernel.check.MifishCheck) || @annotation(com.ruanyi.mifish.kernel.check.MifishCheck)")
    public void mifishCheckAspect() {

    }

    /**
     * arround
     * 
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("mifishCheckAspect()")
    public Object arround(ProceedingJoinPoint pjp) throws Throwable {
        Signature signature = pjp.getSignature();
        if (signature instanceof MethodSignature) {
            Method method = ((MethodSignature)signature).getMethod();
            // 从方法上获取注解，配置信息
            MifishCheck mifishCheck = method.getAnnotation(MifishCheck.class);
            if (mifishCheck == null) {
                // 从类上获取注解，配置信息
                mifishCheck = pjp.getTarget().getClass().getAnnotation(MifishCheck.class);
            }
            // 构建上下文
            MifishCheckContext authContext = buildMifishCheckContext(method, pjp);
            // 构建链条
            List<MifishCheckNode> authNodes = ApplicationContextHolder.getBeans(MifishCheckNode.class);
            MifishCheckChain authChain = SimpleMifishCheckChain.buildSimpleChain(authNodes);
            authChain.doChain(mifishCheck, authContext);
        }
        // 继续做下一步业务
        Object result = pjp.proceed();
        return result;
    }

    /**
     * buildMifishCheckContext
     * 
     * @param method
     * @param pjp
     * @return
     */
    private MifishCheckContext buildMifishCheckContext(Method method, ProceedingJoinPoint pjp) {
        HttpServletRequest request = MifishHttpUtils.getHttpServletRequest();
        HttpServletResponse response = MifishHttpUtils.getHttpServletResponse();
        MifishCheckContext authContext = new MifishCheckContext();
        authContext.setRequest(request);
        authContext.setResponse(response);
        authContext.setMethod(method);
        authContext.setTarget(pjp.getTarget());
        authContext.setArgs(pjp.getArgs());
        return authContext;
    }
}
