package demo1.tools;



import io.jsonwebtoken.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.*;

/**
 * jwt工具
 */
public class JwtUtils {
    public static final long EXPIRATION = 1000 * 60 * 10;//失效时间为30min
    public static final long VALIDATE_TIME = 1000 * 60 * 60 * 24;//允许刷新的时间为1天,够持久了
    public static final String ROLE_CLAIM = "rol";
    private static PublicKey publicKey;
    private static RSAPrivateCrtKey privateKey;


    static{
        InputStream is = JwtUtils.class.getClassLoader().getResourceAsStream("xxx.jks");

        try {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(is,"123456".toCharArray());
            privateKey =(RSAPrivateCrtKey) keyStore.getKey("abcdefg", "123456".toCharArray()); //读取keyTools生成的秘钥
            RSAPublicKeySpec spec= new RSAPublicKeySpec(privateKey.getModulus(), privateKey.getPublicExponent());
            publicKey = KeyFactory.getInstance("RSA").generatePublic(spec);//公钥
        }catch (Exception e){
             e.printStackTrace();
        }

    }

    /**
     * 通过验证成功后的Principal获取Jwt
     * @param user 用户凭证
     * @return
     */
    public static String createJwt(UserDetails user,long time){

          if (user == null || user.getUsername()==null || user.getAuthorities()==null){
              return null;
          }

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        Set<String> rols =new HashSet<>();
        authorities.forEach(authority -> rols.add(authority.getAuthority()));
        Map<String,Object> claims = new HashMap<>();
        claims.put(ROLE_CLAIM,rols);

        String jwt = Jwts.builder()
                .setClaims(claims).setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + time))
                .signWith(SignatureAlgorithm.RS256, privateKey)//使用私钥加密
                .compact();

          return jwt;
    }



    /**
     * 获取用户的角色信息
     * @param jwt
     * @return
     */
    public static List<String> getRoles(String jwt) {

        List<String> rols = null;
        try {
            rols = getClaims(jwt).get(ROLE_CLAIM,List.class);
        } catch (ExpiredJwtException e) {
            rols = e.getClaims().get(ROLE_CLAIM,List.class);
        }
        return rols;
    }

    /**
     * 获取用户的标识别信息，如id、name等
     * @param jwt
     * @return
     */
    public static String getSub(String jwt){
        String sub=null;
        try{
            sub = getClaims(jwt).getSubject();
        }catch (ExpiredJwtException e){
            sub = e.getClaims().getSubject();
        }
        return sub;
    }


    /**
     * 判断token是否过期
     * @param jwt
     * @return
     */
    public static boolean isExpiration(String jwt){
        Date expirationTime = null;
        try{
            expirationTime = getClaims(jwt).getExpiration();
            return expirationTime.before(new Date());
        }catch (ExpiredJwtException e){
            return true;
        }

    }

    //当jwt过期了会抛出异常,我们可以捕获异常来进行操作
    public static Claims getClaims(String jwt) throws JwtException {
        return  Jwts.parser()
                .setSigningKey(privateKey)//可以使用公钥进行解密
                .parseClaimsJws(jwt)
                .getBody();

    }

}
