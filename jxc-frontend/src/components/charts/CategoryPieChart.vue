<template>
  <div class="category-pie-chart">
    <VChart :option="chartOption" :style="{ height: height, width: '100%' }" autoresize />
  </div>
</template>

<script setup lang="ts">
  import { computed, type PropType } from 'vue'

  // 接口类型定义
  interface CategoryData {
    name: string
    value: number
    percentage?: number
  }

  // 属性定义
  const props = defineProps({
    data: {
      type: Array as PropType<CategoryData[]>,
      required: true
    },
    height: {
      type: String,
      default: '400px'
    },
    title: {
      type: String,
      default: '商品分类销售分布'
    }
  })

  // 颜色配置
  const colors = [
    '#1890ff',
    '#52c41a',
    '#faad14',
    '#f5222d',
    '#722ed1',
    '#13c2c2',
    '#eb2f96',
    '#fa8c16',
    '#a0d911',
    '#2f54eb'
  ]

  // 图表配置
  const chartOption = computed(() => ({
    title: {
      text: props.title,
      left: 'center',
      textStyle: {
        fontSize: 16,
        fontWeight: 'bold'
      }
    },
    tooltip: {
      trigger: 'item',
      formatter: (params: any) => {
        return `
        <div style="margin-bottom: 8px;">
          ${params.marker} ${params.name}
        </div>
        <div>
          销售额: <span style="font-weight: bold; color: ${params.color};">¥${params.value.toLocaleString()}</span>
        </div>
        <div>
          占比: <span style="font-weight: bold;">${params.percent}%</span>
        </div>
      `
      }
    },
    legend: {
      type: 'scroll',
      orient: 'vertical',
      right: 10,
      top: 20,
      bottom: 20,
      data: props.data.map(item => item.name)
    },
    series: [
      {
        name: '销售额',
        type: 'pie',
        radius: ['40%', '70%'],
        center: ['40%', '50%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 5,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 20,
            fontWeight: 'bold',
            formatter: (params: any) => {
              return `${params.name}\n¥${params.value.toLocaleString()}`
            }
          }
        },
        labelLine: {
          show: false
        },
        data: props.data.map((item, index) => ({
          ...item,
          itemStyle: {
            color: colors[index % colors.length]
          }
        }))
      }
    ]
  }))
</script>

<style scoped>
  .category-pie-chart {
    width: 100%;
    background: #fff;
    border-radius: 8px;
    padding: 16px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  }
</style>
