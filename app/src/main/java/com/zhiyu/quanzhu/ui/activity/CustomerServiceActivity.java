package com.zhiyu.quanzhu.ui.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lcw.library.imagepicker.ImagePicker;
import com.leon.chic.dao.MessageDao;
import com.leon.chic.listener.ServiceMessageListener;
import com.leon.chic.model.ServiceMessage;
import com.leon.chic.utils.MessageUtils;
import com.leon.chic.utils.TimeUtils;
import com.zhiyu.quanzhu.R;
import com.zhiyu.quanzhu.base.BaseActivity;
import com.zhiyu.quanzhu.base.BaseResult;
import com.zhiyu.quanzhu.model.bean.CustomerServiceMessage;
import com.zhiyu.quanzhu.model.bean.MessageImage;
import com.zhiyu.quanzhu.model.bean.MessageText;
import com.zhiyu.quanzhu.model.result.CustomerServiceResult;
import com.zhiyu.quanzhu.ui.adapter.CustomerServiceAdapter;
import com.zhiyu.quanzhu.ui.toast.MessageToast;
import com.zhiyu.quanzhu.ui.widget.emoji.widget.EmojiBoard;
import com.zhiyu.quanzhu.ui.widget.emoji.widget.EmojiEdittext;
import com.zhiyu.quanzhu.utils.ConstantsUtils;
import com.zhiyu.quanzhu.utils.GlideLoader;
import com.zhiyu.quanzhu.utils.GsonUtils;
import com.zhiyu.quanzhu.utils.ImageUtils;
import com.zhiyu.quanzhu.utils.MyPtrHandlerFooter;
import com.zhiyu.quanzhu.utils.MyPtrHandlerHeader;
import com.zhiyu.quanzhu.utils.MyPtrRefresherFooter;
import com.zhiyu.quanzhu.utils.MyPtrRefresherHeader;
import com.zhiyu.quanzhu.utils.MyRequestParams;
import com.zhiyu.quanzhu.utils.ScreentUtils;
import com.zhiyu.quanzhu.utils.SoftKeyboardUtil;
import com.zhiyu.quanzhu.utils.UploadImageUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class CustomerServiceActivity extends BaseActivity implements View.OnClickListener, ServiceMessageListener {
    private LinearLayout mainLayout, backLayout;
    private ImageView settingImageView;
    private PtrFrameLayout ptrFrameLayout;
    private CardView bottomCardView;
    private RecyclerView mRecyclerView;
    private CustomerServiceAdapter adapter;
    private LinearLayout showEmojiLayout, addImageLayout;
    private EmojiEdittext textEditor;
    private EmojiBoard emojiBoard;
    private TextView sendBtn, enterShopTextView, titleTextView;
    private List<CustomerServiceMessage> list = new ArrayList<>();
    private final int REQUEST_SELECT_IMAGES_CODE = 1001;
    private ArrayList<String> mImageList = new ArrayList<>();
    private LinkedHashMap<String, String> map = new LinkedHashMap<>();
    private String myUserName, myAvatar;
    private int uid;
    private MyHandler myHandler = new MyHandler(this);
    private int shop_id;
    private String shop_name;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss");

    private static class MyHandler extends Handler {
        WeakReference<CustomerServiceActivity> activityWeakReference;

        public MyHandler(CustomerServiceActivity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            CustomerServiceActivity activity = activityWeakReference.get();
            switch (msg.what) {
                case 1:
                    activity.titleTextView.setText(activity.shop_name);
                    activity.ptrFrameLayout.refreshComplete();
                    activity.adapter.setList(activity.list);
                    if (null != activity.list && activity.list.size() > 0)
                        activity.mRecyclerView.smoothScrollToPosition(activity.list.size() - 1);
                    break;
                case 2:
                    MessageToast.getInstance(activity).show(activity.baseResult.getMsg());
                    break;
                case 3:
                    activity.ptrFrameLayout.refreshComplete();
                    activity.adapter.setList(activity.list);
                    if (activity.isRefresh) {
                        activity.mRecyclerView.smoothScrollToPosition(activity.list.size() - 1);
                    }
                    break;
                case 4:
                    activity.finish();
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.activity_customer_service);
        ScreentUtils.getInstance().setStatusBarLightMode(this, true);
        MessageDao.getInstance().setServiceMessageListener(this);
        shop_id = getIntent().getIntExtra("shop_id", 0);
        initPtr();
        initViews();
        hello();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MessageUtils.getInstance().setCurrentPage(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MessageUtils.getInstance().setCurrentPage(false);
    }

    private void initViews() {
        mainLayout = findViewById(R.id.mainLayout);
        bottomCardView = findViewById(R.id.bottomCardView);
        backLayout = findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        titleTextView = findViewById(R.id.titleTextView);
        enterShopTextView = findViewById(R.id.enterShopTextView);
        enterShopTextView.setOnClickListener(this);
        settingImageView = findViewById(R.id.settingImageView);
        settingImageView.setOnClickListener(this);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        adapter = new CustomerServiceAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);
        adapter.setList(list);

        showEmojiLayout = findViewById(R.id.showEmojiLayout);
        showEmojiLayout.setOnClickListener(this);
        addImageLayout = findViewById(R.id.addImageLayout);
        addImageLayout.setOnClickListener(this);
        sendBtn = findViewById(R.id.input_send);
        sendBtn.setOnClickListener(this);
        emojiBoard = findViewById(R.id.input_emoji_board);
        emojiBoard.hideBoard();
        textEditor = findViewById(R.id.input_editor);
        textEditor.isEnable(sendBtn);//这里是为了让未输入内容的时候不让点击
        emojiBoard.setItemClickListener(new EmojiBoard.OnEmojiItemClickListener() {//表情框点击事件
            @Override
            public void onClick(String code) {
                if (code.equals("/DEL")) {//删除图标
                    textEditor.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
                } else {//插入表情
                    textEditor.getText().insert(textEditor.getSelectionStart(), code);
                }
            }
        });
//        addLayoutListener(mainLayout, bottomCardView);
    }


    private void initPtr() {
        ptrFrameLayout = findViewById(R.id.ptr_frame_layout);
        ptrFrameLayout.setHeaderView(new MyPtrRefresherHeader(this));
        ptrFrameLayout.addPtrUIHandler(new MyPtrHandlerHeader(this, ptrFrameLayout));
        ptrFrameLayout.setFooterView(new MyPtrRefresherFooter(this));
        ptrFrameLayout.addPtrUIHandler(new MyPtrHandlerFooter(this, ptrFrameLayout));
        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                page = 1;
                isRefresh = true;
                historyMessageList();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page++;
                isRefresh = false;
                historyMessageList();
            }
        });
        ptrFrameLayout.setMode(PtrFrameLayout.Mode.REFRESH);
    }

    @Override
    public void serviceMessage(ServiceMessage message) {
        CustomerServiceMessage customerServiceMessage = new CustomerServiceMessage();
        customerServiceMessage.setCreate_time(TimeUtils.getInstance().getFormatTime(Long.parseLong(message.getCreate_time())));
        customerServiceMessage.setFrom(1);
        customerServiceMessage.setMsg_type(message.getMsg_type());
        customerServiceMessage.setShop_id(message.getShop_id());
        customerServiceMessage.setUser_avatar(message.getUser_avatar());
        customerServiceMessage.setUser_name(message.getUser_name());
        com.zhiyu.quanzhu.model.bean.Message serviceMessage = GsonUtils.GsonToBean(message.getMessage_content(), com.zhiyu.quanzhu.model.bean.Message.class);
        customerServiceMessage.setMessage(serviceMessage);
        list.add(customerServiceMessage);
        adapter.setList(list);
        mRecyclerView.smoothScrollToPosition(list.size() - 1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
//                finish();
                finishService();
                break;
            case R.id.showEmojiLayout:
                showEmojiBoard();
                break;
            case R.id.input_send:
                SoftKeyboardUtil.hideSoftKeyBoard(getWindow());
                emojiBoard.hideBoard();
                String content = textEditor.getText().toString();
                createTextMessage(content);
                textEditor.getText().clear();
                break;
            case R.id.addImageLayout:
                mImageList.clear();
                selectImages();
                break;
            case R.id.enterShopTextView:
                Intent shopIntent = new Intent(this, ShopInformationActivity.class);
                shopIntent.putExtra("shop_id", String.valueOf(shop_id));
                startActivity(shopIntent);
                break;
            case R.id.settingImageView:
                Intent settingIntent = new Intent(this, CustomerServiceSettingActivity.class);
                settingIntent.putExtra("shop_id", shop_id);
                startActivity(settingIntent);
                break;
        }
    }

    private void selectImages() {
        ImagePicker.getInstance()
                .setTitle("图片选择")//设置标题
                .showCamera(true)//设置是否显示拍照按钮
                .showImage(true)//设置是否展示图片
                .showVideo(false)//设置是否展示视频
                .setSingleType(true)//设置图片视频不能同时选择
                .setMaxCount(9)//设置最大选择图片数目(默认为1，单选)
                .setImagePaths(mImageList)//保存上一次选择图片的状态，如果不需要可以忽略
                .setImageLoader(new GlideLoader())//设置自定义图片加载器
                .start(CustomerServiceActivity.this, REQUEST_SELECT_IMAGES_CODE);//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_IMAGES_CODE && resultCode == RESULT_OK) {
            mImageList = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);
            uploadImages();
        }
    }


    private void createTextMessage(String content) {
        CustomerServiceMessage customerServiceMessage = new CustomerServiceMessage();
        MessageText messageText = new MessageText();
        messageText.setContent(content);
        com.zhiyu.quanzhu.model.bean.Message message = new com.zhiyu.quanzhu.model.bean.Message();
        message.setTxt(messageText);
        customerServiceMessage.setMessage(message);
        customerServiceMessage.setMsg_type(CustomerServiceMessage.TYPE_TEXT);
        customerServiceMessage.setFrom(2);
        customerServiceMessage.setCreate_time(sdf.format(new Date()));
        customerServiceMessage.setUser_name(myUserName);
        customerServiceMessage.setUser_avatar(myAvatar);
        customerServiceMessage.setUser_id(uid);
        sendMessage(GsonUtils.GsonString(customerServiceMessage));
        list.add(customerServiceMessage);
        adapter.setList(list);
        mRecyclerView.smoothScrollToPosition(list.size() - 1);
    }

    private void createImageMessage(String path, String url) {
        MessageImage messageImage = ImageUtils.getInstance().getMessageImage(path, url);
        CustomerServiceMessage customerServiceMessage = new CustomerServiceMessage();
        com.zhiyu.quanzhu.model.bean.Message message = new com.zhiyu.quanzhu.model.bean.Message();
        message.setImage(messageImage);
        customerServiceMessage.setMessage(message);
        customerServiceMessage.setMsg_type(CustomerServiceMessage.TYPE_IMAGE);
        customerServiceMessage.setFrom(2);
        customerServiceMessage.setUser_name(myUserName);
        customerServiceMessage.setUser_avatar(myAvatar);
        customerServiceMessage.setUser_id(uid);
        customerServiceMessage.setCreate_time(sdf.format(new Date()));
        sendMessage(GsonUtils.GsonString(customerServiceMessage));
        list.add(customerServiceMessage);
        adapter.setList(list);
    }

    private List<String> imagesUploadList = new ArrayList<>();

    private void uploadImages() {
        for (final String path : mImageList) {
            if (!map.containsKey(path)) {
                UploadImageUtils.getInstance().uploadFile(UploadImageUtils.CIRCLEFEES, path, new UploadImageUtils.OnUploadCallback() {
                    @Override
                    public void onUploadSuccess(String name) {
                        map.put(path, name);
                        imagesUploadList.add(name);
                        createImageMessage(path, name);
                    }
                });
            }
        }
    }

    @Override
    public void onBackPressed() {//监听返回键，如果表情框已显示就隐藏
        if (emojiBoard.getVisibility() == VISIBLE) {
            showEmojiBoard();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * addLayoutListener方法如下
     *
     * @param main   根布局
     * @param scroll 需要显示的最下方View
     */
    public void addLayoutListener(final View main, final View scroll) {
        main.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                //1、获取main在窗体的可视区域
                main.getWindowVisibleDisplayFrame(rect);
                //2、获取main在窗体的不可视区域高度，在键盘没有弹起时，main.getRootView().getHeight()调节度应该和rect.bottom高度一样
                int mainInvisibleHeight = main.getRootView().getHeight() - rect.bottom;
                int screenHeight = main.getRootView().getHeight();//屏幕高度
                //3、不可见区域大于屏幕本身高度的1/4：说明键盘弹起了
                if (mainInvisibleHeight > screenHeight / 4) {
                    int[] location = new int[2];
                    scroll.getLocationInWindow(location);
                    // 4､获取Scroll的窗体坐标，算出main需要滚动的高度
                    int srollHeight = (location[1] + scroll.getHeight()) - rect.bottom;
                    //5､让界面整体上移键盘的高度
                    main.scrollTo(0, srollHeight);
                } else {
                    //3、不可见区域小于屏幕高度1/4时,说明键盘隐藏了，把界面下移，移回到原有高度
                    main.scrollTo(0, 0);
                }
            }
        });
    }

    /**
     * 展开or隐藏表情框
     */
    public void showEmojiBoard() {
        showEmojiLayout.setSelected(emojiBoard.getVisibility() == GONE);//设置图片选中效果
        emojiBoard.showBoard();//是否显示表情框
    }

    private BaseResult baseResult;
    private CustomerServiceResult customerServiceResult;

    private void hello() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL_2 + ConstantsUtils.CUSTOMER_SERVICE_HELLO);
        params.addBodyParameter("shop_id", String.valueOf(shop_id));
        params.setAsJsonContent(true);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("hello: " + result);
                customerServiceResult = GsonUtils.GsonToBean(result, CustomerServiceResult.class);
                shop_name = customerServiceResult.getData().getData().getShop().getName();
                myUserName = customerServiceResult.getData().getData().getUser().getUsername();
                myAvatar = customerServiceResult.getData().getData().getUser().getAvatar();
                uid = customerServiceResult.getData().getData().getUser().getUid();
                list = customerServiceResult.getData().getData().getList();
                Message message = myHandler.obtainMessage(1);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("hello: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void sendMessage(String messageJson) {
//        System.out.println("shop_id: " + shop_id + " , messaage: " + messageJson);
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL_2 + ConstantsUtils.CUSTOMER_SERVICE_SEND_MESSAGE);
        params.addBodyParameter("shop_id", String.valueOf(shop_id));
        params.addBodyParameter("message", messageJson);
        params.setAsJsonContent(true);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                System.out.println("sendMessage: " + result);
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(2);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
//                System.out.println("sendMessage: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private int page = 1;
    private boolean isRefresh = true;

    private void historyMessageList() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL_2 + ConstantsUtils.CUSTOMER_SERVICE_MESSAGE_LIST);
        params.addBodyParameter("shop_id", String.valueOf(shop_id));
        params.addBodyParameter("page", String.valueOf(page));
        params.setAsJsonContent(true);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("历史消息: " + result);
                customerServiceResult = GsonUtils.GsonToBean(result, CustomerServiceResult.class);
                if (isRefresh) {
                    list = customerServiceResult.getData().getData().getList();
                } else {
                    list.addAll(0, customerServiceResult.getData().getData().getList());
                }
                Message message = myHandler.obtainMessage(3);
                message.sendToTarget();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("历史消息: " + ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 当按下返回键时所执行的命令
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 此处写你按返回键之后要执行的事件的逻辑
            finishService();
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    private void finishService() {
        RequestParams params = MyRequestParams.getInstance(this).getRequestParams(ConstantsUtils.BASE_URL_2 + ConstantsUtils.USER_FINISH_SERVICE);
        params.addBodyParameter("shop_id", String.valueOf(shop_id));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("finish service: " + result);
                baseResult = GsonUtils.GsonToBean(result, BaseResult.class);
                Message message = myHandler.obtainMessage(4);
                message.sendToTarget();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
