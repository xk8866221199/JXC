import { createApp } from 'vue'
import './style.css'
import Antd from 'ant-design-vue'
import 'ant-design-vue/dist/reset.css'
import * as Icons from '@ant-design/icons-vue'
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
import router from './router'
import App from './App.vue'

// 引入Vue-ECharts组件
import ECharts from 'vue-echarts'
import { use } from 'echarts/core'
// 手动引入ECharts各模块来减小打包体积
import { CanvasRenderer } from 'echarts/renderers'
import { BarChart, LineChart, PieChart, GaugeChart, RadarChart } from 'echarts/charts'
import {
  GridComponent,
  TooltipComponent,
  LegendComponent,
  TitleComponent,
  ToolboxComponent,
  DataZoomComponent
} from 'echarts/components'

// 注册必要的组件
use([
  CanvasRenderer,
  BarChart,
  LineChart,
  PieChart,
  GaugeChart,
  RadarChart,
  GridComponent,
  TooltipComponent,
  LegendComponent,
  TitleComponent,
  ToolboxComponent,
  DataZoomComponent
])

const app = createApp(App)

// 全局注册Vue-ECharts组件
app.component('VChart', ECharts)

// 注册所有Ant Design图标
Object.keys(Icons).forEach(key => {
  app.component(key, Icons[key as keyof typeof Icons])
})

// 配置Pinia状态管理
const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)

app.use(Antd)
app.use(pinia)
app.use(router)

app.mount('#app')
