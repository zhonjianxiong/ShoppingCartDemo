# ShoppDemo尚品规格选择器
简单的购物车规格选择器，数据格式参考JD选择
# 技术点
集合 ： 用到了List,HashSet,HashMap,需要知道这几个集合的特点与用法
使用RecyclerView和FlowLayout对产品规格进行匹配，RecyclerView本身就是一个循环体
在RecyclerView绑定和刷新数据的时候使用了多层for循环，导致数据数据量大可能会慢，这块会在后期的版本中优化
# 因为无法根据后面的item数据刷新前面的item数据，目前做了一个胭脂刷新和获取多选数据的定时
  //延迟获取所选数据
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setSelectShop();
            }
        }, 200);
        
# 