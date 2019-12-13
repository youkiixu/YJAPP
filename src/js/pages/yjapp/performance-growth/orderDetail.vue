<template>

    <div>
        <wxc-cell label="业务员"
                  :title="listItem.DeliverName"
                  @wxcCellClicked="getParams">
        </wxc-cell>
        <wxc-cell label="订单号"
                  :title="listItem.Id "
                  @wxcCellClicked="getParams">
        </wxc-cell>
        <wxc-cell label="客户"
                  :title="listItem.Column1 "
                  @wxcCellClicked="getParams">
        </wxc-cell>
        <wxc-cell label="会员号"
                  :title="listItem.UserId "
                  @wxcCellClicked="getParams">
        </wxc-cell>
        <wxc-cell label="产品名称"
                  :title="listItem.ProductName "
                  @wxcCellClicked="getParams">
        </wxc-cell>
        <wxc-cell label="总价"
                  :title="listItem.ProductTotalAmount "
                  @wxcCellClicked="getParams">
        </wxc-cell>
        <wxc-cell label="折扣额"
                  :title="listItem.OnlinePaymentDiscounts  "
                  @wxcCellClicked="getParams">
        </wxc-cell>
        <wxc-cell label="运费"
                  :title="listItem.Freight "
                  @wxcCellClicked="getParams">
        </wxc-cell>
        <wxc-cell label="订单状态"
                  :title="listItem.strOrderStatus "
                  @wxcCellClicked="getParams">
        </wxc-cell>
        <wxc-cell label="配送类型"
                  :title="listItem.strRemindType "
                  @wxcCellClicked="getParams">
        </wxc-cell>
        <wxc-cell label="订单日期"
                  :title="listItem.OrderDate "
                  @wxcCellClicked="getParams">
        </wxc-cell>
    </div>
</template>
<script>
import { WxcCell } from "weex-ui";
import API from "Utils/api";
export default {
    components: { WxcCell },
    data() {
        return {
            tapBackTime: 0,
            navShow: true,
            statusBarStyle: "LightContent",
            listItem: {},
            total: {},
            totalDate: ""
        };
    },
    eros: {
        beforeAppear(params, options) {
            this.listItem = params;
        }
    },
    mounted() {},
    created() {}
};
</script>
<style>
.top-three-top {
    flex-direction: row;
    justify-items: center;
    align-items: center;
    width: 750px;
    /* height: 72px; */
    margin-top: 20px;
    /* padding-right: 40px; */
}
.bottom-btn {
    width: 750px;
    border-top-width: 1px;
    border-top-style: solid;
    border-top-color: #e5e5e5;
    padding-top: 20px;
    margin-top: 20px;
    margin-bottom: 20px;
    flex-direction: row;
    align-items: center;
    justify-content: center;
}
.table {
    width: 1650px;
    /* min-height: 750px; */
}
.table-cell {
    position: relative;
    width: 1650px;
    flex-direction: row;
}
.table-td {
    display: block;
    float: left;
    width: 150px;
    height: 100px;
    line-height: 100px;
    align-items: center;
    justify-content: center;
    border-width: 2px;
    border-color: #2096f2;
    border-style: solid;
    background-color: #fff;
}
.table-text {
    font-size: 32px;
}
.table-head {
    height: 75px;
    background-color: #e9eaec;
}
.width-50px {
    width: 50px;
}
.width-200px {
    width: 200px;
}
.width-100px {
    width: 100px;
}
</style>

