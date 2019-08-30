<template>
    <div class="win">
        <div class="page">
            <div class="search-bar">
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

                    <div class="search-bar-left"
                         v-if="!isDeliver"
                         @click="selectDeliver">
                        <div class="search-text-box">
                            <text class="search-bar-left-text">业务员: </text>
                            <text class="search-bar-right-text">{{selectDeliverData.RealName}}</text>
                            <image src="http://yj.kiy.cn/Content/Images/App/assets/la.png"
                                   class="search-bar-left-icon"></image>
                        </div>
                    </div>
                </div>

            </div>

            <scroller scroll-direction="horizontal"
                      class="table"
                      :style="{height: `${deviceHeight}px`}">

                <div class="table-cell">
                    <div class="table-td table-head"><text class="table-text">业务员</text></div>
                    <div class="table-td table-head width-100px"><text class="table-text">会员数</text></div>
                    <div class="table-td table-head width-100px"><text class="table-text">订单数</text></div>
                    <div class="table-td table-head width-200px"><text class="table-text">订单额</text></div>
                    <div class="table-td table-head width-200px"><text class="table-text">客单价</text></div>
                </div>
                <!-- @loadmore="getData" -->
                <list class="bui-list"
                      ref="list"
                      :show-scrollbar="true"
                      :showRefresh="true"
                      @refresh="getData"
                      loadmoreoffset="2">
                    <cell class="table-cell"
                          v-for="(item , key) in listData"
                          :key="key">
                        <div class="table-td"><text class="table-text">{{item.DeliverName}}</text></div>
                        <div class="table-td width-100px"><text class="table-text">{{item.intUCount}}</text></div>
                        <div class="table-td width-100px"><text class="table-text">{{item.intOCount}}</text></div>
                        <div class="table-td width-200px"><text class="table-text">{{item.intSumPrice}}</text></div>
                        <div class="table-td width-200px"><text class="table-text">{{item.intPerPrice}}</text></div>
                    </cell>
                </list>

            </scroller>

        </div>
        <div class="bottom-sum">
            <div class="table-cell">
                <div class="table-td table-bottom "><text class="table-text">汇总:</text></div>
                <div class="table-td table-bottom width-100px"><text class="table-text">{{intUCountTotal}}</text></div>
                <div class="table-td table-bottom width-100px"><text class="table-text">{{intOCountTotal}}</text></div>
                <div class="table-td table-bottom width-200px"><text class="table-text">{{intSumPriceTotal}}</text></div>
                <div class="table-td table-bottom width-200px"><text class="table-text">{{intPerPriceTotal }}</text></div>

            </div>
        </div>
    </div>
</template>
<script>
import datepick from "../_mods/datepick";
import API from "Utils/api";
import { accAdd } from "Utils/tool";
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
        datepick
    },
    data() {
        return {
            listData: [],
            param: {},
            showload: false,
            sub: "",
            listType: "",
            onrefreshState: false,
            intUCountTotal: 0,
            intOCountTotal: 0,
            intSumPriceTotal: 0,
            intPerPriceTotal: 0,
            selectDeliverData: {
                RealName: "全部"
            },
            selectProductData: {
                strName: "全部"
            },
            deliverList: [],
            productsList: [],
            index: -1,
            index2: -1
        };
    },
    computed: {
        userInfo() {
            var userInfo = API.get_userInfo(this);
            return userInfo;
        },
        deviceHeight() {
            return (
                (weex.config.env.deviceHeight / weex.config.env.deviceWidth) *
                    750 -
                420
            );
        },
        isDeliver() {
            var userInfo = API.get_userInfo(this);
            return userInfo.RoleId == 8 || userInfo.RoleId == 13;
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
        async getData(onrefreshState) {
            const _this = this;
            let param = {
                key: "RyAkcFaz6KCKdW06EQKFHNEISQDDA",
                strSysMac: "30-5A-3A-E4-4F-14",
                strSysCode: "YJAPP",
                strAuthor: "YJ"
            };

            param = Object.assign(this.param, param);
            if (this.userInfo.RoleId == 1 || this.userInfo.RoleId == 4) {
            } else {
                if (this.userInfo.RoleId == 8 || this.userInfo.RoleId == 13) {
                    param = Object.assign(param, {
                        SalesmanId: this.userInfo.adminId
                    });
                } else {
                    param = Object.assign(param, {
                        IdManager: this.userInfo.adminId
                    });
                }
            }
            if (this.selectDeliverData.RealName != "全部") {
                param = Object.assign(param, {
                    SalesmanId: this.selectDeliverData.Id
                });
            }

            if (this.selectProductData.strName != "全部") {
                param = Object.assign(param, {
                    qCode: this.selectProductData.Id
                });
            }
            if (onrefreshState) {
            } else {
                this.$notice.loading.show("正在加载");
            }
            var RES = await API.get_OrderSumManager_YSH(param);
            this.listData = [];
            var DGDATA = RES.DATA;
            this.intUCountTotal = 0;
            this.intOCountTotal = 0;
            this.intSumPriceTotal = 0;
            this.intPerPriceTotal = 0;
            if (DGDATA.length != 0) {
                var newData = [];
                DGDATA.map(item => {
                    this.intUCountTotal = accAdd(
                        this.intUCountTotal,
                        item.intUCount
                    );
                    this.intOCountTotal = accAdd(
                        this.intOCountTotal,
                        item.intOCount
                    );
                    this.intSumPriceTotal = accAdd(
                        this.intSumPriceTotal,
                        item.intSumPrice
                    );
                    this.intPerPriceTotal = accAdd(
                        this.intPerPriceTotal,
                        item.intPerPrice
                    );
                });
                this.intSumPriceTotal = this.intSumPriceTotal.toFixed(2)
                this.intPerPriceTotal = (this.intPerPriceTotal / DGDATA.length).toFixed(6)
                this.listData = DGDATA;
            } else {
                this.$notice.toast({
                    message: "没有更多数据"
                });
            }
            if (onrefreshState) {
                this.$refs["list"].refreshEnd();
            } else {
                this.$notice.loading.hide();
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

            this.QueryAdminList();
            this.getProductsType();
            this.setNav();
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
                        this.getData();
                    }
                }
            );
        },
        setNav() {
            if (this.userInfo.RoleId === 1 || this.userInfo.RoleId === 4) {
                // this.$navigator.setRightItem(
                //     {
                //         text: this.selectDeliverData.RealName, // 展示的文字 (和图片 二选一)
                //         textColor: "", // 文字颜色 (默认为白色)
                //         fontSize: "40", // 字号（默认32px）
                //         fontWeight: "normal" // 是否加粗（默认不加粗）
                //     },
                //     () => {
                //         // 点击回调
                //         this.selectDeliver();
                //     }
                // );
            }
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
        toDetail(item, type) {
            var par = {};
            par = Object.assign(par, this.param);
            if (type) {
                par = Object.assign(par, type);
            }
            par = Object.assign(par, { "@DistributorId": item.Id });
            par = Object.assign(par, { name: item.RealName });
            this.$router.open({
                name: "echartdetail",
                type: "PUSH",
                params: par
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
    width: 750px;
    /* min-height: 750px; */
}
.table-cell {
    position: relative;
    width: 750px;
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
.table-bottom {
    height: 75px;
    background-color: yellow;
}
</style>
