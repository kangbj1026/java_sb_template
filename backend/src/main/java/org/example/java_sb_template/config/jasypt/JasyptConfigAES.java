package org.example.java_sb_template.config.jasypt;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 애플리케이션에서 민감한 설정 정보를 암호화하고 복호화하기 위한 Jasypt 설정 클래스
 * AES 기반 알고리즘을 사용하며, 환경 변수로부터 암호화 비밀번호와 알고리즘을 로드함
 * Jasypt의 StringEncryptor 빈을 등록해 프로퍼티 파일의 암호화된 값을 자동으로 해석하도록 구성
 * .
 * EnableEncryptableProperties
 * 애플리케이션의 설정 파일(application.yml 등)에 포함된 암호화된 값을
 * 실행 시점에 자동으로 복호화하도록 활성화하는 어노테이션
 * 해당 어노테이션이 적용되면 `${ENC(암호화된값)}` 형태의 프로퍼티를 읽을 때
 * Jasypt 설정을 기반으로 복호화된 값이 주입
 */
@Configuration
@EnableEncryptableProperties
public class JasyptConfigAES
{
	// 암호화 비밀번호 ( 환경 변수에서 주입 )
	@Value("${ENCRYPTOR.PASSWORD}")
	private String password;
	
	// 암호화 알고리즘 ( 환경 변수에서 주입 )
	@Value("${ENCRYPTOR.ALGORITHM}")
	private String algorithm;
	
	/**
	 * AES 기반의 문자열 암호화기를 생성하여 빈으로 등록
	 * PooledPBEStringEncryptor를 사용해 성능을 유지하면서 보안을 강화하고,
	 * 반복 해싱, Salt, IV(Initialization Vector) 설정으로 안전성을 높임
	 * Base64 방식으로 암호화된 문자열을 출력
	 *
	 * @return Jasypt 문자열 암호화기
	 */
	@Bean("jasyptEncryptorAES")
	public StringEncryptor stringEncryptor()
	{
		// 문자열 암호화 및 복호화를 담당하는 Jasypt의 암호화기 인스턴스를 생성
		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		// 암호화에 필요한 설정 정보를 정의하는 객체를 생성
		SimpleStringPBEConfig config = new SimpleStringPBEConfig();
		
		config.setPassword(password); // 암호화 및 복호화에 사용할 비밀 키를 지정
		config.setAlgorithm(algorithm); // 사용할 암호화 알고리즘을 지정 , 예를 들어 AES256, PBEWithMD5AndDES 등의 알고리즘
		config.setKeyObtentionIterations("1000"); // 키 생성 시 해싱을 몇 번 반복할지 설정
		config.setPoolSize("1"); // 암호화기 인스턴스 풀의 크기를 설정
		config.setProviderName("SunJCE"); // 암호화 연산을 제공할 보안 프로바이더를 지정 , 일반적으로 자바 기본 암호화 모듈인 SunJCE를 사용
		config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator"); // 암호화 시 사용할 Salt 값을 랜덤으로 생성하는 클래스 지정
		config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator"); // 초기화 벡터(IV)를 랜덤으로 생성하도록 설정
		config.setStringOutputType("base64"); // 암호화 결과를 Base64 문자열 형태로 출력하도록 설정
		
		encryptor.setConfig(config); // 위에서 정의한 암호화 설정(config)을 암호화기 인스턴스에 적용 , encryptor는 지정된 알고리즘과 키 설정을 기반으로 동
		return encryptor;
	}
}