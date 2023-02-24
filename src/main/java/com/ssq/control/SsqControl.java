package com.ssq.control;

import com.ssq.service.SsqService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@Slf4j
@RestController
@RequestMapping("/ssq")
public class SsqControl {

    @Autowired
    private SsqService ssqService;

    @RequestMapping(value = "findBonus", method = RequestMethod.POST)
    public String ssq(String ssq)  {
        try {
            //调用方法获取本期中奖彩票
            String[] aa = StringUtils.split(ssq, ',');
            return ssqService.judge(aa);
        } catch (UnsupportedEncodingException e) {
            return e.getMessage();
        }
    }
}
