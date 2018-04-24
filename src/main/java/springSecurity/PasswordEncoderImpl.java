package springSecurity;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;

/**
 * @program: md
 * @description: springSecurity的密码加密服务、
 * @author: Zhang
 * @create: 2018-04-24 10:14
 **/
public class PasswordEncoderImpl implements PasswordEncoder {

    @Override
    public String encode(CharSequence charSequence) {
        try {
            //md5加密
            return DigestUtils.md5DigestAsHex(charSequence.toString().getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return encode(charSequence).equals(s);
    }
}
