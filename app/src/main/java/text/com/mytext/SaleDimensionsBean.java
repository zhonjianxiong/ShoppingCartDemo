package text.com.mytext;

import java.util.List;

public class SaleDimensionsBean {


    /** 数据源 */
    private List<DimBean> dim;
    /** 库存 */
    private List<StockBean> stock;

    public List<DimBean> getDim() {
        return dim;
    }

    public void setDim(List<DimBean> dim) {
        this.dim = dim;
    }

    public List<StockBean> getStock() {
        return stock;
    }

    public void setStock(List<StockBean> stock) {
        this.stock = stock;
    }

    public static class DimBean {
        /**
         * id : 2
         * saleName : 颜色
         * saleAttr : [{"saleValue":"运动休闲纯色6双（2黑2灰2白）","image":"http://img13.360buyimg.com/vc/jfs/t17704/124/1151702311/299126/462ee0a1/5abdd7b6N3f59ab75.jpg","skuIds":[10]},
         * {"saleValue":"运动网眼6双混色","image":"http://img13.360buyimg.com/vc/jfs/t17128/181/1751028252/369205/acecf4d6/5ad6be98Nab473716.jpg","skuIds":[11]},{
         * "saleValue":"时尚休闲6双混色","image":"http://img13.360buyimg.com/vc/jfs/t23392/238/2531382392/130202/a32be10b/5b84da97Nfeac1714.jpg","skuIds":[7]},
         * {"saleValue":"时尚条纹6双混色","image":"http://img13.360buyimg.com/vc/jfs/t16690/364/1264046990/300830/6901e412/5ac20e0bN159fb93c.jpg","skuIds":[8]},
         * {"saleValue":"时尚波点6双混色（3白2黑1灰）","image":"http://img13.360buyimg.com/vc/jfs/t19522/192/487003054/351035/15e33103/5a98c4b7N83c1163e.jpg","skuIds":[9]}]
         */

        private int id;
        private String saleName;
        private List<SaleAttrBean> saleAttr;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getSaleName() {
            return saleName;
        }

        public void setSaleName(String saleName) {
            this.saleName = saleName;
        }

        public List<SaleAttrBean> getSaleAttr() {
            return saleAttr;
        }

        public void setSaleAttr(List<SaleAttrBean> saleAttr) {
            this.saleAttr = saleAttr;
        }

        public static class SaleAttrBean {
            /**
             * saleValue : 运动休闲纯色6双（2黑2灰2白）
             * image : http://img13.360buyimg.com/vc/jfs/t17704/124/1151702311/299126/462ee0a1/5abdd7b6N3f59ab75.jpg
             * skuIds : [10]
             */

            private String saleValue;
            private String image;
            private List<Integer> skuIds;
            // true  可选择， false 不可选择
            private boolean isSelect = false;

            public String getSaleValue() {
                return saleValue;
            }

            public void setSaleValue(String saleValue) {
                this.saleValue = saleValue;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public List<Integer> getSkuIds() {
                return skuIds;
            }

            public void setSkuIds(List<Integer> skuIds) {
                this.skuIds = skuIds;
            }

            public boolean isSelect() {
                return isSelect;
            }

            public void setSelect(boolean select) {
                isSelect = select;
            }
        }
    }

    public static class StockBean {
        /**
         * skuId : 11
         * stock : 0
         */

        /**
         * skuId 库存id
         */
        private int skuId;
        /**
         * 库存
         */
        private int stock;

        public int getSkuId() {
            return skuId;
        }

        public void setSkuId(int skuId) {
            this.skuId = skuId;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }
    }
}
