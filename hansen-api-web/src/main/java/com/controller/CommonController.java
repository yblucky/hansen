package com.controller;

import com.base.page.JsonResult;
import com.base.page.ResultCode;
import com.constant.RedisKey;
import com.model.User;
import com.redis.Strings;
import com.service.UserService;
import com.utils.numberutils.RandomUtil;
import com.utils.numberutils.UUIDUtil;
import com.utils.smsUtils.SmsTemplate;
import com.utils.smsUtils.SmsUtil;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller
@RequestMapping("/common")
public class CommonController {

    private static int width = 230;// 定义图片的width
    private static int height = 60;// 定义图片的height
    private static int codeCount = 4;// 定义图片上显示验证码的个数
    private static int xx = 40;
    private static int fontHeight = 50;
    private static int codeY = 50;
    static char[] codeSequence = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2',
            '3', '4', '5', '6', '7', '8', '9'};

    private static String prefix = "data:image/jpeg;base64,";

    @Resource
    private UserService userService;


    @ResponseBody
    @RequestMapping(value = "/pic64code", method = RequestMethod.GET)
    public JsonResult getPicBase64Code(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String picCode = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        picCode = generateCode(baos);
        String uuid = UUIDUtil.getUUID();
        Strings.setEx(RedisKey.PIC_CODE.getKey() + uuid, RedisKey.PIC_CODE.getSeconds(), picCode);
        String rstr = "data:image/jpg;base64," + Base64.encodeBase64String(baos.toByteArray());
        baos.close();
        Map<String, String> resMap = new HashMap<String, String>();
        resMap.put("picCode", rstr);
        resMap.put("key", uuid);
        return new JsonResult(resMap);
    }


    private static String generateCode(OutputStream outputStream) throws IOException {
        // 定义图像buffer
        BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics gd = buffImg.getGraphics();
        // 创建一个随机数生成器类
        Random random = new Random();
        // 将图像填充为白色
        gd.setColor(Color.WHITE);
        gd.fillRect(0, 0, width, height);
        // 创建字体，字体的大小应该根据图片的高度来定。
        Font font = new Font("Fixedsys", Font.BOLD, fontHeight);
        // 设置字体。
        gd.setFont(font);
        // 画边框。
        gd.setColor(Color.BLACK);
        gd.drawRect(0, 0, width - 1, height - 1);
        // 随机产生100条干扰线，使图象中的认证码不易被其它程序探测到。
        gd.setColor(Color.BLACK);
        for (int i = 0; i < 100; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            gd.drawLine(x, y, x + xl, y + yl);
        }
        // randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
        StringBuffer randomCode = new StringBuffer();
        int red = 0, green = 0, blue = 0;
        // 随机产生codeCount数字的验证码。
        for (int i = 0; i < codeCount; i++) {
            // 得到随机产生的验证码数字。
            String code = String.valueOf(codeSequence[random.nextInt(36)]);
            // 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同。
            red = random.nextInt(255);
            green = random.nextInt(255);
            blue = random.nextInt(255);
            // 用随机产生的颜色将验证码绘制到图像中。
            gd.setColor(new Color(red, green, blue));
            gd.drawString(code, (i + 1) * xx, codeY);
            // 将产生的四个随机数组合在一起。
            randomCode.append(code);
        }
        ImageIO.write(buffImg, "jpeg", outputStream);
        String str = randomCode.toString();
        return str;
    }

    /**
     * 获取手机验证码
     *
     * @param request
     * @param response
     * @param phoneNumber
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/sendSms", method = RequestMethod.GET)
    public JsonResult getPicBase64Code(HttpServletRequest request, HttpServletResponse response, String phoneNumber) throws Exception {

        if (StringUtils.isEmpty(phoneNumber)) {
            return new JsonResult(ResultCode.ERROR.getCode(), "手机号不能为空");
        }

        //判断手机号是否存在
        User model = new User();
        model.setPhone(phoneNumber);
        User user = userService.readOne(model);
        if (user == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "该手机号尚未绑定");
        }

        String phoneCode = RandomUtil.getCode();
        SmsUtil.sendSmsCode(phoneNumber, SmsTemplate.COMMON, phoneCode);
        //保存到redis
        Strings.setEx(RedisKey.SMS_CODE.getKey() + phoneNumber, RedisKey.SMS_CODE.getSeconds(), phoneCode);
        return new JsonResult();
    }
}
