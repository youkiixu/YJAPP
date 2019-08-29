<template>
    <list ref="scrollerList" >
        <cell>
            <scroller>
                <wxc-cell
                    label="订单号"
                    :title="listItem.Id"
                    @wxcCellClicked="getParams">
                </wxc-cell>
                <wxc-cell
                    label="会员号"
                    :title="listItem.UserId "
                    @wxcCellClicked="getParams">
                </wxc-cell>
                <wxc-cell
                    label="下单时间"
                    :title="listItem.OrderDate "
                    @wxcCellClicked="getParams">
                </wxc-cell>
                <wxc-cell
                    label="订单金额"
                    :title="listItem.ProductTotalAmount "
                    @wxcCellClicked="getParams">
                </wxc-cell>
                <wxc-cell
                    label="发货方式"
                    :title="listItem.strRemindtype  "
                    @wxcCellClicked="getParams">
                </wxc-cell>
                <wxc-cell
                    label="业务员"
                    :title="listItem.DeliverName "
                    @wxcCellClicked="getParams">
                </wxc-cell>
                <wxc-cell label="品类"
                        :title="listItem.strQName"
                        @wxcCellClicked="getParams">
                </wxc-cell>
                <wxc-cell
                    label="省"
                    :title="listItem.RegionFullName "
                    @wxcCellClicked="getParams">
                </wxc-cell>
            </scroller>
        </cell>
    </list>
</template>
<script>
import { WxcCell , WxcSimpleFlow , WxcButton } from 'weex-ui'
import API from 'Utils/api'
export default {
    components: { WxcCell, WxcSimpleFlow , WxcButton },
    data () {
        return {
            tapBackTime: 0,
            navShow: true,
            statusBarStyle: 'LightContent',
            listItem: {},
            testData: []
        }
    },
    eros: {
        beforeAppear(params, options ) {
            this.listItem = params
            // this.onrefresh()
        }
    },
    methods: {
        call(number) {
            this.$coms.call(number)
        },
        async onrefresh(type) {
            var par = {
                "orderId" : this.listItem.Id,
                'page_no' : 1,
                'page_size' : 1
            }
            var RES = await API.KIY_SEARCHORDER(par)
            if(RES.Success) {
                var RESDATA = JSON.parse(RES.Data)
                var DGDATA = RESDATA.data.Models
                this.listItem = DGDATA[0]
                this.getChildOrder()
            }
            if(type) {
                this.$refs["scrollerList"].refreshEnd()
            }
        },
        async getChildOrder () {
            const Status = await API.YJ_ORDERLISTSTATUS({'@cytId': this.listItem.Id})
            this.testData = []
            // 解决map的问题
            if (Status.map) {
                var StatusArr = Status.map.dgData
            } else {
                var StatusArr = Status.dgData
            }

            StatusArr.map((item , index) => {
                var data = {
                    'date': item.CreateDate,
                    'desc': item.Remark,
                    'title':  '操作人: ' + item.RealName
                }
                if(index === 0) {
                    data = Object.assign(data , {'highlight' : true})
                }
                this.testData.push(data)
            })
        },
        async wxcButtonClicked () {
            this.$notice.loading.show()
            const status = await API.get_IsOrderRefunds({'orderId': this.listItem.Id})
            this.$notice.loading.hide()
            if(status.Success) {
                this.$router.open({
                	name: 'orderRefund',
                	type: 'PUSH',
                	params: this.listItem
                })
            } else {
                this.$notice.toast({
                    message: status.Message
                });
            }
            console.log(status)

        }
    },
    mounted() {

    },
    created () {

    }

}
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
    border-top-color: #E5E5E5;
    padding-top: 20px;
    margin-top: 20px;
    margin-bottom: 20px;
    flex-direction: row;
    align-items: center;
    justify-content: center;
}
</style>

