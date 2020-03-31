package com.zhiyu.quanzhu.model.dao;

import com.qiniu.android.utils.StringUtils;
import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.model.bean.CardFrend;
import com.zhiyu.quanzhu.utils.FirstCharUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 名片好友持久化
 */
public class CardFrendDao {
    private static CardFrendDao dao;
    private static List<String> letterList = new ArrayList<>();

    public static CardFrendDao getDao() {
        if (null == dao) {
            synchronized (CardFrendDao.class) {
                dao = new CardFrendDao();
                initData();
            }
        }
        return dao;
    }

    private static void initData() {
        letterList.add("a");
        letterList.add("b");
        letterList.add("c");
        letterList.add("d");
        letterList.add("e");
        letterList.add("f");
        letterList.add("g");
        letterList.add("h");
        letterList.add("i");
        letterList.add("j");
        letterList.add("k");
        letterList.add("l");
        letterList.add("m");
        letterList.add("n");
        letterList.add("o");
        letterList.add("p");
        letterList.add("q");
        letterList.add("r");
        letterList.add("s");
        letterList.add("t");
        letterList.add("u");
        letterList.add("v");
        letterList.add("w");
        letterList.add("x");
        letterList.add("y");
        letterList.add("z");

    }

    public void saveCardFendList(List<CardFrend> list) {
        clearCardFrends();
        if (null != list && list.size() > 0) {
            for (CardFrend frend : list) {
                if (frend.getId() > 0) {
                    String name = StringUtils.isNullOrEmpty(frend.getNotename()) ? frend.getCard_name() : frend.getNotename();
                    String firstChar = FirstCharUtils.first(name);
                    System.out.println("firstChar: " + firstChar);
                    frend.setLetter(firstChar);
                    try {
                        BaseApplication.db.saveBindingId(frend);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private List<CardFrend> cardFrendList(String letter) {
        List<CardFrend> list = null;
        try {
            list = BaseApplication.db.selector(CardFrend.class).where("letter", "=", letter).orderBy("id", false).findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private List<CardFrend> searchCardFrendList(String search) {
        List<CardFrend> list = null;
        try {
            list = BaseApplication.db.selector(CardFrend.class).where("notename", "LIKE", search + "%").orderBy("id", false).findAll();
            System.out.println("list1:"+list.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private List<CardFrend> searchCardNameCardFrendList(String search) {
        List<CardFrend> list = null;
        try {
            list = BaseApplication.db.selector(CardFrend.class).where("card_name", "LIKE", search + "%").orderBy("id", false).findAll();
            System.out.println("list2:"+list.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<List<CardFrend>> searchFrend(String search) {
        List<List<CardFrend>> lists = new ArrayList<>();
        List<CardFrend> list = searchCardFrendList(search);
        if (null != list && list.size() > 0) {
            lists.add(list);
        }
        List<CardFrend> list2 = searchCardNameCardFrendList(search);
        if (null != list2 && list2.size() > 0) {
            lists.add(list2);
        }
        return lists;
    }

    public List<List<CardFrend>> cardFrendList() {
        List<List<CardFrend>> list = new ArrayList<>();
        for (String letter : letterList) {
            List<CardFrend> frendList = null;
            try {
                frendList = cardFrendList(letter.toUpperCase());
                if (null != frendList && frendList.size() > 0) {
                    list.add(frendList);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }


    private void clearCardFrends() {
        try {
            BaseApplication.db.delete(CardFrend.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
