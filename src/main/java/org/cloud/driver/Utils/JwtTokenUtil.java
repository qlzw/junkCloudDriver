package org.cloud.driver.Utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.cloud.driver.exception.ExceptionCast;
import org.cloud.driver.response.ResultCode;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTokenUtil {
    private static String SERCET_KEY="chengwenjieshiswoerzi"; //密钥
    private static int EXPIRED_TIME=60;  //过期时间 6小时

    public static String createToken(String mail){
        //签发时间
        Date iatTime=new Date();
        //设置过期时间
        Calendar nowTime=Calendar.getInstance();
        //加60分钟
        nowTime.add(Calendar.MINUTE,EXPIRED_TIME);
        Date expiredTime=nowTime.getTime();
        HashMap<String,Object> map=new HashMap<>();
        map.put("alg","HS256");
        map.put("type","JWT");
        String token = JWT.create()
                .withHeader(map)
                .withClaim("name","JunkCloud")
                .withClaim("version","1.0")
                .withClaim("mail",mail)
                .withExpiresAt(expiredTime)//过期时间
                .withIssuedAt(iatTime)//签发时间
                .sign(Algorithm.HMAC256(SERCET_KEY));//加密
        return token;
    }

    public static Map<String, Claim> verifyToken(String token){
        JWTVerifier jwtVerifier=JWT.require(Algorithm.HMAC256(SERCET_KEY)).build();
        DecodedJWT jwt=null;
        try {
            jwt=jwtVerifier.verify(token);
        }catch (Exception e){
            ExceptionCast.cast(ResultCode.USER_TOKEN_ERROR);
        }
        return jwt.getClaims();
    }
}
