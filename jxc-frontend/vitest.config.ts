/// <reference types="vitest" />
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, 'src')
    }
  },
  test: {
    // 测试环境
    environment: 'happy-dom',
    // 全局测试设置
    globals: true,
    // 包含的测试文件
    include: ['src/**/*.{test,spec}.{js,mjs,cjs,ts,mts,cts,jsx,tsx}'],
    // 排除的文件
    exclude: ['node_modules', 'dist', '.idea', '.git', '.cache'],
    // 覆盖率报告
    coverage: {
      provider: 'c8',
      reporter: ['text', 'json', 'html'],
      exclude: [
        'node_modules/',
        'src/test/',
        'src/**/*.d.ts',
        'src/**/*.test.{js,ts,vue}',
        'src/**/*.spec.{js,ts,vue}'
      ]
    },
    // 测试超时
    testTimeout: 10000,
    // 设置文件
    setupFiles: ['src/test/setup.ts']
  }
})