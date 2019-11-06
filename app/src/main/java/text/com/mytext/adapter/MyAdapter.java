package text.com.mytext.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import text.com.mytext.FlowLayout;
import text.com.mytext.R;
import text.com.mytext.SaleDimensionsBean;

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

    private Context mContext;

    public MyAdapter(Context context, SaleDimensionsBean saleDimensionsBean) {
        this.mContext = context;
        this.mDimBeanList = saleDimensionsBean.getDim();
        this.mStockBeanList = saleDimensionsBean.getStock();
        mSelectSaleAttrBean = new HashMap<>();
    }


    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select, parent, false);
        RecyclerView.ViewHolder holder = new MyAdapter.ViewHolder(view);
        return (MyAdapter.ViewHolder) holder;
    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {

        /**
         * 当前规格信息
         */
        SaleDimensionsBean.DimBean dimBean = mDimBeanList.get(position);
        /**
         * 产品规格分类名称，如颜色， 尺码等
         */
        holder.mTvTitle.setText(dimBean.getSaleName());
        /**
         * 根据本规格循环添加规格流式布局参数数据（纯色、黑色、白色等）
         */
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
        /** 循环遍历库存，判断库存是否为0， 为0 不可选择*/
        // 7 8 9 10 11
        for (int i = 0; i < mStockBeanList.size(); i++) {
            // 7 8 9 10 11
            for (int j = 0; j < saleAttrBean.getSkuIds().size(); j++) {
                if (mStockBeanList.get(i).getSkuId() == saleAttrBean.getSkuIds().get(j)) {
                    //库存==0 不可点击
                    if (mStockBeanList.get(i).getStock() != 0) {
                        saleAttrBean.setSelect(true);
                        if (!compareList(saleAttrBean.getSkuIds(), mSelectSaleAttrBean, position)) {
                            saleAttrBean.setSelect(false);
                        }
                    }
                }
            }
        }
        if (saleAttrBean.isSelect()) { //可选项
            tvChild.setEnabled(true);
            //已经选择的规格,显示红色
            if (mSelectSaleAttrBean.get(position) != null && mSelectSaleAttrBean.get(position).getSaleValue().equals(saleAttrBean.getSaleValue())) {
                tvChild.setBackgroundResource(R.drawable.frame_reed_gray_10);
                tvChild.setTextColor(mContext.getResources().getColor(R.color.color_dd4e40));
            } else { // 未选择
                tvChild.setBackgroundResource(R.drawable.frame_f4_10);
                tvChild.setTextColor(mContext.getResources().getColor(R.color.color_333333));
            }
        } else { //库存不足，不可选择
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
                if (mSelectSaleAttrBean.get(position) != null && mSelectSaleAttrBean.get(position).getSaleValue().equals(saleAttrBean.getSaleValue())) {
                    mSelectSaleAttrBean.remove(position);
                } else {
                    //点击，同一个类型的只存储一个规格
                    mSelectSaleAttrBean.put(position, saleAttrBean);
                }
                //点击事件回调
                listener.onItemClicks(position, mSelectSaleAttrBean);
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

    //集合数据对比是否相同
    private boolean compareList(List<Integer> list1, Map<Integer, SaleDimensionsBean.DimBean.SaleAttrBean> saleAttrBeanMap,  int position) {
        for (Map.Entry<Integer, SaleDimensionsBean.DimBean.SaleAttrBean> entry : saleAttrBeanMap.entrySet()) {
            if (entry.getKey() != position) {
                if (entry.getKey() != position) {
                    SaleDimensionsBean.DimBean.SaleAttrBean saleAttrBean = saleAttrBeanMap.get(entry.getKey());
                    Set<Integer> hashCodeSet = new HashSet<>();
                    if (entry.getKey() > position) {
                        for (Integer adInfoData : saleAttrBean.getSkuIds()) {
                            hashCodeSet.add(adInfoData.hashCode());
                        }
                        for (Integer adInfoData : list1) {
                            if (!hashCodeSet.contains(adInfoData.hashCode())){
                                return false;
                            }
                        }
                    } else {
                        for (Integer adInfoData : list1) {
                            hashCodeSet.add(adInfoData.hashCode());
                        }
                        for (Integer adInfoData : saleAttrBean.getSkuIds()) {
                            if (!hashCodeSet.contains(adInfoData.hashCode())){
                                return false;
                            }
                        }
                    }

                }
            }
        }
        return true;
    }




}
