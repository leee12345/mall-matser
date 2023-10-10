Vue.component('header-simple', {
	template: `
		<div class="header-simple">
			<min-width-container>
				<div class="header-simple-content">
					<img class="pointer" src="/images/logo.png" @click="window.location.href='/'" />
				</div>
			</min-width-container>
		</div>
	`,
})
