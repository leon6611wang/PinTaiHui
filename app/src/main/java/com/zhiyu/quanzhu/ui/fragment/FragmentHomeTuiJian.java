package com.zhiyu.quanzhu.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.model.bean.DongTai;
import com.zhiyu.quanzhu.model.bean.ShangQuan;
import com.zhiyu.quanzhu.ui.activity.CityListActivity;
import com.zhiyu.quanzhu.ui.adapter.HomeTuiJianDongTaiRecyclerViewAdapter;
import com.zhiyu.quanzhu.ui.adapter.ShangQuanTuiJianRecyclerViewAdapter;
import com.zhiyu.quanzhu.ui.popupwindow.HomeTuiJianMenuWindow;

import java.util.ArrayList;
import java.util.List;

public class FragmentHomeTuiJian extends Fragment implements OnBannerListener {
    private Banner banner;
    private ArrayList<String> list_path;
    private ArrayList<String> list_title;
    private View view;
    private View headerView, backgroudView;
    private RecyclerView shangquanTuiJianRecyclerView;
    private ShangQuanTuiJianRecyclerViewAdapter shangQuanTuiJianRecyclerViewAdapter;
    private List<ShangQuan> shangQuanList=new ArrayList<>();
    private RecyclerView dongtaiRecyclerView;
    private HomeTuiJianDongTaiRecyclerViewAdapter homeTuiJianDongTaiRecyclerViewAdapter;
    private ArrayList<DongTai> dongTaiList=new ArrayList<>();
    private FrameLayout bannerRootLayout;
    private ImageView showMenuImage;
    private LinearLayout showMenuLayout;
    private LinearLayout cityLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_tuijian, null);
        headerView = view.findViewById(R.id.headerView);
        showMenuLayout=headerView.findViewById(R.id.showMenuLayout);
        showMenuImage=headerView.findViewById(R.id.showMenuImage);
        showMenuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HomeTuiJianMenuWindow(getActivity()).showAtBottom(showMenuImage);
            }
        });
        cityLayout=headerView.findViewById(R.id.cityLayout);
        cityLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), CityListActivity.class);
                startActivity(intent);
            }
        });
        initDongTaiViews();
        return view;
    }
    private int bannerRootLayoutHeight=0;
    private boolean changeColor=true;
    private void initBanner() {
        backgroudView = dongTaiHeaderView.findViewById(R.id.backgroudView);
        banner = dongTaiHeaderView.findViewById(R.id.banner);
        bannerRootLayout=dongTaiHeaderView.findViewById(R.id.bannerRootLayout);
        bannerRootLayout.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        bannerRootLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                        bannerRootLayout.getWidth(); // 获取宽度
                        bannerRootLayoutHeight= bannerRootLayout.getHeight(); // 获取高度
                        Log.i("bannerRootLayoutHeight","bannerRootLayoutHeight: "+bannerRootLayoutHeight);
                        return true;
                    }
                });

        //放图片地址的集合
        list_path = new ArrayList<>();
        //放标题的集合
        list_title = new ArrayList<>();
        list_path.add("https://img.zcool.cn/community/01afcb5b61314ba801215c8fc23109.jpg@1280w_1l_2o_100sh.jpg");
        list_path.add("https://img.zcool.cn/community/01f64358392991a8012060c8a15648.jpg@1280w_1l_2o_100sh.jpg");
        list_path.add("https://img.zcool.cn/community/01cedd583929b4a801219c77676fbc.jpg@1280w_1l_2o_100sh.jpg");
        list_path.add("https://img.zcool.cn/community/0153ca57e2abd40000012e7e86e826.jpg@1280w_1l_2o_100sh.jpg");
        list_path.add("https://img.zcool.cn/community/01771e57e2abd60000012e7e313939.jpg@1280w_1l_2o_100sh.jpg");
        list_title.add("好好学习");
        list_title.add("天天向上");
        list_title.add("热爱劳动");
        list_title.add("不搞对象");
        list_title.add("banner测试");
        //设置内置样式，共有六种可以点入方法内逐一体验使用。
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器，图片加载器在下方
        banner.setImageLoader(new MyLoader());
        //设置图片网址或地址的集合
        banner.setImages(list_path);
        //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
        banner.setBannerAnimation(Transformer.Default);
        //设置轮播图的标题集合
        banner.setBannerTitles(list_title);
        //设置轮播间隔时间
        banner.setDelayTime(4000);
        //设置是否为自动轮播，默认是“是”。
        banner.isAutoPlay(true);
        //设置指示器的位置，小点点，左中右。
        banner.setIndicatorGravity(BannerConfig.CENTER)
                //以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。
                .setOnBannerListener(this)
                //必须最后调用的方法，启动轮播图。
                .start();
        banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position < list_path.size() + 1) {
                    currentBannerIndex=position-1;
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state){
                    case 2:
                        if(currentBannerIndex==list_path.size()-1){
                            currentBannerIndex=-1;
                        }
                        Glide.with(getContext()).asBitmap().load(list_path.get(currentBannerIndex+1)).into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                if(changeColor){
                                    parseRGB(resource);
                                }
                            }
                        });
                        break;
                }

            }
        });
    }

    //轮播图的监听方法
    @Override
    public void OnBannerClick(int position) {
        Log.i("tag", "你点了第" + position + "张轮播图");
    }

    private boolean isFirstLoadImage = true;
    private int currentBannerIndex=0;

    //自定义的图片加载器
    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load((String) path).into(imageView);
            if (isFirstLoadImage) {
                Glide.with(getContext()).asBitmap().load(list_path.get(0)).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        parseRGB(resource);
                    }
                });
                isFirstLoadImage = false;
            }
        }
    }

    private void parseRGB(Bitmap bitmap) {
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch swatch = palette.getVibrantSwatch();
                if (swatch != null) {
                    int rgb = swatch.getRgb();
                    headerView.setBackgroundColor(rgb);
                    backgroudView.setBackgroundColor(rgb);
                }
            }
        });
    }



    private void initReDianDatas(){
        DongTai d1=new DongTai();
        d1.setImage("http://hbimg.b0.upaiyun.com/b743592a1ae6949e20e530ef875947d0092e9a3ca59d-ANes3d_fw658");
        List<String> ilist1=new ArrayList<>();
        ilist1.add("https://www.agri35.com/UploadFiles/img_1_657306500_2705644997_26.jpg");
        ilist1.add("https://www.agri35.com/UploadFiles/img_1_375685424_4292515771_26.jpg");
        ilist1.add("https://www.agri35.com/UploadFiles/img_2_771354053_877389237_26.jpg");
        ilist1.add("https://www.agri35.com/UploadFiles/img_0_283566222_3155104450_26.jpg");
        ilist1.add("https://www.agri35.com/UploadFiles/img_1_3964834309_599461538_26.jpg");
        ilist1.add("https://www.agri35.com/UploadFiles/img_3_2668667811_4079707720_15.jpg");
        d1.setImageList(ilist1);
        dongTaiList.add(d1);

        DongTai d2=new DongTai();
        d2.setImage("https://c-ssl.duitang.com/uploads/item/201907/26/20190726183439_cPykA.thumb.700_0.jpeg");
        List<String> ilist2=new ArrayList<>();
        ilist2.add("https://c-ssl.duitang.com/uploads/item/201909/17/20190917110520_8FNyj.thumb.700_0.jpeg");
        ilist2.add("https://c-ssl.duitang.com/uploads/item/201909/23/20190923184304_3krMj.thumb.700_0.jpeg");
        ilist2.add("https://c-ssl.duitang.com/uploads/item/201907/18/20190718170010_sGEhK.thumb.700_0.jpeg");
        ilist2.add("https://c-ssl.duitang.com/uploads/item/201909/23/20190923184307_yw5MX.thumb.700_0.jpeg");
        ilist2.add("https://c-ssl.duitang.com/uploads/item/201812/13/20181213084735_yfplr.thumb.700_0.jpg");
        ilist2.add("https://c-ssl.duitang.com/uploads/item/201910/26/20191026215816_EGRic.thumb.700_0.jpeg");
        ilist2.add("https://c-ssl.duitang.com/uploads/item/201805/21/20180521143944_ezdGt.thumb.700_0.jpeg");
        ilist2.add("https://c-ssl.duitang.com/uploads/item/201909/30/20190930181403_3ZeLe.thumb.700_0.jpeg");
        ilist2.add("https://c-ssl.duitang.com/uploads/item/201910/29/20191029053629_tNAfy.thumb.700_0.jpeg");
        d2.setImageList(ilist2);
        dongTaiList.add(d2);
        dongTaiList.add(d2);
        dongTaiList.add(d2);
        dongTaiList.add(d2);
        dongTaiList.add(d2);
        dongTaiList.add(d2);
    }

    private View dongTaiHeaderView;
    private void initDongTaiHeaderView(){
        dongTaiHeaderView=LayoutInflater.from(getContext()).inflate(R.layout.header_home_tuijian_recyclerview,null);
        homeTuiJianDongTaiRecyclerViewAdapter.setHeaderView(dongTaiHeaderView);
        initBanner();
        initShangQuanTuiJian();
    }



    private void initShangQuanTuiJianDatas(){
        ShangQuan s1=new ShangQuan();
        s1.setImage("https://c-ssl.duitang.com/uploads/item/201710/08/20171008151547_4h8mX.thumb.700_0.jpeg");
        s1.setName("商圈1");
        shangQuanList.add(s1);

        ShangQuan s2=new ShangQuan();
        s2.setImage("https://c-ssl.duitang.com/uploads/item/201707/19/20170719211350_4PnBt.thumb.700_0.jpeg");
        s2.setName("商圈2");
        shangQuanList.add(s2);

        ShangQuan s3=new ShangQuan();
        s3.setImage("https://c-ssl.duitang.com/uploads/item/201612/04/20161204000928_jfHPk.thumb.700_0.jpeg");
        s3.setName("商圈3");
        shangQuanList.add(s3);

        ShangQuan s4=new ShangQuan();
        s4.setImage("https://c-ssl.duitang.com/uploads/item/201607/09/20160709202914_LYx4H.thumb.700_0.jpeg");
        s4.setName("商圈4");
        shangQuanList.add(s4);

        ShangQuan s5=new ShangQuan();
        s5.setImage("https://c-ssl.duitang.com/uploads/item/201506/19/20150619182752_iTm5A.thumb.700_0.jpeg");
        s5.setName("商圈5");
        shangQuanList.add(s5);

        ShangQuan s6=new ShangQuan();
        s6.setImage("https://c-ssl.duitang.com/uploads/item/201810/18/20181018164316_wddcq.thumb.700_0.jpg");
        s6.setName("商圈6");
        shangQuanList.add(s6);
    }

    private void initShangQuanTuiJian(){
        initShangQuanTuiJianDatas();
        shangquanTuiJianRecyclerView=dongTaiHeaderView.findViewById(R.id.shangquanTuiJianRecyclerView);
        shangQuanTuiJianRecyclerViewAdapter=new ShangQuanTuiJianRecyclerViewAdapter(getContext());
        LinearLayoutManager ms= new LinearLayoutManager(getContext());
        ms.setOrientation(LinearLayoutManager.HORIZONTAL);
        shangquanTuiJianRecyclerView.setLayoutManager(ms);
        shangquanTuiJianRecyclerView.setAdapter(shangQuanTuiJianRecyclerViewAdapter);
        shangQuanTuiJianRecyclerViewAdapter.setData(shangQuanList);
    }

    int totalDy = 0;
    private void initDongTaiViews(){
        initReDianDatas();
        dongtaiRecyclerView =view.findViewById(R.id.dongtaiRecyclerView);
        homeTuiJianDongTaiRecyclerViewAdapter =new HomeTuiJianDongTaiRecyclerViewAdapter(getActivity());
        dongtaiRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dongtaiRecyclerView.setLayoutManager(mLayoutManager);
        dongtaiRecyclerView.setNestedScrollingEnabled(false);
        homeTuiJianDongTaiRecyclerViewAdapter.addDatas(dongTaiList);
        dongtaiRecyclerView.setAdapter(homeTuiJianDongTaiRecyclerViewAdapter);

        initDongTaiHeaderView();

        dongtaiRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalDy -= dy;
                if(Math.abs(totalDy)>=bannerRootLayoutHeight&&bannerRootLayoutHeight>0){
                    changeColor=false;
                    headerView.setBackgroundColor(getContext().getResources().getColor(R.color.text_color_yellow));
                    backgroudView.setBackgroundColor(getContext().getResources().getColor(R.color.text_color_yellow));
                }else{
                    changeColor=true;
                    Glide.with(getContext()).asBitmap().load(list_path.get(currentBannerIndex)).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            if(changeColor){
                                parseRGB(resource);
                            }
                        }
                    });
                }
            }
        });
    }

}
