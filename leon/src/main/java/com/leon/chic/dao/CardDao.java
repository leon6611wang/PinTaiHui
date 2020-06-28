package com.leon.chic.dao;

import android.app.Application;
import android.database.Cursor;
import android.util.Log;

import com.leon.chic.bean.CardResult;
import com.leon.chic.model.Card;
import com.leon.chic.model.ServiceMessage;
import com.leon.chic.model.ServiceMessageMessage;
import com.leon.chic.utils.DaoUtils;
import com.leon.chic.utils.FirstCharUtils;
import com.leon.chic.utils.GsonUtils;
import com.leon.chic.utils.LetterUtils;
import com.leon.chic.utils.LogUtils;
import com.leon.chic.utils.MessageTypeUtils;
import com.leon.chic.utils.ThreadPoolUtils;

import org.xutils.db.sqlite.WhereBuilder;

import java.util.ArrayList;
import java.util.List;

public class CardDao {
    private final String TAG = "CardDao";
    private static CardDao dao;
    private static List<String> letterList = new ArrayList<>();
    private static List<String> letterUpperList = new ArrayList<>();

    public static CardDao getInstance() {
        if (null == dao) {
            synchronized (CardDao.class) {
                dao = new CardDao();
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

        letterUpperList.add("A");
        letterUpperList.add("B");
        letterUpperList.add("C");
        letterUpperList.add("D");
        letterUpperList.add("E");
        letterUpperList.add("F");
        letterUpperList.add("G");
        letterUpperList.add("H");
        letterUpperList.add("I");
        letterUpperList.add("J");
        letterUpperList.add("K");
        letterUpperList.add("I");
        letterUpperList.add("M");
        letterUpperList.add("N");
        letterUpperList.add("O");
        letterUpperList.add("P");
        letterUpperList.add("Q");
        letterUpperList.add("R");
        letterUpperList.add("S");
        letterUpperList.add("T");
        letterUpperList.add("U");
        letterUpperList.add("V");
        letterUpperList.add("W");
        letterUpperList.add("X");
        letterUpperList.add("Y");
        letterUpperList.add("Z");
    }

    public List<String> getLetterList() {
        return letterList;
    }

    public void saveList(final String s, final Application app) {
        ThreadPoolUtils.getInstance().init().execute(new Runnable() {
            @Override
            public void run() {
                CardResult result = GsonUtils.GsonToBean(s, CardResult.class);
                if (null != result && null != result.getData() && null != result.getData().getMy_friends_card()) {
                    List<Card> list = result.getData().getMy_friends_card();
//                    System.out.println("名片好友 saveList " + list.size());
                    if (null != list && list.size() > 0) {
                        for (Card card : list) {
                            String first = null;
                            String cName = "未知";
                            if (null != card.getNotename() && !"".equals(card.getNotename())) {
                                cName = card.getNotename();
                            } else if (null != card.getCard_name() && !"".equals(card.getCard_name())) {
                                cName = card.getCard_name();
                            } else {
                                card.setNotename(cName);
                            }

                            boolean is_zimu = false;
                            String zimu = "";
                            for (String l : letterList) {
                                if (cName.startsWith(l)) {
                                    is_zimu = true;
                                    zimu = l;
                                    break;
                                }
                            }
                            for (String l : letterUpperList) {
                                if (cName.startsWith(l)) {
                                    is_zimu = true;
                                    zimu = l;
                                    break;
                                }
                            }
                            if (is_zimu) {
                                first = zimu.toLowerCase();
                            } else {
                                first = LetterUtils.getFirstLetter(cName);
                            }
//                            System.out.println("------------------------------------> first: " + first + " , card_name: " + card.getCard_name() + " , note_name: " + card.getNotename());
                            if (null != first && !"".equals(first)) {
                                card.setLetter(first);
                                Card c = select(card.getId(), app);
                                if (null != c) {
                                    deleteCard(card.getId(), app);
                                }
                                try {
                                    DaoUtils.getInstance(app).getDb().saveBindingId(card);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        });

    }

    public void updateCardNoteName(int id, String noteName, Application app) {
//        System.out.println("更新名片好友备注名");
        try {
            Card c = select(id, app);
//            System.out.println("是否已查到改好友: " + (null != c));
            if (null != c) {
                c.setNotename(noteName);
            }
            save(GsonUtils.GsonString(c), app);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save(String s, Application app) {
//        System.out.println("名片解析数据源: " + s);
        Card card = GsonUtils.GsonToBean(s, Card.class);
//        System.out.println("解析出的名片: " + card.getCard_id());
        String first = null;
        if (null != card.getNotename() && !"".equals(card.getNotename())) {
            first = LetterUtils.getFirstLetter(card.getNotename());
//            System.out.println("noteName不为空，首字母: " + first);
        } else if (null != card.getCard_name() && !"".equals(card.getCard_name())) {
//            System.out.println("noteName为空，cardName不为空");
            boolean is_zimu = false;
            String zimu = "";
            for (String l : letterList) {
                if (card.getCard_name().startsWith(l)) {
                    is_zimu = true;
                    zimu = l;
                    break;
                }
            }
            for (String l : letterUpperList) {
                if (card.getCard_name().startsWith(l)) {
                    is_zimu = true;
                    zimu = l;
                    break;
                }
            }
            if (is_zimu) {
                first = zimu.toLowerCase();
            } else {
                first = LetterUtils.getFirstLetter(card.getCard_name());
            }
        }
//        System.out.println("save名片的首字母: " + first);
        if (null != first && !"".equals(first)) {
            card.setLetter(first);
            Card c = select(card.getId(), app);
            if (null != c) {
                deleteCard(card.getId(), app);
            }
            try {
                DaoUtils.getInstance(app).getDb().saveBindingId(card);
//                System.out.println("名片数据保存完成");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void editCard(String s, Application app) {
        Card card = null;
        if (null != s && !"".equals(s)) {
            card = GsonUtils.GsonToBean(s, Card.class);
        }
        Card c = select(card.getId(), app);
        if (null != c) {
            deleteCard(card.getId(), app);
        }
        try {
            save(s, app);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Card select(int id, Application app) {
        Card card = null;
        try {
            card = DaoUtils.getInstance(app).getDb().selector(Card.class).where("id", "=", String.valueOf(id)).findFirst();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return card;
    }

    public String select(String name, Application app) {
        String json = null;
        Card card;
        try {
            card = DaoUtils.getInstance(app).getDb().selector(Card.class).where("note_name", "=", name).findFirst();
            if (null == card) {
                card = DaoUtils.getInstance(app).getDb().selector(Card.class).where("card_name", "=", name).findFirst();
            }
            if (null != card) {
                json = GsonUtils.GsonString(card);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public void deleteCard(int id, Application app) {
        try {
            DaoUtils.getInstance(app).getDb().delete(Card.class, WhereBuilder.b("id", "=", String.valueOf(id)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clear(Application app) {
        try {
            DaoUtils.getInstance(app).getDb().dropTable(Card.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String cardList(Application app, MessageDao.OnCardMessageChangeListener listener) {
        MessageDao.getInstance().setOnCardMessageChangeListener(listener);
        String jsonString = null;
        try {
            List<Card> list = DaoUtils.getInstance(app).getDb().selector(Card.class).orderBy("letter", false).findAll();
            if (null != list && list.size() > 0) {
                jsonString = GsonUtils.GsonString(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonString;
    }

    public String searchCardList(Application app, String search) {
        String jsonString = null;
        try {
            List<Card> list = DaoUtils.getInstance(app).getDb().selector(Card.class).where("card_name", "like", search)
                    .or("note_name", "like", search)
                    .orderBy("letter", false).findAll();
            if (null != list && list.size() > 0) {
                jsonString = GsonUtils.GsonString(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    public String searchCardListSQL(Application app, String search) {
        String jsonString = null;
        String sql = "select * from card where card_name like '%" + search + "%' or note_name like '%" + search + "%'";
        try {
            Cursor cursor = DaoUtils.getInstance(app).getDb().execQuery(sql);
            List<Card> list = new ArrayList<>();
            while (cursor.moveToNext()) {
                Card card = new Card();
                card.setId(cursor.getInt(cursor.getColumnIndex("id")));
                card.setUid(cursor.getInt(cursor.getColumnIndex("uid")));
                card.setNotename(cursor.getString(cursor.getColumnIndex("note_name")));
                card.setCard_name(cursor.getString(cursor.getColumnIndex("card_name")));
                card.setCard_thumb(cursor.getString(cursor.getColumnIndex("card_thumb")));
                card.setOccupation(cursor.getString(cursor.getColumnIndex("occupation")));
                card.setCompany(cursor.getString(cursor.getColumnIndex("company")));
                card.setLetter(cursor.getString(cursor.getColumnIndex("letter")));
                list.add(card);
            }
            if (cursor != null) {
                cursor.close();
            }
            if (null != list && list.size() > 0) {
                jsonString = GsonUtils.GsonString(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    public String cardChangeList(Application app) {
        String jsonString = null;
        try {
            List<Card> list = DaoUtils.getInstance(app).getDb().selector(Card.class).orderBy("letter", false).findAll();
            if (null != list && list.size() > 0) {
                jsonString = GsonUtils.GsonString(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonString;
    }


//    private OnCardDataChangeListener onCardDataChangeListener;
//    public void setOnCardDataChangeListener(OnCardDataChangeListener listener){
//        this.onCardDataChangeListener=listener;
//    }
//    public interface OnCardDataChangeListener{
//        void onCardDataChange();
//    }

    public String selectCardListByLetter(Application app) {
        List<List<Card>> list = new ArrayList<>();
        for (String letter : letterList) {
            List<Card> frendList = null;
            try {
                frendList = cardFrendList(app, letter.toLowerCase());
                if (null != frendList && frendList.size() > 0) {
                    list.add(frendList);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return GsonUtils.GsonString(list);
    }


    private List<Card> cardFrendList(Application app, String letter) {
        List<Card> list = null;
        try {
            list = DaoUtils.getInstance(app).getDb().selector(Card.class).where("letter", "=", letter.toLowerCase()).orderBy("id", false).findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private List<Card> cardFrendListByName(Application app, String name) {
        List<Card> list = null;
        try {
            list = DaoUtils.getInstance(app).getDb().selector(Card.class).where("card_name", "=", name)
                    .or("note_name", "=", name).orderBy("id", false).findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public String searchCardListByName(Application app, String name) {
        List<List<Card>> list = new ArrayList<>();
        List<Card> frendList = null;
        try {
            frendList = cardFrendListByName(app, name);
            if (null != frendList && frendList.size() > 0) {
                list.add(frendList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return GsonUtils.GsonString(list);
    }


}
