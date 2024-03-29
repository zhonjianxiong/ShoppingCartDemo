package text.com.mytext;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import text.com.mytext.adapter.MyAdapter;


/**
 * Created by hu on 2019/11/1.
 */

public class SelectActivity extends AppCompatActivity {

    ImageView mIvPic;
    TextView mTvTitle;
    TextView mTvSelectResult;
    RecyclerView mRecyclerView;
    SaleDimensionsBean saleDimensionsBean;
    MyAdapter mAdapter;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        mIvPic = findViewById(R.id.iv_pic);
        mTvTitle = findViewById(R.id.tv_title);
        mTvSelectResult = findViewById(R.id.tv_selected_result);
        mRecyclerView = findViewById(R.id.recyclerview);
        mContext = SelectActivity.this;
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //本地数据源
        int type = getIntent().getIntExtra("type", 0);
        if (type == 0) {
            saleDimensionsBean = new Gson().fromJson(ConstantsHelper.selectJson, new TypeToken<SaleDimensionsBean>() {
            }.getType());
        } else if (type == 1) {
//            saleDimensionsBean = new Gson().fromJson(ConstantsHelper.selectJson2, new TypeToken<SaleDimensionsBean>() {
//            }.getType());
            saleDimensionsBean = new Gson().fromJson(ConstantsHelper.selectJson2, new TypeToken<SaleDimensionsBean>() {
            }.getType());
        } else if (type == 2) {
//            saleDimensionsBean = new Gson().fromJson(ConstantsHelper.selectJson2, new TypeToken<SaleDimensionsBean>() {
//            }.getType());
            saleDimensionsBean = new Gson().fromJson(ConstantsHelper.selectJson3, new TypeToken<SaleDimensionsBean>() {
            }.getType());
        } else if (type == 3) {
//            saleDimensionsBean = new Gson().fromJson(ConstantsHelper.selectJson2, new TypeToken<SaleDimensionsBean>() {
//            }.getType());
            saleDimensionsBean = new Gson().fromJson(ConstantsHelper.selectJson4, new TypeToken<SaleDimensionsBean>() {
            }.getType());
        }
        Log.e("selectJson", ConstantsHelper.selectJson);
        mAdapter = new MyAdapter(mContext, saleDimensionsBean);
        //设置默认的skuid,是否是团购商品
        mAdapter.setSelectSkuid(MainActivity.PUBLIC_SKUID, true);
        List<Integer> similarGroupSkuIds = new ArrayList<>();
        similarGroupSkuIds.add(50);
        similarGroupSkuIds.add(51);
        mAdapter.setSimilarGroupSkuIds(similarGroupSkuIds);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(SelectActivity.this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        select();
    }


    /**
     * 点击相应的规格，返回结果
     * 返回的是一个 MAP 集合
     */
    private void select() {
        mAdapter.setListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClicks(int pos,  Map<Integer, SaleDimensionsBean.DimBean.SaleAttrBean> selectSaleAttrBean) {
                selectShop();
            }
        });
        selectShop();
    }
    /**
     * 选择尚品
     */
    private void selectShop () {
        //延迟获取所选数据
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setSelectShopView();
            }
        }, 200);
    }

    /**
     * 将已经选择的产品显示出来
     */
    private void setSelectShopView() {
        Map<Integer, SaleDimensionsBean.DimBean.SaleAttrBean> selectSaleAttrBean = mAdapter.getSelectSaleAttrBean();
        StringBuffer result = new StringBuffer();
        for (Map.Entry<Integer, SaleDimensionsBean.DimBean.SaleAttrBean> entry : selectSaleAttrBean.entrySet()) {
            result.append(selectSaleAttrBean.get(entry.getKey()).getSaleValue() + "  ");
        }

        mTvSelectResult.setText("已选择： " + result);
        Toast.makeText(mContext, "已选择" + result, Toast.LENGTH_SHORT).show();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            MainActivity.PUBLIC_SKUID = mAdapter.getPublicSkuid();
            Toast.makeText(mContext, "公共的  skuid:" + MainActivity.PUBLIC_SKUID+ "", Toast.LENGTH_SHORT).show();
        }
        return super.onKeyDown(keyCode, event);
    }
}
