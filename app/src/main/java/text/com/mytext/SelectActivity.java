package text.com.mytext;

import android.content.Context;
import android.os.Bundle;
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
            saleDimensionsBean = new Gson().fromJson(ConstantsHelper.selectJson2, new TypeToken<SaleDimensionsBean>() {
            }.getType());
        } else {
//            saleDimensionsBean = new Gson().fromJson(ConstantsHelper.selectJson2, new TypeToken<SaleDimensionsBean>() {
//            }.getType());
            saleDimensionsBean = new Gson().fromJson(ConstantsHelper.selectJson3, new TypeToken<SaleDimensionsBean>() {
            }.getType());
        }
        Log.e("selectJson", ConstantsHelper.selectJson);
                mAdapter = new MyAdapter(mContext, saleDimensionsBean);
                //设置默认的skuid
                mAdapter.setSelectSkuid(MainActivity.PUBLIC_SKUID);
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
                StringBuffer result = new StringBuffer();
                for (Map.Entry<Integer, SaleDimensionsBean.DimBean.SaleAttrBean> entry : selectSaleAttrBean.entrySet()) {
                    result.append(selectSaleAttrBean.get(entry.getKey()).getSaleValue() + "  ");
                }
                Toast.makeText(mContext, "点击了" + result, Toast.LENGTH_SHORT).show();
                mTvSelectResult.setText("已选择： " + result);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            MainActivity.PUBLIC_SKUID = mAdapter.getPublicSkuid();
            Toast.makeText(mContext, "公共的  skuid:" + mAdapter.getPublicSkuid() + "", Toast.LENGTH_SHORT).show();
            mAdapter.removerPublicSkuid();
        }
        return super.onKeyDown(keyCode, event);
    }
}
