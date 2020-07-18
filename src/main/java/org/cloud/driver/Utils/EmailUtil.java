package org.cloud.driver.Utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import java.util.concurrent.Callable;

/**
 * 邮箱工具类
 */

public class EmailUtil implements Callable<Boolean> {
    private String emailAddress;
    private String code;

    public EmailUtil(String emailAddress, String code){
        this.emailAddress = emailAddress;
        this.code = code;
    }

    //验证
    public boolean sendEmail(){
        try {
            HtmlEmail email = new HtmlEmail();//不用更改
            email.setHostName("smtp.qq.com");//需要修改，126邮箱为smtp.126.com,163邮箱为163.smtp.com，QQ为smtp.qq.com
            email.setCharset("UTF-8");
            // 收件地址
            email.addTo(this.emailAddress);
            //邮箱地址和用户名
            email.setFrom("874549232@qq.com","JunkCloud");
            //邮箱地址和客户端授权码
            email.setAuthentication("874549232@qq.com","uznulqxhcqlxbdge");
            //填写邮件名，邮件名可任意填写
            email.setSubject("请激活您的JunkCloud账户");
            email.setMsg("尊敬的用户您好,您本次注册的验证码是:<p style=\"color:red\"><B>" + this.code + "</B>");//此处填写邮件内容
            //阿里云25号端口默认不开启 改为这个
            email.setSmtpPort(465);
            email.setSSLOnConnect(true);
            email.send();
            return true;
        } catch (EmailException e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public Boolean call() throws Exception {
        return this.sendEmail();
    }
}
