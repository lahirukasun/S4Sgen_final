package itech.s4sgen.config;

import org.springframework.security.crypto.password.PasswordEncoder;

import itech.s4sgen.utils.HelpingClass;

public class Md5PasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence charSequence) {
        return HelpingClass.cryptWithMD5(charSequence.toString());
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return encode(charSequence).equals(s);
    }
}