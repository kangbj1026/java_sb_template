package org.example.java_sb_template.config.property;

import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.util.Properties;

/**
 * YAML 파일을 PropertySource로 읽어올 수 있게 하는 클래스
 * - 기본적으로 Spring은 @PropertySource에서 .properties만 지원
 * - YamlPropertySourceFactory를 사용하면 application-*.yml, env.yml 등 읽기 가능
 */
@Configuration
public class YamlPropertySourceFactory implements PropertySourceFactory
{
	@Override
	@Nonnull
	public PropertySource<?> createPropertySource(String name, EncodedResource resource)
	{
		YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
		factory.setResources(resource.getResource()); // YAML 파일 설정
		
		Properties properties = factory.getObject(); // YAML → Properties 변환
		if (properties == null) properties = new Properties();// null이면 빈 Properties로 초기화
		
		String filename = resource.getResource().getFilename();
		if (filename == null) filename = name != null ? name : "unknown.yml";// null이면 기본 이름 지정
		
		return new PropertiesPropertySource(filename, properties); // PropertySource 생성
	}
}
