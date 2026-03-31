package org.example.java_sb_template.config.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

/**
 * [AOP 기반 전역 API 로깅 가이드]
 * 1. AOP(Aspect Oriented Programming) 도입 배경
 *    - 핵심 비즈니스 로직과 부가 기능(로깅, 보안, 트랜잭션)의 완전한 분리
 *    - 수백 개의 API 메서드마다 반복되는 로깅 코드 작성의 비효율성 제거
 * 2. AOP 미사용 시 발생하는 문제 (Manual Logging)
 *    - 코드 중복: 모든 서비스/컨트롤러 메서드에 System.currentTimeMillis() 등 수동 삽입 필요
 *    - 유지보수 저하: 로그 형식 변경 시 대상이 되는 모든 파일을 일일이 수정해야 함 (실수 유발)
 *    - 가독성 오염: 순수 비즈니스 로직보다 로깅 코드가 더 길어지는 주객전도 현상 발생
 * 3. AOP 사용 시 이점 (Automated Logging)
 *    - 자동화: @Around 설정을 통해 특정 패키지의 모든 호출을 투명하게 가로채 기록함
 *    - 중앙 집중 관리: 로그 기록 방식이나 성능 측정 로직 변경 시 본 클래스만 수정하면 전체 즉시 반영
 *    - 비즈니스 집중: 실제 로직(Controller)에는 부가 기능 코드가 전무하여 가독성 및 유지보수성 극대화
 * 4. 핵심 메커니즘
 *    - Pointcut: 어디서(Where) 로그를 찍을지 정의 (여기서는 모든 Controller 대상)
 *    - Advice: 언제(When), 무엇을(What) 할지 정의 (여기서는 실행 전후로 시간 및 성공/실패 기록)
 */
@Slf4j // 로그 출력을 위한 SLF4J 로거 자동 생성
@Aspect // 공통 관심 사항을 분리하여 적용하는 AOP 클래스 선언
@Component // 스프링 컨테이너에서 관리하는 빈으로 등록
public class ApiLoggingAspect {

    /**
     * 프로젝트 내 모든 Controller 계층의 메서드를 타겟으로 지정하는 포인트컷
     * org.example.java_sb_template 패키지 하위의 'Controller'로 끝나는 모든 클래스 포함
     */
    @Pointcut("within(org.example.java_sb_template..*Controller)")
    public void controllerPointcut() {}

    /**
     * 대상 메서드 실행 전후로 로깅 및 실행 시간을 측정하는 핵심 로직
     */
    @Around("controllerPointcut()") // 지정된 포인트컷 실행 전후에 가로채기 수행
    public Object logApiExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        // 현재 스레드의 HTTP 요청 객체 확보
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String method = request.getMethod().toUpperCase(); // 호출된 HTTP 메서드 (GET, POST 등)
        
        long start = System.currentTimeMillis(); // 시스템 현재 시간 기록 (시작)
        
        try {
            // 실제 대상 메서드(Controller 로직) 실행
            Object result = joinPoint.proceed();
            
            long executionTime = System.currentTimeMillis() - start; // 처리 소요 시간 산출

            // 로그 동적 파일 분리를 위한 메타데이터(MDC) 설정
            MDC.put("logType", "access"); // 성공 폴더 경로 식별자 지정
            MDC.put("method", method); // 메서드별 폴더 경로 식별자 지정
            
            // 성공 로그 기록
            log.info("[API SUCCESS] {} {} | Method: {} | Execution Time: {}ms",
                    method, request.getRequestURI(), joinPoint.getSignature().toShortString(), executionTime);
            
            return result;
        } catch (Exception e) {
            // 예외 발생 시 에러용 로그 경로로 변경
            MDC.put("logType", "errors"); // 에러 폴더 경로 식별자 지정
            MDC.put("method", "ERROR"); // 에러 통합 폴더 지정
            
            // 에러 상황 로그 기록
            log.error("[API ERROR] {} {} | Method: {} | Error: {}",
                    method, request.getRequestURI(), joinPoint.getSignature().toShortString(), e.getMessage());
            throw e; // 가로챈 예외를 다시 던져 GlobalExceptionHandler가 처리할 수 있도록 함
        } finally {
            // 요청 처리가 끝나면 로그 컨텍스트를 반드시 초기화하여 메모리 누수 방지
            MDC.clear();
        }
    }
}
