package com.ssq.service;

import com.alibaba.fastjson.JSONObject;
import com.ssq.util.HttpURLConnectionUtil;
import com.ssq.vo.ResultBean;
import com.ssq.vo.SsqVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.*;

@Service
public class SsqService {

    /**
     * 获取最新双色球中奖号码
     *
     * @return
     */
    public String[] getLastSsq() throws UnsupportedEncodingException {
        String ssq = null;
        String url = "http://www.cwl.gov.cn/cwl_admin/front/cwlkj/search/kjxx/findDrawNotice?name=ssq&issueCount=1";
        Map<String, String> head = new HashMap<String, String>();
        head.put("Cookie", "HMF_CI=a76a1a17a9c2371ed880d725c7a2ab738aafecc050ff110f043996a09c1e51355937253876c71a185e38e5be7cfe2d066db3358a6b2190a00ece7f9373cff6fd57; 21_vq=3");
        String result = HttpURLConnectionUtil.doGet(url, "utf-8", head, null);
        if (StringUtils.isNotBlank(result)) {
            ResultBean resultBean = JSONObject.parseObject(result, ResultBean.class);
            SsqVo ssqVo = resultBean.getResult().get(0);
            ssq = ssqVo.getRed() + "," + ssqVo.getBlue();
        }
        return StringUtils.split(ssq, ',');
    }

    public String judge(String[] userNumbers) throws UnsupportedEncodingException {
        String[] luckyNumbers = getLastSsq();
//        定义两个变量分别存储红球和蓝球命中的个数
        int redNumbers = 0;
        int blueNumbers = 0;
        List<Integer> resut = new ArrayList();
        //判断红球中了几个
        for (int i = 0; i < userNumbers.length - 1; i++) {
            for (int j = 0; j < luckyNumbers.length - 1; j++) {
                if (Integer.valueOf(userNumbers[i].trim()) == Integer.valueOf(luckyNumbers[j].trim())) {
                    resut.add(Integer.valueOf(userNumbers[i].trim()));
                    redNumbers++;
                    break;
                }
            }
        }
        //判断蓝球是否命中
        blueNumbers = Integer.valueOf(userNumbers[6].trim()) == Integer.valueOf(luckyNumbers[6]) ? 1 : 0;

        //判断中奖情况

        System.out.println("中奖号码是：");
        printArray(luckyNumbers);
        System.out.println("您投注的号码是：");
        printArray(userNumbers);
        System.out.println("您中了" + redNumbers + "个红球,他们分别是:" + resut.toString());
        System.out.println("您是否命中蓝球:" + (blueNumbers == 1 ? "是" : "否"));


        if (blueNumbers == 1 && redNumbers < 3) {
            System.err.println("恭喜你中了六等奖");
            return "恭喜你中了六等奖";
        } else if (blueNumbers == 1 && redNumbers == 3 || blueNumbers == 0 && redNumbers == 4) {
            return "恭喜你中了五等奖";
        } else if (blueNumbers == 1 && redNumbers == 4 || blueNumbers == 0 && redNumbers == 5) {
            return "恭喜你中了四等奖";
        } else if (blueNumbers == 1 && redNumbers == 5) {
            return "恭喜你中了三等奖";
        } else if (blueNumbers == 0 && redNumbers == 6) {
            return "恭喜你中了二等奖";
        } else if (blueNumbers == 1 && redNumbers == 6) {
            return "恭喜你中了一等奖";
        } else {
            return "您未中奖，感谢你为福利事业做出贡献!!!";
        }
    }


    public void printArray(int[] arr) {
        System.out.print("[");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(i == arr.length - 1 ? arr[i] : arr[i] + ", ");
        }
        System.out.println("]");
    }

    public void printArray(String[] arr) {
        System.out.print("[");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(i == arr.length - 1 ? arr[i] : arr[i] + ", ");
        }
        System.out.println("]");
    }

    public int[] userInputNumbers() {
        //定义一个数组存储7个数字
        int[] numbers = new int[7];
        //让用户存入前6个数字
        Scanner sc = new Scanner(System.in);
        for (int i = 0; i < numbers.length - 1; i++) {
            System.out.println("请输入第" + (i + 1) + "个数(1-33之间，要求不重复)：");
            int data = sc.nextInt();
            //将当前的数据存入到数组中去
            numbers[i] = data;
        }
//        单独输入第七个数
        System.out.println("最后一个数请输入（1-16）之间的数：");
        numbers[6] = sc.nextInt();
        return numbers;
    }

    public int[] createLuckNumber() {
        int[] numbers = new int[7];

        Random r = new Random();
        for (int i = 0; i < numbers.length; i++) {
            //用一个死循环，找出一个1-33没有重复的数字
            while (true) {
                int data = r.nextInt(33) + 1;
                //判断data是否已经出现过
                //定义一个flag变量，默认data是没有重复的
                boolean flag = true;
                for (int j = 0; j < i; j++) {
                    if (numbers[j] == data) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    numbers[i] = data;
                    break;
                }
            }
        }

        //为第七位生成一个1-16的随机数
        numbers[numbers.length - 1] = r.nextInt(16) + 1;
        return numbers;
    }


}
