<template>

    <div>
        <wxc-cell label="统计"
                  :title="totalDate"
                  @wxcCellClicked="getParams">
        </wxc-cell>
        <wxc-cell label="业务员"
                  :title="total.DeliverName"
                  @wxcCellClicked="getParams">
        </wxc-cell>
        <wxc-cell label="会员数"
                  :title="total.intUCount "
                  @wxcCellClicked="getParams">
        </wxc-cell>
        <wxc-cell label="订单数"
                  :title="total.intOCount "
                  @wxcCellClicked="getParams">
        </wxc-cell>
        <wxc-cell label="订单额"
                  :title="total.intSumPrice "
                  @wxcCellClicked="getParams">
        </wxc-cell>
        <wxc-cell label="客单价"
                  :title="total.intPerPrice  "
                  @wxcCellClicked="getParams">
        </wxc-cell>
        <wxc-cell label="关闭单金额"
                  :title="total.intClosePrice "
                  @wxcCellClicked="getParams">
        </wxc-cell>
        <wxc-cell label="折扣额"
                  :title="total.intDisPrice  "
                  @wxcCellClicked="getParams">
        </wxc-cell>
        <scroller scroll-direction="horizontal"
                  class="table"
                  :style="{height: `${deviceHeight}px`}">
            <list class="bui-list"
                  ref="list"
                  loadmoreoffset="2">
                <cell class="table-cell">
                    <div class="table-td table-head width-200px"><text class="table-text">日期</text></div>
                    <div class="table-td table-head"><text class="table-text">业务员</text></div>
                    <div class="table-td table-head width-200px"><text class="table-text">客户</text></div>
                    <div class="table-td table-head width-100px"><text class="table-text">会员号</text></div>
                    <div class="table-td table-head width-200px"><text class="table-text">产品</text></div>
                    <div class="table-td table-head width-200px"><text class="table-text">总价</text></div>
                    <div class="table-td table-head width-100px"><text class="table-text">运费</text></div>
                    <div class="table-td table-head width-100px"><text class="table-text">折扣</text></div>
                    <div class="table-td table-head width-200px"><text class="table-text">订单状态</text></div>
                </cell>
                <cell class="table-cell"
                      v-for="(item , key) in listItem"
                      :key="key">
                    <div class="table-td width-200px"><text class="table-text">{{item.OrderDate}}</text></div>
                    <div class="table-td"><text class="table-text">{{item.DeliverName}}</text></div>
                    <div class="table-td width-200px"><text class="table-text">{{item.Column1}}</text></div>
                    <div class="table-td width-100px"><text class="table-text">{{item.UserId}}</text></div>
                    <div class="table-td width-200px"><text class="table-text">{{item.ProductName}}</text></div>
                    <div class="table-td width-200px"><text class="table-text">{{item.ProductTotalAmount}}</text></div>
                    <div class="table-td width-100px"><text class="table-text">{{item.Freight}}</text></div>
                    <div class="table-td width-100px"><text class="table-text">{{item.OnlinePaymentDiscounts}}</text></div>
                    <div class="table-td width-200px"><text class="table-text">{{item.strOrderStatus}}</text></div>
                </cell>
            </list>

        </scroller>

    </div>
</template>
<script>
import { WxcCell, WxcSimpleFlow, WxcButton } from "weex-ui";
import API from "Utils/api";
export default {
    components: { WxcCell, WxcSimpleFlow, WxcButton },
    data() {
        return {
            tapBackTime: 0,
            navShow: true,
            statusBarStyle: "LightContent",
            listItem: [],
            total: {},
            totalDate: ""
        };
    },
    eros: {
        beforeAppear(params, options) {
            this.total = params.total;
            this.listItem = params.listItem;
            var len = params.listItem.length;
            this.totalDate =
                params.listItem[0].OrderDate +
                " 至 " +
                params.listItem[len - 1].OrderDate;
        }
    },
    computed: {
        deviceHeight() {
            return (
                (weex.config.env.deviceHeight / weex.config.env.deviceWidth) *
                750 *
                100
            );
        }
    },
    methods: {
        toDetail(item) {
            this.$router.open({
                name: "performanceDetail",
                type: "PUSH",
                params: item
            });
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
    width: 1450px;
    /* min-height: 750px; */
}
.table-cell {
    position: relative;
    width: 1450px;
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

