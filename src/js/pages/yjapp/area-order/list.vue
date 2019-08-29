<template>
    <div class="page">
        <div class="search-bar">
            <search-bar :showScanBtn="true"
                        :searchType="searchType"
                        @onChange="typeChange"
                        @inputChange="inputChange"
                        @scanClick="scanClick"
                        @searchClick="searchClick">
            </search-bar>
            <datepick ref="datePick"
                      :beginDate="param['@StartDate']"
                      :endDate="param['@EndDate']"
                      init
                      @selectTime="selectTime"
                      @startDateFinish="startDateFinish"
                      @endDateFinish="endDateFinish"></datepick>
            <div class="search-bar-top">
                <div class="search-bar-left"
                     @click="selectProduct">
                    <div class="search-text-box">
                        <text class="search-bar-left-text">品类: </text>
                        <text class="search-bar-right-text">{{selectProductData.strName}}</text>
                        <image src="http://yj.kiy.cn/Content/Images/App/assets/la.png"
                               class="search-bar-left-icon"></image>
                    </div>
                </div>
                <!-- <div class="search-bar-left"
                     @click="selectDeliver">
                    <div class="search-text-box">
                        <text class="search-bar-left-text">业务员: </text>
                        <text class="search-bar-right-text">{{selectDeliverData.RealName}}</text>
                        <image src="http://yj.kiy.cn/Content/Images/App/assets/la.png"
                               class="search-bar-left-icon"></image>
                    </div>
                </div> -->
            </div>

        </div>
        <scroller scroll-direction="horizontal"
                  class="table">
            <div class="table-cell">
                <div class="table-td table-head"><text class="table-text">订单号</text></div>
                <div class="table-td table-head"><text class="table-text">会员号</text></div>
                <div class="table-td table-head"><text class="table-text">下单时间</text></div>
                <div class="table-td table-head"><text class="table-text">订单金额</text></div>
                <div class="table-td table-head"><text class="table-text">发货方式</text></div>
                <div class="table-td table-head"><text class="table-text">业务员</text></div>
                <div class="table-td table-head"><text class="table-text">品类</text></div>
                <div class="table-td table-head"><text class="table-text">省</text></div>
            </div>
            <!-- @loadmore="getData" -->
            <list class="bui-list"
                  ref="list"
                  :show-scrollbar="true"
                  :showRefresh="true"
                  @refresh="onrefresh"
                  loadmoreoffset="2">
                <cell class="table-cell"
                      v-for="(item , key) in listData"
                      @click="toDetail(item)"
                      :key="key">
                    <div class="table-td"><text class="table-text">{{item.Id}}</text></div>
                    <div class="table-td"><text class="table-text">{{item.UserId}}</text></div>
                    <div class="table-td"><text class="table-text">{{item.OrderDate}}</text></div>
                    <div class="table-td"><text class="table-text">{{item.ProductTotalAmount}}</text></div>
                    <div class="table-td"><text class="table-text">{{item.strRemindtype}}</text></div>
                    <div class="table-td"><text class="table-text">{{item.DeliverName}}</text></div>
                    <div class="table-td"><text class="table-text">{{item.strQName}}</text></div>
                    <div class="table-td"><text class="table-text">{{item.RegionFullName}}</text></div>
                </cell>
                <loading class="loading"
                         @loading="loadMore"
                         :display="showload ? 'show' : 'hide'">
                    <text class="indicator-text">加载更多 ...</text>
                    <loading-indicator class="indicator"></loading-indicator>
                </loading>
            </list>

        </scroller>
        <wxc-popover ref="wxc-popover"
                     :buttons="btns"
                     :position="popoverPosition"
                     :arrowPosition="popoverArrowPosition"
                     @wxcPopoverButtonClicked="popoverButtonClicked"></wxc-popover>
    </div>

</template>
<script>
import { WxcPopover } from "weex-ui";
import datepick from "../_mods/datepick";
import searchBar from "../_mods/search-bar";
import API from "Utils/api";
const picker = weex.requireModule("picker");
function checkNumber(theObj) {
    var reg = /^[0-9]+.?[0-9]*$/;
    if (reg.test(theObj)) {
        return true;
    }
    return false;
}
export default {
    components: {
        datepick,
        searchBar,
        WxcPopover
    },
    data() {
        return {
            listData: [],
            param: {
                "@rowIndex": 0
            },
            showload: false,
            sub: "",
            listType: "",
            onrefreshState: false,
            PayCollectionMoneySum: 0,
            UnPayCollectionMoneySum: 0,
            orderSumPriceSum: 0,
            orderCountSum: 0,
            UnSyncSum: 0,
            selectDeliverData: {
                RealName: "全部"
            },
            selectProductData: {
                strName: "全部"
            },
            deliverList: [],
            productsList: [],
            index: -1,
            index2: -1,
            showload: false,
            refresh: false,
            initGet: true,
            searchType: "订单号",
            searchValue: "",
            btns: [
                {
                    text: "订单号",
                    key: "订单号"
                },
                {
                    text: "会员号",
                    key: "会员号"
                }
            ],
            popoverPosition: { x: 15, y: 100 },
            popoverArrowPosition: { pos: "top", x: 50 }
        };
    },
    computed: {
        userInfo() {
            var userInfo = API.get_userInfo(this);
            return userInfo;
        }
    },
    created() {
        this.$router.getParams().then(resData => {
            this.init(resData);
        });
    },
    mounted() {
        var _this = this;
        this.$event.on("paySuccess", params => {
            _this.selectAllStatu = false;
            _this.getData();
        });
    },
    methods: {
        async getData(loadmore) {
            const _this = this;
            let param = {
                key: "RyAkcFaz6KCKdW06EQKFHNEISQDDA",
                strSysMac: "30-5A-3A-E4-4F-14",
                strSysCode: "YJAPP",
                strAuthor: "YJ"
            };
            this.param["@rowIndex"]++;
            if (this.initGet) {
                this.$notice.loading.show("正在加载");
            }
            param = Object.assign(this.param, param);

            if (this.searchType == this.btns[0].key && this.searchValue != "") {
                param = Object.assign(param, {
                    Id: this.searchValue
                });
            } else {
                delete param.Id;
            }
            if (this.searchType == this.btns[1].key && this.searchValue != "") {
                param = Object.assign(param, {
                    UserId: this.searchValue
                });
            } else {
                delete param.UserId;
            }

            if (this.userInfo.RoleId == 1 || this.userInfo.RoleId == 4) {

            } else {
                if(this.userInfo.RoleId == 8 || this.userInfo.RoleId == 13) {
                    param = Object.assign(param, {
                        SalesmanId: this.userInfo.adminId
                    });
                }else {
                    param = Object.assign(param, {
                        IdManager: this.userInfo.adminId
                    });
                }

            }
            // if (this.selectDeliverData.RealName != "全部") {
            //     param = Object.assign(param, {
            //         "@DistributorId": this.selectDeliverData.Id
            //     });
            // }
            if (this.selectProductData.strName != "全部") {
                param = Object.assign(param, {
                    qCode: this.selectProductData.Id
                });
            }
            try {
                var RES = await API.get_areaList(param);
                if (param["@rowIndex"] === 1) {
                    this.listData = [];
                }
                var DGDATA = RES.DATA.Head;
                if (DGDATA.length != 0) {
                    this.listData = [];
                    this.listData = this.listData.concat(DGDATA);
                } else {
                    this.$notice.toast({
                        message: "已经到底部"
                    });
                }
                this.setStatus();
            } catch (error) {
                this.$notice.toast({
                    message: error
                });
            }
        },
        init(param) {
            this.$navigator.setCenterItem(
                {
                    text: param.params.title, // 展示的文字 (和图片 二选一)
                    textColor: "", // 文字颜色 (默认为白色)
                    fontSize: "32", // 字号（默认32px）
                    fontWeight: "normal", // 是否加粗（默认不加粗）
                    image: "" // 展示的图片url (和文字 二选一，文字优先级更高)
                },
                () => {
                    // 点击回调
                }
            );
            // 获取月头
            const dateFormate = {
                startDate: API.get_date("beginMonth").split(" ")[0],
                endDate: API.get_date("today")
            };
            this.$refs["datePick"].searchDate["@beginDate"] =
                dateFormate.startDate;
            this.$refs["datePick"].searchDate["@endDate"] = dateFormate.endDate;
            this.param = Object.assign(this.param, {
                dStartDate: dateFormate.startDate,
                dEndDate: dateFormate.endDate
            });
            this.param = Object.assign(this.param, param.type);

            // this.QueryAdminList();
            this.getProductsType();
            // this.setNav();
            this.getData();
        },
        startDateFinish(startDate) {
            this.param["dStartDate"] = startDate;
            if (this.param["dEndDate"] == undefined) {
                this.param["dEndDate"] = startDate;
            }
            this.getData();
        },
        endDateFinish(endDate) {
            this.param["dEndDate"] = endDate;
            if (this.param["dEndDate"] == undefined) {
                this.param["dEndDate"] = endDate;
            }
            this.getData();
        },
        selectDeliver() {
            var items = [];
            if (items.length === 0) {
                this.deliverList.map(item => {
                    items.push(item.RealName);
                });
            }
            picker.pick(
                {
                    index: this.index,
                    items
                },
                event => {
                    if (event.result === "success") {
                        this.selectDeliverData = this.deliverList[event.data];
                        this.index = event.data;
                        this.getData();
                    }
                }
            );
        },
        selectProduct() {
            var items = [];
            if (items.length === 0) {
                // items.push("全部")
                this.productsList.map(item => {
                    items.push(item.strName);
                });
            }
            picker.pick(
                {
                    index: this.index2,
                    items
                },
                event => {
                    if (event.result === "success") {
                        this.selectProductData = this.productsList[event.data];
                        this.index2 = event.data;
                        this.initGetData();
                    }
                }
            );
        },
        async getProductsType() {
            let par = {
                key: "RyAkcFaz6KCKdW06EQKFHNEISQDDA",
                strSysMac: "30-5A-3A-E4-4F-14",
                strSysCode: "YJAPP",
                strAuthor: "YJ"
            };
            const RES = await API.get_productsList(par);
            if (RES.result == "ok") {
                this.productsList = RES.dgData;
            } else {
                this.$notice.toast({
                    message: "获取产品信息失败"
                });
            }
        },
        async QueryAdminList() {
            var par = {
                $RoleId: "8,13"
            };
            const RES = await API.get_adminList(par);
            if (RES.map) {
                var DATA = RES.map.dgData;
            } else {
                var DATA = RES.dgData;
            }
            this.deliverList = DATA;
        },
        initGetData() {
            this.param["@rowIndex"] = 0;
            this.listData = [];
            this.initGet = true;
            this.getData();
        },
        onrefresh() {
            this.refresh = true;
            this.param["@rowIndex"] = 0;
            this.listData = [];
            this.getData();
        },
        loadMore() {
            this.showload = true;
            this.getData();
        },
        setStatus() {
            if (this.refresh) {
                this.$refs["list"].refreshEnd();
                this.refresh = false;
            }
            if (this.showload) {
                this.showload = false;
            }
            if (this.initGet) {
                this.initGet = false;
                this.$notice.loading.hide();
            }
        },
        typeChange(value) {
            this.$refs["wxc-popover"].wxcPopoverShow();
        },
        inputChange(val) {
            this.searchValue = val;
        },
        scanClick(val) {
            this.searchValue = val;
            this.initGetData();
        },
        searchClick(value) {
            this.initGetData();
            this.$tools.resignKeyboard().then(
                resData => {
                    // 收起成功的回调
                },
                error => {}
            );
        },
        popoverButtonClicked(obj) {
            this.searchType = obj.key;
        },
        toDetail(item) {
            let Router = "areaOrderDetail";
            let Item = item;
            this.$router.open({
                name: Router,
                type: "PUSH",
                params: Item
            });
        }
    }
};
</script>

<style scoped>
.page {
    height: auto;
}
.scroller {
    height: auto;
}
.search-bar {
    border-bottom-width: 1px;
    border-bottom-style: solid;
    border-bottom-color: #dddddd;
}
.mask {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(0, 0, 0, 0.4);
}

.modal-top {
    align-items: flex-start;
}

.calendar {
    height: 800px;
    background-color: #ffffff;
    select-color: #1da1f2;
}

.search-date {
    flex-direction: row;
    justify-content: center;
    height: 100px;
    background-color: rgba(31, 181, 252, 0.4);
    border-top-width: 1px;
    border-top-style: solid;
    border-top-color: #eee;
}
.search-date-text {
    text-align: center;
    width: 375px;
    height: 100px;
    line-height: 100px;
    color: #000;
}
.search-batch {
    width: 750px;
    height: 100px;
    justify-content: center;
    align-items: center;
    border-bottom-width: 1px;
    border-bottom-style: solid;
    border-bottom-color: #eee;
}
.search-batch-btn {
    width: 400px;
    height: 80px;
    line-height: 80px;
    color: #fff;
    border-radius: 12px;
    text-align: center;
    background-color: #1da1f2;
}
.bottom-box {
    position: relative;
    width: 750px;
    height: 100px;
}
.pay-content {
    margin-left: 84px;
    margin-top: 312px;
    width: 580px;
    height: 300px;
    background-color: #fff;
    border-radius: 4px;
}
.pay-title {
    height: 105px;
    justify-content: center;
    padding-left: 48px;
    border-bottom-width: 1px;
    border-bottom-style: dotted;
    border-bottom-color: #cccccc;
}
.pay-group {
    flex-direction: row;
    padding-top: 52px;
    justify-content: center;
}
.pay-type {
    width: 215px;
    height: 66px;
}
.example {
    position: absolute;
    right: 0px;
    top: 20px;
    flex-direction: row;
    justify-content: flex-start;
}
.label {
    font-size: 20px;
    line-height: 60px;
    width: 350px;
    color: #666;
    text-align: right;
    margin-right: 20px;
}
.info {
    font-size: 30px;
    line-height: 60px;
    color: #bbb;
    margin-left: 10px;
}
.table {
    width: 1600px;
    min-height: 750px;
}
.table-cell {
    position: relative;
    width: 1600px;
    flex-direction: row;
}
.table-td {
    display: block;
    float: left;
    width: 200px;
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
.width-200px {
    width: 200px;
}
.width-100px {
    width: 100px;
}
.loading {
    width: 750;
    display: -ms-flex;
    display: -webkit-flex;
    display: flex;
    -ms-flex-align: center;
    -webkit-align-items: center;
    -webkit-box-align: center;
    align-items: center;
}
.indicator-text {
    color: #888888;
    font-size: 42px;
    text-align: center;
}
.indicator {
    margin-top: 16px;
    height: 40px;
    width: 40px;
    color: blue;
}
.select {
    border-width: 1px;
    border-style: solid;
    border-color: rgb(25, 190, 107);
    border-radius: 4px;
    padding: 5px;
    color: rgb(25, 190, 107);
}
.select-two {
    border-color: rgb(237, 63, 20);
    color: rgb(237, 63, 20);
}
.select-three {
    border-color: blue;
    color: blue;
}
.search-bar-top {
    display: flex;
    height: 100px;
    flex-direction: row;
    background-color: #fff;
}
.search-bar-left {
    width: 375px;
    height: 84px;
    /* padding-top: 35px; */
    background-color: #fff;
    align-content: center;
    justify-content: center;
    flex-direction: row;
    color: #666666;
}
.search-text-box {
    flex-direction: row;
    align-items: center;
    justify-content: center;
    width: 375px;
    height: 100px;
    line-height: 100px;
    /* color: #000; */
}
.search-bar-right-text {
    width: 250px;
    height: 50px;
    padding-left: 20px;
    line-height: 50px;
    background-color: #f2f2f2;
    color: #7c7c7c;
    lines: 1;
    text-overflow: hidden;
    font-size: 28px;
}
.search-bar-left-text {
    color: #282828;
    font-size: 28px;
}
.search-bar-left-icon {
    margin-top: 5px;
    margin-left: 5px;
    width: 20px;
    height: 20px;
}
</style>
