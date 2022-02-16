package com.sunhongbing.petadoption.config;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
	@Bean
	public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
		System.out.println("ShiroConfig.shirFilter()");
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager);

		// 设置拦截后跳转的请求路径
		shiroFilterFactoryBean.setLoginUrl("/fore/");
		// 登录成功后要跳转的链接，首页
//		shiroFilterFactoryBean.setSuccessUrl("/admin/index");
		// 未授权页面;
		shiroFilterFactoryBean.setUnauthorizedUrl("/admin/unAuth");
		// 拦截器，此处应该使用LinkedHashMap，否则会出现资源只能加载一次然后就被拦截的情况
		Map<String,String> filterChainDefinitionMap = new LinkedHashMap<>();
		// 配置不会被拦截的链接，顺序判断
		filterChainDefinitionMap.put("/static/**", "anon");
		filterChainDefinitionMap.put("/fore/**", "anon");

		//adminUserLogin
		filterChainDefinitionMap.put("/admin/login", "anon");
		//commonUserLogin
		filterChainDefinitionMap.put("/login", "anon");

		// 配置退出登录过滤器，退出代码Shiro已经实现了
		filterChainDefinitionMap.put("/admin/logout", "logout");
		// 过滤链定义，从上向下顺序执行，一般将/**放在最为下边
		// authc:所有url都必须认证通过才可以访问 anon:所有url都都可以匿名访问
		filterChainDefinitionMap.put("/admin/**", "authc");

		// 自定义filters，覆盖默认的Filter列表
		Map<String, Filter> filters = new LinkedHashMap<>();
		// 定制logout 过滤，指定注销后跳转到登录页(默认为根路径)
		LogoutFilter logoutFilter = new LogoutFilter();
		logoutFilter.setRedirectUrl("/fore/");
		filters.put("logout", logoutFilter);
		shiroFilterFactoryBean.setFilters(filters);

		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return shiroFilterFactoryBean;
	}

	/**
	 * 凭证匹配器
	 * （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了）
	 */
	// 因为我们的密码是加过密的，所以，如果要Shiro验证用户身份的话，需要告诉它我们用的是md5加密的，并且是加密了两次。
	// 同时我们在自己的Realm中也通过SimpleAuthenticationInfo返回了加密时使用的盐。
	// 这样Shiro就能顺利的解密密码并验证用户名和密码是否正确了。
	@Bean
	public HashedCredentialsMatcher hashedCredentialsMatcher(){
		HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
		hashedCredentialsMatcher.setHashAlgorithmName("md5");//散列算法:这里使用MD5算法;
		hashedCredentialsMatcher.setHashIterations(2);//散列的次数，比如散列两次，相当于 md5(md5(""));
		return hashedCredentialsMatcher;
	}

	@Bean
	public MyShiroRealm myShiroRealm(){
		MyShiroRealm myShiroRealm = new MyShiroRealm();
//		myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());// 设置解密规则
		return myShiroRealm;
	}

	// SecurityManager是Shiro架构的核心，通过它来链接Realm和用户(文档中称之为Subject.)
	@Bean
	public SecurityManager securityManager(){
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(myShiroRealm());
		return securityManager;
	}

	/**
	 *  开启shiro aop注解支持，不开启无法使用 shiro 的注解.
	 *  使用代理方式;所以需要开启代码支持;
	 * @param securityManager
	 * @return
	 */
	/** * Shiro生命周期处理器 * @return */
	@Bean
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
		return new LifecycleBeanPostProcessor();
	}
	@Bean
	@DependsOn({"lifecycleBeanPostProcessor"})
	public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator(){
		DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
		advisorAutoProxyCreator.setProxyTargetClass(true);
		return advisorAutoProxyCreator;
	}
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(){
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
		return authorizationAttributeSourceAdvisor;
	}
}