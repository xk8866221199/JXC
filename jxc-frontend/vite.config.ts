import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue()
  ],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, 'src')
    }
  },
  // 性能优化配置
  build: {
    // 构建目标
    target: 'es2015',
    // 启用/禁用 CSS 代码拆分
    cssCodeSplit: true,
    // 构建后是否生成 source map 文件
    sourcemap: false,
    // 自定义底层的 Rollup 打包配置
    rollupOptions: {
      output: {
        // 分包策略
        manualChunks: {
          // 将 Vue 相关库打包到 vue-vendor
          'vue-vendor': ['vue', 'vue-router', 'pinia'],
          // 将 Ant Design Vue 打包到 antd-vendor
          'antd-vendor': ['ant-design-vue', '@ant-design/icons-vue'],
          // 将 ECharts 打包到 charts-vendor
          'charts-vendor': ['echarts', 'vue-echarts'],
          // 将工具库打包到 utils-vendor
          'utils-vendor': ['axios', 'dayjs']
        },
        // 资源文件命名
        chunkFileNames: 'js/[name]-[hash].js',
        entryFileNames: 'js/[name]-[hash].js',
        assetFileNames: '[ext]/[name]-[hash].[ext]'
      }
    },
    // 压缩配置
    minify: 'esbuild',
    // 启用/禁用 gzip 压缩大小报告
    reportCompressedSize: false,
    // chunk 大小警告限制（kb）
    chunkSizeWarningLimit: 1000
  },
  // 开发服务器配置
  server: {
    // 启用热更新
    hmr: true,
    // 预热文件以提高启动期间的初始页面加载速度
    warmup: {
      clientFiles: ['./src/main.ts', './src/App.vue']
    }
  },
  // 依赖优化
  optimizeDeps: {
    include: [
      'vue',
      'vue-router',
      'pinia',
      'ant-design-vue',
      '@ant-design/icons-vue',
      'axios',
      'dayjs'
    ]
  }
})
