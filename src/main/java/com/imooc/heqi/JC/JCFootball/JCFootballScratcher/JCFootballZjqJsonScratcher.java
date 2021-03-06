package com.imooc.heqi.JC.JCFootball.JCFootballScratcher;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.imooc.heqi.JC.JCFootball.JCFootballModel.JCFootballInfoModel;
import com.imooc.heqi.JC.JCFootball.JCFootballModel.JCFootballZjqInfoModel;
import com.imooc.heqi.util.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JCFootballZjqJsonScratcher {
    private static final Logger logger = LoggerFactory.getLogger(JCFootballZjqJsonScratcher.class);

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    //组装比赛数据
    public void getMatchList(Elements matchList){
        for (int i = 0; i < matchList.size(); i++) {
            Element tr = matchList.get(i);
            Elements tds = tr.select("td");
            if(1 == tds.size()) continue;
            for (int j = 0; j < tds.size(); j++) {
                Element td = tds.get(j);
                if(3 == j) logger.info("队名： " + tds.get(j).text());
//                if(j == 3) {
//                    logger.info("td[" + j + "]: " + td.text());
//                }



            }
//            logger.info("----------------------------------------");
        }
    }



    public Map<String, List<String>> scratch() {
        if (null == url || "".equals(url)) return null;
        if (logger.isInfoEnabled()) {
            logger.info("开始抓取竞彩网竞足开奖数据");
        }
        Map<String, List<String>> retVal = new HashMap<String, List<String>>();
        try {
            String json = new String();
            json = HttpUtils.doGet(url);
            json = StringUtils.substringBetween(json, "getData(", ");");
            json = json.replaceAll("int","intt");
            logger.info("json: " + json);

            if (!StringUtils.isEmpty(json)) {
                //开奖信息注入
                Gson gson = new Gson();
                logger.info("开始转换！");
                JCFootballInfoModel<JCFootballZjqInfoModel> jcFootballModel = gson.fromJson(json, new TypeToken<JCFootballInfoModel<JCFootballZjqInfoModel>>() {}.getType());
                for(Map.Entry<String, JCFootballZjqInfoModel> entry:jcFootballModel.getData().entrySet()){
                    JCFootballZjqInfoModel result = entry.getValue();
                    logger.info("p_code: " + result.getP_code());
                    logger.info("s0: " + result.getS0());
                }

                //TODO
            }


            if (logger.isInfoEnabled()) {
                logger.info("结束抓取竞彩网竞足开奖数据");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("竞彩网竞足开奖数据抓包失败");
        }
        return retVal;
    }




    public static void main(String[] args) {
        try {
            String url = "http://i.sporttery.cn/odds_calculator/get_odds?i_format=json&i_callback=getData&poolcode[]=ttg";
            JCFootballZjqJsonScratcher scratcher = new JCFootballZjqJsonScratcher();
            scratcher.setUrl(url);
            scratcher.scratch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
