package text.com.mytext.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import text.com.mytext.FlowLayout;
import text.com.mytext.R;
import text.com.mytext.SaleDimensionsBean;
import text.com.mytext.Utils;

/**
 * 作者：Zhon JianXiong
 * 时间：2019/11/4 11:15
 */
public class MyAdapter  extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    /** 规格列表 */
    private List<SaleDimensionsBean.DimBean> mDimBeanList;
    /** 已经选择的规格集合， MAP 每种规格类型通过 position 去重*/
    private Map<Integer, SaleDimensionsBean.DimBean.SaleAttrBean> mSelectSaleAttrBean;
    /** 库存列表 */
    private List<SaleDimensionsBean.StockBean> mStockBeanList;

    private List<Integer> mSimilarGroupSkuIds;

    private Context mContext;

    private int mSelectSkuid = 0;
    /** 是否为团购商品  默认是 false */
    private boolean mIsGroupBuying = false;

    private int mPublicSkuid = 0;

    private StringBuffer mPublicSkuidArr = new StringBuffer();

    public MyAdapter(Context context, SaleDimensionsBean saleDimensionsBean) {
        this.mContext = context;
        this.mDimBeanList = saleDimensionsBean.getDim();
        this.mStockBeanList = saleDimensionsBean.getStock();
        mSelectSaleAttrBean = new HashMap<>();

    }


    public void setSimilarGroupSkuIds(List<Integer> similarGroupSkuIds ) {
        this.mSimilarGroupSkuIds = similarGroupSkuIds;
    }

    /**
     * 设置已选中的产品
     * @param mSelectSkuid  默认的或者已经选中的skuid
     * @param isGroupBuying 是否为团购商品  默认是 false
     */
    public void setSelectSkuid(int mSelectSkuid, boolean isGroupBuying) {
        this.mSelectSkuid = mSelectSkuid;

        //传入的skuid不能为空或者0，如果是就false,非团购善品
        if (mSelectSkuid == 0) {
            this.mIsGroupBuying = false;
        } else {
            this.mIsGroupBuying = isGroupBuying;
        }
    }

    public Map<Integer, SaleDimensionsBean.DimBean.SaleAttrBean> getSelectSaleAttrBean() {
        return mSelectSaleAttrBean;
    }

    /**
     * 获取共同的skuid
     * @return
     */
    public int getPublicSkuid() {
        if (mPublicSkuidArr.length() > 0) {
            Log.e("mPublicSkuid", mPublicSkuidArr.toString() + "");
            String[] arr = mPublicSkuidArr.toString().split(",");
            try {
                String mPublicSkuidStr = Utils.findMaxString(arr);
                mPublicSkuid = Integer.valueOf(mPublicSkuidStr);
            } catch (Exception e) {
                Log.e("mPublicSkuid", e.getMessage() + "");
                mPublicSkuid = 0;
            }
        }
        return mPublicSkuid;
    }

    /**
     * 清除相关信息
     */
    public void removerPublicSkuid() {
        mPublicSkuidArr.setLength(0);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return (ViewHolder) holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        /** 当前规格信息 */
        SaleDimensionsBean.DimBean dimBean = mDimBeanList.get(position);
        /*** 产品规格分类名称，如颜色， 尺码等 */
        holder.mTvTitle.setText(dimBean.getSaleName());
        /** 根据本规格循环添加规格流式布局参数数据（纯色、黑色、白色等）*/
        List<SaleDimensionsBean.DimBean.SaleAttrBean> mSaleAttrBean = dimBean.getSaleAttr();
        //判断此规格是否为空且有数据
        if (mSaleAttrBean != null && mSaleAttrBean.size() > 0) {
            holder.mFlowLayout.removeAllViews();
            for (int i = 0; i < mSaleAttrBean.size(); i++) {
                addChildText(holder.mFlowLayout, mSaleAttrBean.get(i), position);
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTvTitle;
        FlowLayout mFlowLayout;

        public ViewHolder(View view) {
            super(view);
            mTvTitle = view.findViewById(R.id.tv_title);
            mFlowLayout = view.findViewById(R.id.flow_history);
        }
    }

    @Override
    public int getItemCount() {
        return mDimBeanList.size();
    }

    /**
     * 动态添加列数布局的数据
     * @param group 流式布局
     * @param saleAttrBean   流式布局中的子View
     */
    private void addChildText(FlowLayout group, final SaleDimensionsBean.DimBean.SaleAttrBean saleAttrBean,final  int position) {
        TextView tvChild = (TextView) LayoutInflater.from(mContext).inflate(R.layout.flow_select, group, false);
        /** 循环遍历库存，判断库存是否为0， 为0 不可选择，这里循环较多，建议优化*/
        // 7 8 9 10 11
        for (int i = 0; i < mStockBeanList.size(); i++) {
            // 7 8 9 10 11
            for (int j = 0; j < saleAttrBean.getSkuIds().size(); j++) {
                if (mStockBeanList.get(i).getSkuId() == saleAttrBean.getSkuIds().get(j)) {
                    //库存== 0 不可点击
                    if (mStockBeanList.get(i).getStock() != 0) {
                        saleAttrBean.setSelect(true);
                        //在库存满足的情况下，skuids 匹配，匹配不上则不可选择
                        if (mSelectSkuid != 0 && saleAttrBean.getSkuIds().get(j).equals(mSelectSkuid)) {
                            mSelectSaleAttrBean.put(position, saleAttrBean);
                        }
                        if (compareList(saleAttrBean.getSkuIds(), mSelectSaleAttrBean, position) && mSelectSaleAttrBean.size() > 0) {
                            saleAttrBean.setSelect(false);
                        }
                        //团购
                        for (int i1 = 0; i1 < mSimilarGroupSkuIds.size(); i1++) {
                            if (mSimilarGroupSkuIds.get(i1).equals(saleAttrBean.getSkuIds().get(j))) {
                                saleAttrBean.setSelect(false);
                            }
                        }
                    }
                }
            }
        }
        if (saleAttrBean.isSelect()) { //可选项
            //已经选择的规格,显示红色
            if (mSelectSaleAttrBean.get(position) != null && mSelectSaleAttrBean.get(position).getSaleValue().equals(saleAttrBean.getSaleValue())) {
                if (mIsGroupBuying) {//团购商品
                    tvChild.setEnabled(false);
                } else {
                    tvChild.setEnabled(true);
                }
                for (int i = 0; i < saleAttrBean.getSkuIds().size(); i++) {
                    mPublicSkuidArr.append(saleAttrBean.getSkuIds().get(i) + ",");
                }
                tvChild.setBackgroundResource(R.drawable.frame_reed_gray_10);
                tvChild.setTextColor(mContext.getResources().getColor(R.color.color_dd4e40));
            } else { // 未选择
                if (mIsGroupBuying) {//团购商品
                    tvChild.setEnabled(false);
                    tvChild.setBackgroundResource(R.drawable.frame_f4_10);
                    tvChild.setTextColor(mContext.getResources().getColor(R.color.color_33171717));
                } else {//非团购
                    tvChild.setEnabled(true);
                    tvChild.setBackgroundResource(R.drawable.frame_f4_10);
                    tvChild.setTextColor(mContext.getResources().getColor(R.color.color_333333));
                }
            }
        } else { //库存不足或skuid不匹配，不可选择
            tvChild.setEnabled(false);
            tvChild.setBackgroundResource(R.drawable.frame_f4_10);
            tvChild.setTextColor(mContext.getResources().getColor(R.color.color_33171717));
        }
        //添加规格
        tvChild.setText(saleAttrBean.getSaleValue());
        tvChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //二次点击取消上次所选规格,重复点击同一个规格时
                mSelectSkuid = 0;
                if (mSelectSaleAttrBean.get(position) != null && mSelectSaleAttrBean.get(position).getSaleValue().equals(saleAttrBean.getSaleValue())) {
                    mSelectSaleAttrBean.remove(position);
                } else {
                    //点击，同一个类型的只存储一个规格
                    mSelectSaleAttrBean.put(position, saleAttrBean);
                }
                //点击事件回调
                listener.onItemClicks(position, mSelectSaleAttrBean);
                removerPublicSkuid();
                notifyDataSetChanged();
            }
        });
        //流式布局添加子VIEW
        group.addView(tvChild);
    }

    /**
     * 点击按钮的事件
     */
    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClicks(int pos, Map<Integer, SaleDimensionsBean.DimBean.SaleAttrBean> selectSaleAttrBean);
    }

    /**
     * 根据已经选择的规格对象，匹配 skuids
     * @param list1  当前规格
     * @param saleAttrBeanMap   已经选择的规格集合
     * @param position  规格类型位置
     * @return
     */
    private boolean compareList(List<Integer> list1, Map<Integer, SaleDimensionsBean.DimBean.SaleAttrBean> saleAttrBeanMap,  int position) {

        for (Map.Entry<Integer, SaleDimensionsBean.DimBean.SaleAttrBean> entry : saleAttrBeanMap.entrySet()) {
            //skuid不和同类型的规格进行匹配
            if (entry.getKey() != position) {
                int containNum = 0;
                SaleDimensionsBean.DimBean.SaleAttrBean saleAttrBean = saleAttrBeanMap.get(entry.getKey());
                for (int j = 0; j < list1.size(); j++) {
                    for (int i = 0; i < saleAttrBean.getSkuIds().size(); i++) {
                        Log.e("aleAttrBean=: ",  saleAttrBean.getSkuIds().get(i).toString());
                        if (saleAttrBean.getSkuIds().get(i).equals(list1.get(j))) {
                            containNum = containNum + 1;
                        }
                    }
                }
                if (containNum > 0) {
                    return false;
                } else {
                    return true;
                }
            }
        }
        return false;
    }
}
