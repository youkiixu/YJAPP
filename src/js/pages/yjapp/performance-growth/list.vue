<template>
    <div class="win">
        <div class="page">
            <div class="search-bar">
                <!-- <search-bar :showScanBtn="true"
                            v-if="!isDeliver"
                            :searchType="searchType"
                            @inputChange="inputChange"
                            @searchClick="searchClick">
                </search-bar> -->
                <!-- <datepick ref="datePick"
                          :beginDate="param['@StartDate']"
                          :endDate="param['@EndDate']"
                          init
                          @selectTime="selectTime"
                          @startDateFinish="startDateFinish"
                          @endDateFinish="endDateFinish"></datepick> -->
                <div class="search-bar-top">
                    <div class="search-bar-left"
                         @click="selectYear">
                        <div class="search-text-box">
                            <text class="search-bar-left-text">年: </text>
                            <text class="search-bar-right-text">{{param.intYear}}</text>
                            <image src="http://yj.kiy.cn/Content/Images/App/assets/la.png"
                                   class="search-bar-left-icon"></image>
                        </div>
                    </div>

                    <div class="search-bar-left"
                         @click="selectMonth">
                        <div class="search-text-box">
                            <text class="search-bar-left-text">月: </text>
                            <text class="search-bar-right-text">{{param.intMonth}}</text>
                            <image src="http://yj.kiy.cn/Content/Images/App/assets/la.png"
                                   class="search-bar-left-icon"></image>
                        </div>
                    </div>
                </div>
                <!-- <div class="search-bar-top">
                    <div class="search-bar-left"
                         @click="selectOrderStatus">
                        <div class="search-text-box">
                            <text class="search-bar-left-text">状态: </text>
                            <text class="search-bar-right-text">{{selectOrderStatusData.strName}}</text>
                            <image src="http://yj.kiy.cn/Content/Images/App/assets/la.png"
                                   class="search-bar-left-icon"></image>
                        </div>
                    </div>

                    <div class="search-bar-left"
                         @click="selectProduction">
                        <div class="search-text-box">
                            <text class="search-bar-left-text">审核: </text>
                            <text class="search-bar-right-text">{{selectProductionData.strName}}</text>
                            <image src="http://yj.kiy.cn/Content/Images/App/assets/la.png"
                                   class="search-bar-left-icon"></image>
                        </div>
                    </div>
                </div> -->
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
                         @click="selectRemindType">
                        <div class="search-text-box">
                            <text class="search-bar-left-text">配送: </text>
                            <text class="search-bar-right-text">{{selectRemindTypeData.strName}}</text>
                            <image src="http://yj.kiy.cn/Content/Images/App/assets/la.png"
                                   class="search-bar-left-icon"></image>
                        </div>
                    </div>

                </div>
                <div class="search-bar-top"
                     >
                    <!-- <div class="search-bar-left"
                         v-if="!isDeliver"
                         @click="selectDeliver">
                        <div class="search-text-box">
                            <text class="search-bar-left-text">业务员: </text>
                            <text class="search-bar-right-text">{{selectDeliverData.RealName}}</text>
                            <image src="http://yj.kiy.cn/Content/Images/App/assets/la.png"
                                   class="search-bar-left-icon"></image>
                        </div>
                    </div> -->

                    <div class="search-bar-left"
                         @click="selectbNew">
                        <div class="search-text-box">
                            <text class="search-bar-left-text">新会员: </text>
                            <text class="search-bar-right-text">{{selectbNewData.strName}}</text>
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
                    <div class="table-td table-head width-350px"><text class="table-text">业务员</text></div>
                    <div class="table-td table-head width-400px"><text class="table-text">订单金额</text></div>
                    <!-- <div class="table-td table-head width-100px"><text class="table-text">订单数</text></div>
                    <div class="table-td table-head width-200px"><text class="table-text">订单额</text></div>
                    <div class="table-td table-head width-200px"><text class="table-text">客单价</text></div>
                    <div class="table-td table-head width-200px"><text class="table-text">关闭单金额</text></div>
                    <div class="table-td table-head width-200px"><text class="table-text">折扣额</text></div>
                    <div class="table-td table-head width-200px"><text class="table-text">日期</text></div> -->
                </div>
                <!-- @loadmore="getData" -->
                <list class="bui-list"
                      ref="list"
                      :show-scrollbar="true"
                      :showRefresh="true"
                      @refresh="getData"
                      loadmoreoffset="2">
                    <cell class="table-cell">
                        <div class="table-td table-bottom width-350px"><text class="table-text">汇总:</text></div>
                        <div class="table-td table-bottom width-400px"><text class="table-text">{{intUCountTotal}}</text></div>
                        <!-- <div class="table-td table-bottom width-100px"><text class="table-text">{{intOCountTotal}}</text></div> -->
                        <!-- <div class="table-td table-bottom width-200px"><text class="table-text">{{intSumPriceTotal}}</text></div>
                        <div class="table-td table-bottom width-200px"><text class="table-text">{{intPerPriceTotal }}</text></div>
                        <div class="table-td table-bottom width-200px"><text class="table-text">{{intClosePriceTotal }}</text></div>
                        <div class="table-td table-bottom width-200px"><text class="table-text">{{intDisPriceTotal ? intDisPriceTotal : intOnlinePaymentDiscountsTotal }}</text></div>
                        <div class="table-td table-bottom width-200px"><text class="table-text">日期</text></div> -->
                    </cell>
                          <!-- @click="toDetail(item)" -->
                    <cell class="table-cell"
                          v-for="(item , key) in listData"
                          :key="key">
                        <div class="table-td width-350px"><text class="table-text">{{item.strName}}</text></div>
                        <div class="table-td width-400px"><text class="table-text">{{item.intSumPrice}}</text></div>
                        <!-- <div class="table-td width-100px"><text class="table-text">{{item.intOCount}}</text></div>
                        <div class="table-td width-200px"><text class="table-text">{{item.intSumPrice}}</text></div>
                        <div class="table-td width-200px"><text class="table-text">{{item.intPerPrice}}</text></div>
                        <div class="table-td width-200px"><text class="table-text">{{item.intClosePrice}}</text></div>
                        <div class="table-td width-200px"><text class="table-text">{{item.intDisPrice ? item.intDisPrice : item.OnlinePaymentDiscounts}}</text></div>
                        <div class="table-td width-200px"><text class="table-text">{{item.orderDate}}</text></div> -->
                    </cell>
                </list>

            </scroller>

        </div>
        <bmmask class="mask"
                animation="transition"
                position="top"
                :duration="300"
                ref="payType">
            <bmpop class="modal-top">
                <div class="pay-content">
                    <div class="pay-title">
                        <wxc-button text="确定"
                                    type="blue"
                                    size="big"
                                    @wxcButtonClicked="wxcButtonClicked"></wxc-button>
                    </div>
                    <div class="pay-group">
                        <scroller class="scroller"
                                  :show-scrollbar="false"
                                  loadmoreoffset="10">
                            <wxc-checkbox-list ref="payList"
                                               :list="productsCheckList"
                                               @wxcCheckBoxListChecked="wxcCheckBoxListChecked"></wxc-checkbox-list>
                        </scroller>
                    </div>
                </div>
            </bmpop>
        </bmmask>
    </div>
</template>
<script>
import datepick from "../_mods/datepick";
import searchBar from "../_mods/search-bar";
import API from "Utils/api";
import { accAdd } from "Utils/tool";
const picker = weex.requireModule("picker");
import { WxcCheckbox, WxcCheckboxList, WxcButton } from "weex-ui";
function checkNumber(theObj) {
    var reg = /^[0-9]+.?[0-9]*$/;
    if (reg.test(theObj)) {
        return true;
    }
    return false;
}
export default {
    components: {
        searchBar,
        datepick,
        WxcCheckboxList,
        WxcCheckbox,
        WxcButton
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
            intClosePriceTotal: 0,
            intDisPriceTotal: 0,
            intOnlinePaymentDiscountsTotal: 0,
            selectDeliverData: {
                RealName: "全部"
            },
            selectProductData: {
                strName: "全部"
            },
            selectProductionData: {
                strName: "全部"
            },
            selectRemindTypeData: {
                strName: "全部"
            },
            selectRemindTypeData: {
                strName: "全部"
            },
            selectbNewData: {
                strName: '否'
            },
            bNewList: [
                {
                    strName: '否',
                    Id: false
                },
                {
                    strName: '是',
                    Id: true
                }
            ],
            productionList: [
                {
                    strName: "全部"
                },
                {
                    strName: "已审",
                    Id: 1
                },
                {
                    strName: "未审",
                    Id: 0
                }
            ],
            selectOrderStatusData: {
                strName: "全部"
            },
            orderStatusList: [
                {
                    strName: "全部"
                },
                {
                    strName: "待付款",
                    Id: 1
                },
                {
                    strName: "待发货",
                    Id: 2
                },
                {
                    strName: "待收货",
                    Id: 3
                },
                {
                    strName: "交易完成",
                    Id: 5
                },
                {
                    strName: "已关闭",
                    Id: 4
                }
            ],
            remindTypeList: [
                {
                    strName: "全部"
                },
                {
                    strName: "印捷配送",
                    Id: 3
                },
                {
                    strName: "代发快递",
                    Id: 1
                },
                {
                    strName: "自提",
                    Id: 2
                },

                {
                    strName: "代发物流",
                    Id: 5
                },
                {
                    strName: "商家直邮",
                    Id: 0
                }
            ],
            yearList: [2019,2020,2021,2022,2023,2024,2025],
            monthList: [1,2,3,4,5,6,7,8,9,10,11,12],
            deliverList: [],
            productsList: [],
            productsCheckList: [],
            index: -1,
            index2: 0,
            index3: 0,
            index4: 0,
            index5: 0,
            btnStyle: {
                position: "absolute"
            },
            searchType: "业务员",
            searchValue: ""
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
                380
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
    mounted() {},
    methods: {
        // 获取数据 onrefreshState是下拉刷新
        async getData(onrefreshState) {
            const _this = this;
            let param = {
                key: "RyAkcFaz6KCKdW06EQKFHNEISQDDA",
                strSysMac: "30-5A-3A-E4-4F-14",
                strSysCode: "YJAPP",
                strAuthor: "YJ"
                // UserId: this.userInfo.Id
            };

            param = Object.assign(this.param, param);
            // 做权限控制
            // if (this.userInfo.RoleId == 1 || this.userInfo.RoleId == 4) {
            // } else {
            //     if (this.userInfo.RoleId == 8 || this.userInfo.RoleId == 13) {
            //         param = Object.assign(param, {
            //             SalesmanId: this.userInfo.adminId
            //         });
            //     } else {
            //         param = Object.assign(param, {
            //             IdManager: this.userInfo.adminId
            //         });
            //     }
            // }
            // 选择业务员
            // if (this.selectDeliverData.RealName != "全部") {
            //     param = Object.assign(param, {
            //         SalesmanId: this.selectDeliverData.Id
            //     });
            // }

            // 选择产品类别
            if (this.selectProductData.strName != "全部") {
                param = Object.assign(param, {
                    qCode: this.selectProductData.Id
                });
            } else {
                delete param["qCode"];
            }

            // 选择审核
            // if (this.selectProductionData.strName != "全部") {
            //     param = Object.assign(param, {
            //         Production: this.selectProductionData.Id
            //     });
            // } else {
            //     delete param["Production"];
            // }

            // 订单状态
            if (this.selectOrderStatusData.strName != "全部") {
                param = Object.assign(param, {
                    OrderStatus: this.selectOrderStatusData.Id
                });
            } else {
                delete param["OrderStatus"];
            }

            //是否新会员
            if(this.selectbNewData.strName != '否') {
                param = Object.assign(param, {
                    bNew: this.selectbNewData.Id
                });
            } else {
                delete param['bNew']
            }

            if(this.searchValue != '') {
                param = Object.assign(param , {
                    'DeliverName': this.searchValue
                })
            } else {
                delete param['DeliverName']
            }


            // 配送类型
            if (this.selectRemindTypeData.strName != "全部") {
                param = Object.assign(param, {
                    Remindtype: this.selectRemindTypeData.Id
                });
            } else {
                delete param["Remindtype"];
            }

            if (onrefreshState) {
            } else {
                this.$notice.loading.show("正在加载");
            }
            var RES;
            console.log(JSON.stringify(param));

            // if (this.userInfo.RoleId == 8 || this.userInfo.RoleId == 13) {
                RES = await API.get_DayRise_YSH(param);
            // } else {
            //     RES = await API.get_OrderSumManager_YSH(param);
            // }
            this.listData = [];
            var DGDATA = RES.DATA;
            // 现将统计全部归零
            this.intUCountTotal = 0;
            this.intOCountTotal = 0;
            this.intSumPriceTotal = 0;
            this.intPerPriceTotal = 0;
            this.intClosePriceTotal = 0;
            this.intDisPriceTotal = 0;
            if (DGDATA.length != 0) {
                var newData = [];
                DGDATA.map(item => {
                    this.intUCountTotal = accAdd(
                        this.intUCountTotal,
                        item.intSumPrice
                    );
                    // this.intOCountTotal = accAdd(
                    //     this.intOCountTotal,
                    //     item.intOCount
                    // );
                    // this.intSumPriceTotal = accAdd(
                    //     this.intSumPriceTotal,
                    //     item.intSumPrice
                    // );
                    // this.intPerPriceTotal = accAdd(
                    //     this.intPerPriceTotal,
                    //     item.intPerPrice
                    // );
                    // this.intClosePriceTotal = accAdd(
                    //     this.intClosePriceTotal,
                    //     item.intClosePrice
                    // );
                    // this.intDisPriceTotal = accAdd(
                    //     this.intDisPriceTotal,
                    //     item.intDisPrice
                    // );
                    // this.intOnlinePaymentDiscountsTotal = accAdd(
                    //     this.intOnlinePaymentDiscountsTotal,
                    //     item.OnlinePaymentDiscounts
                    // )
                });
                // this.intSumPriceTotal = this.intSumPriceTotal.toFixed(2);
                // this.intClosePriceTotal = this.intClosePriceTotal.toFixed(2);
                // this.intOnlinePaymentDiscountsTotal = this.intOnlinePaymentDiscountsTotal.toFixed(2);
                // this.intPerPriceTotal = (
                //     this.intPerPriceTotal / DGDATA.length
                // ).toFixed(2);
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
        async toDetail(item) {
            this.$notice.loading.show("正在加载");
            let param = {
                key: "RyAkcFaz6KCKdW06EQKFHNEISQDDA",
                strSysMac: "30-5A-3A-E4-4F-14",
                strSysCode: "YJAPP",
                strAuthor: "YJ",
                dStartDate: item.orderDate
                    ? item.orderDate
                    : this.param.dStartDate,
                dEndDate: item.orderDate ? item.orderDate : this.param.dEndDate
            };
            // let deliverId = "";
            // this.deliverList.map(u => {
            //     if (u.RealName == item.DeliverName) {
            //         deliverId = u.Id;
            //     }
            // });

            // param = Object.assign(param, {
            //     SalesmanId: deliverId
            // });
            // 选择业务员
            param = Object.assign(param , {
                'DeliverName': item.DeliverName
            })

            // 选择产品类别
            if (this.selectProductData.strName != "全部") {
                param = Object.assign(param, {
                    qCode: this.selectProductData.Id
                });
            } else {
                delete param["qCode"];
            }

            // 选择审核
            if (this.selectProductionData.strName != "全部") {
                param = Object.assign(param, {
                    Production: this.selectProductionData.Id
                });
            } else {
                delete param["Production"];
            }

            // 订单状态
            if (this.selectOrderStatusData.strName != "全部") {
                param = Object.assign(param, {
                    OrderStatus: this.selectOrderStatusData.Id
                });
            } else {
                delete param["OrderStatus"];
            }

            // 配送类型
            if (this.selectRemindTypeData.strName != "全部") {
                param = Object.assign(param, {
                    Remindtype: this.selectRemindTypeData.Id
                });
            } else {
                delete param["Remindtype"];
            }

            //是否新会员
            if(this.selectbNewData.strName != '否') {
                param = Object.assign(param, {
                    bNews: this.selectbNewData.Id
                });
            } else {
                delete param['bNews']
            }

            let { DATA } = await API.get_OrderManagerDetail_YSH(param);

            this.$notice.loading.hide();
            if (this.isDeliver) {
                this.$router.open({
                    name: "performanceGrowthDetail",
                    type: "PUSH",
                    params: {
                        total: item,
                        listItem: DATA
                    }
                });
            } else {
                this.$router.open({
                    name: "performanceGrowthAdminDetail",
                    type: "PUSH",
                    params: {
                        total: item,
                        listItem: DATA
                    }
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
                intMonth: API.get_date("month"),
                intYear: API.get_date("year")
            };
            // this.$refs["datePick"].searchDate["@beginDate"] =
            //     dateFormate.startDate;
            // this.$refs["datePick"].searchDate["@endDate"] = dateFormate.endDate;
            this.param = Object.assign(this.param, {
                intMonth: dateFormate.intMonth,
                intYear: dateFormate.intYear
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
        selectYear() {
            var items = this.yearList;
            var index = this.yearList.indexOf(Number(this.param.intYear))
            picker.pick(
                {
                    index: index,
                    items
                },
                event => {
                    if (event.result === "success") {
                        this.param.intYear = this.yearList[event.data]
                        this.getData();
                    }
                }
            );
        },
        selectMonth() {
            var items = this.monthList;
            var index = this.monthList.indexOf(Number(this.param.intMonth))
            picker.pick(
                {
                    index: index,
                    items
                },
                event => {
                    if (event.result === "success") {
                        this.param.intMonth = this.monthList[event.data]
                        this.getData();
                    }
                }
            );
        },
        selectProduct() {
            this.productsCheckList = [];
            this.productsCheckList = this.productsList.map(item => {
                return {
                    title: item.strName,
                    value: item.Id,
                    checked: false
                };
            });
            this.$refs["payType"].show();
        },

        wxcCheckBoxListChecked(e) {
            console.log(e);

            if (e.checkedList.length) {
                let strName = "";
                let Id = "";
                this.productsList.map(item => {
                    e.checkedList.map((i, index) => {
                        if (i === item.Id) {
                            strName += item.strName + ",";
                            Id += item.Id + ",";
                        }
                    });
                });
                this.selectProductData = {
                    strName: strName.substring(0, strName.lastIndexOf(",")),
                    Id: Id.substring(0, Id.lastIndexOf(","))
                };
            } else {
                this.selectProductData = {
                    strName: "全部",
                    Id: ""
                };
            }
        },
        wxcButtonClicked() {
            this.$refs["payType"].hide();
            this.getData();
        },
        selectProduction() {
            var items = [];
            if (items.length === 0) {
                this.productionList.map(item => {
                    items.push(item.strName);
                });
            }

            picker.pick(
                {
                    index: this.index3,
                    items
                },
                event => {
                    if (event.result === "success") {
                        this.selectProductionData = this.productionList[
                            event.data
                        ];
                        this.index3 = event.data;
                        this.getData();
                    }
                }
            );
        },
        selectOrderStatus() {
            var items = [];
            if (items.length === 0) {
                this.orderStatusList.map(item => {
                    items.push(item.strName);
                });
            }

            picker.pick(
                {
                    index: this.index4,
                    items
                },
                event => {
                    if (event.result === "success") {
                        this.selectOrderStatusData = this.orderStatusList[
                            event.data
                        ];
                        this.index4 = event.data;
                        this.getData();
                    }
                }
            );
        },
        selectRemindType() {
            var items = [];
            if (items.length === 0) {
                this.remindTypeList.map(item => {
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
                        this.selectRemindTypeData = this.remindTypeList[
                            event.data
                        ];
                        this.index2 = event.data;
                        this.getData();
                    }
                }
            );
        },
        selectbNew() {
            var items = [];
            if (items.length === 0) {
                this.bNewList.map(item => {
                    items.push(item.strName);
                });
            }

            picker.pick(
                {
                    index: this.index5,
                    items
                },
                event => {
                    if (event.result === "success") {
                        this.selectbNewData = this.bNewList[
                            event.data
                        ];
                        this.index5 = event.data;
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
        inputChange(val) {
            this.searchValue = val;
        },
        searchClick() {
            this.getData()
        }
    }
};
</script>

<style scoped>
.page {
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
.width-400px {
    width: 400px;
}
.width-350px {
    width: 350px;
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
.pay-content {
    margin-left: 84px;
    margin-top: 312px;
    width: 580px;
    height: 600px;
    background-color: #fff;
    border-radius: 4px;
}
.pay-title {
    height: 105px;

    justify-content: center;
    padding-left: 120px;
    border-bottom-width: 1px;
    border-bottom-style: dotted;
    border-bottom-color: #cccccc;
}
.pay-group {
    flex-direction: row;
    /* width: 350px; */
    /* padding-top: 52px; */
    justify-content: center;
}
.scroller {
    width: 550px;
    height: 700px;
}
</style>
