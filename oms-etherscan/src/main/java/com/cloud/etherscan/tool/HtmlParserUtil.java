package com.cloud.etherscan.tool;

import com.cloud.etherscan.model.EthHolderDetail;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Description:
 *
 * @author sheng.wang
 * @version 1.0.0
 * @email sheng.wang@chinaredstar.com
 * @date: 2018-05-22 13:24
 */
public class HtmlParserUtil {

    public static List<EthHolderDetail> parseTokenDetail(String html){
        List<EthHolderDetail> list = new ArrayList<>(52);
        try {
            Document document = Jsoup.parse(html);
            Iterator<Element> table = document.getElementsByTag("table").iterator();
            while (table.hasNext()){
                Element next = table.next();
                Iterator<Element> trIter = next.getElementsByTag("tr").iterator();

                int i = 0;
                while (trIter.hasNext()){
                    Element tr = trIter.next();
                    if (i == 0){
                        i++;
                        continue;
                    }
                    Iterator<Element> iterator = tr.getElementsByTag("td").iterator();
                    iterator.next();
                    Element addrEle = iterator.next();
                    addrEle = addrEle.getElementsByTag("a").iterator().next();

                    Element quantityEle = iterator.next();
                    Element percentageEle = iterator.next();

                    EthHolderDetail detail = new EthHolderDetail();
                    detail.setAddress(addrEle.text());
                    detail.setQuantity(quantityEle.text());
                    detail.setPercentage(percentageEle.text());
                    list.add(detail);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }


    public static String[] parseTokenCount(String html){
        try {
            Document document = Jsoup.parse(html);
            Elements select = document.select("td[class=tditem]");
            Iterator<Element> iterator = select.iterator();
            Element next = iterator.next();
            String tokenCount = next.text().split(" ")[0].replaceAll(",","");

            Iterator<Element> script = document.getElementsByTag("script").iterator();
            String s = null;
            while (script.hasNext()){
                if (s != null){
                    break;
                }
                Element next1 = script.next();
                String string = next1.toString();
                if (string.contains("function")){
                    String[] split = string.split("\r\n");
                    for (String str : split){
                        if (str.contains("tokeholdersiframe")){
                            s = str.substring(str.lastIndexOf("=")+1,str.length()-2);
                            break;
                        }
                    }
                }
            }
            return new String[]{tokenCount,s};
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
