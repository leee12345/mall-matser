Vue.component('user-frame', {
    template: `
		<min-width-container>
			<div class="user-frame">
				<div class="user-frame-left text-4 text-general-1">
<!--				左边的功能菜单，显示一些个人信息相关的内容-->
					<div class="user-frame-left-info">
<!--						<div class="user-frame-left-info-portraint"><div></div></div>-->
<!--						<div class="user-frame-left-info-aloha center m-t-16 m-b-16 text-general-2" >hi, 亲爱的{{account}}欢迎回来</div>-->
<!--						<div class="user-frame-left-info-level m-l-4 p-l-20">会员等级: <span class="text-important-very m-l-8">菜鸟</span></div>-->
<!--						<div class="user-frame-left-info-score m-l-4 p-l-20 m-t-8">会员积分: <span class="text-important-very m-l-8">0</span></div>-->
					</div>
					<el-menu
					  default-active="1"
					  :default-openeds="['1', '2']"
					  background-color="transparent"
					  class="el-menu-vertical-demo">
					  <el-submenu index="1">
						<template slot="title">
						  <i class="el-icon-goods"></i>
						  <span>我的交易</span>
						</template>
						  <el-menu-item index="1-1" @click="window.location.href='/order.html'">我的订单</el-menu-item>
						  <el-menu-item index="1-2" @click="window.location.href='/cart.html'">我的购物车</el-menu-item>
						  <el-menu-item index="1-3" @click="window.location.href='/address.html'">收货地址管理</el-menu-item>
					  </el-submenu>
					  <el-submenu index="2">
						<template slot="title">
						  <i class="el-icon-user"></i>
						  <span>我的账户</span>
						</template>
						  <el-menu-item index="2-1"@click="window.location.href='/information-modify.html'">个人资料</el-menu-item>
						  <el-menu-item index="2-2"@click="window.location.href='/password-modify.html'">修改密码</el-menu-item>
					  </el-submenu>
					</el-menu>
				</div>
				
				<div class="user-frame-right">
<!--			通过slot显示右边相应的内容，让这个部分可以复用-->
<!--			匿名插槽，通过插槽的方式注入代码-->
					<slot></slot>
				</div>
			</div>
		</min-width-container>
	`,
    data() {
        return {
            account: ''
        }
    },
    created() {
        const user = JSON.parse(localStorage.getItem("user"));
        if (user) {
            this.account = user.account;
        }
    },
})