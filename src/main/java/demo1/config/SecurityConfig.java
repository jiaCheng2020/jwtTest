package demo1.config;



import demo1.filter.JwtAuthenticationFilter;
import demo1.handler.JwtAccessDeniedHandler;
import demo1.handler.JwtAuthenticationEntryPoint;
import demo1.handler.JwtFailureHandler;
import demo1.handler.JwtSuccessHandler;
import demo1.service.impl.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@EnableWebSecurity()
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtSuccessHandler jwtSuccessHandler;

    @Autowired
    private JwtFailureHandler jwtFailureHandler;

    @Autowired
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Autowired
    private JwtAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
   private MyUserDetailsService detailsService;

    @Bean(name="passwordEncoder")
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    //验证管理器配置
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(detailsService).passwordEncoder(passwordEncoder());
    }

    //http过滤配置
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http    .csrf().disable()//关闭跨站访问保护
                .cors()//开启跨站访问
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//配置session方针为无状态,即不自动创建session
                .and()
                .authorizeRequests().anyRequest().authenticated()//任何请求都需要进行身份验证
                .and()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).accessDeniedHandler(jwtAccessDeniedHandler)
                .and().addFilterBefore(jwtAuthenticationFilter,UsernamePasswordAuthenticationFilter.class)
                .formLogin().successHandler(jwtSuccessHandler).failureHandler(jwtFailureHandler).permitAll()//配置登录验证,成功后返回jwt
                .and()
                .logout().permitAll();//登出


    }

}
