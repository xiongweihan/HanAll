package com.example.hanall.activity;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hanall.R;
import com.example.hanall.adapter.AddressBookAdapter;
import com.example.hanall.model.CityBean;
import com.example.hanall.utils.recycleview.RecycleUtil;
import com.example.hanall.widget.SectionDecoration;
import com.example.hanall.widget.SideBarView;
import com.github.promeg.pinyinhelper.Pinyin;
import com.github.promeg.pinyinhelper.PinyinDict;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 通讯录
 */
public class AddressListActivity extends TitleActivity {

    //    @BindView(R.id.recycler_view_address_list)
    RecyclerView recyclerView;
    //    @BindView(R.id.sidebar_view)
    SideBarView sideBarView;
    private List<String> characterList = new LinkedList<>();
    private TextView tvCharacherView;
    private List<CityBean> addressBookList = new ArrayList<>();
    private AddressBookAdapter addressBookAdapter;

    @Override
    protected int getLayoutId2() {
        return R.layout.activity_address_list;
    }

    @Override
    protected void initView() {
        super.initView();
        recyclerView = findViewById(R.id.recycler_view_address_list);
        sideBarView = findViewById(R.id.sidebar_view);
        tvCharacherView = findViewById(R.id.tv_character_view);
        requestData();

    }


    @Override
    protected void initData() {
        setTitle("通讯录");
        super.initData();
//        characterList = Arrays.asList(getResources().getStringArray(R.array.characters));
        sideBarView.setEqualItemSpace(false);
        sideBarView.setContentDataList(characterList);

        addressBookAdapter = new AddressBookAdapter(addressBookList, AddressListActivity.this);
        RecycleUtil.getInstance(this, recyclerView)
                .setVertical()
                .setDivder(new SectionDecoration(this, position -> {
                    if (!TextUtils.isEmpty(addressBookList.get(position).getTag())) {
                        return addressBookList.get(position).getTag();
                    }
                    return "";
                }))
                .setAdapter(addressBookAdapter);
    }

    @Override
    protected void initListener() {
        super.initListener();
        sideBarView.setOnItemClickListener(new SideBarView.onItemClickListener() {
            @Override
            public void onItemDown(int position, String itemContent) {
                tvCharacherView.setVisibility(View.VISIBLE);
                tvCharacherView.setText(itemContent);
            }

            @Override
            public void onItemMove(int position, String itemContent) {
                tvCharacherView.setText(itemContent);
                if (position != -1) {
                    ((LinearLayoutManager) Objects.requireNonNull(recyclerView.getLayoutManager())).scrollToPositionWithOffset(position, 0);
                }

            }

            @Override
            public void inItemUp(int position, String itemContent) {
                tvCharacherView.setText(itemContent);
                new Handler().postDelayed(() -> tvCharacherView.setVisibility(View.GONE), 300);

            }
        });
    }

    private void requestData() {
        addressBookList.clear();
        addressBookList.add(new CityBean("z", "张三"));
        addressBookList.add(new CityBean("z", "张三南风"));
        addressBookList.add(new CityBean("l", "李四"));
        addressBookList.add(new CityBean("w", "王五"));
        addressBookList.add(new CityBean("m", "慕容铁柱"));
        addressBookList.add(new CityBean("o", "欧雁翠花"));
        addressBookList.add(new CityBean("", "afanfa"));
        addressBookList.add(new CityBean("", "@"));
        addressBookList.add(new CityBean("", "2"));
        addressBookList.add(new CityBean("", "使得否"));
        addressBookList.add(new CityBean("", "丁福根"));
        addressBookList.add(new CityBean("", "券位"));
        addressBookList.add(new CityBean("", "有机会"));
        addressBookList.add(new CityBean("", "雷"));
        addressBookList.add(new CityBean("", "看i"));
        addressBookList.add(new CityBean("", "新追查"));
        addressBookList.add(new CityBean("", "光环"));
        addressBookList.add(new CityBean("", "欢个"));

        initSourceDatas();
    }

    /**
     * 初始化原始数据源，并取出索引数据源
     */
    private void initSourceDatas() {
        for (int i = 0; i < addressBookList.size(); i++) {
            CityBean cityBean = addressBookList.get(i);

            /*
              利用TinyPinyin将char转成拼音
               查看源码，方法内 如果char为汉字，则返回大写拼音
               如果c不是汉字，则返回String.valueOf(c)
              如：张三 ---> Z(大写)
              a张三 --- >z（小写）
             */
            String pinyin = Pinyin.toPinyin(cityBean.getCity().charAt(0));

            Log.e("pinyin", "pinyin:" + pinyin);

            String tagStr = pinyin.substring(0, 1);
            if (tagStr.matches("[a-z]")) {
                tagStr = tagStr.toUpperCase();//将字母转为大写
            }
            if (tagStr.matches("[A-Z]")) {
                cityBean.setTag(tagStr);
            } else {
                cityBean.setTag("#");
            }
        }
        sortData();
        for (CityBean cityBean : addressBookList) {
            if (!characterList.contains(cityBean.getTag())) {
                characterList.add(cityBean.getTag());
            }
        }

    }

    private void sortData() {
        Collections.sort(addressBookList, (bean1, bean2) -> {
            if (bean1.getTag().equals("#")) {
                return 1;
            } else if (bean2.getTag().equals("#")) {
                return -1;
            } else {
                return bean1.getTag().compareTo(bean2.getTag());
            }
        });
    }
}
