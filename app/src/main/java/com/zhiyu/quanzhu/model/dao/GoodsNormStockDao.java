package com.zhiyu.quanzhu.model.dao;

import android.database.Cursor;

import com.zhiyu.quanzhu.base.BaseApplication;
import com.zhiyu.quanzhu.model.bean.GoodsNorm;
import com.zhiyu.quanzhu.model.bean.GoodsNormGroup;
import com.zhiyu.quanzhu.model.bean.GoodsStock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 商品规格库存dao
 */
public class GoodsNormStockDao {
    private static GoodsNormStockDao dao;

    public static GoodsNormStockDao getInstance() {
        if (null == dao) {
            synchronized (GoodsNormStockDao.class) {
                dao = new GoodsNormStockDao();
            }
        }
        return dao;
    }

    public void saveStockList(List<GoodsStock> list) {
        clearStocks();
        if (null != list && list.size() > 0) {
            for (GoodsStock stock : list) {
                try {
                    BaseApplication.db.saveBindingId(stock);
                    System.out.println(stock);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        selectStockList();
    }

    private void selectStockList() {
        try {
            List<GoodsStock> list = BaseApplication.db.findAll(GoodsStock.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Integer> selectStocks(List<GoodsNorm> list) {
        Set<Integer> idSet = new HashSet<>();
        if (null != list && list.size() > 0) {
            String sql = "select * from goods_stock where ";
            for (int i = 0; i < list.size(); i++) {
                String id = "id" + list.get(i).getP_id();
                sql += id + " = " + list.get(i).getNorms_id();
                if (i < (list.size() - 1)) {
                    sql += " and ";
                }
            }
            try {
                Cursor cursor = BaseApplication.db.execQuery(sql);
                while (cursor.moveToNext()) {
//                    GoodsStock stock = new GoodsStock();
                    for (int i = 1; i < 10; i++) {
                        long _id = cursor.getLong(cursor.getColumnIndex("id" + i));
                        if (_id > 0)
                            idSet.add((int) _id);
                    }
//                    int _stock = cursor.getInt(cursor.getColumnIndex("stock"));
//                    String _img = cursor.getString(cursor.getColumnIndex("img"));
//                    stock.setStock(_stock);
//                    stock.setImg(_img);
//                    System.out.println("查询到的库存为 " + _stock);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        List<Integer> idList = new ArrayList<>(idSet);
        return idList;
    }

    private GoodsStock getGoodsStock(List<GoodsNorm> list) {
        List<GoodsStock> stockList = new ArrayList<>();
        if (null != list && list.size() > 0) {
            String sql = "select * from goods_stock where ";
            for (int i = 0; i < list.size(); i++) {
                String id = "id" + list.get(i).getP_id();
                sql += id + " = " + list.get(i).getNorms_id();
                if (i < (list.size() - 1)) {
                    sql += " and ";
                }
            }
            try {
                Cursor cursor = BaseApplication.db.execQuery(sql);
                while (cursor.moveToNext()) {
                    GoodsStock stock = new GoodsStock();
                    int _stock = cursor.getInt(cursor.getColumnIndex("stock"));
                    String _img = cursor.getString(cursor.getColumnIndex("img"));
                    stock.setStock(_stock);
                    stock.setImg(_img);
                    stockList.add(stock);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (stockList.size() == 1) {
            return stockList.get(0);
        } else {
            return null;
        }
    }

    private void clearStocks() {
        try {
            BaseApplication.db.delete(GoodsStock.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Integer> getInitStock() {
        Set<Integer> set = new HashSet<>();
        List<GoodsStock> list = null;
        try {
            list = BaseApplication.db.findAll(GoodsStock.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null != list && list.size() > 0) {
            for (GoodsStock goodsStock : list) {
                set.add((int) goodsStock.getId1());
                set.add((int) goodsStock.getId2());
                set.add((int) goodsStock.getId3());
                set.add((int) goodsStock.getId4());
                set.add((int) goodsStock.getId5());
                set.add((int) goodsStock.getId6());
                set.add((int) goodsStock.getId7());
                set.add((int) goodsStock.getId8());
                set.add((int) goodsStock.getId9());
                set.add((int) goodsStock.getId10());
            }
        }
        List<Integer> idlist = new ArrayList<>(set);
        return idlist;
    }

    /**
     * 初始化规格-库存数据
     *
     * @param list
     * @return
     */
    public List<GoodsNormGroup> initGoodsNormsStock(List<GoodsNormGroup> list) {
        if (null == goodsNormList) {
            goodsNormList = new ArrayList<>();
        } else {
            goodsNormList.clear();
        }
        if (null == map) {
            map = new HashMap<>();
        } else {
            map.clear();
        }
        List<Integer> idlist = getInitStock();
        if (null != list && list.size() > 0 && null != idlist && idlist.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                for (int j = 0; j < list.get(i).getList().size(); j++) {
                    list.get(i).getList().get(j).setSelected(false);
                    for (int k = 0; k < idlist.size(); k++) {
                        if (list.get(i).getList().get(j).getNorms_id() == idlist.get(k)) {
                            list.get(i).getList().get(j).setSelectable(true);
                        }
                    }
                }
            }
        }
        return list;
    }

    public List<GoodsNormGroup> initGoodsNormsStock2(List<GoodsNormGroup> list) {
        List<Integer> idlist = getInitStock();
        if (null != list && list.size() > 0 && null != idlist && idlist.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                for (int j = 0; j < list.get(i).getList().size(); j++) {
                    list.get(i).getList().get(j).setSelected(false);
                    for (int k = 0; k < idlist.size(); k++) {
                        if (list.get(i).getList().get(j).getNorms_id() == idlist.get(k)) {
                            list.get(i).getList().get(j).setSelectable(true);
                        }
                    }
                }
            }
        }
        return list;
    }

    private List<GoodsNorm> goodsNormList;
    private Map<Integer, GoodsNorm> map;

    /**
     * 实时计算规格-库存数据
     *
     * @param list
     * @param parent_position
     * @param child_position
     * @return
     */
    public List<GoodsNormGroup> calculateGoodsNormsStock(List<GoodsNormGroup> list, int parent_position, int child_position) {
//        System.out.println("parent_position: "+parent_position+" ,child_position : "+child_position);
        //将选中的规格放入数组中(每一大规格只会放入一个小规格)
        GoodsNorm goodsNorm = list.get(parent_position).getList().get(child_position);
        if (map.containsKey(parent_position)) {
//            System.out.println("has : " + parent_position);
            GoodsNorm gn = map.get(parent_position);
            if (gn.getNorms_id() == goodsNorm.getNorms_id()) {
//                System.out.println("已有，进行删除 : " + parent_position);
                map.remove(parent_position);
            } else {
//                System.out.println("has 没有，进行添加 : " + parent_position);
                map.put(parent_position, goodsNorm);
            }
        } else {
//            System.out.println("unhas : " + parent_position);
            map.put(parent_position, goodsNorm);
        }
//        System.out.println("map size: "+map.size());
        Collection<GoodsNorm> collection = map.values();
        goodsNormList = new ArrayList<>(collection);
        if (goodsNormList.size() == 0) {//未选中任一规格，回到初始化状态
//            System.out.println("未选中任一规格，回到初始化状态");
            list = initGoodsNormsStock(list);
        } else {//有规格被选中
            //点击的每一大规格内只有一个小规格被选中
            for (int i = 0; i < list.size(); i++) {
                if (i == parent_position) {
                    for (int j = 0; j < list.get(parent_position).getList().size(); j++) {
                        if (j != child_position) {
                            if (list.get(i).getList().get(j).isSelectable()) {
                                list.get(i).getList().get(j).setSelected(false);
                            }
                        }
                    }
                }
            }
            //将选中的小规格设置选中状态/未选中
            if (!list.get(parent_position).getList().get(child_position).isSelected()) {
                list.get(parent_position).getList().get(child_position).setSelected(true);
            } else {
                list.get(parent_position).getList().get(child_position).setSelected(false);
            }
            //计算选中规格匹配的库存数据
            List<Integer> stockIntegerList = selectStocks(goodsNormList);

            System.out.println(goodsNormList.size() + " , " + stockIntegerList.toString());
            if (goodsNormList.size() > 1) {
                for (GoodsNormGroup group : list) {
                    for (GoodsNorm norm : group.getList()) {
                        norm.setSelectable(false);
                    }
                }

                for (int k = 0; k < stockIntegerList.size(); k++) {
                    for (int i = 0; i < list.size(); i++) {
                        for (int j = 0; j < list.get(i).getList().size(); j++) {
                            if (list.get(i).getList().get(j).getNorms_id() == stockIntegerList.get(k)) {
                                list.get(i).getList().get(j).setSelectable(true);
                            }
                        }
                    }
                }
            } else if (goodsNormList.size() == 1) {
                long p_id = goodsNormList.get(0).getP_id();
                long norms_id = goodsNormList.get(0).getNorms_id();
                List<GoodsNormGroup> pList = initGoodsNormsStock2(list);
                for (int i = 0; i < pList.size(); i++) {
                    for (int j = 0; j < pList.get(i).getList().size(); j++) {
                        for (GoodsNormGroup group : list) {
                            for (GoodsNorm norm : group.getList()) {
                                norm.setSelectable(false);
                            }
                        }
                        for (int k = 0; k < stockIntegerList.size(); k++) {
                            for (int ii = 0; ii < list.size(); ii++) {
                                for (int jj = 0; jj < list.get(ii).getList().size(); jj++) {
                                    if (list.get(ii).getList().get(jj).getNorms_id() == stockIntegerList.get(k)) {
                                        list.get(ii).getList().get(jj).setSelectable(true);
                                    }
                                }
                            }
                        }
                    }
                }
                List<Integer> idlist = getInitStock();
                for (int i = 0; i < pList.size(); i++) {
                    for (int j = 0; j < pList.get(i).getList().size(); j++) {
                        if (p_id == pList.get(i).getList().get(j).getP_id()) {
                            for (Integer id : idlist) {
                                if (id == pList.get(i).getList().get(j).getNorms_id()) {
                                    list.get(i).getList().get(j).setSelectable(true);
                                }
                            }
//                            if (pList.get(i).getList().get(j).isSelectable()) {
//                                list.get(i).getList().get(j).setSelectable(true);
//                            }
                            if (norms_id == list.get(i).getList().get(j).getNorms_id()) {
                                list.get(i).getList().get(j).setSelected(true);
                            }
                        }
                    }
                }
            }

//            for (GoodsNormGroup goodsNormGroup : list) {
//                for (GoodsNorm gn : goodsNormGroup.getList()) {
//                    if (gn.isSelectable()) {
//                        System.out.println("norm_id: " + gn.getNorms_id());
//                    }
//                }
//            }
        }
        return list;
    }

}