Vue.component('cart-frame', {
    template: `
		<min-width-container>
			<div class="cart-frame">
				<div class="cart-frame-in">
<!--			通过slot显示右边相应的内容，让这个部分可以复用-->
<!--			匿名插槽，通过插槽的方式注入代码-->
					<slot></slot>
				</div>
			</div>
		</min-width-container>
	`,
})